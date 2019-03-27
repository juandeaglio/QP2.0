package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Task_Name varchar(255), Task_Due_Date varchar(255), Task_Priority INT, Task_Description varchar(255), Task_Completed INT, Task_ID INT)"); //SQL querey creating our database

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
        contentValues.put(COL_2, dueDate.toString());
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
}
