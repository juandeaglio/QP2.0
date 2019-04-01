package com.example.qp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class TaskCardRecyclerAdapter extends RecyclerView.Adapter<TaskCardRecyclerAdapter.TaskCardViewHolder> {

    private List<Task> taskList;

    public class TaskCardViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView priority;
        TextView dueDate;
        CheckBox checkBox;

        public TaskCardViewHolder(View v)
        {
            super(v);
            taskName = (TextView) v.findViewById(R.id.card_task_name);
            priority = (TextView) v.findViewById(R.id.card_priority);
            dueDate = (TextView) v.findViewById(R.id.card_due_date);
            checkBox = (CheckBox) v.findViewById(R.id.card_check_box);
        }
    }

    public TaskCardRecyclerAdapter (List<Task> taskList)
    {
        this.taskList = taskList;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    @Override
    public void onBindViewHolder(TaskCardViewHolder taskCardViewHolder, int i)
    {
        Task task = taskList.get(i);
        taskCardViewHolder.taskName.setText(task.getTaskName());
        taskCardViewHolder.priority.setText(task.getPriority());
        taskCardViewHolder.dueDate.setText(task.getDueDate());
    }

    @Override
    public TaskCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);

        return new TaskCardViewHolder(itemView);
    }
}
