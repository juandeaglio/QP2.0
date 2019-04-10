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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CalendarView extends AppCompatActivity {

    ArrayList<Task>sortedTaskList = new ArrayList<>();
    TaskCardRecyclerAdapter adapter = new TaskCardRecyclerAdapter(sortedTaskList, this); //sortedTaskList is passed into adapter and any changes to sortedTaskList will changed array in the adapter. use updateSortedData to update screen elements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.widget.CalendarView calendar = findViewById(R.id.calendarView);

        RecyclerView recyclerView = findViewById(R.id.calendar_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        activateRecycler(calendar.getDate());



        calendar.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull android.widget.CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                String date = String.valueOf(month) + "/" + dayOfMonth + "/" + year;
                updateRecyclerView(date);
            }
        });




    }

    /*Populates the recycler when calendar is opened*/
    private void activateRecycler(long dateAsLong){
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        String date = sdf.format(new Date(dateAsLong));
        updateRecyclerView(date);
    }

    private void updateRecyclerView(String date){
        sortedTaskList.clear();
        for(int i=0; i < MainActivity.globalTaskList.size(); i++)
        {
            if(date.equals(MainActivity.globalTaskList.get(i).getDueDate()))
                sortedTaskList.add(MainActivity.globalTaskList.get(i));
        }
       adapter.updateData();
    }

}
