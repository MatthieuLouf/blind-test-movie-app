package com.example.moviedb_app.ui.blindtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.moviedb_app.R;

import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.model.Movie;

import com.example.moviedb_app.network.MovieAPIHelper;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BlindtestMovieActivity extends AppCompatActivity {
    private String TAG = "BlindtestMovieActivity";

    private static final String PARAMETERS_KEY = "PARAMETERS_KEY";

    private BlindtestParameters blindtestParameters;

    boolean firstTime= true;

    Integer movie_count=0;
    Integer error_count = 0;

    //private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();

    MovieAPIHelper movieAPIHelper = new MovieAPIHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindtest_movie_page);

        Intent intent = getIntent();
        if (intent != null) {
            blindtestParameters = (BlindtestParameters) intent.getSerializableExtra(PARAMETERS_KEY);
        }

        getRandomMovie(false);

    }

    public void getRandomMovie(boolean loadingFail) {
        Log.d(TAG, "enter getRandomMovie");

        movieAPIHelper.loadList(this,blindtestParameters, new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if(response.body()!=null)
                {
                    Log.d(TAG, "Got a random movie not null : "+response.body().getId() +" - " +response.body().getTitle());
                    startFragment(response.body(),loadingFail);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                Log.d(TAG, "Error while getting a random movie, error count : "+error_count);
                if(error_count<5)
                {
                    Log.d(TAG, "Restart getRandom Movie");
                    int numberPage = blindtestParameters.getMaximumPage() ==1 ? 1 : blindtestParameters.getMaximumPage()-1;
                    blindtestParameters.setMaximumPage(numberPage);
                    error_count++;
                    getRandomMovie(false);
                }
                else{
                    Log.d(TAG, "Finish activity: ");
                    finish();
                }
            }
        });
    }

    public void startFragment(Movie movie,boolean loadingFail) {
        Log.d(TAG, "Starting a new OneMovieFragment, firstTime = " + firstTime);
        if(!loadingFail)
        {
            movie_count++;
        }
        OneMovieFragment fragment = OneMovieFragment.newInstance(movie.getId(),
                getString(blindtestParameters.getIdName())+" : " +movie_count);
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

    public static void start(Context context, BlindtestParameters parameters) {
        Intent intent = new Intent(context, BlindtestMovieActivity.class);
        intent.putExtra(PARAMETERS_KEY, parameters);
        context.startActivity(intent);
    }

}
