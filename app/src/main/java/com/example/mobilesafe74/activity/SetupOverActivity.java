package com.example.mobilesafe74.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;

/**
 * Created by yueyue on 2017/1/17.
 */
public class SetupOverActivity extends AppCompatActivity{

    private TextView tv_phone;
    private TextView tv_retset_setup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean setup_over = SpUtils.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (setup_over){
            //密码输入成功,并且四个导航界面设置完成----->停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);

            initUI();
        }else {
            //密码输入成功,四个导航界面没有设置完成----->跳转到导航界面第1个
            Intent intent = new Intent(this, Setup1Activity.class);
            startActivity(intent);

            finish();////开启了一个新的界面以后,关闭功能列表界面
        }
    }

    /**
     * 初始化UI
     */
    private void initUI() {

        //找到我们需要的组件
        tv_phone = (TextView) findViewById(R.id.tv_phone);

        //拿到手机联系人电话号码
        String phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        tv_phone.setText(phone);

        tv_retset_setup = (TextView) findViewById(R.id.tv_retset_setup);

        tv_retset_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击事件,跳转到Setup1Activity这个界面
                Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
                startActivity(intent);
            }
        });
    }
}
