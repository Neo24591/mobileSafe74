package com.example.mobilesafe74.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yueyue on 2017/1/15.
 */

public class SpUtils {


    private static SharedPreferences sp;

    /**
     * 写操作,写入设置中心那里的状态
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */

    public static void putBoolean(Context context, String key, boolean value) {

        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sp.edit().putBoolean(key, value);
        editor.commit();

    }

    /**
     * 读操作,获得设置中心那里的更新的设置
     * @param context  上下文环境
     * @param key      键
     * @param defValue 默认值
     * @return  默认值或者键存储的值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sp.getBoolean(key, defValue);

    }

    /**
     * 写入状态的操作
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */

    public static void putString(Context context, String key, String value) {

        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sp.edit().putString(key, value);
        editor.commit();

    }

    /**
     * 读操作
     * @param context  上下文环境
     * @param key      键
     * @param defValue 默认值
     * @return  默认值或者键存储的值
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sp.getString(key, defValue);

    }
    /**
     * 写入状态的操作
     * @param context 上下文环境
     * @param key     键
     * @param value   值
     */

    public static void putInt(Context context, String key, int value) {

        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sp.edit().putInt(key, value);
        editor.commit();

    }

    /**
     * 读操作
     * @param context  上下文环境
     * @param key      键
     * @param defValue 默认值
     * @return  默认值或者键存储的值
     */
    public static int getInt(Context context, String key, int defValue) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        return sp.getInt(key, defValue);

    }

    /**
     * 从sp中移除特定的节点
     * @param context 上下文环境
     * @param key  键
     */
    public static void remove(Context context, String key) {
        if (sp == null) {
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sp.edit().remove(key);
        editor.commit();
    }
}
