package com.matthieu_louf.movie_blindtest_app.recycler.languagePreference;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.ProductionCompany;

public class LanguagePreferenceHolder extends RecyclerView.ViewHolder {
    public TextView languagePreferenceTextView;
    public TextView positionTextView;
    public CardView cardView;

    public LanguagePreferenceHolder(@NonNull View itemView, Context context) {
        super(itemView);
        languagePreferenceTextView = itemView.findViewById(R.id.language_preference_text_view);
        positionTextView = itemView.findViewById(R.id.language_preference_position_text_view);
        cardView = itemView.findViewById(R.id.language_preference_card_view);
        cardView.setBackgroundColor(context.getResources().getColor(R.color.lt_grey));
    }

    public void bind(final String languagePreferenceText, int position) {
        languagePreferenceTextView.setText(languagePreferenceText);
        updatePosition(position + 1);
    }

    public void updatePosition(int position) {
        positionTextView.setText(position + " - ");
    }
}
