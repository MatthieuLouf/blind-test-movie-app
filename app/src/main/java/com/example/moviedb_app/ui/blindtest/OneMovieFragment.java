package com.example.moviedb_app.ui.blindtest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.model.Video;
import com.example.moviedb_app.model.VideoPageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.ui.detail_movie_activity.MovieDetailsActivity;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OneMovieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneMovieFragment extends Fragment {
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";
    private static final String ARG_PARAM1 = "movie_id";

    private Integer movie_id;
    private Movie searched_movie;

    private Button hider_top;
    private Button next_movie;
    private NumberPicker picker;
    private TextView movie_title;
    private YouTubePlayerView youTubePlayerView;
    private CardView movieCardView;

    private boolean next = false;
    private List<String> listSimilarTitles = new ArrayList<String>();

    private View root;
    private Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    private GetMovieService retrofitService = retrofit.create(GetMovieService.class);

    public OneMovieFragment() {
    }

    public static OneMovieFragment newInstance(Integer movie_id) {
        OneMovieFragment fragment = new OneMovieFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, movie_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movie_id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_one_movie, container, false);

        movie_title = root.findViewById(R.id.movie_title);
        hider_top = root.findViewById(R.id.hider_top);
        next_movie = root.findViewById(R.id.next_movie);
        picker = root.findViewById(R.id.movie_picker);
        picker.setVisibility(View.INVISIBLE);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        movieCardView = root.findViewById(R.id.card_movie_view);
        movieCardView.setVisibility(View.INVISIBLE);

        next_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!next) {
                    showResult();
                } else {
                    changeFragment();
                }
            }
        });

        youTubePlayerView = root.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        fetchMovieDetails();

        setYouTubePlayerView();


        return root;
    }

    private void fetchMovieDetails() {
        retrofitService.getMovie(movie_id.toString(), getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
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

    private void initVideo(String video_id) {
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(video_id, 0f);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                if (state == PlayerConstants.PlayerState.PLAYING) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            hider_top.setVisibility(View.INVISIBLE);
                        }
                    }, 5000);
                }
                if (state == PlayerConstants.PlayerState.PAUSED) {
                    hider_top.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onVideoId(@NonNull YouTubePlayer youTubePlayer, String videoId) {

            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerError error) {
                changeFragment();
            }
        });
    }

    public void getBestTrailer(String movie_id) {
        retrofitService.getVideos(movie_id, getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<VideoPageResult>() {
            @Override
            public void onResponse(Call<VideoPageResult> call, Response<VideoPageResult> response) {
                if (response.body() != null) {
                    List<Video> videoList = response.body().getResults();
                    Video video = selectBestTrailer(videoList);
                    if (video != null) {
                        initVideo(video.getKey());
                    } else {
                        changeFragment();
                    }
                }
            }

            @Override
            public void onFailure(Call<VideoPageResult> call, Throwable t) {
            }
        });
    }

    private Video selectBestTrailer(List<Video> videoList) {
        Video videoSelected = null;
        for (Video video : videoList) {
            if (video.getType().equals("Trailer") && video.getSite().equals("YouTube")) {
                if (getString(R.string.api_region_key).equals("FR")) {
                    if (video.getName().toLowerCase().contains("vost")) {
                        videoSelected = video;
                        break;
                    } else {
                        videoSelected = video;
                    }
                } else {
                    videoSelected = video;
                    break;
                }
            }
        }
        return videoSelected;
    }

    private void setSimilarMovies() {
        retrofitService.getSimilarMovies(movie_id.toString(), 1, getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(Call<MoviePageResult> call, Response<MoviePageResult> response) {
                MoviePageResult moviePageResult = response.body();
                if (moviePageResult != null) {
                    List<Movie> movieList = moviePageResult.getResults();
                    for (Movie movie : movieList) {
                        if (movie.getTitle() != null && !movie.getTitle().replaceAll("[^a-zA-Z0-9]", "").equals("")) {
                            listSimilarTitles.add(movie.getTitle());
                        }
                    }
                    listSimilarTitles.add(searched_movie.getTitle());
                    Collections.sort(listSimilarTitles);
                    picker.setMinValue(0);
                    picker.setMaxValue(listSimilarTitles.size() - 1);
                    picker.setDisplayedValues(listSimilarTitles.toArray(new String[listSimilarTitles.size()]));
                    picker.setValue(listSimilarTitles.size() / 2);
                    picker.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void changeFragment() {
        BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();
        blindtestMovieActivity.getRandomMovie();
    }

    private void showResult() {
        next = true;
        next_movie.setText(R.string.next_movie);
        next_movie.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        movieCardView.setVisibility(View.VISIBLE);
        picker.setVisibility(View.INVISIBLE);
    }

    private void setMovieViewComponents()
    {
        ImageView movie_image = root.findViewById(R.id.movie_image);
        TextView movie_rating = root.findViewById(R.id.movie_rating);
        TextView movie_release_date = root.findViewById(R.id.movie_release_date);
        TextView movie_language = root.findViewById(R.id.movie_language);

        Glide.with(this).load(BASE_URL_IMAGE + searched_movie.getPosterPath()).into(movie_image);
        movie_title.setText(searched_movie.getTitle());
        movie_rating.setText(getString(R.string.average_rate)+" : "+searched_movie.getVoteAverage().toString()+"/10");
        movie_release_date.setText(getString(R.string.release_date)+" : "+searched_movie.getReleaseDate().replace('-', '/'));
        movie_language.setText(getString(R.string.language_is)+" : "+searched_movie.getOriginalLanguage().toUpperCase());
    }
}