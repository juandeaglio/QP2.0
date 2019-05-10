package com.example.qp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.Window;

import com.DatabaseHelper;

import java.util.ArrayList;
import java.util.UUID;

public class Projects extends AppCompatActivity {


    public ArrayList<ProjectObj> projectArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_projects);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.createProjectBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Projects.this,CreateProject.class));
            }
        });

        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.matteOrange));


        populateProjectArray();
        setUpRecyclerView();
    }

    private void setUpRecyclerView(){

        SwipeControllerActions swipeControllerActions = new SwipeControllerActions() {
            @Override
            public void onLeftSwiped(UUID taskID) {
                // TODO: 5/8/2019 NEEDS TO BE IMPLEMENTED
            }

            @Override
            public void onRightSwiped(UUID taskID) {
                // TODO: 5/8/2019 NEEDS TO BE IMPLEMENTED
                super.onRightSwiped(taskID);
            }
        };

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.project_recycler);
        ProjectRecyclerAdapter adapter = new ProjectRecyclerAdapter(projectArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        SwipeControllerProjects swipeController = new SwipeControllerProjects(swipeControllerActions,this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);


    }

    private void populateProjectArray(){
        DatabaseHelper db = new DatabaseHelper(this);

        Cursor projectDB =  db.getAllProjects();
        for (projectDB.moveToFirst(); !projectDB.isAfterLast(); projectDB.moveToNext()) {
            // do what you need with the projectDB here
            ProjectObj newProject = new ProjectObj(projectDB.getString(0),projectDB.getString(1),projectDB.getString(2),projectDB.getString(4),0,projectDB.getString(3),projectArrayList.size());
            projectArrayList.add(newProject);
        }


        // TODO: 5/8/2019 NEEDS TO BE IMPLEMENTED
    }

}
