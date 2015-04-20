package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.demos.R;
import com.app.demos.adapter.MyFragmentPagerAdapter;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.ui.fragment.Fragment2;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.MainFragment;
import com.app.demos.list.MyList;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.GonggaoSqlite;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;

/**
 * 鍙傝�鍘熶綔鑰匘.Winter鍩虹锛� * 
 * @author avenwu
 * iamavenwu@gmail.com
 * 
 */
public class MainActivity extends BaseUi implements OnGestureListener {
    private static final String TAG = "MainActivity";
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private TextView tvTabActivity, tvTabGroups, tvTabFriends, tvTabChat;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 1000;
    private int position_one;
    private int position_two;
    private int position_three;
    private Resources resources;
    private MainFragment activityfragment;
    ////////////////////////////////////
    private ListView list;
	private MyList blogListAdapter;
	private GonggaoSqlite gonggaoSqlite;
	private ArrayList<Gonggao> ggList;
	private String lastId;
	public int lastIdNum;
	private String Maxid;
	public int MaxIdNum;
	private SharedPreferences sharedPreferences;
	private String lastTime;
	private String cacheMaxId;
    private PopupWindow popupwindow;
    private View pupView;
    //下拉刷新Layout
    private SwipeRefreshLayout swipeLayout;

    // ListView底部View
    private View moreView;
    private Button bt;
    private ProgressBar pg;
    public Handler handler;
    // 设置一个最大的数据条数，超过即不再加载
    
    // 最后可见条目的索引
    private int lastVisibleIndex;
    private int NUM ;
    private GestureDetector mygesture;
    private ImageButton add;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        ///////////////////////////////////////////
        this.setHandler(new MyHandler(this));

        //构建手势探测器
         mygesture = new GestureDetector(this, this);

