package com.example.moviedb_app.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.recycler.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private BlindtestParameters[] blindtestParameters = new BlindtestParameters[]{
            new BlindtestParameters(R.string.top_rated_cardname, R.mipmap.infiltres, 5, "vote_average.desc", "", "", "","", ""),
            new BlindtestParameters(R.string.heigthies_cardname, R.mipmap.indiana_jones, 3, "vote_average.desc", "1980-01-01", "1989-12-31", "","",  "en"),
            new BlindtestParameters(R.string.japanese_animation_cardname, R.mipmap.your_name, 2, "vote_average.desc", "", "", "16","",  "ja"),
            new BlindtestParameters(R.string.french_2010_cardname, R.mipmap.grand_bain, 2, "vote_average.desc", "2010-01-01", "", "","",  "fr"),
            new BlindtestParameters(R.string.horror_cardname, R.mipmap.halloween, 3, "vote_average.desc", "1980-01-01", "", "27","",  ""),
            new BlindtestParameters(R.string.science_fiction_cardname, R.mipmap.star_wars, 4, "vote_average.desc", "1980-01-01", "", "878","",  ""),
            new BlindtestParameters(R.string.war_cardname, R.mipmap.war, 4, "vote_average.desc", "1960-01-01", "", "10752","",  ""),
            new BlindtestParameters(R.string.crime_thriller_cardname, R.mipmap.thriller_crime, 5, "vote_average.desc", "1960-01-01", "", "80,53","",  ""),
            new BlindtestParameters(R.string.american_comedy_cardname, R.mipmap.comedy, 3, "vote_average.desc", "1980-01-01", "", "35","16",  "en"),
            new BlindtestParameters(R.string.western_cardname, R.mipmap.western, 2, "vote_average.desc", "1960-01-01", "", "37","",  ""),
            new BlindtestParameters(R.string.american_animation_cardname, R.mipmap.american_animation, 4, "vote_average.desc", "1930-01-01", "", "16","",  "en"),
            new BlindtestParameters(R.string.romance_cardname, R.mipmap.romance, 3, "vote_average.desc", "1980-01-01", "", "10749","",  ""),
            new BlindtestParameters(R.string.american_movies_30_60_cardname, R.mipmap.american_30_60, 3, "vote_average.desc", "1930-01-01", "1959-12-31", "","",  "en"),
            new BlindtestParameters(R.string.action_movies_90s_cardname, R.mipmap.action_90s, 2, "vote_average.desc", "1990-01-01", "1999-12-31", "28","",  ""),
            new BlindtestParameters(R.string.worst_movies_cardname, R.mipmap.worst_movie, 5, "vote_average.asc", "1980-01-01", "", "","",  "")
    };

    private ThemeAdapter themeAdapter;
    private RecyclerView recyclerView;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayoutManager linearLayoutManager = null;
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        recyclerView = root.findViewById(R.id.recycler_view_theme_cards);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<BlindtestParameters> parametersList = new ArrayList<BlindtestParameters>();

        for (int i = 0; i < blindtestParameters.length; i++) {
            parametersList.add(blindtestParameters[i]);
        }

        themeAdapter = new ThemeAdapter(parametersList, R.layout.preview_theme_card, getContext());
        recyclerView.setAdapter(themeAdapter);

        return root;
    }
}