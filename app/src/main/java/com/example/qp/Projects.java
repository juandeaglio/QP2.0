package com.example.qp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
    ColorManager colorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_projects);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        colorManager = MainActivity.colorManager;
        FloatingActionButton fab = findViewById(R.id.createProjectBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Projects.this,CreateProject.class));
            }
        });


        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        Window window = getWindow();
        window.setStatusBarColor(colorManager.getColorAccent());
        toolbar.setBackgroundColor(colorManager.getColorAccent());


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
        ProjectRecyclerAdapter adapter = new ProjectRecyclerAdapter(projectArrayList, this);
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
            ProjectObj newProject = new ProjectObj();
            newProject.setProjectId(UUID.fromString(projectDB.getString(0)));
            newProject.setProjectName(projectDB.getString(1));
            newProject.setDueDate(projectDB.getString(2));
            newProject.setTimeDueDate(projectDB.getString(3));
            newProject.setDescription(projectDB.getString(4));
            newProject.setCompleted((short)projectDB.getInt(5));
            newProject.setNumOfStages(Integer.parseInt(projectDB.getString(6)));
            projectArrayList.add(newProject);
        }


        // TODO: 5/8/2019 NEEDS TO BE IMPLEMENTED
    }

}
