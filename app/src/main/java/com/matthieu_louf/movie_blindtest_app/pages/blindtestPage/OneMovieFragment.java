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

import com.bumptech.glide.Glide;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.movie.Movie;
import com.matthieu_louf.movie_blindtest_app.models.video.Video;
import com.matthieu_louf.movie_blindtest_app.api.GetMovieService;
import com.matthieu_louf.movie_blindtest_app.api.MovieAPIHelper;
import com.matthieu_louf.movie_blindtest_app.api.RetrofitInstance;
import com.matthieu_louf.movie_blindtest_app.pages.detailsPage.MovieDetailsActivity;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.UserLikeService;
import com.google.android.material.button.MaterialButton;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

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

public class OneMovieFragment extends Fragment {
    private static final String TAG = "OneMovieFragment";
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";
    private static final String MOVIE_ID = "movie_id";
    private static final String AB_TITLE = "ab_title";

    private Integer movie_id;
    private String ab_title;
    private Movie searched_movie;
    private Video video_movie;

    private Button hider_top;
    private MaterialButton next_movie;
    private NumberPicker picker;
    private TextView movie_title;
    private CardView movieCardView;
    private ProgressBar progressBar;

    private YouTubePlayerView youTubePlayerView;
    private YouTubePlayerTracker youTubePlayerTracker;
    private YouTubePlayer youTubePlayer;

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

        BlindtestMovieActivity activity = (BlindtestMovieActivity) getActivity();
        ActionBar ab = activity.getSupportActionBar();
        ab.setTitle(ab_title);

        movie_title = root.findViewById(R.id.movie_title);
        hider_top = root.findViewById(R.id.hider_top);
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

        youTubePlayerView = root.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        setIsLikedIcon();

        fetchMovieDetails();

        setYouTubePlayerView();

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.menu_blindtest_actionbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_blindtest_notify:
                NotifyDialogFragment newFragment = new NotifyDialogFragment(video_movie, searched_movie);
                newFragment.show(getActivity().getSupportFragmentManager(), "notify");
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
        PlayerUiController playerUiController = youTubePlayerView.getPlayerUiController();
        playerUiController.showVideoTitle(false);
        playerUiController.showCurrentTime(false);
        playerUiController.showDuration(false);
        playerUiController.showMenuButton(false);
        playerUiController.showFullscreenButton(false);
        playerUiController.showYouTubeButton(false);
        playerUiController.showBufferingProgress(false);
        playerUiController.showPlayPauseButton(false);
        playerUiController.showCustomAction1(false);
        playerUiController.showCustomAction2(false);
        playerUiController.showSeekBar(false);

    }

    private void initVideo() {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayerOnReady) {
                youTubePlayer = youTubePlayerOnReady;
                Log.d(TAG, "Start video loading");
                youTubePlayer.loadVideo(video_movie.getKey(), video_movie.getStart_time());
                youTubePlayerTracker = new YouTubePlayerTracker();
                youTubePlayer.addListener(youTubePlayerTracker);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                Log.d(TAG, "Video change of state : " + state.toString());
                if (state == PlayerConstants.PlayerState.PLAYING) {
                    Log.d(TAG, "Start of the video, set timer to hide the bar on title");
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            hider_top.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);

                    dismissLoadingDialog();
                    setProgressBar();
                    setHasOptionsMenu(true);
                }
                if (state == PlayerConstants.PlayerState.BUFFERING) {
                    BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();
                    if (blindtestMovieActivity != null) {
                        blindtestMovieActivity.changeLoadingText(true);
                    }
                }
                if (state == PlayerConstants.PlayerState.PAUSED) {
                    hider_top.setVisibility(View.VISIBLE);
                }
                if (state == PlayerConstants.PlayerState.ENDED) {
                    showResult(false);
                }
            }

            @Override
            public void onVideoId(@NonNull YouTubePlayer youTubePlayer, String videoId) {

            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                Log.d(TAG, "Error while loading the video, changing fragment");
                changeFragment(true);
            }
        });
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
            youTubePlayer.play();
        }
    }

    private void setSimilarMovies() {
        movieAPIHelper.getSimilarMovies(getContext(), movie_id, new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                listSimilarTitles = response.body();
                if (listSimilarTitles != null && listSimilarTitles.size() > 5) {
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
        youTubePlayerView.release();
    }

    private void dismissLoadingDialog() {
        Log.d(TAG, "Dismiss Dialog");
        BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();
        blindtestMovieActivity.hideLoading();
    }

    private void changeFragment(boolean loadingFail) {
        onDestroy();
        if (loadingFail) {
            movieAPIHelper.setBugMovie(searched_movie);
        }
        Log.d(TAG, "Change Fragment method");
        BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();
        if (blindtestMovieActivity != null) {
            blindtestMovieActivity.getRandomMovie(loadingFail);
        }
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
                BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();

                if (listSimilarTitles.get(picker.getValue()).equals(searched_movie.getTitle())) {
                    Integer score = (int)getScore();
                    result_sentence.setText(getString(R.string.good_response,score));
                    result_sentence.setTextColor(getResources().getColor(R.color.colorPrimary));

                    if (blindtestMovieActivity != null) {
                        blindtestMovieActivity.newResponse(true,score);
                    }
                } else {
                    result_sentence.setText(getString(R.string.not_good_movie,listSimilarTitles.get(picker.getValue()),0) );
                    result_sentence.setTextColor(getResources().getColor(R.color.colorAccent));

                    if (blindtestMovieActivity != null) {
                        blindtestMovieActivity.newResponse(false,0);
                    }
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

    private float getTotalDuration()
    {
        return youTubePlayerTracker.getVideoDuration()-video_movie.getStart_time();
    }

    private float getCurrentTime()
    {
        return youTubePlayerTracker.getCurrentSecond() -video_movie.getStart_time();
    }

    private float getScore()
    {
        float total_duration = youTubePlayerTracker.getVideoDuration()-video_movie.getStart_time();
        float guessed_time = youTubePlayerTracker.getCurrentSecond() -video_movie.getStart_time();
        float score = 0;
        if(guessed_time<10)
        {
            score = 500;
        }
        else{
            score = (1-((guessed_time-10)/total_duration))*500;
        }
        if(score<0)
        {
            score =0;
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

            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
            Date date = null;
            try {
                date = ft.parse(searched_movie.getReleaseDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String dateText  = DateUtils.formatDateTime(getContext(), date.getTime(), DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NUMERIC_DATE | DateUtils.FORMAT_SHOW_YEAR);

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
        Log.d(TAG, "Set ProgressBar Loader Thread " + youTubePlayerTracker.getVideoDuration());

        int progressBarMax = 10000;
        progressBar.setMax(progressBarMax);
        progressBar.setProgress(progressBarMax);

        new Thread(new Runnable() {
            public void run() {

                while (getTotalDuration()- getCurrentTime() >= 0f) {

                    progressBar.setProgress(progressBarMax - (int) ((getCurrentTime() * progressBarMax) / getTotalDuration()));

                    if ((getTotalDuration() / 3) - getCurrentTime() <= 0f) {
                        //Log.d(TAG, "Set ProgressBar Error");
                        //ProgressBar progressBar1 = root.findViewById(R.id.test_progress_bar);
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
        super.onDestroyView();
    }

}
