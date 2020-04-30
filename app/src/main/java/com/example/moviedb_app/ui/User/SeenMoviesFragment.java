package com.example.moviedb_app.ui.User;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.data.GetMovieService;
import com.example.moviedb_app.data.RetrofitInstance;
import com.example.moviedb_app.data.SeenMoviesService;
import com.example.moviedb_app.data.UserLikeService;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.recycler.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SeenMoviesFragment extends Fragment {

    private List<Integer> movieIdsList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;

    public void onStart() {
        super.onStart();

        movieIdsList = new ArrayList<>();
        loadMovies();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stared_movies, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_user);

        LinearLayoutManager gridLayoutManager = null;
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            gridLayoutManager = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        } else {
            gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        }
        recyclerView.setLayoutManager(gridLayoutManager);

        return root;
    }

    private void loadMovies() {
        SeenMoviesService seenMoviesService = new SeenMoviesService(this.getActivity());
        List<Integer> userLikesIds = seenMoviesService.getSeenMovies();
        if (userLikesIds.size() > 0) {
            for (int i = userLikesIds.size() - 1; i >= 0; i--) {
                loadOneMovie(userLikesIds.get(i));
            }
        } else {
            Toast.makeText(getActivity(), "There is no seen Movies", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOneMovie(int movieId) {
        movieIdsList.add(movieId);

        if (movieIdsList.size() == 1) {
            movieAdapter = new MovieAdapter(movieIdsList, R.layout.preview_movie_user, "grid_view", getContext());

            recyclerView.setAdapter(movieAdapter);
        } else {
            movieAdapter.notifyItemInserted(movieIdsList.size() - 1);
        }
    }

}