package com.example.qp;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Project{
    public UUID projectId;
    public String projectName;
    public String dueDate;
    public String timeDueDate;
    public Date dateAssigned = new Date();
    public String description;
    public short completed; //1 for yes, 0 for no
    public ArrayList<Stage> stageList;

    //Default constructor
    public Project(String projectName, String  dueDate, String description, int completed, String dueTime)
    {
        this.projectId = UUID.randomUUID();
        this.projectName = projectName;
        this.dueDate = dueDate;
        this.timeDueDate = dueTime;
        this.description = description;
        this.completed = 0;
    }

    //For prototype
    public Project() {
        this.projectId = UUID.randomUUID();
        this.projectName = "";
        this.dueDate = "";
        this.description = "";
        this.completed = 0;
        this.timeDueDate = "";
        this.stageList = new ArrayList<>(); //initialization of the stage array
    }




}
