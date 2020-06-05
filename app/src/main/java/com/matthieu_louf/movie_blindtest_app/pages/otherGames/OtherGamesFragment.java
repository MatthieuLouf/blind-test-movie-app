package com.matthieu_louf.movie_blindtest_app.pages.otherGames;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.matthieu_louf.movie_blindtest_app.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OtherGamesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherGamesFragment extends Fragment {

    public OtherGamesFragment() {
    }

    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_games, container, false);
    }
}