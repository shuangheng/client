package com.app.demos.ui.test;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.BaseUiAuth;
import com.app.demos.base.C;
import com.app.demos.list.MyList;
import com.app.demos.model.Blogg;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.BlogSqlite;
import com.app.demos.ui.MainActivity;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class UiIndex extends BaseUiAuth {

	private ListView blogListView;
	private MyList blogListAdapter;
	private BlogSqlite blogSqlite;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_index);
		
		// set handler
		this.setHandler(new IndexHandler(this));
		
		/*/ tab button
		ImageButton ib = (ImageButton) this.findViewById(R.id.main_tab_1);
		ib.setImageResource(R.drawable.tab_blog_2);
		*/
		ImageButton right = (ImageButton) this.findViewById(R.id.my_top_right);
		right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast.makeText(getContext(), "Hello", Toast.LENGTH_SHORT).show();
				overlay(MainActivity.class);
				//overlay(UiBlogs.class);
			}
		});
		
	////add 快捷键
			ImageButton add = (ImageButton) this.findViewById(R.id.add_ibtn);
			add.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					overlay(TestImage.class);
					Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
				}
			});
		
		// init sqlite
		blogSqlite = new BlogSqlite(this);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		//记录系统时间
		/*
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
            	long loginTime = System.currentTimeMillis();
        		
        		SharedPreferences sharedPreferences = getSharedPreferences("TEST", MODE_PRIVATE);
        		String lastTime = sharedPreferences.getString("time","0");
        		long time0 = Long.valueOf(lastTime);
        		long jgtime = loginTime - time0;
        		
        		Log.w("LoginTime", ""+loginTime);        		
        		Log.w("Time0", ""+time0);        		
        		Log.w("jgtime", ""+jgtime);
        		if (jgtime>30000){
        			//从网路获取列表
        			loadLocalData();
        			getBlog();
        		}else{
        			//获取本地列表
        			loadLocalData();
        		}
        		String nowTime = Long.toString(loginTime);
        		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器   
        		editor.putString("time", nowTime);  
        		//editor.putInt("sex", (int) loginTime);  
        		editor.commit();//提交修改  
        	}  
        }, 100); 
		*/
		getData();
	}
	
	//从网络获取数据
	public void getBlog(){
		// show all blog list
		HashMap<String, String> blogParams = new HashMap<String, String>();
		blogParams.put("typeId", "0");
		blogParams.put("pageId", "0");
		this.doTaskAsync(C.task.blogList, C.api.blogList, blogParams, true);
	}
	
	public void getData(){
		// show all  list
	HashMap<String, String> blogParams = new HashMap<String, String>();		
	blogParams.put("typeId", "0");
	blogParams.put("pageId", "0");
	this.doTaskAsync(C.task.gg, C.api.gg, blogParams, true);
				
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods
	
	@Override
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);

		switch (taskId) {
			case C.task.gg:
				try {
					@SuppressWarnings("unchecked")
					final ArrayList<Gonggao> blogList = (ArrayList<Gonggao>) message.getResultList("Gonggao");
					// load face image
					for (Gonggao blog : blogList) {
						loadImage(blog.getFace());
						//blogSqlite.updateBlog(blog);
					}
					// show text
					blogListView = (ListView) this.findViewById(R.id.app_index_list_view);
					blogListAdapter = new MyList(this,R.layout.tpl_list_speak, blogList);
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
				break;
		}
	}
	
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
		toast(C.err.network);
		switch (taskId) {
			case C.task.blogList:
				loadLocalData();
				break;
		}
	}
	
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
//			blogListAdapter = new BlogList(this,R.layout.tpl_list_blog, blogList);
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
}