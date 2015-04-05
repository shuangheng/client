package com.app.demos.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.PageTransformer.DepthPageTransformer_1;
import com.app.demos.PageTransformer.RotateDownPageTransformer;
import com.app.demos.PageTransformer.ZoomOutPageTransformer;
import com.app.demos.R;
import com.app.demos.adapter.MyFragmentPagerAdapter;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.fragment.FindFragment;
import com.app.demos.fragment.Fragment2;
import com.app.demos.fragment.Fragment3;
import com.app.demos.fragment.SpeakFragment;
import com.app.demos.layout.TabRedDian;
import com.app.demos.list.MyList;
import com.app.demos.model.Gonggao;
import com.app.demos.PageTransformer.DepthPageTransformer;
import com.app.demos.sqlite.GonggaoSqlite;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tom on 15-3-25.
 */
public class UiActionBar extends BaseUi implements ActionBar.TabListener {
    private static final String TAG = "MainActivity";
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private ImageView ivBottomLine;
    private ImageView ivBottomAdd0;
    private ImageView ivBottomAdd1;
    private ImageView ivBottomAdd2;
    private TextView tvTabActivity, tvTabGroups, tvTabFriends, tvTabChat;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 1000;
    private int position_one;
    private int position_two;
    private int position_three;
    private Resources resources;
    private SpeakFragment activityfragment;
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
    private ActionBar actionBar;
    private TabRedDian viewTab;
    private FrameLayout ivLayout;
    private TextView tv;
    private int i;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_actionbar);
        //tv = (TextView) findViewById(R.id.ui_actionbar_tv);
        ivLayout = (FrameLayout) findViewById(R.id.ui_actionbar_layout_add);
        ivBottomAdd0 = (ImageView) findViewById(R.id.ui_actionbar_iv0);
        ivBottomAdd1 = (ImageView) findViewById(R.id.ui_actionbar_iv1);
        ivBottomAdd2 = (ImageView) findViewById(R.id.ui_actionbar_iv2);
        ///////////////////////////////////////////
        this.setHandler(new MyHandler(this));

        resources = getResources();
        setUpActionBar();
        InitViewPager();

        ivBottomAdd0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottomAdd0.setVisibility(View.GONE);
                ivBottomAdd1.setVisibility(View.VISIBLE);
            }
        });
        ivBottomAdd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottomAdd1.setVisibility(View.GONE);
                ivBottomAdd2.setVisibility(View.VISIBLE);
            }
        });
        ivBottomAdd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivBottomAdd2.setVisibility(View.GONE);
                ivBottomAdd0.setVisibility(View.VISIBLE);
            }
        });

        gonggaoSqlite = new GonggaoSqlite(this);
    }

    private void setUpActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab()
                .setTabListener(this)
                .setCustomView(new TabRedDian(this) {

                    @Override
                    public String setText() {
                        return getResources().getString(R.string.tab_1);
                    }

                    @Override
                    public Boolean showRed() {
                        return true;
                    }
                }));
                //.setIcon(R.drawable.ic_launcher)
                viewTab = new TabRedDian(this) {
                    @Override
                    public String setText() {
                        return getResources().getString(R.string.tab_2);
                    }

                    @Override
                    public Boolean showRed() {
                        return true;
                    }
                };

        actionBar.addTab(actionBar.newTab()
                .setCustomView(viewTab)
                //.setIcon(R.drawable.ic_launcher)
                .setTabListener(this));

        actionBar.addTab(actionBar.newTab()
                .setText("Test")
                //.setIcon(R.drawable.ic_launcher)
                .setTabListener(this));
        actionBar.setCustomView(R.layout.red_dian);
        //默认显示
        //actionBar.selectTab(actionBar.getTabAt(1));
    }

    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.ui_actionbar_vPager);
        fragmentsList = new ArrayList<Fragment>();
        //LayoutInflater mInflater = getLayoutInflater();
        //View activityView = mInflater.inflate(R.layout.fragment_list_speak, null);

        activityfragment = SpeakFragment.newInstance("Hello Activity.");
        Fragment groupFragment = new Fragment2();
        Fragment friendsFragment=new Fragment3();
        //Fragment chatFragment=SpeakFragment.newInstance("Hello Chat.");


        fragmentsList.add(activityfragment);
        fragmentsList.add(groupFragment);
        fragmentsList.add(friendsFragment);
        //fragmentsList.add(chatFragment);

        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
        //左右预加载个数
        mPager.setOffscreenPageLimit(2);

        /////dev3  bug2  切换动画

        mPager.setPageTransformer(true,new DepthPageTransformer());
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if (mPager != null)
            mPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 1) {
            viewTab.iv.setVisibility(View.GONE);
            //viewTab.tv.setText("hello");
        }


    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

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
    };

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            actionBar.selectTab(actionBar.getTabAt(arg0));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            ivLayout.setRotationY(arg1*180);
            //ivBottomAdd0.setRotationY(arg1);
            //ivBottomAdd2.setRotationY(arg1);
            //ivBottomAdd1.setRotationY(arg1);
            //将要进入的Fragment
             i = mPager.getCurrentItem();
            //if ((int)arg1 == 0.5) {
            //actionBar.getTabAt(0).setText(""+arg0);
            //actionBar.getTabAt(1).setText(""+i);
           // tv.setText("arg0 : "+arg0+"\n"
              //          +"arg1 : "+arg1+"\n"
               //         +"i : "+i);
                switch (i) {
                    case 0:
                        ivBottomAdd0.setVisibility(View.VISIBLE);
                        ivBottomAdd1.setVisibility(View.GONE);
                        ivBottomAdd2.setVisibility(View.GONE);
                        //View pb = findViewById(R.id.fragment_speak_layout);
                        //pb.setTranslationX(720*arg1);
                        //pb.setRotationY(arg1*90);
                        break;
                    case 1:
                        ivBottomAdd2.setVisibility(View.GONE);
                        ivBottomAdd0.setVisibility(View.GONE);
                        ivBottomAdd1.setVisibility(View.VISIBLE);
                        //View pb1 = findViewById(R.id.fragment_2);
                        //pb1.setTranslationX(720*arg1);
                       // pb1.setRotationY(arg1*90);
                        break;

                    case 2:
                        ivBottomAdd0.setVisibility(View.GONE);
                        ivBottomAdd1.setVisibility(View.GONE);
                        ivBottomAdd2.setVisibility(View.VISIBLE);
                        //View pb2 = findViewById(R.id.fragment_3);
                        //pb2.setTranslationX(720*arg1);
                        //pb2.setRotationY(arg1*90);
                        break;

                }
            //}
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
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
        Log.e("maxid", Maxid);
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

    //show ivBottomAdd
    public void showIvBottomAdd(int i) {
        ArrayList<ImageView> ivList = new ArrayList<ImageView>();
        ivList.add(ivBottomAdd0);
        ivList.add(ivBottomAdd1);
        ivList.add(ivBottomAdd2);
        ivList.get(i).setVisibility(View.VISIBLE);
    }

    /*/-----actionBar添加iterm菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //加载action items
        getMenuInflater().inflate(R.menu.ui_actionbar_menu, menu);
        return true;
    }
    */

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
