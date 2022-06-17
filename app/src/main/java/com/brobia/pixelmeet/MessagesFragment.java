package com.brobia.pixelmeet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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