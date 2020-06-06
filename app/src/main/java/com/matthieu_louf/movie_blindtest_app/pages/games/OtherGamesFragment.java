package com.matthieu_louf.movie_blindtest_app.pages.games;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.GameType;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;

public class OtherGamesFragment extends Fragment {

    private Button start_good_or_bad_game_button;

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

        start_good_or_bad_game_button = root.findViewById(R.id.start_good_or_bad_button);
        start_good_or_bad_game_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameParameters gameParameters = new GameParameters(1000,GameType.GOOD_OR_BAD,R.string.good_or_bad_game,R.mipmap.grand_bain,5,"vote_average.asc", "", "", "", "", "");
                MovieGameContainerActivity.start(getContext(), gameParameters);
            }
        });
        return root;
    }
}