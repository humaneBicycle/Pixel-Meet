package com.brobia.pixelmeet;

import android.location.Location;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.SharedElementCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brobia.pixelmeet.adapter.CardStackAdapter;
import com.brobia.pixelmeet.model.NearbyUser;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.util.ArrayList;

public class SwipeFragment extends Fragment {

    Location location;
    private int radius = 1;
    PixelMeet pixelMeet;
    ImageView background;
    ArrayList<NearbyUser> nearbyUsers;
    ArrayList<String> uids;
    CardStackLayoutManager cardStackLayoutManager;
    CardStackView cardStackView;
    SwipeFragmentCallback callback;

    public SwipeFragment(){// Required empty public constructor
    }

    public SwipeFragment(SwipeFragmentCallback callback){// Required empty public constructor
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);
        location = ((PixelMeet)getActivity().getApplication()).getmLocation();

        nearbyUsers = new ArrayList<>();
        //dummy data test
        nearbyUsers.add(new NearbyUser("uid","abhay","eye","gender","hairstyle","religion","hobby","smoking","prologue","bio","professoin","address",10,0,"","","","","",""));

        uids = new ArrayList<>();

        background = view.findViewById(R.id.plate_background_swipe_fragment);

        cardStackLayoutManager = new CardStackLayoutManager(getActivity());
        CardStackAdapter cardStackAdapter = new CardStackAdapter(getActivity(), nearbyUsers);
        cardStackView = view.findViewById(R.id.card_stack_view_swipe);
        cardStackView.setLayoutManager(cardStackLayoutManager);
        cardStackView.setAdapter(cardStackAdapter);

        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                HomeFragment homeFragment = (HomeFragment) getActivity().getSupportFragmentManager().findFragmentByTag("home");

                if (homeFragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, homeFragment, "home").commit();
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeFragment((HomeActivity)getActivity()), "home").commit();
                }
                pixelMeet.setActiveFragment("home");
                callback.onBackPressedSwipe();
            }
        });


        pixelMeet = (PixelMeet)getActivity().getApplication();
        //Picasso.get().load(pixelMeet.getUser().getActiveBackground()).into(background);
        getClosestUser();
        return view;
    }

    private void getClosestUser(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("userLocation");

        GeoFire geoFire = new GeoFire(databaseReference);

        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(),location.getLongitude()),radius);



        geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                //key returned will be th user id which we get from firebase auth
                if(!pixelMeet.getUser().getUid().equals(key) && !uids.contains(key)){
                    FirebaseFirestore.getInstance().collection("nearBuyUser").whereEqualTo("uid",key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(!task.getResult().isEmpty()){

                                    NearbyUser nearbyUser = task.getResult().toObjects(NearbyUser.class).get(0);
                                    nearbyUsers.add(nearbyUser);
                                    uids.add(key);
                                    Log.d("pwd user near user added", "onComplete: "+ nearbyUser.getName());
                                }else {
                                    Log.d("pwd getClosestUser", "result is empty");
                                }
                            }else{
                                Log.d("pwd getClosestUser", "task failed");
                            }
                        }
                    });

                }else{
                    Log.d("pwd getClosestUser", "if failed");
                }
                Log.d("pwd getClosestUser : ", "onKeyEntered: "+key);

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }


}