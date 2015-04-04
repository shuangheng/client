package com.app.demos.list;

import java.util.ArrayList;
import com.app.demos.R;
import com.app.demos.base.BaseUi;
import com.app.demos.base.BaseList;
import com.app.demos.model.Blogg;
import com.app.demos.model.Gonggao;
import com.app.demos.model.News;
import com.app.demos.util.AppCache;
import com.app.demos.util.AppFilter;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsList extends BaseList {

	private BaseUi ui;
	private LayoutInflater inflater;
	private ArrayList<News> newsList;
	private int resourceId;
	
	public final class BlogListItem {
		public ImageView face;
		//public TextView title;
		public TextView content;
		public TextView who;
		public TextView uptime;
		//public TextView seecount;
	}
	
	public NewsList (BaseUi ui,int resourceId, ArrayList<News> newsList) {
		this.ui = ui;
		this.resourceId =resourceId;
		this.inflater = LayoutInflater.from(this.ui);
		this.newsList = newsList;
	}
	
	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int p, View v, ViewGroup parent) {
		// init tpl
		BlogListItem  blogItem = null;
		// if cached expired
		if (v == null) {
			v = inflater.inflate(resourceId, null);
			blogItem = new BlogListItem();
			blogItem.face = (ImageView) v.findViewById(R.id.tpl_list_blog_image_face);
			blogItem.who = (TextView) v.findViewById(R.id.tpl_list_blog_text_title);
			//blogItem.user = (TextView) v.findViewById(R.id.tpl_list_blog_text_user);
			blogItem.content = (TextView) v.findViewById(R.id.tpl_list_blog_text_content);
			blogItem.uptime = (TextView) v.findViewById(R.id.tpl_list_blog_text_comment);
			//blogItem.seecount = (TextView) v.findViewById(R.id.tpl_list_blog_text_uptime);
			v.setTag(blogItem);
		} else {
			blogItem = (BlogListItem) v.getTag();
		}
		// fill data
		blogItem.uptime.setText(newsList.get(p).getUptime().substring(0, 10));
		// fill html data
		blogItem.who.setText(newsList.get(p).getWho());
		blogItem.content.setText(AppFilter.getHtml(newsList.get(p).getContent()));
		//blogItem.user.setText(AppFilter.getHtml(newsList.get(p).getUser()));
		//blogItem.seecount.setText(newsList.get(p).getSeecount());
		// load face image
		/*
		String faceUrl = newsList.get(p).getFace();
		Bitmap faceImage = AppCache.getImage(faceUrl);
	
		if (faceImage != null) {
			blogItem.face.setImageBitmap(faceImage);
		}*/
		return v;
	}
}