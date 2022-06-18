package com.brobia.pixelmeet;

import android.app.Application;
import android.util.Log;
import android.widget.ImageView;

import com.brobia.pixelmeet.model.User;

public class PixelMeet extends Application {

    static User user;
    static String ACTIVE_FRAGMENT="home";//inventory, settings, messages, wallet, config, marketPlace, quests, profile, swipe, contactAndSupport, menu, lounge;

    public String getActiveFragment() {
        return ACTIVE_FRAGMENT;
    }

    public void setActiveFragment(String activeFragment) {
        Log.d("pwd active fragment", "setActiveFragment: "+ activeFragment);
        ACTIVE_FRAGMENT = activeFragment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        PixelMeet.user = user;
    }


}
