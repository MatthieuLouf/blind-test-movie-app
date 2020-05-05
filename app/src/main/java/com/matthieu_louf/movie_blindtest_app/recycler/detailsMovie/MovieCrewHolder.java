package com.matthieu_louf.movie_blindtest_app.recycler.detailsMovie;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.Crew;

public class MovieCrewHolder extends RecyclerView.ViewHolder {
    private final String BASE_IMAGE_URL = "https://image.tmdb.org/t/p/w92/";
    private TextView crewName;
    private TextView crewRole;
    private ImageView crewImage;
    private ProgressBar progressBar;

    MovieCrewHolder(@NonNull View itemView) {
        super(itemView);
        crewImage = itemView.findViewById(R.id.movie_details_person_image);
        crewName = itemView.findViewById(R.id.movie_details_person_name);
        crewRole = itemView.findViewById(R.id.movie_details_person_role);
        progressBar = itemView.findViewById(R.id.movie_details_progress_circular);
    }

    public void bind(final Crew crew) {
        crewName.setText(crew.getName());
        crewRole.setText("-> " + crew.getJob());

        if (crew.getProfilePath() != "null") {
            Glide.with(itemView)
                    .applyDefaultRequestOptions(new RequestOptions()
                            .error(R.drawable.default_cast_or_crew))
                    .load(BASE_IMAGE_URL + crew.getProfilePath()).listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    progressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(crewImage);
        } else {
            progressBar.setVisibility(View.GONE);
        }


    }
}
