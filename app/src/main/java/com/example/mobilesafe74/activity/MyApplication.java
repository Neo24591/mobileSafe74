package com.example.mobilesafe74.activity;

import android.app.Application;
import android.content.Context;

/**
 * Created by yueyue on 2017/1/11.
 */

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }


}

