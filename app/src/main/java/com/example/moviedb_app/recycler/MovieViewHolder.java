package com.example.moviedb_app.recycler;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviedb_app.R;
import com.example.moviedb_app.model.Movie;
import com.example.moviedb_app.ui.detail_movie_activity.MovieDetailsActivity;

public class MovieViewHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView original_title;
    private TextView rate;
    private TextView release_date;
    private TextView language;
    private String BASE_URL_IMAGE ="https://image.tmdb.org/t/p/w600_and_h900_bestv2/";

    private final String viewType;

    public MovieViewHolder(@NonNull View itemView,String viewType) {
        super(itemView);
        this.image=itemView.findViewById(R.id.image);
        this.original_title=itemView.findViewById(R.id.original_title);
        this.rate=itemView.findViewById(R.id.rating);
        this.release_date=itemView.findViewById(R.id.release_date);
        this.viewType = viewType;
        if(this.viewType.equals("horizontal_view"))
        {
            this.language = itemView.findViewById(R.id.language);
        }
    }

    public void bind(final Movie movie) {
        original_title.setText(movie.getOriginalTitle());
        Glide.with(itemView).load(BASE_URL_IMAGE+movie.getPosterPath()).into(image);

        if(this.viewType.equals("vertical_view"))
        {
            rate.setText("Rating : "+movie.getVoteAverage().toString());
            release_date.setText("Release date : "+movie.getReleaseDate().replace('-','/'));
        }
        else if(this.viewType.equals("horizontal_view"))
        {
            rate.setText("Rating : "+movie.getVoteAverage().toString());
            release_date.setText("Release date :\n"+movie.getReleaseDate().replace('-','/'));
            language.setText("Language : " + movie.getOriginalLanguage());
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MovieDetailsActivity.start(view.getContext(),movie.getId().toString());
            }

        });
    }
}
