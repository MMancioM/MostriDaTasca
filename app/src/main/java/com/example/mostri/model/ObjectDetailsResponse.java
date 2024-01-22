package com.example.mostri.model;

public class ObjectDetailsResponse {
    private String id;
    private String type;
    private int level;
    private double lat;
    private double lon;
    private String image;
    private String name;

    public ObjectDetailsResponse(String id, String type, int level, double lat, double lon, String image, String name) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


