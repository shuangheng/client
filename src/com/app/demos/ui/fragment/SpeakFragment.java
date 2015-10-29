package com.app.demos.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.demos.Listener.HideFabScrollListener;
import com.app.demos.R;
import com.app.demos.base.C;
import com.app.demos.layout.swipeRefreshLayout.Progress_m;
import com.app.demos.layout.swipeRefreshLayout.Progress_m.OnRefreshListener;
import com.app.demos.list.RecyclerAdapter.SpeakRecyclerAdapter;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.model.FavoriteSpeak;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.GonggaoSqlite;
import com.app.demos.ui.UiActionBar;
import com.app.demos.ui.UiImageZoom;
import com.app.demos.ui.UiSpeakComment;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import static com.app.demos.util.Math_my.isEven;

public class SpeakFragment extends Fragment implements  OnRefreshListener {
    private static final String TAG = "SpeakFragment";
    private String hello;// = "hello android";
    private String defaultHello = "default value";
    //////////////////////////////////////////////
    private UiActionBar activity;
    
    //---View---
    private ListView list;
    private ImageButton ib;
	//public MyList blogListAdapter;
	private SharedPreferences sharedPreferences;
    private PopupWindow popupwindow;
    private View pupView;
    //下拉刷新Layout
    public Progress_m swipeLayout;
    
    // ListView底部View
    private View moreView;
    private TextView bt;    
    private ProgressBar pg;    
    
    
    // 设置一个最大的数据条数，超过即不再加载
    
    // 最后可见条目的索引
    private int lastVisibleIndex;
    private int NUM ;
    public int lastIdNum;	
	private int MaxIdNum;
	
    private String lastTime;
	private String cacheMaxId;
	private String Maxid;
	public String lastId;
	
	private ArrayList<Gonggao> ggList;
	private GonggaoSqlite gonggaoSqlite;
	private Handler handler;
    private Boolean isLoade_more;
    public RecyclerView recyclerView;
    public SpeakRecyclerAdapter speakRecyclerAdapter;
    private LinearLayoutManager layoutManager;

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
    	//MaxIdNum = new MainActivity().MaxIdNum;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "TestFragment-----onCreate");
        Bundle args = getArguments();
        hello = args != null ? args.getString("hello") : defaultHello;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Log.d(TAG, "TestFragment-----onCreateView");
        View view = inflater.inflate(R.layout.fragment_list_speak_recycler, container, false);
//        TextView viewhello = (TextView) view.findViewById(R.id.tv_hello);
//        viewhello.setText(hello);
       // viewhello.setVisibility();
    ////////////////////////////////////
        // 实例化load more data 底部布局
        moreView = activity.getLayoutInflater().inflate(R.layout.load_more, null);
        bt = (TextView) moreView.findViewById(R.id.load_more_tv);
        pg = (ProgressBar) moreView.findViewById(R.id.load_more_pg);
        ib = (ImageButton) view.findViewById(R.id.tpl_list_speak_ib_like);
        handler = new Handler();
        //list.addFooterView(moreView);
        //list.setOnScrollListener(this);
        //必须在addFootView()之前用
        ggList =  new ArrayList<Gonggao>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        //recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //int paddingTop = Utils.getToolbarHeight(activity) + Utils.getTabsHeight(activity);
        //recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        recyclerView.setHasFixedSize(true);     //使RecyclerView保持固定的大小,这样会提高RecyclerView的性能。
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        speakRecyclerAdapter = new SpeakRecyclerAdapter(activity, ggList);
        speakRecyclerAdapter.setOnRecyclerViewListener(new SpeakRecyclerAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                Gonggao g = ggList.get(position);
                UiSpeakComment.actionStart(activity, g, position);
            }

