package com.example.moviedb_app.recycler;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.moviedb_app.R;
import com.example.moviedb_app.data.GetMovieService;
import com.example.moviedb_app.data.RetrofitInstance;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.ui.detail_movie_activity.MovieDetailsActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ProgressBar progressBar;
    private ImageView image;
    private TextView original_title;
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";

    private final String viewType;

    Retrofit retrofit = RetrofitInstance.getRetrofitInstance();
    GetMovieService retrofitService = retrofit.create(GetMovieService.class);

    public MovieViewHolder(@NonNull View itemView, String viewType) {
        super(itemView);
        this.progressBar = itemView.findViewById(R.id.progress_circular);
        this.image = itemView.findViewById(R.id.image);
        this.original_title = itemView.findViewById(R.id.original_title);
        this.viewType = viewType;
    }

    public void bind(final Integer movieId, Context context) {
        retrofitService.getMovie(String.valueOf(movieId), context.getResources().getString(R.string.tmdb_api_key), context.getResources().getString(R.string.api_language_key)).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                Movie movie = response.body();
                original_title.setText(movie.getTitle());
                progressBar.setVisibility(View.VISIBLE);

                Glide.with(itemView).applyDefaultRequestOptions(new RequestOptions()
                        .error(R.drawable.ic_dashboard_black_24dp))
                        .load(BASE_URL_IMAGE + movie.getPosterPath()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).into(image);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MovieDetailsActivity.start(view.getContext(), movie.getId().toString());
                    }

                });
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {

            }

        });

    }
}
