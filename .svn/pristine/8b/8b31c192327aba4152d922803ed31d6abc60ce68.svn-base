package com.example.mobilesafe74.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.example.mobilesafe74.db.BlackNumberDao;

public class BlacknumberService extends Service {


    private InnerSmsService mInnerSmsService;
    private BlackNumberDao mDao;
    private TelephonyManager mTM;
    private MyPhoneStateListener mPhoneStateListener;
    private MyContentObserver mMyContentObserver;

    @Override
    public void onCreate() {

        mDao = BlackNumberDao.getInstance(getApplicationContext());

        //动态注册一个短信服务
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);

        mInnerSmsService = new InnerSmsService();
        registerReceiver(mInnerSmsService, intentFilter);

        //拦截电话
        //电话状态的监听(服务开启的时候,需要去做监听,关闭的时候电话状态就不需要监听)
        //1,电话管理者对象
        mTM = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //2,监听电话状态
        mPhoneStateListener = new MyPhoneStateListener();
        mTM.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        super.onCreate();
    }

    class MyPhoneStateListener extends PhoneStateListener {
        //3,手动重写,电话状态发生改变会触发的方法
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    //空闲状态,没有任何活动(移除吐司)
                    deleteBlackCallLog(incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    //摘机状态，至少有个电话活动。该活动或是拨打（dialing）或是通话
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    //响铃(展示吐司)
                    break;
            }
            super.onCallStateChanged(state, incomingNumber);
        }

    }

    /**
     * 删除那些黑名单来电的手机号码
     */
    private void deleteBlackCallLog(String phone) {
        mMyContentObserver = new MyContentObserver(new Handler(), phone);
        getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"), true, mMyContentObserver);
    }

    private class MyContentObserver extends ContentObserver {
        private final String phone;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler, String phone) {
            super(handler);
            this.phone = phone;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getContentResolver().delete(Uri.parse("content://call_log/calls"), "number=?", new String[]{phone});
        }
    }
/*
    //拦截电话
    private void endCall(String phone) {
      // ITelephony.Stub.asInterface(ServiceManager.getService(Context.TELEPHONY_SERVICE));
        int mode = mDao.getMode(phone);
        //模式为2或者3才会拦截电话
        if (mode==2 || mode==3){


            try {
                Class<?> clazz  = Class.forName("android.os.ServiceManager");
                Method method = clazz.getMethod("getService", String.class);
                IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
//                ITelephony.Stub.asInterface(iBinder);


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

*/

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onDestroy() {
        //取消短信监听服务
        if (mInnerSmsService!=null){

            unregisterReceiver(mInnerSmsService);
        }
        //取消内容观察者
        if (mMyContentObserver!=null){
            getContentResolver().unregisterContentObserver(mMyContentObserver);
        }

        if (mPhoneStateListener!=null){
            mTM.listen(mPhoneStateListener,PhoneStateListener.LISTEN_NONE);
        }
        super.onDestroy();
    }

    class InnerSmsService extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //获取发送者的号码 和发送内容 pdus是固定写法来的
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            //循环遍历短信
            for (Object object : objects) {
                //[1]获取smsmessage实例
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) object);
                //[2]获取发送短信的内容
                String messageBody = smsMessage.getMessageBody();//获得发来短信的内容
                String address = smsMessage.getOriginatingAddress();//获得短信的发送者的手机号码

                int mode = mDao.getMode(address);
                //如果模式是1(短信)或者3(所有)的话,那就拦截
                if (mode == 1 || mode == 3) {
                    abortBroadcast();
                }
            }
        }
    }


}