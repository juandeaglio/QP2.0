package com.example.qp;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.Calendar;
import java.util.UUID;

public class CreateTask extends AppCompatActivity {

    //Global variable for the array list of tasks
    MainActivity mainActivity = new MainActivity();
    DatabaseHelper db = new DatabaseHelper(this);

    private TextView dueDate;
    private DatePickerDialog.OnDateSetListener mDateSetListeener;
    Toast toast = null;
    private String taskDueDateValue = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        this.dueDate = findViewById(R.id.taskDueDate);


        Button saveTaskBtn = findViewById(R.id.saveTaskButton);
        this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);

        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task taskToSave = new Task();
                saveTask();
                goBackToHomepage();

            }



        });

        this.dueDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal  = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateTask.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListeener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();


            }


        });

        mDateSetListeener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("Date Picker", "onDateSet: date " + (month + 1)+ "/" + dayOfMonth + "/" + year);
                taskDueDateValue = String.valueOf(month + 1) +  "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
                dueDate.setText(taskDueDateValue);
            }
        };




    }

    public void populateArrayList(String sortBy, String orderBy){
        MainActivity.globalTaskList.clear();
        Cursor cursor = db.sortTable("Task_Priority", "asc");

        if(cursor.moveToFirst()){
            do {
                //MainActivity.globalTaskList.add();
                Task newTask  = new Task();
                newTask.setTaskName(cursor.getString(0)); //Task Name
                newTask.setDueDate(cursor.getString(1)); // Due Date
                newTask.setPriority(cursor.getInt(2)); //Priority
                newTask.setDescription(cursor.getString(3)); //Description
                newTask.setCompleted(cursor.getShort(4)); //Is Completed: 1 = yes; 2 = no
                newTask.setTaskId(UUID.fromString(cursor.getString(5))); // Check this method in Task class. Generates a random UUID through Java

                MainActivity.globalTaskList.add(newTask); //Adds it to the global array list
            }while (cursor.moveToNext());

        }

    }


    public void saveTask(){
        //Saves task in array list
        //mainActivity.globalTaskList.add(newTask);
        //Task newTask = new Task();
        //goBackToHomepage();

        EditText taskName = (EditText) findViewById(R.id.taskName);
        EditText priority = (EditText) findViewById(R.id.priorityNum);
        EditText taskNotes = (EditText) findViewById(R.id.taskNotes);
        //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
        TextView dueDate =  findViewById(R.id.taskDueDate);
        UUID taskID = UUID.randomUUID();

        boolean saveCompleted = db.insertData(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()),this.taskDueDateValue, taskNotes.getText().toString(), 0, taskID);

        if(saveCompleted == true){
            populateArrayList("Task_Priority", "asc");
            toast.show();
        }
        else {

            this.toast = Toast.makeText(mainActivity,"Task failed to save", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void goBackToHomepage(){
        startActivity(new Intent(CreateTask.this, MainActivity.class));
    }



    public void printNow(){
        System.out.println("Hello");
    }

}
