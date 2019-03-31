package com.example.qp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Time;

public class ViewTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    protected void onResume()
    {
        super.onResume();
        setContentView(R.layout.activity_view_task);
        Intent myIntent = getIntent();

        //displayTaskToCard(myIntent.getIntExtra("index",0));

    }

    public void goHome(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void save(View view){
        //Save the changes to the task details
        startActivity(new Intent(this, MainActivity.class));

    }

}
