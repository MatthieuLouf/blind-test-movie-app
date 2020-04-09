package com.example.moviedb_app.recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.model.Movie;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeViewHolder> {
    private final List<BlindtestParameters> parameters;
    private final int layout;
    private final Context context;

    public ThemeAdapter(List<BlindtestParameters> parameters, int layout, Context context) {
        this.parameters = parameters;
        this.layout = layout;
        this.context = context;
    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ThemeViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder viewHolder, int i) {
        viewHolder.bind(parameters.get(i));
    }

    @Override
    public int getItemCount() {
        return this.parameters == null ? 0 : this.parameters.size();
    }
}
