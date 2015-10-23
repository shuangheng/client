package com.app.demos.util;

/**
 * 数学运算类
 * <p>Created by tom on 15-8-28.
 */
public class Math_my {
    /**
     * 用取余运算
     */
    public static boolean isEven(int num) {

        return (num % 2 == 0);

    }

    /**
     * 产生0和 max 之间的整数
     */
    public static int random(int max) {
        int i = (int) Math.round(Math.random() * max);//Math.round()将值舍入到最接近的整数或指定的小数位数
        return i;
    }

    /**
     * 产生 min 和 max 之间的整数
     */
    public static int random(int min, int max) {
        int i = (int) Math.round(Math.random() * (max - min) + min);//Math.round()将值舍入到最接近的整数或指定的小数位数
        return i;

        /*
        Random ra =new Random();
        ra.netInt(max - min + 1) + min;
        */

    }
}
