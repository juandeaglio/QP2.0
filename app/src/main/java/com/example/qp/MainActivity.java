package com.example.qp;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
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
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {

    public static ArrayList<Task> globalTaskList = new ArrayList<>();
    public static ArrayList<Task> globalCompletedTaskList = new ArrayList<>();
    DatabaseHelper db = new DatabaseHelper(this);
    SQLiteDatabase taskDB;
    private Toast toast = null;
    public static final String CHANNEL_ID = "com.chikeandroid.tutsplustalerts.ANDROID";
    public static final String CHANNEL_NAME = "ANDROID CHANNEL";
    private NotificationManager notifManager;
    private TaskCardRecyclerAdapter adapter;
    SwipeController swipeController;
    public static ColorManager colorManager;
    public Toolbar toolbar;
    public FloatingActionButton fab;
    public String sortSelector = "Task_Priority"; // Default sorting priority

    NavigationView navigationView;

    AlarmManager am;

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
        //boolean firstStart = true;
        if(firstStart){
            showfirstTimeDialog();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        if(db.checkIfColorExists())
        {
            Cursor colorVals = db.getColorValues();
            int colorArr[] = new int[4];
            colorVals.moveToNext();
            colorArr[0] = colorVals.getInt(0);
            colorArr[1] = colorVals.getInt(1);
            colorArr[2] = colorVals.getInt(2);
            colorArr[3] = colorVals.getInt(3);

            colorManager = new ColorManager(0,0,0,0);
            colorManager.setColorPrimary(colorArr[0]);
            colorManager.setColorPrimaryDark(colorArr[1]);
            colorManager.setColorAccent(colorArr[2]);
            colorManager.setColorText(colorArr[3]);
        }
        else
        {
            colorManager = new ColorManager(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)
                    , getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorBackgroundReminder));

            db.insertColorData(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)
                    , getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorBackgroundReminder));
        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        fab = findViewById(R.id.createTaskBtn);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openCreateTaskActivity(view);
            }
        });

        navigationView.setItemIconTintList(ColorStateList.valueOf(colorManager.getColorPrimaryDark()));
        navigationView.getHeaderView(0).setBackgroundColor(colorManager.getColorPrimaryDark());
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

    private void setUpRecyclerView()
    {
        RecyclerView taskRecycler;
        taskRecycler = (RecyclerView) findViewById(R.id.task_card_recycler);
        SwipeControllerActions swipeControllerActions = new SwipeControllerActions() {
            @Override
            public void onLeftSwiped(UUID taskID) { // MARK COMPLETED
                db.deleteTask(taskID.toString());
                populateArrayList(db, sortSelector);
                adapter.updateData();
                toast = Toast.makeText(getApplicationContext(), "Task Deleted Successfully", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onRightSwiped(UUID taskID){ // DELETE
                int taskPendingID = db.getTaskPendingIntent(taskID.toString());
                Intent intent1 = new Intent(MainActivity.this, StartService.class);
                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, taskPendingID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                am.cancel(pendingIntent);
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

        swipeController = new SwipeController(swipeControllerActions, this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(taskRecycler);


    }
    @Override
    protected void onResume()
    {
        super.onResume();
        populateArrayList(this.db, this.sortSelector);

        adapter.updateData();

        if(db.checkIfColorExists())
        {
            Cursor colorVals = db.getColorValues();

            int colorArr[] = new int[4];

            colorVals.moveToNext();
            colorArr[0] = colorVals.getInt(0);
            colorArr[1] = colorVals.getInt(1);
            colorArr[2] = colorVals.getInt(2);
            colorArr[3] = colorVals.getInt(3);

            colorManager = new ColorManager(0,0,0,0);
            colorManager.setColorPrimary(colorArr[0]);
            colorManager.setColorPrimaryDark(colorArr[1]);
            colorManager.setColorAccent(colorArr[2]);
            colorManager.setColorText(colorArr[3]);
        }
        else
        {
            colorManager = new ColorManager(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)
                    , getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorBackgroundReminder));

            db.insertColorData(getResources().getColor(R.color.colorPrimary), getResources().getColor(R.color.colorPrimaryDark)
                    , getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorBackgroundReminder));
        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(colorManager.getColorAccent());

        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(colorManager.getColorAccent());

        fab = findViewById(R.id.createTaskBtn);
        fab.setBackgroundTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                openCreateTaskActivity(view);
            }
        });

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        navigationView.getHeaderView(0).setBackgroundColor(colorManager.getColorAccent());
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

    public void openCreateTaskActivity(View view) {
        startActivity(new Intent(this, CreateTask.class));
        CustomIntent.customType(this, "left-to-right");
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


}