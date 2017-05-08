package com.example.mobilesafe74.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        //看看手机防盗卫士是否开启了防盗功能
        boolean open_security = SpUtils.getBoolean(context, ConstantValue.OPEN_SECURITY, false);

        if (open_security) {

            //获取发送者的号码 和发送内容 pdus是固定写法来的
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //循环遍历短信
            for (Object object : objects
                    ) {
                //[1]获取smsmessage实例
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                //[2]获取发送短信的内容
                String messageBody = smsMessage.getMessageBody();//获得发来短信的内容
//                String address = smsMessage.getOriginatingAddress();//获得短信的发送者的手机号码

                //判断一下短信是否有我们自定义的关键字
                if (messageBody.contains("#*alarm*#")) {

                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();

                }
            }

        }
    }
}
