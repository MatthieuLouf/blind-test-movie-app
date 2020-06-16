package com.matthieu_louf.movie_blindtest_app.pages.homePage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.GameType;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;
import com.matthieu_louf.movie_blindtest_app.recycler.theme.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private GameParameters[] gameParameters = new GameParameters[]{
            new GameParameters(1, GameType.BLIND_TEST, R.string.top_rated_cardname, R.mipmap.infiltres, 4, "vote_average.desc", "", "", "", "","","", ""),
            // new BlindtestParameters(2, R.string._2020, R.mipmap.birds_of_prey, 2, "popularity.desc", "2020-01-01", "2020-12-31", "", "", ""),
            new GameParameters(3, GameType.BLIND_TEST, R.string._2019, R.mipmap.parasite, 2, "popularity.desc", "2019-01-01", "2019-12-31", "", "","","",  ""),
            new GameParameters(4, GameType.BLIND_TEST, R.string.science_fiction_cardname, R.mipmap.star_wars, 4, "vote_average.desc", "1980-01-01", "", "878", "","","",  ""),
            new GameParameters(5, GameType.BLIND_TEST, R.string.war_cardname, R.mipmap.war, 4, "vote_average.desc", "1960-01-01", "", "10752", "","","",  ""),
            new GameParameters(6, GameType.BLIND_TEST, R.string.crime_thriller_cardname, R.mipmap.thriller_crime, 4, "vote_average.desc", "1960-01-01", "", "80,53", "","","",  ""),
            new GameParameters(7, GameType.BLIND_TEST, R.string.american_comedy_cardname, R.mipmap.comedy, 2, "vote_average.desc", "1980-01-01", "", "35", "16","","",  "en"),
            new GameParameters(8, GameType.BLIND_TEST, R.string.romance_cardname, R.mipmap.romance, 2, "vote_average.desc", "1980-01-01", "", "10749", "","","",  ""),
            new GameParameters(9, GameType.BLIND_TEST, R.string.western_cardname, R.mipmap.western, 2, "vote_average.desc", "1960-01-01", "", "37", "","","",  ""),
            new GameParameters(10, GameType.BLIND_TEST, R.string.horror_cardname, R.mipmap.halloween, 2, "vote_average.desc", "1980-01-01", "", "27", "","","",  ""),
            new GameParameters(11, GameType.BLIND_TEST, R.string.american_animation_cardname, R.mipmap.american_animation, 4, "vote_average.desc", "1930-01-01", "", "16", "", "","", "en"),
            new GameParameters(12, GameType.BLIND_TEST, R.string.japanese_animation_cardname, R.mipmap.your_name, 2, "vote_average.desc", "", "", "16", "10770","","",  "ja"),
            new GameParameters(13, GameType.BLIND_TEST, R.string.french_2010_cardname, R.mipmap.grand_bain, 2, "vote_average.desc", "2010-01-01", "", "", "","","",  "fr"),
            new GameParameters(14, GameType.BLIND_TEST, R.string.heigthies_cardname, R.mipmap.indiana_jones, 4, "vote_average.desc", "1980-01-01", "1989-12-31", "", "","","",  "en"),
            new GameParameters(15, GameType.BLIND_TEST, R.string.american_movies_30_60_cardname, R.mipmap.american_30_60, 2, "vote_average.desc", "1930-01-01", "1959-12-31", "", "","","",  "en"),
            new GameParameters(16, GameType.BLIND_TEST, R.string.action_movies_90s_cardname, R.mipmap.action_90s, 2, "vote_average.desc", "1990-01-01", "1999-12-31", "28", "","","",  ""),
            new GameParameters(17, GameType.BLIND_TEST, R.string.worst_movies_cardname, R.mipmap.worst_movie, 4, "vote_average.asc", "1980-01-01", "", "", "","","",  "")
    };

    private ThemeAdapter themeAdapter;
    private RecyclerView recyclerView;

    public void onStart() {
        super.onStart();
        for (int i = 0; i < gameParameters.length; i++) {
            themeAdapter.notifyItemChanged(i);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view_theme_cards);
        int orientation = GridLayoutManager.VERTICAL;
        int spanCount = 1;
        if (getContext().getResources().getConfiguration().isLayoutSizeAtLeast(Configuration.SCREENLAYOUT_SIZE_LARGE)) {
            spanCount = 2;
        }
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            orientation = GridLayoutManager.HORIZONTAL;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount, orientation, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        List<GameParameters> parametersList = new ArrayList<GameParameters>();

        for (int i = 0; i < gameParameters.length; i++) {
            parametersList.add(gameParameters[i]);
        }

        themeAdapter = new ThemeAdapter(parametersList, R.layout.preview_theme_card, getActivity(), false);
        recyclerView.setAdapter(themeAdapter);
        return root;
    }
}