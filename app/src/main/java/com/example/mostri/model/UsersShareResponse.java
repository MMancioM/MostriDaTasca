package com.example.mostri.model;

public class UsersShareResponse {
    private String uid;
    private String lat;
    private String lon;
    private int profileversion;
    private int life;
    private int experience;
    private String time;

    public UsersShareResponse(String uid, String lat, String lon, int profileversion, int life, int experience, String time) {
        this.uid = uid;
        this.lat = lat;
        this.lon = lon;
        this.profileversion = profileversion;
        this.life = life;
        this.experience = experience;
        this.time = time;
    }

    // Includi i metodi getter e setter qui

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public int getProfileversion() {
        return profileversion;
    }

    public void setProfileversion(int profileversion) {
        this.profileversion = profileversion;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getTime(){ return time; }

    public void setTime(String time) {
        this.time = time;
    }
}



