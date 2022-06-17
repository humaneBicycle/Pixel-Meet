package com.brobia.pixelmeet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WalletFragment extends Fragment {

    static WalletFragment fragment;

    public WalletFragment() {
        // Required empty public constructor
    }

    public static WalletFragment getInstance() {
        if(fragment!=null) {
            return fragment;
        }else{
            fragment = new WalletFragment();
            return fragment;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        return view;
    }
}