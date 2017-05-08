package com.example.mobilesafe74.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by yueyue on 2017/1/27.
 */
public class SmsBackUp {

    private static int index = 0;
    private static Cursor sCursor;
    private static FileOutputStream sFos;

    public static void backup(Context context, String path, CallBack callBack) {
        try {
            File file = new File(path);

       /* Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                new String[]{"address", "date", "type", "body"}, null, null, null);*/
            //2,获取内容解析器,获取短信数据库中数据
            sCursor = context.getContentResolver().query(Uri.parse("content://sms/"),
                    new String[]{"address", "date", "type", "body"}, null, null, null);

            //设置进入条最大的进度
            if (callBack!=null){
                callBack.setMax(sCursor.getCount());
            }

            sFos = new FileOutputStream(file);

            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(sFos, "utf-8");
            xmlSerializer.startDocument("utf-8", true);

            xmlSerializer.startTag(null, "smss");

            while (sCursor.moveToNext()) {
                xmlSerializer.startTag(null, "sms");

                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(sCursor.getString(0));
                xmlSerializer.endTag(null, "address");

                xmlSerializer.startTag(null, "date");
                xmlSerializer.text(sCursor.getString(1));
                xmlSerializer.endTag(null, "date");


                xmlSerializer.startTag(null, "type");
                xmlSerializer.text(sCursor.getString(2));
                xmlSerializer.endTag(null, "type");

                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(sCursor.getString(3));
                xmlSerializer.endTag(null, "body");


                xmlSerializer.endTag(null, "sms");

                index++;
                Thread.sleep(500);//睡眠0.5秒,方便用户看到进度条使得其知道短信正在备份
                if (callBack!=null){
                    callBack.setProgress(index);
                }
            }

            xmlSerializer.endTag(null, "smss");

            xmlSerializer.endDocument();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (sCursor != null) {
                sCursor.close();
            }

            if (sFos != null) {
                try {
                    sFos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    public interface CallBack{
        public void  setMax(int max);
        public void  setProgress(int progress);
    }
}
