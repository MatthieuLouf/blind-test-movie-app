package com.example.moviedb_app.ui.blindtest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.Video;
import com.example.moviedb_app.model.VideoPageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;

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
    private String KEY_API = "5b061cba26b441ddec657d88428cc9fc";

    private static final String ARG_PARAM1 = "movie_id";

    private Integer movie_id;
    private Movie searched_movie;

    private Button hider_top;
    private Button next_movie;

    boolean next = false;

    private TextView movie_title;
    private YouTubePlayerView youTubePlayerView;

    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    GetMovieService retrofitService = retrofit.create(GetMovieService.class);

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
        View root = inflater.inflate(R.layout.fragment_one_movie, container, false);

        movie_title = root.findViewById(R.id.one_movie_title);
        hider_top = root.findViewById(R.id.hider_top);
        next_movie = root.findViewById(R.id.next_movie);

        next_movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!next)
                {
                    movie_title.setText(searched_movie.getTitle());
                    next = true;
                    next_movie.setText("Next");
                }
                else{
                    BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();
                    blindtestMovieActivity.getRandomMovie();
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
        retrofitService.getMovie(movie_id.toString(), KEY_API, getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                searched_movie = response.body();
                getBestTrailer(searched_movie.getId().toString());
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
        });
    }

    public void getBestTrailer(String movie_id) {
        retrofitService.getVideos(movie_id, KEY_API, getString(R.string.api_language_key)).enqueue(new Callback<VideoPageResult>() {
            @Override
            public void onResponse(Call<VideoPageResult> call, Response<VideoPageResult> response) {
                if (response.body() != null) {
                    List<Video> videoList = response.body().getResults();
                    Video video = selectBestTrailer(videoList);
                    if (video != null) {
                        initVideo(video.getKey());
                    } else {
                        BlindtestMovieActivity blindtestMovieActivity = (BlindtestMovieActivity) getActivity();
                        blindtestMovieActivity.getRandomMovie();
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
                    }
                } else {
                    videoSelected = video;
                    break;
                }
            }
        }
        return videoSelected;
    }
}
