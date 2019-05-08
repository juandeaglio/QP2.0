package com.example.qp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.UUID;

import maes.tech.intentanim.CustomIntent;

public class CompletedTasks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TaskCardRecyclerAdapter adapter = new TaskCardRecyclerAdapter(MainActivity.globalCompletedTaskList, this);
    private DatabaseHelper db;
    private MainActivity mainActivity= new MainActivity();
    public ColorManager colorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colorManager = MainActivity.colorManager;

        FloatingActionButton fab = findViewById(R.id.deleteAllButton);

        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        toolbar.setBackgroundColor(colorManager.getColorAccent());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        MainActivity mainActivity = new MainActivity();
        db = new DatabaseHelper(this);
        mainActivity.populateCompletedTaskList(db, mainActivity.sortSelector);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        navigationView.getHeaderView(0).setBackgroundColor(colorManager.getColorAccent());

        setUpRecycler();

    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "left-to-right");
    }


    private void setUpRecycler(){
        SwipeController swipeController;
        RecyclerView taskRecycler;
        taskRecycler = (RecyclerView) findViewById(R.id.completed_task_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecycler.setLayoutManager(layoutManager);
        taskRecycler.setAdapter(adapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftSwiped(UUID taskID) {
                db.deleteTask(taskID.toString());
                mainActivity.populateArrayList(db, mainActivity.sortSelector);
                adapter.updateData();
            }

            @Override
            public void onRightSwiped(UUID taskID){
                db.unCheckCompletedTask(taskID.toString());
                mainActivity.populateArrayList(db, mainActivity.sortSelector);
                mainActivity.populateCompletedTaskList(db, mainActivity.sortSelector);
                adapter.updateData();
            }
        }, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(taskRecycler);

    }


    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            startActivity(new Intent(CompletedTasks.this, MainActivity.class));
            CustomIntent.customType(this, "right-to-left");

        }
       else if (id == R.id.nav_calendar) {
            startActivity(new Intent(CompletedTasks.this, CalendarView.class));
            CustomIntent.customType(this, "right-to-left");


        } else if (id == R.id.nav_completed_tasks) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "*poof*",
                    Toast.LENGTH_SHORT);
            toast.show();


        }  else if (id == R.id.nav_reminder) {
            startActivity(new Intent(CompletedTasks.this, Reminder.class));
            CustomIntent.customType(this, "right-to-left");

        }else if(id == R.id.nav_project){
            startActivity(new Intent(CompletedTasks.this, CreateProject.class));
            CustomIntent.customType(this, "right-to-left");
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void deleteAllTasks(View view){
        DeleteAllCompletedTasksPrompt deletePrompt = new DeleteAllCompletedTasksPrompt();
        deletePrompt.show(getSupportFragmentManager(), "deletePrompt");
//        db.deleteTask(this.taskIDStr);
//        startActivity(new Intent(this, MainActivity.class));

    }


}
