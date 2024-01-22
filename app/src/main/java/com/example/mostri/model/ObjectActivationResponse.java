package com.example.mostri.model;
public class ObjectActivationResponse {
    private boolean died;
    private int life;
    private int experience;
    private String weapon;
    private String armor;
    private String amulet;

    public boolean isDied() {
        return died;
    }

    public void setDied(boolean died) {
        this.died = died;
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

    public String getAmulet() {
        return amulet;
    }

    public void setAmulet(String amulet) {
        this.amulet = amulet;
    }
}


