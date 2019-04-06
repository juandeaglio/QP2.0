package com.example.qp;

import android.content.Context;
import android.content.Intent;
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
        final Task task = taskList.get(i);
        taskCardViewHolder.taskName.setText(task.getTaskName());
        taskCardViewHolder.priority.setText(Integer.toString(task.getPriority()));
        taskCardViewHolder.dueDate.setText(dateCorrection(task.getDueDate()));
        taskCardViewHolder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewTask.class);
                intent.putExtra("taskid", task.getTaskId().toString());
                context.startActivity(intent);
            }
        });
    }

    private String dateCorrection(String date)
    {
        String dateArr [];
        dateArr = date.split("/");
        int month = Integer.parseInt(dateArr[0]);
        month++;
        date = month + "/" + dateArr[1] + "/" + dateArr[2];
        return date;
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
