package com.example.mostri.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class StoredUser {
    @PrimaryKey
    @NonNull
    private int uid;

    @NonNull
    private int profileVersion;
    private String picture;
    private String name;
    private int life;
    private int experience;
    private int amulet;
    private String weapon;
    private String armor;
    private boolean positionshare;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getProfileVersion() {
        return profileVersion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setProfileVersion(int profileVersion) {
        this.profileVersion = profileVersion;
    }

    public String getPicture() {
        return picture;
    }

    public boolean isPositionshare() {
        return positionshare;
    }

    public void setPositionshare(boolean positionshare) {
        this.positionshare = positionshare;
    }


    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "StoredUser{" +
                "uid='" + uid + '\'' +
                ", profileVersion=" + profileVersion +
                ", nome=" + name +
                ", life=" + life +
                ", experience=" + experience +
                ", arma=" + armor +
                ", amuleto=" + amulet +
                ", weapon=" + weapon +
                ", positionShare=" + positionshare +
                ", picture='" + picture + '\'' +
                '}';
    }
}
