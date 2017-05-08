package com.example.mobilesafe74.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe74.R;

/**
 * Created by yueyue on 2017/1/13.
 */

public class SettingClickView extends RelativeLayout {

    private Context mContext;
    private ImageView iv_toast;
    private TextView tv_des;
    private TextView tv_title;


    public SettingClickView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_click_view, this);
        this.mContext = context;

        //找到我们需要的组件
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        iv_toast = (ImageView) findViewById(R.id.iv_toast);


//        tv_title.setText(mDestitle);
    }

    public SettingClickView(Context context) {
        this(context, null);
    }

    /**
     * 设置标题
     *
     * @param title 设置标题的内容
     */
    public void setTitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 设置描述
     *
     * @param des 设置标题的描述内容
     */
    public void setDes(String des) {
        tv_des.setText(des);
    }

}
