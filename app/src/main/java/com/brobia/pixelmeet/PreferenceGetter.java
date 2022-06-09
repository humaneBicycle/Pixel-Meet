package com.brobia.pixelmeet;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PreferenceGetter {

    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public static final String IS_SIGNED_IN= "is_signed_in";
    public static final String HAS_LOCATION_ACCESS="has_location_access";

    public PreferenceGetter(Context c){
        this.context = c;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();
    }

    public void putString(String key, String value){
        editor.putString(key,value).apply();
    }

    public String getString(String key){
        //second argument is the value is first do not exist
        return preferences.getString(key,null);
    }

    public void putBoolean(String key , boolean b){
        editor.putBoolean(key,b).apply();
    }

    public boolean getBoolean(String key){
        //second argument is the value is first do not exist
        return preferences.getBoolean(key,false);
    }



}
