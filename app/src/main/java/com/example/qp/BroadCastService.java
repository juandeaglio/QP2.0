package com.example.qp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCastService extends BroadcastReceiver {
    MainActivity mainActivity = new MainActivity();

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskName = intent.getStringExtra("Task Name");
        mainActivity.createNotification(taskName,context);
    }
}
