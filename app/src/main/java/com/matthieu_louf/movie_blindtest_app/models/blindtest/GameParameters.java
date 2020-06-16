package com.matthieu_louf.movie_blindtest_app.models.blindtest;

import com.matthieu_louf.movie_blindtest_app.models.GameType;

import java.io.Serializable;

public class GameParameters implements Serializable {
    private Integer id;
    private GameType gameType;
    private Integer idName;
    private Integer idImage;
    private Integer maximumPage;
    private String sortBy;
    private String releaseDateGTE;
    private String releaseDateLTE;
    private String withGenres;
    private String withOutGenres;
    private String withKeywords;
    private String withOutKeywords;
    private String withOriginalLanguage;

    public GameParameters(Integer id, GameType gameType, Integer idName, Integer idImage, Integer maximumPage, String sortBy, String releaseDateGTE, String releaseDateLTE, String withGenres, String withOutGenres, String withKeywords, String withOutKeywords, String withOriginalLanguage) {
        this.id = id;
        this.gameType = gameType;
        this.idName = idName;
        this.idImage = idImage;
        this.maximumPage = maximumPage;
        this.sortBy = sortBy;
        this.releaseDateGTE = releaseDateGTE;
        this.releaseDateLTE = releaseDateLTE;
        this.withGenres = withGenres;
        this.withOutGenres = withOutGenres;
        this.withKeywords = withKeywords;
        this.withOutKeywords = withOutKeywords;
        this.withOriginalLanguage = withOriginalLanguage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
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

    public String getWithKeywords() {
        return withKeywords;
    }

    public void setWithKeywords(String withKeywords) {
        this.withKeywords = withKeywords;
    }

    public String getWithOutKeywords() {
        return withOutKeywords;
    }

    public void setWithOutKeywords(String withOutKeywords) {
        this.withOutKeywords = withOutKeywords;
    }

    public String getWithOriginalLanguage() {
        return withOriginalLanguage;
    }

    public void setWithOriginalLanguage(String withOriginalLanguage) {
        this.withOriginalLanguage = withOriginalLanguage;
    }
}
