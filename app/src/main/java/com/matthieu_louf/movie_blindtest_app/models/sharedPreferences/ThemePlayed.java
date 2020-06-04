package com.matthieu_louf.movie_blindtest_app.models.sharedPreferences;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ThemePlayed implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("best_score")
    private Integer best_score;
    @SerializedName("best_number_guesses")
    private Integer best_number_guesses;
    @SerializedName("number_blind_tests_played")
    private Integer number_blind_tests_played;
    @SerializedName("number_movie_played")
    private Integer number_movie_played;
    @SerializedName("expected_movie_number")
    private Integer expected_movie_number;

    public ThemePlayed(Integer id, Integer best_score, Integer best_number_guesses, Integer number_blind_tests_played, Integer number_movie_played, Integer expected_movie_number) {
        this.id = id;
        this.best_score = best_score;
        this.best_number_guesses = best_number_guesses;
        this.number_blind_tests_played = number_blind_tests_played;
        this.number_movie_played = number_movie_played;
        this.expected_movie_number = expected_movie_number;
    }

    public ThemePlayed(Integer id, Integer expected_movie_number)
    {
        this.id = id;
        this.expected_movie_number =expected_movie_number;
        this.best_score = 0;
        this.best_number_guesses = 0;
        this.number_blind_tests_played = 0;
        this.number_movie_played = 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBest_score() {
        return best_score;
    }

    public void setBest_score(Integer best_score) {
        this.best_score = best_score;
    }

    public Integer getBest_number_guesses() {
        return best_number_guesses;
    }

    public void setBest_number_guesses(Integer best_number_guesses) {
        this.best_number_guesses = best_number_guesses;
    }

    public Integer getNumber_blind_tests_played() {
        return number_blind_tests_played;
    }

    public void setNumber_blind_tests_played(Integer number_blind_tests_played) {
        this.number_blind_tests_played = number_blind_tests_played;
    }

    public Integer getNumber_movie_played() {
        return number_movie_played;
    }

    public void setNumber_movie_played(Integer number_movie_played) {
        this.number_movie_played = number_movie_played;
    }

    public Integer getExpected_movie_number() {
        return expected_movie_number;
    }

    public void setExpected_movie_number(Integer expected_movie_number) {
        this.expected_movie_number = expected_movie_number;
    }
}