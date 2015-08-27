package com.app.demos.util;

import com.app.demos.R;

import java.util.Random;

/**
 * Created by Administrator on 15-2-21.
 */
public class Suiji {

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
