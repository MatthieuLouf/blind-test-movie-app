package com.example.moviedb_app.ui.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Display;
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
            new BlindtestParameters(R.string.top_rated_cardname, R.mipmap.infiltres, 5, "vote_average.desc", "", "", "", ""),
            new BlindtestParameters(R.string.heigthies_cardname, R.mipmap.indiana_jones, 3, "vote_average.desc", "1980-01-01", "1989-12-31", "", "en"),
            new BlindtestParameters(R.string.japanese_animation_cardname, R.mipmap.your_name, 1, "vote_average.desc", "", "", "16", "ja"),
            new BlindtestParameters(R.string.french_2010_cardname, R.mipmap.grand_bain, 2, "vote_average.desc", "2010-01-01", "", "", "fr"),
            new BlindtestParameters(R.string.horror_cardname, R.mipmap.halloween, 3, "vote_average.desc", "1980-01-01", "", "27", ""),
            new BlindtestParameters(R.string.science_fiction_cardname, R.mipmap.star_wars, 4, "vote_average.desc", "1980-01-01", "", "878", "")
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