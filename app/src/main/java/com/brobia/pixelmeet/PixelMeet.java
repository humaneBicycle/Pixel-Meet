package com.brobia.pixelmeet;

import android.app.Application;
import android.widget.ImageView;

import com.brobia.pixelmeet.model.User;

public class PixelMeet extends Application {

    static User user;
    static String ACTIVE_FRAGMENT="home";//inventory, settings, messages, wallet, config;

    public String getActiveFragment() {
        return ACTIVE_FRAGMENT;
    }

    public void setActiveFragment(String activeFragment) {
        ACTIVE_FRAGMENT = activeFragment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        PixelMeet.user = user;
    }


}
