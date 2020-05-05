package com.matthieu_louf.movie_blindtest_app.recycler.theme;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.BlindtestParameters;
import com.matthieu_louf.movie_blindtest_app.pages.blindtestPage.BlindtestMovieActivity;

public class ThemeViewHolder extends RecyclerView.ViewHolder {

    private TextView theme_title;
    private ImageView theme_image;
    private Context context;
    private TextView theme_movie_number;

    ThemeViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.theme_title = itemView.findViewById(R.id.theme_title);
        this.theme_image = itemView.findViewById(R.id.theme_image);
        this.theme_movie_number = itemView.findViewById(R.id.theme_movie_number);
        this.context = context;
    }

    public void bind(final BlindtestParameters parameters,Activity activity,boolean finish_activity) {
        theme_title.setText(context.getResources().getString(parameters.getIdName()));
        theme_image.setImageResource(parameters.getIdImage());
        Integer movie_number = parameters.getMaximumPage()*20;
        theme_movie_number.setText(movie_number.toString());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finish_activity)
                {
                    activity.finish();
                }
                BlindtestMovieActivity.start(view.getContext(),parameters);
            }

        });
    }
}
