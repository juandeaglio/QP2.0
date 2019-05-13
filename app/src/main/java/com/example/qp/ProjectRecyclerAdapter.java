package com.example.qp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.UUID;

import maes.tech.intentanim.CustomIntent;

public class ProjectRecyclerAdapter extends RecyclerView.Adapter<ProjectRecyclerAdapter.ProjectViewHolder> {

    public static ArrayList<ProjectObj> projectArrayList;
    //private Projects projects = new Projects();
    ColorManager colorManager;
    Context context;
    public class ProjectViewHolder extends RecyclerView.ViewHolder {

        TextView projectName;
        TextView dueDate;
        TextView dueTime;
        CardView cardView;
        UUID uuid;

        //CardView projectCard;


        public ProjectViewHolder(View v) {
            super(v);
            projectName = v.findViewById(R.id.card_task_name);
            dueDate = v.findViewById(R.id.card_due_date);
            dueTime = v.findViewById(R.id.card_time);
            cardView = v.findViewById(R.id.task_card);
        }
    }

    public ProjectRecyclerAdapter(ArrayList<ProjectObj> projectArrayList, Context context) {
        this.projectArrayList = projectArrayList;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ProjectViewHolder projectViewHolder, int i) {

        final ProjectObj project = projectArrayList.get(i);
        colorManager = MainActivity.colorManager;

        //final ProjectObj project = projects.projectArrayList.get(i);
        projectViewHolder.cardView.setCardBackgroundColor(colorManager.getColorPrimaryDark());
        projectViewHolder.uuid = project.getProjectId();

        projectViewHolder.projectName.setText(project.getProjectName());
        projectViewHolder.dueDate.setText(project.getDueDate());
        projectViewHolder.dueTime.setText(project.getTimeDueDate());

        projectViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ViewTask.class);
//                intent.putExtra("taskid", task.getTaskId().toString());
//                context.startActivity(intent);
                //TODO: Add onClick event to grab all the stages associated with this project. - Ethan
                context.startActivity(new Intent(context, ActionMap.class));
                CustomIntent.customType(context, "left-to-right");


            }


        });

    }

    @Override
    public ProjectViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);
        return new ProjectViewHolder(itemView);

    }

    @Override
    public int getItemCount() {
        return projectArrayList.size();
    }
}
