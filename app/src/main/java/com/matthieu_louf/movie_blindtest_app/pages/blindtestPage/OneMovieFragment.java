package com.matthieu_louf.movie_blindtest_app.pages.blindtestPage;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubePlayer;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.movie.Movie;
import com.matthieu_louf.movie_blindtest_app.models.video.Video;
import com.matthieu_louf.movie_blindtest_app.api.GetMovieService;
import com.matthieu_louf.movie_blindtest_app.api.MovieAPIHelper;
import com.matthieu_louf.movie_blindtest_app.api.RetrofitInstance;
import com.matthieu_louf.movie_blindtest_app.pages.detailsPage.MovieDetailsActivity;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.UserLikeService;
import com.google.android.material.button.MaterialButton;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OneMovieFragment extends Fragment {
    private static final String TAG = "OneMovieFragment";
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";
    private static final String MOVIE_ID = "movie_id";
    private static final String AB_TITLE = "ab_title";

    private Integer movie_id;
    private String ab_title;
    private Movie searched_movie;
    private Video video_movie;

    private MaterialButton next_movie;
    private NumberPicker picker;
    private TextView movie_title;
    private CardView movieCardView;
    private ProgressBar progressBar;

    private ImageView isLikedIcon;
    private boolean isLiked;
    private UserLikeService userLikeService;

    private boolean next = false;
    private List<String> listSimilarTitles = new ArrayList<String>();

    private View root;
    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private GetMovieService retrofitService = retrofit.create(GetMovieService.class);
    private MovieAPIHelper movieAPIHelper;

    private boolean hasBeenPaused = false;

    private BlindtestMovieActivity blindtestMovieActivity;

    MutableLiveData<Boolean> listen = new MutableLiveData<>();

    public OneMovieFragment() {
    }

    public static OneMovieFragment newInstance(Integer movie_id, String ab_title) {
        OneMovieFragment fragment = new OneMovieFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movie_id);
        args.putString(AB_TITLE, ab_title);
        fragment.setArguments(args);
        Log.d(TAG, "Instantiate OneMovieFragment");

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie_id = getArguments().getInt(MOVIE_ID);
            ab_title = getArguments().getString(AB_TITLE);
        }

        movieAPIHelper = new MovieAPIHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_one_movie, container, false);
        setHasOptionsMenu(true);
        blindtestMovieActivity = (BlindtestMovieActivity) getActivity();

        ActionBar ab = blindtestMovieActivity.getSupportActionBar();
        ab.setTitle(ab_title);

        movie_title = root.findViewById(R.id.movie_title);
        next_movie = root.findViewById(R.id.next_movie);
        picker = root.findViewById(R.id.movie_picker);
        picker.setVisibility(View.INVISIBLE);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        movieCardView = root.findViewById(R.id.card_movie_view);
        movieCardView.setVisibility(View.INVISIBLE);
        progressBar = root.findViewById(R.id.test_progress_bar);

        next_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!next) {
                    Log.d(TAG, "Ask to show test movie on valid");
                    showResult(true);
                } else {
                    Log.d(TAG, "Ask to change movie on next");
                    changeFragment(false);
                }
            }
        });

        listen.setValue(false);

        listen.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                if(b)
                {
                    progressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    Log.d(TAG, "First third past");
                }
            }
        });

        setIsLikedIcon();

        fetchMovieDetails();

        setYouTubePlayerView();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        requireActivity().getMenuInflater().inflate(R.menu.menu_blindtest_actionbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_blindtest_notify:
                NotifyDialogFragment newFragment = new NotifyDialogFragment(video_movie, searched_movie);
                newFragment.show(requireActivity().getSupportFragmentManager(), "notify");
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchMovieDetails() {
        retrofitService.getMovie(movie_id.toString(), getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.d(TAG, "Retrieve Test Movie infos");
                searched_movie = response.body();
                getBestTrailer(searched_movie.getId().toString());
                setSimilarMovies();
                setMovieViewComponents();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    private void setYouTubePlayerView() {

    }

    private void initVideo() {
        try {
            blindtestMovieActivity.youTubePlayer.loadVideo(video_movie.getKey(), (int) video_movie.getStart_time() * 1000);
            blindtestMovieActivity.youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    blindtestMovieActivity.changeLoadingText(true);
                }

                @Override
                public void onLoaded(String s) {

                }

                @Override
                public void onAdStarted() {
                    blindtestMovieActivity.changeLoadingText(true);
                }

                @Override
                public void onVideoStarted() {
                    dismissLoadingDialog();
                    setProgressBar();
                }

                @Override
                public void onVideoEnded() {
                    if (!next) {
                        showResult(false);
                    }
                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {
                    Log.d(TAG, "Error while loading the video, changing fragment, reason : " + errorReason);
                    if (errorReason != YouTubePlayer.ErrorReason.UNAUTHORIZED_OVERLAY) {

                        changeFragment(true);
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "Exception : " + e);

        }
    }

    private void getBestTrailer(String movie_id) {
        movieAPIHelper.getBestTrailer(getContext(), movie_id, searched_movie.getOriginalLanguage(), new Callback<Video>() {
            @Override
            public void onResponse(Call<Video> call, Response<Video> response) {
                video_movie = response.body();
                if (video_movie != null) {
                    Log.d(TAG, "Got best trailer not null, ask to start the video");
                    initVideo();
                } else {
                    Log.d(TAG, "Null video, ask to restart the fragment");
                    changeFragment(true);
                }
            }

            @Override
            public void onFailure(Call<Video> call, Throwable t) {
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        hasBeenPaused = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasBeenPaused) {
            blindtestMovieActivity.youTubePlayer.play();
        }
    }

    private void setSimilarMovies() {
        movieAPIHelper.getSimilarMovies(getContext(), movie_id, new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                listSimilarTitles = response.body();
                if (listSimilarTitles != null && listSimilarTitles.size() >= 15) {
                    listSimilarTitles = listSimilarTitles.subList(0,14);
                    Log.d(TAG, "Retrieve not null list of similar movies");
                    if (!listSimilarTitles.contains(searched_movie.getTitle())) {
                        listSimilarTitles.add(searched_movie.getTitle());
                    }
                    Collections.sort(listSimilarTitles);
                    picker.setMinValue(0);
                    picker.setMaxValue(listSimilarTitles.size() - 1);
                    picker.setDisplayedValues(listSimilarTitles.toArray(new String[listSimilarTitles.size()]));
                    picker.setValue(listSimilarTitles.size() / 2);
                    if (listSimilarTitles.get(listSimilarTitles.size() / 2).equals(searched_movie.getTitle())) {
                        picker.setValue((listSimilarTitles.size() / 2) - 2);
                    }
                    picker.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "Retrieve null list of similar movies, change fragment");
                    changeFragment(true);
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private float pxFromDp(float dp)
    {
        return dp * this.getContext().getResources().getDisplayMetrics().density;
    }

    private void changeValueByOne(final NumberPicker higherPicker, final boolean increment) {

        Method method;
        try {
            // refelction call for
            // higherPicker.changeValueByOne(true);
            method = higherPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(higherPicker, increment);

        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void dismissLoadingDialog() {
        Log.d(TAG, "Dismiss Dialog");
        blindtestMovieActivity.hideLoading();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                for(int i =1;i<=15;i++)
                {
                    Handler handler2 = new Handler();
                    handler2.postDelayed(new Runnable() {
                        public void run() {
                            changeValueByOne(picker,true);
                        }
                    }, i*50);
                }
            }
        }, 100);
    }

    private void changeFragment(boolean loadingFail) {
        onDestroy();
        next = true;
        if (loadingFail) {
            movieAPIHelper.setBugMovie(searched_movie);
        }
        Log.d(TAG, "Change Fragment method");
        blindtestMovieActivity.getRandomMovie(loadingFail);
    }

    private void showResult(boolean isGuessed) {
        Log.d(TAG, "Show result card !");

        next = true;
        next_movie.setText(R.string.next_movie);
        next_movie.setHighlightColor(getResources().getColor(R.color.colorPrimary));
        next_movie.setTextColor(getResources().getColor(R.color.colorPrimary));
        next_movie.setStrokeColorResource(R.color.colorPrimary);
        movieCardView.setVisibility(View.VISIBLE);
        picker.setVisibility(View.INVISIBLE);

        if (listSimilarTitles.size() != 0) {
            TextView result_sentence = root.findViewById(R.id.result_sentence);
            if (isGuessed) {

                if (listSimilarTitles.get(picker.getValue()).equals(searched_movie.getTitle())) {
                    Integer score = (int) getScore();
                    result_sentence.setText(getString(R.string.good_response, score));
                    result_sentence.setTextColor(getResources().getColor(R.color.colorPrimary));

                    blindtestMovieActivity.newResponse(true, score);
                } else {
                    result_sentence.setText(getString(R.string.not_good_movie, listSimilarTitles.get(picker.getValue()), 0));
                    result_sentence.setTextColor(getResources().getColor(R.color.colorAccent));

                    blindtestMovieActivity.newResponse(false, 0);

                }
            } else {
                result_sentence.setText(getString(R.string.no_response));
            }

        }

        movieCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsActivity.start(getContext(), movie_id.toString());
            }
        });

    }

    private float getTotalDuration() {
        float total_duration = 0;
        try {
            total_duration = blindtestMovieActivity.youTubePlayer.getDurationMillis() / 1000f - video_movie.getStart_time();
        } catch (Exception e) {
            Log.d(TAG, "Time error : " + e.getMessage());
        }
        return total_duration;
    }

    private float getCurrentTime() {
        float current_time = 0;
        try {
            current_time = blindtestMovieActivity.youTubePlayer.getCurrentTimeMillis() / 1000f - video_movie.getStart_time();
        } catch (Exception e) {
            Log.d(TAG, "Time error : " + e.getMessage());
        }

        return current_time;
    }

    private float getScore() {
        float total_duration = getTotalDuration();
        float guessed_time = getCurrentTime();
        float score = 0;
        if (guessed_time < 10) {
            score = 500;
        } else {
            score = (1 - ((guessed_time - 10) / total_duration)) * 500;
        }
        if (score < 0) {
            score = 0;
        }
        return score;
    }

    private void setMovieViewComponents() {
        if (getContext() != null) {
            Log.d(TAG, "Set movie view components");

            ImageView movie_image = root.findViewById(R.id.movie_image);
            TextView movie_rating = root.findViewById(R.id.movie_rating);
            TextView movie_release_date = root.findViewById(R.id.movie_release_date);
            TextView movie_language = root.findViewById(R.id.movie_language);

            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            Date date = null;
            try {
                date = ft.parse(searched_movie.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateText = DateUtils.formatDateTime(getContext(), date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);

            Locale locale = new Locale(searched_movie.getOriginalLanguage());
            Locale local_language = new Locale(getContext().getResources().getString(R.string.language_iso639));

            Glide.with(this).load(BASE_URL_IMAGE + searched_movie.getPosterPath()).into(movie_image);
            movie_title.setText(searched_movie.getTitle());
            movie_rating.setText(getString(R.string.average_rate) + " : " + searched_movie.getVoteAverage().toString() + "/10");
            movie_release_date.setText(getString(R.string.release_date) + " : " + dateText);
            movie_language.setText(getString(R.string.language_is) + " : " + locale.getDisplayLanguage(local_language));
        }
    }

    private void setIsLikedIcon() {
        Log.d(TAG, "Set is liked icon");

        userLikeService = new UserLikeService((AppCompatActivity) getActivity());
        isLikedIcon = root.findViewById(R.id.movie_is_liked_icon);

        isLiked = userLikeService.isLiked(movie_id);

        if (isLiked) {
            setLikedIconFromDrawable(R.drawable.ic_star_black_24dp, getResources().getColor(R.color.colorAccent));
        } else {
            setLikedIconFromDrawable(R.drawable.ic_star_border_black_24dp, Color.GRAY);
        }

        isLikedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLiked) {
                    userLikeService.removeLike(movie_id);
                    setLikedIconFromDrawable(R.drawable.ic_star_border_black_24dp, Color.GRAY);
                } else {
                    userLikeService.addLike(movie_id);
                    setLikedIconFromDrawable(R.drawable.ic_star_black_24dp, getResources().getColor(R.color.colorAccent));
                }
                isLiked = !isLiked;
            }
        });
    }

    private void setLikedIconFromDrawable(int drawable, int color) {
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(getContext(), drawable);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        isLikedIcon.setImageDrawable(wrappedDrawable);
    }

    private void setProgressBar() {
        Log.d(TAG, "Set ProgressBar Loader Thread " + getTotalDuration());

        int progressBarMax = 10000;
        progressBar.setMax(progressBarMax);
        progressBar.setProgress(progressBarMax);

        new Thread(new Runnable() {
            public void run() {

                while (getTotalDuration() - getCurrentTime() >= 0f && !next) {

                    progressBar.setProgress(progressBarMax - (int) ((getCurrentTime() * progressBarMax) / getTotalDuration()));

                    if (!listen.getValue() && (getTotalDuration() / 2) - getCurrentTime() <= 0f) {
                        listen.postValue(true);
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroyView() {
        next = true;
        super.onDestroyView();
    }

}
