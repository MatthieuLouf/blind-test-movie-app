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
import android.widget.Toast;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.google.android.material.badge.BadgeUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;


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

    private TextView movie_title;
    private YouTubePlayerView youTubePlayerView;

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

        youTubePlayerView = root.findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        PlayerUiController playerUiController =youTubePlayerView.getPlayerUiController();
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


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = "S0Q4gqBUs7c";
                youTubePlayer.loadVideo(videoId, 0f);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state){
                if(state== PlayerConstants.PlayerState.PLAYING)
                {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            hider_top.setVisibility(View.INVISIBLE);
                        }
                    }, 700);
                }
                if(state== PlayerConstants.PlayerState.PAUSED)
                {
                    hider_top.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onVideoId(@NonNull YouTubePlayer youTubePlayer, String videoId){
                Toast.makeText(getActivity(), "onVideoId", Toast.LENGTH_LONG).show();
            }
        });

        fetchMovieDetails();

        return root;
    }

    public void fetchMovieDetails() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovie(movie_id.toString(), KEY_API, getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                searched_movie = response.body();
                movie_title.setText(searched_movie.getTitle());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }
        });
    }
}
