package com.app.demos.util;

import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *Int型时间戳, 字符串时间   转换
 *<p>java时间戳精确到毫秒，腾讯微博返回时间戳为秒，需注意。
 *<p>Created by tom on 15-4-24.
 */
public class TimeUtil {

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     * @param timeStr   时间戳(精确到毫秒)
     * @return String "距现在多久之前"的字符串
     */
    public static String getStandardDate(String timeStr) {

        StringBuffer sb = new StringBuffer();

        long t = int2String(timeStr);
        //Log.e("timeStr", timeStr);
        //Log.e("timeStr", ""+t);
        long time = System.currentTimeMillis() - t;
        long mill = (long) Math.ceil(time /1000);//秒前

        long minute = (long) Math.ceil(time/60/1000.0f);// 分钟前

        long hour = (long) Math.ceil(time/60/60/1000.0f);// 小时

        float day = (time/24/60/60/1000.0f);// 天前

        Log.e("timeStr", timeStr + "\n"+t + "\n"+"day  " +day + "\n"+"hour   "+hour+ "\n"+"m  "+minute
                + "\n"+"s  "+mill);

        if (day - 1 > 0) {
            if (day < 1) {
                sb.append("今天");
            } else if (day < 2) {
                sb.append("昨天");
            } else if (day < 3 ) {
                sb.append("前天");
            } else {
                sb.append(day + "天");
            }
        } else if (hour - 1 > 0) {
            if (hour >= 24 ) {
                sb.append("今天");
            } else {
                sb.append(hour + "小时");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小时");
            } else {
                sb.append(minute + "分钟");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分钟");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("刚刚");
        }
        if (!sb.toString().equals("刚刚")) {
            sb.append("前");
        }
        return sb.toString();
    }

    /**
     * <p>String 转换成 时间戳
     * @param time 时间戳
     * @return long 到毫秒
     */
    public static long int2String (String time) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        long l = 0;
        try {
            Date d = sdf.parse(time);
            l = d.getTime();//到毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return l;
    }


    public static String getDate(String month,String day){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//24小时制
        java.util.Date d = new java.util.Date(); ;
        String str = sdf.format(d);
        String nowmonth = str.substring(5, 7);
        String nowday = str.substring(8,10 );
        String result = null;

        int temp =   Integer.parseInt(nowday)-Integer.parseInt(day);
        switch (temp) {
            case 0:
                result="今天";
                break;
            case 1:
                result = "昨天";
                break;
            case 2:
                result = "前天";
                break;
            default:
                StringBuilder sb = new StringBuilder();
                sb.append(Integer.parseInt(month)+"月");
                sb.append(Integer.parseInt(day)+"日");
                result  = sb.toString();
                break;
        }
        return result;
    }
    public static String getTime(int timestamp){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time= null;
        try {
            java.util.Date currentdate = new java.util.Date();//当前时间

            long i = (currentdate.getTime()/1000-timestamp)/(60);
            System.out.println(currentdate.getTime());
            System.out.println(i);
            Timestamp now = new Timestamp(System.currentTimeMillis());//获取系统当前时间
            System.out.println("now-->"+now);//返回结果精确到毫秒。

            String str = sdf.format(new Timestamp(IntToLong(timestamp)));
            time = str.substring(11, 16);

            String month = str.substring(5, 7);
            String day = str.substring(8,10 );
            System.out.println(str);
            System.out.println(time);
            System.out.println(getDate(month, day));
            time =getDate(month, day)+ time;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return time;
    }
    //java Timestamp构造函数需传入Long型
    public static long IntToLong(int i){
        long result = (long)i;
        result*=1000;
        return  result;
    }

    /*
    public static void main(String[] args) {
        int timestamp = 1310457552;  // 假设腾讯微博返回时间戳为秒
        String time = TimeUtil.getTime(timestamp);
        System.out.println("timestamp-->"+time);

        //print  timestamp-->7月12日15:59
    }
    */

}
