package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.adapter.MyFragmentPagerAdapter;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.fragment.FindFragment;
import com.app.demos.fragment.SpeakFragment;
import com.app.demos.list.MyList;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.GonggaoSqlite;
//import com.app.demos.ui.MainActivity.MyOnClickListener;
import com.app.demos.ui.MainActivity.MyOnPageChangeListener;

public class UiSpeakMain extends BaseUi{
    private int lastIdNum;
	private SpeakFragment activityfragment;
	private String lastId;
	private String Maxid;
	private int MaxIdNum;
	private GonggaoSqlite gonggaoSqlite;
	private ArrayList<Gonggao> ggList;
	private String cacheMaxId;
	private SharedPreferences sharedPreferences;
	private MyList blogListAdapter;
	
	private ImageButton ibRight;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_speak_main);
         
        this.setHandler(new MyHandler(this));
        
        activityfragment = (SpeakFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_speak);
      
		///Fragment 转换键/
			ibRight = (ImageButton) this.findViewById(R.id.ui_speak_main_ibRight);
			ibRight.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
					FindFragment findFragment = new FindFragment();
					FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
					transaction.replace(R.id.ui_speak_main_fragmentLayout, findFragment);
					transaction.commit();
				}
			});		
		    
        
        gonggaoSqlite = new GonggaoSqlite(this);
    }    

	
		@Override
		@SuppressWarnings("unchecked")
		public void onTaskComplete(int taskId, BaseMessage message) {
			super.onTaskComplete(taskId, message);
			//bt.setText("更多");
			switch (taskId) {
				//---get/refresh data
				case C.task.gg:
					try {			
						ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");	
						/*/缓存数据
						gonggaoSqlite.delete(null, null);
						for(Gonggao g : ggList){
							gonggaoSqlite.updateGonggao(g);
						}
						*/
                        // load face image
                        for (Gonggao g : ggList1) {
                            loadImage(g.getBgimage());
                            gonggaoSqlite.updateGonggao(g);
                        }
                            //blogSqlite.updateBlog(blog);
                        activityfragment.getLastId(ggList1);
					    getFirstId(ggList1);
					    //ggList.clear();
					    //ggList.addAll(ggList1);
					    //setListAdapter();
					    activityfragment.setGgList(ggList1);
					    //activityfragment.blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
					} catch (Exception e) {
						e.printStackTrace();
						toast(e.getMessage());
					}
					break;
				
				case C.task.gg1:
					try {	//load more Data
						ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");
                        for (Gonggao g : ggList1) {
                            loadImage(g.getBgimage());
                            gonggaoSqlite.updateGonggao(g);
                        }
                        activityfragment.getLastId(ggList1);
						//ggList.addAll(ggList1);
						activityfragment.addGgList(ggList1);
						//blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据				
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						break;	
						
				case C.task.gg2:
					try {	//new Data
						ggList = (ArrayList<Gonggao>) message.getResultList("Gonggao");
                        for (Gonggao g : ggList) {
                            loadImage(g.getBgimage());
                            gonggaoSqlite.updateGonggao(g);
                        }
						 getFirstId(ggList);
						 getLastId(ggList);
						 //setListAdapter();
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
						break;	
			}
		}

		//加载更多数据
		public void loadMoreData() {
			// TODO Auto-generated method stub
			if(lastIdNum == 1){
				activityfragment.hideLoadMore();
				Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
			}else{
			HashMap<String, String> blogParams = new HashMap<String, String>();
			blogParams.put("Id", activityfragment.lastId);
			blogParams.put("typeId", "0");
			blogParams.put("pageId", "0");
			//this.doTaskAsync(C.task.gg1, C.api.gg, blogParams);
			}
		}	
			
		//从网络获取数据
		public void getData(){
				// show all  list
			HashMap<String, String> blogParams = new HashMap<String, String>();		
			blogParams.put("typeId", "0");
			blogParams.put("pageId", "0");
			this.doTaskAsync(C.task.gg, C.api.gg, blogParams);
						
			}
		
		//获取最后一条数据的ID
		public void getLastId(ArrayList<Gonggao> list){
			int i = list.size();
			Gonggao j = list.get(i-1);			
			lastId = j.getId();
			lastIdNum =Integer.parseInt(lastId);
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
   
  //likeButton事件
  		public int likeButtonClick(Context ctx){
  			SharedPreferences sharedPreferences = ctx.getSharedPreferences("fragment1_isLike", 0);
      		Boolean isLike = sharedPreferences.getBoolean("isLike",false);
      		if (isLike == false) {
      			int i = R.drawable.ic_card_liked;
      			//获取编辑器
      			SharedPreferences.Editor editor = sharedPreferences.edit();   
          		editor.putBoolean("isLike", true);          		
          		editor.commit();//提交修改
          		Log.d("isLike", "true");
          		return i;
      		} else {
      			int i = R.drawable.ic_card_like_grey;
      			//获取编辑器
      			SharedPreferences.Editor editor = sharedPreferences.edit();   
          		editor.putBoolean("isLike", false);        		  
          		editor.commit();//提交修改 
          		Log.d("isLike", "false");
          		return i;
      		}    		
  		}

    // inner classes
    private class MyHandler extends BaseHandler {
        public MyHandler(BaseUi ui) {
            super(ui);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case BaseTask.LOAD_IMAGE:
                        activityfragment.listChanged();
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ui.toast(e.getMessage());
            }
        }
    }
}
