//changed datatype of dueDate, removed timeDueDate since Date() includes it
//added new variable, dateAssigned, just in case we need it

package com.example.qp;

import java.sql.Time;
import java.util.Date;

public class Task {

    private String taskName;
    private Date dueDate;
    //private Time timeDueDate;
    private Date dateAssigned = new Date();
    private int priority;
    private String description;
    private boolean completed;

    //Default constuctor
    public Task(String taskName, Date  dueDate, int priority, String description, boolean completed) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.priority = priority;
        this.description = description;
        this.completed = completed;
    }

    //For prototype
    public Task() {
        this.taskName = "";
        this.dueDate = new Date();
        //this.timeDueDate = null;
        this.priority = 0;
        this.description = "";
        this.completed = false;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //temporary: public Date getDueDate() {
    public Date getDueDate() { return this.dueDate; }

    public void setDueDate(Date dueDate) {
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

    public boolean isCompleted() { return this.completed; }

    public void setCompleted(boolean completed) { this.completed = completed; }

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
}
