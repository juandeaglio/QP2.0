package com.example.qp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ReminderRecyclerAdapter extends RecyclerView.Adapter<ReminderRecyclerAdapter.ReminderCardViewHolder> {

    private ArrayList<ReminderObject> mReminderArrayList = new ArrayList<>();
    public class ReminderCardViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        TextView interval;
        Switch activeSwitch;

        public ReminderCardViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.card_reminder_name);
            time = v.findViewById(R.id.card_reminder_time);
            interval = v.findViewById(R.id.card_reminder_interval);
            activeSwitch = v.findViewById(R.id.reminder_card_switch);
        }
    }

    public ReminderRecyclerAdapter(ArrayList<ReminderObject> reminderList){
        mReminderArrayList = reminderList;
    }

    @Override
    public int getItemCount(){
        return mReminderArrayList.size();
    }

    @Override
    public void onBindViewHolder(final ReminderCardViewHolder reminderCardViewHolder, int i){
        final ReminderObject reminder = mReminderArrayList.get(i);

        reminderCardViewHolder.name.setText(reminder.getReminderName());
        reminderCardViewHolder.time.setText(reminder.getReminderTime());

        String intervalConcat = "every " + reminder.getReminderInterval() + " " + reminder.getReminderType();
        reminderCardViewHolder.interval.setText(intervalConcat);

        reminderCardViewHolder.activeSwitch.setOnCheckedChangeListener(null);
        reminderCardViewHolder.activeSwitch.setChecked(reminder.isReminderToggle());
        reminderCardViewHolder.activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public ReminderCardViewHolder onCreateViewHolder(ViewGroup v, int i){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.reminder_card_recycler, v, false);
        return new ReminderCardViewHolder(itemView);
    }
}
