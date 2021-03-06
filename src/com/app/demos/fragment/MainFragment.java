package com.app.demos.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.C;
import com.app.demos.list.MyList;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.GonggaoSqlite;
import com.app.demos.ui.MainActivity;
import com.app.demos.ui.UiSpeakComment;
import com.app.demos.ui.UiSpeakMain;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tom on 15-3-27.
 */
public class MainFragment extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "MainFragment";
    private String hello;// = "hello android";
    private String defaultHello = "default value";
    //////////////////////////////////////////////
    private MainActivity activity;

    //---View---
    private ListView list;
    private ImageButton ib;
    public MyList blogListAdapter;
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
    public int lastIdNum;
    private int MaxIdNum;

    private String lastTime;
    private String cacheMaxId;
    private String Maxid;
    public String lastId;

    private ArrayList<Gonggao> ggList;
    private GonggaoSqlite gonggaoSqlite;
    private Handler handler;

    public static MainFragment newInstance(String s) {
        MainFragment newFragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        this.activity = (MainActivity) activity;
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
        View view = inflater.inflate(R.layout.fragment_list_speak, container, false);
//        TextView viewhello = (TextView) view.findViewById(R.id.tv_hello);
//        viewhello.setText(hello);
        // viewhello.setVisibility();
        ////////////////////////////////////
        list = (ListView) view.findViewById(R.id.ui_gongga_list_view);
        ib = (ImageButton) view.findViewById(R.id.tpl_list_speak_ib_like);

        // 实例化load more data 底部布局
        moreView = activity.getLayoutInflater().inflate(R.layout.load_more, null);
        //bt = (TextView) moreView.findViewById(R.id.bt_load);
        //pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();
        list.addFooterView(moreView);
        list.setOnScrollListener(this);
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
        gonggaoSqlite = new GonggaoSqlite(activity);

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
                    onRefresh();

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
        ggList =  new ArrayList<Gonggao>();
        //Gonggao g = new Gonggao("10000","","my","","","2015-01-09");

        //ggList.add(g);
        //getLastId(ggList);
        blogListAdapter = new MyList(activity,R.layout.tpl_list_speak, ggList);
        list.setAdapter(blogListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {
                Bundle params = new Bundle();
                params.putString("speakId", ggList.get(postion).getId());
                params.putString("content", ggList.get(postion).getContent());
                params.putString("typeAll", ggList.get(postion).getTypeAll());
                params.putString("likeCount", ggList.get(postion).getLikeCount());
                params.putString("bgImageUrl", ggList.get(postion).getBgimage());
                activity.overlay(UiSpeakComment.class, params);
            }
        });

    }

    //更新ListView数据
    public void setGgList(ArrayList<Gonggao> g){
        ggList.clear();
        ggList.addAll(g);
        blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据

    }

    //更新ListView数据
    public void addGgList(ArrayList<Gonggao> g){
        ggList.addAll(g);
        blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
    }

    //通知ListView加载图片
    public void listChanged() {
        blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
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
        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex == blogListAdapter.getCount()) {
            // 当滑到底部时自动加载
            //pg.setVisibility(View.VISIBLE);
            //bt.setVisibility(View.VISIBLE);
            moreView.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMoreData();
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
        list.removeFooterView(moreView);
        //pg.setVisibility(View.GONE);

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
            activity.doTaskAsync(C.task.gg1, C.api.gg, blogParams);
        }
    }
}