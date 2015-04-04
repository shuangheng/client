package com.app.demos.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.app.demos.R;
import com.app.demos.list.FindList;
import com.app.demos.model.Find;
import com.app.demos.ui.UiActionBar;

public class FindFragment extends Fragment implements OnScrollListener, OnRefreshListener{
	 	private static final String TAG = "FindFragment";	    
	    private UiActionBar activity;
	    //---View---
	    private ListView listView;
	    private ImageButton ib;
		public FindList findListAdapter;	
		private SharedPreferences sharedPreferences;	
	    private PopupWindow popupwindow;
	    private View pupView;
	    //下拉刷新Layout
	    private SwipeRefreshLayout swipeLayout;
	    
	    // ListView底部View
	    private View moreView;
	    private TextView bt;    
	    private ProgressBar pg;    
	    
	    
	    // 设置一个最大的数据条数，超过即不再加载
	    
	    // 最后可见条目的索引
	    private int lastVisibleIndex;
	    private int NUM ;
	    private int lastIdNum;	
		private int MaxIdNum;
		
	    private String lastTime;
		private String cacheMaxId;
		private String Maxid;
		private String lastId;
		
		private ArrayList<Find> findList;
		
		private Handler handler;

	    public static SpeakFragment newInstance(String s) {
	        SpeakFragment newFragment = new SpeakFragment();
	        Bundle bundle = new Bundle();
	        bundle.putString("hello", s);
	        newFragment.setArguments(bundle);
	        return newFragment;

	    }
	    
	    @Override
	    public void onAttach(Activity activity){
	    	super.onAttach(activity);
	    	this.activity = (UiActionBar) activity;
	    }

	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        Log.d(TAG, "-----onCreate");
	        Bundle args = getArguments();
	        //hello = args != null ? args.getString("hello") : defaultHello;
	    }

	    @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
	        Log.d(TAG, "-----onCreateView");
	        View view = inflater.inflate(R.layout.fragment_list_speak, container, false);
