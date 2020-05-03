package com.matthieu_louf.movie_blindtest_app.ui.blindtest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.matthieu_louf.movie_blindtest_app.R;

import com.matthieu_louf.movie_blindtest_app.model.BlindtestParameters;
import com.matthieu_louf.movie_blindtest_app.model.Movie;

import com.matthieu_louf.movie_blindtest_app.data.MovieAPIHelper;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlindtestMovieActivity extends AppCompatActivity {
    private String TAG = "BlindtestMovieActivity";

    private static final String PARAMETERS_KEY = "PARAMETERS_KEY";

    private BlindtestParameters blindtestParameters;

    private List<Movie> movieList = new ArrayList<Movie>();

    boolean firstTime = true;

    Integer movie_count = 0;
    Integer error_count = 0;
    Integer good_responses_count = 0;

    FragmentContainerView fragmentContainerView;
    ProgressBar progressBar;
    TextView loadingText;

    //private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FragmentManager fragmentManager = getSupportFragmentManager();

    MovieAPIHelper movieAPIHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindtest_movie_page);
        movieAPIHelper = new MovieAPIHelper(this);

        fragmentContainerView = findViewById(R.id.one_movie_fragment_container);
        progressBar = findViewById(R.id.loading_progress);
        loadingText = findViewById(R.id.loading_textview);

        Intent intent = getIntent();
        if (intent != null) {
            blindtestParameters = (BlindtestParameters) intent.getSerializableExtra(PARAMETERS_KEY);
        }
        showLoading();

        loadList();

    }

    public void loadList() {
        Log.d(TAG, "enter getRandomMovie");

        movieAPIHelper.loadList(this, blindtestParameters, new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.body() != null) {
                    Log.d(TAG, "Got a sub list of movies, movieList as length :" + movieList.size());
                    movieList.addAll(response.body());
                    getRandomMovie(false);
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {

            }
        });
    }

    public void getRandomMovie(boolean loadingFail) {
        showLoading();
        Movie movie = movieAPIHelper.chooseMovieInList(movieList,loadingFail);
        if (movie == null) {
            Log.d(TAG, "Error while getting a random movie, error count : " + error_count);
            if (error_count < (blindtestParameters.getMaximumPage() * 20)+1) {
                Log.d(TAG, "Restart getRandom Movie");
                error_count++;
                getRandomMovie(loadingFail);
            } else {
                Log.d(TAG, "Finish activity");
                Toast.makeText(getBaseContext(), getString(R.string.load_error_not_enough_movies), Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        } else {
            startFragment(movie, loadingFail);
            error_count = 0;
        }

    }

    public void startFragment(Movie movie, boolean loadingFail) {
        Log.d(TAG, "Starting a new OneMovieFragment, firstTime = " + firstTime);
        if (!loadingFail) {
            movie_count++;
        }
        if (movie_count <= 10) {
            OneMovieFragment fragment = OneMovieFragment.newInstance(movie.getId(),
                    getString(blindtestParameters.getIdName()) + " : " + movie_count + "/10");
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            if (firstTime) {
                fragmentTransaction.add(R.id.one_movie_fragment_container, fragment);
                firstTime = false;
            } else {
                fragmentTransaction.replace(R.id.one_movie_fragment_container, fragment);
            }
            fragmentTransaction.commit();
        } else {
            startFinishFragment();
        }
    }

    public static void start(Context context, BlindtestParameters parameters) {
        Intent intent = new Intent(context, BlindtestMovieActivity.class);
        intent.putExtra(PARAMETERS_KEY, parameters);
        context.startActivity(intent);
    }

    public void showLoading() {
        changeLoadingText(false);
        loadingText.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        fragmentContainerView.setVisibility(View.INVISIBLE);
    }

    public void hideLoading() {
        loadingText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        fragmentContainerView.setVisibility(View.VISIBLE);
    }

    public void changeLoadingText(boolean isVideoLoading) {
        if (isVideoLoading) {
            loadingText.setText(R.string.loading_trailer);
        } else {
            loadingText.setText(R.string.loading);
        }
    }

    public void newResponse(boolean right_guessed) {
        if (right_guessed) {
            good_responses_count++;
        }
    }

    public void startFinishFragment() {
        Log.d(TAG, "Starting a new FinishPageFragment");
        loadingText.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        fragmentContainerView.setVisibility(View.VISIBLE);

        FinishPageFragment fragment = FinishPageFragment.newInstance(good_responses_count, blindtestParameters);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.one_movie_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }
}
