package com.example.qp;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class ProjectObj {
    private UUID projectId;
    private String projectName;
    private String dueDate;
    private String timeDueDate;
    private Date dateAssigned = new Date();
    private String description;
    private short completed; //1 for yes, 0 for no
    private ArrayList<Stage> stageList;

    public UUID getProjectId() {
        return projectId;
    }

    public void setProjectId(UUID projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getTimeDueDate() {
        return timeDueDate;
    }

    public void setTimeDueDate(String timeDueDate) {
        this.timeDueDate = timeDueDate;
    }

    public Date getDateAssigned() {
        return dateAssigned;
    }

    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public short getCompleted() {
        return completed;
    }

    public void setCompleted(short completed) {
        this.completed = completed;
    }

    public ArrayList<Stage> getStageList() {
        return stageList;
    }

    public void setStageList(ArrayList<Stage> stageList) {
        this.stageList = stageList;
    }

    //Default constructor
    public ProjectObj(String projectName, String  dueDate, String description, int completed, String dueTime)
    {
        this.projectId = UUID.randomUUID();
        this.projectName = projectName;
        this.dueDate = dueDate;
        this.timeDueDate = dueTime;
        this.description = description;
        this.completed = 0;
    }

    //For prototype
    public ProjectObj() {
        this.projectId = UUID.randomUUID();
        this.projectName = "TEST 1";
        this.dueDate = "09/04/2019";
        this.description = "";
        this.completed = 0;
        this.timeDueDate = "11:50 AM";
        this.stageList = new ArrayList<>(); //initialization of the stage array
    }




}
