package com.brobia.pixelmeet.model;

public class NearbyUser {
    //personal info
    String name, eyeColor, gender, hairstyle, religion, hobby, smoking, prologue, bio, profession, address;
    int age, level;

    //active inventory
    String activePlate, activeAvatar, activeBackground;

    //location
    String latitude, longitude, hash;

    public NearbyUser(String name, String eyeColor, String gender, String hairstyle, String religion, String hobby, String smoking, String prologue, String bio, String profession, String address, int age, int level, String activePlate, String activeAvatar, String activeBackground, String latitude, String longitude, String hash) {
        this.name = name;
        this.eyeColor = eyeColor;
        this.gender = gender;
        this.hairstyle = hairstyle;
        this.religion = religion;
        this.hobby = hobby;
        this.smoking = smoking;
        this.prologue = prologue;
        this.bio = bio;
        this.profession = profession;
        this.address = address;
        this.age = age;
        this.level = level;
        this.activePlate = activePlate;
        this.activeAvatar = activeAvatar;
        this.activeBackground = activeBackground;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(String eyeColor) {
        this.eyeColor = eyeColor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHairstyle() {
        return hairstyle;
    }

    public void setHairstyle(String hairstyle) {
        this.hairstyle = hairstyle;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getSmoking() {
        return smoking;
    }

    public void setSmoking(String smoking) {
        this.smoking = smoking;
    }

    public String getPrologue() {
        return prologue;
    }

    public void setPrologue(String prologue) {
        this.prologue = prologue;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getActivePlate() {
        return activePlate;
    }

    public void setActivePlate(String activePlate) {
        this.activePlate = activePlate;
    }

    public String getActiveAvatar() {
        return activeAvatar;
    }

    public void setActiveAvatar(String activeAvatar) {
        this.activeAvatar = activeAvatar;
    }

    public String getActiveBackground() {
        return activeBackground;
    }

    public void setActiveBackground(String activeBackground) {
        this.activeBackground = activeBackground;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
