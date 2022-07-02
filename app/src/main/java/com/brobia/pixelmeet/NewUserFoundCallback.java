package com.brobia.pixelmeet;

import com.brobia.pixelmeet.model.NearbyUser;

public interface NewUserFoundCallback {
    void onNewUserLoaded(NearbyUser nearbyUser);//when this is loaded display all the things it has in main activity
    void onFailed(String reason); // failed// no user nearby or something else. pass string as the reason
    void backPressed();//back is pressed. now display the global user data held in pixel meet application


}
