package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.BaseUiAuth;
import com.app.demos.base.C;
import com.app.demos.list.BlogList;
import com.app.demos.list.NewsList;
import com.app.demos.model.Blogg;
import com.app.demos.model.News;
import com.app.demos.sqlite.BlogSqlite;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class UiNews extends BaseUiAuth {

	private ListView lv;
	private NewsList listAdapter;
	//private BlogSqlite blogSqlite;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_news);
		
		// set handler
		//this.setHandler(new IndexHandler(this));
		
		// tab button
		ImageButton ib = (ImageButton) this.findViewById(R.id.main_tab_1);
		ib.setImageResource(R.drawable.tab_blog_2);
		
		ImageButton right = (ImageButton) this.findViewById(R.id.my_top_right);
		right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast.makeText(getContext(), "news", Toast.LENGTH_SHORT).show();
				overlay(UiGongGao.class);
				//overlay(UiBlogs.class);
			}
		});
		
		// init sqlite
		//blogSqlite = new BlogSqlite(this);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		getNews();
	}
	
	//从网络获取数据
	public void getNews(){
		// show all blog list
		//HashMap<String, String> blogParams = new HashMap<String, String>();
		//blogParams.put("typeId", "0");
		//blogParams.put("pageId", "0");
		this.doTaskAsync(C.task.newsList, C.api.newsList, true);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);

		switch (taskId) {
			case C.task.newsList:
				try {
					@SuppressWarnings("unchecked")
					final ArrayList<News> blogList = (ArrayList<News>) message.getResultList("News");
					// load face image
					/*for (Blogg blog : blogList) {
						loadImage(blog.getFace());
						blogSqlite.updateBlog(blog);
					}*/
					// show text
					lv = (ListView) this.findViewById(R.id.ui_news_list_view);
					listAdapter = new NewsList(this,R.layout.tpl_list_blog, blogList);
					lv.setAdapter(listAdapter);
					lv.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
							Bundle params = new Bundle();
							params.putString("blogId", blogList.get(pos).getId());
							overlay(UiBlog.class, params);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
				break;
		}
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
		toast(C.err.network);
		switch (taskId) {
			case C.task.blogList:
				//loadLocalData();
				break;
		}
	}
	/*
	//load sqlite data
	public void loadLocalData(){
		try {
			Log.w("sql"," ok)");
			final ArrayList<Blogg> blogList = blogSqlite.getAllBlogs();
			// load face image
			for (Blogg blog : blogList) {
				loadImage(blog.getFace());
				blogSqlite.updateBlog(blog);
			}
			// show text
			blogListView = (ListView) this.findViewById(R.id.app_index_list_view);
			blogListAdapter = new BlogList(this,R.layout.tpl_list_blog, blogList);
			blogListView.setAdapter(blogListAdapter);
			blogListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					Bundle params = new Bundle();
					params.putString("blogId", blogList.get(pos).getId());
					overlay(UiBlog.class, params);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			toast(e.getMessage());
		}
	}
	*/
	////////////////////////////////////////////////////////////////////////////////////////////////
	// other methods
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// inner classes
	/*
	private class IndexHandler extends BaseHandler {
		public IndexHandler(BaseUi ui) {
			super(ui);
		}
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			try {
				switch (msg.what) {
					case BaseTask.LOAD_IMAGE:
						blogListAdapter.notifyDataSetChanged();
						break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				ui.toast(e.getMessage());
			}
		}
	}
	*/
}