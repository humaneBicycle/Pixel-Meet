package com.brobia.pixelmeet.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.brobia.pixelmeet.HomeActivity;
import com.brobia.pixelmeet.PixelMeet;
import com.brobia.pixelmeet.R;
import com.brobia.pixelmeet.SwipeFragmentCallback;
import com.brobia.pixelmeet.adapter.CardStackAdapter;
import com.brobia.pixelmeet.model.NearbyUser;
import com.brobia.pixelmeet.model.NotifyUserToSwipeFragment;
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
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;

public class SwipeFragment extends Fragment implements NotifyUserToSwipeFragment {

    Location location;
    private int radius = 100;
    PixelMeet pixelMeet;
    ImageView background;
    ArrayList<NearbyUser> nearbyUsers;
    ArrayList<String> uids;
    CardStackLayoutManager cardStackLayoutManager;
    CardStackView cardStackView;
    SwipeFragmentCallback callback;
    CardStackAdapter cardStackAdapter;
    CardStackListener cardStackListener;


    public SwipeFragment(){// Required empty public constructor
    }

    public SwipeFragment(SwipeFragmentCallback callback){// Required empty public constructor
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe, container, false);


        nearbyUsers = new ArrayList<>();
        //dummy data test
        nearbyUsers.add(new NearbyUser("uid","abhaydummy","eye","gender","hairstyle","religion","hobby","smoking","prologue","bio","professoin","address",10,0,"https://firebasestorage.googleapis.com/v0/b/pixel-meet-67f55.appspot.com/o/plate_default.png?alt=media&token=b7560b4b-82c3-40b7-9d8e-13ae7be995f4","https://firebasestorage.googleapis.com/v0/b/pixel-meet-67f55.appspot.com/o/avatar_default.png?alt=media&token=c85a21c4-7fa4-4eb0-aa25-51745aced624","https://firebasestorage.googleapis.com/v0/b/pixel-meet-67f55.appspot.com/o/background_default.png?alt=media&token=c5d3eb91-8b5c-4381-9e37-8b925061a334","","",""));

        uids = new ArrayList<>();

        background = view.findViewById(R.id.plate_background_swipe_fragment);
        cardStackView = view.findViewById(R.id.card_stack_view_swipe);

        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(((PixelMeet)getActivity().getApplication()).getmLocation()!=null) {
                    location = ((PixelMeet)getActivity().getApplication()).getmLocation();
                    Log.d("pwd getting closest user", "runnable ");
                    getClosestUser();
                    handler.removeCallbacks(this);
                }
            }
        };
        new Handler().postDelayed(runnable,1000);



        cardStackListener = new CardStackListener() {
            @Override
            public void onCardDragging(Direction direction, float ratio) {

            }

            @Override
            public void onCardSwiped(Direction direction) {
                //card is swiped
                Log.d("pwd card swiped ", "in direction : "+ direction.name());

            }

            @Override
            public void onCardRewound() {

            }

            @Override
            public void onCardCanceled() {

            }

            @Override
            public void onCardAppeared(View view, int position) {

            }

            @Override
            public void onCardDisappeared(View view, int position) {

            }
        };


        requireActivity().getOnBackPressedDispatcher().addCallback(getActivity(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("pwd handleOnBackPressed", "handleOnBackPressed: back pressed on swipe frag");
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
                Log.d("pwd getClosestUser : ", "onKeyEntered: "+key+" and for this key ");
                if(!pixelMeet.getUser().getUid().equals(key) && !uids.contains(key)){
                    FirebaseFirestore.getInstance().collection("nearBuyUser").whereEqualTo("uid",key).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                if(!task.getResult().isEmpty()){

                                    NearbyUser nearbyUser = task.getResult().toObjects(NearbyUser.class).get(0);
                                    nearbyUsers.add(nearbyUser);
                                    uids.add(key);
                                    cardStackAdapter.notifyItemInserted(nearbyUsers.size()-1);
                                    Log.d("pwd name is (onkeyEntered)", nearbyUser.getName());
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


            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                //ui changes
                //notify adapter
                Log.d("pwd", "setting adapter with list  "+String.valueOf(nearbyUsers.size()));
                cardStackLayoutManager = new CardStackLayoutManager(getActivity(),cardStackListener);
                cardStackLayoutManager.setVisibleCount(1);
                cardStackAdapter = new CardStackAdapter(getActivity(), nearbyUsers,(HomeActivity)getActivity(), SwipeFragment.this);
                cardStackView.setLayoutManager(cardStackLayoutManager);
                cardStackView.setAdapter(cardStackAdapter);
                callback.onSearchEnded();

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }

        });

    }


    @Override
    public void topUserNotification(NearbyUser user) {
        Picasso.get().load(user.getActivePlate()).into(background);
    }
}