package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.qp.ProjectObj;
import com.example.qp.Projects;
import com.example.qp.Stage;

import java.util.ArrayList;
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
    public static final String COL_8 = "Task_Pending_Intent";


    public static final String REMINDERS_TABLE_NAME = "reminders_table";
    // COLS for Reminders table
    public static final String R_COL_1 = "Intent_ID";
    public static final String R_COL_2 = "Reminder_ID";
    public static final String R_COL_3 = "Reminder_Name";
    public static final String R_COL_4 = "Reminder_Date";
    public static final String R_COL_5 = "Reminder_Interval";
    public static final String R_COL_6 = "Reminder_Interval_Type";
    public static final String R_COL_7 = "Reminder_Time";
    public static final String R_COL_8 = "is_Active";


    public static final String COLORS_TABLE_NAME = "colors_table";
    // COLS for Colors table
    public static final String COLOR_COL_1 = "Color_Primary";
    public static final String COLOR_COL_2 = "Color_Primary_Dark";
    public static final String COLOR_COL_3 = "Color_Primary_Accent";
    public static final String COLOR_COL_4 = "Color_Card_Text";
    public static final String COLOR_COL_5 = "Color_Header_Text";

    public static final String PROJECT_TABLE_NAME = "project_table";
    public static final String PROJECT_COL_1 = "Project_ID"; //Primary Key
    public static final String PROJECT_COL_2 = "Project_Name";
    public static final String PROJECT_COL_3 = "Project_Due_Date";
    public static final String PROJECT_COL_4 = "Project_Time";
    public static final String PROJECT_COL_5 = "Project_Description";
    public static final String PROJECT_COL_6 = "Is_Completed";
    public static final String PROJECT_COL_7 = "Num_of_Stages";


    public static final String STAGE_TABLE_NAME = "stage_table";
    public static final String STAGE_COL_1 = "Stage_ID";
    public static final String STAGE_COL_2 = "Stage_Name";
    public static final String STAGE_COL_3 = "Stage_Due_Date";
    public static final String STAGE_COL_4 = "Stage_Description";
    public static final String STAGE_COL_5 = "Stage_Pending_Intent_ID";
    public static final String STAGE_COL_6 = "Stage_Num";
    public static final String STAGE_COL_7 = "Project_ID";


    public static final String USER_TABLE_NAME = "user_table";
    public static final String USER_COL_1 = "Username";


    public String[] allColumns = {COL_1, COL_2, COL_3, COL_4, COL_5, COL_6, COL_7};
    public String[] reminderColumns = {R_COL_1, R_COL_2, R_COL_3, R_COL_4, R_COL_5, R_COL_6, R_COL_7};

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(Task_Name varchar(255), Task_Due_Date varchar(255), Task_Priority INT, Task_Description varchar(255), Task_Completed INT, Task_ID varchar(255), Task_Time varchar(255), Task_Pending_Intent int)"); //SQL querey creating our database
        db.execSQL("create table " + REMINDERS_TABLE_NAME + "(Intent_ID int, Reminder_ID varchar(255), Reminder_Name varchar(255), Reminder_Date varchar(255), Reminder_Interval int, Reminder_Interval_Type varchar(255), Reminder_Time varchar(255), is_Active int)"); //SQL query to create the reminder table
        db.execSQL("create table " + COLORS_TABLE_NAME + "(Color_Primary int, Color_Primary_Dark int, Color_Primary_Accent int, Color_Card_Text int, Color_Header_Text int)");

        //Project/Stages tables
        db.execSQL("create table " + PROJECT_TABLE_NAME + "(Project_ID varchar(255) PRIMARY KEY, Project_name varchar(255),Project_Due_Date varchar(255), Project_Time varchar(255), Project_Description varchar(255), Is_Completed int ,Num_Of_Stages int)");
        db.execSQL("create table " + STAGE_TABLE_NAME + "(Stage_ID varchar(255) PRIMARY KEY, Stage_Name varchar(255), Stage_Due_Date varchar(255), Stage_Description varchar(255), Stage_Pending_Intent_ID int , Stage_Num int , Project_ID varchar(255), FOREIGN KEY (Project_ID) REFERENCES project_table (Project_ID))");

        //User name
        db.execSQL("create table " + USER_TABLE_NAME + "(Username varchar(255))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //If the table already exists in android studio we ignore
        db.execSQL("DROP TABLE IF EXISTS " + REMINDERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + COLORS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PROJECT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }


    public boolean insertUserData(String userName) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COL_1, userName);

        long result = db.insert(USER_TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public String getUserName() {

        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + USER_TABLE_NAME, null);

        if (result.moveToFirst()) {
            do {
                return result.getString(0);
            } while (result.moveToNext());
        }
        return "-1";
    }

    public boolean insertReminderData(int IntentID, String ReminderID, String reminderName, String reminderDate, String reminderInterval, String reminderIntervalType, String reminderTime, int isActive) {
        SQLiteDatabase reminderDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(R_COL_1, IntentID);
        contentValues.put(R_COL_2, ReminderID);
        contentValues.put(R_COL_3, reminderName);
        contentValues.put(R_COL_4, reminderDate);
        contentValues.put(R_COL_5, reminderInterval);
        contentValues.put(R_COL_6, reminderIntervalType);
        contentValues.put(R_COL_7, reminderTime);
        contentValues.put(R_COL_8, isActive);
        long result = reminderDB.insert(REMINDERS_TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean turnOffReminder(String reminderID) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(R_COL_8, 0);

        long result = db.update(REMINDERS_TABLE_NAME, contentValues, "Reminder_ID = '" + reminderID + "'", null);

        if (result == -1) {
            return false;

        } else {
            return true;
        }
    }

    public boolean turnOnReminder(String reminderID) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(R_COL_8, 1);

        long result = db.update(REMINDERS_TABLE_NAME, contentValues, "Reminder_ID = '" + reminderID + "'", null);

        if (result == -1) {
            return false;

        } else {
            return true;
        }
    }

    public boolean insertStageData(String stage_ID, String stage_Name, String stage_Due_Date, String stage_Description, String stage_Pending_Intent_ID,  int stageNum,String project_ID) {
        SQLiteDatabase stageDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(STAGE_COL_1, stage_ID);
        contentValues.put(STAGE_COL_2, stage_Name);
        contentValues.put(STAGE_COL_3, stage_Due_Date);
        contentValues.put(STAGE_COL_4, stage_Description);
        contentValues.put(STAGE_COL_5, stage_Pending_Intent_ID);
        contentValues.put(STAGE_COL_6, stageNum);
        contentValues.put(STAGE_COL_7, project_ID);

        long result = stageDB.insert(STAGE_TABLE_NAME, null, contentValues);


        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean insertProjectData(String project_ID, String projectName, String projectDueDate, String projectTime, String projectDesc, int completed, int numOfStages) {
        SQLiteDatabase projectDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PROJECT_COL_1, project_ID);
        contentValues.put(PROJECT_COL_2, projectName);
        contentValues.put(PROJECT_COL_3, projectDueDate);
        contentValues.put(PROJECT_COL_4, projectTime);
        contentValues.put(PROJECT_COL_5, projectDesc);
        contentValues.put(PROJECT_COL_6, 0);
        contentValues.put(PROJECT_COL_7, numOfStages);


        long result = projectDB.insert(PROJECT_TABLE_NAME, null, contentValues);


        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

//
//    public Cursor getAllStagesForProject() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor result = db.rawQuery("select Stage_ID, Stage_Name, Stage_Due_Date, Stage_Description, Stage_Num, Stage_Pending_Intent_ID, s.Project_ID from " + STAGE_TABLE_NAME + " s" + " join project_table p on s.Project_ID = p.Project_ID", null);
//
//        return result;
//    }


    public ArrayList<Stage> getStagesForProject(String projectID){
        ArrayList<Stage> resultStageList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + STAGE_TABLE_NAME + " where Project_ID = '" + projectID + "' ", null);
        if(result.moveToFirst()){
            do {
            Stage newStage = new Stage(result.getString(1),result.getString(2),result.getString(3),0, result.getString(4),result.getInt(5), result.getString(6));
            resultStageList.add(newStage);
            }while (result.moveToNext());
        }
        return resultStageList;
    }

    public Cursor getAllProjects() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + PROJECT_TABLE_NAME + " order by " + PROJECT_COL_6 + " desc ", null);
        return result;
    }

    public ProjectObj getProject(String projectID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + PROJECT_TABLE_NAME + " where Project_ID = '" + projectID + "'", null);
        ProjectObj resultProj = new ProjectObj();

        if (result.moveToFirst()) {
            do {
            resultProj.setProjectId(UUID.fromString(result.getString(0)));
            resultProj.setProjectName(result.getString(1));
            resultProj.setDueDate(result.getString(2));
            resultProj.setTimeDueDate(result.getString(3));
            resultProj.setDescription(result.getString(4));
            resultProj.setCompleted(result.getShort(5));
            resultProj.setNumOfStages(result.getInt(6));

            } while (result.moveToNext());

        }
        return resultProj;
    }


    public boolean checkIfColorExists() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + COLORS_TABLE_NAME, null);

        if (result.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }

    public Cursor getColorValues() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + COLORS_TABLE_NAME, null);

        return result;
    }

    public boolean insertColorData(int color_Primary, int color_Primary_Dark, int color_Primary_Accent, int color_Card_Text, int color_Header_Text) {
        SQLiteDatabase colorDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOR_COL_1, color_Primary);
        contentValues.put(COLOR_COL_2, color_Primary_Dark);
        contentValues.put(COLOR_COL_3, color_Primary_Accent);
        contentValues.put(COLOR_COL_4, color_Card_Text);
        contentValues.put(COLOR_COL_5, color_Header_Text);
        long result = colorDB.insert(COLORS_TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public boolean updateColordata(int color_Primary, int color_Primary_Dark, int color_Primary_Accent, int color_Card_Text, int color_Header_Text) {
        SQLiteDatabase colorDB = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLOR_COL_1, color_Primary);
        contentValues.put(COLOR_COL_2, color_Primary_Dark);
        contentValues.put(COLOR_COL_3, color_Primary_Accent);
        contentValues.put(COLOR_COL_4, color_Card_Text);
        contentValues.put(COLOR_COL_5, color_Header_Text);

        long result = colorDB.update(COLORS_TABLE_NAME, contentValues, COLOR_COL_1 + " is not NULL", null);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public int getIntentID(String reminderID) {
        SQLiteDatabase reminderDB = getWritableDatabase();

        Cursor result = reminderDB.rawQuery("select Intent_ID from " + REMINDERS_TABLE_NAME + " where " + R_COL_2 + " = '" + reminderID + "'", null);
        if (result.moveToFirst()) {
            return result.getInt(0);
        }
        return -1; //Error
    }

    public boolean insertData(String taskName, int priority, String dueDate, String description, int isCompleted, UUID taskID, String taskTime, int pendingIntentID) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskName);
        contentValues.put(COL_2, dueDate);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, isCompleted);
        contentValues.put(COL_6, taskID.toString());
        contentValues.put(COL_7, taskTime);
        contentValues.put(COL_8, pendingIntentID);


        long result = db.insert(TABLE_NAME, null, contentValues); //Will return -1 if not inserted properly

        if (result == -1) {
            return false;

        } else {
            return true;
        }
    }


    public int getTaskPendingIntent(String taskID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_6 + " = '" + taskID + "'", null);
        while (result.moveToNext()) {
            if (result.getString(5).equals(taskID)) {
                return result.getInt(7);
            }
        }

        return -1; //Error
    }

    public int getToggleValue(String reminderID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + REMINDERS_TABLE_NAME + " where " + R_COL_2 + " = '" + reminderID + "'", null);

        if ((result.moveToFirst())) {
            do {
                if (result.getString(1).equals(reminderID)) {
                    return result.getInt(7);
                }
            } while (result.moveToNext());
        }

        return -1;

    }

    public int getReminderPendingIntent(String taskID) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + REMINDERS_TABLE_NAME + " where " + R_COL_2 + " = '" + taskID + "'", null);
        while (result.moveToNext()) {
            if (result.getString(1).equals(taskID)) {
                return result.getInt(0);
            }
        }

        return -1; //Error
    }

    public Cursor getAllUnCompletedTasksFromTable() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME + " where " + COL_5 + " != 0", null);

        return result;
    }

    public Cursor getAllTasksFromTable() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + TABLE_NAME, null);

        return result;
    }

    public Cursor getAllRemindersFromTable() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor result = db.query(REMINDERS_TABLE_NAME, this.reminderColumns, null, null, null, null, R_COL_3 + " " + "asc", null);
        return result;
    }

    public boolean updateTable(String taskName, int priority, String dueDate, String description, int isCompleted, UUID taskID, String taskTime) {
        SQLiteDatabase tempDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, taskName);
        contentValues.put(COL_2, dueDate);
        contentValues.put(COL_3, priority);
        contentValues.put(COL_4, description);
        contentValues.put(COL_5, isCompleted);
        contentValues.put(COL_6, taskID.toString());
        contentValues.put(COL_7, taskTime);

        tempDb.update(TABLE_NAME, contentValues, "Task_ID = ?", new String[]{taskID.toString()});
        return true;
    }


    public String getTaskDueDate(String taskID) {
        Cursor data = getAllTasksFromTable();

        if ((data.moveToFirst())) {
            do {
                if (data.getString(5).equals(taskID)) {
                    return data.getString(1);
                }
            } while (data.moveToNext());
        }

        return ""; //Didn't find it
    }

    //Marks the task with the associated TaskID and changes it's completed field to 1
    //On the home page have a function that if this returns tru then you move the task to the global completed list
    public boolean markTaskCompleted(String taskID) {
        Cursor data = getAllTasksFromTable();
        SQLiteDatabase tempDB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_5, 1);
        if (data.moveToFirst()) {
            do {
                if (data.getString(5).equals(taskID)) {
                    tempDB.update(TABLE_NAME, contentValues, "Task_ID = ?", new String[]{taskID});
                    return true; //Successful update
                }
            } while (data.moveToNext());
        }

        return false; // :(
    }


    public Cursor sortCompletedTasks(String sortSelector) {
        Cursor sortedTable = this.getWritableDatabase().query(TABLE_NAME + " Where " + COL_5 + " != 0", this.allColumns, null, null, null, null, sortSelector + " " + "asc"); //ex: Task_Priority(Column) + order("asc" or "desc")
        return sortedTable;
    }

    public Cursor sortUnCompletedTasks(String sortSelector) {
        Cursor sortedTable = this.getWritableDatabase().query(TABLE_NAME + " Where " + COL_5 + " != 1", this.allColumns, null, null, null, null, sortSelector + " " + "asc"); //ex: Task_Priority(Column) + order("asc" or "desc")
        return sortedTable;
    }

    public boolean unCheckCompletedTask(String taskID) {
        Cursor data = getAllTasksFromTable();
        SQLiteDatabase tempDB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_5, "0");
        if (data.moveToFirst()) {
            do {
                if (data.getString(5).equals(taskID)) {
                    tempDB.update(TABLE_NAME, contentValues, "Task_ID = ?", new String[]{taskID});
                    return true; //Successful update
                }
            } while (data.moveToNext());
        }

        return false;
    }

    public void deleteTask(String taskID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String querey = "DELETE FROM " + TABLE_NAME + " WHERE " +
                COL_6 + " = '" + taskID + "'";

        db.execSQL(querey);
    }

    public void deleteReminder(String taskID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String querey = "DELETE FROM " + REMINDERS_TABLE_NAME + " WHERE " +
                R_COL_2 + " = '" + taskID + "'";

        db.execSQL(querey);
    }


    public void deleteAllCompletedTasks() {
        SQLiteDatabase db = this.getWritableDatabase();
        String querey = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_5 + " = 1";
        db.execSQL(querey);
    }


}
