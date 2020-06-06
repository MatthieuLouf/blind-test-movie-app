package com.matthieu_louf.movie_blindtest_app.pages.games.goodOrBadGame;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.api.GetMovieService;
import com.matthieu_louf.movie_blindtest_app.api.MovieAPIHelper;
import com.matthieu_louf.movie_blindtest_app.api.RetrofitInstance;
import com.matthieu_louf.movie_blindtest_app.firebase.FirebaseLog;
import com.matthieu_louf.movie_blindtest_app.models.movie.Movie;
import com.matthieu_louf.movie_blindtest_app.models.video.Video;
import com.matthieu_louf.movie_blindtest_app.pages.detailsPage.MovieDetailsActivity;
import com.matthieu_louf.movie_blindtest_app.pages.games.MovieGameContainerActivity;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.UserLikeService;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GoodOrBadGameFragment extends Fragment {
    private static final String TAG = "OneMovieFragment";
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w185/";
    private static final String MOVIE_ID = "movie_id";
    private static final String BLINDTEST_STEP_NUMBER = "blindtest_step_number";
    private static final String AB_TITLE = "ab_title";

    private Integer movie_id;
    private Integer blindtest_step_number;
    private String ab_title;
    private Movie searched_movie;
    private Video video_movie;

    private MaterialButton choice_bad_button;
    private MaterialButton choice_good_button;
    private TextView gobg_text_choose;

    private MaterialButton next_movie;
    private TextView movie_title;
    private CardView movieCardView;
    private ProgressBar progressBar;
    private TextView result_sentence;

    private ImageView isLikedIcon;
    private boolean isLiked;
    private UserLikeService userLikeService;

    private boolean next = false;

    private View root;
    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private GetMovieService retrofitService = retrofit.create(GetMovieService.class);
    private MovieAPIHelper movieAPIHelper;

    private boolean hasBeenPaused = false;

    public MovieGameContainerActivity movieGameContainerActivity;
    private FirebaseLog firebaseLog;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    private MutableLiveData<Boolean> listen = new MutableLiveData<>();

    private List<String> video_id_error_list = new ArrayList<String>();

    public GoodOrBadGameFragment() {
    }

    public static GoodOrBadGameFragment newInstance(Integer movie_id, String ab_title, Integer blindtest_step_number) {
        GoodOrBadGameFragment fragment = new GoodOrBadGameFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_ID, movie_id);
        args.putString(AB_TITLE, ab_title);
        args.putInt(BLINDTEST_STEP_NUMBER, blindtest_step_number);
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
            blindtest_step_number = getArguments().getInt(BLINDTEST_STEP_NUMBER);
        }
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseLog = new FirebaseLog(getContext());
        movieAPIHelper = new MovieAPIHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_good_or_bad_game, container, false);
        setHasOptionsMenu(true);
        movieGameContainerActivity = (MovieGameContainerActivity) getActivity();

        ActionBar ab = movieGameContainerActivity.getSupportActionBar();
        ab.setTitle(ab_title);

        movie_title = root.findViewById(R.id.movie_title);
        choice_good_button = root.findViewById(R.id.gobg_choice_good_button);
        choice_bad_button = root.findViewById(R.id.gobg_choice_bad_button);
        gobg_text_choose = root.findViewById(R.id.gobg_text_choose);
        next_movie = root.findViewById(R.id.next_movie);
        movieCardView = root.findViewById(R.id.card_movie_view);
        movieCardView.setVisibility(View.INVISIBLE);
        progressBar = root.findViewById(R.id.test_progress_bar);
        result_sentence = root.findViewById(R.id.result_sentence);

        next_movie.setVisibility(View.INVISIBLE);
        choice_good_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ask to show test movie on valid");
                showResult(true, true);
            }
        });

        choice_bad_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ask to show test movie on valid");
                showResult(true, false);
            }
        });

        listen.setValue(false);

        listen.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean b) {
                if (b) {
                    progressBar.getProgressDrawable().setColorFilter(
                            getResources().getColor(R.color.colorAccent), android.graphics.PorterDuff.Mode.SRC_IN);
                    Log.d(TAG, "First third past");
                }
            }
        });

        setIsLikedIcon();

        fetchMovieDetails();

        return root;
    }

    private void fetchMovieDetails() {
        retrofitService.getMovie(movie_id.toString(), getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.d(TAG, "Retrieve Test Movie infos");
                searched_movie = response.body();
                setVoteAverageOnButtons();
                getBestTrailer(searched_movie.getId().toString());
                setMovieViewComponents();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }

    public void setVoteAverageOnButtons()
    {
        if(searched_movie.getVoteAverage()<=5)
        {
            choice_bad_button.setText(String.valueOf(Math.round(searched_movie.getVoteAverage()))+"/10");
            choice_good_button.setText(String.valueOf(Math.round(searched_movie.getVoteAverage()+3.0))+"/10");
        }
        else {
            choice_good_button.setText(String.valueOf(Math.round(searched_movie.getVoteAverage()))+"/10");
            choice_bad_button.setText(String.valueOf(Math.round(searched_movie.getVoteAverage()-3.0))+"/10");
        }
    }


    private void initVideo() {
        try {
            int pause_time = 0;
            if (blindtest_step_number % 3 == 0) {
                pause_time = (int) mFirebaseRemoteConfig.getLong("ads_time_length") * 1000;
                movieGameContainerActivity.youTubePlayer.cueVideo(video_movie.getKey(), ((int) video_movie.getStart_time() * 1000) + 1);
            } else {
                movieGameContainerActivity.youTubePlayer.loadVideo(video_movie.getKey(), ((int) video_movie.getStart_time() * 1000) + 1);
            }
            Handler handler = new Handler();
            int finalPause_time = pause_time;
            handler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        movieGameContainerActivity.youTubePlayer.play();
                    } catch (Exception e) {
                        Log.d(TAG, e.getMessage());
                    }
                    if (finalPause_time > 0) {
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            public void run() {
                                dismissLoadingDialog();
                                setProgressBar();
                            }
                        }, 300);
                    }
                }
            }, pause_time);
            movieGameContainerActivity.youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                @Override
                public void onLoading() {
                    movieGameContainerActivity.changeLoadingText(true);
                }

                @Override
                public void onLoaded(String s) {
                }

                @Override
                public void onAdStarted() {
                    movieGameContainerActivity.changeLoadingText(true);
                }

                @Override
                public void onVideoStarted() {
                    if (finalPause_time == 0) {
                        movieGameContainerActivity.youTubePlayer.play();
                        dismissLoadingDialog();
                        setProgressBar();
                    }
                    firebaseLog.newMovieSeenEvent(searched_movie);
                }

                @Override
                public void onVideoEnded() {
                    showResult(false, null);

                }

                @Override
                public void onError(YouTubePlayer.ErrorReason errorReason) {
                    Log.d(TAG, "Error while loading the video, changing video, reason : " + errorReason);
                    if (errorReason != YouTubePlayer.ErrorReason.UNKNOWN && errorReason != YouTubePlayer.ErrorReason.UNAUTHORIZED_OVERLAY) {
                        video_id_error_list.add(video_movie.getKey());
                        getBestTrailer(searched_movie.getId().toString());
                    }
                }
            });
        } catch (Exception e) {
            Log.d(TAG, "Exception : " + e);

        }
    }

    private void getBestTrailer(String movie_id) {
        movieAPIHelper.getBestTrailer(getContext(), movie_id, searched_movie.getOriginalLanguage(), video_id_error_list, new Callback<Video>() {
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
            movieGameContainerActivity.youTubePlayer.play();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private float pxFromDp(float dp) {
        return dp * this.getContext().getResources().getDisplayMetrics().density;
    }

    private void changeValueByOne(final NumberPicker higherPicker, final boolean increment) {

        Method method;
        try {
            method = higherPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
            method.setAccessible(true);
            method.invoke(higherPicker, increment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void dismissLoadingDialog() {
        Log.d(TAG, "Dismiss Dialog");
        movieGameContainerActivity.hideLoading();
    }

    private void changeFragment(boolean loadingFail) {
        onDestroy();
        next = true;
        if (loadingFail) {
            movieAPIHelper.setBugMovie(searched_movie);
        }
        Log.d(TAG, "Change Fragment method");
        movieGameContainerActivity.getRandomMovie(loadingFail);
    }

    private void showResult(Boolean isGuessed, Boolean isChoiceGood) {
        Log.d(TAG, "Show result card !");

        next_movie.setText(R.string.next_movie);
        next_movie.setVisibility(View.VISIBLE);
        choice_good_button.setVisibility(View.INVISIBLE);
        choice_bad_button.setVisibility(View.INVISIBLE);
        gobg_text_choose.setVisibility(View.INVISIBLE);
        movieCardView.setVisibility(View.VISIBLE);
        next=true;

        if (isGuessed) {

            if ((searched_movie.getVoteAverage() <= 5 && !isChoiceGood) || (searched_movie.getVoteAverage() >= 7 && isChoiceGood)) {
                Integer score = (int) getScore();
                result_sentence.setText(getString(R.string.good_response, score));
                result_sentence.setTextColor(getResources().getColor(R.color.colorPrimary));

                movieGameContainerActivity.newResponse(true, score);
            } else {
                result_sentence.setText(getString(R.string.not_right_rate,  0));
                result_sentence.setTextColor(getResources().getColor(R.color.colorAccent));

                movieGameContainerActivity.newResponse(false, 0);

            }
        } else {
            result_sentence.setText(getString(R.string.no_response));
        }

        movieCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsActivity.start(getContext(), movie_id.toString());
            }
        });

        next_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Ask to change movie on next");
                changeFragment(false);
            }
        });

    }

    private float getTotalDuration() {
        float total_duration = 0;
        try {
            total_duration = movieGameContainerActivity.youTubePlayer.getDurationMillis() / 1000f - video_movie.getStart_time();
        } catch (Exception e) {
            Log.d(TAG, "Time error : " + e.getMessage());
        }
        return total_duration;
    }

    private float getCurrentTime() {
        float current_time = 0;
        try {
            current_time = movieGameContainerActivity.youTubePlayer.getCurrentTimeMillis() / 1000f - video_movie.getStart_time();
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
            score = mFirebaseRemoteConfig.getLong("score_maximum_value");
        } else {
            score = (1 - ((guessed_time - 10) / total_duration)) * mFirebaseRemoteConfig.getLong("score_maximum_value");
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
