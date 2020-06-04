package com.matthieu_louf.movie_blindtest_app.models.video;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VideoPageResult {

    @SerializedName("id")
    private Integer id;
    @SerializedName("results")
    private List<Video> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Video> getResults() {
        return results;
    }

    public void setResults(List<Video> results) {
        this.results = results;
    }

}