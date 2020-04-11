package com.example.moviedb_app.ui.Param;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.ui.blindtest.BlindtestMovieActivity;
import com.google.android.material.badge.BadgeDrawable;

import java.util.Calendar;

public class ParamFragment extends Fragment {

    private NumberPicker numberPickerMinYear;
    private NumberPicker numberPickerMaxYear;

    private Button startBlindtestButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_param, container, false);

        startBlindtestButton = root.findViewById(R.id.params_start);
        numberPickerMinYear = root.findViewById(R.id.number_picker_min_year);
        numberPickerMaxYear = root.findViewById(R.id.number_picker_max_year);

        setNumberPickers();

        startBlindtestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String releaseDateGTE = numberPickerMinYear.getValue() + "0-01-01";
                String releaseDateLTE = numberPickerMaxYear.getValue() + "0-12-31";

                BlindtestParameters blindtestParameters = new BlindtestParameters(R.string.param,
                        R.mipmap.infiltres,
                        5,
                        "vote_average.desc",
                        releaseDateGTE,
                        releaseDateLTE,
                        "",
                        "");

                BlindtestMovieActivity.start(getContext(),blindtestParameters);
            }
        });

        return root;
    }

    private void setNumberPickers()
    {
        numberPickerMinYear.setMinValue(195);
        numberPickerMinYear.setMaxValue(201);
        numberPickerMinYear.setValue(198);

        String[] display_table = new String[(201-195)+1];
        for(int i=0;i<(201-195)+1;i++)
        {
            Integer inte = 195+i;
            display_table[i] = inte.toString()+"0";
        }
        numberPickerMinYear.setDisplayedValues(display_table);

        numberPickerMaxYear.setMinValue(196);
        numberPickerMaxYear.setMaxValue(202);
        numberPickerMaxYear.setValue(202);

        display_table = new String[(202-196)+1];
        for(int i=0;i<(202-196)+1;i++)
        {
            Integer inte = 196+i;
            display_table[i] = inte.toString()+"0";
        }
        numberPickerMaxYear.setDisplayedValues(display_table);

        numberPickerMinYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPickerMaxYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }
}


