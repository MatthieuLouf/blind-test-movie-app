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
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.recycler.MovieAdapter;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        search_input = root.findViewById(R.id.search_input);
        recyclerView = root.findViewById(R.id.recycler_view_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        search_button = root.findViewById(R.id.search_button);
        start();


        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSearch(search_input.getText().toString());

            }
        });
        return root;
    }

    public void start() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getPopularMovies(1, MOVIE_KEY).enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();

                recyclerView.setAdapter(new MovieAdapter(res.getResults(),R.layout.preview_movie_home,"vertical_view"));

            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        });

    }

    public void startSearch(String query) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getSearchResult(MOVIE_KEY, query).enqueue(new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();
                if (res != null) {
                    recyclerView.setAdapter(new MovieAdapter(res.getResults(),R.layout.preview_movie_home,"vertical_view"));
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