package com.example.mobilesafe74.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.service.LockScreenService;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.ServiceUtil;
import com.example.mobilesafe74.utils.SpUtils;


public class ProcessSettingActivity extends AppCompatActivity {

    private CheckBox cb_show_system,cb_lock_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_setting);

        initSystemShow();
        initLockScreenClear();
    }

    /**
     * 锁屏清理
     */
    private void initLockScreenClear() {
        cb_lock_clear = (CheckBox) findViewById(R.id.cb_lock_clear);
        //根据锁屏清理服务是否开启去,决定是否单选框选中

        boolean isRunning = ServiceUtil.isRunning(this, "com.example.mobilesafe74.service.LockScreenService");
        if(isRunning){
            cb_lock_clear.setText("锁屏清理已开启");
        }else{
            cb_lock_clear.setText("锁屏清理已关闭");
        }
        //cb_lock_clear选中状态维护
        cb_lock_clear.setChecked(isRunning);

        //对选中状态进行监听
        cb_lock_clear.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked就作为是否选中的状态
                if(isChecked){
                    cb_lock_clear.setText("锁屏清理已开启");
                    //开启服务
                    startService(new Intent(getApplicationContext(), LockScreenService.class));
                }else{
                    cb_lock_clear.setText("锁屏清理已关闭");
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), LockScreenService.class));
                }
            }
        });

    }

    private void initSystemShow() {
        cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
        //对之前存储过的状态进行回显
        boolean showSystem = SpUtils.getBoolean(this, ConstantValue.SHOW_SYSTEM, false);
        //单选框的显示状态
        cb_show_system.setChecked(showSystem);

        if(showSystem){
            cb_show_system.setText("显示系统进程");
        }else{
            cb_show_system.setText("隐藏系统进程");
        }


        //对选中状态进行监听
        cb_show_system.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //isChecked就作为是否选中的状态
                if(isChecked){
                    cb_show_system.setText("显示系统进程");
                }else{
                    cb_show_system.setText("隐藏系统进程");
                }
                SpUtils.putBoolean(ProcessSettingActivity.this,ConstantValue.SHOW_SYSTEM, isChecked);
            }
        });
    }
}
