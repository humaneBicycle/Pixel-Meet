package com.brobia.pixelmeet.model;

import java.util.ArrayList;

public class User {
    //personal info
    String name, dob, eyeColor, gender, hairstyle, religion, hobby, smoking, prologue, bio, profession, address;
    int age, level;

    //active inventory
    String activePlate, activeAvatar, activeBackground;

    //full inventory
    ArrayList<String> plates, backgrounds, avatars;

    //Identification
    String email, uid;

    //crypto wallet information
    String walletAddress;

    public User(String name,String dob, String eyeColor, String gender, String hairstyle, String religion, String hobby, String smoking, String prologue, String bio, String profession, String address, int age, int level, String activePlate, String activeAvatar, String activeBackground, ArrayList<String> plates, ArrayList<String> backgrounds, ArrayList<String> avatars, String email, String uid, String walletAddress) {
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
        this.plates = plates;
        this.backgrounds = backgrounds;
        this.avatars = avatars;
        this.email = email;
        this.uid = uid;
        this.walletAddress = walletAddress;
        this.dob = dob;
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

    public ArrayList<String> getPlates() {
        return plates;
    }

    public void setPlates(ArrayList<String> plates) {
        this.plates = plates;
    }

    public ArrayList<String> getBackgrounds() {
        return backgrounds;
    }

    public void setBackgrounds(ArrayList<String> backgrounds) {
        this.backgrounds = backgrounds;
    }

    public ArrayList<String> getAvatars() {
        return avatars;
    }

    public void setAvatars(ArrayList<String> avatars) {
        this.avatars = avatars;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }
}
