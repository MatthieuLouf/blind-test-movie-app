package com.matthieu_louf.movie_blindtest_app.model;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class GenrePageResult implements Serializable {

    @SerializedName("genres")
    private List<Genre> genres = null;
    private final static long serialVersionUID = -9005308834626377011L;

    /**
     * @param genres
     */
    public GenrePageResult(List<Genre> genres) {
        super();
        this.genres = genres;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }
}