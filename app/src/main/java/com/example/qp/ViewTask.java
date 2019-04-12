package com.example.qp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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
    DatabaseHelper db = new DatabaseHelper(this);
    Toast toast = null;
    String taskIDStr;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Calendar calendar = Calendar.getInstance();



    private Intent myIntent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        Intent myIntent = getIntent();
        this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);


        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        this.taskIDStr = myIntent.getStringExtra("taskid");
        final UUID taskID = UUID.fromString(taskIDStr);
        displayTask(taskID);
        Button saveTaskBtn = findViewById(R.id.editViewTask);
        saveTaskBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (saveTask(taskID)){
                    goHome(v);
                }


            }
        });

        TextView dueDate = findViewById(R.id.viewDueDate);
        dueDate.setOnClickListener(new View.OnClickListener(){

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
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                //taskDueDateValue = String.valueOf(month + 1) +  "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
                TextView dueDateF = findViewById(R.id.viewDueDate);

                dueDateF.setText(String.valueOf((month + 1))+ "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year));
            }
        };

        TextView time = findViewById(R.id.viewTime);
        time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });


    }


    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView taskTime = (TextView) findViewById(R.id.viewTime);
        String am_pm = "";
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if(calendar.get(Calendar.AM_PM) == Calendar.AM){
            am_pm = "AM";
        }
        else if(calendar.get(Calendar.AM_PM) == Calendar.PM){
            am_pm = "PM";
        }
        String tempText = (calendar.get(Calendar.HOUR) == 0) ?"12":calendar.get(Calendar.HOUR)+"";
        taskTime.setText(tempText+":"+calendar.get(Calendar.MINUTE)+" "+am_pm );
        //.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + am_pm);

        //this.taskTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute) + " " + am_pm;

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


    public void deleteTask(View view){
        DeletePrompt deletePrompt = new DeletePrompt();
        deletePrompt.show(getSupportFragmentManager(), "deletePrompt");
//        db.deleteTask(this.taskIDStr);
//        startActivity(new Intent(this, MainActivity.class));

    }

    //TODO: goHome should be changed to just go back to screen that was before this (can't assume it was home)
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
        TextView dueDate = (TextView) findViewById(R.id.viewDueDate);
        TextView dueTime = (TextView) findViewById(R.id.viewTime);
        int priorityTemp = viewedTask.getPriority();
        String textBoxName = viewedTask.getTaskName();
        String textBoxDescription = viewedTask.getDescription();
        String textBoxDueDate = viewedTask.getDueDate();
        String textBoxTime = viewedTask.getTimeDueDate();

        taskName.setText(textBoxName);
        priority.setText(Integer.toString(priorityTemp));
        taskNotes.setText(textBoxDescription);
        dueDate.setText(textBoxDueDate);
        dueTime.setText(textBoxTime);

    }
    //TODO: test this method - Ant
    public boolean saveTask(UUID taskID)
    {
        //TODO: fix crashing here - Ant
        //Edits task from array list


        EditText taskName = (EditText) findViewById(R.id.viewTaskName);
        if(taskName.getText().length() == 0){
            taskName.setError("Task Name cannot be Blank");
            return false;
        }

        TextView dueDate = findViewById(R.id.viewDueDate);
        if(dueDate.getText().toString().length() == 0){
            dueDate.setError("Task Due Date cannot be blank");
            return false;
        }

        TextView taskTime = (TextView) findViewById(R.id.viewTime);
        if(taskTime.getText().toString().length() == 0){
            taskTime.setError("Task Time cannot be blank");
            return false;
        }

        EditText taskNotes = (EditText) findViewById(R.id.viewDescription);
        if(taskNotes.getText().length() == 0){
            taskNotes.setText(""); // IIf user didn't specify priority just set to 1
        }

        EditText priority = (EditText) findViewById(R.id.viewPriority);
        if(priority.getText().length() == 0){
            priority.setText("1"); // IIf user didn't specify priority just set to 1
        }
        //TODO: change dueDate so that the input fields are converted into a Date that can be used by Task class.
        //calendar.set
        boolean updateCompleted = db.updateTable(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()),dueDate.getText().toString(), taskNotes.getText().toString(), 0, UUID.fromString(this.taskIDStr), taskTime.getText().toString());

        if(updateCompleted)
        {
            //TODO: Fix when editing task to go to it's assigned time not the current time on system
            Intent intent1 = new Intent(ViewTask.this, BroadCastService.class);
            intent1.putExtra("Task Name",taskName.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(ViewTask.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) ViewTask.this.getSystemService(ViewTask.this.ALARM_SERVICE);

            String taskDueDate = taskTime.getText().toString();
            String hour = taskDueDate.substring( 0, taskDueDate.indexOf(":"));
            String minute = taskDueDate.substring(taskDueDate.indexOf(":")+1, taskDueDate.indexOf(" "));

            int hourToInt = Integer.parseInt(hour);
            int minuteToInt = Integer.parseInt(minute);

            calendar.set(Calendar.HOUR_OF_DAY, hourToInt);
            calendar.set(Calendar.MINUTE, minuteToInt);
            calendar.set(Calendar.SECOND,0);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
            toast.show();
        }
        else
        {
            this.toast = Toast.makeText(mainActivity,"Task failed to save", Toast.LENGTH_LONG);
            toast.show();
        }

        return true;
    }
}
