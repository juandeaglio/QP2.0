package com.example.qp;

public class ReminderObject {

    private String reminderName;
    private int reminderInterval;
    private String reminderType;
    private boolean reminderToggle;

    public ReminderObject(String mReminderName, int mReminderInterval, String mReminderType, boolean mReminderToggle)
    {
        this.reminderName = mReminderName;
        this.reminderInterval = mReminderInterval;
        this.reminderType = mReminderType;
        this.reminderToggle = mReminderToggle;
    }
    public String getReminderName() {
        return reminderName;
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
