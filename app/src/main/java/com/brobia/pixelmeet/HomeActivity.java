package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.brobia.pixelmeet.fragments.ConfigurationFragment;
import com.brobia.pixelmeet.fragments.HomeFragment;
import com.brobia.pixelmeet.fragments.InventoryFragment;
import com.brobia.pixelmeet.fragments.MenuFragment;
import com.brobia.pixelmeet.fragments.MessagesFragment;
import com.brobia.pixelmeet.fragments.SettingsFragment;
import com.brobia.pixelmeet.fragments.WalletFragment;
import com.brobia.pixelmeet.model.NearbyUser;
import com.brobia.pixelmeet.model.User;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener, HomeFragmentCallback, SwipeFragmentCallback, NotifyUserToHomeActivity {

    ImageView avatarImageView, messagesImageVIew, configImageView, settingsImageView, walletImageView, inventoryImageView, activeBackgroundImageView, homeImageVIew, menuImageView, acceptButton, rejectButton;
    TextView nameTextView;
    ProgressBar progressBarSimple;
    PixelMeet pixelMeet;
    GoogleApiClient mGoogleAPIClient;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    String userID;
    public static final int INTERNAL = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pixelMeet = (PixelMeet) getApplication();

        //buildGoogleAPIClient();//deprecated
        alternateLocationGetter();//not deprecated but unstable

        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ((PixelMeet) getApplication()).setUser(task.getResult().toObjects(User.class).get(0));
                    nameTextView.setText(pixelMeet.getUser().getName());
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveAvatar()).into(avatarImageView);
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveBackground()).into(activeBackgroundImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
                            if (fragment != null) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "home").commit();
                            } else {
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeFragment(HomeActivity.this), "home").commit();
                            }
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });


                }
            }
        });
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        initUI();

        //0 for settings, 1 for config, 2 for messages
        messagesImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(2);
                MessagesFragment fragment = (MessagesFragment) getSupportFragmentManager().findFragmentByTag("messages");
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "messages").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new MessagesFragment(), "messages").commit();
                }
                pixelMeet.setActiveFragment("messages");
            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(5);
                SettingsFragment fragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settings");
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "settings").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new SettingsFragment(), "setings").commit();
                }
                pixelMeet.setActiveFragment("settings");
            }
        });
        configImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(1);
                ConfigurationFragment fragment = (ConfigurationFragment) getSupportFragmentManager().findFragmentByTag("config");
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "config").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new ConfigurationFragment(), "config").commit();
                }
                pixelMeet.setActiveFragment("config");
            }
        });
        inventoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryFragment fragment = (InventoryFragment) getSupportFragmentManager().findFragmentByTag("inventory");
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "inventory").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new InventoryFragment(), "inventory").commit();
                }
                pixelMeet.setActiveFragment("inventory");
            }
        });

        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletFragment fragment = (WalletFragment) getSupportFragmentManager().findFragmentByTag("wallet");
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "wallet").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new WalletFragment(), "wallet").commit();
                }
                pixelMeet.setActiveFragment("wallet");
            }
        });

        homeImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(3);
                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");

                if (homeFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, homeFragment, "home").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeFragment(HomeActivity.this), "home").commit();
                }
                pixelMeet.setActiveFragment("home");
            }
        });
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(4);
                MenuFragment menu = (MenuFragment) getSupportFragmentManager().findFragmentByTag("menu");

                if (menu != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, menu, "menu").commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new MenuFragment(), "menu").commit();
                }
                pixelMeet.setActiveFragment("menu");
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void alternateLocationGetter(){
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(HomeActivity.this);
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if(task.isSuccessful()){
                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userLocation");

                        mLastLocation = task.getResult();
                        pixelMeet.setmLocation(mLastLocation);
                        GeoFire geoFire = new GeoFire(databaseReference);
                        geoFire.setLocation(userID, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
                        Log.d("pwd fusedLocationClient HomeActivity", "onComplete: "+mLastLocation.getLongitude()+"+"+mLastLocation.getLatitude());
                    }
                }
            });
        }else{
            new PreferenceGetter(HomeActivity.this).putBoolean(PreferenceGetter.HAS_LOCATION_ACCESS,false);
            Log.d("pwd onConnected", "no perm");
            //TODO has no location perm. build a pipleline
        }


