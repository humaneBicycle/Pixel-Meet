package com.brobia.pixelmeet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    ImageView plate;
    PixelMeet pixelMeet;
    LinearLayout profile, marketplace, swipe, collect;
    RelativeLayout freeCoins;
    TextView freePicks, timeLeftHomeFrag;
    SeekBar timeSeekBar;
    HomeFragmentCallback callback;

    public HomeFragment() {
        // Required empty public constructor
    }

    public HomeFragment(HomeFragmentCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pixelMeet = (PixelMeet) getActivity().getApplication();

        plate = view.findViewById(R.id.plate_background_home_fragment);
        Picasso.get().load(((PixelMeet) getActivity().getApplication()).getUser().getActivePlate()).into(plate, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                callback.onPlateLoaded();
                initUI(view);
                setOnClickListeners();
            }

            @Override
            public void onError(Exception e) {

            }
        });
        return view;
    }

    private void initUI(View view){

        marketplace = view.findViewById(R.id.market_place_button_fragment_home);
        profile = view.findViewById(R.id.profile_button_fragment_home);
        swipe = view.findViewById(R.id.swipe_button_fragment_home);
        collect = view.findViewById(R.id.collect_button_fragment_home);
        freeCoins = view.findViewById(R.id.free_coin_button_home_frag);
        freePicks = view.findViewById(R.id.picks_available_textview_home_frag);
        timeLeftHomeFrag = view.findViewById(R.id.time_left_home_frag);
        timeSeekBar = view.findViewById(R.id.seekBar_time_left);

        SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
        String currentDateandTime = sdf.format(new Date());
        int hours = Integer.parseInt(String.valueOf(currentDateandTime.charAt(0)+currentDateandTime.charAt(1)))*60;
        int min = hours+Integer.parseInt(String.valueOf(currentDateandTime.charAt(2)+currentDateandTime.charAt(3)));
        int minSum= min+hours;
        timeSeekBar.setProgress(minSum/1440);
    }

    private void setOnClickListeners(){
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.buttonClicked();
                ProfileFragment fragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentByTag("profile");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "profile").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new ProfileFragment(), "profile").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("profile");
            }
        });

        swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.buttonClicked();
                callback.swipeButtonPressed();
                SwipeFragment fragment = (SwipeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("swipe");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "swipe").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new SwipeFragment((HomeActivity)getActivity()), "swipe").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("swipe");
            }
        });

        marketplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.buttonClicked();
                MarketPlaceFragment fragment = (MarketPlaceFragment) getActivity().getSupportFragmentManager().findFragmentByTag("marketplace");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "marketplace").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new MarketPlaceFragment(), "marketplace").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("marketplace");
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callback.buttonClicked();
                QuestsFragment fragment = (QuestsFragment) getActivity().getSupportFragmentManager().findFragmentByTag("quests");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "quests").addToBackStack(null).commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new QuestsFragment(), "quests").addToBackStack(null).commit();
                }
                pixelMeet.setActiveFragment("quests");
            }
        });
    }

}