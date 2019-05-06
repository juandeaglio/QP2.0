package com.example.qp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class StageCardRecyclerAdapter extends RecyclerView.Adapter<StageCardRecyclerAdapter.StageCardViewHolder> {

    private ArrayList<Stage> stageArray;
    public class StageCardViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView name;
        TextView dueDate;
        TextView dueTime;
        TextView stageNumber;

        public StageCardViewHolder(View v){
            super(v);
            cardView = v.findViewById(R.id.task_card);
            name = (TextView) v.findViewById(R.id.card_task_name);
            stageNumber = (TextView) v.findViewById(R.id.card_priority);
            dueDate = (TextView) v.findViewById(R.id.card_due_date);
            dueTime = (TextView) v.findViewById(R.id.card_time);
        }
    }

    public StageCardRecyclerAdapter(ArrayList<Stage> stageArray){
        this.stageArray = stageArray;
    }

    @Override
    public int getItemCount(){
        return stageArray.size();
    }

    @Override
    public void onBindViewHolder(StageCardViewHolder stageCardViewHolder, int i){
        final Stage stage = stageArray.get(i);
        stageCardViewHolder.name.setText(stage.getStageName());
        stageCardViewHolder.dueDate.setText(stage.getStageDueDate());
        stageCardViewHolder.dueTime.setText(stage.getStageTimeDue());


    }

    @Override
    public StageCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);
        return new StageCardViewHolder(itemView);
    }
}
