package com.example.qp;

import java.util.UUID;

public class Stage {

    private UUID stageID;
    private String stageName;
    private int stageNum;
    private String stageDueDate;
    private String stageTimeDue;
    private String stageDescription;
    private int isCompleted; //1 for yes, 0 for no
    private String pendingIntentID;
    private String projectID; //link back to the project


    //Default constructor
    public Stage(String stageName, String stageDueDate,String stageDescription, int isCompleted, String pendingIntentID, int stageNum, String projectID)
    {
        this.stageID = UUID.randomUUID();
        this.stageName = stageName;
        this.stageDueDate = stageDueDate;
        this.stageDescription = stageDescription;
        this.isCompleted = isCompleted;
        this.pendingIntentID = pendingIntentID;
        this.projectID = projectID;
        this.stageNum = stageNum;
    }



    public UUID getStageID() {
        return stageID;
    }

    public void setStageID(UUID stageID) {
        this.stageID = stageID;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageDueDate() {
        return stageDueDate;
    }

    public void setStageDueDate(String stageDueDate) {
        this.stageDueDate = stageDueDate;
    }

    public String getStageTimeDue() {
        return stageTimeDue;
    }

    public void setStageTimeDue(String stageTimeDue) {
        this.stageTimeDue = stageTimeDue;
    }

    public String getStageDescription() {
        return stageDescription;
    }

    public void setStageDescription(String stageDescription) {
        this.stageDescription = stageDescription;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    public String getPendingIntentID() {
        return pendingIntentID;
    }

    public void setPendingIntentID(String pendingIntentID) {
        this.pendingIntentID = pendingIntentID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public int getStageNum() {
        return stageNum;
    }

    public void setStageNum(int stageNum) {
        this.stageNum = stageNum;
    }
}
