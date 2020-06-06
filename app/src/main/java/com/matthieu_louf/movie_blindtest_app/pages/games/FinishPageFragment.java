package com.matthieu_louf.movie_blindtest_app.pages.games;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.models.GameType;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.GameParameters;
import com.matthieu_louf.movie_blindtest_app.recycler.theme.ThemeAdapter;

import java.util.ArrayList;
import java.util.List;

public class FinishPageFragment extends Fragment {
    private String TAG = "FinishPageFragment";

    private static final String ARG_NUMBER_GUESSES = "number_guesses";
    private static final String ARG_SCORE_TOTAL = "score_total";
    private static final String ARG_BLINDTEST_PARAMETERS = "blindtest_parameters";

    private Integer number_guesses;
    private Integer score_total;
    private GameParameters gameParameters;

    RecyclerView recyclerView;
    TextView resultSentenceTextView;
    TextView finish_next_text;
    MaterialButton exitButton;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    public FinishPageFragment() {
    }

    public static FinishPageFragment newInstance(Integer number_guesses, Integer score_total, GameParameters gameParameters) {
        FinishPageFragment fragment = new FinishPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER_GUESSES, number_guesses);
        args.putInt(ARG_SCORE_TOTAL, score_total);
        args.putSerializable(ARG_BLINDTEST_PARAMETERS, gameParameters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number_guesses = getArguments().getInt(ARG_NUMBER_GUESSES);
            score_total = getArguments().getInt(ARG_SCORE_TOTAL);
            gameParameters = (GameParameters) getArguments().getSerializable(ARG_BLINDTEST_PARAMETERS);
        }
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");

        View root = inflater.inflate(R.layout.fragment_finish_page, container, false);
        resultSentenceTextView = root.findViewById(R.id.finish_result_text);
        finish_next_text = root.findViewById(R.id.finish_next_text);
        resultSentenceTextView.setText(getString(R.string.result_text,
                getGameName(),
                number_guesses,
                "/" + mFirebaseRemoteConfig.getLong("movie_number_in_one_blindtest"),
                score_total,
                "/" + mFirebaseRemoteConfig.getLong("movie_number_in_one_blindtest") * mFirebaseRemoteConfig.getLong("score_maximum_value")));

        finish_next_text.setText(getString(R.string.result_next_text,getParameterType()));

        MovieGameContainerActivity activity = (MovieGameContainerActivity) getActivity();
        ActionBar ab = activity.getSupportActionBar();
        ab.setTitle(getString(gameParameters.getIdName()));

        LinearLayoutManager linearLayoutManager = null;
        if (Configuration.ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation) {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        } else {
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        }
        recyclerView = root.findViewById(R.id.recycler_view_theme_cards);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<GameParameters> parametersList = new ArrayList<GameParameters>();
        parametersList.add(this.gameParameters);

        RecyclerView.Adapter themeAdapter = new ThemeAdapter(parametersList, R.layout.preview_theme_card, getActivity(), true);
        recyclerView.setAdapter(themeAdapter);

        exitButton = root.findViewById(R.id.finish_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieGameContainerActivity activity = (MovieGameContainerActivity) getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        });
        return root;
    }

    public String getGameName()
    {
        String gameName="";
        switch (gameParameters.getGameType())
        {
            case GOOD_OR_BAD:
                gameName = getContext().getResources().getString(R.string.game);
                break;
            case BLIND_TEST:
                gameName ="Blind test";
                break;
        }
        return gameName;
    }

    public String getParameterType()
    {
        String gameName="";
        switch (gameParameters.getGameType())
        {
            case GOOD_OR_BAD:
                gameName = getContext().getResources().getString(R.string.game);
                break;
            case BLIND_TEST:
                gameName = getContext().getResources().getString(R.string.theme);
                break;
        }
        return gameName;
    }
}
