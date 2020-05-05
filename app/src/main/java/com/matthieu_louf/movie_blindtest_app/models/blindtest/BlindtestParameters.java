package com.matthieu_louf.movie_blindtest_app.models.blindtest;

import java.io.Serializable;

public class BlindtestParameters implements Serializable {
    private Integer idName;
    private Integer idImage;
    private Integer maximumPage;
    private String sortBy;
    private String releaseDateGTE;
    private String releaseDateLTE;
    private String withGenres;
    private String withOutGenres;
    private String withOriginalLanguage;

    public BlindtestParameters(Integer idName, Integer idImage, Integer maximumPage, String sortBy, String releaseDateGTE, String releaseDateLTE, String withGenres, String withOutGenres, String withOriginalLanguage) {
        this.idName = idName;
        this.idImage = idImage;
        this.maximumPage = maximumPage;
        this.sortBy = sortBy;
        this.releaseDateGTE = releaseDateGTE;
        this.releaseDateLTE = releaseDateLTE;
        this.withGenres = withGenres;
        this.withOutGenres = withOutGenres;
        this.withOriginalLanguage = withOriginalLanguage;
    }

    public Integer getIdName() {
        return idName;
    }

    public void setIdName(Integer idName) {
        this.idName = idName;
    }

    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
        this.idImage = idImage;
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

    public String getWithOutGenres() {
        return withOutGenres;
    }

    public void setWithOutGenres(String withOutGenres) {
        this.withOutGenres = withOutGenres;
    }

    public String getWithOriginalLanguage() {
        return withOriginalLanguage;
    }

    public void setWithOriginalLanguage(String withOriginalLanguage) {
        this.withOriginalLanguage = withOriginalLanguage;
    }
}
