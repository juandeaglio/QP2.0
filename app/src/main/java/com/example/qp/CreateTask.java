package com.example.qp;

import android.support.v4.*;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class CreateTask extends AppCompatActivity {

    //Global variable for the array list of tasks
    MainActivity mainActivity = new MainActivity();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);


        Button saveTaskBtn = findViewById(R.id.saveTaskButton);

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task newTask = new Task();
                //goBackToHomepage();

                EditText taskName = (EditText) findViewById(R.id.taskName);
                //EditText priority = (EditText) findViewById(R.id.priorityNum);
                EditText taskNotes = (EditText) findViewById(R.id.taskNotes);
                EditText dueDate = (EditText) findViewById(R.id.taskDueDate);

                newTask.setDescription(taskNotes.getText().toString());
                newTask.setTaskName(taskName.getText().toString());
                newTask.setPriority(2);
                newTask.setDueDate(dueDate.getText().toString());

                saveTask(newTask);
                goBackToHomepage();

            }



        });

    }


    public void saveTask(Task newTask){
        //Saves task in array list
        mainActivity.globalTaskList.add(newTask);
        //mainActivity.globalTaskList.sort();

    }

    public void goBackToHomepage(){
        startActivity(new Intent(CreateTask.this, MainActivity.class));
    }



    public void newAnthonyMethod(){
        //new method
    }

    public void printNow(){
        System.out.println("Hello");
    }

}
