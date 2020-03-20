package com.example.moviedb_app.ui.Top;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.recycler.MovieAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopFragment extends Fragment {
    private String KEY_API = "5b061cba26b441ddec657d88428cc9fc";

    private RecyclerView recyclerViewPopularMovies;
    private RecyclerView recyclerViewTopRatedMovies;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_top, container, false);

        LinearLayoutManager layoutManagerPopular
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManagerTopRated
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerViewPopularMovies = root.findViewById(R.id.recycler_view_popular);
        recyclerViewPopularMovies.setLayoutManager(layoutManagerPopular);

        recyclerViewTopRatedMovies = root.findViewById(R.id.recycler_view_toprated);
        recyclerViewTopRatedMovies.setLayoutManager(layoutManagerTopRated);

        startPopularMovies();
        startTopRatedMovies();

        return root;
    }

    public void startPopularMovies() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getPopularMovies(1, KEY_API).enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();

                recyclerViewPopularMovies.setAdapter(new MovieAdapter(res.getResults(),R.layout.preview_movie_top,"horizontal_view"));
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        });
    }

    public void startTopRatedMovies() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getTopRatedMovies(1, KEY_API).enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();

                recyclerViewTopRatedMovies.setAdapter(new MovieAdapter(res.getResults(),R.layout.preview_movie_top,"horizontal_view"));
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        });
    }
}