package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primecalendar.japanese.JapaneseCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    public TextView[] mDots;
    LinearLayout mDotsLayout;
    ImageView prev, next, location;
    int[] registration_page_state = {1, 0, 0};//1 means active page, 0 means non active page
    LinearLayout screenOne, screenTwo, screenThree;
    EditText dobSelector, inputAge, locationEditText;
    public static final int LOCATION_ACCESS_CODE = 1;
    protected Double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDotsLayout = findViewById(R.id.dots_layout);
        next = findViewById(R.id.img_register_next);
        prev = findViewById(R.id.img_register_prev);
        screenOne = findViewById(R.id.screen_one);
        screenTwo = findViewById(R.id.screen_two);
        screenThree = findViewById(R.id.screen_three);
        dobSelector = findViewById(R.id.age_selector_register);
        inputAge = findViewById(R.id.age_year_selector_register);
        location = findViewById(R.id.location_button_register);
        locationEditText=findViewById(R.id.location_edittext_register);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                locationEditText.setText(getAddress(latitude,longitude));

                            }
                        }
                    });
        }


        findViewById(R.id.register_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevButtonConfiguration(getVisibleLinearLayout());
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextButtonConfiguration(getVisibleLinearLayout());
            }
        });

        SingleDayPickCallback callback = new SingleDayPickCallback() {
            @Override
            public void onSingleDayPicked(PrimeCalendar singleDay) {
                // TODO
                dobSelector.setText(singleDay.getLongDateString());
                inputAge.setText(String.valueOf(Calendar.getInstance().get(Calendar.YEAR) -  singleDay.getYear()));
                inputAge.setClickable(false);

            }
        };

        dobSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrimeCalendar today = new CivilCalendar();

                PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                        .pickSingleDay(callback)
                        .build();

                datePicker.show(getSupportFragmentManager(), "SOME_TAG");
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(
                        RegisterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

                }else{
                    requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, LOCATION_ACCESS_CODE);

                }

            }
        });

        addDotsIndicator();
    }

    public void addDotsIndicator(){

        mDots = new TextView[3];

        for (int i =0 ; i<mDots.length;i++){
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.white));


            mDotsLayout.addView(mDots[i]);
        }
        mDots[0].setTextColor(getResources().getColor(R.color.green));


    }

    private void nextButtonConfiguration(LinearLayout layout){
        if(layout.getTag()=="screen_two"){
            next.setImageDrawable(getResources().getDrawable(R.drawable.tick_button));
        }
        if(layout.getTag()=="screen_one"){

            screenOne.animate().translationX(-500.0f).alpha(0).setDuration(200);
            screenTwo.animate().translationX(500.0f).alpha(1).setDuration(200);
            screenOne.setVisibility(View.GONE);
            screenTwo.setVisibility(View.VISIBLE);
        }



    }

    private void prevButtonConfiguration(LinearLayout layout) {
        if (layout.getTag() == "screen_two") {
            prev.setVisibility(View.GONE);

        }

        if (layout.getTag() == "screen_three") {
            next.setImageDrawable(getResources().getDrawable(R.drawable.front_arrow));
        }

        prev.setBackground(getResources().getDrawable(R.drawable.green_background));
    }


    private LinearLayout getVisibleLinearLayout(){
        if(screenOne.getVisibility()==View.VISIBLE){
            return screenOne;
        }else
        if(screenTwo.getVisibility()==View.VISIBLE){
            return screenTwo;
        }else
        if(screenThree.getVisibility()==View.VISIBLE){
            return screenThree;
        }else{
            return null;
        }


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_ACCESS_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new PreferenceGetter(this).putBoolean(PreferenceGetter.HAS_LOCATION_ACCESS,true);

                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();

            Log.v("IGA", "Address" + add);
            return add;
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }




}