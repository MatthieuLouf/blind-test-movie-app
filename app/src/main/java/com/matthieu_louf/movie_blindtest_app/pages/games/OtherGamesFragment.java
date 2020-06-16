package com.matthieu_louf.movie_blindtest_app.pages.games;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.GameType;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;
import com.matthieu_louf.movie_blindtest_app.recycler.theme.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;

public class OtherGamesFragment extends Fragment {

    private GameParameters[] gameParameters = new GameParameters[]{
            new GameParameters(1000, GameType.GOOD_OR_BAD, R.string.good_or_bad_game, R.mipmap.good_or_bad, 10, "vote_average.asc", "", "", "", "","","", "")
    };

    private Button start_good_or_bad_game_button;

    private ThemeAdapter themeAdapter;
    private RecyclerView recyclerView;

    public void onStart() {
        super.onStart();
        for (int i = 0; i < gameParameters.length; i++) {
            themeAdapter.notifyItemChanged(i);
        }
    }

    public OtherGamesFragment() {
    }

    public static OtherGamesFragment newInstance(String param1, String param2) {
        OtherGamesFragment fragment = new OtherGamesFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_other_games, container, false);
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