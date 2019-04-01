package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qp.CreateTask;
import com.example.qp.MainActivity;

import java.util.Date;
import java.util.UUID;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task.db";
    public static final String TABLE_NAME = "task_table";
    public static final String COL_1 = "Task_Name";
    public static final String COL_2 = "Task_Due_Date";
    public static final String COL_3 = "Task_Priority";
    public static final String COL_4 = "Task_Description";
    public static final String COL_5 = "Task_Completed"; //This data will be an int. 1 for completed, 0 for not
    public static final String COL_6 = "Task_ID";

    public String[] allColumns = {COL_1, COL_2, COL_3, COL_4, COL_5, COL_6}; //Some parameters require that you pass in all the columns being affected, updated, sorted, etc...

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Task_Name varchar(255), Task_Due_Date varchar(255), Task_Priority INT, Task_Description varchar(255), Task_Completed INT, Task_ID varchar(255))"); //SQL querey creating our database

    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //If the table already exists in android studio we ignore
        onCreate(db);
    }

    public boolean insertData(String taskName, int priority, String dueDate, String description, int isCompleted, UUID taskID){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskName);
        contentValues.put(COL_2, dueDate);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, isCompleted);
        contentValues.put(COL_6, taskID.toString());
        long result = db.insert(TABLE_NAME, null, contentValues); //Will return -1 if not inserted properly

        if(result == -1){
            return false;

        }
        else {
            return true;
        }
    }

    public Cursor getAllDataFromTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME,null);

        return  result;
    }

    //TODO: needs testing - Ethan
    public boolean updateTable(String taskName, int priority, String dueDate, String description, int isCompleted, UUID taskID){
        SQLiteDatabase tempDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskName);
        contentValues.put(COL_2, dueDate);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5,isCompleted);
        contentValues.put(COL_6, taskID.toString());

        tempDb.update(TABLE_NAME , contentValues, "Task_ID = ?", new String[] { taskID.toString() });
        return true;
    }


    //Returns the due date to a specific task id
    public String getTaskDueDate(String taskID){
        Cursor data = getAllDataFromTable();

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
        Cursor data = getAllDataFromTable();
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

    public Cursor sortTable(String column, String order){
        //Thinking we clear the data table and repopulate it after we sort the table
        //MainActivity.globalTaskList.clear();
        Cursor sortedTable = this.getWritableDatabase().query(TABLE_NAME, this.allColumns,null,null,null,null, column + " " + order); //ex: Task_Priority(Column) + order("asc" or "desc")
        return sortedTable;
    }






}
