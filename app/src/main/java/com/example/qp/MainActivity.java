package com.example.qp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    public static ArrayList<Task> globalTaskList = new ArrayList<>();
    public static ArrayList<Task> globalCompletedTaskList = new ArrayList<>();
    public Intent myIntent;
    DatabaseHelper db = new DatabaseHelper(this);
    SQLiteDatabase taskDB;
    private CreateTask createTask;
    //private DatabaseHelper mDB;
    private Toast toast = null;

    //Database Variables

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = findViewById(R.id.createTaskBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openCreateTaskActivity(view);
            }
        });
        //TODO: populate arrayList w/ database entries - Ant
/*
        int cursorSize = 0;
        if(retrievedData.moveToFirst())
        {
            cursorSize = 1;
            while(retrievedData.moveToNext())
            {
                cursorSize++;
            }
        }
        databasesize = cursorSize;
        */
        //populate();
        //createTask = new CreateTask();
        populateArrayList("Task_Priority", "asc");
        TaskCardRecyclerAdapter adapter = new TaskCardRecyclerAdapter(globalTaskList, this);
        RecyclerView taskRecycler = (RecyclerView) findViewById(R.id.task_card_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecycler.setLayoutManager(layoutManager);
        taskRecycler.setAdapter(adapter);

    }



    //TODO: code dynamically - Ant
//    private void populate()
//    {
//        /*
//        Task currentTask;
//        for(int i = 0; i < databasesize; i++)
//        {
//            currentTask = new Task(retrievedData.getString(0), retrievedData.getString(1)
//                    , retrievedData.getInt(2), retrievedData.getString(3), retrievedData.getInt(4));
//            globalTaskList.add(currentTask);
//        }*/
//        globalTaskList.add(new Task("Task 1", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 2", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 3", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 4", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 5", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 6", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 7", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 8", "2/31/2019", 1, "nothing", 0));
//        globalTaskList.add(new Task("Task 9", "2/31/2019", 1, "nothing", 0));
//    }



    public void populateArrayList(String sortBy, String orderBy){
        //TODO: Check if arraylist is null here - Ethan
        MainActivity.globalTaskList.clear();
        Cursor cursor = db.sortTable(sortBy, orderBy);

        if(cursor.moveToFirst()){
            do {
                //MainActivity.globalTaskList.add();
                Task newTask  = new Task();
                newTask.setTaskName(cursor.getString(0)); //Task Name
                newTask.setDueDate(cursor.getString(1)); // Due Date
                newTask.setPriority(cursor.getInt(2)); //Priority
                newTask.setDescription(cursor.getString(3)); //Description
                newTask.setCompleted(cursor.getShort(4)); //Is Completed: 1 = yes; 2 = no
                newTask.setTaskId(UUID.fromString(cursor.getString(5))); // Check this method in Task class. Generates a random UUID through Java
                newTask.setTimeDueDate(cursor.getString(6));
                MainActivity.globalTaskList.add(newTask); //Adds it to the global array list
            }while (cursor.moveToNext());

        }

    }
    //TODO: check this works - Ant
    public void openViewTaskActivity(UUID taskID) {
        startActivity(myIntent);
    }
    //TODO: Ant
//    public void completeTask(View view) {
//        if (db.markTaskCompleted(globalTaskList.get(0).getTaskId().toString())) {
//            this.toast = Toast.makeText(this, "Task Marked Completed", Toast.LENGTH_SHORT);
//            toast.show();
//        } else {
//            this.toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//    }

    //TODO: refactor this code

    public void openViewTask() {
        startActivity(new Intent(this, ViewTask.class));
    }

    public void openReminderActivity() {
        startActivity(new Intent(this, Reminder.class));
    }

    public void openCalendarViewActivity() {
        startActivity(new Intent(MainActivity.this, CalendarView.class));

    }


    public void openCreateTaskActivity(View view) {
        startActivity(new Intent(this, CreateTask.class));
    }


    public void openCalendarView() {
        startActivity(new Intent(MainActivity.this, CalendarView.class));
    }

    public void openCompletedTasks() {
        startActivity(new Intent(MainActivity.this, CompletedTasks.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //TODO: To be implemented and tested - Ethan
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId()){
//            case R.id.mSortPriority:
//                //Maybe use mDB.SortTable()?
//                break;
//            case R.id.mSortDate:
//                this.createTask.populateArrayList("Task_Due_Date", "desc");
//                break;
//            case R.id.mSortNames:
//                this.createTask.populateArrayList("Task_Name", "asc");
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) { // to be further implemented -keghvart hagopian.
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            openCalendarView();

        } else if (id == R.id.nav_completed_tasks) {
            openCompletedTasks();

        } else if (id == R.id.nav_tools) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Implement me",
                    Toast.LENGTH_SHORT);
            toast.show();


        } else if (id == R.id.nav_reminder) {
            openReminderActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}