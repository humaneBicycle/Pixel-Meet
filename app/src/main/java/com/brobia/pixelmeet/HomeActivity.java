package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView avatarImageView, messagesImageVIew, configImageView, settingsImageView, walletImageView, inventoryImageView, activeBackgroundImageView, homeImageVIew, menuImageView;
    TextView nameTextView;
    ProgressBar progressBarSimple;
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
                    nameTextView.setText(pixelMeet.getUser().getName());
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveAvatar()).into(avatarImageView);
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveBackground()).into(activeBackgroundImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("home");
                            if(fragment!=null) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "home").commit();
                            }else{
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeFragment(), "home").commit();
                            }
                            progressBarSimple.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });


                }
            }
        });

        initUI();

        //0 for settings, 1 for config, 2 for messages
        messagesImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(2);
                MessagesFragment fragment = (MessagesFragment) getSupportFragmentManager().findFragmentByTag("messages");
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "messages").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new MessagesFragment(), "messages").commit();
                }
                pixelMeet.setActiveFragment("messages");
            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(0);
                SettingsFragment fragment = (SettingsFragment) getSupportFragmentManager().findFragmentByTag("settings");
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "settings").commit();
                }else{
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
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "config").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new ConfigurationFragment(), "config").commit();
                }
                pixelMeet.setActiveFragment("config");
            }
        });
        inventoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InventoryFragment fragment = (InventoryFragment) getSupportFragmentManager().findFragmentByTag("inventory");
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "inventory").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new InventoryFragment(), "inventory").commit();
                }
                pixelMeet.setActiveFragment("inventory");
            }
        });

        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletFragment fragment = (WalletFragment) getSupportFragmentManager().findFragmentByTag("wallet");
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, fragment, "wallet").commit();
                }else{
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

                if(homeFragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, homeFragment, "home").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new HomeFragment(), "home").commit();
                }
                pixelMeet.setActiveFragment("home");
            }
        });
        menuImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(4);
                MenuFragment menu = (MenuFragment) getSupportFragmentManager().findFragmentByTag("menu");

                if(menu!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, menu, "menu").commit();
                }else{
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, new MenuFragment(), "menu").commit();
                }
                pixelMeet.setActiveFragment("menu");
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
        progressBarSimple = findViewById(R.id.progress_home_activity_simple);
        activeBackgroundImageView = findViewById(R.id.active_background_home_activity);
        homeImageVIew = findViewById(R.id.home_button_home_activity);
        nameTextView = findViewById(R.id.name_home_activity);
        menuImageView = findViewById(R.id.menu_button_home_activity);

        Sprite sprite = new CubeGrid();
        sprite.setColor(getColor(R.color.white));
        progressBarSimple.setIndeterminateDrawable(sprite);

    }

    private void setActiveView(int activeView){//0 for settings, 1 for config, 2 for messages, 3 for home
        if(activeView==0) {
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        }else if (activeView==1){
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        }else if(activeView==2) {
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        }else if (activeView==3){
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_green));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_white));
        }else if(activeView == 4){
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
            menuImageView.setImageDrawable(getDrawable(R.drawable.menu_icon_green));
        }

    }

}