package com.matthieu_louf.movie_blindtest_app.recycler.languagePreference;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.ProductionCompany;

import java.util.List;

public class LanguagePreferenceAdapter extends RecyclerView.Adapter<LanguagePreferenceHolder> {
    private final List<String> languagePreferences;
    private final int layout;
    private Context context;

    public LanguagePreferenceAdapter(List<String> languagePreferences, int layout, Context context) {
        this.languagePreferences = languagePreferences;
        this.layout=layout;
        this.context = context;
    }

    @NonNull
    @Override
    public LanguagePreferenceHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup,false);
        return new LanguagePreferenceHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguagePreferenceHolder viewHolder, int i) {
        viewHolder.bind(languagePreferences.get(i));
    }

    @Override
    public int getItemCount() {
        return this.languagePreferences == null ? 0 : this.languagePreferences.size();
    }
}