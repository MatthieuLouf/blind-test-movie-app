package com.example.moviedb_app.model;

import java.io.Serializable;

public class BlindtestParameters implements Serializable {
    private Integer idName;
    private Integer maximumPage;
    private String sortBy;
    private String releaseDateGTE;
    private String releaseDateLTE;
    private String withGenres;
    private String withOriginalLanguage;

    public BlindtestParameters(Integer idName, Integer maximumPage, String sortBy, String releaseDateGTE, String releaseDateLTE, String withGenres, String withOriginalLanguage) {
        this.idName = idName;
        this.maximumPage = maximumPage;
        this.sortBy = sortBy;
        this.releaseDateGTE = releaseDateGTE;
        this.releaseDateLTE = releaseDateLTE;
        this.withGenres = withGenres;
        this.withOriginalLanguage = withOriginalLanguage;
    }

    public Integer getIdName() {
        return idName;
    }

    public void setIdName(Integer idName) {
        this.idName = idName;
    }

    public Integer getMaximumPage() {
        return maximumPage;
    }

    public void setMaximumPage(Integer maximumPage) {
        this.maximumPage = maximumPage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getReleaseDateGTE() {
        return releaseDateGTE;
    }

    public void setReleaseDateGTE(String releaseDateGTE) {
        this.releaseDateGTE = releaseDateGTE;
    }

    public String getReleaseDateLTE() {
        return releaseDateLTE;
    }

    public void setReleaseDateLTE(String releaseDateLTE) {
        this.releaseDateLTE = releaseDateLTE;
    }

    public String getWithGenres() {
        return withGenres;
    }

    public void setWithGenres(String withGenres) {
        this.withGenres = withGenres;
    }

    public String getWithOriginalLanguage() {
        return withOriginalLanguage;
    }

    public void setWithOriginalLanguage(String withOriginalLanguage) {
        this.withOriginalLanguage = withOriginalLanguage;
    }
}
