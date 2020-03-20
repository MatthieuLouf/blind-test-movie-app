package com.example.moviedb_app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class HomeFragment extends Fragment {
    public HomeFragment() {
    }

    private String MOVIE_KEY = "5b061cba26b441ddec657d88428cc9fc";
    private EditText search_input;
    private RecyclerView recyclerView;
    private Button search_button;

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private int totalPages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        search_input = root.findViewById(R.id.search_input);
        recyclerView = root.findViewById(R.id.recycler_view_search);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        search_button = root.findViewById(R.id.search_button);
        load(1);


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadSearch(1,search_input.getText().toString());

            }
        });

        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                if ((page + 1) <= totalPages) {
                    load(page + 1);
                }
            }
        };

        recyclerView.addOnScrollListener(scrollListener);

        return root;
    }

    public void load(int page) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getPopularMovies(page, MOVIE_KEY,"US").enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                if(page==1)
                {
                    MoviePageResult res = response.body();
                    totalPages = res.getTotalPages();
                    movieList = res.getResults();
                    movieAdapter =new MovieAdapter(res.getResults(),R.layout.preview_movie_home,"vertical_view");

                    recyclerView.setAdapter(movieAdapter);
                }
                else{
                    List<Movie> movies = response.body().getResults();
                    for (Movie movie : movies) {
                        movieList.add(movie);
                        movieAdapter.notifyItemInserted(movieList.size() - 1);
                    }
                }

            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        });

    }

    public void loadSearch(int page, String query) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getSearchResult(page,MOVIE_KEY, query).enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();
                if (res != null) {
                    if(page==1)
                    {
                        totalPages = res.getTotalPages();
                        movieList = res.getResults();
                        movieAdapter =new MovieAdapter(res.getResults(),R.layout.preview_movie_home,"vertical_view");

                        recyclerView.setAdapter(movieAdapter);
                    }
                    else{
                        List<Movie> movies = response.body().getResults();
                        for (Movie movie : movies) {
                            movieList.add(movie);
                            movieAdapter.notifyItemInserted(movieList.size() - 1);
                        }
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Please insert what you want before !",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        });


    }
}