package com.app.demos.layout.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.view.View.MeasureSpec; 

public class ListViewForScrollView extends ListView {
    public ListViewForScrollView(Context context) { 
        super(context); 
        // TODO Auto-generated constructor stub 
    } 
    public ListViewForScrollView(Context context, AttributeSet attrs) { 
        super(context, attrs); 
        // TODO Auto-generated constructor stub 
    } 
    public ListViewForScrollView(Context context, AttributeSet attrs, int defStyle) { 
        super(context, attrs, defStyle); 
        // TODO Auto-generated constructor stub 
    } 
    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) { 
        // TODO Auto-generated method stub 
        int expandSpec = MeasureSpec.makeMeasureSpec(  
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
         
        super.onMeasure(widthMeasureSpec, expandSpec); 
    } 
}