//	        TextView viewhello = (TextView) view.findViewById(R.id.tv_hello);
//	        viewhello.setText(hello);
	       // viewhello.setVisibility();
	    ////////////////////////////////////
	        listView = (ListView) view.findViewById(R.id.ui_gongga_list_view);       
	        
	        // 实例化load more data 底部布局
		    moreView = activity.getLayoutInflater().inflate(R.layout.load_more, null);				    			
		    //bt = (TextView) moreView.findViewById(R.id.bt_load);
		    //pg = (ProgressBar) moreView.findViewById(R.id.pg);
		    handler = new Handler();				    
			listView.addFooterView(moreView);
			listView.setOnScrollListener(this);
			//必须在addFootView()之前用
			setMyAdapter();
			//list.setAdapter(blogListAdapter);
			/*/ 绑定监听器
	        list.setOnScrollListener(this);
			bt.setOnClickListener(new OnClickListener() {

	            @Override
	            public void onClick(View v) {
	                pg.setVisibility(View.VISIBLE);// 将进度条可见
	                bt.setVisibility(View.GONE);// 不可见
	                handler.postDelayed(new Runnable() {
	                    @Override
	                    public void run() {
	                        activity.loadMoreData();// 加载更多数据                              
	                        bt.setVisibility(View.VISIBLE);//按钮可见
	                        pg.setVisibility(View.GONE);
	                       // blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
	                        
	                    }

						
	                }, 2000);
	            }
	        });
			*/
			//下拉更新Layout
			 swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
			    swipeLayout.setOnRefreshListener(this);			
	        
	        return view;

	    }
	    
	    @Override
	    public void onStart() {
	    	super.onStart();
	    	new Handler().post(new Runnable() {  
	            public void run() {  
	            	long loginTime = System.currentTimeMillis();
	        		
	        		SharedPreferences sharedPreferences = activity.getSharedPreferences("fragment1", 0);
	        		String lastTime = sharedPreferences.getString("time","0");
	        		long time0 = Long.valueOf(lastTime);
	        		long jgtime = loginTime - time0;
	        		
	        		Log.w("LoginTime", ""+loginTime);        		
	        		Log.w("Time0", ""+time0);        		
	        		Log.w("jgtime", ""+jgtime);
	        		if (jgtime>30000){
	        			//从网路获取列表
	                    swipeLayout.setRefreshing(true);
	                    //onRefresh();
	        			
	        			String nowTime = Long.toString(loginTime);
	            		SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器   
	            		editor.putString("time", nowTime);  
	            		//editor.putInt("sex", (int) loginTime);  
	            		editor.commit();//提交修改  
	        		}else{
	        			//获取本地列表
	        			//loadLocalData();
	        		}
	        		
	        	}  
	        });  		
	    	
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        Log.d(TAG, "-----onDestroy");
	    }

	   
		@Override
		public void onStop() {
			super.onStop();
			// debug memory
			//debugMemory("onStop");
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		//设置默认List adapter
			public void setMyAdapter(){
					findList =  new ArrayList<Find>();
					Find g = new Find("10000","2015-01-09");
					
					findList.add(g);
					//getLastId(ggList);
					findListAdapter = new FindList(activity,R.layout.tpl_list_find, findList);
				    listView.setAdapter(findListAdapter);
					listView.setOnItemClickListener(new OnItemClickListener(){
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
							Log.d("ss", "findFragment");
						}
					});
				
			}
							
			//更新ListView数据
			public void setGgList(ArrayList<Find> g){
				findList.clear();
			    findList.addAll(g);
			    findListAdapter.notifyDataSetChanged();// 通知listView刷新数据
			    
			}
			
			//更新ListView数据
			public void addGgList(ArrayList<Find> g){					
				findList.addAll(g);
				findListAdapter.notifyDataSetChanged();// 通知listView刷新数据
			}

	        //通知ListView加载图片
	        public void listChanged() {
	            findListAdapter.notifyDataSetChanged();// 通知listView刷新数据
	        }

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
			}
			
			// 滑到底部后自动加载
			@Override
		    public void onScrollStateChanged(AbsListView view, int scrollState) {		
		        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
		        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
		        		&& lastVisibleIndex == findListAdapter.getCount()) {
		            // 当滑到底部时自动加载
		             //pg.setVisibility(View.VISIBLE);
		             //bt.setVisibility(View.VISIBLE);
		        	moreView.setVisibility(View.VISIBLE);
		             handler.postDelayed(new Runnable() {            
		            	 @Override
		            	 public void run() {
		            		 activity.loadMoreData();
		            		 //bt.setVisibility(View.GONE);
		            		 //pg.setVisibility(View.GONE);  
		            	 }            
		             }, 2000);
		        }
		    }
			
			
			@Override
	    	public void onRefresh() {
	    		// TODO Auto-generated method stub		    		
	    			new Handler().post(new Runnable() {  
	    	            public void run() {  
	    	                swipeLayout.setRefreshing(false);  
	    	               activity.getData();
	    	            }  
	    	        });	    		
	    	}
			
			//likeButton事件
			public void likeButtonClick(){
				SharedPreferences sharedPreferences = activity.getSharedPreferences("fragment1_isLike", 0);
	    		Boolean isLike = sharedPreferences.getBoolean("isLike",false);
	    		if (isLike = false) {
	    			ib.setImageResource(R.drawable.ic_card_liked);
	    			//获取编辑器
	    			SharedPreferences.Editor editor = sharedPreferences.edit();   
	        		editor.putBoolean("isLike", true);        		  
	        		editor.commit();//提交修改  
	    		} else {
	    			ib.setImageResource(R.drawable.ic_card_like_grey);
	    			//获取编辑器
	    			SharedPreferences.Editor editor = sharedPreferences.edit();   
	        		editor.putBoolean("isLike", false);        		  
	        		editor.commit();//提交修改  
	    		}    		
			}
			
			//隐藏load more
			public void hideLoadMore(){
				//moreView.setVisibility(View.GONE);
				listView.removeFooterView(moreView);
	   		    //pg.setVisibility(View.GONE);  
				//list.removeFooterView(moreView);
			}	
}
