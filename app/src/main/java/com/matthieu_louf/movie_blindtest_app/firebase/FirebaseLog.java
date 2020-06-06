package com.matthieu_louf.movie_blindtest_app.firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;
import com.matthieu_louf.movie_blindtest_app.models.detailsMovie.MovieDetails;
import com.matthieu_louf.movie_blindtest_app.models.movie.Movie;

public class FirebaseLog {

    private FirebaseAnalytics mFirebaseAnalytics;
    private Context context;

    public FirebaseLog(Context context) {
        this.context = context;
        if (context != null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.context);
        }
    }

    public void startBlindtestEvent(int theme) {
        Bundle bundle = new Bundle();
        bundle.putInt("theme", theme);
        mFirebaseAnalytics.logEvent("start_blindtest", bundle);
    }

    public void newMovieSeenEvent(Movie movie) {
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, movie.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, movie.getOriginalTitle());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "movie");
        mFirebaseAnalytics.logEvent("new_movie_seen", bundle);
    }

    public void endBlindtestEvent(int guessed_movies_count, int points, int theme) {
        Bundle bundle = new Bundle();
        bundle.putInt("guessed_movies_count", guessed_movies_count);
        bundle.putInt("points", points);
        bundle.putInt("theme", theme);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "score");
        mFirebaseAnalytics.logEvent("blindtest_end", bundle);
    }

    public void startCustomBlindtest(GameParameters gameParameters) {
        Bundle bundle = new Bundle();
        bundle.putInt("maximum_page", gameParameters.getMaximumPage());
        bundle.putString("release_date_gte", gameParameters.getReleaseDateGTE());
        bundle.putString("release_date_lte", gameParameters.getReleaseDateLTE());
        bundle.putString("sort_by", gameParameters.getSortBy());
        bundle.putString("with_genres", gameParameters.getWithGenres());
        bundle.putString("with_original_language", gameParameters.getWithOriginalLanguage());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "customized_blindtest");
        mFirebaseAnalytics.logEvent("start_custom_blindtest", bundle);
    }

    public void removeSeenMovies() {
        Bundle bundle = new Bundle();
        mFirebaseAnalytics.logEvent("remove_seen_movies", bundle);
    }

    public void seeMovieDetails(MovieDetails movieDetails) {
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, movieDetails.getId());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "movie");
        mFirebaseAnalytics.logEvent("see_movie_details", bundle);
    }
}
