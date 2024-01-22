package com.example.mostri.model;

import com.google.gson.annotations.SerializedName;

public class ObjectsResponse {

    @SerializedName("id")
    private String id;

    @SerializedName("lat")
    private String lat;

    @SerializedName("lon")
    private String lon;

    private String picture;

    private String name;

    private String type;

    public ObjectsResponse(String id, String lat, String lon, String picture, String name, String type) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.picture = picture;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getType() {return type;}

    public void setType(String type) {this.type = type;}

}