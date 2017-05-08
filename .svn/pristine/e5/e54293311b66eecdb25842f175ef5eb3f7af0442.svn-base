package com.example.mobilesafe74.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;

public class BootReceiver extends BroadcastReceiver {


    private String TAG="BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastRceiver is receivin        // an Intent broadcast.
        Log.i(TAG,"检测到了手机重启了");

        //获得手机卡当前的序列号
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String serialNumber = tm.getSimSerialNumber()+"xxx";

        //获得上次在xml中保存的序列号
        String preSerialNumber = SpUtils.getString(context, ConstantValue.SIM_NUMBER, "");

        //两者比较,如果不相等,说明序列号已经发生了更改
        if (!serialNumber.equals(preSerialNumber)){
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage("5556",null,"phoneNumber chang!!!",null,null);
        }


    }
}
