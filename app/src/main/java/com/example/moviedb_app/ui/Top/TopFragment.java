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
    private RecyclerView recyclerViewDiscoverMovies;

    private RecyclerView[] recyclerViews = new RecyclerView[3];
    private int[] recycler_view_ids = {R.id.recycler_view_popular, R.id.recycler_view_toprated, R.id.recycler_view_discover};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_top, container, false);

        LinearLayoutManager[] linearLayoutManagers = new LinearLayoutManager[3];
        for (int i = 0; i < linearLayoutManagers.length; i++) {
            linearLayoutManagers[i] = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViews[i] = root.findViewById(recycler_view_ids[i]);
            recyclerViews[i].setLayoutManager(linearLayoutManagers[i]);
            startRecyclerViewsMovies(i);
        }

        return root;
    }

    public void startRecyclerViewsMovies(int index) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        switch (index) {
            case 0:
                retrofitService.getPopularMovies(1, KEY_API, "US").enqueue(getMoviePageCallback(index));
                break;
            case 1:
                retrofitService.getTopRatedMovies(1, KEY_API, "US").enqueue(getMoviePageCallback(index));
                break;
            case 2:
                retrofitService.getDiscoverEigthiesMovies(1, KEY_API, "vote_count.desc", "US",
                        "false", "1980-01-01", "1989-12-31").enqueue(getMoviePageCallback(index));
                break;
        }
    }

    public Callback<MoviePageResult> getMoviePageCallback(int index) {
        Callback<MoviePageResult> callback = new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();

                recyclerViews[index].setAdapter(new MovieAdapter(res.getResults(), R.layout.preview_movie_top, "horizontal_view"));
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
        return callback;
    }
}


