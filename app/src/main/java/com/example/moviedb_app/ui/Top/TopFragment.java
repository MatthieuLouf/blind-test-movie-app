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
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.recycler.EndlessRecyclerViewScrollListener;
import com.example.moviedb_app.recycler.MovieAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopFragment extends Fragment {
    private String KEY_API = "5b061cba26b441ddec657d88428cc9fc";

    private RecyclerView[] recyclerViews = new RecyclerView[3];
    private int[] recycler_view_ids = {R.id.recycler_view_popular, R.id.recycler_view_toprated, R.id.recycler_view_discover};
    private MovieAdapter[] movieAdapters = new MovieAdapter[3];
    private int[] totalPages = new int[3];

    private List<Movie> popularMovies = new ArrayList<Movie>();
    private List<Movie> topratedMovies = new ArrayList<Movie>();
    private List<Movie> discoverMovies = new ArrayList<Movie>();
    private List<List<Movie>> movieLists = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_top, container, false);
        LinearLayoutManager[] linearLayoutManagers = new LinearLayoutManager[3];

        movieLists.add(popularMovies);
        movieLists.add(topratedMovies);
        movieLists.add(discoverMovies);

        for (int i = 0; i < linearLayoutManagers.length; i++) {
            linearLayoutManagers[i] = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViews[i] = root.findViewById(recycler_view_ids[i]);
            recyclerViews[i].setLayoutManager(linearLayoutManagers[i]);
            loadRecyclerViewsMovies(i, 1);

            int finalI = i;
            EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManagers[finalI]) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    if ((page + 1) <= totalPages[finalI]) {
                        loadRecyclerViewsMovies(finalI, page + 1);
                    }
                }
            };

            recyclerViews[i].addOnScrollListener(scrollListener);
        }

        return root;
    }

    public void loadRecyclerViewsMovies(int recyclerIndex, int page) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        switch (recyclerIndex) {
            case 0:
                retrofitService.getPopularMovies(page, KEY_API, getString(R.string.api_language_key),"US").enqueue(getMoviePageCallback(recyclerIndex, page));
                break;
            case 1:
                retrofitService.getTopRatedMovies(page, KEY_API,getString(R.string.api_language_key), "US").enqueue(getMoviePageCallback(recyclerIndex, page));
                break;
            case 2:
                retrofitService.getDiscoverEigthiesMovies(page, KEY_API,getString(R.string.api_language_key), "vote_count.desc", "US",
                        "false", "1980-01-01", "1989-12-31").enqueue(getMoviePageCallback(recyclerIndex, page));
                break;
        }
    }


    public Callback<MoviePageResult> getMoviePageCallback(int index, int page) {
        Callback<MoviePageResult> callback = new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                if (page == 1) {
                    MoviePageResult res = response.body();
                    totalPages[index] = res.getTotalPages();
                    movieLists.set(index, res.getResults());
                    movieAdapters[index] = new MovieAdapter(movieLists.get(index), R.layout.preview_movie_top, "horizontal_view");
                    recyclerViews[index].setAdapter(movieAdapters[index]);
                } else {
                    List<Movie> movies = response.body().getResults();
                    for (Movie movie : movies) {
                        movieLists.get(index).add(movie);
                        movieAdapters[index].notifyItemInserted(movieLists.get(index).size() - 1);
                    }
                }

            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
        return callback;
    }
}


