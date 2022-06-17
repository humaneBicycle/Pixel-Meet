package com.brobia.pixelmeet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.dynamic.IFragmentWrapper;

public class SettingsFragment extends Fragment {

    static SettingsFragment fragment;

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment getInstance() {
        if(fragment!=null) {
            return fragment;
        }else{
            fragment = new SettingsFragment();
            return fragment;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        return view;
    }
}