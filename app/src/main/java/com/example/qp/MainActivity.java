package com.example.qp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static ArrayList<Task> globalTaskList = new ArrayList<>();
    public static ArrayList<Task> globalCompletedTaskList = new ArrayList<>();
    public Intent myIntent;

    //Database Variables

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
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


        FloatingActionButton fab =  findViewById(R.id.createTaskBtn);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                openCreateTaskActivity(view);
            }
        });


    }


    public void openViewTaskActivity(int index) {
        myIntent = new Intent(MainActivity.this, ViewTask.class);
        myIntent.putExtra("index", index);
        startActivity(myIntent);


    }


    //TODO: needs recycler view
    public void displayTaskToCard()
    {
        //Dummy task fields
        Date testDate= new Date(2019, 4, 9, 13, 0, 0);
        Task testTask = new Task("Prototype", testDate.toString(), 1, "I need to finish the prototype and present it to the class.", 0);
        Task testTask2 = new Task("Some other task",  testDate.toString(), 5, "I need to finish this task sometime.", 0);
        globalTaskList.add(testTask);
        globalTaskList.add(testTask2);
        if (!globalTaskList.isEmpty()) {
            //card 1
            if (globalTaskList.get(0) != null) {
                TextView taskName = findViewById(R.id.taskName02);
                taskName.setText(globalTaskList.get(0).getTaskName());
                //TODO: Use date() to display day, month, year, time, etc.
                /*
                TextView dueDate = findViewById(R.id.dueDateDesc02);
                dueDate.setText(globalTaskList.get(0).getDueDate());
                */
                TextView description = findViewById(R.id.descriptionText02);
                description.setText(globalTaskList.get(0).getDescription());

                EditText priority = findViewById(R.id.numPriority02);
                priority.setText(String.format("%d", globalTaskList.get(0).getPriority()));

                CheckBox completed = findViewById(R.id.checkBox6);
                completed.setChecked(false);
                if (completed.isChecked()) {
                    //move to completed tasks
                }
            }
            //card 2
            if (globalTaskList.get(1) != null) {
                TextView taskName = findViewById(R.id.taskName03);
                taskName.setText(globalTaskList.get(1).getTaskName());
                //TODO: Use date() to display day, month, year, time, etc.
                /*
                TextView dueDate = findViewById(R.id.dueDateDesc03);
                dueDate.setText(globalTaskList.get(1).getDueDate());
                */
                TextView description = findViewById(R.id.descriptionText03);
                description.setText(globalTaskList.get(1).getDescription());

                EditText priority = findViewById(R.id.numPriority03);
                priority.setText(String.format("%d", globalTaskList.get(1).getPriority()));

                CheckBox completed = findViewById(R.id.checkBox7);
                completed.setChecked(false);
                if (completed.isChecked()) {
                    //move to completed tasks
                }
            }

        }
    }
    //TODO: refactor this code
    // Sorting home page cards
    public ArrayList<Task> sortCards(int mode)
    {
        ArrayList<Task> copyGlobalArray = globalTaskList;
        ArrayList<Task> sortedArrList = new ArrayList<Task>();
        if(!globalTaskList.isEmpty())
        {
            Task currentTask = globalTaskList.get(0);
            Task temp = new Task();
            int index = 0;
            switch(mode)
            {
                // "Name" sorting mode
                //TODO: check this for loop, possibly needs to be a while loop?
                //TODO: verify this sorts correctly
                //"dueDate" sorting mode
                case 0:
                    // a sorted Array, continues until whole array is iterated through.
                    for(int i = 0; i < copyGlobalArray.size(); i++)
                    {
                        for(int j = 0; j < copyGlobalArray.size(); j++)
                        {
                            temp = copyGlobalArray.get(j);
                            if(currentTask.getDueDate().compareTo(temp.getDueDate()) > 0)
                            {
                                currentTask = temp;
                                index = j;
                            }
                            else if(currentTask.getDueDate().compareTo(temp.getDueDate()) == 0)
                            {

                                if(currentTask.getPriority() < temp.getPriority())
                                {
                                    currentTask = temp;
                                    index = j;
                                }
                            }
                        }
                        copyGlobalArray.remove(index);
                        sortedArrList.add(temp);
                    }
                    break;

                //"priority" sorting mode
                case 1:
                    // a sorted Array, continues until whole array is iterated through.
                    for(int i = 0; i < copyGlobalArray.size(); i++)
                    {
                        for(int j = 0; j < copyGlobalArray.size(); j++)
                        {
                            temp = copyGlobalArray.get(j);
                            if(currentTask.getPriority() < temp.getPriority())
                            {
                                currentTask = temp;
                                index = j;
                            }
                            else if (currentTask.getPriority() == temp.getPriority())
                            {
                                if (currentTask.getDueDate().compareTo(temp.getDueDate()) > 0)
                                {
                                    currentTask = temp;
                                    index = j;
                                }
                            }
                        }
                        copyGlobalArray.remove(index);
                        sortedArrList.add(temp);
                    }
                    break;

                //"Name" sorting mode (on startup of application)
                case 2:
                default:
                    // search through global task array, finds the first in alphabetical order adds to
                    // a sorted Array, continues until whole array is iterated through.
                    for(int i = 0; i < copyGlobalArray.size(); i++)
                    {
                        for(int j = 0; j < copyGlobalArray.size(); j++)
                        {
                            temp = copyGlobalArray.get(j);
                            if(currentTask.getTaskName().compareTo(temp.getTaskName()) > 0)
                            {
                                currentTask = temp;
                                index = j;
                            }
                        }
                        copyGlobalArray.remove(index);
                        sortedArrList.add(temp);
                    }
                    break;
            }

        }
        return sortedArrList;

    }
    public void openViewTask(){
        startActivity(new Intent(this, ViewTask.class));
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
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
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}