package com.example.qp;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import java.sql.Time;
import java.util.ArrayList;

public class CalendarView extends AppCompatActivity {

    ArrayList<String>taskNames = new ArrayList<>();
    ArrayList<Task>globalTaskList = new ArrayList<>();
    RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, taskNames);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.widget.CalendarView calendar = findViewById(R.id.calendarView);
        populate();
        RecyclerView recyclerView = findViewById(R.id.task_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        calendar.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull android.widget.CalendarView view, int year, int month, int dayOfMonth) {
                String date = month + "/" + dayOfMonth + "/" + year;
                updateRecyclerView(date);
            }
        });


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    private void populate(){

        //Using fall back constructor for fake data/testing
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());
        globalTaskList.add(new Task());

    }

    private void updateRecyclerView(String date){
        taskNames = new ArrayList<>();
        for(int i=0; i < globalTaskList.size(); i++)
        {
            if(date.equals(globalTaskList.get(i).getDueDate()))
                taskNames.add(globalTaskList.get(i).getTaskName());
        }
        adapter.updateData(taskNames);
    }

}
