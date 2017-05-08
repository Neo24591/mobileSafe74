package com.example.mobilesafe74.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobilesafe74.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yueyue on 2017/1/18.
 */
public class ContactslistActivity extends AppCompatActivity {

    private ListView lv_contact;
    private static final String TAG = "ContactlistActivity";
    private List<HashMap<String,String>> contactlists = new ArrayList<>();
    public Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);

        }
    };
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        //初始化UI
        initUI();

        //初始化数据
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获得内容解析者
                ContentResolver resolver = getContentResolver();
                // 查询raw_contacts表.获得联系人游标
                Cursor cursor = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);
                //使用之前先清空里面原有的数据
                contactlists.clear();
                //通过cursor拿出整张raw_contacts联系人表id
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    Log.i(TAG, id);
                    Cursor indexCursor = resolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"}, "raw_contact_id=?",
                            new String[]{id}, null);
                  //  Person person = new Person();
                    HashMap<String, String> hashMap = new HashMap<>();
                    //indexCursor拿到我们需要的名字以及电话号码.接着Person类的数据封装到contactlists中
                    while (indexCursor.moveToNext()) {
                        String data1 = indexCursor.getString(0);
                        String type = indexCursor.getString(1);
                        Log.i(TAG, data1);
                        Log.i(TAG, type);

                        //如果联系人的表格中data1数据不为空,那么才执行操作
                        if (!TextUtils.isEmpty(data1)) {
                            //判断data1表中的数据,获得我们需要的数据如名字以及电话号码
                            if (type.equals("vnd.android.cursor.item/name")) {
//                                person.setName(data1);
                                hashMap.put("name",data1);
                            } else if (type.equals("vnd.android.cursor.item/phone_v2")) {
//                                person.setPhone(data1);
                                hashMap.put("phone",data1);
                            }
                        }
                    }
                    //关闭indexCursor游标,释放资源
                    if (indexCursor != null) {
                        indexCursor.close();
                    }
                    contactlists.add(hashMap);
                }
                //关闭cursor游标,释放资源
                if (cursor != null) {
                    cursor.close();
                }

                //发送消息机制,通知主线程已经把数据准备好了,可以拿去用了
                mHandler.sendEmptyMessage(0);


            }
        }) {
        }.start();


    }

    /**
     * 初始化UI
     */
    private void initUI() {
        //找到我们需要的组件
        lv_contact = (ListView) findViewById(R.id.lv_contact);
        //为listView组件注册一个监听事件
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
           //     HashMap<String, String> hashMap = contactlists.get(position);
                HashMap<String, String> hashMap = mAdapter.getItem(position);
                if (hashMap!=null){
                    String phone = hashMap.get("phone");
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(101,intent);

                    finish();
                }
            }
        });
    }

    /**
     * 设置一个内容适配器,主要用来显示ContactActivity界面的数据
     */
    public class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return contactlists.size();
        }

        @Override
        public HashMap<String,String> getItem(int position) {
            return contactlists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            ItemHolder itemHolder;
            if (convertView==null){
                view=View.inflate(getApplicationContext(),R.layout.listview_contact_item,null);
                itemHolder=new ItemHolder();
                itemHolder.tv_name= (TextView) view.findViewById(R.id.tv_name);
                itemHolder.tv_phone= (TextView) view.findViewById(R.id.tv_phone);
                view.setTag(itemHolder);
            }else {
                view=convertView;
                itemHolder= (ItemHolder) view.getTag();
            }

            //设置显示的数据
            HashMap<String, String> hashMap = contactlists.get(position);
            if (hashMap!=null){
                itemHolder.tv_name.setText(hashMap.get("name"));
                itemHolder.tv_phone.setText(hashMap.get("phone"));
            }

            return view;
        }

        //设置一个内部类,主要用来装两个组件
        private class ItemHolder {
            TextView tv_name;
            TextView tv_phone;
        }
    }
}
