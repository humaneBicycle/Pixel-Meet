package com.brobia.pixelmeet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ConfigurationFragment extends Fragment {

    static ConfigurationFragment fragment;

    public ConfigurationFragment() {
        // Required empty public constructor
    }

    public static  ConfigurationFragment getInstance(){
        if(fragment!=null){
            return fragment;
        }else{
            fragment = new ConfigurationFragment();
            return fragment;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuration, container, false);
        return view;
    }


}