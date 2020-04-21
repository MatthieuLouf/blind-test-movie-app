package com.example.moviedb_app.ui.User;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.data.GetMovieService;
import com.example.moviedb_app.data.RetrofitInstance;
import com.example.moviedb_app.recycler.MovieAdapter;
import com.example.moviedb_app.data.UserLikeService;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StaredMoviesFragment extends Fragment {
    private static final String TAG = "StaredMoviesFragment";

    //private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private List<Movie> movieList = new ArrayList<>();
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;

    public void onStart() {
        super.onStart();

        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            login();
        } else {
            movieList = new ArrayList<>();
            loadMovies();
        }*/
        Log.d(TAG, "onStart()");
        movieList = new ArrayList<>();
        loadMovies();
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stared_movies, container, false);

        recyclerView = root.findViewById(R.id.recycler_view_user);
        Log.d(TAG, "onCreateView()");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        return root;
    }

    private void loadMovies() {
        Log.d(TAG, "loadMovies()");

        UserLikeService userLikeService = new UserLikeService((AppCompatActivity)this.getActivity());
        List<Integer> userLikesIds = userLikeService.getLikes();
        if (userLikesIds.size() > 0) {
            for(int i =userLikesIds.size()-1;i>=0;i--)
            {
                loadOneMovie(userLikesIds.get(i));
            }
        } else {
            Toast.makeText(getActivity(), "There is no liked Movies", Toast.LENGTH_LONG).show();
        }
    }

    private void loadOneMovie(int movieId) {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovie(String.valueOf(movieId), getContext().getResources().getString(R.string.tmdb_api_key), getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                Movie res = response.body();
                movieList.add(res);
                if (movieList.size() == 1) {
                    movieAdapter = new MovieAdapter(movieList, R.layout.preview_movie_user, "grid_view");

                    recyclerView.setAdapter(movieAdapter);
                } else {
                    movieAdapter.notifyItemInserted(movieList.size() - 1);
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }

        });
    }

    /*private void login() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

// Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(new Intent(getActivity(), MainActivity.class));
            } else {
                Toast.makeText(getActivity(), R.string.login_failed, Toast.LENGTH_LONG).show();
            }
        }
    }*/

}