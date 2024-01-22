package com.example.mostri.model;

import com.google.gson.annotations.SerializedName;

public class UserUpdate {
    private String name;
    private String picture;
    private boolean positionshare;

    public UserUpdate(String name, String picture, boolean positionshare) {
        this.name = name;
        this.picture = picture;
        this.positionshare = positionshare;
    }

    // Metodi getter e setter per ogni campo
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isPositionshare() {
        return positionshare;
    }

    public void setPositionshare(boolean positionshare) {
        this.positionshare = positionshare;
    }
}