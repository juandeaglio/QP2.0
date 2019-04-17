package com.example.qp;

import android.content.Intent;
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
import android.widget.Toast;

import com.DatabaseHelper;

public class CompletedTasks extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SwipeController swipeController;
    private TaskCardRecyclerAdapter adapter = new TaskCardRecyclerAdapter(MainActivity.globalCompletedTaskList, this);
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




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

        setUpRecycler();

    }

    private void setUpRecycler(){
        RecyclerView taskRecycler;
        taskRecycler = (RecyclerView) findViewById(R.id.completed_task_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecycler.setLayoutManager(layoutManager);
        taskRecycler.setAdapter(adapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                db.deleteTask(MainActivity.globalCompletedTaskList.get(position).getTaskId().toString());
                MainActivity.globalCompletedTaskList.remove(position);
                adapter.updateData();
            }
        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(taskRecycler);
        taskRecycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    }


    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.nav_home){
            startActivity(new Intent(CompletedTasks.this, MainActivity.class));
        }
       else if (id == R.id.nav_calendar) {
            startActivity(new Intent(CompletedTasks.this, CalendarView.class));

        } else if (id == R.id.nav_completed_tasks) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "*poof*",
                    Toast.LENGTH_SHORT);
            toast.show();

        }  else if (id == R.id.nav_reminder) {
            //openReminderActivity();
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
