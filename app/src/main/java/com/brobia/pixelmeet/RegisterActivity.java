package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.brobia.pixelmeet.model.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {

    public TextView[] mDots;
    LinearLayout mDotsLayout;
    ImageView prev, next, location;
    LinearLayout screenOne, screenTwo, screenThree;
    EditText  inputAge, locationEditText, name, emailEditText, hobbyRegisterET, bioRegisterET, prologueET,professionET;
    public static final int LOCATION_ACCESS_CODE = 1;
    protected Double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;
    TextView dobSelector;
    ArrayList<EditText> editTexts;
    ArrayList<PowerSpinnerView> powerSpinners;
    PowerSpinnerView genderSpinner, eyeColorSpinner, hairStyleSpinner, smokingSpinner, religionSpinner;
    ProgressBar progressBar;
    String dob;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d("pwd", "onCreate: register activity created");

        //three dots on the bottom
        mDotsLayout = findViewById(R.id.dots_layout);

        //imageView buttons
        next = findViewById(R.id.img_register_next);
        prev = findViewById(R.id.img_register_prev);
        location = findViewById(R.id.location_button_register);

        //screens aka linear layout
        screenOne = findViewById(R.id.screen_one);
        screenTwo = findViewById(R.id.screen_two);
        screenThree = findViewById(R.id.screen_three);

        //textview
        dobSelector = findViewById(R.id.age_selector_register);

        //power spinners
        genderSpinner = findViewById(R.id.gender_spinner);
        eyeColorSpinner = findViewById(R.id.eye_color_registration);
        hairStyleSpinner = findViewById(R.id.hair_style_registration);
        smokingSpinner = findViewById(R.id.smoking_pref_registration);
        religionSpinner = findViewById(R.id.religion_registration);

        //edittext
        emailEditText = findViewById(R.id.email_register);
        name = findViewById(R.id.name_register);
        locationEditText=findViewById(R.id.location_edittext_register);
        inputAge = findViewById(R.id.age_year_selector_register);
        hobbyRegisterET = findViewById(R.id.hobby_register);
        bioRegisterET = findViewById(R.id.bio_register);
        prologueET = findViewById(R.id.prologue_register);
        professionET = findViewById(R.id.profession_register);

        //progress bar
        progressBar = findViewById(R.id.progress_bar_loading_register_activity);




        String email = getIntent().getStringExtra("email");
        if(email!=null && !email.equals("")){
            emailEditText.setText(email);
            emailEditText.setFocusable(false);
            findViewById(R.id.register_activity_login).setVisibility(View.GONE);
        } else if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Log.d("pmd",FirebaseAuth.getInstance().getCurrentUser().getEmail());
            emailEditText.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            emailEditText.setFocusable(false);
            findViewById(R.id.register_activity_login).setVisibility(View.GONE);
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
                dob = singleDay.getLongDateString();
                dobSelector.setText(dob);
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
                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(RegisterActivity.this);

                    getAndUpdateUserLocation();
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

    private void setActiveDot(int i){
        switch (i){
            case 0:
                mDots[0].setTextColor(getResources().getColor(R.color.green));
                mDots[1].setTextColor(getResources().getColor(R.color.white));
                mDots[2].setTextColor(getResources().getColor(R.color.white));
                break;
            case 1:
                mDots[0].setTextColor(getResources().getColor(R.color.white));
                mDots[1].setTextColor(getResources().getColor(R.color.green));
                mDots[2].setTextColor(getResources().getColor(R.color.white));
                break;
            case 2:
                mDots[0].setTextColor(getResources().getColor(R.color.white));
                mDots[1].setTextColor(getResources().getColor(R.color.white));
                mDots[2].setTextColor(getResources().getColor(R.color.green));
                break;
        }


    }

    private void nextButtonConfiguration(LinearLayout layout){
        if(layout.getTag().toString().equals("screen_two")){
            next.setImageDrawable(getResources().getDrawable(R.drawable.tick_button));
            screenTwo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_to_left));
            screenThree.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_from_right));
            screenTwo.setVisibility(View.GONE);
            screenThree.setVisibility(View.VISIBLE);
            setActiveDot(2);

        }
        if(layout.getTag().toString().equals("screen_one")){


            screenTwo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_from_right));
            screenOne.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_to_left));
            screenOne.setVisibility(View.GONE);
            screenTwo.setVisibility(View.VISIBLE);
            prev.setVisibility(View.VISIBLE);
            setActiveDot(1);
        }

        if(layout.getTag().toString().equals("screen_three")){
            Log.d("three_screen_finish_pressed","o");
            //boolean b =isFormComplete();
            Log.d("pwd", "tick button called");
            registerUser();




        }


    }

    int isFormComplete =0;//0 for true 1 for false
    private void registerUser() {
        Log.d("pwd", "registereUser called");
        com.brobia.pixelmeet.Callback callback = new Callback() {
            @Override
            public void onComplete() {
                Log.d("pwd", "onComplete: register user");
                if(isFormComplete==0){
                    String s = "Dummy address";
                    Log.d("pwd", "time to start firebase registration");
                    User user = new User(name.getText().toString(),dob,eyeColorSpinner.getText().toString(),genderSpinner.getText().toString(),hairStyleSpinner.getText().toString(),religionSpinner.getText().toString(),hobbyRegisterET.getText().toString(),smokingSpinner.getText().toString(),prologueET.getText().toString(),bioRegisterET.getText().toString(),professionET.getText().toString(),s,Integer.parseInt(inputAge.getText().toString()),0,null,null,null,null,null,null,emailEditText.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid(),null);
                    FirebaseFirestore.getInstance().collection("users").document().set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "User Registered Successfully!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            new PreferenceGetter(RegisterActivity.this).putBoolean(PreferenceGetter.IS_REGISTERED,true);
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(RegisterActivity.this,HomeActivity.class));
                            finish();
                        }
                    });
                }else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "Please Complete all the Form", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
                isFormComplete = 0;

            }
        };
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("pwd", "runnable run start");
                FirebaseFirestore.getInstance().collection("users").whereEqualTo("email",emailEditText.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                isFormComplete=1;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "Email Id already registered. Please login first.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
                if(isFormComplete==0) {
                    editTexts = new ArrayList<>();
                    editTexts.add(emailEditText);
                    editTexts.add(name);
                    editTexts.add(inputAge);
                    editTexts.add(locationEditText);
                    editTexts.add(hobbyRegisterET);
                    editTexts.add(bioRegisterET);
                    editTexts.add(prologueET);
                    editTexts.add(professionET);
                    editTexts.add(locationEditText);

                    powerSpinners = new ArrayList<>();
                    powerSpinners.add(genderSpinner);
                    powerSpinners.add(smokingSpinner);
                    powerSpinners.add(religionSpinner);
                    powerSpinners.add(hairStyleSpinner);
                    powerSpinners.add(eyeColorSpinner);
                }

                if(isFormComplete==0) {
                    for (int i = 0; i < editTexts.size(); i++) {
                        if (!(editTexts.get(i).getText().toString().trim().length() > 0)) {
                            isFormComplete = 1;
                            break;
                        }
                    }
                }
                //String s = getAddress(latitude,longitude);
                if(isFormComplete==0) {
                    for (int i = 0; i < powerSpinners.size(); i++) {
                        if (powerSpinners.get(i).getSelectedIndex() == -1) {
                            isFormComplete = 1;
                            break;
                        }
                    }
                }
                //if(s==null && s.equals("")){isFormComplete=false;}
                Log.d("is form complete aka finale",String.valueOf(isFormComplete));
                Log.d("pwd: runnable run end", String.valueOf(isFormComplete));
            }
        };

        Thread thread = new Thread(new CallbackTask(callback,runnable));
        progressBar.setVisibility(View.VISIBLE);
        Log.d("pwd", "thread start");
        thread.start();

    }

    private void prevButtonConfiguration(LinearLayout layout) {
        if (layout.getTag().toString().equals("screen_two")) {
            prev.setVisibility(View.GONE);
            screenTwo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_to_right));
            screenOne.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_from_left));
            screenTwo.setVisibility(View.GONE);
            screenOne.setVisibility(View.VISIBLE);
            setActiveDot(0);
        }

        if (layout.getTag().toString().equals("screen_three")) {
            next.setImageDrawable(getResources().getDrawable(R.drawable.front_arrow));
            screenTwo.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_from_left));
            screenThree.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_out_to_right));
            screenTwo.setVisibility(View.VISIBLE);
            screenThree.setVisibility(View.GONE);
            setActiveDot(1);
        }
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

    private void getAndUpdateUserLocation(){
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
                                Log.v("location pmd","lat:"+ latitude+"lon: "+ longitude);
                                locationEditText.setText(getAddress(latitude,longitude));

                            }
                        }
                    });
        }else{
            new PreferenceGetter(this).putBoolean(PreferenceGetter.HAS_LOCATION_ACCESS,false);
            //TODO show user app cant work without location nperm
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
                    getAndUpdateUserLocation();
                } else {
                    //TODO explain user app will not work without perm
                }
        }
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



    class CallbackTask implements Runnable {

        private Runnable task;

        private final com.brobia.pixelmeet.Callback callback;


        public CallbackTask(Callback callback, Runnable task) {
            this.task = task;
            this.callback = callback;
        }

        @Override
        public void run() {
            task.run();



            callback.onComplete();

        }

    }

}