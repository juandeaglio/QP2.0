//changed datatype of dueDate, removed timeDueDate since Date() includes it
//added new variable, dateAssigned, just in case we need it

package com.example.qp;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Task {

    private UUID taskId;
    private String taskName;
    private String dueDate;
    //private Time timeDueDate;
    private Date dateAssigned = new Date();
    private int priority;
    private String description;
    private short completed; //1 for yes, 0 for no

    //Default constructor
    public Task(String taskName, String  dueDate, int priority, String description, int completed) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.priority = priority;
        this.description = description;
        this.completed = 0;
    }

    //For prototype
    public Task() {
        this.taskName = "";
        //this.dueDate = new Date();
        this.dueDate = "";
        //this.timeDueDate = null;
        this.priority = 0;
        this.description = "";
        this.completed = 0;
    }

    public UUID getTaskId() {
        return taskId;
    }

    public void setTaskId(UUID newTaskID) { //Generates a random uuid no need for a parameter when setting
        this.taskId = newTaskID;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //temporary: public Date getDueDate() {
    public String getDueDate() { return this.dueDate; }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Date getDateAssigned() { return this.dateAssigned; }

    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) { this.priority = priority; }

    public String getDescription() {
        return this.description;
    }

    public int getCompleted() { return this.completed; }

    public void setCompleted(short completed) { this.completed = completed; }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", dueDate=" + dueDate.toString() + '\'' +
                ", priority=" + priority + '\'' +
                ", description='" + description + '\'' +
                ", completed='" + completed + '\'' +
                '}';
    }

    public void getDescription(String string) {
        this.description = string;
    }


}
