package com.example.mostri.api;

public class UpdateUser {
    private String sid;
    private String name;
    private String picture;
    private boolean positionshare;

    public UpdateUser(String sid, String name, String picture, boolean positionshare) {
        this.sid = sid;
        this.name = name;
        this.picture = picture;
        this.positionshare = positionshare;
    }

    // Metodi getter e setter per ogni campo
    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

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