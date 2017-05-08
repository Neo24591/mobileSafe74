package com.example.mobilesafe74.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.utils.ConstantValue;
import com.example.mobilesafe74.utils.SpUtils;

/**
 * Created by yueyue on 2017/1/24.
 */
public class ToastLocationActivity extends AppCompatActivity {

    private ImageView iv_drag;
    private Button bt_top, bt_bottom;
    private WindowManager mWindowManager;
    private int mScreenHeight;
    private int mScreenWidth;
    private long[] mHits = new long[2];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);

        initUI();

    }

    private void initUI() {
        //可拖拽双击居中的图片控件
        iv_drag = (ImageView) findViewById(R.id.iv_drag);
        bt_top = (Button) findViewById(R.id.bt_top);
        bt_bottom = (Button) findViewById(R.id.bt_bottom);

        //获得屏幕的高度以及宽度
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();
        mScreenWidth = mWindowManager.getDefaultDisplay().getWidth();

        //获得sp存储的宽度以及高度
        int locationX = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_X, 0);
        int locationY = SpUtils.getInt(getApplicationContext(), ConstantValue.LOCATION_Y, 0);

        //文字显示的位置处理,图片在屏幕下方,文字在下方显示,否则在上方显示
        if (locationY > mScreenHeight / 2) {
            bt_top.setVisibility(View.INVISIBLE);
            bt_bottom.setVisibility(View.VISIBLE);
        }else {
            bt_top.setVisibility(View.VISIBLE);
            bt_bottom.setVisibility(View.INVISIBLE);
        }

        //设置宽度以及高度
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = locationX;
        params.topMargin = locationY;
        iv_drag.setLayoutParams(params);

        iv_drag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
                mHits[mHits.length-1] = SystemClock.uptimeMillis();
                if (mHits[mHits.length-1] -mHits[0] <500){
                    //满足双击事件后,调用代码
                    int left = mScreenWidth/2 - iv_drag.getWidth()/2;
                    int top = mScreenHeight/2 - iv_drag.getHeight()/2;
                    int right = mScreenWidth/2+iv_drag.getWidth()/2;
                    int bottom = mScreenHeight/2+iv_drag.getHeight()/2;

                    //控件按以上规则显示
                    iv_drag.layout(left, top, right, bottom);

                    //存储最终位置
                    SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
                    SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
                }
            }
        });

        iv_drag.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //最初的坐标位置
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //移动中的坐标位置
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();

                        //移动的位置(距离)
                        int disX = moveX - startX;
                        int disY = moveY - startY;


                        //1,当前控件所在屏幕的(左,上,右,下)角的位置
                        int left = iv_drag.getLeft() + disX;
                        int top = iv_drag.getTop() + disY;
                        int right = iv_drag.getRight() + disX;
                        int bottom = iv_drag.getBottom() + disY;

                        //容错处理
                        if (left < 0 || top < 0 || right > mScreenWidth || bottom > (mScreenHeight - 22)) {
                            return true;
                        }

                        //文字显示的位置处理,图片在屏幕下方,文字在下方显示,否则在上方显示
                        if (top > mScreenHeight / 2) {
                            bt_top.setVisibility(View.INVISIBLE);
                            bt_bottom.setVisibility(View.VISIBLE);
                        }else {
                            bt_top.setVisibility(View.VISIBLE);
                            bt_bottom.setVisibility(View.INVISIBLE);
                        }


                        //设置移动之后的位置
                        iv_drag.layout(left, top, right, bottom);

                        //重置坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:

                        //4,存储移动到的位置
                        SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_X, iv_drag.getLeft());
                        SpUtils.putInt(getApplicationContext(), ConstantValue.LOCATION_Y, iv_drag.getTop());
                        break;
                }
                return false;
            }
        });


    }
}
