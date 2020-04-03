package com.example.moviedb_app.model;


import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Genre implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("name")
    private String name;
    private final static long serialVersionUID = 6587201694890093991L;


    /**
     * @param name
     * @param id
     */
    public Genre(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}