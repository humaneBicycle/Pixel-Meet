package com.brobia.pixelmeet;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

    public HomeFragment() {
        // Required empty public constructor
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

        initUI(view);
        Picasso.get().load(((PixelMeet) getActivity().getApplication()).getUser().getActivePlate()).into(plate);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProfileFragment fragment = (ProfileFragment) getActivity().getSupportFragmentManager().findFragmentByTag("profile");
                if(fragment!=null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "profile").commit();
                }else{
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new ProfileFragment(), "profile").commit();
                }
            }
        });

        return view;
    }

    private void initUI(View view){
        plate = view.findViewById(R.id.plate_background_home_fragment);
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

}