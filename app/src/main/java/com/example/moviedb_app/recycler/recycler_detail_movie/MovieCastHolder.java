package com.example.moviedb_app.recycler.recycler_detail_movie;

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
import com.example.moviedb_app.model.model_detail_movie.Cast;
import com.example.moviedb_app.model.model_detail_movie.ProductionCompany;

public class MovieCastHolder extends RecyclerView.ViewHolder {
    private final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w92/";
    private TextView castName;
    private TextView castRole;
    private ImageView castImage;
    private ProgressBar progressBar;

    MovieCastHolder(@NonNull View itemView) {
        super(itemView);
        castImage = itemView.findViewById(R.id.movie_details_person_image);
        castName = itemView.findViewById(R.id.movie_details_person_name);
        castRole = itemView.findViewById(R.id.movie_details_person_role);
        progressBar = itemView.findViewById(R.id.movie_details_progress_circular);
    }

    public void bind(final Cast cast) {
        castName.setText(cast.getName());
        castRole.setText("("+cast.getCharacter()+")");

        if (cast.getProfilePath() != null) {
            Glide.with(itemView)
                    .load(BASE_IMAGE_URL + cast.getProfilePath()).listener(new RequestListener<Drawable>() {
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
            }).into(castImage);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }
}