		//add 快捷键
			 add = (ImageButton) this.findViewById(R.id.add_ibtn);
			add.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v){
					Toast.makeText(getContext(), "add", Toast.LENGTH_SHORT).show();
				}
			});		

        resources = getResources();
        InitWidth();
        InitTextView();
        InitViewPager();

        gonggaoSqlite = new GonggaoSqlite(this);
    }

    private void InitTextView() {
        tvTabActivity = (TextView) findViewById(R.id.tv_tab_activity);
        tvTabGroups = (TextView) findViewById(R.id.tv_tab_groups);
        tvTabFriends = (TextView) findViewById(R.id.tv_tab_friends);
        tvTabChat = (TextView) findViewById(R.id.tv_tab_chat);

        tvTabActivity.setOnClickListener(new MyOnClickListener(0));
        tvTabGroups.setOnClickListener(new MyOnClickListener(1));
        tvTabFriends.setOnClickListener(new MyOnClickListener(2));
        tvTabChat.setOnClickListener(new MyOnClickListener(3));
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        fragmentsList = new ArrayList<Fragment>();
        LayoutInflater mInflater = getLayoutInflater();
        View activityView = mInflater.inflate(R.layout.fragment_list_speak, null);

        activityfragment = MainFragment.newInstance("Hello Activity.");
        Fragment groupFragment = new Fragment2();
        Fragment friendsFragment= new Fragment3();
        Fragment chatFragment=MainFragment.newInstance("Hello Chat.");

        fragmentsList.add(activityfragment);
        fragmentsList.add(groupFragment);
        fragmentsList.add(friendsFragment);
        fragmentsList.add(chatFragment);
        
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
        mPager.setOffscreenPageLimit(2);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        Log.e("down","down");
                        //tvTabActivity.setText("down");
                    case MotionEvent.ACTION_MOVE:
                        Log.e("move",""+event.getX());
                        //tvTabGroups.setText(""+event.getX());
                        //ivBottomLine.setTranslationX(event.getX() / 4);
                        //add.setScaleX((720-event.getX())/(position_one*4));
                        //add.setScaleY((720-event.getX() )/ (position_one * 4));
                       // add.setRotation(360 * (event.getX() / (position_one * 4)));
                    case MotionEvent.ACTION_SCROLL:
                        Log.e("scroll",""+event.getX());
                        //tvTabFriends.setText(""+event.getX());
                    case MotionEvent.ACTION_UP:
                        Log.e("up",""+event.getX());
                        //tvTabChat.setText(""+event.getX());
                }
                return false;
            }
        });


    }

    private void InitWidth() {
        ivBottomLine = (ImageView) findViewById(R.id.iv_bottom_line);
        bottomLineWidth = ivBottomLine.getLayoutParams().width;
        Log.d(TAG, "cursor imageview width=" + bottomLineWidth);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
//        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
//        Log.i("MainActivity", "offset=" + offset);

        position_one = (int) (screenW / 4.0);
        position_two = position_one * 2;
        position_three = position_one * 3;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        //tvTabFriends.setText("down");
        tvTabActivity.setText(""+e.getX());
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        tvTabGroups.setText(""+e2.getX());

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        tvTabChat.setText(""+e2.getX());
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mygesture.onTouchEvent(event))
                    return true;
           else
               return false;
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        @Override
        public void onClick(View v) {
            mPager.setCurrentItem(index);
        }
    }


    public class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            /*
           Animation animation = null;
            switch (arg0) {
            case 0:
                ivBottomLine.setX(0);
                if (currIndex == 1) {
                    ivBottomLine.setX(0);
                    animation = new TranslateAnimation(position_one, 0, 0, 0);
                    tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    ivBottomLine.setX(0);
                    animation = new TranslateAnimation(position_two, 0, 0, 0);
                    tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, 0, 0, 0);
                    tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabActivity.setTextColor(resources.getColor(R.color.white));
                break;
            case 1:
                ivBottomLine.setX(position_one);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, position_one, 0, 0);
                    tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_one, 0, 0);
                    tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_one, 0, 0);
                    tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabGroups.setTextColor(resources.getColor(R.color.white));
                break;
            case 2:
                ivBottomLine.setX(position_two);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, position_two, 0, 0);
                    tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_two, 0, 0);
                    tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 3) {
                    animation = new TranslateAnimation(position_three, position_two, 0, 0);
                    tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabFriends.setTextColor(resources.getColor(R.color.white));
                break;
            case 3:
                ivBottomLine.setX(position_three);
                if (currIndex == 0) {
                    animation = new TranslateAnimation(0, position_three, 0, 0);
                    tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(position_one, position_three, 0, 0);
                    tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(position_two, position_three, 0, 0);
                    tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                }
                tvTabChat.setTextColor(resources.getColor(R.color.white));
                break;
            }
            currIndex = arg0;
           // animation.setFillAfter(true);
            //animation.setDuration(100);
            //ivBottomLine.startAnimation(animation);
*/
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
               tvTabActivity.setText("aeg0"+"\n"+arg0);
            tvTabGroups.setText("arg1"+arg1);
            tvTabFriends.setText(""+arg2);
            //add.setY(arg2);
            Float f = currIndex*position_one+arg1*200;
            //ivBottomLine.setX(f);
            //ViewHelper.setTranslationX(ivBottomLine, arg2/(position_one*4)*position_one);
            //ivBottomLine.invalidate();

            Log.e("arg1", ""+f);

            Animation animation =null;
            switch (arg0) {
                case 0:
                    //ivBottomLine.setX(0);
                    if (currIndex == 1) {
                        //ivBottomLine.setX(0);
                        animation = new TranslateAnimation(position_one, 0, 0, 0);
                        tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 2) {
                        //ivBottomLine.setX(0);
                        animation = new TranslateAnimation(position_two, 0, 0, 0);
                        tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 3) {
                        //ivBottomLine.setX(0);
                        animation = new TranslateAnimation(position_three, 0, 0, 0);
                        tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                    }
                    tvTabActivity.setTextColor(resources.getColor(R.color.white));
                    break;
                case 1:
                    //ivBottomLine.setX(position_one);
                    if (currIndex == 0) {
                        //ivBottomLine.setX(position_one);
                        animation = new TranslateAnimation(0, position_one, 0, 0);
                        tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 2) {
                        //ivBottomLine.setX(position_one);
                        animation = new TranslateAnimation(position_two, position_one, 0, 0);
                        tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 3) {
                        //ivBottomLine.setX(position_one);
                        animation = new TranslateAnimation(position_three, position_one, 0, 0);
                        tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                    }
                    tvTabGroups.setTextColor(resources.getColor(R.color.white));
                    break;
                case 2:
                   //ivBottomLine.setX(position_two);
                    if (currIndex == 0) {
                       /// ivBottomLine.setX(position_two);
                        animation = new TranslateAnimation(0, position_two, 0, 0);
                        tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 1) {
                        //ivBottomLine.setX(position_two);
                        animation = new TranslateAnimation(position_one, position_two, 0, 0);
                        tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 3) {
                        //ivBottomLine.setX(position_two);
                        animation = new TranslateAnimation(position_three, position_two, 0, 0);
                        tvTabChat.setTextColor(resources.getColor(R.color.lightwhite));
                    }
                    tvTabFriends.setTextColor(resources.getColor(R.color.white));
                    break;
                case 3:


                    if (currIndex == 0) {
                        //ivBottomLine.setX(position_three);
                        animation = new TranslateAnimation(0, position_three, 0, 0);
                        tvTabActivity.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 1) {
                        //ivBottomLine.setX(position_three);
                        animation = new TranslateAnimation(position_one, position_three, 0, 0);
                        tvTabGroups.setTextColor(resources.getColor(R.color.lightwhite));
                    } else if (currIndex == 2) {
                        //ivBottomLine.setX(position_three);
                        animation = new TranslateAnimation(position_two, position_three, 0, 0);
                        tvTabFriends.setTextColor(resources.getColor(R.color.lightwhite));
                    }
                    tvTabChat.setTextColor(resources.getColor(R.color.white));
                    break;
            }
            currIndex = arg0;
            if (animation != null) {
                animation.setFillAfter(true);

                animation.setDuration(100);
                ivBottomLine.startAnimation(animation);
            }
            add.setRotationY(arg1*180);
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            switch (arg0){
                case 1:
                tvTabChat.setText(""+currIndex+"\n"+currIndex*position_one);
                ivBottomLine.setX(currIndex*position_one);
                    break;
                case 2:
                    tvTabChat.setText("two"+"\n"+currIndex+"\n"+mPager.getCurrentItem());
                    add.setScaleX(1);
                    add.setScaleY(1);
                    //add.setRotation(180);
                    break;
                case 0:
                    tvTabChat.setText("00 : "+currIndex+"\n"+mPager.getCurrentItem());
                    add.setScaleX(1);
                    add.setScaleY(1);
                    //add.setRotation(180);
            }
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
			blogParams.put("Id", lastId);
			blogParams.put("typeId", "0");
			blogParams.put("pageId", "0");
			this.doTaskAsync(C.task.gg1, C.api.gg, blogParams);
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
	
		@Override
		@SuppressWarnings("unchecked")
		public void onTaskComplete(int taskId, BaseMessage message) {
			super.onTaskComplete(taskId, message);
			//bt.setText("更多");
			switch (taskId) {
				case C.task.gg:
					try {	//all Data				
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
					    getLastId(ggList1);
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
					try {	//剩余Data
						ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");
                        for (Gonggao g : ggList1) {
                            loadImage(g.getBgimage());
                            gonggaoSqlite.updateGonggao(g);
                        }
						getLastId(ggList1);
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