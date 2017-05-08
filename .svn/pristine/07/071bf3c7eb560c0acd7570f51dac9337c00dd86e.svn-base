package com.example.mobilesafe74.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.GestureDetector;
import android.view.View;

import com.example.mobilesafe74.R;

/**
 * Created by yueyue on 2017/1/17.
 */
public class Setup1Activity extends BaseSetupActivity {


    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_setup1);


    }


    @Override
    protected void showPrePage() {

    }

    /**
     * 从Setup1Activity界面跳转到Setup2Activity界面
     */
    @Override
    protected void showNextPage() {

        Intent intent = new Intent(Setup1Activity.this, Setup2Activity.class);
        startActivity(intent);


        //设置平移动画效果
        overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

        finish();//跳转到下一页的是否,销毁当前的页面
    }

    /**
     * 从Setup1Activity界面跳转到Setup2Activity界面
     *
     * @param view
     */
    public void nextPage(View view) {
        showNextPage();

    }

    @Override
    public void prePage(View view) {

    }


}
