package com.example.qp;

import java.util.Calendar;
import java.util.UUID;

public class ReminderObject {

    private String reminderName;
    private int reminderInterval;
    private String reminderType;
    private boolean reminderToggle;
    private String reminderTime;
    private String reminderUUID;
    private Calendar dueDate;
    private long frequencyOfAlarm;

    public long getFrequencyOfAlarm() {
        return frequencyOfAlarm;
    }

    public void setFrequencyOfAlarm(long frequencyOfAlarm) {
        this.frequencyOfAlarm = frequencyOfAlarm;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(Calendar dueDate) {
        this.dueDate = dueDate;
    }


    public String getReminderUUID() {
        return reminderUUID;
    }

    public void setReminderUUID(String reminderUUID) {
        this.reminderUUID = reminderUUID;
    }

    public ReminderObject(String mReminderName, int mReminderInterval, String mReminderType, boolean mReminderToggle, String reminderID, Calendar dueDate)
    {
        this.reminderName = mReminderName;
        this.reminderInterval = mReminderInterval;
        this.reminderType = mReminderType;
        this.reminderToggle = mReminderToggle;
        this.reminderUUID = reminderID;
        this.dueDate = dueDate;

    }
    public String getReminderName() {
        return reminderName;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(String reminderTime) {
        this.reminderTime = reminderTime;
    }

    public void setReminderName(String reminderName) {
        this.reminderName = reminderName;
    }

    public int getReminderInterval() {
        return reminderInterval;
    }

    public void setReminderInterval(int reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public String getReminderType() {
        return reminderType;
    }

    public void setReminderType(String reminderType) {
        this.reminderType = reminderType;
    }

    public boolean isReminderToggle() {
        return reminderToggle;
    }

    public void setReminderToggle(boolean reminderToggle) {
        this.reminderToggle = reminderToggle;
    }
}
