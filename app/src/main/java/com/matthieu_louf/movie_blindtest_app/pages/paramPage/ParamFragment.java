package com.matthieu_louf.movie_blindtest_app.pages.paramPage;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.matthieu_louf.movie_blindtest_app.R;
import com.matthieu_louf.movie_blindtest_app.firebase.FirebaseLog;
import com.matthieu_louf.movie_blindtest_app.models.blindtest.BlindtestParameters;
import com.matthieu_louf.movie_blindtest_app.models.genre.Genre;
import com.matthieu_louf.movie_blindtest_app.api.MovieAPIHelper;
import com.matthieu_louf.movie_blindtest_app.pages.blindtestPage.BlindtestMovieActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParamFragment extends Fragment {

    private NumberPicker numberPickerMinYear;
    private NumberPicker numberPickerMaxYear;
    private Spinner sortBySpinner;
    private Spinner numberMoviesSpinner;
    private ChipGroup genresChipGroup;
    private ChipGroup languagesChipGroup;

    private MaterialButton startBlindtestButton;

    private String[] sort_by_array = new String[]{"vote_average.desc", "popularity.desc", "revenue.desc"};
    private String[] sort_by_display;
    private String[] numberMoviesDisplay = new String[10];
    private List<Genre> genreList = new ArrayList<Genre>();
    private String[] languageKeyArray = new String[]{"en", "fr", "ja", "es", "it"};

    private FirebaseLog firebaseLog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_param, container, false);

        firebaseLog = new FirebaseLog(getContext());

        startBlindtestButton = root.findViewById(R.id.params_start);
        numberPickerMinYear = root.findViewById(R.id.number_picker_min_year);
        numberPickerMaxYear = root.findViewById(R.id.number_picker_max_year);
        sortBySpinner = root.findViewById(R.id.params_order_spinner);
        numberMoviesSpinner = root.findViewById(R.id.params_number_spinner);
        genresChipGroup = root.findViewById(R.id.params_genres_chip_group);
        languagesChipGroup = root.findViewById(R.id.params_language_chip_group);

        setSpinners();
        setNumberPickers();
        setGenresChipGroup();
        setLanguagesChipGroup();

        startBlindtestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String releaseDateGTE = numberPickerMinYear.getValue() + "0-01-01";
                String releaseDateLTE = numberPickerMaxYear.getValue() + "0-12-31";
                String sort_by = sort_by_array[sortBySpinner.getSelectedItemPosition()];
                Integer maximumPage = Integer.parseInt(numberMoviesDisplay[numberMoviesSpinner.getSelectedItemPosition() + 1]) / 20;

                String genres = "";
                for (int i = 0; i < genresChipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) genresChipGroup.getChildAt(i);
                    if (chip.isChecked()) {
                        genres += genreList.get(i).getId();
                        if (i != genresChipGroup.getChildCount() - 1) {
                            genres += ",";
                        }
                    }
                }

                String language = "";
                for (int i = 0; i < languagesChipGroup.getChildCount(); i++) {
                    Chip chip = (Chip) languagesChipGroup.getChildAt(i);
                    if (chip.isChecked()) {
                        language = languageKeyArray[i];
                        break;
                    }
                }

                BlindtestParameters blindtestParameters = new BlindtestParameters(
                        0,
                        R.string.param,
                        R.mipmap.infiltres,
                        maximumPage,
                        sort_by,
                        releaseDateGTE,
                        releaseDateLTE,
                        genres,
                        "",
                        language);

                firebaseLog.startCustomBlindtest(blindtestParameters);

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

    private void setSpinners() {
        for (int i = 0; i < numberMoviesDisplay.length; i++) {
            numberMoviesDisplay[i] = String.valueOf((i + 1) * 20);
        }
        ArrayAdapter<String> adapterNumber = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, numberMoviesDisplay);
        numberMoviesSpinner.setAdapter(adapterNumber);
        numberMoviesSpinner.setSelection(1);

        sort_by_display = new String[]{getString(R.string.average_rate),
                getString(R.string.popularity),
                getString(R.string.revenue)};
        ArrayAdapter<String> adapterOrder = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, sort_by_display);
        sortBySpinner.setAdapter(adapterOrder);
    }

    private void setGenresChipGroup() {
        MovieAPIHelper movieAPIHelper = new MovieAPIHelper(getContext());
        movieAPIHelper.scrapGenres(getContext(), new Callback<List<Genre>>() {
            @Override
            public void onResponse(Call<List<Genre>> call, Response<List<Genre>> response) {
                genreList.addAll(response.body());
                for (Genre genre : genreList) {
                    if (genre.getId() != 99 && getContext() != null) {
                        Chip chip = new Chip(getContext());
                        ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
                        chip.setChipDrawable(chipDrawable);
                        chip.setText(genre.getName());
                        setOnCheckedChangeListener(chip, R.color.colorPrimary);

                        genresChipGroup.addView(chip);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Genre>> call, Throwable t) {

            }
        });
    }

    private void setLanguagesChipGroup() {
        String[] language_array = getResources().getStringArray(R.array.language_array);
        for (int i = 0; i < languageKeyArray.length; i++) {

            Chip chip = new Chip(getContext());

            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(getContext(), null, 0, R.style.Widget_MaterialComponents_Chip_Choice);
            chip.setChipDrawable(chipDrawable);
            chip.setText(language_array[i]);

            setOnCheckedChangeListener(chip, R.color.colorAccent);

            languagesChipGroup.addView(chip);
        }
    }

    private void setOnCheckedChangeListener(Chip chip, int color) {
        chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    chip.setChipBackgroundColorResource(color);
                    chip.setTextColor(Color.WHITE);
                } else {
                    chip.setChipBackgroundColorResource(R.color.lt_grey);
                    chip.setTextColor(Color.BLACK);
                }
            }
        });
    }
}


