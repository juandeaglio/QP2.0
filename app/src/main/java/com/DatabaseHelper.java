package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task.db";
    public static final String TABLE_NAME = "task_table";
    //COLS for task table
    public static final String COL_1 = "Task_Name";
    public static final String COL_2 = "Task_Due_Date";
    public static final String COL_3 = "Task_Priority";
    public static final String COL_4 = "Task_Description";
    public static final String COL_5 = "Task_Completed"; //This data will be an int. 1 for completed, 0 for not
    public static final String COL_6 = "Task_ID";
    public static final String COL_7 = "Task_Time";


    public static final String REMINDERS_TABLE_NAME = "reminders_table";
    // COLS for Reminders table
    public static final String R_COL_1 = "Intent_ID";
    public static final String R_COL_2 = "Reminder_ID";


    public static final String COLORS_TABLE_NAME = "colors_table";
    // COLS for Colors table
    public static final String COLOR_COL_1 = "Color_Primary";
    public static final String COLOR_COL_2 = "Color_Primary_Dark";
    public static final String COLOR_COL_3 = "Color_Primary_Accent";
    public static final String COLOR_COL_4 = "Text_Color";





    public String[] allColumns = {COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, COL_7};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Task_Name varchar(255), Task_Due_Date varchar(255), Task_Priority INT, Task_Description varchar(255), Task_Completed INT, Task_ID varchar(255), Task_Time varchar(255))"); //SQL querey creating our database
        db.execSQL("create table " + REMINDERS_TABLE_NAME + "(Intent_ID int, Reminder_ID varchar(255))"); //SQL query to create the reminder table
        db.execSQL("create table " + COLORS_TABLE_NAME + "(Color_Primary int, Color_Primary_Dark int, Color_Primary_Accent int, Text_Color int)");
    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //If the table already exists in android studio we ignore
        db.execSQL("DROP TABLE IF EXISTS " + REMINDERS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertReminderData(int IntentID, String ReminderID){
        SQLiteDatabase reminderDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(R_COL_1, IntentID);
        contentValues.put(R_COL_2, ReminderID);
        long result = reminderDB.insert(REMINDERS_TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }


    public boolean inserColorData(int color_Primary, int color_Primary_Dark, int color_Primary_Accent, int text_Color){
        SQLiteDatabase colorDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOR_COL_1, color_Primary);
        contentValues.put(COLOR_COL_2, color_Primary_Dark);
        contentValues.put(COLOR_COL_3, color_Primary_Accent);
        contentValues.put(COLOR_COL_4, text_Color);


        long result = colorDB.insert(COLORS_TABLE_NAME, null, contentValues);

        if (result == -1){
            return false;
        }
        else {
            return true;
        }
    }

    public int getIntentID(String reminderID){
        SQLiteDatabase reminderDB = getWritableDatabase();

        Cursor result = reminderDB.rawQuery("select Intent_ID from " + REMINDERS_TABLE_NAME + " where " + R_COL_2 + " = '" + reminderID + "'",null);
        if (result.moveToFirst()){
            return result.getInt(0);
        }
        return -1; //Error
    }

    public boolean insertData(String taskName, int priority, String dueDate, String description, int isCompleted, UUID taskID, String taskTime){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskName);
        contentValues.put(COL_2, dueDate);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, isCompleted);
        contentValues.put(COL_6, taskID.toString());
        contentValues.put(COL_7, taskTime);

        long result = db.insert(TABLE_NAME, null, contentValues); //Will return -1 if not inserted properly

        if(result == -1){
            return false;

        }
        else {
            return true;
        }
    }

    public Cursor getAllUnCompletedTasksFromTable(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_5 + " != 0",null);

        return  result;
    }

    public Cursor getAllTasksFromtable(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME ,null);

        return  result;
    }

    public boolean updateTable(String taskName, int priority, String dueDate, String description, int isCompleted, UUID taskID, String taskTime){
        SQLiteDatabase tempDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskName);
        contentValues.put(COL_2, dueDate);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5,isCompleted);
        contentValues.put(COL_6, taskID.toString());
        contentValues.put(COL_7, taskTime);

        tempDb.update(TABLE_NAME , contentValues, "Task_ID = ?", new String[] { taskID.toString() });
        return true;
    }


    public String getTaskDueDate(String taskID){
        Cursor data = getAllTasksFromtable();

        if ((data.moveToFirst())){
            do {
                if(data.getString(5).equals(taskID)){
                    return data.getString(1);
                }
            }while (data.moveToNext());
        }

        return ""; //Didn't find it
    }

    //Marks the task with the associated TaskID and changes it's completed field to 1
    //On the home page have a function that if this returns tru then you move the task to the global completed list
    public boolean markTaskCompleted(String taskID){
        Cursor data = getAllTasksFromtable();
        SQLiteDatabase tempDB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_5, 1);
        if(data.moveToFirst()){
            do {
                if (data.getString(5).equals(taskID)){
                    tempDB.update(TABLE_NAME, contentValues, "Task_ID = ?", new String[] { taskID});
                    return true; //Successful update
                }
            }while (data.moveToNext());
        }

        return false; // :(
    }



    public Cursor sortCompletedTasks(String sortSelector){
        Cursor sortedTable = this.getWritableDatabase().query(TABLE_NAME + " Where " + COL_5 + " != 0", this.allColumns,null,null,null,null, sortSelector + " " + "asc"); //ex: Task_Priority(Column) + order("asc" or "desc")
        return sortedTable;
    }

    public Cursor sortUnCompletedTasks(String sortSelector){
        Cursor sortedTable = this.getWritableDatabase().query(TABLE_NAME + " Where " + COL_5 + " != 1", this.allColumns,null,null,null,null, sortSelector + " " + "asc"); //ex: Task_Priority(Column) + order("asc" or "desc")
        return sortedTable;
    }

    public boolean unCheckCompletedTask(String taskID){
        Cursor data = getAllTasksFromtable();
        SQLiteDatabase tempDB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_5, "0");
        if(data.moveToFirst()){
            do {
                if (data.getString(5).equals(taskID)){
                    tempDB.update(TABLE_NAME, contentValues, "Task_ID = ?", new String[] { taskID});
                    return true; //Successful update
                }
            }while (data.moveToNext());
        }

        return false;
    }

    public void deleteTask(String taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        String querey = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL_6 + " = '" + taskID + "'";

        db.execSQL(querey);
    }

    public void deleteAllCompletedTasks(){
        SQLiteDatabase db = this.getWritableDatabase();
        String querey = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_5 + " = 1";
        db.execSQL(querey);
    }






}
