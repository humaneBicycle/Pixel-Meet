package com.brobia.pixelmeet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

    ImageView avatarImageView, messagesImageVIew, configImageView, settingsImageView, walletImageView, inventoryImageView;
    TextView nameTextView;
    ProgressBar progressBar;
    LinearLayout splashScreenLoadingLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseFirestore.getInstance().collection("users").whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ((PixelMeet)getApplication()).setUser(task.getResult().toObjects(User.class).get(0));
                    splashScreenLoadingLayout.setVisibility(View.GONE);
                }
            }
        });

        initUI();

        if(FireBaseDataHolder.getUser().getActiveAvatar()==null && FireBaseDataHolder.getUser().getActiveAvatar().trim().length()>0){
            Picasso.get().load(FireBaseDataHolder.getUser().getActiveAvatar()).into(avatarImageView);
        }else{
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/pixel-meet-67f55.appspot.com/o/avatar_default.png?alt=media&token=c85a21c4-7fa4-4eb0-aa25-51745aced624").into(avatarImageView);
        }


    }

    private void initUI(){
        avatarImageView = findViewById(R.id.avatar_home_activity);
        messagesImageVIew = findViewById(R.id.messages_home_activity);
        configImageView = findViewById(R.id.configuration_home_activity);
        settingsImageView = findViewById(R.id.settings_home_activity);
        walletImageView = findViewById(R.id.wallet_image_view_home);
        inventoryImageView = findViewById(R.id.inventory_image_view_home);
        nameTextView = findViewById(R.id.name_home_activity);
        splashScreenLoadingLayout = findViewById(R.id.home_loading_screen);
        progressBar = findViewById(R.id.progress_home);

        Sprite sprite = new CubeGrid();
        progressBar.setIndeterminateDrawable(sprite);


    }
}