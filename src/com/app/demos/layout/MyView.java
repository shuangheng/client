package com.app.demos.layout;

import com.app.demos.util.BaseDevice;
import com.app.demos.util.AppCache;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class MyView extends View{
	
	public Bitmap b = AppCache.getCachedImage(getContext(),"http://10.0.2.2:8002/faces/default/s_25.jpg");

	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("DrawAllocation")
	@Override
	public void onDraw(Canvas canvas){
		int w = BaseDevice.getScreenWidth(getContext());
		canvas.drawBitmap(b, null, new Rect(0, 0, w, w), null);	 
	}
}
