package com.example.mobilesafe74.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mobilesafe74.bean.BlackNumberInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueyue on 2017/1/25.
 */

public class BlackNumberDao {

    private BlackNumberOpenHelper mBlackNumberOpenHelper;

    //1.创建一个私有的方法
    private BlackNumberDao(Context context) {
        mBlackNumberOpenHelper = new BlackNumberOpenHelper(context);
    }

    //2.创建一个私有的对象
    private static BlackNumberDao mBlackNumberDao = null;

    //3,提供一个静态方法,如果当前类的对象为空,创建一个新的
    public static BlackNumberDao getInstance(Context context) {
        if (mBlackNumberDao == null) {
            mBlackNumberDao = new BlackNumberDao(context);
        }
        return mBlackNumberDao;
    }

    /**
     * 增加一条条目
     *
     * @param phone 拦截的电话号码
     * @param mode  拦截类型(1:短信	2:电话	3:拦截所有(短信+电话))
     */
    public void insert(String phone, String mode) {
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put("phone", phone);
            contentValues.put("mode", mode);
            db.insert("blacknumber", null, contentValues);

        db.close();
    }

    /**
     * 从数据库中删除一条电话号码
     *
     * @param phone 删除电话号码
     */
    public void delete(String phone) {
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

        db.delete("blacknumber", "phone=?", new String[]{phone});
        db.close();
    }

    /**
     * 根据电话号码去,更新拦截模式
     *
     * @param phone 更新拦截模式的电话号码
     * @param mode  要更新为的模式(1:短信	2:电话	3:拦截所有(短信+电话)
     */
    public void update(String phone, String mode) {
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("mode", mode);
        db.update("blacknumber", contentValues, "phone=?", new String[]{phone});

        db.close();
    }

    /**
     * @return 查询到数据库中所有的号码以及拦截类型所在的集合
     */
    public List<BlackNumberInfo> findAll() {
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

        List<BlackNumberInfo> blackNumberInfoList = new ArrayList<>();
        Cursor cursor = db.query("blacknumber", new String[]{"phone", "mode"}, null, null, null, null, "_id desc");
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setPhone(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfoList.add(blackNumberInfo);
        }

        cursor.close();
        db.close();
        return blackNumberInfoList;
    }

    /**
     * 每次查询20条
     * @param index 查询数据库的索引值
     * @return 查询到数据库的号码以及拦截类型所在的集合
     */
    public List<BlackNumberInfo> find(int index) {
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

        List<BlackNumberInfo> blackNumberInfoList = new ArrayList<>();
        //select phone,mode from blacknumber order by _id desc limit ?,20;
        Cursor cursor = db.rawQuery("select phone,mode from blacknumber order by _id desc limit ?,20;",new String[]{index+""});
        while (cursor.moveToNext()) {
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setPhone(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfoList.add(blackNumberInfo);
        }

        cursor.close();
        db.close();
        return blackNumberInfoList;
    }

    /**
     * @return	数据库中数据的总条目个数,返回0代表没有数据或异常
     */
    public int getCount(){
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

        int count=0;
        List<BlackNumberInfo> blackNumberInfoList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select count(*) from blacknumber;",null);
        while (cursor.moveToNext()) {
            count=cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * @param phone	作为查询条件的电话号码
     * @return	传入电话号码的拦截模式	1:短信	2:电话	3:所有	0:没有此条数据
     */
    public int getMode(String phone){
        SQLiteDatabase db = mBlackNumberOpenHelper.getReadableDatabase();

        int mode=0;
        List<BlackNumberInfo> blackNumberInfoList = new ArrayList<>();
        Cursor cursor = db.query("blacknumber",new String[]{"mode"},"phone=?",new String[]{phone},null,null,null);
        while (cursor.moveToNext()) {
            mode=cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return mode;
    }





}
