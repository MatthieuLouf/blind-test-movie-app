package com.example.moviedb_app.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.model.Movie;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieViewHolder> {
    private final List<Integer> moviesId;
    private final int layout;
    private final String viewType;

    private final Context context;

    public MovieAdapter(List<Integer> moviesId,int layout, String viewType,Context context) {
        this.moviesId = moviesId;
        this.layout = layout;
        this.viewType = viewType;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new MovieViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder viewHolder, int i) {
        viewHolder.bind(moviesId.get(i),context);
    }

    @Override
    public int getItemCount() {
        return this.moviesId == null ? 0 : this.moviesId.size();
    }
}
