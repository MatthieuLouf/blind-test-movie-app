package com.matthieu_louf.movie_blindtest_app.pages.homePage;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.BlindtestParameters;
import com.matthieu_louf.movie_blindtest_app.recycler.theme.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private BlindtestParameters[] blindtestParameters = new BlindtestParameters[]{
            new BlindtestParameters(1, R.string.top_rated_cardname, R.mipmap.infiltres, 5, "vote_average.desc", "", "", "", "", ""),
            new BlindtestParameters(2, R.string._2020, R.mipmap.birds_of_prey, 3, "popularity.desc", "2020-01-01", "2020-12-31", "", "", ""),
            new BlindtestParameters(3, R.string._2019, R.mipmap.parasite, 3, "popularity.desc", "2019-01-01", "2019-12-31", "", "", ""),
            new BlindtestParameters(4, R.string.science_fiction_cardname, R.mipmap.star_wars, 4, "vote_average.desc", "1980-01-01", "", "878", "", ""),
            new BlindtestParameters(5, R.string.war_cardname, R.mipmap.war, 4, "vote_average.desc", "1960-01-01", "", "10752", "", ""),
            new BlindtestParameters(6, R.string.crime_thriller_cardname, R.mipmap.thriller_crime, 5, "vote_average.desc", "1960-01-01", "", "80,53", "", ""),
            new BlindtestParameters(7, R.string.american_comedy_cardname, R.mipmap.comedy, 3, "vote_average.desc", "1980-01-01", "", "35", "16", "en"),
            new BlindtestParameters(8, R.string.romance_cardname, R.mipmap.romance, 3, "vote_average.desc", "1980-01-01", "", "10749", "", ""),
            new BlindtestParameters(9, R.string.western_cardname, R.mipmap.western, 2, "vote_average.desc", "1960-01-01", "", "37", "", ""),
            new BlindtestParameters(10, R.string.horror_cardname, R.mipmap.halloween, 3, "vote_average.desc", "1980-01-01", "", "27", "", ""),
            new BlindtestParameters(11, R.string.american_animation_cardname, R.mipmap.american_animation, 4, "vote_average.desc", "1930-01-01", "", "16", "", "en"),
            new BlindtestParameters(12, R.string.japanese_animation_cardname, R.mipmap.your_name, 2, "vote_average.desc", "", "", "16", "10770", "ja"),
            new BlindtestParameters(13, R.string.french_2010_cardname, R.mipmap.grand_bain, 2, "vote_average.desc", "2010-01-01", "", "", "", "fr"),
            new BlindtestParameters(14, R.string.heigthies_cardname, R.mipmap.indiana_jones, 3, "vote_average.desc", "1980-01-01", "1989-12-31", "", "", "en"),
            new BlindtestParameters(15, R.string.american_movies_30_60_cardname, R.mipmap.american_30_60, 3, "vote_average.desc", "1930-01-01", "1959-12-31", "", "", "en"),
            new BlindtestParameters(16, R.string.action_movies_90s_cardname, R.mipmap.action_90s, 2, "vote_average.desc", "1990-01-01", "1999-12-31", "28", "", ""),
            new BlindtestParameters(17, R.string.worst_movies_cardname, R.mipmap.worst_movie, 5, "vote_average.asc", "1980-01-01", "", "", "", "")
    };

    private ThemeAdapter themeAdapter;
    private RecyclerView recyclerView;

    public void onStart() {
        super.onStart();
        themeAdapter.notifyDataSetChanged();
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
        List<BlindtestParameters> parametersList = new ArrayList<BlindtestParameters>();

        for (int i = 0; i < blindtestParameters.length; i++) {
            parametersList.add(blindtestParameters[i]);
        }

        themeAdapter = new ThemeAdapter(parametersList, R.layout.preview_theme_card, getActivity(), false);
        recyclerView.setAdapter(themeAdapter);
        return root;
    }

    private void loadThemeList()
    {
    }
}