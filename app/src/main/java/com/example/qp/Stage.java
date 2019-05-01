package com.example.qp;

import java.util.Date;
import java.util.UUID;

public class Stage {

    private UUID projectId;
    private String projectName;
    private String dueDate;
    private String timeDueDate;
    private Date dateAssigned = new Date();
    private String description;
    private short completed; //1 for yes, 0 for no

    //Default constructor
    public Stage(String projectName, String  dueDate, String description, int completed, String dueTime)
    {
        this.projectId = UUID.randomUUID();
        this.projectName = projectName;
        this.dueDate = dueDate;
        this.timeDueDate = dueTime;
        this.description = description;
        this.completed = 0;
    }
}
