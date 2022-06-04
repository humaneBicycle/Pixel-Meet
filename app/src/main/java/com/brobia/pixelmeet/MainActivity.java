package com.brobia.pixelmeet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(new PreferenceGetter(MainActivity.this).getString(PreferenceGetter.IS_SIGNED_IN)==null){
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user!=null){
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                new PreferenceGetter(MainActivity.this).putBoolean(PreferenceGetter.IS_SIGNED_IN,true);
            }
        }


        findViewById(R.id.enter_on_screen_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}