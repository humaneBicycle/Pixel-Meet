package com.brobia.pixelmeet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        if(new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_REGISTERED)){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isSignIn = (new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_SIGNED_IN));
        boolean isRegistered = (new PreferenceGetter(this).getBoolean(PreferenceGetter.IS_REGISTERED));
        if( isSignIn && isRegistered ){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }

        setContentView(R.layout.activity_main);



        findViewById(R.id.enter_on_screen_one).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSignIn){
                    startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                }else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}