//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                //insert above lines above
//            }
//        };
//        new Handler().postDelayed(runnable,INTERNAL);
    }

    public void initUI() {
        avatarImageView = findViewById(R.id.avatar_home_activity);
        messagesImageVIew = findViewById(R.id.messages_home_activity);
        configImageView = findViewById(R.id.configuration_home_activity);
        settingsImageView = findViewById(R.id.settings_home_activity);
        walletImageView = findViewById(R.id.wallet_image_view_home);
        inventoryImageView = findViewById(R.id.inventory_image_view_home);
        nameTextView = findViewById(R.id.name_home_activity);
        progressBarSimple = findViewById(R.id.progress_home_activity_simple);
        activeBackgroundImageView = findViewById(R.id.active_background_home_activity);
        homeImageVIew = findViewById(R.id.home_button_home_activity);
        nameTextView = findViewById(R.id.name_home_activity);
        menuImageView = findViewById(R.id.menu_button_home_activity);
        acceptButton = findViewById(R.id.accept_button_home_activity);
        rejectButton = findViewById(R.id.reject_button_home_activity);

        Sprite sprite = new CubeGrid();
        sprite.setColor(getColor(R.color.progress_bar_color));
        progressBarSimple.setIndeterminateDrawable(sprite);

    }

    private void setActiveView(int activeView) {//5 for settings, 1 for config, 2 for messages, 3 for home, 0 for blank
        prepareUIForHome();
        if (activeView == 0) {
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        } else if (activeView == 1) {
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        } else if (activeView == 2) {
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        } else if (activeView == 3) {
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_green));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        } else if (activeView == 4) {
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_green));
        } else if (activeView == 5) {
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        }

    }

    protected synchronized void buildGoogleAPIClient() {
        mGoogleAPIClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleAPIClient.connect();
    }

    private void prepareUIForSwipe() {
        inventoryImageView.setVisibility(View.GONE);
        walletImageView.setVisibility(View.GONE);
        acceptButton.setVisibility(View.VISIBLE);
        rejectButton.setVisibility(View.VISIBLE);

    }

    private void prepareUIForHome() {
        inventoryImageView.setVisibility(View.VISIBLE);
        walletImageView.setVisibility(View.VISIBLE);
        acceptButton.setVisibility(View.GONE);
        rejectButton.setVisibility(View.GONE);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        pixelMeet.setmLocation(location);
        Log.d("pwd onLocationChanged", "location = " + location.toString());
        Log.d("pwd onLocationChanged", "lat = " + location.getLatitude() + " long " + location.getLongitude());

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("userLocation");


        GeoFire geoFire = new GeoFire(databaseReference);
        geoFire.setLocation(userID, new GeoLocation(location.getLatitude(), location.getLongitude()));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("pwd onConnected", "onConnected called");
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERNAL);
        mLocationRequest.setFastestInterval(INTERNAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);






        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleAPIClient, mLocationRequest, HomeActivity.this);
            Log.d("pwd onLocationChanged", "location update set" );
        }else{
            new PreferenceGetter(this).putBoolean(PreferenceGetter.HAS_LOCATION_ACCESS,false);
            Log.d("pwd onConnected", "no perm");
            //TODO has no location perm. build a pipleline
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    //swipe frag implementations
    @Override
    public void onNewUserLoaded(NearbyUser nearbyUser) {
        nameTextView.setText(nearbyUser.getName());
        Picasso.get().load(nearbyUser.getActiveAvatar()).into(avatarImageView);
        Picasso.get().load(nearbyUser.getActiveBackground()).into(activeBackgroundImageView);

        //show swipe buttons
    }

    @Override
    public void onFailed(String reason) {
        Toast.makeText(this, "Could not load nearby users now", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backPressed() {
        //back button is pressed on swipe fragment
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "home").commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeFragment(HomeActivity.this), "home").commit();
        }
    }

    @Override
    public void onSearchStart() {
    }

    @Override
    public void onSearchEnded() {
        progressBarSimple.setVisibility(View.GONE);
    }
    //swipe fragment implementation ended

    @Override
    public void onPlateLoaded() {
        progressBarSimple.setVisibility(View.GONE);
        Log.d("pwd callback", "onComplete: called");
    }

    @Override
    public void swipeButtonPressed() {
        prepareUIForSwipe();
        progressBarSimple.setVisibility(View.VISIBLE);
    }

    @Override
    public void buttonClicked() {
        prepareUIForHome();
        setActiveView(0);
    }

    @Override
    public void onBackPressedSwipe() {
        prepareUIForHome();
    }

    @Override
    public void onUserArrayListLoaded() {
        progressBarSimple.setVisibility(View.GONE);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public void getCurrentUser(NearbyUser nearbyUser) {
        nameTextView.setText(nearbyUser.getName());
        Picasso.get().load(nearbyUser.getActiveBackground()).into(activeBackgroundImageView);
        Picasso.get().load(nearbyUser.getActiveAvatar()).into(avatarImageView);

    }
}