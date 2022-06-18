package com.brobia.pixelmeet;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class MenuFragment extends Fragment {

    ImageView backgroundImageVIew;
    PixelMeet pixelMeet;
    AppCompatButton  lounge, wallet, contactAndSupport, inventory, quests, profile;

    public MenuFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        pixelMeet = (PixelMeet) getActivity().getApplication();
        iniUI(view);

        Picasso.get().load(pixelMeet.getUser().getActivePlate()).into(backgroundImageVIew);

        lounge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoungeFragment fragment = (LoungeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("lounge");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "lounge").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new LoungeFragment(), "lounge").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("lounge");
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentByTag("profile");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "profile").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new ProfileFragment(), "profile").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("profile");
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletFragment fragment = (WalletFragment) getActivity().getSupportFragmentManager().findFragmentByTag("wallet");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "wallet").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new WalletFragment(), "wallet").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("wallet");
            }
        });
        inventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryFragment fragment = (InventoryFragment) getActivity().getSupportFragmentManager().findFragmentByTag("inventory");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "inventory").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new InventoryFragment(), "inventory").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("inventory");
            }
        });

        quests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestsFragment fragment = (QuestsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("quests");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "quests").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new QuestsFragment(), "quests").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("quests");
            }
        });
        contactAndSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QuestsFragment fragment = (QuestsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("contactAndSupport");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "contactAndSupport").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new ContactAndSupportFragment(), "contactAndSupport").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("contactAndSupport");
            }
        });


        return view;
    }

    private void iniUI(View view ) {
        backgroundImageVIew = view.findViewById(R.id.plate_background_menu_fragment);
        lounge = view.findViewById(R.id.menu_lounge);
        inventory = view.findViewById(R.id.menu_inventory);
        wallet = view.findViewById(R.id.menu_wallet);
        contactAndSupport = view.findViewById(R.id.menu_contact_and_support);
        quests=view.findViewById(R.id.menu_quests);
        profile = view.findViewById(R.id.menu_profile);
    }
}