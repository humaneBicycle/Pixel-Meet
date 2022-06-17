package com.brobia.pixelmeet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class InventoryFragment extends Fragment {

    static InventoryFragment fragment;

    public InventoryFragment() {
        // Required empty public constructor
    }

    public static InventoryFragment getInstance() {
        if(fragment!=null) {
            return fragment;
        }else{
            fragment = new InventoryFragment();
            return fragment;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);;
        return view;
    }
}