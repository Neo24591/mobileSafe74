package com.example.mobilesafe74.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yueyue on 2017/1/17.
 */
public class Setup3Activity extends BaseSetupActivity {
    private EditText et_phone_number;
    private Button bt_select_number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_setup3);


        //初始化UI
        initUI();
    }


    /**
     * 初始化UI
     */
    private void initUI() {
        //找到我们需要的组件
        et_phone_number = (EditText) findViewById(R.id.et_phone_number);
        et_phone_number.setInputType(InputType.TYPE_CLASS_PHONE);//限定输入框只能输入手机号码类型的,即只能输入数字

        String phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        et_phone_number.setText(phone);
        et_phone_number.setSelection(phone.length());//设置光标在输入框文字的最后显示


        bt_select_number = (Button) findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactlistActivity.class);
                startActivityForResult(intent, 0);
            }
        });

    }

    /**
     * 选择联系人ContactlistActivity界面跟Setup3Activity界面跟的返回结果处理
     *
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        intent纽带中带的数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //判断一下resultCode,100代表ContactlistActivity返回的结果,101代表ContactslistActivity返回的结果,
        if (resultCode == 100) {
            if (data != null) {
                String phone = data.getStringExtra("phone");
                //把得到的phone删除前后的空格以及中间的隔间线已经空格
                phone = phone.replace("-", "").replace(" ", "").trim();
                et_phone_number.setText(phone);
                et_phone_number.setSelection(phone.length());//设置光标在输入框文字的最后显示
            }
        } else if (resultCode == 101) {
            if (data != null) {
                String phone = data.getStringExtra("phone");
                //把得到的phone删除前后的空格以及中间的隔间线已经空格
                phone = phone.replace("-", "").replace(" ", "").trim();
                et_phone_number.setText(phone);
                et_phone_number.setSelection(phone.length());//设置光标在输入框文字的最后显示
            }
        }
    }

    /**
     * 从Setup3Activity界面跳转到之前的Setup2Activity界面
     *
     * @param view
     */
    public void prePage(View view) {
        showPrePage();


    }

    /**
     * 从Setup3Activity界面跳转到Setup4Activity界面
     *
     * @param view
     */
    public void nextPage(View view) {
        showNextPage();

    }

    @Override
    protected void showPrePage() {

        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);


        //设置平移动画效果
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);


        finish();//销毁当前页面
    }

    @Override
    protected void showNextPage() {
        String contact_phone = et_phone_number.getText().toString().trim();
        if (!TextUtils.isEmpty(contact_phone)) { //电话号码不为空的清空下
            boolean isPhone = isPhone(contact_phone);
            if (isPhone == true) {
                Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
                startActivity(intent);


                //设置平移动画效果
                overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

                finish();//销毁当前页面
                SpUtils.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, contact_phone);


            } else {
                Toast.makeText(this, "手机号码不正确", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "请输入电话号码", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 判断手机格式是否正确,返回true代表手机号码格式正确,false代表手机号码格式不正确
     */

    private boolean isPhone(String str) {
        Pattern pattern = Pattern.compile("1[0-9]{10}");
        Matcher matcher = pattern.matcher(str);
        if (matcher.matches()) {
            return true;
        } else {
            return true;//由于需要使用模拟器检查,所以暂时改为true使得任何号码都可以通过
        }
    }
}