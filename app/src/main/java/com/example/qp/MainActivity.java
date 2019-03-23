package com.example.qp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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

import java.sql.Time;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public static ArrayList<Task> globalTaskList = new ArrayList<>();
    public static ArrayList<Task> globalCompletedTaskList = new ArrayList<>();
    public Intent myIntent;

    @Override
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


        Button createTaskButton = findViewById(R.id.createTaskBtn);
//        createTaskButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //openCalendarViewActivity();
//                openCreateTaskActivity();
//            }
//        });

        Button viewTask1 = findViewById(R.id.viewTask1);
        viewTask1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                openViewTaskActivity(0);
            }
        });
//        Button viewTask2 = findViewById(R.id.viewTask2);
//        viewTask2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openViewTaskActivity(1);
//            }
//        });
    }

//    protected void onResume()
//    {
//        super.onResume();
//        //setContentView(R.layout.content_main);
//
//        displayTaskToCard();
//
//
//    }

    public void openViewTaskActivity(int index)
    {
        myIntent = new Intent(MainActivity.this, ViewTask.class);
        myIntent.putExtra("index", index);
        startActivity(myIntent);
    }



//    public void displayTaskToCard()
//    {
//        //Dummy task fields
//        Time testTime = new Time(13, 44, 3);
//        Task testTask = new Task("Prototype", "03/11/19", testTime, 1, "I need to finish the prototype and present it to the class.", false);
//        Task testTask2 = new Task("Some other task", "03/14/19", testTime, 5, "I need to finish this task sometime.", false);
//        globalTaskList.add(testTask);
//        globalTaskList.add(testTask2);
//        if (!globalTaskList.isEmpty()) {
//            //card 1
//            if (globalTaskList.get(0) != null) {
//                TextView taskName = findViewById(R.id.taskName02);
//                taskName.setText(globalTaskList.get(0).getTaskName());
//
//                TextView dueDate = findViewById(R.id.dueDateDesc02);
//                dueDate.setText(globalTaskList.get(0).getDueDate());
//
//                TextView description = findViewById(R.id.descriptionText02);
//                description.setText(globalTaskList.get(0).getDescription());
//
//                EditText priority = findViewById(R.id.numPriority02);
//                priority.setText(String.format("%d", globalTaskList.get(0).getPriority()));
//
//                CheckBox completed = findViewById(R.id.checkBox6);
//                completed.setChecked(false);
//                if(completed.isChecked())
//                {
//                //move to completed tasks
//                }
//            }
//            //card 2
//            if (globalTaskList.get(1) != null) {
//                TextView taskName = findViewById(R.id.taskName03);
//                taskName.setText(globalTaskList.get(1).getTaskName());
//
//                TextView dueDate = findViewById(R.id.dueDateDesc03);
//                dueDate.setText(globalTaskList.get(1).getDueDate());
//
//                TextView description = findViewById(R.id.descriptionText03);
//                description.setText(globalTaskList.get(1).getDescription());
//
//                EditText priority = findViewById(R.id.numPriority03);
//                priority.setText(String.format("%d", globalTaskList.get(1).getPriority()));
//
//                CheckBox completed = findViewById(R.id.checkBox7);
//                completed.setChecked(false);
//                if(completed.isChecked())
//                {
//                    //move to completed tasks
//                }
//            }
//
//        }
//    }

    public void openViewTask(){
        startActivity(new Intent(this, ViewTask.class));
    }


    public void openCalendarViewActivity(){
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
            if (id == R.id.action_settings) {
                return true;
            }

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

