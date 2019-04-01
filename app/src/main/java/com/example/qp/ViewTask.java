package com.example.qp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import java.sql.Time;
import java.util.UUID;

public class ViewTask extends AppCompatActivity {
    MainActivity mainActivity = new MainActivity();
    DatabaseHelper db = new DatabaseHelper(this);
    Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Intent myIntent = getIntent();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final UUID taskID = UUID.fromString(myIntent.getStringExtra("taskid"));
        displayTask(taskID);
        Button saveTaskBtn = findViewById(R.id.editViewTask);
        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask(taskID);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_view_task);
        Intent myIntent = getIntent();

        //displayTaskToCard(myIntent.getIntExtra("index",0));

    }

    public Task findTaskFromArrayList(UUID taskID)
    {
        Task viewedTask = new Task();
        for (Task taskIter : MainActivity.globalTaskList)
        {
            if(taskIter.getTaskId() == taskID)
            {
                viewedTask = taskIter;
            }
        }
        return viewedTask;
    }
    public void goHome(View view){
        startActivity(new Intent(this, MainActivity.class));
    }
    public void displayTask(UUID taskID)
    {
        //Display task from array lis
        Task viewedTask = findTaskFromArrayList(taskID);

        EditText taskName = (EditText) findViewById(R.id.viewTaskName);
        EditText priority = (EditText) findViewById(R.id.viewPriority);
        EditText taskNotes = (EditText) findViewById(R.id.viewDescription);
        //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
        EditText dueDate = (EditText) findViewById(R.id.viewDueDate);

        taskName.setText(viewedTask.getTaskName());
        priority.setText(viewedTask.getPriority());
        taskNotes.setText(viewedTask.getDescription());
        dueDate.setText(viewedTask.getDueDate());

    }

    public void saveTask(UUID taskID)
    {
        //Edits task from array list
        EditText taskName = (EditText) findViewById(R.id.viewTaskName);
        EditText priority = (EditText) findViewById(R.id.viewPriority);
        EditText taskNotes = (EditText) findViewById(R.id.viewDescription);
        //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
        EditText dueDate = (EditText) findViewById(R.id.viewDueDate);


        boolean updateCompleted = db.updateTable(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()),dueDate.getText().toString(), taskNotes.getText().toString(), 0, taskID);

        if(updateCompleted)
        {
            toast.show();
        }
        else
        {
            this.toast = Toast.makeText(mainActivity,"Task failed to save", Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
