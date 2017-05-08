package com.example.mobilesafe74.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.example.mobilesafe74.engine.ProcessInfoProvider;

public class LockScreenService extends Service {


    private InnerReceiver mInnerReceiver;

    @Override
    public void onCreate() {

        //锁屏action
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mInnerReceiver = new InnerReceiver();
        registerReceiver(mInnerReceiver, intentFilter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mInnerReceiver!=null){
            unregisterReceiver(mInnerReceiver);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    private class InnerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ProcessInfoProvider.kiiAll(context);

        }
    }
}
