package com.example.moviedb_app.data;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviedb_app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserLikeService {
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String userDataString = String.valueOf(R.string.USER_LIKE_KEY);

    public UserLikeService(AppCompatActivity activity) {
        Context context = activity;
        this.sharedPreferences = context.getSharedPreferences("USER", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public boolean isLiked(int movieId) {
        List<Integer> likedMovies = this.getLikes();
        return likedMovies.contains(movieId);
    }

    public void addLike(int movieId) {
        List<Integer> likedMovies = this.getLikes();
        if(!likedMovies.contains(movieId))
        {
            likedMovies.add(movieId);
            saveLikes(likedMovies);
        }
    }

    public void removeLike(int movieId) {
        List<Integer> likedMovies = this.getLikes();
        if(likedMovies.contains(movieId))
        {
            likedMovies.remove(likedMovies.indexOf(movieId));
            saveLikes(likedMovies);
        }
    }

    public List<Integer> getLikes() {
        String json = sharedPreferences.getString(this.userDataString, "");

        if (json.isEmpty()) {
            return new ArrayList<Integer>();
        } else {
            Type type = new TypeToken<List<Integer>>() {
            }.getType();
            List<Integer> arrPackageData = gson.fromJson(json, type);
            return arrPackageData;
        }
    }

    private void saveLikes(List<Integer> likedMovies)
    {
        String json = gson.toJson(likedMovies);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(this.userDataString,json );
        editor.commit();
    }
}
