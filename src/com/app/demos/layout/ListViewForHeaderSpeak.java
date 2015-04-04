package com.app.demos.layout;

import com.app.demos.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class ListViewForHeaderSpeak extends ListView{

	public TextView tvContent;
	public TextView tvType;
	public TextView tvLikeCount;
	public ImageView ivBgImage;
	public LinearLayout layout;

	public ListViewForHeaderSpeak(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater.from(context).inflate(R.layout.tpl_list_speak, this);
		tvContent = (TextView) findViewById(R.id.tpl_list_speak_tv_content);
		tvType = (TextView) findViewById(R.id.tpl_list_speak_tv_type);
		tvLikeCount = (TextView) findViewById(R.id.tpl_list_speak_tv_like);
		ivBgImage = (ImageView) findViewById(R.id.tpl_list_speak_iv_bg);
		layout = (LinearLayout) findViewById(R.id.tpl_list_speak_bottom_layout);
	}

}
