package com.app.demos.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.demos.Listener.HideFabScrollListener;
import com.app.demos.Listener.OnHideOrShowListener;
import com.app.demos.R;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.layout.swipeRefreshLayout.Progress_m;
import com.app.demos.layout.swipeRefreshLayout.Progress_m.OnRefreshListener;
import com.app.demos.list.RecyclerAdapter.FindRecycAdapter;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.list.bitmap_load_list.LoaderAdapter;
import com.app.demos.model.Find;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.GonggaoSqlite;
import com.app.demos.ui.UiActionBar;

import android.app.Activity;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindFragment extends Fragment implements  OnRefreshListener {
    private static final String TAG = "FindFragment";
    private String hello;// = "hello android";
    private String defaultHello = "default value";
    //////////////////////////////////////////////
    private UiActionBar activity;

    //---View---
    private ListView list;
    private ImageButton ib;
    //public MyList blogListAdapter;
    private LoaderAdapter adapter;
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

    private GonggaoSqlite gonggaoSqlite;
    private Handler handler;
    private Boolean isLoade_more;
    public RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public FindRecycAdapter findRecyclerAdapter;
    private ArrayList<Find> findList;
    private OnHideOrShowListener onHideOrShowListener;

    public static FindFragment newInstance(String s) {
        FindFragment newFragment = new FindFragment();
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
        Log.d(TAG, "FindFragment-----onCreate");
        Bundle args = getArguments();
        hello = args != null ? args.getString("hello") : defaultHello;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        LogMy.d(activity, "FindFragment-----onCreateView");
        View view = inflater.inflate(R.layout.fragment_list_find, container, false);

        handler = new Handler();

        findList =  new ArrayList<Find>();
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_find_recyclerView);

        //recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //int paddingTop = Utils.getToolbarHeight(activity) + Utils.getTabsHeight(activity);
        //recyclerView.setPadding(recyclerView.getPaddingLeft(), paddingTop, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
        recyclerView.setHasFixedSize(true);     //使RecyclerView保持固定的大小,这样会提高RecyclerView的性能。
        layoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(layoutManager);
        findRecyclerAdapter = new FindRecycAdapter(activity, findList);

        recyclerView.setAdapter(findRecyclerAdapter);
        //recyclerView.setOnScrollListener(new RecyclerView.OnScrollListe
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setOnScrollListener(new HideFabFooterScrollListener());


        //list = (ListView) view.findViewById(R.id.ui_gongga_list_view);
        //ib = (ImageButton) view.findViewById(R.id.tpl_list_speak_ib_like);


        //setMyAdapter();
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
        swipeLayout = (Progress_m) view.findViewById(R.id.find_swipe_refresh);
        //swipeLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        swipeLayout.setColorSchemeColors( Color.RED, Color.YELLOW, Color.GREEN);
        swipeLayout.setOnRefreshListener(this);
        gonggaoSqlite = new GonggaoSqlite(activity);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            public void run() {
                /*
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
                    onRefresh();

                    String nowTime = Long.toString(loginTime);
                    SharedPreferences.Editor editor = sharedPreferences.edit();//获取编辑器
                    editor.putString("time", nowTime);
                    //editor.putInt("sex", (int) loginTime);
                    editor.commit();//提交修改
                }else{
                    //获取本地列表
                    //loadLocalData();
                }*/
                swipeLayout.setRefreshing(true);
                onRefresh();

            }
        });

    }

    @Override
    public void onDestroy() {
        ImageLoader imageLoader = adapter.getImageLoader();
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



    //设置默认List adapter
    public void setMyAdapter(){


    }

    //更新ListView数据
    public void setGgList(ArrayList<Find> g){
        findList.clear();
        findList.addAll(g);
        Log.e("findf", "setGgList()");
        //int i = recyclerAdapter.getBasicItemCount();
        //recyclerAdapter.notifyItemRangeChanged(0, i);
        findRecyclerAdapter.notifyDataSetChanged();// 通知listView刷新数据

    }

    //更新ListView数据
    public void addGgList(ArrayList<Find> g){
        int i = findRecyclerAdapter.getBasicItemCount();
        findRecyclerAdapter.notifyItemInserted(i);//显示动画
        findList.addAll(g);
        findRecyclerAdapter.notifyItemChanged(i);

        //recyclerAdapter.notifyDataSetChanged();// 通知listView刷新数据
    }

    //通知ListView加载图片
    public void listChanged() {
        adapter.notifyDataSetChanged();// 通知listView刷新数据
    }
/*
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
		}
		
		// 滑到底部后自动加载
		@Override
	    public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_FLING:
                    adapter.setFlagBusy(true);
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    adapter.setFlagBusy(false);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    adapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            adapter.notifyDataSetChanged();
	        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
	        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
	        		&& lastVisibleIndex == adapter.getCount()) {
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
	*/

    @Override
    public void onRefresh() {
        // TODO Auto-generated method stub
        new Handler().post(new Runnable() {
            public void run() {
                // swipeLayout.setRefreshing(false);
                activity.getFindData();
                //showLoadMore();
                findRecyclerAdapter.setisEnd(false);
            }
        });
    }

    //likeButton事件
    public void likeButtonClick(){
        SharedPreferences sharedPreferences = activity.getSharedPreferences("fragment1_isLike", 0);
        Boolean isLike = sharedPreferences.getBoolean("isLike",false);
        if (isLike) {
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
                    findRecyclerAdapter.setFlagBusy(true);
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    findRecyclerAdapter.setFlagBusy(false);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    findRecyclerAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            findRecyclerAdapter.notifyDataSetChanged();
            // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && isBottom(recyclerView)) {
                // 当滑到底部时自动加载
                //pg.setVisibility(View.VISIBLE);
                //bt.setVisibility(View.VISIBLE);
                //moreView.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.loadMoreFindData();
                        //bt.setVisibility(View.GONE);
                        //pg.setVisibility(View.GONE);
                    }
                }, 2000);

            }
        }



        @Override
        public void onHide() {
            if (onHideOrShowListener != null) {
                onHideOrShowListener.onhide();
            }
        }


        @Override
        public void onShow() {
            if (onHideOrShowListener != null) {
                onHideOrShowListener.onshow();
            }
        }
    }

    public void setOnHideOrShowListener(OnHideOrShowListener h) {
        onHideOrShowListener = h;
    }

    public boolean isBottom(RecyclerView recyclerView) {
        //int lastVisiblePosition = findLastCompletelyVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        return lastVisiblePosition == lastPosition;
    }
}
