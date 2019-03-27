package com.example.qp;

import java.sql.Time;
import java.util.Date;

public class Task {

    private String taskName;
    //private Date dueDate;
    private String dueDate;
    private Time timeDueDate;
    private int priority;
    //private String priority;
    private String description;
    private int completed; //1 for yes, 0 for no

    //Default constuctor
    public Task(String taskName, String  dueDate, Time timeDueDate, int priority, String description, int completed) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.timeDueDate = timeDueDate;
        this.priority = priority;
        this.description = description;
        this.completed = 0;
    }

    //For prototype
    public Task() {
        this.taskName = "";
        this.dueDate = "";
        this.timeDueDate = null;
        this.priority = 0;
        this.description = "";
        this.completed = 0;
    }
    /*
    //Overloaded constuctor
    public Task(String taskName, Date dueDate, Time timeDueDate, int priority, String description) {
        this.taskName = taskName;
        this.dueDate = dueDate;
        this.timeDueDate = timeDueDate;
        this.priority = priority;
        this.description = description;
        this.completed = false;
    }
    */
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    //temporary: public Date getDueDate() {
    public String getDueDate() { return dueDate; }


    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public Time getTimeDueDate() {
        return timeDueDate;
    }

    public void setTimeDueDate(Time timeDueDate) {
        this.timeDueDate = timeDueDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) { this.priority = priority; }

    public String getDescription() {
        return description;
    }

    public int getCompleted(){
        return this.completed;
    }

    public void setCompleted(int completed) { this.completed = completed; }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", dueDate=" + dueDate +
                ", timeDueDate=" + timeDueDate +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", completed='" + completed + '\'' +
                '}';
    }
}
