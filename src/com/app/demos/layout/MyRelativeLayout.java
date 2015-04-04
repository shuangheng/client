package com.app.demos.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.app.demos.base.BaseDevice;

/**
 * Created by Administrator on 15-2-22.
 */
public class MyRelativeLayout extends RelativeLayout {
    private int height;

    public MyRelativeLayout(Context context, AttributeSet attrs){
        super(context,attrs);
       // this.setHeight(BaseDevice.getScreenWidth(context));
    }
}
