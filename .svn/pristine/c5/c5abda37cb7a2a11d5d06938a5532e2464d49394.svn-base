package com.example.mobilesafe74.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.mobilesafe74.R;
import com.example.mobilesafe74.engine.CommonnumDao;

import java.util.List;

/**
 * Created by yueyue on 2017/1/29.
 */
public class CommonNumberQueryActivity extends AppCompatActivity {
    private ExpandableListView elv_common_number;
    private List<CommonnumDao.Group> mGroup;
    private MyAdapter mMyAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_number);


        initUI();

        initData();

    }

    private void initData() {
        //准备数据
        CommonnumDao commonnumDao = new CommonnumDao();
        mGroup = commonnumDao.getGroup();


        mMyAdapter = new MyAdapter();
        elv_common_number.setAdapter(mMyAdapter);

        elv_common_number.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                startCall(mGroup.get(groupPosition).childList.get(childPosition).number);

                return false;
            }
        });
    }

    private void startCall(String number) {
    /*    Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        startActivity(intent);*/
        //开启系统的打电话界面
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE},10);

        }
        startActivity(intent);

    }

    private void initUI() {
        elv_common_number = (ExpandableListView) findViewById(R.id.elv_common_number);
    }


     class MyAdapter extends BaseExpandableListAdapter {
         @Override
         public int getGroupCount() {
             return mGroup.size();
         }

         @Override
         public int getChildrenCount(int groupPosition) {
             return mGroup.get(groupPosition).childList.size();
         }

         @Override
         public CommonnumDao.Group getGroup(int groupPosition) {
             return mGroup.get(groupPosition);
         }

         @Override
         public CommonnumDao.Child getChild(int groupPosition, int childPosition) {
             return mGroup.get(groupPosition).childList.get(childPosition);
         }

         @Override
         public long getGroupId(int groupPosition) {
             return groupPosition;
         }

         @Override
         public long getChildId(int groupPosition, int childPosition) {
             return childPosition;
         }

         @Override
         public boolean hasStableIds() {
             return false;
         }

         @Override
         public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
             TextView textView = new TextView(getApplicationContext());
             textView.setText("			"+getGroup(groupPosition).name);
             textView.setTextColor(Color.RED);
             textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
             return textView;
         }

         @Override
         public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

             if (convertView==null){
                 convertView=View.inflate(getApplicationContext(),R.layout.elv_child_item,null);
             }

             TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
             TextView tv_number = (TextView) convertView.findViewById(R.id.tv_number);

             tv_name.setText(mGroup.get(groupPosition).childList.get(childPosition).name);
             tv_number.setText(mGroup.get(groupPosition).childList.get(childPosition).number);

             return convertView;
         }

         @Override
         public boolean isChildSelectable(int groupPosition, int childPosition) {
             return true;
         }
     }
}
