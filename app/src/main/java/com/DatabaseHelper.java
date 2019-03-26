package com;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "task.db";
    public static final String TABLE_NAME = "task_table";
    public static final String COL_1 = "Task_Name";
    public static final String COL_2 = "Task_Due_Date";
    public static final String COL_3 = "Task_Priority";
    public static final String COL_4 = "Task_Description";
    public static final String COL_5 = "Task_Completed"; //This data will be an int. 1 for completed, 0 for not


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase(); //Get instance of our database so we can use this class in other classes
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Task_Name varchar(255), Task_Due_Date varchar(255), Task_Priority INT, Task_Description varchar(255), Task_Completed INT)"); //SQL querey creating our database

    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //If the table already exists in android studio we ignore
        onCreate(db);
    }
}
