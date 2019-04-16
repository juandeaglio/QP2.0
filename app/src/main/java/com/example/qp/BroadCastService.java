package com.example.qp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCastService extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceNotification = new Intent(context,StartService.class);
        String taskName = intent.getStringExtra("Task Name");
        serviceNotification.putExtra("Task Name", taskName);
        context.startService(serviceNotification);


    }
}
