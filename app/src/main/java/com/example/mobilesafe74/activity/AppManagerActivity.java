package com.example.mobilesafe74.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.bean.AppInfo;
import com.example.mobilesafe74.provider.AppInfoProvider;

import java.util.List;


public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lv_app_list;
    private List<AppInfo> mAppInfoList;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            mMyAdapter = new MyAdapter();
            lv_app_list.setAdapter(mMyAdapter);
        }
    };
    private MyAdapter mMyAdapter;
    private PopupWindow mPopupWindow;
    private AppInfo mAppInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initTitle();

        initList();
    }


    private void initList() {
        lv_app_list = (ListView) findViewById(R.id.lv_app_list);

        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mHandler.sendEmptyMessage(0);
            }
        }){}.start();

        lv_app_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //滚动过程中调用方法
                //AbsListView中view就是listView对象
                //firstVisibleItem第一个可见条目索引值
                //visibleItemCount当前一个屏幕的可见条目数
                //总共条目总数
                mAppInfo = mAppInfoList.get(position);
                showPopupWindow(view);
            }
        });
    }

    @Override
    protected void onResume() {
        //重新获取数据
        getData();
        super.onResume();
    }

    private void getData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                mHandler.sendEmptyMessage(0);
            }
        }){}.start();
    }

    private void showPopupWindow(View view) {
        View popupView = View.inflate(this, R.layout.popupwindow_layout, null);

        TextView tv_uninstall = (TextView) popupView.findViewById(R.id.tv_uninstall);
        TextView tv_start = (TextView) popupView.findViewById(R.id.tv_start);
        TextView tv_share = (TextView) popupView.findViewById(R.id.tv_share);

        tv_uninstall.setOnClickListener(this);
        tv_start.setOnClickListener(this);
        tv_share.setOnClickListener(this);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);

        //创建一个popupWindow实例
        mPopupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);

        //不带参数就是说明是透明的
       mPopupWindow.setBackgroundDrawable(new ColorDrawable());
        //3指定窗体位置
        mPopupWindow.showAtLocation(view, Gravity.CENTER,50,view.getHeight()*(-1));

        //开始执行两种动画
        popupView.startAnimation(animationSet);

    }


    /**
     * 获得磁盘以及sd卡可用大小
     */
    private void initTitle() {
        //获得磁盘的路径
        String path= Environment.getDataDirectory().getAbsolutePath();
        //获得sd卡路径
        String sdPath=Environment.getExternalStorageDirectory().getAbsolutePath();

        String memorySize = Formatter.formatFileSize(this, getAvailSpace(path));
        String sdMemorySize = Formatter.formatFileSize(this, getAvailSpace(sdPath));

        //找到我们需要组件
        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);

        tv_memory.setText("磁盘可用:"+memorySize);
        tv_sd_memory.setText("sd卡可用"+sdMemorySize);
    }

    /**
     * 获得磁盘(sd卡)可用空间
     * @param path 磁盘(获得sd卡)路径
     */
    private long getAvailSpace(String path) {
        StatFs statFs = new StatFs(path);
        //获取可用区块的个数

        long count = statFs.getAvailableBlocks();
        //获取可用区块的大小
        long size = statFs.getBlockSize();

        return count*size;
    }

    /**
     * 点击事件的响应
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_uninstall:
                //卸载应用
                if (mAppInfo.isSystem){
                    Toast.makeText(this, "系统应用不能卸载", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent("android.intent.action.VIEW");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package"));
                    startActivity(intent);
                }
                break;
            case R.id.tv_start:
                PackageManager packageManager = getPackageManager();
                Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(mAppInfo.getPackageName());
                if(launchIntentForPackage!=null){
                    startActivity(launchIntentForPackage);
                }else{
                    Toast.makeText(this, "此应用不能被开启", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.tv_share:
                Intent intent1 = new Intent(Intent.ACTION_SEND);
                intent1.putExtra(Intent.EXTRA_TEXT,"分享一个应用,应用名称为"+mAppInfo.getName());
                intent1.setType("text/plain");
                startActivity(intent1);
            break;
        }
        if (mPopupWindow!=null){
            mPopupWindow.dismiss();
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mAppInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return mAppInfoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder=null;
            if (convertView==null){
                convertView=View.inflate(getApplicationContext(),R.layout.listview_app_item,null);
                viewHolder=new ViewHolder();
                viewHolder.iv_icon = (ImageView)convertView.findViewById(R.id.iv_icon);
                viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_name);
                viewHolder.tv_path = (TextView) convertView.findViewById(R.id.tv_path);
                convertView.setTag(viewHolder);
            }else {
              viewHolder=(ViewHolder)  convertView.getTag();
            }
            viewHolder.iv_icon.setBackgroundDrawable(mAppInfoList.get(position).getIcon());
            viewHolder.tv_name.setText(mAppInfoList.get(position).getName());
            if(mAppInfoList.get(position).isSdCard()){
                viewHolder.tv_path.setText("sd卡应用");
            }else{
                viewHolder.tv_path.setText("手机应用");
            }



            return convertView;
        }
    }


    static class ViewHolder {
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_path;
    }
}
