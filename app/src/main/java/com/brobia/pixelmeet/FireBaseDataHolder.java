package com.brobia.pixelmeet;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.brobia.pixelmeet.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FireBaseDataHolder {

    static User user;

    static {
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    user = (User) task.getResult().toObjects(User.class).get(0);
                } else {
                    Log.d("pwd", "FireBaseDataHelper onComplete: user fetch from firebase");
                }
            }
        });
    }


    public static User getUser(){
        Log.d("pwd ", "getUser: started"+String.valueOf(user==null));

            return user;

    }
    public static void setUser(User user){
//        if(user!=null){
//            FirebaseFirestore.getInstance().collection("email").whereEqualTo("email",FirebaseAuth.getInstance().getCurrentUser().getEmail()).
//
//            FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    if(task.isSuccessful()){
//
//                    }
//                }
//            })
//        }
    }



}
