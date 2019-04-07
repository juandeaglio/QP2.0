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
        this.toast = Toast.makeText(this,"Task Successfuly Saved!", Toast.LENGTH_SHORT);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        String taskIDStr = myIntent.getStringExtra("taskid");
        final UUID taskID = UUID.fromString(taskIDStr);
        displayTask(taskID);
        Button saveTaskBtn = findViewById(R.id.editViewTask);
        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTask(taskID);
            }
        });

        Button deleteTask = findViewById(R.id.deleteViewTask);
        deleteTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask(taskID);
            }
        });
    }

//    protected void onResume() {
//        super.onResume();
//        setContentView(R.layout.activity_view_task);
//        Intent myIntent = getIntent();
//    }

    public Task findTaskFromArrayList(UUID taskID)
    {
        UUID searchFor = taskID;
        UUID found = null;
        for (Task taskIter : MainActivity.globalTaskList)
        {
            found = taskIter.getTaskId();
            if(found.toString().equals(searchFor.toString()))
            {
                return taskIter;
            }
        }
        return null;
    }
    public void goHome(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void returnToHome(){
        startActivity(new Intent(this, MainActivity.class));

    }
    //TODO: test this method - Ant
    public void displayTask(UUID taskID)
    {
        //Display task from array lis
        Task viewedTask = findTaskFromArrayList(taskID);

        EditText taskName = (EditText) findViewById(R.id.viewTaskName);
        EditText priority = (EditText) findViewById(R.id.viewPriority);
        EditText taskNotes = (EditText) findViewById(R.id.viewDescription);
        //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
        EditText dueDate = (EditText) findViewById(R.id.viewDueDate);
        EditText dueTime = (EditText) findViewById(R.id.viewTime);
        int priorityTemp = viewedTask.getPriority();
        String str1 = viewedTask.getTaskName();
        String str3 = viewedTask.getDescription();
        String str4 = viewedTask.getDueDate();
        String str5 = viewedTask.getTimeDueDate();

        taskName.setText(str1);
        priority.setText(Integer.toString(priorityTemp));
        taskNotes.setText(str3);
        dueDate.setText(str4);
        dueTime.setText(str5);

    }
    //TODO: test this method - Ant
    public void saveTask(UUID taskID)
    {
        //TODO: fix crashing here - Ant
        //Edits task from array list

        EditText taskName = (EditText) findViewById(R.id.viewTaskName);
        EditText priority = (EditText) findViewById(R.id.viewPriority);
        EditText taskNotes = (EditText) findViewById(R.id.viewDescription);
        //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
        EditText dueDate = (EditText) findViewById(R.id.viewDueDate);
        EditText taskTime = (EditText) findViewById(R.id.viewTime);

        boolean updateCompleted = db.updateTable(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()),dueDate.getText().toString(), taskNotes.getText().toString(), 0, taskID, taskTime.getText().toString());

        if(updateCompleted)
        {
            toast.show();
            returnToHome();

        }
        else
        {
            this.toast = Toast.makeText(this,"Task failed to save", Toast.LENGTH_LONG);
            toast.show();
        }
        */
    }

    public void deleteTask(UUID taskID)
    {
        //deletes the task from the arrayList and database
        //this should be a return to previous screen since viewTask may be accessed from multiple places not just homepage.
        returnToHome();
    }
}
