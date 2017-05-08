package com.example.mobilesafe74.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by yueyue on 2017/1/17.
 */

public class MD5Utils {

    /**
     * 给制定的字符串按照MD5算法加密
     *
     * @param pwd 表示需要加密的字符串
     */
    public static String encoder(String pwd) {
        // TODO Auto-generated method stub

        try {
            // 1,制定使用的算法类型
            MessageDigest digest = MessageDigest.getInstance("MD5");
            // 2,将需要加密的字符串中转换成byte类型,然后进行随机Hash过程
            byte[] bs = digest.digest(pwd.getBytes());
            // 3,拼接字符串过程
            StringBuffer sb = new StringBuffer();
            // 4,循环遍历bs,然后让其生成32位字符串,固定写法
            for (byte b : bs) {
                int i = b & 0xff;
                String string = Integer.toHexString(i);
                //判断16为的数字是否有2位,没有2为的补0补到2位
                if (string.length() < 2) {
                    string = 0 + string;
                }
                // 将转为32位的字符串不断拼接到Stringbuffer对象中
                sb.append(string);
                return sb.toString();
            }
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
