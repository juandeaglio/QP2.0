package com.example.qp;

import android.support.v4.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.DatabaseHelper;

public class CreateTask extends AppCompatActivity {

    //Global variable for the array list of tasks
    MainActivity mainActivity = new MainActivity();
    DatabaseHelper db = new DatabaseHelper(this);
    Toast toast = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        Button saveTaskBtn = findViewById(R.id.saveTaskButton);
        this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task newTask = new Task();
                //goBackToHomepage();

                EditText taskName = (EditText) findViewById(R.id.taskName);
                //EditText priority = (EditText) findViewById(R.id.priorityNum);
                EditText taskNotes = (EditText) findViewById(R.id.taskNotes);
                //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
                EditText dueDate = (EditText) findViewById(R.id.taskDueDate);

                newTask.setDescription(taskNotes.getText().toString());
                newTask.setTaskName(taskName.getText().toString());
                newTask.setPriority(2);
                //TODO: change this so it creates a Date() rather than a string
                //newTask.setDueDate(dueDate.getText().toString());

                saveTask(newTask);
                goBackToHomepage();

            }



        });

    }


    public void saveTask(Task newTask){
        //Saves task in array list
        mainActivity.globalTaskList.add(newTask);
        boolean saveCompleted = db.insertData(newTask.getTaskName(), newTask.getPriority(), newTask.getDueDate(), newTask.getDescription(), newTask.getCompleted());

        if(saveCompleted == true){
            toast.show();
        }
        else {
            //Toast.makeText(mainActivity,"Task Failed to save", Toast.LENGTH_LONG);
        }
    }

    public void goBackToHomepage(){
        startActivity(new Intent(CreateTask.this, MainActivity.class));
    }



    public void printNow(){
        System.out.println("Hello");
    }

}
