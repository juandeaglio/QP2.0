package com.example.qp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import maes.tech.intentanim.CustomIntent;

public class splash_screen extends AppCompatActivity {


    private static int timeOut = 3500; //3.5 seconds
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences preferences = getSharedPreferences("firstrun", MODE_PRIVATE);

        boolean firstStart = preferences.getBoolean("firstStart", true);

        if(firstStart){
            ImageView logo = (ImageView) findViewById(R.id.qpLogo);
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

            logo.startAnimation(animation);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(splash_screen.this, MainActivity.class));

                    finish();
                }
            }, timeOut);
            SharedPreferences.Editor editor =  preferences.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();

            //startActivity(new Intent(this, IntroActivity.class));
        }else{
            startActivity(new Intent(splash_screen.this, MainActivity.class));
            finish();
        }

//
//        ImageView logo = (ImageView) findViewById(R.id.qpLogo);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//
//        logo.startAnimation(animation);
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startActivity(new Intent(splash_screen.this, MainActivity.class));
//
//                finish();
//            }
//        }, timeOut);

    }
}
