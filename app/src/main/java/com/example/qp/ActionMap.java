package com.example.qp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.DatabaseHelper;

public class ActionMap extends AppCompatActivity {

    ColorManager colorManager = MainActivity.colorManager;
    DatabaseHelper db = new DatabaseHelper(this);
    private String projectIDStr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(colorManager.getColorAccent());
        toolbar.setTitleTextColor(colorManager.getHeaderTextColor());
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = getIntent();
        projectIDStr = intent.getStringExtra("ProjectID");
        ProjectObj currentProj = db.getProject(projectIDStr);
        currentProj.setStageList(db.getStagesForProject(projectIDStr));


        //TODO: Dynamically build/make buttons in vertical linearl layout based on the stageArrayList in the currentProj variable - QP
        LinearLayout mapLayout = (LinearLayout) findViewById(R.id.mapContainer);

        Button btnShow = new Button(this);
        btnShow.setText("Project Start");
        btnShow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActionMap.this, "This is working", Toast.LENGTH_LONG).show();
            }
        });


        if (mapLayout != null) {
            mapLayout.addView(btnShow);
        }




    }

}
