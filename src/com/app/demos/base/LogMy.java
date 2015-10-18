/***
 This is free and unencumbered software released into the public domain.

 Anyone is free to copy, modify, publish, use, compile, sell, or
 distribute this software, either in source code form or as a compiled
 binary, for any purpose, commercial or non-commercial, and by any
 means.

 For more information, please refer to <http://unlicense.org/>
 */

package com.app.demos.base;

import android.content.Context;
import android.util.Log;



/**
 * @date 21.06.2012
 * @author Mustafa Ferhan Akman
 *
 * Create a simple and more understandable Android logs. 
 * */

public class LogMy {

    static String className;
    static String methodName;
    static int lineNumber;

    private LogMy(){
        /* Protect from instantiations */
    }

    public static boolean isDebuggable() {
        //return BuildConfig.DEBUG;
        return true;
    }

    private static String createLog( String log ) {

        StringBuffer buffer = new StringBuffer();
        buffer.append(log);
        buffer.append("--->>>DebugLog");
        buffer.append("[");
        buffer.append(methodName);
        buffer.append(":");
        buffer.append(lineNumber);
        buffer.append("]");


        return buffer.toString();
    }

    private static void getMethodNames(Context context, StackTraceElement[] sElements){
        //className = sElements[1].getFileName();
        className = context.getClass().getSimpleName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static void e(Context context, String message){
        if (!isDebuggable())
            return;

        // Throwable instance must be created before any methods
        getMethodNames(context, new Throwable().getStackTrace());
        Log.e(className, createLog(message));
    }

    public static void i(Context context, String message){
        if (!isDebuggable())
            return;

        getMethodNames(context, new Throwable().getStackTrace());
        Log.i(className, createLog(message));
    }

    public static void d(Context context, String message){
        if (!isDebuggable())
            return;

        getMethodNames(context, new Throwable().getStackTrace());
        Log.d(className, createLog(message));
    }

    public static void v(Context context, String message){
        if (!isDebuggable())
            return;

        getMethodNames(context, new Throwable().getStackTrace());
        Log.v(className, createLog(message));
    }

    public static void w(Context context, String message){
        if (!isDebuggable())
            return;

        getMethodNames(context, new Throwable().getStackTrace());
        Log.w(className, createLog(message));
    }

    public static void wtf(Context context, String message){
        if (!isDebuggable())
            return;

        getMethodNames(context, new Throwable().getStackTrace());
        Log.wtf(className, createLog(message));
    }

}
