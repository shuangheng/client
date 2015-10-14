package com.app.demos.ui.test.dragelayoutDown;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by tom on 15-10-12.
 */
public class ListViewDrag extends ListView {

    private DragDownLayout mDragDownLayout;

    public ListViewDrag(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewDrag(Context context) {
        super(context);
    }

    public void setDragLayout(DragDownLayout mDragDownLayout) {
        this.mDragDownLayout = mDragDownLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mDragDownLayout.getStatus() == DragDownLayout.Status.Close){
            return super.onInterceptTouchEvent(ev);
        }else {
            return true;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mDragDownLayout.getStatus() == DragDownLayout.Status.Close){
            if (getFirstVisiblePosition() == 0 && getChildAt(0).getTop()==0) {
                return super.onInterceptTouchEvent(event);
            }
            return super.onTouchEvent(event);
        }else {
            if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP){
                mDragDownLayout.close();
            }
            return true;
        }
    }

}
