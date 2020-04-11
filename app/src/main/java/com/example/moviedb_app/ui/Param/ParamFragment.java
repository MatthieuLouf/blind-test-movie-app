package com.example.moviedb_app.ui.Param;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.moviedb_app.R;
import com.example.moviedb_app.model.BlindtestParameters;
import com.example.moviedb_app.ui.blindtest.BlindtestMovieActivity;
import com.google.android.material.badge.BadgeDrawable;

import java.lang.reflect.Array;
import java.util.Calendar;

public class ParamFragment extends Fragment {

    private NumberPicker numberPickerMinYear;
    private NumberPicker numberPickerMaxYear;
    private Spinner sortBySpinner;
    private Spinner numberMoviesSpinner;

    private Button startBlindtestButton;

    String[] sort_by_array = new String[]{"vote_average.desc", "popularity.desc", "revenue.desc"};
    String[] sort_by_display;
    String[] numberMoviesDisplay = new String[10];

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_param, container, false);

        startBlindtestButton = root.findViewById(R.id.params_start);
        numberPickerMinYear = root.findViewById(R.id.number_picker_min_year);
        numberPickerMaxYear = root.findViewById(R.id.number_picker_max_year);
        sortBySpinner = root.findViewById(R.id.params_order_spinner);
        numberMoviesSpinner = root.findViewById(R.id.params_number_spinner);

        setSpinners();
        setNumberPickers();

        startBlindtestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String releaseDateGTE = numberPickerMinYear.getValue() + "0-01-01";
                String releaseDateLTE = numberPickerMaxYear.getValue() + "0-12-31";
                String sort_by = sort_by_array[sortBySpinner.getSelectedItemPosition()];
                Integer maximumPage = Integer.parseInt(numberMoviesDisplay[numberMoviesSpinner.getSelectedItemPosition()+1])/20;

                BlindtestParameters blindtestParameters = new BlindtestParameters(R.string.param,
                        R.mipmap.infiltres,
                        maximumPage,
                        sort_by,
                        releaseDateGTE,
                        releaseDateLTE,
                        "",
                        "");

                BlindtestMovieActivity.start(getContext(), blindtestParameters);
            }
        });

        return root;
    }

    private void setNumberPickers() {
        numberPickerMinYear.setMinValue(195);
        numberPickerMinYear.setMaxValue(201);
        numberPickerMinYear.setValue(198);

        String[] display_table = new String[(201 - 195) + 1];
        for (int i = 0; i < (201 - 195) + 1; i++) {
            Integer inte = 195 + i;
            display_table[i] = inte.toString() + "0";
        }
        numberPickerMinYear.setDisplayedValues(display_table);

        numberPickerMaxYear.setMinValue(196);
        numberPickerMaxYear.setMaxValue(202);
        numberPickerMaxYear.setValue(202);

        display_table = new String[(202 - 196) + 1];
        for (int i = 0; i < (202 - 196) + 1; i++) {
            Integer inte = 196 + i;
            display_table[i] = inte.toString() + "0";
        }
        numberPickerMaxYear.setDisplayedValues(display_table);

        numberPickerMinYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPickerMaxYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
    }

    private void setSpinners()
    {
        for(int i =0;i<numberMoviesDisplay.length;i++)
        {
            numberMoviesDisplay[i]= String.valueOf((i+1)*20);
        }
        ArrayAdapter<String> adapterNumber = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, numberMoviesDisplay);
        numberMoviesSpinner.setAdapter(adapterNumber);
        numberMoviesSpinner.setSelection(1);

        sort_by_display= new String[] {getString(R.string.average_rate),
                getString(R.string.popularity),
                getString(R.string.revenue)};
        ArrayAdapter<String> adapterOrder = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, sort_by_display);
        sortBySpinner.setAdapter(adapterOrder);
    }
}


