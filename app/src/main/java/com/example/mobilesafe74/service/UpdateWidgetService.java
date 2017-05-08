package com.example.mobilesafe74.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.engine.ProcessInfoProvider;
import com.example.mobilesafe74.receiver.MyAppWidgetProvider;

import java.util.Timer;
import java.util.TimerTask;

public class UpdateWidgetService extends Service {

    private innerService mInnerService;
    private Timer mTimer;
    private String Tag="UpdateWidgetService";

    @Override
    public void onCreate() {
        //管理进程总数和可用内存数更新(定时器)
        startTimer();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);

        mInnerService = new innerService();
        registerReceiver(mInnerService,intentFilter);

        super.onCreate();
    }

    private class innerService extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF)){
               //关闭定时更新任务
                cancelTimerTask();
            }else if (action.equals(Intent.ACTION_SCREEN_ON)){
                startTimer();

            }
        }
    }

    private void cancelTimerTask() {

        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //ui定时刷新
                updateAppWidget();
                Log.i(Tag,"五秒更新一次");
            }
        }, 0, 500);
    }

    private void updateAppWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.process_widget);
        //3.给窗体小部件布view对象,内部控件赋值
        remoteViews.setTextViewText(R.id.tv_process_count, "进程总数:"+ ProcessInfoProvider.getProcessCount(this));
        //4.显示可用内存大小
        String strAvailSpace = Formatter.formatFileSize(this, ProcessInfoProvider.getAvailSpace(this));
        remoteViews.setTextViewText(R.id.tv_process_memory, "可用内存:"+strAvailSpace);

        //点击窗体小部件,进入应用
        //1:在那个控件上响应点击事件2:延期的意图
        Intent intent = new Intent("android.intent.action.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.ll_root, pendingIntent);

        //通过延期意图发送广播,在广播接受者中杀死进程,匹配规则看action
        Intent broadCastintent = new Intent("android.intent.action.KILL_BACKGROUND_PROCESS");
        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, broadCastintent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_clear,broadcast);

        ComponentName componentName = new ComponentName(this, MyAppWidgetProvider.class);
        appWidgetManager.updateAppWidget(componentName,remoteViews);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        if (mInnerService!=null){
            unregisterReceiver(mInnerService);
        }
        cancelTimerTask();
        super.onDestroy();
    }


}
