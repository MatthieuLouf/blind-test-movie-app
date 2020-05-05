package com.matthieu_louf.movie_blindtest_app.recycler.theme;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.models.blindtest.BlindtestParameters;

import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<ThemeViewHolder> {
    private final List<BlindtestParameters> parameters;
    private final int layout;
    private final Activity activity;
    private final boolean finish_activity;

    public ThemeAdapter(List<BlindtestParameters> parameters, int layout, Activity activity,boolean finish_activity) {
        this.parameters = parameters;
        this.layout = layout;
        this.activity = activity;
        this.finish_activity = finish_activity;
    }

    @NonNull
    @Override
    public ThemeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ThemeViewHolder(view,activity);
    }

    @Override
    public void onBindViewHolder(@NonNull ThemeViewHolder viewHolder, int i) {
        viewHolder.bind(parameters.get(i),activity, finish_activity);
    }

    @Override
    public int getItemCount() {
        return this.parameters == null ? 0 : this.parameters.size();
    }
}
