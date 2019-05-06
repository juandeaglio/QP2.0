package com.example.qp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import maes.tech.intentanim.CustomIntent;


public class Reminder extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {
    Calendar mCalendar = Calendar.getInstance();
    private EditText mTitleText;
    private TextView mDateText, mTimeText, mRepeatText, mRepeatNoText, mRepeatTypeText;
    private FloatingActionButton mSaveButton;

    public static ArrayList<ReminderObject> globalReminderList = new ArrayList<>();
    private int mYear, mMonth, mHour, mMinute, mDay;
    private long mRepeatTime;
    private Switch mRepeatSwitch;
    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;
    private Toast toast;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private ReminderObject reminder;

    DatabaseHelper db;
    AlarmManager am;
    private ReminderRecyclerAdapter adapter;

    public ColorManager colorManager;

    public Reminder() {
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        db = new DatabaseHelper(this);

        mTitleText = (EditText) findViewById(R.id.reminder_title);
        mDateText = (TextView) findViewById(R.id.set_date);
        mTimeText = (TextView) findViewById(R.id.set_time);
        mRepeatText = (TextView) findViewById(R.id.set_repeat);
        mRepeatNoText = (TextView) findViewById(R.id.set_repeat_no);
        mRepeatTypeText = (TextView) findViewById(R.id.set_repeat_type);
        mRepeatSwitch = (Switch) findViewById(R.id.repeat_switch);
        mSaveButton = (FloatingActionButton) findViewById(R.id.floatingActionButtonSave);

        // Initialize default values
        mActive = "true";
        mRepeat = "true";
        mRepeatNo = Integer.toString(1);
        mRepeatType = "Hour";

        mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute = mCalendar.get(Calendar.MINUTE);
        mYear = mCalendar.get(Calendar.YEAR);
        mMonth = mCalendar.get(Calendar.MONTH) + 1;
        mDay = mCalendar.get(Calendar.DATE);

        mDate = mMonth + "/" + mDay + "/" + mYear;
        mTime = mHour + ":" + mMinute;
        this.mDateText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = mCalendar.get(Calendar.YEAR);
                int month = mCalendar.get(Calendar.MONTH);
                int day = mCalendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(Reminder.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });

        this.mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReminder();
            }
        });
        // Setup Reminder Title EditText
        mTitleText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitle = s.toString().trim();
                mTitleText.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setup TextViews using reminder values
        mDateText.setText(mDate);
        mTimeText.setText(mTime);
        mRepeatNoText.setText(mRepeatNo);
        mRepeatTypeText.setText(mRepeatType);
        mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDate = (month + 1) + "/" + dayOfMonth + "/" + year;
                mCalendar.set(year, month, dayOfMonth);
                mDateText.setText(mDate);

            }
        };


        colorManager = MainActivity.colorManager;
        //mRepeatSwitch.setThumbTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        //mRepeatSwitch.setTrackTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        mSaveButton.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        LinearLayout header = findViewById(R.id.add_reminder_layout_top);
        header.setBackgroundColor(colorManager.getColorAccent());

        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.reminder_recycler);
        adapter = new ReminderRecyclerAdapter(globalReminderList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "left-to-right");
    }

    public void setDate(View view) {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(Reminder.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setTime(View view) {
        DialogFragment timePicker = new TimePickerFragment();
        timePicker.show(getSupportFragmentManager(), "time picker");
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mTimeText = (TextView) findViewById(R.id.set_time);
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.SECOND, 0);
        mTime = hourOfDay + ":" + minute;
        mTimeText.setText(mTime);
    }

    public void goBackToHomepage() {
        this.finish();
    }

    public void saveReminder() {

        Intent intent1 = new Intent(Reminder.this, StartService.class);
        intent1.putExtra("Task Name", mTitle);

        int intentId = (int) System.currentTimeMillis();
        UUID uniqueID = UUID.randomUUID();
        boolean saveCompleted = false;

        PendingIntent pendingIntent = PendingIntent.getService(Reminder.this, intentId, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        am = (AlarmManager) Reminder.this.getSystemService(ALARM_SERVICE);


        if (mRepeat == "true") {
            reminder = new ReminderObject(mTitle, Integer.parseInt(mRepeatNo), mRepeatType, true);
            globalReminderList.add(reminder);
            saveCompleted = db.insertReminderData(intentId, uniqueID.toString(), mTitle, mDate, mRepeatNo, mRepeatType, mTime);

            switch (mRepeatType) {
                case "Minute":
                    am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Integer.parseInt(mRepeatNo) * 60 * 1000, pendingIntent);
                    break;
                case "Hour":
                    am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Integer.parseInt(mRepeatNo) * AlarmManager.INTERVAL_HOUR, pendingIntent);
                    break;
                case "Day":
                    am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Integer.parseInt(mRepeatNo) * AlarmManager.INTERVAL_DAY, pendingIntent);
                    break;
                case "Week":
                    am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Integer.parseInt(mRepeatNo) * 7 * AlarmManager.INTERVAL_DAY, pendingIntent);
                    break;
                case "Month":
                    am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), Integer.parseInt(mRepeatNo) * AlarmManager.INTERVAL_DAY * 31, pendingIntent);
                    break;
            }

        } else {
            am.set(AlarmManager.RTC_WAKEUP, mCalendar.getTimeInMillis(), pendingIntent);
            reminder = new ReminderObject(mTitle, Integer.parseInt(mRepeatNo), mRepeatType, true);
            globalReminderList.add(reminder);
            //saveCompleted = db.insertReminderData(intentId, uniqueID.toString());
        }

        if (saveCompleted) {
            this.toast = Toast.makeText(this, "Reminder Successfully Saved!", Toast.LENGTH_SHORT);
            toast.show();
            goBackToHomepage();

        } else {
            this.toast = Toast.makeText(this, "Reminder Failed", Toast.LENGTH_SHORT);
            toast.show();
        }
        adapter.notifyDataSetChanged();


        // Database stuff
    }


    public void cancelReminder(PendingIntent intentReminder) {
        am.cancel(intentReminder);
    }

    public void onSwitchRepeat(View view) {
        boolean on = ((Switch) view).isChecked();
        if (on) {
            mRepeat = "true";
            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
        } else {
            mRepeat = "false";
            mRepeatText.setText(R.string.repeat_off);
        }
    }

    public void selectRepeatType(View v) {
        final String[] items = new String[5];

        items[0] = "Minute";
        items[1] = "Hour";
        items[2] = "Day";
        items[3] = "Week";
        items[4] = "Month";

        // Create List Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                mRepeatType = items[item];
                mRepeatTypeText.setText(mRepeatType);
                mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // On clicking repeat interval button
    public void setRepeatNo(View v) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Enter Number");

        // Create EditText box to input repeat number
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        if (input.getText().toString().length() == 0) {
                            mRepeatNo = Integer.toString(1);
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        } else {
                            mRepeatNo = input.getText().toString().trim();
                            mRepeatNoText.setText(mRepeatNo);
                            mRepeatText.setText("Every " + mRepeatNo + " " + mRepeatType + "(s)");
                        }
                    }
                });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // do nothing
            }
        });
        alert.show();
    }


}