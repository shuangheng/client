package com.app.demos.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.app.demos.base.BaseApp;

/**
 * 系统键盘-class
 * Created by tom on 15-10-16.
 */
public class ImmUtil {
    public static void showInput(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //隐藏软键盘 //
        //imm.hideSoftInputFromWindow(view.getWindowToken(), 0);//强制隐藏键盘
        // 显示软键盘 //
         imm.showSoftInputFromInputMethod(view.getWindowToken(), 0);
        // 切换软键盘的显示与隐藏
        //imm.toggleSoftInputFromWindow(view.getWindowToken(), 0, InputMethodManager.HIDE_NOT_ALWAYS);
        //或者
        //                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        //关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的

    }

    public static void showInput2(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }
    //(如果输入法在窗口上已经显示，则隐藏，反之则显示)
    //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    //（view为接受软键盘输入的视图，SHOW_FORCED表示强制显示）
    //InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    //imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);


    //            4、获取输入法打开的状态
    //    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开



}
