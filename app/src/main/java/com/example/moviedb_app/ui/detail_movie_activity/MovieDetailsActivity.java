package com.example.moviedb_app.ui.detail_movie_activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviedb_app.R;
import com.example.moviedb_app.network.GetMovieService;
import com.example.moviedb_app.network.RetrofitInstance;
import com.example.moviedb_app.ui.detail_movie_activity.model.Genre;
import com.example.moviedb_app.ui.detail_movie_activity.model.MovieDetails;
import com.example.moviedb_app.userdata.UserLikeService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

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
    private TextView synopsis;
    private TextView budget;
    private TextView collection_name;
    private TextView collection_separator;
    private ImageView collection_image;

    private Button likeButton;
    private UserLikeService userLikeService;
    private boolean isLiked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getStringExtra(MOVIE_ID);
        }

        userLikeService = new UserLikeService(this);
        isLiked = userLikeService.isLiked(Integer.parseInt(movieId));
        this.likeButton = findViewById(R.id.button_like);
        likeButton.setText(isLiked? R.string.unlike : R.string.like);

        this.image=findViewById(R.id.image_details);
        this.original_title=findViewById(R.id.original_title_details);
        this.rate=findViewById(R.id.rating_details);
        this.release_date=findViewById(R.id.release_date_details);
        this.genres=findViewById(R.id.genre_details);
        this.synopsis=findViewById(R.id.synopsis_details);
        this.budget=findViewById(R.id.budget_details);
        this.collection_image=findViewById(R.id.collection_details_image);
        this.collection_name=findViewById(R.id.collection_details_name);
        this.collection_separator=findViewById(R.id.collection_details_separator);
        startSearch(movieId);

        this.likeButton.setOnClickListener(v -> {
            if(isLiked)
            {
                userLikeService.removeLike(Integer.parseInt(movieId));
            }
            else{
                userLikeService.addLike(Integer.parseInt(movieId));
            }
            isLiked = !isLiked;
            likeButton.setText(isLiked? R.string.unlike : R.string.like);
        });
    }

    public void startSearch(String query) {

        Retrofit retrofit = RetrofitInstance.getRetrofitInstance();

        GetMovieService retrofitService = retrofit.create(GetMovieService.class);

        retrofitService.getMovieDetails(query, MOVIE_KEY).enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetails> call, @NonNull Response<MovieDetails> response) {
                MovieDetails res = response.body();

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
                synopsis.setText("Synopsis : \n \n"+res.getOverview());
                budget.setText("Budget : \n \n"+res.getBudget().toString()+"$"+"\n \nRevenue : \n \n"+res.getRevenue().toString()+"$");
                if (res.getBelongsToCollection() != null)
                {
                    collection_name.setText(res.getBelongsToCollection().getName());
                    collection_separator.setText("Collection :");
                    Glide.with(MovieDetailsActivity.this).load(BASE_URL_IMAGE+res.getBelongsToCollection().getPosterPath()).into(collection_image);
                }


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
