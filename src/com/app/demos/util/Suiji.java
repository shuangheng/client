package com.app.demos.util;

import com.app.demos.R;

/**
 * Created by Administrator on 15-2-21.
 */
public class Suiji {
    public static int randomNumber(int start, int end){
        int i = (int) (Math.random()*end + start);
        int number[] = {R.drawable.l_2,R.drawable.l_3,
                R.drawable.l_4,R.drawable.l_5,R.drawable.l_6,R.drawable.l_7,
                R.drawable.l_8,R.drawable.l_9,R.drawable.l_10,R.drawable.l_11,
                R.drawable.l_12,R.drawable.l_13,R.drawable.l_14,R.drawable.l_15,
                R.drawable.l_16,R.drawable.l_17,R.drawable.l_18,R.drawable.l_19,
                R.drawable.l_20,R.drawable.l_21,R.drawable.l_22,R.drawable.l_23,
                R.drawable.l_24,R.drawable.l_25,R.drawable.l_26,R.drawable.l_27,
                R.drawable.l_28,R.drawable.l_29,R.drawable.l_30} ;
        int num = number[i-1];
        return num;
    }
}
