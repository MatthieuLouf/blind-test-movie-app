package com.example.moviedb_app.recycler;


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
import com.bumptech.glide.request.target.Target;
import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.ui.detail_movie_activity.MovieDetailsActivity;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ProgressBar progressBar;
    private ImageView image;
    private TextView original_title;
    private TextView rate;
    private TextView release_date;
    private TextView language;
    private String BASE_URL_IMAGE = "https://image.tmdb.org/t/p/w600_and_h900_bestv2/";

    private final String viewType;

    public MovieViewHolder(@NonNull View itemView, String viewType) {
        super(itemView);
        this.progressBar = itemView.findViewById(R.id.progress_circular);
        this.image = itemView.findViewById(R.id.image);
        this.original_title = itemView.findViewById(R.id.original_title);
        this.rate = itemView.findViewById(R.id.rating);
        this.release_date = itemView.findViewById(R.id.release_date);
        this.viewType = viewType;
        if (this.viewType.equals("horizontal_view")) {
            this.language = itemView.findViewById(R.id.language);
        }
    }

    public void bind(final Movie movie) {
        original_title.setText(movie.getTitle());
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(itemView).load(BASE_URL_IMAGE + movie.getPosterPath()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(image);

        if (this.viewType.equals("vertical_view")) {
            rate.setText("Rating : " + movie.getVoteAverage().toString());
            release_date.setText("Release date : " + movie.getReleaseDate().replace('-', '/'));
        } else if (this.viewType.equals("horizontal_view")) {
            rate.setText("Rating : " + movie.getVoteAverage().toString());
            if (movie.getReleaseDate() != null) {
                release_date.setText("Release date :\n" + movie.getReleaseDate().replace('-', '/'));
            }
            language.setText("Language : " + movie.getOriginalLanguage());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MovieDetailsActivity.start(view.getContext(), movie.getId().toString());
                }

            });
        }
    }
}
