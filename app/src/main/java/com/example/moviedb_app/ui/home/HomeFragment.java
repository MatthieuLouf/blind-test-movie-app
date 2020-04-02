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
import com.example.moviedb_app.ui.blindtest.BlindtestMovieActivity;


public class HomeFragment extends Fragment {

    private Button button_start;

    public HomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        button_start = root.findViewById(R.id.start_blindtest);

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BlindtestMovieActivity.class);
                v.getContext().startActivity(intent);
            }
        });
        return root;
    }
}