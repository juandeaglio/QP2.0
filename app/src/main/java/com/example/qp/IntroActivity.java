package com.example.qp;


import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.widget.TextView;

import com.DatabaseHelper;
import com.github.paolorotolo.appintro.AppIntro;
import com.spark.submitbutton.SubmitButton;

import static com.github.paolorotolo.appintro.AppIntro2Fragment.newInstance;

public class IntroActivity extends AppIntro {

    private Handler mHandler = new Handler();
    private String userName = "";
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_app_intro);
        dbHelper = new DatabaseHelper(this);

        addSlide(newInstance("Welcome to Quick Plan\n\n The All in One Task Planner", "Quickly plan your busy schedule all in one place",
                R.drawable.edit, ContextCompat.getColor(getApplicationContext(), R.color.matteOrange)));

        addSlide(newInstance("Stay focused on what's important", "Schedule alarms and reminders to keep you on track",
                R.drawable.alarm_on, ContextCompat.getColor(getApplicationContext(), R.color.navyBlue)));

        addSlide(newInstance("Lets Get to Planning", "Welcome to Quick Plan",
                R.drawable.idea, ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary)));

    }
    private Runnable waitForAnimation = new Runnable() {
        @Override
        public void run() {
            if(dbHelper.insertUserData(userName)){
                startActivity(new Intent(IntroActivity.this, MainActivity.class));
                finish();

            }
        }
    };



    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        final Dialog enterUser = new Dialog(this);
        enterUser.setTitle("NumberPicker");
        enterUser.setContentView(R.layout.user_enter_name);
        enterUser.show();



        final SubmitButton submitButton = (SubmitButton) enterUser.findViewById(R.id.saveButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = (TextView) enterUser.findViewById(R.id.userName);
                userName = textView.getText().toString();
                submitButton.postDelayed(waitForAnimation,2000);
            }
        });


    }


        @Override
        public void onSkipPressed (Fragment currentFragment){
            super.onDonePressed(currentFragment);
            final Dialog enterUser = new Dialog(this);
            enterUser.setTitle("NumberPicker");
            enterUser.setContentView(R.layout.user_enter_name);
            enterUser.show();



            final SubmitButton submitButton = (SubmitButton) enterUser.findViewById(R.id.saveButton);
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = (TextView) enterUser.findViewById(R.id.userName);
                    userName = textView.getText().toString();
                    submitButton.postDelayed(waitForAnimation,2000);
                }
            });

        }
    }