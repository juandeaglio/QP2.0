package com.example.qp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class TaskCardRecyclerAdapter extends RecyclerView.Adapter<TaskCardRecyclerAdapter.TaskCardViewHolder> {


    private ArrayList<Task> taskList;
    private Context context;
    private static final int RED = 1;
    private static final int ORANGE = 2;
    private static final int YELLOW = 3;
    private static final int LIGHT_YELLOW = 4;
    private static final int GREEN = 5
            ;
    public TaskCardRecyclerAdapter(ArrayList<Task> globalTaskList, MainActivity context) {
        this.taskList = globalTaskList;
        this.context = context;

    }

    public class TaskCardViewHolder extends RecyclerView.ViewHolder {
        CardView taskCard;
        TextView taskName;
        TextView priority;
        TextView dueDate;
        CheckBox checkBox;

        public TaskCardViewHolder(View v)
        {
            super(v);
            taskCard = v.findViewById(R.id.task_card);
            taskName = (TextView) v.findViewById(R.id.card_task_name);
            priority = (TextView) v.findViewById(R.id.card_priority);
            dueDate = (TextView) v.findViewById(R.id.card_due_date);
            checkBox = v.findViewById(R.id.card_check_box);
        }
    }

    public TaskCardRecyclerAdapter (ArrayList<Task> taskList, Context context)
    {
        this.taskList = taskList;

        this.context = context;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void onBindViewHolder(TaskCardViewHolder taskCardViewHolder, int i)
    {
        Task task = this.taskList.get(i);
        final String taskID = taskList.get(i).getTaskId().toString();
        taskCardViewHolder.taskName.setText(task.getTaskName());
        if (task.getPriority() == RED)
        {
            taskCardViewHolder.priority.setTextColor(Color.parseColor("#d32f2f"));
        }

        else if (task.getPriority() == ORANGE)
        {
            taskCardViewHolder.priority.setTextColor(Color.parseColor("#f57c00"));
        }

        else if (task.getPriority() == YELLOW)
        {
            taskCardViewHolder.priority.setTextColor(Color.parseColor("#fbc02d"));
        }

        else if (task.getPriority() == LIGHT_YELLOW)
        {
            taskCardViewHolder.priority.setTextColor(Color.parseColor("#fff263"));
        }

        else
        {
            taskCardViewHolder.priority.setTextColor(Color.parseColor("#388e3c"));
        }

        taskCardViewHolder.priority.setText(Integer.toString(task.getPriority()));
        taskCardViewHolder.dueDate.setText(task.getDueDate());
        taskCardViewHolder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent myIntent = new Intent(context, ViewTask.class);
                myIntent.putExtra("taskid", taskID);
                context.startActivity(myIntent);
            }
        });
    }

    @Override
    public TaskCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);


        return new TaskCardViewHolder(itemView);
    }

    public void updateData(){
        notifyDataSetChanged();
    }
}
