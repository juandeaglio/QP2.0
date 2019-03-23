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

//    public void displayTaskToCard(int index)
//    {
//        //Dummy task fields
//        if (!MainActivity.globalTaskList.isEmpty())
//        {
//            //card 1
//            if (MainActivity.globalTaskList.get(index) != null)
//            {
//                TextView taskName = findViewById(R.id.editText11);
//                taskName.setText(MainActivity.globalTaskList.get(index).getTaskName());
//
//                TextView dueDate = findViewById(R.id.editText12);
//                dueDate.setText(MainActivity.globalTaskList.get(index).getDueDate());
//
//                TextView description = findViewById(R.id.editText14);
//                description.setText(MainActivity.globalTaskList.get(index).getDescription());
//
//                EditText priority = findViewById(R.id.editText13);
//                priority.setText(String.format("%d", MainActivity.globalTaskList.get(index).getPriority()));
//
//            }
//        }
//    }
}
