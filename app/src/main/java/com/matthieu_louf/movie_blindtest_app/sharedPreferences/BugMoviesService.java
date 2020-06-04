package com.matthieu_louf.movie_blindtest_app.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.matthieu_louf.movie_blindtest_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BugMoviesService {
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String moviesDataString = String.valueOf(R.string.BUG_MOVIES_KEY);

    public BugMoviesService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("BUG_MOVIES", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public boolean isBug(int movieId) {
        List<Integer> bugMovies = this.getBugMovies();
        return bugMovies.contains(movieId);
    }

    public void addBugMovies(int movieId) {
        List<Integer> bugMovies = this.getBugMovies();
        if (!bugMovies.contains(movieId)) {
            bugMovies.add(movieId);
            saveBugMovies(bugMovies);
        }
    }

    public void removeBugMovies(int movieId) {
        List<Integer> bugMovies = this.getBugMovies();
        if (bugMovies.contains(movieId)) {
            bugMovies.remove(bugMovies.indexOf(movieId));
            saveBugMovies(bugMovies);
        }
    }

    public void removeAllBugMovies() {
        List<Integer> list = new ArrayList<Integer>();
        saveBugMovies(list);
    }

    public List<Integer> getBugMovies() {
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

    private void saveBugMovies(List<Integer> bugMovies) {
        String json = gson.toJson(bugMovies);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(this.moviesDataString, json);
        editor.commit();
    }
}
