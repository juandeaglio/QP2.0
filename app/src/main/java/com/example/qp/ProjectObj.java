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
    private int numOfStages;

    public int getNumOfStages() {
        return numOfStages;
    }

    public void setNumOfStages(int numOfStages) {
        this.numOfStages = numOfStages;
    }

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
//    public ProjectObj(String projectID,String projectName, String  dueDate, String description, int completed, String dueTime, int numOfStages)
//    {
//        this.projectId =  UUID.fromString(projectID);
//        this.projectName = projectName;
//        this.dueDate = dueDate;
//        this.timeDueDate = dueTime;
//        this.description = description;
//        this.completed = 0;
//        this.numOfStages = numOfStages;
//    }

    public ProjectObj(){
        this.projectId = UUID.randomUUID();
        this.projectName = "";
        this.dueDate = "";
        this.timeDueDate = "";
        this. description = "";
        this.completed = 0;
        this.numOfStages = 0;
        this.stageList = new ArrayList<>();
    }




}
