package com.matthieu_louf.movie_blindtest_app.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.matthieu_louf.movie_blindtest_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SeenMoviesService {
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String moviesDataString = String.valueOf(R.string.SEEN_MOVIES_KEY);

    public SeenMoviesService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("SEEN_MOVIES", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public boolean isSeen(int movieId) {
        List<Integer> seenMovies = this.getSeenMovies();
        return seenMovies.contains(movieId);
    }

    public void addSeenMovies(int movieId) {
        List<Integer> seenMovies = this.getSeenMovies();
        if(!seenMovies.contains(movieId))
        {
            seenMovies.add(movieId);
            saveSeenMovies(seenMovies);
        }
    }

    public void removeSeenMovies(int movieId) {
        List<Integer> seenMovies = this.getSeenMovies();
        if(seenMovies.contains(movieId))
        {
            seenMovies.remove(seenMovies.indexOf(movieId));
            saveSeenMovies(seenMovies);
        }
    }

    public void removeAllSeenMovies()
    {
        List<Integer> list = new ArrayList<Integer>();
        saveSeenMovies(list);
    }

    public List<Integer> getSeenMovies() {
        String json = sharedPreferences.getString(this.moviesDataString, "");

        if (json.isEmpty()) {
            return new ArrayList<Integer>();
        } else {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> arrPackageData = gson.fromJson(json, type);
            return arrPackageData;
        }
    }

    private void saveSeenMovies(List<Integer> seenMovies)
    {
        String json = gson.toJson(seenMovies);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(this.moviesDataString,json );
        editor.commit();
    }
}
