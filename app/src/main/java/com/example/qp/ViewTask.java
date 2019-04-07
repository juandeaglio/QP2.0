package com.example.qp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.Calendar;
import java.util.UUID;

public class ViewTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener  {
    MainActivity mainActivity = new MainActivity();
    private TextView dueDate;
    DatabaseHelper db = new DatabaseHelper(this);
    Toast toast = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_task);
        Intent myIntent = getIntent();
        this.toast = Toast.makeText(this,"Task Successfuly Saved!", Toast.LENGTH_SHORT);
        Toolbar toolbar = findViewById(R.id.toolbar);

        TextView time = findViewById(R.id.viewTime);
        time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

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


        this.dueDate = findViewById(R.id.viewDueDate);
        this.dueDate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal  = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ViewTask.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("Date Picker", "onDateSet: date " + (month + 1)+ "/" + dayOfMonth + "/" + year);
                 //String.valueOf(month + 1) +  "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
                dueDate.setText(String.valueOf(month + 1) +  "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year));
            }
        };



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
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView taskTime = (TextView) findViewById(R.id.viewTime);
        taskTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        //this.taskTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
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
        //TextView dueDate = (TextView) findViewById(R.id.viewDueDate);
        TextView dueTime = (TextView) findViewById(R.id.viewTime);
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
        TextView dueDate = (TextView) findViewById(R.id.viewDueDate);
        TextView taskTime = (TextView) findViewById(R.id.viewTime);

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

    }
}
