package com.matthieu_louf.movie_blindtest_app.recycler.theme;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.GameType;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;
import com.matthieu_louf.movie_blindtest_app.models.sharedPreferences.ThemePlayed;
import com.matthieu_louf.movie_blindtest_app.pages.games.MovieGameContainerActivity;

public class ThemeViewHolder extends RecyclerView.ViewHolder {

    private TextView theme_title;
    private ImageView theme_image;
    private Context context;
    private Chip number_movie_played_chip;
    private Chip best_score_chip;

    ThemeViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.theme_title = itemView.findViewById(R.id.theme_title);
        this.theme_image = itemView.findViewById(R.id.theme_image);
        this.number_movie_played_chip = itemView.findViewById(R.id.theme_number_movie_played_chip);
        this.best_score_chip = itemView.findViewById(R.id.theme_best_score_chip);
        this.context = context;
    }

    public void bind(final GameParameters parameters, ThemePlayed themePlayed, Activity activity, boolean finish_activity) {
        theme_title.setText(context.getResources().getString(parameters.getIdName()));
        theme_image.setImageResource(parameters.getIdImage());

        number_movie_played_chip.setText(themePlayed.getNumber_movie_played() + "/" + themePlayed.getExpected_movie_number());
        if (themePlayed.getBest_score() == 0) {
            best_score_chip.setVisibility(View.GONE);
        } else {
            best_score_chip.setText(themePlayed.getBest_score().toString());
        }

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (finish_activity) {
                    activity.finish();
                }
                MovieGameContainerActivity.start(view.getContext(), parameters);
            }

        });
    }
}
