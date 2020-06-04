package com.matthieu_louf.movie_blindtest_app.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

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
        return Stream.of(allThemePlayed).filter(theme->theme.getId().equals(themePlayedId)).findFirst().isPresent();
    }

    public ThemePlayed initThemePlayed(int themePlayedId,int numberPages)
    {
        ThemePlayed themePlayed = new ThemePlayed(themePlayedId,numberPages*20);
        addThemePlayed(themePlayed);
        return themePlayed;
    }

    public void addThemePlayed(ThemePlayed themePlayed) {
        List<ThemePlayed> allThemePlayed = this.getAllThemePlayed();
        if(!Stream.of(allThemePlayed).filter(theme->theme.getId().equals(themePlayed.getId())).findFirst().isPresent())
        {
            allThemePlayed.add(themePlayed);
            saveAllThemePlayed(allThemePlayed);
        }
    }

    public void removeAllThemePlayed()
    {
        List<ThemePlayed> list = new ArrayList<ThemePlayed>();
        saveAllThemePlayed(list);
    }

    public ThemePlayed getOneThemePlayed(int themePlayedId, int numberPages)
    {
        List<ThemePlayed> allThemePlayed = getAllThemePlayed();
        ThemePlayed themePlayed = Stream.of(allThemePlayed).filter(theme->theme.getId().equals(themePlayedId)).findFirst().get();
        if(themePlayed==null)
        {
            themePlayed = initThemePlayed(themePlayedId,numberPages);
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

    private void saveAllThemePlayed(List<ThemePlayed> allThemePlayed)
    {
        String json = gson.toJson(allThemePlayed);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(this.allThemePlayedDataString,json );
        editor.commit();
    }
}
