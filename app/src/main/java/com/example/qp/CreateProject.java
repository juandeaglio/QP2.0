package com.example.qp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.Calendar;

import maes.tech.intentanim.CustomIntent;

public class CreateProject extends AppCompatActivity {


    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    private DatePickerDialog.OnDateSetListener projectDateSetListener;
    private TimePickerDialog.OnTimeSetListener projectTimeSetListener;
    private NumberPicker.OnValueChangeListener mNumberSetListener;
    DatabaseHelper db = new DatabaseHelper(this);
    private Toast toast = null;
    private AlarmManager am;
    private Project newProject;
    ColorManager colorManager;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_create_project);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        newProject = new Project(); //Create the project, UUID is generated in constructor
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        colorManager = MainActivity.colorManager;
        toolbar.setBackgroundColor(colorManager.getColorAccent());
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        final TextView projectDueDate = (TextView) findViewById(R.id.projectDueDate);
        projectDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateProject.this, projectDateSetListener, year, month, day);

                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        projectDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                //Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                projectDueDate.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                //stageDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);


            }
        };

        final TextView projectTime = (TextView) findViewById(R.id.projectTime) ;
        projectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog timePicker = new TimePickerDialog(CreateProject.this, projectTimeSetListener, hourOfDay, minute, false);
                //timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.show();

            }
        });

        projectTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                //TextView stageTime = (TextView) findViewById(R.id.stageTime);
                String am_pm = "";
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

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
                projectTime.setText(tempText + ":" + minuteStr + " " + am_pm);
            }
        };

        FloatingActionButton fab = findViewById(R.id.createNewStageButton);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                showCreateStageDialog();
            }
        });


        Button cancelButton = (Button) findViewById(R.id.cancelButton);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateProject.this, MainActivity.class));
                CustomIntent.customType(CreateProject.this, "left-to-right");


                finish(); //If the user cancels making a new project wipe the Project ID here
            }
        });


        Button saveButton = (Button) findViewById(R.id.saveProjectButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Save the project and it's stages
                //P---S1---S2---S3---PF
                TextView projName = (TextView) findViewById(R.id.projectName);
                newProject.projectName = projName.getText().toString();
                newProject.dueDate = projectDueDate.getText().toString();
                newProject.timeDueDate = projectTime.getText().toString();
                newProject.completed = 0;

                TextView projDescription = (TextView) findViewById(R.id.projectDescription);
                newProject.description = projDescription.getText().toString();

                for (Stage currentStage : newProject.stageList) {
                    boolean saveCompleted = db.insertStageData(currentStage.getStageID().toString(), currentStage.getStageName(), currentStage.getStageDueDate(), currentStage.getStageDescription(),"1",currentStage.getPendingIntentID(),newProject.projectId.toString());
                    if(saveCompleted){
                        System.out.println("saveCompleted = " + saveCompleted);
                    }
                    else {
                        //error
                        toast = Toast.makeText(CreateProject.this, "ERROR: Couldn't save stage to DB", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                boolean saveProjectData = db.insertProjectData(newProject.projectId.toString(),newProject.projectName, newProject.dueDate, newProject.timeDueDate, newProject.description, newProject.stageList.size());

                if(saveProjectData){
                    toast = Toast.makeText(CreateProject.this, "Project saved successfully!", Toast.LENGTH_LONG);
                    toast.show();
                    startActivity(new Intent(CreateProject.this, MainActivity.class));
                    CustomIntent.customType(CreateProject.this, "left-to-right");

                }
                else {
                    toast = Toast.makeText(CreateProject.this, "Project failed to save", Toast.LENGTH_LONG);
                    toast.show();
                }


            }
        });
    }

    public void setUpRecycler(){
        StageCardRecyclerAdapter adapter;
        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
    }


    public void showCreateStageDialog() {
        final Dialog ctDialog = new Dialog(this);
        ctDialog.setTitle("Create New Stage");
        ctDialog.setContentView(R.layout.create_stage_dialog);
        //ctDialog.findViewById(R.id.toolbar3).setBackgroundColor(colorManager.getColorAccent());
        ctDialog.show();

        final EditText stageNameDialog = ctDialog.findViewById(R.id.stageNameDialog);
        final EditText stageDescription = ctDialog.findViewById(R.id.stageDescription);

        Button cancelButtonDialog = (Button) ctDialog.findViewById(R.id.cancelButtonDialog);
        cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctDialog.dismiss();
            }
        });

        final TextView stageDueDate = ctDialog.findViewById(R.id.stageDueDate);
        ctDialog.findViewById(R.id.toolbar3).setBackgroundColor(colorManager.getColorAccent());
        stageDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(CreateProject.this, mDateSetListener, year, month, day);
                //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                //Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                stageDueDate.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                //stageDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);

            }
        };


        TextView time = ctDialog.findViewById(R.id.stageTime);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int hourOfDay = cal.get(Calendar.HOUR_OF_DAY);
                int minute = cal.get(Calendar.MINUTE);
                TimePickerDialog timePicker = new TimePickerDialog(CreateProject.this, mTimeSetListener, hourOfDay, minute, false);
                //timePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePicker.show();

            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                TextView taskTime = (TextView) ctDialog.findViewById(R.id.stageTime);
                String am_pm = "";
                calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

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


        final TextView stageNum = (TextView) ctDialog.findViewById(R.id.stageNumberDialog);
        stageNum.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showNumberPicker();
            }
        });

        mNumberSetListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                stageNum.setText(String.valueOf(newVal));
            }
        };

        Button saveButtonDialog = (Button) ctDialog.findViewById(R.id.saveStageButtonDialog);

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (stageNameDialog.getText().length() == 0) {
                    stageNameDialog.setError("Title cannot be Blank");
                    return;
                }
                stageNameDialog.setText(fixStageName(stageNameDialog.getText().toString()));

                TextView dueDate = ctDialog.findViewById(R.id.stageDueDate);
                if (dueDate.length() == 0) {
                    dueDate.setError("Due Date cannot be blank");
                    return;
                }
                TextView stageTime = (TextView) ctDialog.findViewById(R.id.stageTime);
                //taskTime.setText(taskTimeValue);
                if (stageTime.getText().length() == 0) {
                    TextView time = findViewById(R.id.stageTime);
                    time.setError("Time cannot be left blank");
                    return;
                }

                Switch allDay = (Switch) ctDialog.findViewById(R.id.isAllDay);
                if (allDay.isChecked()) {
                    //taskTime = "All day";
                    //TODO: change the time of the task to just go off at the day instead of the time
                }

                EditText stageDesc = (EditText) ctDialog.findViewById(R.id.stageDescription);
                if (stageDesc.getText().length() == 0) {
                    stageDesc.setText(""); // IIf user didn't specify priority just set to 1
                }

                TextView priority = (TextView) ctDialog.findViewById(R.id.stageNameDialog);
                if (priority.getText().toString().equals("")) {
                    priority.setText("1"); // IIf user didn't specify priority just set to 1
                }

                //Generating of StageID

                int pendinIntentID = (int) System.currentTimeMillis();


                Stage newStage = new Stage(stageNameDialog.getText().toString(), dueDate.getText().toString(), stageTime.getText().toString(), stageDesc.getText().toString(), 0, String.valueOf(pendinIntentID), newProject.projectId.toString());

                newProject.stageList.add(newStage); //Add the new stage to project to the list of stages in out Project obj


                Intent intent1 = new Intent(CreateProject.this, StartService.class);
                intent1.putExtra("Task Name", stageNameDialog.getText().toString());

                PendingIntent pendingIntent = PendingIntent.getService(CreateProject.this, pendinIntentID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();

                if (diff > 0) { // To accommodate if the user input's time from the past e.g Current Date: 4/15/19 1:03 p.m Set Date : 4/14/19 1:03 p.m
                    calendar.add(Calendar.DATE, 1);
                }

                am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                toast = Toast.makeText(CreateProject.this, "Stage Successfully Saved!", Toast.LENGTH_SHORT);

                toast.show();
                //populateArrayList(db, sortSelector);
                //adapter.updateData();
                ctDialog.dismiss();


            }
        });

    }

    public void showNumberPicker() {
        final Dialog numPicker = new Dialog(this);
        numPicker.setTitle("NumberPicker");
        numPicker.setContentView(R.layout.number_picker_dialog);
        Button b1 = (Button) numPicker.findViewById(R.id.button1);
        Button b2 = (Button) numPicker.findViewById(R.id.button2);
        final NumberPicker np = numPicker.findViewById(R.id.numberPicker1);

        np.setMaxValue(5); // max value 100
        np.setMinValue(1);   // min value 0
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(mNumberSetListener);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // nPDialog.setText(String.valueOf(np.getValue()));
                numPicker.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numPicker.dismiss(); // dismiss the dialog
            }
        });
        numPicker.show();


    }

    private String fixStageName(String taskName) {
        char capitalLetter = Character.toUpperCase(taskName.charAt(0));
        return taskName.replace(taskName.charAt(0), capitalLetter);


    }


}
