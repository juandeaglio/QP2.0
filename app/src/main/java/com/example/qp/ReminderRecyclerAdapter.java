package com.example.qp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.DatabaseHelper;

import java.util.ArrayList;

public class ReminderRecyclerAdapter extends RecyclerView.Adapter<ReminderRecyclerAdapter.ReminderCardViewHolder> {

    private ArrayList<ReminderObject> mReminderArrayList = new ArrayList<>();
    ColorManager colorManager;
    private Context context;
    private DatabaseHelper db;
    private AlarmManager am;
    public class ReminderCardViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView time;
        TextView interval;
        Switch activeSwitch;
        CardView cardView;
        String reminderID;


        public ReminderCardViewHolder(View v){
            super(v);
            name = v.findViewById(R.id.card_reminder_name);
            time = v.findViewById(R.id.card_reminder_time);
            interval = v.findViewById(R.id.card_reminder_interval);
            activeSwitch = v.findViewById(R.id.reminder_card_switch);
            cardView = v.findViewById(R.id.reminder_card);

        }
    }

    public ReminderRecyclerAdapter(ArrayList<ReminderObject> reminderList, Context context, AlarmManager alarm){
        mReminderArrayList = reminderList;
        this.context = context;
        this.am = alarm;
    }

    @Override
    public int getItemCount(){
        return mReminderArrayList.size();
    }

    public void updateData()
    {
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(final ReminderCardViewHolder reminderCardViewHolder, int i){
        colorManager = MainActivity.colorManager;
        final ReminderObject reminder = mReminderArrayList.get(i);
        reminderCardViewHolder.name.setText(reminder.getReminderName());
        reminderCardViewHolder.time.setText(reminder.getReminderTime());
        reminderCardViewHolder.reminderID = reminder.getReminderUUID();
        String intervalConcat = "every " + reminder.getReminderInterval() + " " + reminder.getReminderType();
        reminderCardViewHolder.interval.setText(intervalConcat);

        reminderCardViewHolder.activeSwitch.setOnCheckedChangeListener(null);
        reminderCardViewHolder.activeSwitch.setChecked(reminder.isReminderToggle());

        reminderCardViewHolder.interval.setTextColor(colorManager.getColorText());
        reminderCardViewHolder.name.setTextColor(colorManager.getColorText());
        reminderCardViewHolder.time.setTextColor(colorManager.getColorText());

        reminderCardViewHolder.activeSwitch.setThumbTintList(ColorStateList.valueOf(colorManager.getColorAccent()));
        reminderCardViewHolder.activeSwitch.setTrackTintList(ColorStateList.valueOf(colorManager.getColorAccent()));

        reminderCardViewHolder.activeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                db = new DatabaseHelper(context);
                if (isChecked) // set alarm
                {
                    Toast toast = Toast.makeText(context, "Repeating alarm set. ", Toast.LENGTH_LONG);
                    toast.show();
                    int reminderPendingID = db.getReminderPendingIntent(reminderCardViewHolder.reminderID);
                    Intent intent1 = new Intent(context, StartService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(context, reminderPendingID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.setRepeating(AlarmManager.RTC_WAKEUP,reminder.getDueDate(),reminder.getFrequencyOfAlarm(),pendingIntent);
                                    }
                else
                {
                    Toast toast = Toast.makeText(context, "Repeating alarm disabled. ", Toast.LENGTH_LONG);
                    toast.show();
                    int reminderPendingID = db.getReminderPendingIntent(reminderCardViewHolder.reminderID);
                    Intent intent1 = new Intent(context, StartService.class);
                    PendingIntent pendingIntent = PendingIntent.getService(context, reminderPendingID, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.cancel(pendingIntent);
                }



            }
        });

        reminderCardViewHolder.cardView.setBackgroundColor(colorManager.getColorPrimaryDark());
    }

    @Override
    public ReminderCardViewHolder onCreateViewHolder(ViewGroup v, int i){
        View itemView = LayoutInflater.from(v.getContext()).inflate(R.layout.reminder_card_recycler, v, false);
        return new ReminderCardViewHolder(itemView);
    }
}
