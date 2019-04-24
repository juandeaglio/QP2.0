package com.example.qp;

import android.app.AlarmManager;
import static maes.tech.intentanim.CustomIntent.customType;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.UUID;

import maes.tech.intentanim.CustomIntent;

import static android.app.PendingIntent.getActivity;

public class CreateTask extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, NumberPicker.OnValueChangeListener {

        //Global variable for the array list of tasks
        //MainActivity mainActivity = new MainActivity();
        DatabaseHelper db = new DatabaseHelper(this);

        private TextView dueDate;
        private TextView numberPicker;

        private DatePickerDialog.OnDateSetListener mDateSetListener;
        Toast toast;
        private String taskDueDateValue = "";
        private String taskTime = "";
        Calendar calendar = Calendar.getInstance();
        AlarmManager am;
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_task);
             am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


            this.toast = Toast.makeText(this, "Task Successfully Saved!", Toast.LENGTH_SHORT);
            this.dueDate = findViewById(R.id.taskDueDate);
            Button saveTaskBtn = findViewById(R.id.saveTaskButton);
            Button cancelButton = findViewById(R.id.cancelButton);


            saveTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (saveTask()){
                        goBackToHomepage();
                    }
                    else {
                        toast = Toast.makeText(getApplicationContext(), "Task failed to save", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }


            });

            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goBackToHomepage();

                }
            });

            this.dueDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int day = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog dialog = new DatePickerDialog(CreateTask.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);

                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();

                }
            });


            mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    TextView taskDueDateText = findViewById(R.id.taskDueDateText);
                    calendar.set(year, month, dayOfMonth);
                    Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                    taskDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);
                    taskDueDateText.setText(taskDueDateValue);
                    taskDueDateText.setVisibility(View.VISIBLE);
                }
            };

            TextView time = findViewById(R.id.taskTime);

            time.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");

                }
            });

            numberPicker = findViewById(R.id.priorityNum);
            numberPicker.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showNumberPicker();
                }
            });

        }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "left-to-right");
    }

    public void showNumberPicker() {
            final Dialog d = new Dialog(CreateTask.this);
            d.setTitle("NumberPicker");
            d.setContentView(R.layout.number_picker_dialog);
            Button b1 = (Button) d.findViewById(R.id.button1);
            Button b2 = (Button) d.findViewById(R.id.button2);
            final NumberPicker np = d.findViewById(R.id.numberPicker1);
            np.setMaxValue(5); // max value 100
            np.setMinValue(1);   // min value 0
            np.setWrapSelectorWheel(true);
            np.setOnValueChangedListener(this);
            b1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberPicker.setText(String.valueOf(np.getValue())); //set the value to textview
                    d.dismiss();
                }
            });
            b2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss(); // dismiss the dialog
                }
            });
            d.show();


        }

        //TODO: get day of week
        private String getDayOfWeekStr(int dayOfTheMonth) {
            switch (dayOfTheMonth) {
                case 1:
                    return "Mon";
                case 2:
                    return "Tues";
                case 3:
                    return "Wed";
            }
            return "";
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            TextView taskTime = (TextView) findViewById(R.id.taskTimeText);
            String am_pm = "";
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);

            if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
                am_pm = "AM";
            } else if (calendar.get(Calendar.AM_PM) == Calendar.PM) {
                am_pm = "PM";
            }
            String tempText = (calendar.get(Calendar.HOUR) == 0) ? "12" : calendar.get(Calendar.HOUR) + "";
            String minuteStr = "";

            if (minute <= 9) {
                minuteStr = "0" + String.valueOf(minute);
            } else {
                minuteStr = String.valueOf(minute);
            }
            taskTime.setText(tempText + ":" + minuteStr + " " + am_pm);
            taskTime.setVisibility(View.VISIBLE);

            this.taskTime = String.valueOf(tempText) + ":" + minuteStr + " " + am_pm;

        }

        private String fixTaskName(){
            EditText taskName = (EditText) findViewById(R.id.taskName);
            char capitalLetter = Character.toUpperCase(taskName.getText().toString().charAt(0));
            return taskName.getText().toString().replace(taskName.getText().toString().charAt(0),capitalLetter);


        }




        public boolean saveTask() {


            EditText taskName = (EditText) findViewById(R.id.taskName);
            if(taskName.getText().length() == 0){
                taskName.setError("Title cannot be Blank");
                return false;
            }
            taskName.setText(fixTaskName());

            TextView dueDate = findViewById(R.id.taskDueDate);
            if(this.taskDueDateValue.length() == 0){
                dueDate.setError("Due Date cannot be blank");
                return false;
            }

            if(this.taskTime.length() == 0){
                TextView time = findViewById(R.id.taskTime);
                time.setError("Time cannot be left blank");
                return false;
            }

            Switch allDay = findViewById(R.id.isAllDay);
            if (allDay.isChecked()){
                taskTime = "All day";
                //TODO: change the time of the task to just go off at the day instead of the time
            }

            EditText taskNotes = (EditText) findViewById(R.id.taskNotes);
            if(taskNotes.getText().length() == 0){
                taskNotes.setText(""); // IIf user didn't specify priority just set to 1
            }

            TextView priority = (TextView) findViewById(R.id.priorityNum);
            if(priority.getText().toString().equals("")){
                priority.setText("1"); // IIf user didn't specify priority just set to 1
            }

            UUID taskID = UUID.randomUUID();

            boolean saveCompleted = db.insertData(taskName.getText().toString(), Integer.parseInt(priority.getText().toString()), this.taskDueDateValue, taskNotes.getText().toString(), 0, taskID, this.taskTime);

            if (saveCompleted) {
                Intent intent1 = new Intent(CreateTask.this, StartService.class);
                intent1.putExtra("Task Name", taskName.getText().toString());

                int id = (int) System.currentTimeMillis();
                PendingIntent pendingIntent = PendingIntent.getService(this, id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();

                if (diff > 0) { // To accommodate if the user input's time from the past e.g Current Date: 4/15/19 1:03 p.m Set Date : 4/14/19 1:03 p.m
                    calendar.add(Calendar.DATE, 1);
                }

                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                toast.show();
                return true;
            } else {

                this.toast = Toast.makeText(getApplicationContext(), "Task failed to save", Toast.LENGTH_LONG);
                toast.show();
                return false;

            }
        }

        public void goBackToHomepage() {
            CustomIntent.customType(this, "right-to-left");

            this.finish();
        }


        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

        }
}