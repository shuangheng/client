package com.app.demos.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by tom on 15-5-7.
 */
public class MoveView  extends LinearLayout {

    private float cx ;      //圆点默认X坐标
    private float cy ;

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            this.setX(cx);
            this.setY(cy);
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        cx = (int) event.getX();
        cy = (int) event.getY();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 按下
                cx = (int) event.getX();
                cy = (int) event.getY();
                // 通知重绘
                invalidate();   //该方法会调用onDraw方法，重新绘图
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动
                cx = (int) event.getX();
                cy = (int) event.getY();
                // 通知重绘
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // 抬起
                cx = (int) event.getX();
                cy = (int) event.getY();
                // 通知重绘
                invalidate();
                break;
        }

        return true;//处理了触摸消息，消息不再传递
    }

}
