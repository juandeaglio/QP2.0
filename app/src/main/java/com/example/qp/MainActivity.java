package com.example.qp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Context;
import static maes.tech.intentanim.CustomIntent.customType;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.DatabaseHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener{

    public static ArrayList<Task> globalTaskList = new ArrayList<>();
    public static ArrayList<Task> globalCompletedTaskList = new ArrayList<>();
    DatabaseHelper db = new DatabaseHelper(this);
    SQLiteDatabase taskDB;
    private Toast toast = null;
    public static final String CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";
    public static final String CHANNEL_NAME = "ANDROID CHANNEL";
    private NotificationManager notifManager;
    private TaskCardRecyclerAdapter adapter;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private AlarmManager am;

    SwipeController swipeController;
    public static ColorManager colorManager;
    public Toolbar toolbar;
    private FloatingActionButton fab;
    public String sortSelector = "Task_Priority"; // Default sorting priority
    private String taskTimeValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


        SharedPreferences preferences = getSharedPreferences("prefs", MODE_PRIVATE);

        boolean firstStart = preferences.getBoolean("firstStart", true);
        if(firstStart){
            showfirstTimeDialog();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        colorManager = new ColorManager(MainActivity.this);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        fab = findViewById(R.id.createTaskBtn);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openCreateTaskDialog(view);
            }
        });


        populateArrayList(this.db, this.sortSelector);

        adapter = new TaskCardRecyclerAdapter(globalTaskList, this);
        setUpRecyclerView();
    }

    private void  showfirstTimeDialog(){

        startActivity(new Intent(this, IntroActivity.class));

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor =  prefs.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
        }



    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.task_card_long_press, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.option1:
                this.toast  = Toast.makeText(getApplicationContext(), "Implement me", Toast.LENGTH_SHORT);
                toast.show();
                break;
            case R.id.option2:
                this.toast  = Toast.makeText(getApplicationContext(), "Implement NOW!", Toast.LENGTH_SHORT);
                toast.show();

        }


        return super.onContextItemSelected(item);
    }

    private void setUpRecyclerView(){
        RecyclerView taskRecycler;
        taskRecycler = (RecyclerView) findViewById(R.id.task_card_recycler);
        SwipeControllerActions swipeControllerActions = new SwipeControllerActions() {
            @Override
            public void onLeftSwiped(UUID taskID) {
                db.deleteTask(taskID.toString());
                populateArrayList(db, sortSelector);
                adapter.updateData();
                toast = Toast.makeText(getApplicationContext(), "Task Deleted Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onRightSwiped(UUID taskID){
                db.markTaskCompleted(taskID.toString());
                populateArrayList(db, sortSelector);
                populateCompletedTaskList(db, sortSelector);
                adapter.updateData();
                toast = Toast.makeText(getApplicationContext(), "Task Marked As Completed", Toast.LENGTH_SHORT);
                toast.show();
            }
        };
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        taskRecycler.setLayoutManager(layoutManager);
        taskRecycler.setAdapter(adapter);

        swipeController = new SwipeController(swipeControllerActions);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(taskRecycler);


    }
    @Override
    protected void onResume()
    {
        super.onResume();
        populateArrayList(this.db, this.sortSelector);
        adapter.updateData();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(colorManager.getColorAccent());

        fab = findViewById(R.id.createTaskBtn);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openCreateTaskDialog(view);
            }
        });

    }

    public void createNotification(String aMessage, Context context) {
        final int NOTIFY_ID = 0; // ID of notification
        String id = CHANNEL_ID; // default_channel_id
        String title = (CHANNEL_NAME); // Default Channel
        Intent intent;
        PendingIntent pendingIntent;
        NotificationCompat.Builder builder;
        if (notifManager == null) {
            notifManager = (NotificationManager)context.getSystemService(Service.NOTIFICATION_SERVICE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(id);
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, title, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(context, id);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required
                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        }
        else {
            builder = new NotificationCompat.Builder(context, id);
            intent = new Intent(context, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            builder.setSound(sound);
            pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            builder.setContentTitle(aMessage)                            // required
                    .setSmallIcon(android.R.drawable.ic_popup_reminder)   // required

                    .setContentText(context.getString(R.string.app_name)) // required
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setTicker(aMessage)
                    .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400})
                    .setPriority(Notification.PRIORITY_HIGH);
        }
        Notification notification = builder.build();
        notifManager.notify(NOTIFY_ID, notification);
    }




    public void populateArrayList(DatabaseHelper db, String sortSelector){
        this.taskDB = db.getWritableDatabase();
        globalTaskList.clear();
        Cursor cursor = db.sortUnCompletedTasks(sortSelector); //Going to assume the user wants in ascending order?

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
                newTask.setTimeDueDate(cursor.getString(6));
                MainActivity.globalTaskList.add(newTask); //Adds it to the global array list
            }while (cursor.moveToNext());

        }


    }

    public void populateCompletedTaskList(DatabaseHelper db, String sortSelector){
        this.taskDB = db.getWritableDatabase();
        globalCompletedTaskList.clear();
        Cursor cursor = db.sortCompletedTasks(sortSelector);

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
                newTask.setTimeDueDate(cursor.getString(6));
                MainActivity.globalCompletedTaskList.add(newTask); //Adds it to the global array list
            }while (cursor.moveToNext());

        }
    }



    public void openViewTask() {
        startActivity(new Intent(this, ViewTask.class));
    }

    public void openReminderActivity() {
        startActivity(new Intent(this, Reminder.class));
        CustomIntent.customType(this, "left-to-right");

    }

    public void openCreateTaskDialog(View view) {
//        CreateTaskDialogHandler createTaskDialogHandler = new CreateTaskDialogHandler();
//        //createTaskDialogHandler.show(getSupportFragmentManager(), "Create Task Dialog");
//        //createTaskDialogHandler.onCreate();
//        createTaskDialogHandler.show();


        final Dialog ctDialog = new Dialog(this);
        ctDialog.setTitle("Create New Task");
        ctDialog.setContentView(R.layout.create_task_dialog);
        ctDialog.show();
        final EditText taskNameDialog = ctDialog.findViewById(R.id.taskNameDialog);
        EditText taskDescription = ctDialog.findViewById(R.id.taskDescription);

        Button saveButtonDialog = (Button)ctDialog.findViewById(R.id.saveTaskButtonDialog);

        saveButtonDialog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(taskNameDialog.getText().length() == 0){
                    taskNameDialog.setError("Title cannot be Blank");
                    return;
                }
                taskNameDialog.setText(fixTaskName(taskNameDialog.getText().toString()));

                TextView dueDate = ctDialog.findViewById(R.id.taskDueDate);
                if(dueDate.length() == 0){
                    dueDate.setError("Due Date cannot be blank");
                    return;
                }
                TextView taskTime = (TextView) ctDialog.findViewById(R.id.taskTimeDialog);
                taskTime.setText(taskTimeValue);
                if(taskTime.getText().length() == 0){
                    TextView time = findViewById(R.id.taskTimeDialog);
                    time.setError("Time cannot be left blank");
                    return;
                }

                Switch allDay = (Switch) ctDialog.findViewById(R.id.isAllDay);
                if (allDay.isChecked()){
                    //taskTime = "All day";
                    //TODO: change the time of the task to just go off at the day instead of the time
                }

                EditText taskNotes = (EditText) ctDialog.findViewById(R.id.taskDescription);
                if(taskNotes.getText().length() == 0){
                    taskNotes.setText(""); // IIf user didn't specify priority just set to 1
                }

//                TextView priority = (TextView) findViewById(R.id.priorityNum);
//                if(priority.getText().toString().equals("")){
//                    priority.setText("1"); // IIf user didn't specify priority just set to 1
//                }

                UUID taskID = UUID.randomUUID();

                boolean saveCompleted = db.insertData(taskNameDialog.getText().toString(), 1, dueDate.getText().toString(), taskNotes.getText().toString(), 0, taskID, taskTime.getText().toString());

                if (saveCompleted) {
                    Intent intent1 = new Intent(MainActivity.this, StartService.class);
                    intent1.putExtra("Task Name", taskNameDialog.getText().toString());

                    int id = (int) System.currentTimeMillis();
                    PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, id, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

                    long diff = Calendar.getInstance().getTimeInMillis() - calendar.getTimeInMillis();

                    if (diff > 0) { // To accommodate if the user input's time from the past e.g Current Date: 4/15/19 1:03 p.m Set Date : 4/14/19 1:03 p.m
                        calendar.add(Calendar.DATE, 1);
                    }

                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    toast = Toast.makeText(MainActivity.this, "Task Successfully Saved!", Toast.LENGTH_SHORT);

                    toast.show();
                    ctDialog.dismiss();
                    populateArrayList(db, sortSelector);
                    adapter.updateData();
                } else {

                    toast = Toast.makeText(getApplicationContext(), "Task failed to save", Toast.LENGTH_LONG);
                    toast.show();
                    //return false;

                }

            }
        });



        Button cancelButtonDialog = (Button)ctDialog.findViewById(R.id.cancelButtonDialog);
        cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ctDialog.dismiss();
            }
        });



        final TextView taskDueDate = ctDialog.findViewById(R.id.taskDueDate);

        taskDueDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, android.R.style.Theme_Holo_Dialog_MinWidth, mDateSetListener, year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Log.d("Date Picker", "onDateSet: date " + (month + 1) + "/" + dayOfMonth + "/" + year);
                taskDueDate.setText((month + 1) + "/" + (dayOfMonth) + "/" + year);
                //taskDueDateValue = String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth) + "/" + String.valueOf(year);

            }
        };




        TextView time = ctDialog.findViewById(R.id.taskTimeDialog);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");

            }
        });


    }


    public void openCalendarView() {
        startActivity(new Intent(MainActivity.this, CalendarView.class));
        CustomIntent.customType(this, "left-to-right");

    }

    public void openCompletedTasks() {
        startActivity(new Intent(MainActivity.this, CompletedTasks.class));
        CustomIntent.customType(this, "right-to-left");
    }

    public void openCustomizationActivity(){
        startActivity(new Intent(this, Customization.class));
        CustomIntent.customType(this, "right-to-left");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mSortPriority:
                //Maybe use mDB.SortTable()?
                this.sortSelector = "Task_Priority";
                populateArrayList(this.db, sortSelector);
                adapter.updateData();
                break;
            case R.id.mSortDate:
                this.sortSelector = "Task_Due_Date";
                populateArrayList(this.db, sortSelector);
                adapter.updateData();
                break;
            case R.id.mSortNames:
                this.sortSelector = "Task_Name";
                populateArrayList(this.db, sortSelector);
                adapter.updateData();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) { // to be further implemented -keghvart hagopian.
        int id = item.getItemId();

        if (id == R.id.nav_calendar) {
            openCalendarView();

        } else if (id == R.id.nav_completed_tasks) {
            openCompletedTasks();

        }  else if (id == R.id.nav_reminder) {
            openReminderActivity();
        }else if(id == R.id.customization){
            openCustomizationActivity();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Dialog ctDialog = new Dialog(this);
        ctDialog.setContentView(R.layout.create_task_dialog);
        TextView taskTime = (TextView) ctDialog.findViewById(R.id.taskTimeDialog);
        String am_pm = "";
        calendar = Calendar.getInstance();
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
        this.taskTimeValue = tempText + ":" + minuteStr + " " + am_pm;
        taskTime.setText(taskTimeValue);
    }



    private String fixTaskName(String taskName){
        char capitalLetter = Character.toUpperCase(taskName.charAt(0));
        return taskName.replace(taskName.charAt(0),capitalLetter);


    }


//    @Override
//    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//
//    }
}