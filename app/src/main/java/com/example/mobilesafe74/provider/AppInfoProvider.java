package com.example.mobilesafe74.provider;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.mobilesafe74.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueyue on 2017/1/27.
 */

public class AppInfoProvider {
    /**
     * 返回当前手机所有的应用的相关信息(名称,包名,图标,(手机内存,sd卡),(系统,用户));
     *
     * @param context 获取包管理者的上下文环境
     * @return 包含手机安装应用相关信息的集合
     */
    public static List<AppInfo> getAppInfoList(Context context) {
        //获得报名的管理者对象
        PackageManager packageManager = context.getPackageManager();
        //获取安装在手机上应用相关信息的集合
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (PackageInfo packageInfo : packageInfos) {
            AppInfo appInfo = new AppInfo();
            //获得应用的包名
            appInfo.packageName = packageInfo.packageName;
            //应用名称
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            appInfo.name = applicationInfo.loadLabel(packageManager).toString();
            //获取图标
            appInfo.icon = applicationInfo.loadIcon(packageManager);
            //判断是否为系统应用(每一个手机上的应用对应的flag都不一致)
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==ApplicationInfo.FLAG_SYSTEM){
                //系统应用
                appInfo.isSystem=true;
            }else {
                //用户应用
                appInfo.isSystem=false;
            }
            //是否为sd卡中安装应用
            if ((applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) ==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
                //在sd卡中安装应用
                appInfo.isSdCard=true;
            }else {
                //没有在sd卡安装应用
                appInfo.isSdCard=false;
            }
            appInfoList.add(appInfo);
        }

        return appInfoList;
    }

}
