package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
//import android.widget.AbsListView.OnScrollListener.nSroll;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseModel;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.BaseUiAuth;
import com.app.demos.base.C;
import com.app.demos.list.BlogList;
import com.app.demos.list.ExpandList;
import com.app.demos.list.MyList;
import com.app.demos.model.Blogg;
import com.app.demos.model.Customer;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.BlogSqlite;
import com.app.demos.sqlite.GonggaoSqlite;
import com.app.demos.util.AppUtil;
import com.app.demos.util.UIUtil;

/////////////////////////////////////////////////////////////////////////////////////////////
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class UiGongGao extends BaseUiAuth implements OnScrollListener,OnClickListener, OnRefreshListener, OnTouchListener{
	
	private ListView list;
	private MyList blogListAdapter;
	private GonggaoSqlite gonggaoSqlite;
	private ArrayList<Gonggao> ggList;
	private String lastId;
	private int lastIdNum;
	private String Maxid;
	private int MaxIdNum;
	private SharedPreferences sharedPreferences;
	private String lastTime;
	private String cacheMaxId;
    private PopupWindow popupwindow;
    private View pupView;
    //下拉刷新Layout
    private SwipeRefreshLayout swipeLayout;

    // ListView底部View
    private View moreView;
    private TextView bt;
    private ProgressBar pg;
    private Handler handler;
    // 设置一个最大的数据条数，超过即不再加载
    
    // 最后可见条目的索引
    private int lastVisibleIndex;
    private int NUM ;

     //******************************************///////////////////////////////////////////////////////////////////////
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ui_gonggao);
		
		this.setHandler(new BaseHandler(this));
		
		// show ListView
		list = (ListView) this.findViewById(R.id.ui_gonggao_list_view);		
		
		//下拉更新Layout
		 swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
		    swipeLayout.setOnRefreshListener(this);
		    /*已过时
		    swipeLayout.setColorScheme(android.R.color.holo_blue_bright,   android.R.color.holo_green_light, 
		            android.R.color.holo_orange_light, 
		            android.R.color.holo_red_light);
		*/
		    
		//right top  菜单
		ImageButton right = (ImageButton) this.findViewById(R.id.my_top_right);
		right.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){				 
		            if (popupwindow != null && popupwindow.isShowing()) {  
		                popupwindow.dismiss();  
		                return;  
		            } else {  
		                initmPopupWindowView();  
		                popupwindow.showAsDropDown(v, 0, 10); 		          
		        }  
				Toast.makeText(getContext(), "gonggao", Toast.LENGTH_LONG).show();
			}
		});
		
		//TextView tv = (TextView) this.findViewById(R.id.my_top_text);
		//tv.setText("公告");
		
		////add 快捷键
		ImageButton add = (ImageButton) this.findViewById(R.id.add_ibtn);
		add.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
			}
		});		
			
				
		 // 实例化load more data 底部布局
	    moreView = getLayoutInflater().inflate(R.layout.load_more, null);				    			
	    //bt = (TextView) moreView.findViewById(R.id.bt_load);
	    //pg = (ProgressBar) moreView.findViewById(R.id.pg);
	    handler = new Handler();				    
		list.addFooterView(moreView);
		//list.setAdapter(blogListAdapter);
		// 绑定监听器
        list.setOnScrollListener(this);
		bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                pg.setVisibility(View.VISIBLE);// 将进度条可见
                bt.setVisibility(View.GONE);// 不可见
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();// 加载更多数据                              
                        bt.setVisibility(View.VISIBLE);//按钮可见
                        pg.setVisibility(View.GONE);
                       // blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
                        
                    }

					
                }, 2000);
            }
        });
		gonggaoSqlite = new GonggaoSqlite(this);
		setMyAdapter();
	}
	//***********************************onCreat*********/////////////////////////////////////
	
	//right 下拉菜单 初始化
	@SuppressWarnings("deprecation")
	public void initmPopupWindowView() {  
        pupView = getLayoutInflater().inflate(R.layout.pup_gg_right, null);  //获取自定义布局文件pop.xml的视图
        // 创建PopupWindow实例,200,150分别是宽度和高度  
        popupwindow = new PopupWindow(pupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);  
        popupwindow.setAnimationStyle(R.style.AnimationFade); // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        popupwindow.setBackgroundDrawable(new BitmapDrawable());// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        
     	// 使其聚集  下面3条是为了使 点击popuWindow 以外的区域能够关闭它
        popupwindow.setFocusable(true);  
        popupwindow.setOutsideTouchable(true);// 设置允许在外点击消失
        popupwindow.update();//刷新状态（必须刷新否则无效）
        pupView.setOnTouchListener(this); // 自定义view添加触摸事件
  
        /** 在这里可以实现自定义视图的功能 */  
        Button pup2 = (Button) pupView.findViewById(R.id.gg_right_pup2);  
        Button pup3 = (Button) pupView.findViewById(R.id.gg_right_pup3);  
        Button pup4 = (Button) pupView.findViewById(R.id.gg_right_pup4);  
        pup2.setOnClickListener(this);  
        pup3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
				overlay(MainActivity.class);
			}
		});   
        pup4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				//Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
				overlay(UiNews.class);
			}
		});  
  
    }  
	/*
	@Override  
    public void onClick(View v) { 
		if (popupwindow != null && popupwindow.isShowing())  
            popupwindow.dismiss();  
            popupwindow = null;  

  
        //switch (v.getId()) {  
        //case R.id.button1:  
	}
	*/
	//加载更多数据
	private void loadMoreData() {
		// TODO Auto-generated method stub
		HashMap<String, String> blogParams = new HashMap<String, String>();
		blogParams.put("Id", lastId);
		blogParams.put("typeId", "0");
		blogParams.put("pageId", "0");
		this.doTaskAsync(C.task.gg1, C.api.gg, blogParams, true);
	}	
		
	//从网络获取数据
	public void getData(){
			// show all  list
		HashMap<String, String> blogParams = new HashMap<String, String>();		
		blogParams.put("typeId", "0");
		blogParams.put("pageId", "0");
		this.doTaskAsync(C.task.gg, C.api.gg, blogParams, true);
					
		}	
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
        // 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

        // 所有的条目已经和最大条数相等，则移除底部的View
        if (totalItemCount == MaxIdNum + 1) {
            list.removeFooterView(moreView);
            Toast.makeText(this, "全部加载完成，没有了！", Toast.LENGTH_LONG).show();
        }

    }
	
	 // 滑到底部后自动加载
	@Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == blogListAdapter.getCount()) {
            // 当滑到底部时自动加载
             pg.setVisibility(View.VISIBLE);
             bt.setVisibility(View.GONE);
             handler.postDelayed(new Runnable() {            
            	 @Override
            	 public void run() {
            		 loadMoreData();
            		 bt.setVisibility(View.VISIBLE);
            		 pg.setVisibility(View.GONE);  
            	 }            
             }, 2000);
        }
    }
    
	
	@Override
	public void onStart () {
		super.onStart();		
		getData();				
	}
			
	////////////////////////////////////////////////////////////////////////////////////////////////
	// async task callback methods

	@Override
	@SuppressWarnings("unchecked")
	public void onTaskComplete(int taskId, BaseMessage message) {
		super.onTaskComplete(taskId, message);
		bt.setText("更多");
		switch (taskId) {
			case C.task.gg:
				try {	//all Data				
					ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");	
					//缓存数据
					gonggaoSqlite.delete(null, null);
					for(Gonggao g : ggList){
                        loadImage(g.getBgimage());
						gonggaoSqlite.updateGonggao(g);
					}
				    getLastId(ggList1);
				    getFirstId(ggList1);
				    ggList.clear();
				    ggList.addAll(ggList1);
				    //setListAdapter();
				    blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
				} catch (Exception e) {
					e.printStackTrace();
					toast(e.getMessage());
				}
				break;
			
			case C.task.gg1:
				try {	//剩余Data
					ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");
						
					for(Gonggao g : ggList1){
                        loadImage(g.getBgimage());
						gonggaoSqlite.updateGonggao(g);
					}
					getLastId(ggList1);
					ggList.addAll(ggList1);					 
					blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					break;	
			/*
			case C.task.gg2:
				try {	//new Data
					ggList = (ArrayList<Gonggao>) message.getResultList("Gonggao");	
					for(Gonggao g : ggList){
						gonggaoSqlite.updateGonggao(g);
					}
					 getFirstId(ggList);
					 getLastId(ggList);
					 setListAdapter();
					 cacheMaxId = sharedPreferences.getString("cacheMaxId","0");
					 int i = Integer.parseInt(cacheMaxId)+1;
					 if(lastIdNum == i){
						 ArrayList<Gonggao> sqlList = gonggaoSqlite.getAllGonggao();
						 ggList.addAll(0, sqlList);
						 blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
					 }					 
					 //ggList.addAll(0, ggList);					
					 //blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					break;   */
		}
	}
	public void setListAdapter(){
		if (lastId !=null && ggList!=null){
			blogListAdapter = new MyList(this,R.layout.tpl_list_speak, ggList);
		    list.setAdapter(blogListAdapter);
			list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					Bundle params = new Bundle();
					params.putString("blogId", ggList.get(pos).getId());
					overlay(UiBlog.class, params);
				}
			});
		}
	}
	
	//设置默认List adapter
	public void setMyAdapter(){
			ggList =  new ArrayList<Gonggao>();
			Gonggao g = new Gonggao("10000","","my","","","2015-01-09");
			
			ggList.add(g);
			//getLastId(ggList);
			blogListAdapter = new MyList(this,R.layout.tpl_list_speak, ggList);
		    list.setAdapter(blogListAdapter);
			list.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
					Bundle params = new Bundle();
					params.putString("blogId", ggList.get(pos).getId());
					overlay(UiBlog.class, params);
				}
			});
		
	}
		
	@Override
	public void onNetworkError (int taskId) {
		super.onNetworkError(taskId);
		toast(C.err.network);
		bt.setText("连接网络后，下拉更新");
		switch (taskId) {
			case C.task.gg:
				//loadSqlData();				
				break;
		}
	}
	
	//加载缓存数据
	public void loadSqlData(){
		try {
			Log.w("sql"," ok)");
			final ArrayList<Gonggao> blogList = gonggaoSqlite.getAllGonggao();			
			// show text			
			blogListAdapter = new MyList(this,R.layout.tpl_list_blogs, blogList);
			list.setAdapter(blogListAdapter);
			list.setOnItemClickListener(new OnItemClickListener(){
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
	
	//获取最后一条数据的ID
		public void getLastId(ArrayList<Gonggao> list){
			int i = list.size();
			Gonggao j = list.get(i-1);			
			lastId = j.getId();
			//lastIdNum =Integer.parseInt(lastId);
			Log.e("id",lastId);	
		}
		
	//获取第一条数据的ID
		public void getFirstId(ArrayList<Gonggao> list){
			Gonggao j = list.get(0);		
			Maxid = j.getId();
			MaxIdNum = Integer.parseInt(Maxid);			
			Log.e("maxid",Maxid);
		}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			doFinish();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().post(new Runnable() {  
            public void run() {  
                swipeLayout.setRefreshing(false);  
               getData();
            }  
        });  
		
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
	//更新数据
		private void refreshData() {
			// TODO Auto-generated method stub
			HashMap<String, String> blogParams = new HashMap<String, String>();
			blogParams.put("Maxid", Maxid);
			blogParams.put("typeId", "0");
			blogParams.put("pageId", "0");
			this.doTaskAsync(C.task.gg2, C.api.ggnew, blogParams, true);
		}
		
	//记录系统时间
	public void writeTime(){
		//
		new Handler().postDelayed(new Runnable() {  
            public void run() {  
            	long loginTime = System.currentTimeMillis();        		
            	sharedPreferences = getSharedPreferences("gonggao", MODE_PRIVATE);
        		lastTime = sharedPreferences.getString("time","0");
        		
        		long time0 = Long.valueOf(lastTime);
        		long jgtime = loginTime - time0;
        		
        		Log.w("LoginTime", ""+loginTime);        		
        		Log.w("Time0", ""+time0);        		
        		Log.w("jgtime", ""+jgtime);        		
        		//从网路获取列表
        		        		
        		//记录当前系统时间
        		String nowTime = Long.toString(loginTime);
        		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器   
        		editor.putString("time", nowTime);  
        		//editor.putInt("sex", (int) loginTime);  
        		editor.commit();//提交修改  
        	}  
        }, 200); 				
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (popupwindow != null && popupwindow.isShowing()) {  
            popupwindow.dismiss();  
            popupwindow = null;  
        }  

        return false;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onStop(){
		super.onStop();
		if (popupwindow != null && popupwindow.isShowing()) {
            popupwindow.dismiss();
            popupwindow = null;
        }
	}
}
