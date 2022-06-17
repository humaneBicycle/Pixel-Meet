package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.brobia.pixelmeet.model.User;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView avatarImageView, messagesImageVIew, configImageView, settingsImageView, walletImageView, inventoryImageView, activeBackgroundImageView, homeImageVIew;
    TextView nameTextView;
    ProgressBar progressBarSimple,progressBarSplashScreen;
    LinearLayout splashScreenLoadingLayout;
    PixelMeet pixelMeet ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        pixelMeet = (PixelMeet)getApplication();


        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ((PixelMeet)getApplication()).setUser(task.getResult().toObjects(User.class).get(0));
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveAvatar()).into(avatarImageView);
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveBackground()).into(activeBackgroundImageView);
                    getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, new HomeFragment()).commit();
                    progressBarSplashScreen.setVisibility(View.GONE);
                    splashScreenLoadingLayout.setVisibility(View.GONE);

                }
            }
        });

        initUI();

        messagesImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView("messages");
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,new MessagesFragment()).commit();
                pixelMeet.setActiveFragment("messages");
            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView("settings");
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,new SettingsFragment()).commit();
                pixelMeet.setActiveFragment("settings");
            }
        });
        configImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView("config");
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,new ConfigurationFragment()).commit();
                pixelMeet.setActiveFragment("config");
            }
        });
        inventoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,new ConfigurationFragment()).commit();
                pixelMeet.setActiveFragment("inventory");
            }
        });

        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,new ConfigurationFragment()).commit();
                pixelMeet.setActiveFragment("wallet");
            }
        });

        homeImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,new ConfigurationFragment()).commit();
                pixelMeet.setActiveFragment("wallet");
            }
        });

    }

    public void initUI(){
        avatarImageView = findViewById(R.id.avatar_home_activity);
        messagesImageVIew = findViewById(R.id.messages_home_activity);
        configImageView = findViewById(R.id.configuration_home_activity);
        settingsImageView = findViewById(R.id.settings_home_activity);
        walletImageView = findViewById(R.id.wallet_image_view_home);
        inventoryImageView = findViewById(R.id.inventory_image_view_home);
        nameTextView = findViewById(R.id.name_home_activity);
        splashScreenLoadingLayout = findViewById(R.id.home_loading_screen);
        progressBarSimple = findViewById(R.id.progress_bar_splash_screen_home_activity);
        progressBarSplashScreen = findViewById(R.id.progress_home_simple);
        activeBackgroundImageView = findViewById(R.id.active_background_home_activity);
        homeImageVIew = findViewById(R.id.home_button_home_activity);

        Sprite sprite = new CubeGrid();
        sprite.setColor(Color.BLUE);
        progressBarSplashScreen.setIndeterminateDrawable(sprite);
        progressBarSimple.setIndeterminateDrawable(sprite);

    }

    private void setActiveView(String activeView){
        if(activeView.equals("settings")) {
            messagesImageVIew.setBackground(getDrawable(R.drawable.messages_icon_white));
            configImageView.setBackground(getDrawable(R.drawable.configuration_icon_white));
            settingsImageView.setBackground(getDrawable(R.drawable.settings_icon_green));
        }else if (activeView.equals("config") ){
            messagesImageVIew.setBackground(getDrawable(R.drawable.messages_icon_white));
            settingsImageView.setBackground(getDrawable(R.drawable.settings_icon_white));
            configImageView.setBackground(getDrawable(R.drawable.configuration_icon_green));
        }else if(activeView.equals("messages")) {
            settingsImageView.setBackground(getDrawable(R.drawable.settings_icon_white));
            configImageView.setBackground(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setBackground(getDrawable(R.drawable.messages_icon_green));
        }

    }
}