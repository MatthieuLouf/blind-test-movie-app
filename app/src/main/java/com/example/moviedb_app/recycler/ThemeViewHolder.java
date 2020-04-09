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
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.ui.blindtest.BlindtestMovieActivity;
import com.example.moviedb_app.ui.detail_movie_activity.MovieDetailsActivity;

public class ThemeViewHolder extends RecyclerView.ViewHolder {

    private TextView theme_title;
    private Context context;

    public ThemeViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.theme_title = itemView.findViewById(R.id.theme_title);
        this.context = context;
    }

    public void bind(final BlindtestParameters parameters) {
        theme_title.setText(context.getResources().getString(parameters.getIdName()));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BlindtestMovieActivity.start(view.getContext(),parameters);
            }

        });
    }
}
