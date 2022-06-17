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
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    ImageView avatarImageView, messagesImageVIew, configImageView, settingsImageView, walletImageView, inventoryImageView, activeBackgroundImageView, homeImageVIew;
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
                    Picasso.get().load(((PixelMeet) getApplication()).getUser().getActiveBackground()).into(activeBackgroundImageView);
                    getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, HomeFragment.getInstance()).commit();
                    progressBarSimple.setVisibility(View.GONE);

                }
            }
        });

        initUI();

        //0 for settings, 1 for config, 2 for messages
        messagesImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(2);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,MessagesFragment.getInstance()).commit();
                pixelMeet.setActiveFragment("messages");
            }
        });
        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(0);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,SettingsFragment.getInstance()).commit();
                pixelMeet.setActiveFragment("settings");
            }
        });
        configImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(1);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,ConfigurationFragment.getInstance()).commit();
                pixelMeet.setActiveFragment("config");
            }
        });
        inventoryImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,InventoryFragment.getInstance()).commit();
                pixelMeet.setActiveFragment("inventory");
            }
        });

        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container,WalletFragment.getInstance()).commit();
                pixelMeet.setActiveFragment("wallet");
            }
        });

        homeImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveView(3);
                getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment_container, HomeFragment.getInstance()).commit();
                pixelMeet.setActiveFragment("home");
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
        }else if (activeView==1){
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
        }else if(activeView==2) {
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_green));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_white));
        }else if (activeView==3){
            settingsImageView.setImageDrawable(getDrawable(R.drawable.settings_icon_white));
            configImageView.setImageDrawable(getDrawable(R.drawable.configuration_icon_white));
            messagesImageVIew.setImageDrawable(getDrawable(R.drawable.messages_icon_white));
            homeImageVIew.setImageDrawable(getDrawable(R.drawable.home_icon_green));
        }

    }
}