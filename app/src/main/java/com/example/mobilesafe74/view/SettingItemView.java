package com.example.mobilesafe74.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mobilesafe74.R;

/**
 * Created by yueyue on 2017/1/13.
 */

public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE ="http://schemas.android.com/apk/res/com.example.mobilesafe74";
    private static final String TAG ="SettingItemView";
    private  Context mContext;
    private CheckBox cb_box;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;


    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.setting_item_view, this);
        this.mContext=context;

        //找到我们需要的组件
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        cb_box = (CheckBox) findViewById(R.id.cb_box);

        //拿到attrs的属性
        initAttrs(attrs);

        tv_title.setText(mDestitle);
    }

    /**
     * @param attrs xml文件的属性都在这个集合里面
     */
    private void initAttrs(AttributeSet attrs) {

        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

        Log.i(TAG, mDestitle);
        Log.i(TAG, mDesoff);
        Log.i(TAG,mDeson);

    }


    public SettingItemView(Context context) {
        this(context, null);
    }

    /**
     * @return 返回checkBox是否选中(跟SettingItemView绑定, 即是状态一致),
     * 如果选中,那就返回true,如果没有被选中,那就返回false
     */
    public boolean isCheck() {
        return cb_box.isChecked();
    }

    /**
     * @param isCheck 由外部来传递checkBox是否选中
     */
    public void setCheck(boolean isCheck) {
        cb_box.setChecked(isCheck);
        if (isCheck) {
            tv_des.setText(mDeson);
        } else {
            tv_des.setText(mDesoff);
        }
    }
}
