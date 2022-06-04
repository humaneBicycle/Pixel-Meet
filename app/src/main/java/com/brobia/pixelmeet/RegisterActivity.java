package com.brobia.pixelmeet;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    public TextView[] mDots;
    LinearLayout mDotsLayout;
    ImageView prev, next;
    int[] registration_page_state = {1,0,0};//1 means active page, 0 means non active page

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDotsLayout = findViewById(R.id.dots_layout);
        next = findViewById(R.id.img_register_next);
        prev = findViewById(R.id.img_register_prev);

        findViewById(R.id.register_activity_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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


    }

    private void setGreenButtonNext(){
        prev.setBackground(getResources().getDrawable(R.drawable.green_background));
    }




}