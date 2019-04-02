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
import java.util.Date;

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

    }

    private void populate(){
        globalTaskList.add(new Task("Task 1","2/31/2019", 1, "nothing", 0));
        globalTaskList.add(new Task("Task 2","2/31/2019", 1, "nothing", 0));
        globalTaskList.add(new Task("Task 3","2/31/2019" , 1, "nothing", 0));
        globalTaskList.add(new Task("Task 4","2/31/2019" , 1, "nothing", 0));
        globalTaskList.add(new Task("Task 5","2/31/2019" , 1, "nothing", 0));
        globalTaskList.add(new Task("Task 6","2/31/2019" , 1, "nothing", 0));
        globalTaskList.add(new Task("Task 7","2/31/2019", 1, "nothing", 0));
        globalTaskList.add(new Task("Task 8","2/31/2019" , 1, "nothing", 0));
        globalTaskList.add(new Task("Task 9","2/31/2019" , 1, "nothing", 0));
    }

    private void updateRecyclerView(String date){
        taskNames.clear();
        for(int i=0; i < globalTaskList.size(); i++)
        {
            if(date.equals(globalTaskList.get(i).getDueDate()))
                taskNames.add(globalTaskList.get(i).getTaskName());
        }
        adapter.updateData(taskNames);
    }

}
