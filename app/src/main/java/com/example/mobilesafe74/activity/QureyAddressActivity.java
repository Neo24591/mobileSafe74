package com.example.mobilesafe74.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.engine.AddressDao;

/**
 * Created by yueyue on 2017/1/22.
 */
public class QureyAddressActivity extends AppCompatActivity {
    String TAG = "QureyAddressActivity";
    private EditText et_phone;
    private Button bt_query;
    private TextView tv_address;
    private String mAddress;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tv_address.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        /*//测试专用
        String address = AddressDao.getAddress("13000200000");
        Log.i(TAG,address);*/
        initUI();
    }

    /**
     * 初始化UI
     */
    private void initUI() {
        et_phone = (EditText) findViewById(R.id.et_phone);
        bt_query = (Button) findViewById(R.id.bt_query);
        tv_address = (TextView) findViewById(R.id.tv_address);


        bt_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = et_phone.getText().toString().trim();
//                query("13000200000");
                if (!TextUtils.isEmpty(phone)) {

                    query(phone);
                } else {
                    Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                   et_phone.startAnimation(shake);

                    //手机震动效果(vibrator 震动)
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //震动毫秒值
                    vibrator.vibrate(2000);
                    //规律震动(震动规则(不震动时间,震动时间,不震动时间,震动时间.......),重复次数)
                    vibrator.vibrate(new long[]{2000,5000,2000,5000}, -1);
                }
            }
        });

        //实时查询
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = et_phone.getText().toString().trim();
                query(phone);
            }
        });


    }

    private void query(final String phone) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAddress = AddressDao.getAddress(phone);

                mHandler.sendEmptyMessage(0);

            }
        }) {
        }.start();

    }

}
