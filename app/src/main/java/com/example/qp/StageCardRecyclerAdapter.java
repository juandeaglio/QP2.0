package com.example.qp;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class StageCardRecyclerAdapter extends RecyclerView.Adapter<StageCardRecyclerAdapter.StageCardViewHolder> implements StageTouchHelperAdapter
{
    ColorManager colorManager = MainActivity.colorManager;
    private ArrayList<Stage> stageArray;
    private final ItemTouchHelper mItemTouchHelper;
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

    public StageCardRecyclerAdapter(ArrayList<Stage> stageArray, ItemTouchHelper itemTouchHelper){
        this.stageArray = stageArray;
        mItemTouchHelper = itemTouchHelper;
    }

    @Override
    public int getItemCount(){
        return stageArray.size();
    }

    @Override
    public void onItemDismiss(int position) {
        stageArray.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(stageArray, fromPosition, toPosition);
        // TODO: 5/6/2019 ADD LOGIC TO CHANGE ORDER IN DATABASE
        //TextView stageNum = (TextView)
        for (int i = 0; i < stageArray.size(); i++){
            stageArray.get(i).setStageNum(i);

        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onBindViewHolder(final StageCardViewHolder stageCardViewHolder, int i){
        final Stage stage = stageArray.get(i);

        stageCardViewHolder.name.setText(stage.getStageName());
        stageCardViewHolder.dueDate.setText(stage.getStageDueDate());
        stageCardViewHolder.dueTime.setText(stage.getStageTimeDue());
        stageCardViewHolder.stageNumber.setText(Integer.toString(stage.getStageNum()));

        stageCardViewHolder.name.setTextColor(colorManager.getCardTextColor());
        stageCardViewHolder.dueDate.setTextColor(colorManager.getCardTextColor());
        stageCardViewHolder.dueTime.setTextColor(colorManager.getCardTextColor());
        stageCardViewHolder.stageNumber.setTextColor(colorManager.getCardTextColor());

        colorManager = MainActivity.colorManager;
        stageCardViewHolder.cardView.setBackgroundColor(colorManager.getColorPrimaryDark());
        stageCardViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mItemTouchHelper.startDrag(stageCardViewHolder);
                return false;
            }
        });
    }

    @Override
    public StageCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_card_recycler, viewGroup, false);
        return new StageCardViewHolder(itemView);
    }
}
