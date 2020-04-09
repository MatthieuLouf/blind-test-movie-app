package com.example.moviedb_app.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.ui.blindtest.BlindtestMovieActivity;


public class HomeFragment extends Fragment {

    private Button[] buttons = new Button[3];
    private int[] buttons_id = new int[]{
            R.id.card_top_rated,
            R.id.card_80s,
            R.id.card_japanese_animation
    };

    private BlindtestParameters[] blindtestParameters = new BlindtestParameters[]{
            new BlindtestParameters(R.string.top_rated_cardname,7,"vote_average.desc","","","",""),
            new BlindtestParameters(R.string.heigthies_cardname,5,"vote_average.desc","1980-01-01","1989-12-31","",""),
            new BlindtestParameters(R.string.japanese_animation_cardname,3,"vote_average.desc","","","16","ja")
    };

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        for(int i=0;i<3;i++)
        {
            buttons[i] = root.findViewById(buttons_id[i]);
            int finalI = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BlindtestMovieActivity.start(getContext(),blindtestParameters[finalI]);
                }
            });
        }
        return root;
    }
}