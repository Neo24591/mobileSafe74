package com.example.mobilesafe74.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 数据流转为字符串
 * 返回null代表异常
 */
public class StreamUtils {
    public static String streamToString(InputStream in) throws IOException {

        //定义一个内存输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024]; //1kb
        while ((len = in.read(buffer)) != -1) {

            baos.write(buffer, 0, len);
        }
        if (in != null) {
            in.close();
        }

        String content = new String(baos.toByteArray());

        return content;

    }
}
