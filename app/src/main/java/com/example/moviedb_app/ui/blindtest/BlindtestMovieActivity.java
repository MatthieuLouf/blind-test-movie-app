package com.example.moviedb_app.ui.blindtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.moviedb_app.R;

import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;

import com.google.firebase.firestore.FirebaseFirestore;


import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BlindtestMovieActivity extends AppCompatActivity {
    private String TAG = "create movie";

    private int max_movie_id = 700000;
    Random rnd = new Random();

    boolean firstTime= true;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();

    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    GetMovieService retrofitService = retrofit.create(GetMovieService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindtest_movie_page);

        //rnd.setSeed(700363);
        getRandomMovie();

    }

    public void getRandomMovie() {
        int random_page = rnd.nextInt(6);
        loadList(random_page + 1);
    }

    public void loadList(int page) {
        retrofitService.getTopRatedMovies(page,  getString(R.string.tmdb_api_key), getString(R.string.api_language_key), getString(R.string.api_region_key)).enqueue(movieListCallback());
    }

    public Callback<MoviePageResult> movieListCallback() {
        return new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                if (response.body() != null) {
                    List<Movie> movieList = response.body().getResults();
                    chooseMovieInList(movieList);
                }
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
    }

    public void chooseMovieInList(List<Movie> movies) {
        int random = rnd.nextInt(20);
        Movie movie = movies.get(random);

        startFragment(movie);
    }

    public void startFragment(Movie movie) {
        OneMovieFragment fragment = OneMovieFragment.newInstance(movie.getId());
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(firstTime)
        {
            fragmentTransaction.add(R.id.one_movie_fragment_container, fragment);
            firstTime = false;
        }
        else {
            fragmentTransaction.replace(R.id.one_movie_fragment_container, fragment);
        }
        fragmentTransaction.commit();
    }
}
