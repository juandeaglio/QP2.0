package com.example.qp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.Calendar;

public class ActionMap extends AppCompatActivity {

    ColorManager colorManager = MainActivity.colorManager;
    DatabaseHelper db = new DatabaseHelper(this);
    private String projectIDStr;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private AlarmManager am;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_map);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(colorManager.getColorAccent());
        toolbar.setTitleTextColor(colorManager.getHeaderTextColor());
        setSupportActionBar(toolbar);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        Intent intent = getIntent();
        projectIDStr = intent.getStringExtra("ProjectID");
//        ProjectObj currentProj = db.getProject(projectIDStr);
//        currentProj.setStageList(db.getStagesForProject(projectIDStr));
        createActionMap();


        //TODO: Dynamically build/make buttons in vertical linearl layout based on the stageArrayList in the currentProj variable - QP


    }

    public void createActionMap() {
        LinearLayout mapLayout = (LinearLayout) findViewById(R.id.mapContainer);
        mapLayout.removeAllViews();
        final ProjectObj currentProj = db.getProject(projectIDStr);
        currentProj.setStageList(db.getStagesForProject(projectIDStr));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                350, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(0, 0, 0, 150);


        //Build button for the project start
        Button projStartBtn = new Button(ActionMap.this);
        projStartBtn.setText(currentProj.getProjectName());
        projStartBtn.setLayoutParams(new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT));
        projStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActionMap.this, "Project Button", Toast.LENGTH_LONG).show();
            }
        });

        Drawable resImgDiamond = this.getResources().getDrawable(R.drawable.rhombus_outline);
        projStartBtn.setBackground(resImgDiamond);


        if (mapLayout != null) {
            mapLayout.addView(projStartBtn, layoutParams);
        }


        Drawable resImg = this.getResources().getDrawable(R.drawable.rounded_button);

        for (int i = 0; i < currentProj.getStageList().size(); i++) {

            final Button btnShow = new Button(ActionMap.this);
            btnShow.setText("Stage " + (i + 1) + ": " + currentProj.getStageList().get(i).getStageName());
            btnShow.setLayoutParams(new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT));

            btnShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String[] index = btnShow.getText().toString().replace(':',' ').split(" ");
                    int stageIndex = Integer.valueOf(index[1]) - 1;
                    showViewStageDialog(currentProj.getStageList().get(stageIndex));
                    //Toast.makeText(ActionMap.this, "This is working", Toast.LENGTH_LONG).show();
                }
            });

            btnShow.setBackground(resImg);
            layoutParams.setMargins(0,0,0,228);

            if (mapLayout != null) {
                mapLayout.addView(btnShow, layoutParams);
            }


        }


        Button projFinishBtn = new Button(ActionMap.this);
        projFinishBtn.setText(currentProj.getDueDate());
        projFinishBtn.setLayoutParams(new LinearLayout.LayoutParams(350, ViewGroup.LayoutParams.WRAP_CONTENT));
        projFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActionMap.this, "Project Button", Toast.LENGTH_LONG).show();
            }
        });
        projFinishBtn.setBackground(resImgDiamond);


        if (mapLayout != null) {
            mapLayout.addView(projFinishBtn);
        }


    }


    public void showViewStageDialog(final Stage viewStage){
        final Dialog vtDialog = new Dialog(ActionMap.this);
        vtDialog.setTitle("View Stage");
        vtDialog.setContentView(R.layout.view_task_dialog);
        Toolbar toolbarDialog = vtDialog.findViewById(R.id.toolbar3);
        toolbarDialog.setBackgroundColor(colorManager.getColorAccent());
        toolbarDialog.setTitleTextColor(colorManager.getHeaderTextColor());
        TextView temp = vtDialog.findViewById(R.id.textView2);
        temp.setText("View Stage");

        vtDialog.show();
        
        if (viewStage == null){
            return;
        }
       
        TextView viewTaskName = (TextView)vtDialog.findViewById(R.id.stageNameDialog);
        viewTaskName.setText(viewStage.getStageName());

        TextView viewTaskTime = (TextView) vtDialog.findViewById(R.id.stageTime);
        viewTaskTime.setText(viewStage.getStageTimeDue());

        viewTaskTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hourOfDay = Calendar.HOUR_OF_DAY;
                int minute = Calendar.MINUTE;
                TimePickerDialog timePicker = new TimePickerDialog(ActionMap.this, mTimeSetListener,hourOfDay,minute, false );

                //timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TextView taskTime = (TextView) vtDialog.findViewById(R.id.stageTime);
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
                //taskTimeValue = tempText + ":" + minuteStr + " " + am_pm;
                taskTime.setText(tempText + ":" + minuteStr + " " + am_pm);
            }
        };





        //TODO: Make taskTime and taskDueDate listeners like in create Task dialog - Ethan

        final TextView viewTaskDueDate = (TextView) vtDialog.findViewById(R.id.stageDueDate);
        viewTaskDueDate.setText(viewStage.getStageDueDate());

        viewTaskDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(ActionMap.this, android.R.style.Theme_Material_Dialog_Alert, mDateSetListener, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                viewTaskDueDate.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                //taskDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);

            }
        };

        final EditText viewTaskDescription = (EditText) vtDialog.findViewById(R.id.taskDescription);
        viewTaskDescription.setText(viewStage.getStageDescription());

     


        final Button deleteButton = (Button) vtDialog.findViewById(R.id.deleteButton);
        deleteButton.setBackgroundColor(colorManager.getColorAccent());
        deleteButton.setTextColor(colorManager.getHeaderTextColor());
        //deleteButton.setCompoundDrawableTintList(ColorStateList.valueOf(colorManager.getColorText()));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletePrompt deletePrompt = new DeletePrompt();
                DatabaseHelper db = new DatabaseHelper(ActionMap.this);
                //db.deleteTask(taskID);

                //mainActivity.populateArrayList(db, mainActivity.sortSelector);
                //updateData();
                vtDialog.dismiss();


            }
        });

        Button saveButton = (Button) vtDialog.findViewById(R.id.saveTaskButtonDialog);
        saveButton.setTextColor(colorManager.getHeaderTextColor());
        saveButton.setBackgroundColor(colorManager.getColorAccent());
       // saveButton.setCompoundDrawableTintList(ColorStateList.valueOf(colorManager.getColorText()));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText taskName = (EditText) vtDialog.findViewById(R.id.stageNameDialog);
                if(taskName.getText().length() == 0){
                    taskName.setError("Task Name cannot be Blank");
                    return;
                }

                TextView dueDate = vtDialog.findViewById(R.id.stageDueDate);
                if(dueDate.getText().toString().length() == 0){
                    dueDate.setError("Task Due Date cannot be blank");
                    return;
                }

                TextView taskTime = (TextView) vtDialog.findViewById(R.id.stageTime);
