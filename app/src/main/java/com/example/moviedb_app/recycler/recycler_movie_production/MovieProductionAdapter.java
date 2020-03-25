package com.example.moviedb_app.recycler.recycler_movie_production;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.moviedb_app.recycler.MovieViewHolder;
import com.example.moviedb_app.ui.detail_movie_activity.model.ProductionCompany;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieProductionAdapter extends RecyclerView.Adapter<MovieProductionHolder> {
    private final List<ProductionCompany> productionCompanies;
    private final int layout;

    public MovieProductionAdapter(List<ProductionCompany> movies,int layout) {
        this.productionCompanies = movies;
        this.layout=layout;
    }

    @NonNull
    @Override
    public MovieProductionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup,false);
        return new MovieProductionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieProductionHolder viewHolder, int i) {
        viewHolder.bind(productionCompanies.get(i));
    }

    @Override
    public int getItemCount() {
        return this.productionCompanies == null ? 0 : this.productionCompanies.size();
    }
}