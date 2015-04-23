package com.app.demos.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.app.demos.base.BaseUi;

/**
 * Created by Administrator on 15-2-15.
 */
public class BaseDevice {
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }
    //获取屏幕的高度
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }


    public static int getScreenWidth0() {
        DisplayMetrics dm = new DisplayMetrics();
        new BaseUi().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
        return screenW;
    }


}
