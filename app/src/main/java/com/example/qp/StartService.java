package com.example.qp;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;


public class StartService extends IntentService {

    private Context mContext;
    private Intent mTaskIntent;
    MainActivity mainActivity = new MainActivity();


    public StartService()
    {
        super("StartService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String taskName = (String)intent.getExtras().get("Task Name");
        mainActivity.createNotification(taskName,this);
        Log.d("Reminder", taskName);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
