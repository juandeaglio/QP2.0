package com.example.qp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.UUID;

public class CreateTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    //Global variable for the array list of tasks
    MainActivity mainActivity = new MainActivity();
    DatabaseHelper db = new DatabaseHelper(this);

    private TextView dueDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    Toast toast;
    private String taskDueDateValue = "";
    private String taskTime = "";
    Calendar calendar = Calendar.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
//        TextInputLayout inputTaskName = findViewById(R.id.input_task_name);
//        TextInputLayout inputTaskPriority = findViewById(R.id.input_task_priority);
//        TextInputLayout inputTaskDescription = findViewById(R.id.input_task_description);



        this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);
        this.dueDate = findViewById(R.id.taskDueDate);
        Button saveTaskBtn = findViewById(R.id.saveTaskButton);
        Button cancelButton = findViewById(R.id.cancelButton);


        saveTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Task taskToSave = new Task();
                saveTask();
                goBackToHomepage();
            }



        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                DatePickerDialog dialog = new DatePickerDialog(CreateTask.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });



        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                Log.d("Date Picker", "onDateSet: date " + (month + 1)+ "/" + dayOfMonth + "/" + year);
                taskDueDateValue = String.valueOf(month + 1) +  "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
                dueDate.setText(taskDueDateValue);
            }
        };

        TextView time = findViewById(R.id.taskTime);

        time.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });



    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView taskTime = (TextView) findViewById(R.id.taskTime);
        taskTime.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        this.taskTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);

    }


    /*
    * This function will not reside in MainActivity class
    * */

//    public void populateArrayList(String sortBy, String orderBy, SQLiteDatabase taskDB){
//        MainActivity.globalTaskList.clear();
//        Cursor cursor = db.sortTable("Task_Priority", "asc");
//
//        if(cursor.moveToFirst()){
//            do {
//                //MainActivity.globalTaskList.add();
//                Task newTask  = new Task();
//                newTask.setTaskName(cursor.getString(0)); //Task Name
//                newTask.setDueDate(cursor.getString(1)); // Due Date
//                newTask.setPriority(cursor.getInt(2)); //Priority
//                newTask.setDescription(cursor.getString(3)); //Description
//                newTask.setCompleted(cursor.getShort(4)); //Is Completed: 1 = yes; 2 = no
//                newTask.setTaskId(UUID.fromString(cursor.getString(5))); // Check this method in Task class. Generates a random UUID through Java
//                newTask.setTimeDueDate(cursor.getString(6));
//                MainActivity.globalTaskList.add(newTask); //Adds it to the global array list
//            }while (cursor.moveToNext());
//
//        }
//
//    }


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


//        if(taskName.getText().length() == 0){
//            toast = Toast.makeText(this, "Task name but be longer than 0 characters", Toast.LENGTH_LONG);
//            toast.show();
//        }


        boolean saveCompleted = db.insertData(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()),this.taskDueDateValue, taskNotes.getText().toString(), 0, taskID, this.taskTime);

        if(saveCompleted){
            //mainActivity.populateArrayList(); Commented out because it results in a crash
            Intent intent1 = new Intent(CreateTask.this, BroadCastService.class);
            intent1.putExtra("Task Name",taskName.getText().toString());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(CreateTask.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) CreateTask.this.getSystemService(CreateTask.this.ALARM_SERVICE);
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            toast.show();
        }
        else {

            this.toast = Toast.makeText(getApplicationContext(),"Task failed to save", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void goBackToHomepage(){
        startActivity(new Intent(CreateTask.this, MainActivity.class));
    }



}
