package com.matthieu_louf.movie_blindtest_app.ui.blindtest;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.model.BlindtestParameters;
import com.matthieu_louf.movie_blindtest_app.recycler.ThemeAdapter;
import com.matthieu_louf.movie_blindtest_app.recycler.ThemeViewHolder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class FinishPageFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_NUMBER_GUESSES = "number_guesses";
    private static final String ARG_BLINDTEST_PARAMETERS = "blindtest_parameters";

    private Integer number_guesses;
    private BlindtestParameters blindtestParameters;

    RecyclerView recyclerView;
    TextView resultSentenceTextView;
    MaterialButton exitButton;

    public FinishPageFragment() {
    }

    public static FinishPageFragment newInstance(Integer number_guesses, BlindtestParameters blindtestParameters) {
        FinishPageFragment fragment = new FinishPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUMBER_GUESSES, number_guesses);
        args.putSerializable(ARG_BLINDTEST_PARAMETERS,blindtestParameters);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            number_guesses = getArguments().getInt(ARG_NUMBER_GUESSES);
            blindtestParameters = (BlindtestParameters) getArguments().getSerializable(ARG_BLINDTEST_PARAMETERS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_finish_page, container, false);
        resultSentenceTextView = root.findViewById(R.id.finish_result_text);
        resultSentenceTextView.setText(getText(R.string.result_text_beginning)+" "+number_guesses+"/10 "+getText(R.string.result_text_end));

        LinearLayoutManager linearLayoutManager = null;
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        recyclerView = root.findViewById(R.id.recycler_view_theme_cards);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<BlindtestParameters> parametersList = new ArrayList<BlindtestParameters>();
        parametersList.add(this.blindtestParameters);

        RecyclerView.Adapter themeAdapter = new ThemeAdapter(parametersList, R.layout.preview_theme_card, getContext());
        recyclerView.setAdapter(themeAdapter);

        exitButton = root.findViewById(R.id.finish_exit_button);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity =getActivity();
                if(activity!=null)
                {
                    activity.onBackPressed();
                }
            }
        });
        return root;
    }
}
