package com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.ProductionCompany;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class MovieProductionHolder extends RecyclerView.ViewHolder {
    private final String BASE_IMAGE_URL="https://image.tmdb.org/t/p/w92/";
    private TextView production_name;
    private ImageView production_logo;
    private ProgressBar progressBar;

    public MovieProductionHolder(@NonNull View itemView) {
        super(itemView);
        production_logo=itemView.findViewById(R.id.movie_details_production_image);
        production_name=itemView.findViewById(R.id.movie_details_production_name);
        progressBar=itemView.findViewById(R.id.movie_details_progress_circular);
    }
    public void bind(final ProductionCompany productionCompany) {
        production_name.setText(productionCompany.getName());

        Glide.with(itemView)
                .load(BASE_IMAGE_URL + productionCompany.getLogoPath()).listener(new RequestListener<Drawable>() {
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
        }).into(production_logo);
    }
}
