package com.example.mostri.model;

public class RankingResponse {
    private int uid;
    private int life;
    private int experience;
    private int profileversion;
    private String picture;
    private String name;

    // Costruttore
    public RankingResponse(int uid, int life, int experience, int profileversion, String picture, String name) {
        this.uid = uid;
        this.life = life;
        this.experience = experience;
        this.profileversion = profileversion;
        this.picture = picture;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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

    public int getProfileversion() {
        return profileversion;
    }

    public void setProfileversion(int profileversion) {
        this.profileversion = profileversion;
    }
}