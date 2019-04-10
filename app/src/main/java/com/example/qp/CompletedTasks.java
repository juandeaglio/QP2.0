package com.example.qp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CompletedTasks extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TaskCardRecyclerAdapter adapter = new TaskCardRecyclerAdapter(MainActivity.globalCompletedTaskList, this);
        RecyclerView taskRecycler = (RecyclerView) findViewById(R.id.completed_task_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecycler.setLayoutManager(layoutManager);
        taskRecycler.setAdapter(adapter);




    }


}
