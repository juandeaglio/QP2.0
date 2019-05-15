package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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
    public static final String COL_7 = "Task_Time";

    public String[] allColumns = {COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, COL_7}; //Some parameters require that you pass in all the columns being affected, updated, sorted, etc...

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Task_Name varchar(255), Task_Due_Date varchar(255), Task_Priority INT, Task_Description varchar(255), Task_Completed INT, Task_ID varchar(255), Task_Time varchar(255))"); //SQL querey creating our database

    }

    @Override
    public void onUpgrade (SQLiteDatabase db,int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //If the table already exists in android studio we ignore
        onCreate(db);
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



    public Cursor getAllTasksFromtable(){
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME ,null);

        return  result;
    }

    //TODO: needs testing - Ethan
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

        return false;
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
                    return true;
                }
            }while (data.moveToNext());
        }

        return false; // :(
    }

    public void deleteTask(String taskID){
        SQLiteDatabase db = this.getWritableDatabase();
        String querey = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL_6 + " = '" + taskID + "'";

        db.execSQL(querey);


    }






}
