package com.app.demos.ui.test.dragelayoutDown;


import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

public class MainContentLayout extends RelativeLayout {

	private DragDownLayout mDragLayout;

	public MainContentLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MainContentLayout(Context context) {
		super(context);
	}

	public void setDragLayout(DragDownLayout mDragLayout) {
		this.mDragLayout = mDragLayout;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(mDragLayout.getStatus() == DragDownLayout.Status.Close){
			return super.onInterceptTouchEvent(ev);
		}else {
			return true;
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(mDragLayout.getStatus() == DragDownLayout.Status.Close){
			return super.onTouchEvent(event);
		}else {
			if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_UP){
				mDragLayout.close();
			}
			return true;
		}
		
	}

}
