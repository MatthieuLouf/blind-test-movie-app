package com.matthieu_louf.movie_blindtest_app.pages.savedMoviesPage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.sharedPreferences.SeenMoviesService;
import com.matthieu_louf.movie_blindtest_app.recycler.movie.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

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

        int orientation = GridLayoutManager.VERTICAL;
        int spanCount=2;
        if(getContext().getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE) && Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation)
        {
            spanCount = 4;
        }
        else if (getContext().getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE) || Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            spanCount = 3;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),spanCount,orientation,false);
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
            Toast.makeText(getActivity(), R.string.no_seen_movies, Toast.LENGTH_SHORT).show();
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