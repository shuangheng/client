package com.app.demos.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 15-3-28.
 */
public abstract class TabRedDian extends RelativeLayout {
    public TextView tv;
    public ImageView iv;
    Boolean b;
    public TabRedDian(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.red_dian, this);
        tv = (TextView) findViewById(R.id.red_dian_tv);
        iv = (ImageView) findViewById(R.id.red_dian_iv);
        setTv();
        setShowRedEnable();
    }



    public abstract String setText();
    public abstract Boolean showRed();

    public void setTv(){
        tv.setText(setText());
    }

    public void setShowRedEnable() {
        if (showRed()){
            iv.setVisibility(View.VISIBLE);
        }else {
            iv.setVisibility(View.GONE);
        }
    }
}
