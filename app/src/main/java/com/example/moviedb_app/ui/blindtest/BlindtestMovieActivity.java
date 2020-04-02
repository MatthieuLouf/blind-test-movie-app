package com.example.moviedb_app.ui.blindtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.model.MoviePageResult;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.recycler.MovieAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BlindtestMovieActivity extends AppCompatActivity {
    private String KEY_API = "5b061cba26b441ddec657d88428cc9fc";
    private String TAG = "create movie";
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindtest_movie_page);
    }

    /*private void scrapTopMovies() {
        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
        GetMovieService retrofitService = retrofit.create(GetMovieService.class);
        for (int j = 2010; j <= 2020; j++) {
            for (int i = 0; i < 10; i++) {
                retrofitService.getDiscoverMovies(i, KEY_API, "en-US", "vote_count.desc", "FR",
                        "false", j+"-01-01", j+"-12-31").enqueue(getMoviePageCallback());
            }
        }
    }

    private Callback<MoviePageResult> getMoviePageCallback() {
        Callback<MoviePageResult> callback = new Callback<MoviePageResult>() {
            @Override
            public void onResponse(@NonNull Call<MoviePageResult> call, @NonNull Response<MoviePageResult> response) {
                MoviePageResult res = response.body();
                if (res != null) {
                    List<Movie> list = res.getResults();

                    for (Movie movie : list
                    ) {
                        try {
                            createMovie(movie);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviePageResult> call, Throwable t) {
            }

        };
        return callback;
    }

    private void createMovie(Movie movie) throws ParseException {

        db.collection("movies").document(movie.getId().toString())
                .set(movie)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        try {
                            db.collection("movies").document(movie.getId().toString())
                                    .update("releaseDate", new Timestamp(formatter.parse(movie.getReleaseDate())))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "DocumentSnapshot successfully written!");
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.w(TAG, "Error writing document", e);
                                        }
                                    });
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }*/
}
