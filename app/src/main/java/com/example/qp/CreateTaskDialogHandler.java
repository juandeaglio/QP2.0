package com.example.qp;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.qp.R;

import org.w3c.dom.Text;

import java.util.Calendar;


public class CreateTaskDialogHandler extends AppCompatActivity implements  TimePickerDialog.OnTimeSetListener {
    private TextView taskNameDialog;
   // private ExampleDialogListener listener;
   private String taskTimeValue = "";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Calendar calendar = Calendar.getInstance();
    private String taskDueDateValue;



    public void show(){

        Dialog ctDialog = new Dialog(getBaseContext());
        ctDialog.setTitle("Create New Task");
        ctDialog.setContentView(R.layout.create_task_dialog);
        ctDialog.show();
        EditText taskTimeDialog = findViewById(R.id.taskNameDialog);
        EditText taskDescription = findViewById(R.id.taskDescription);




        Button saveButtonDialog = findViewById(R.id.saveTaskButtonDialog);

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String taskName = taskNameDialog.getText().toString();

            }
        });



        TextView taskDueDate = findViewById(R.id.taskDueDate);

        taskDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateTaskDialogHandler.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView taskDueDateText = view.findViewById(R.id.taskDueDate);
                calendar.set(year, month, dayOfMonth);
                Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                taskDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);

            }
        };



        TextView time = findViewById(R.id.taskTimeDialog);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                //todo: Finish getting the suppoertFragmentManager()
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dialog ctDialog = new Dialog(this);
        ctDialog.setTitle("Create New Task");
        ctDialog.setContentView(R.layout.create_task_dialog);
        ctDialog.show();
        EditText taskTimeDialog = findViewById(R.id.taskNameDialog);
        EditText taskDescription = findViewById(R.id.taskDescription);




        Button saveButtonDialog = findViewById(R.id.saveTaskButtonDialog);

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String taskName = taskNameDialog.getText().toString();

            }
        });



        TextView taskDueDate = findViewById(R.id.taskDueDate);

        taskDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateTaskDialogHandler.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                TextView taskDueDateText = view.findViewById(R.id.taskDueDate);
                calendar.set(year, month, dayOfMonth);
                Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                taskDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);

            }
        };



        TextView time = findViewById(R.id.taskTimeDialog);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                //todo: Finish getting the suppoertFragmentManager()
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });




    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TextView taskTime = (TextView) view.findViewById(R.id.taskNameDialog);
        String am_pm = "";
        Calendar calendar = Calendar.getInstance();
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

        this.taskTimeValue = String.valueOf(tempText) + ":" + minuteStr + " " + am_pm;

    }
}