package com.example.mobilesafe74.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.service.AddressService;
import com.example.mobilesafe74.service.BlacknumberService;
import com.example.mobilesafe74.service.WatchDogService;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.ServiceUtil;
import com.example.mobilesafe74.utils.SpUtils;
import com.example.mobilesafe74.view.SettingClickView;
import com.example.mobilesafe74.view.SettingItemView;


/**
 * Created by yueyue on 2017/1/13.
 */
public class SettingActivity extends AppCompatActivity {

    private String[] mToastStyleDes;
    private int mToastStyle;
    private SettingClickView scv_toast_style;
    private SettingClickView scv_location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //初始化更新的条目
        initUpdate();

        initAddress();

        initToastStyle();

        initLocation();

        initBlacknumber();

        initAppLock();
    }

    /**
     * 初始化程序锁方法
     */
    private void initAppLock() {
        final SettingItemView siv_app_lock = (SettingItemView) findViewById(R.id.siv_app_lock);
        boolean isRunning = ServiceUtil.isRunning(this, "com.example.mobilesafe74.service.WatchDogService");
        siv_app_lock.setCheck(isRunning);

        siv_app_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_app_lock.isCheck();
                siv_app_lock.setCheck(!isCheck);
                if(!isCheck){
                    //开启服务
                    startService(new Intent(getApplicationContext(), WatchDogService.class));
                }else{
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), WatchDogService.class));
                }
            }
        });
    }

    /**
     * 拦截黑名单设置
     */
    private void initBlacknumber() {
        final SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
        boolean isRunning = ServiceUtil.isRunning(getApplication(), "com.example.mobilesafe74.service.BlacknumberService");
        siv_blacknumber.setCheck(isRunning);

        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_blacknumber.isCheck();
                siv_blacknumber.setCheck(!isCheck);
                if (!isCheck) {
                    //开启服务
                    startService(new Intent(getApplicationContext(), BlacknumberService.class));
                } else {
                    //关闭服务
                    stopService(new Intent(getApplicationContext(), BlacknumberService.class));
                }
            }
        });


    }

    /**
     * 双击居中view所在屏幕位置的处理方法
     */
    private void initLocation() {
        scv_location = (SettingClickView) findViewById(R.id.scv_location);

        scv_location.setTitle("归属地提示框的位置");
        scv_location.setDes("设置归属地提示框的位置");


        scv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ToastLocationActivity.class));
            }
        });
    }

    private void initToastStyle() {
        scv_toast_style = (SettingClickView) findViewById(R.id.scv_toast_style);
        scv_toast_style.setTitle("设置归属地显示风格");
        mToastStyleDes = new String[]{"透明", "橙色", "蓝色", "灰色", "绿色"};
        mToastStyle = SpUtils.getInt(this, ConstantValue.TOAST_STYLE, 0);
        scv_toast_style.setDes(mToastStyleDes[mToastStyle]);

        //设置点击事件
        scv_toast_style.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToastStyleDialog();
            }
        });

    }

    /**
     * 创建选中显示样式的对话框
     */
    private void showToastStyleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher).
                setTitle("请选择归属地样式").
                setSingleChoiceItems(mToastStyleDes, mToastStyle, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SpUtils.putInt(getApplicationContext(), ConstantValue.TOAST_STYLE, which);

                        dialog.dismiss();
                        scv_toast_style.setDes(mToastStyleDes[which]);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        }).show();


    }

    /**
     * 是否显示电话号码归属地的方法
     */
    private void initAddress() {
        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
        //对服务是否开的状态做显示
        boolean isRunning = ServiceUtil.isRunning(this, "com.example.mobilesafe74.service.AddressService");
        siv_address.setCheck(isRunning);

        //点击过程中,状态(是否开启电话号码归属地)的切换过程
        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回点击前的选中状态
                boolean isCheck = siv_address.isCheck();
                siv_address.setCheck(!isCheck);
                if (!isCheck) {
                    //开启服务,管理吐司
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    //关闭服务,不需要显示吐司
                    stopService(new Intent(getApplicationContext(), AddressService.class));
                }
            }
        });
    }

    /**
     * 初始化更新的条目
     */
    private void initUpdate() {
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);

        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        siv_update.setCheck(open_update);

        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_update.isCheck();
                siv_update.setCheck(!isCheck);

                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !isCheck);
            }
        });

    }
}
