package com.example.moviedb_app.recycler.recycler_detail_movie;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.model.model_detail_movie.Cast;
import com.example.moviedb_app.model.model_detail_movie.ProductionCompany;

import java.util.List;

public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastHolder> {
    private final List<Cast> castList;
    private final int layout;

    public MovieCastAdapter(List<Cast> castList, int layout) {
        this.castList = castList;
        this.layout=layout;
    }

    @NonNull
    @Override
    public MovieCastHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup,false);
        return new MovieCastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCastHolder viewHolder, int i) {
        viewHolder.bind(castList.get(i));
    }

    @Override
    public int getItemCount() {
        return this.castList == null ? 0 : this.castList.size();
    }
}