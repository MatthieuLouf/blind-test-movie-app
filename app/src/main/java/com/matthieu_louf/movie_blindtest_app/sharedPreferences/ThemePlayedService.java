package com.matthieu_louf.movie_blindtest_app.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.annimon.stream.Optional;
import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.sharedPreferences.ThemePlayed;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ThemePlayedService {
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private String allThemePlayedDataString = String.valueOf(R.string.THEME_PLAYED_KEY);

    public ThemePlayedService(Context context) {
        this.sharedPreferences = context.getSharedPreferences("THEME_PLAYED", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public boolean isThemePlayed(int themePlayedId) {
        List<ThemePlayed> allThemePlayed = this.getAllThemePlayed();
        return Stream.of(allThemePlayed).filter(theme -> theme.getId().equals(themePlayedId)).findFirst().isPresent();
    }

    public ThemePlayed initThemePlayed(int themePlayedId, int numberPages) {
        ThemePlayed themePlayed = new ThemePlayed(themePlayedId, numberPages * 20);
        addThemePlayed(themePlayed);
        return themePlayed;
    }

    public void addThemePlayed(ThemePlayed themePlayed) {
        List<ThemePlayed> allThemePlayed = this.getAllThemePlayed();
        if (!Stream.of(allThemePlayed).filter(theme -> theme.getId().equals(themePlayed.getId())).findFirst().isPresent()) {
            allThemePlayed.add(themePlayed);
            saveAllThemePlayed(allThemePlayed);
        }
    }

    public void removeAllThemePlayed() {
        List<ThemePlayed> list = new ArrayList<ThemePlayed>();
        saveAllThemePlayed(list);
    }

    public void finishBlindTest(int themePlayedId, int score, int number_guesses,int numberPages)
    {
        ThemePlayed themePlayed = getOneThemePlayed(themePlayedId,numberPages);
        themePlayed.setNumber_blind_tests_played(themePlayed.getNumber_blind_tests_played()+1);
        if(themePlayed.getBest_number_guesses()<number_guesses)
        {
            themePlayed.setBest_number_guesses(number_guesses);
        }
        if(themePlayed.getBest_score()<score)
        {
            themePlayed.setBest_score(score);
        }
        updateThemePlayed(themePlayed);
    }

    public void incrementExpectedMovieNumber(int incrementBy,int themePlayedId,int numberPages)
    {
        ThemePlayed themePlayed = getOneThemePlayed(themePlayedId,numberPages);
        if(themePlayed.getExpected_movie_number()+incrementBy>=themePlayed.getNumber_movie_played())
        {
            themePlayed.setExpected_movie_number(themePlayed.getExpected_movie_number()+incrementBy);
            updateThemePlayed(themePlayed);
        }
    }

    public void incrementPlayedMovieNumber(int incrementBy,int themePlayedId,int numberPages)
    {
        ThemePlayed themePlayed = getOneThemePlayed(themePlayedId,numberPages);
        if(themePlayed.getExpected_movie_number()>=themePlayed.getNumber_movie_played()+incrementBy)
        {
            themePlayed.setNumber_movie_played(themePlayed.getNumber_movie_played()+incrementBy);
            updateThemePlayed(themePlayed);
        }
    }

    public void updateThemePlayed(ThemePlayed themePlayed)
    {
        removeThemePlayed(themePlayed.getId());
        addThemePlayed(themePlayed);
    }

    public void removeThemePlayed(int themePlayedId) {
        List<ThemePlayed> allThemePlayed = this.getAllThemePlayed();
        if(Stream.of(allThemePlayed).filter(theme -> theme.getId().equals(themePlayedId)).findFirst().isPresent())
        {
            allThemePlayed.remove(Stream.of(allThemePlayed).filter(theme -> theme.getId().equals(themePlayedId)).findFirst().get());
            saveAllThemePlayed(allThemePlayed);
        }
    }

    public ThemePlayed getOneThemePlayed(int themePlayedId, int numberPages) {
        List<ThemePlayed> allThemePlayed = getAllThemePlayed();
        ThemePlayed themePlayed;
        Optional<ThemePlayed> optionalThemePlayed = Stream.of(allThemePlayed).filter(theme -> theme.getId().equals(themePlayedId)).findFirst();
        if (optionalThemePlayed.isPresent()) {
            themePlayed = optionalThemePlayed.get();
        } else {
            themePlayed = initThemePlayed(themePlayedId, numberPages);
        }
        return themePlayed;
    }

    public List<ThemePlayed> getAllThemePlayed() {
        String json = sharedPreferences.getString(this.allThemePlayedDataString, "");

        if (json.isEmpty()) {
            return new ArrayList<ThemePlayed>();
        } else {
            Type type = new TypeToken<List<ThemePlayed>>() {
            }.getType();
            List<ThemePlayed> arrPackageData = gson.fromJson(json, type);
            return arrPackageData;
        }
    }

    private void saveAllThemePlayed(List<ThemePlayed> allThemePlayed) {
        String json = gson.toJson(allThemePlayed);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(this.allThemePlayedDataString, json);
        editor.commit();
    }
}
