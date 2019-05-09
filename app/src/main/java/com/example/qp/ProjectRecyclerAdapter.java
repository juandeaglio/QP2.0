package com.example.qp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.UUID;

public class ProjectRecyclerAdapter extends RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder>{

    private ArrayList<ProjectObj> projectArrayList;

    public class ProjectViewHolder extends RecyclerView.ViewHolder{

        TextView projectName;
        TextView dueDate;
        TextView dueTime;
        CardView cardView;
        UUID uuid;

        public ProjectViewHolder(View v){
            super(v);
            projectName = v.findViewById(R.id.card_task_name);
            dueDate = v.findViewById(R.id.card_due_date);
            dueTime = v.findViewById(R.id.card_time);
            cardView = v.findViewById(R.id.task_card);
        }
    }

    public ProjectRecyclerAdapter(ArrayList<ProjectObj>projectArrayList){
        this.projectArrayList = projectArrayList;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder projectViewHolder, int i){
        final ProjectObj project = projectArrayList.get(i);
        projectViewHolder.uuid = project.getProjectId();

        projectViewHolder.projectName.setText(project.getProjectName());
        projectViewHolder.dueDate.setText(project.getDueDate());
        projectViewHolder.dueTime.setText(project.getTimeDueDate());

    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);
        return new ProjectViewHolder(itemView);

    }

    @Override
    public int getItemCount(){
        return projectArrayList.size();
    }
}
