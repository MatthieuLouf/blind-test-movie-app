package com.example.moviedb_app.recycler.recycler_detail_movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.model.model_detail_movie.Cast;
import com.example.moviedb_app.model.model_detail_movie.Crew;

import java.util.List;

public class MovieCrewAdapter extends RecyclerView.Adapter<MovieCrewHolder> {
    private final List<Crew> crewList;
    private final int layout;

    public MovieCrewAdapter(List<Crew> crewList, int layout) {
        this.crewList = crewList;
        this.layout=layout;
    }

    @NonNull
    @Override
    public MovieCrewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup,false);
        return new MovieCrewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCrewHolder viewHolder, int i) {
        viewHolder.bind(crewList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.crewList == null ? 0 : this.crewList.size();
    }
}