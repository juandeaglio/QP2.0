package com.example.qp;

import android.graphics.Canvas;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.support.annotation.NonNull;
import android.widget.LinearLayout;

import com.DatabaseHelper;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import maes.tech.intentanim.CustomIntent;

public class CalendarView extends AppCompatActivity {

    ArrayList<Task>sortedTaskList = new ArrayList<>();
    CalendarRecyclerAdapter adapter = new CalendarRecyclerAdapter(sortedTaskList, this); //sortedTaskList is passed into adapter and any changes to sortedTaskList will changed array in the adapter. use updateSortedData to update screen elements
    private SwipeController swipeController;
    private DatabaseHelper db = new DatabaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.widget.CalendarView calendar = findViewById(R.id.calendarView);

        setUpRecycler();

        adapter.updateRecyclerView(convertDate(calendar.getDate()));

        calendar.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull android.widget.CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                String date = String.valueOf(month) + "/" + dayOfMonth + "/" + year;
                adapter.updateRecyclerView(date);
            }
        });




    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }


    private void setUpRecycler(){
        RecyclerView recyclerView = findViewById(R.id.calendar_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                String date = MainActivity.globalTaskList.get(position).getDueDate();
                db.deleteTask(MainActivity.globalTaskList.get(position).getTaskId().toString());
                MainActivity.globalTaskList.remove(position);
                adapter.updateRecyclerView(date);
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }

    /*Populates the recycler when calendar is opened*/
    private String convertDate(long dateAsLong){
        SimpleDateFormat sdf = new SimpleDateFormat("M/dd/yyyy");
        return sdf.format(new Date(dateAsLong));
    }


}
