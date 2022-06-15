package com.brobia.pixelmeet;

import android.app.Application;

import com.brobia.pixelmeet.model.User;

public class PixelMeet extends Application {

    static User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        PixelMeet.user = user;
    }
}
