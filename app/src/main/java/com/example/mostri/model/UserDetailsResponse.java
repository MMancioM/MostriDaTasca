package com.example.mostri.model;

import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.SerializedName;

public class UserDetailsResponse {
    @SerializedName("uid")
    private int uid;
    @SerializedName("name")
    private String name;
    @SerializedName("lat")
    private double lat;
    @SerializedName("lon")
    private double lon;
    @SerializedName("time")
    private String time;
    @SerializedName("life")
    private int life;
    @SerializedName("experience")
    private int experience;
    @SerializedName("weapon")
    private String weapon;
    @SerializedName("armor")
    private String armor;
    @SerializedName("amulet")
    private int amulet;
    @SerializedName("picture")
    private String picture;
    @SerializedName("profileversion")
    private int profileVersion;

    @SerializedName("positionshare")
    private boolean positionshare;

    // Getters and Setters
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getWeapon() {
        return weapon;
    }

    public void setWeapon(String weapon) {
        this.weapon = weapon;
    }

    public String getArmor() {
        return armor;
    }

    public void setArmor(String armor) {
        this.armor = armor;
    }

    public int getAmulet() {
        return amulet;
    }

    public void setAmulet(int amulet) {
        this.amulet = amulet;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getProfileVersion() {
        return profileVersion;
    }

    public void setProfileVersion(int profileVersion) {
        this.profileVersion = profileVersion;
    }

    public boolean isPositionshare() {
        return positionshare;
    }

    public void setPositionshare(boolean positionshare) {
        this.positionshare = positionshare;
    }
}

