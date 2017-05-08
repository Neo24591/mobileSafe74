package com.example.mobilesafe74.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yueyue on 2017/1/19.
 */
public abstract class BaseSetupActivity extends AppCompatActivity{
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

                //根据手势的左右判断向左还是向右滑动
                if ((e1.getX()-e2.getX())>0){
                    //说明由右向左滑动
                    showNextPage();


                }else if ((e1.getX()-e2.getX())<0){
                    //说明由左向右滑动
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });

    }


    /**
     * 监听屏幕响应的事件,按下(1次),移动(多次).抬起(1次)
     * @param event  按下(1次),移动(多次).抬起(1次)
     * @return 都是返回真的,说明无时无刻都在监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 滑动跳转界面,由子类决定跳转到下一个页面
     */
    protected abstract void showPrePage();

    /**
     * 滑动跳转界面,由子类决定跳转到上一个页面
     */
    protected abstract void showNextPage();

    /**
     * 跳转到下一个界面,由子类去实现
     * @param view
     */
    public abstract void nextPage(View view);

    /**
     * 跳转到上一个界面,由子类去实现
     * @param view
     */
    public abstract void prePage(View view);

}
