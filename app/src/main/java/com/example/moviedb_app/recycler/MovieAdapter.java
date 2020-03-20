package com.example.moviedb_app.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private final List<Movie> movies;
    private final int layout;

    public MovieAdapter(List<Movie> movies,int layout) {
        this.movies = movies;
        this.layout = layout;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int i) {
        viewHolder.bind(movies.get(i));
    }

    @Override
    public int getItemCount() {
        return this.movies == null ? 0 : this.movies.size();
    }
}
