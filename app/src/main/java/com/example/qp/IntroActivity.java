package com.example.qp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import static com.github.paolorotolo.appintro.AppIntro2Fragment.newInstance;

public class IntroActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro);

        addSlide(newInstance("Welcome to Quick Plan\n\n The All in One Task Planner", "Quickly plan your busy schedule all in one place",
                R.drawable.edit, ContextCompat.getColor(getApplicationContext(), R.color.matteOrange)));

        addSlide(newInstance("Stay focused on what's important", "Schedule alarms and reminders to keep you on track",
                R.drawable.alarm_on, ContextCompat.getColor(getApplicationContext(), R.color.navyBlue)));

        addSlide(newInstance("Stay focused on what's important", "Schedule alarms and reminders to keep you on track",
                R.drawable.alarm_on, ContextCompat.getColor(getApplicationContext(), R.color.navyBlue)));

    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}