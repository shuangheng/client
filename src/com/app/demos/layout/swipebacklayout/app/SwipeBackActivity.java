
package com.app.demos.layout.swipebacklayout.app;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.app.demos.base.BaseUi;
import com.app.demos.layout.swipebacklayout.SwipeBackLayout;
import com.app.demos.layout.swipebacklayout.Utils;

public class SwipeBackActivity extends BaseUi implements SwipeBackActivityBase, View.OnTouchListener {
    private SwipeBackActivityHelper mHelper;
    private GestureDetector gestureDetector;
    private SwipeBackLayout swipeBackLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        gestureDetector = new GestureDetector(this, onGestureListener);
        swipeBackLayout = getSwipeBackLayout();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.OnGestureListener() {
                @Override
                public boolean onDown(MotionEvent e) {
                    return false;
                }

                @Override
                public void onShowPress(MotionEvent e) {

                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return false;
                }

                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();
                    Boolean isEnable = false;
                    if (x > 0 && (Math.abs(x) > Math.abs(y))) {
                        //h = "手指向R滑";
                        isEnable =true;
                    } else if (x < 0 && (Math.abs(x) > Math.abs(y))) {
                        //h = "手指向左滑";
                    }
                    swipeBackLayout.setEnableGesture(isEnable);
                    return false;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    return false;
                }

            };

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucentMy(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

}
