package com.example.qp;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;



public class StartService extends IntentService {

    private Context mContext;
    private Intent mTaskIntent;
    MainActivity mainActivity = new MainActivity();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.mTaskIntent = intent;
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        String taskName = (String)mTaskIntent.getExtras().get("Task Name");
        mainActivity.createNotification(taskName,this);
        stopService(mTaskIntent);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
