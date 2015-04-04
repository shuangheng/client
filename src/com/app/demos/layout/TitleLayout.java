package com.app.demos.layout;

import com.app.demos.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;



public class TitleLayout extends LinearLayout {
	
	//private final String
	//private final String INDEX = "ui.UiIndex";

	@SuppressWarnings("null")
	public TitleLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.my_top, this);
		ImageButton back = (ImageButton) findViewById(R.id.my_top_back);
		back.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				((Activity) getContext()).finish();
				
			}
		});
		
		String str = (String) ((Activity) getContext()).getLocalClassName();
		TextView tv = (TextView) findViewById(R.id.my_top_text) ;
		switch (str){
		case "ui.UiIndex":
			tv.setText("广场");
			break;
		case "ui.UiGongGao":
			tv.setText("公告");
			break;
			
		
		}		
		//getClass().getSimpleName()
	}

}
