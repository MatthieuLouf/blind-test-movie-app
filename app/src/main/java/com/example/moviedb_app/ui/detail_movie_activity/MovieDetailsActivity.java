package com.example.moviedb_app.ui.detail_movie_activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moviedb_app.R;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.ui.detail_movie_activity.model.Genre;
import com.example.moviedb_app.ui.detail_movie_activity.model.MovieDetails;

public class MovieDetailsActivity extends AppCompatActivity {
    private static final String MOVIE_ID = "";
    private String MOVIE_KEY = "5b061cba26b441ddec657d88428cc9fc";
    private String BASE_URL_IMAGE ="https://image.tmdb.org/t/p/w600_and_h900_bestv2/";
    private String movieId;
    private ImageView image;
    private TextView original_title;
    private TextView rate;
    private TextView release_date;
    private TextView genres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra(MOVIE_ID);
        }

        this.image=findViewById(R.id.image_details);
        this.original_title=findViewById(R.id.original_title_details);
        this.rate=findViewById(R.id.rating_details);
        this.release_date=findViewById(R.id.release_date_details);
        this.genres=findViewById(R.id.genre_details);
        startSearch(movieId);
    }

    public void startSearch(String query) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovieDetails(query, MOVIE_KEY).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                MovieDetails res = response.body();
                TextView test = findViewById(R.id.test);
                test.setText(res.getOriginalTitle());

                Glide.with(MovieDetailsActivity.this).load(BASE_URL_IMAGE+res.getPosterPath()).into(image);
                original_title.setText(res.getOriginalTitle());
                rate.setText("Average note : "+res.getVoteAverage().toString());
                release_date.setText("Release Date : "+res.getReleaseDate());
                String genre_comment="Genres :";
                if (res.getGenres() != null)
                {
                    for (Genre x : res.getGenres())
                    {
                        genre_comment=genre_comment+" "+x.getName();
                    }
                }
                genres.setText(genre_comment);


            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable t) {
            }

        });

    }

    public static void start(Context context, String movieId) {
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(MOVIE_ID, movieId);
        context.startActivity(intent);
    }
}