            @Override
            public boolean onItemLongClick(int position) {
                Toast.makeText(activity, "long", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onImageClick(int position) {
                String imagePath = ggList.get(position).getBgimage();
                String thumburl = C.web.thumb_image + imagePath + ".jpg";//thumb image path
                UiImageZoom.actionStart(activity, C.web.bgimage + ggList.get(position).getBgimage() + ".jpg", thumburl);
                //activity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//动画效果
            }

            @Override
            public void onFavoriteClick(View v, int position) {
                SharedPreferences.Editor editor = activity.sharedPreferences_speak.edit();//获取编辑器
                editor.putBoolean("isLatest_favorite", false);
                editor.commit();//提交修改

                Gonggao g = ggList.get(position);
                if (g.getFavorite() != null && g.getFavorite().equals("0")) {
                    v.setBackgroundResource(!isEven(position) ? R.drawable.ic_card_like : R.drawable.ic_card_like_grey);
                    g.setFavorite("1");
                    g.setLikecount(String.valueOf(Integer.parseInt(g.getLikeCount()) - 1));
                    activity.gonggaoSqlite.updateGonggao(g);
                    activity.favoriteSpeakSqlite.delete(FavoriteSpeak.COL_SPEAKID + "=?", new String[]{g.getId()});
                    activity.getFavoriteSpeakDelete("10", g.getId());//delete on server
                } else {
                    v.setBackgroundResource(R.drawable.ic_card_liked);
                    g.setFavorite("0");
                    g.setLikecount(String.valueOf(Integer.parseInt(g.getLikeCount()) + 1));
                    activity.gonggaoSqlite.updateGonggao(g);

                    ContentValues values = new ContentValues();
                    values.put(FavoriteSpeak.COL_SPEAKID, g.getId());
                    activity.favoriteSpeakSqlite.create(values);
                    activity.getFavoriteSpeakCreate("10", g.getId());
                }
                speakRecyclerAdapter.notifyDataSetChanged();
                Log.e(TAG, "onFavoriteClick");
            }
        });
        recyclerView.setAdapter(speakRecyclerAdapter);
        //recyclerView.setOnScrollListener(new RecyclerView.OnScrollListe
          recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new HideFabFooterScrollListener());



		//下拉更新Layout
		 swipeLayout = (Progress_m) view.findViewById(R.id.speak_swipe_refresh);
        swipeLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
		    swipeLayout.setOnRefreshListener(this);

        initData();


        
        return view;

    }

    private void initData() {
        final ArrayList<Gonggao> ggList = activity.gonggaoSqlite.getAllGonggao();
        if (ggList != null) {
            setGgList(ggList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            public void run() {
                long loginTime = System.currentTimeMillis();

                checkFavoriteSpeak();
                //String lastTime = sharedPreferences.getString("time", "0");
                //long time0 = Long.valueOf(lastTime);
                //long jgtime = loginTime - time0;

                //Log.w("LoginTime", "" + loginTime);
                //Log.w("Time0", "" + time0);
                //Log.w("jgtime", "" + jgtime);
                //if (jgtime > 30000) {
                    //从网路获取列表
                    onRefresh();

                   // String nowTime = Long.toString(loginTime);
                   // SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    //editor.putString("time", nowTime);
                    //editor.putInt("sex", (int) loginTime);
                    //editor.commit();//提交修改
                //} else {
                    //获取本地列表
                    //loadLocalData();
                //}

            }
        });  		
    	
    }

    private void checkFavoriteSpeak() {
        SharedPreferences sharedPreferences = activity.sharedPreferences_speak;
        Boolean isLatest_favorite = sharedPreferences.getBoolean("isLatest_favorite", false);
        if (!isLatest_favorite) {
            activity.getFavoriteSpeakAll(String.valueOf(10));
        }
    }

    @Override
    public void onDestroy() {
        ImageLoader_my imageLoader = speakRecyclerAdapter.getImageLoader();
            if (imageLoader != null){
                imageLoader.clearCache();
            }

            super.onDestroy();

        Log.d(TAG, "TestFragment-----onDestroy");
    }

   
	@Override
	public void onStop() {
		super.onStop();
		// debug memory
		//debugMemory("onStop");
	}
	////////////////////////////////////////////////////////////////////////////////////////////////////////


	

						
		//更新ListView数据
		public void setGgList(ArrayList<Gonggao> g){
			ggList.clear();
		    ggList.addAll(g);
            //int i = recyclerAdapter.getBasicItemCount();
            //recyclerAdapter.notifyItemRangeChanged(0, i);
            speakRecyclerAdapter.notifyDataSetChanged();// 通知listView刷新数据
		    
		}
		
		//更新ListView数据
		public void addGgList(ArrayList<Gonggao> g){
            int i = speakRecyclerAdapter.getBasicItemCount();
            speakRecyclerAdapter.notifyItemInserted(i);//显示动画
			ggList.addAll(g);
            speakRecyclerAdapter.notifyItemChanged(i);

            //recyclerAdapter.notifyDataSetChanged();// 通知listView刷新数据
		}


		
		@Override
    	public void onRefresh() {
    		// TODO Auto-generated method stub		    		
    			new Handler().post(new Runnable() {  
    	            public void run() {
                       // swipeLayout.setRefreshing(false);
                        activity.getGonggaoData();
                        //showLoadMore();
                        speakRecyclerAdapter.setisEnd(false);
    	            }  
    	        });	    		
    	}
		

		//隐藏load more
		public void hideLoadMore(){
			//moreView.setVisibility(View.GONE);
			//list.removeFooterView(moreView);
   		    pg.setVisibility(View.INVISIBLE);
            bt.setText("没有了！我来发表");

		}

    //show load more
    public void showLoadMore(){
        //moreView.setVisibility(View.GONE);
        //list.removeFooterView(moreView);
        pg.setVisibility(View.VISIBLE);
        bt.setText("正在加载...");

    }
		
		//获取最后一条数据的ID
		public void getLastId(ArrayList<Gonggao> list){
			int i = list.size();
			Gonggao j = list.get(i-1);			
			lastId = j.getId();
			lastIdNum =Integer.parseInt(lastId);
			Log.e("LastId",lastId);			
		}
		
		//加载更多数据
				public void loadMoreData() {
					// TODO Auto-generated method stub
					if(lastIdNum == 1){
						hideLoadMore();
						Toast.makeText(activity, "加载完成！", Toast.LENGTH_SHORT).show();
					}else{
					HashMap<String, String> blogParams = new HashMap<String, String>();
					blogParams.put("Id", lastId);
					blogParams.put("typeId", "0");
					blogParams.put("pageId", "0");
					activity.doTaskAsync(C.task.gg1, C.api.gg, blogParams, false);
					}
				}

    private class HideFabFooterScrollListener extends HideFabScrollListener {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            swipeLayout.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);

        }

        // 滑到底部后自动加载
        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_FLING:
                    speakRecyclerAdapter.setFlagBusy(true);
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    speakRecyclerAdapter.setFlagBusy(false);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    speakRecyclerAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            speakRecyclerAdapter.notifyDataSetChanged();
            // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && isBottom(recyclerView)) {
                // 当滑到底部时自动加载
                //pg.setVisibility(View.VISIBLE);
                //bt.setVisibility(View.VISIBLE);
                //moreView.setVisibility(View.VISIBLE);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        activity.loadMoreData();
                        //bt.setVisibility(View.GONE);
                        //pg.setVisibility(View.GONE);
                    }
                });

            }
        }



        @Override
        public void onHide() {
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                //activity.mToolbar.animate().translationY(-activity.mToolbar.getHeight()).setInterpolator(new DecelerateInterpolator(2));
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) activity.mFabButton.getLayoutParams();
                int fabBottomMargin = lp.bottomMargin;
                activity.mFabButton.animate().translationY(activity.mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
                //Log.e("s","ddddddddddddddd");
            }
        }


        @Override
        public void onShow() {
            if (android.os.Build.VERSION.SDK_INT >= 14) {
                //activity.mToolbarContainer.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
                activity.mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        }
    }

    public boolean isBottom(RecyclerView recyclerView) {
        //int lastVisiblePosition = findLastCompletelyVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        return lastVisiblePosition == lastPosition;
    }
}
