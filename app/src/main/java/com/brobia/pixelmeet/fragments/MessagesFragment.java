package com.brobia.pixelmeet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brobia.pixelmeet.R;

public class MessagesFragment extends Fragment {

    static MessagesFragment fragment;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment getInstance() {
        if(fragment!=null) {
            return fragment;
        }else{
            fragment = new MessagesFragment();
            return fragment;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        return view;
    }
}