//                if(taskTime.getText().toString().length() == 0){
//                    taskTime.setError("Task Time cannot be blank");
//                    return;
//                }

                EditText taskNotes = (EditText) vtDialog.findViewById(R.id.taskDescription);
                if(taskNotes.getText().length() == 0){
                    taskNotes.setText(""); // IIf user didn't specify priority just set to 1
                }

                TextView priority = (TextView) vtDialog.findViewById(R.id.taskPriorityDialog);
                if(priority.getText().length() == 0){
                    priority.setText("1"); // IIf user didn't specify priority just set to 1
                }
               int id =  (int) System.currentTimeMillis();
                boolean updateCompleted = db.updateStageTable(viewStage.getStageID().toString(),taskName.getText().toString(),dueDate.getText().toString(),dueDate.getText().toString(),taskNotes.getText().toString(),String.valueOf(id),viewStage.getStageNum(),viewStage.getProjectID());

                //TODO: when having 2 tasks on home screen. If you change the details of one task the task will overwrite itself with data form other task

               //boolean updateCompleted = false;
                if(updateCompleted)
                {
                    Calendar calendar = Calendar.getInstance();
                    Intent intent1 = new Intent(ActionMap.this, BroadCastService.class);
                    intent1.putExtra("Task Name",taskName.getText().toString());
                    PendingIntent pendingIntent = PendingIntent.getService(ActionMap.this, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager am = (AlarmManager) ActionMap.this.getSystemService(ActionMap.this.ALARM_SERVICE);

                    String taskDueDate = taskTime.getText().toString();
                    String hour,minute = "";
                    if(taskTime.getText().toString().equals("")){
                        hour = "12";
                        minute = "00";
                    }
                    else {
                        hour = dueDate.getText().toString().substring( 0, dueDate.getText().toString().indexOf(":"));
                        minute = dueDate.getText().toString().substring(taskDueDate.indexOf(":")+1, dueDate.getText().toString().indexOf(" "));

                    }

                    int hourToInt = Integer.parseInt(hour);
                    int minuteToInt = Integer.parseInt(minute);

                    calendar.set(Calendar.HOUR_OF_DAY, hourToInt);
                    calendar.set(Calendar.MINUTE, minuteToInt);
                    calendar.set(Calendar.SECOND,0);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),pendingIntent);
                    //mainActivity.populateArrayList(db, mainActivity.sortSelector);
                    //updateData();
                    Toast.makeText(ActionMap.this, "Stage edits saved!", Toast.LENGTH_SHORT).show();
                    createActionMap();
                    vtDialog.dismiss();
                }
                else
                {
                     Toast.makeText(ActionMap.this,"Failed to save Task edits", Toast.LENGTH_LONG).show();
                }


            }
        });



    }


}
