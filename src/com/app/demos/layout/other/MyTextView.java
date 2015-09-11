package com.app.demos.layout.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.app.demos.util.BaseDevice;

/**
 * Created by Administrator on 15-2-16.
 */
public class MyTextView extends TextView {

    private int height;

    public MyTextView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.setHeight(BaseDevice.getScreenWidth(context));
    }
}
