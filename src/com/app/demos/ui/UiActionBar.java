package com.app.demos.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.adapter.MyFragmentPagerAdapter;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.layout.other.PagerSlidingTabStrip_my;
import com.app.demos.layout.other.TabRedDian;
import com.app.demos.list.MyList;
import com.app.demos.list.bitmap_load_list.LoaderAdapter;
import com.app.demos.model.FavoriteSpeak;
import com.app.demos.model.Find;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.FavoriteSpeakSqlite;
import com.app.demos.sqlite.GonggaoSqlite;
import com.app.demos.ui.authenticator.UiAuthenticator;
import com.app.demos.ui.fragment.FindFragment;
import com.app.demos.ui.fragment.Fragment2;
import com.app.demos.ui.fragment.Fragment3;
import com.app.demos.ui.fragment.SpeakFragment;
import com.app.demos.ui.test.UiFoxconnEssPost;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tom on 15-3-25.
 */
public class UiActionBar extends BaseUi implements SwipeRefreshLayout.OnRefreshListener {
    public static final String TAG = "UiActionBar";
    private boolean isFirstOpean;
    private ViewPager mPager;
    private ArrayList<Fragment> fragmentsList;
    private TextView drawerTv;
    private ImageView ivBottomAdd0;
    private ImageView ivBottomAdd1;
    private ImageView ivBottomAdd2;
    private TextView tvTabActivity, tvTabGroups, tvTabFriends, tvTabChat;
    private long mPressedTime = 0;

    private int currIndex = 0;
    private int bottomLineWidth;
    private int offset = 1000;
    public int position_one;
    private int position_two;
    private int position_three;
    private Resources resources;
    private SpeakFragment activityfragment;
    private FindFragment findfragment;
    /////////////////////////////////////
    private ListView drawerList;
    private MyList blogListAdapter;
    private LoaderAdapter adapter;
    public GonggaoSqlite gonggaoSqlite;
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
    public SwipeRefreshLayout swipeLayout;

    // ListView底部View
    private View moreView;
    private Button bt;
    private ProgressBar pg;
    public Handler handler;
    // 设置一个最大的数据条数，超过即不再加载

    // 最后可见条目的索引
    private int lastVisibleIndex;
    private int NUM ;
    //private ActionBar actionBar;
    private TabRedDian viewTab;
    private FrameLayout ivLayout;
    private TextView tv;
    private int i;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ShareActionProvider mShareActionProvider;
    public PagerSlidingTabStrip_my mPagerSlidingTabStrip;
    public Toolbar mToolbar;
    public LinearLayout mToolbarContainer;
    public ImageButton mFabButton;
    private String find_lastId;
    private int find_lastIdNum;
    private Context context;
    public SharedPreferences sharedPreferences_speak;
    public FavoriteSpeakSqlite favoriteSpeakSqlite;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ui_actionbar_hide);
/////////////////   test   //////////////////////////////////////////

///////////////////////////   test   ///////////////////////////////
        context = this;
        ivLayout = (FrameLayout) findViewById(R.id.ui_actionbar_layout_add);
        ivBottomAdd0 = (ImageView) findViewById(R.id.ui_actionbar_iv0);
        ivBottomAdd1 = (ImageView) findViewById(R.id.ui_actionbar_iv1);
        ivBottomAdd2 = (ImageView) findViewById(R.id.ui_actionbar_iv2);
        ///////////////////////////////////////////
        this.setHandler(new MyHandler(this));
        isFirstOpean = true;//第一次打开程序
        resources = getResources();
        initWidth();
        setUpActionBar();
        //initSwipeRefresh();
        InitViewPager();
        initBottomButtom();
        initDrawer();

        sharedPreferences_speak = getSharedPreferences("fragment_speak", MODE_PRIVATE);
        gonggaoSqlite = new GonggaoSqlite(this);
        favoriteSpeakSqlite =new FavoriteSpeakSqlite(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mDrawerLayout.closeDrawers();
    }

    public static void actionStart(Context context) {
        Intent localIntent = new Intent( context, UiActionBar.class);
        context.startActivity(localIntent);
    }
    private void initSwipeRefresh() {
        //下拉更新Layout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeLayout.setOnRefreshListener(this);
    }

    private void initBottomButtom() {
        mFabButton = (ImageButton) findViewById(R.id.fabButton);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UiActionBar.this, UiCreateSpeak.class);
                startActivityForResult(intent, 1);
            }
        });
        mFabButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                overlay(UiFoxconnEssPost.class);
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    int Find_index = data.getIntExtra("Find_index", 2);
                    mPager.setCurrentItem(2);
                    findfragment.swipeLayout.setRefreshing(true);
                    findfragment.onRefresh();
                }
        }
    }

    /**
     * 初始化DrawerLayout
     */
    private void initDrawer() {
        drawerTv = (TextView) findViewById(R.id.drawer_tv);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        String[] data = {"设备信息", "other", "login out"};
        drawerList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        TelephonyManager phoneMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                        String phoneName = Build.BRAND;//品牌
                        String phoneModel = Build.MODEL;//手机型号
                        String phoneNumber =phoneMgr.getLine1Number();//本机电话号码
                        int SDK =Build.VERSION.SDK_INT;//SDK版本号
                        String OS =Build.VERSION.RELEASE;//Firmware/OS 版本号

                        drawerTv.setText(phoneName + "\n" + phoneModel + "\n" + phoneNumber + "\n" + SDK + "\n" + OS);
                        break;
                    case 1:
                        //overlay(UiImageZoom.class);
                        startActivity(new Intent(context,UiImageZoom.class));
                        //overridePendingTransition(R.anim.img_zoom_in_center, 0);//动画效果
                        mDrawerLayout.closeDrawers();
                        break;
                    case 2:
                        UiAuthenticator.actionStart(context, 1);
                        //overridePendingTransition(R.anim.img_zoom_in_center, 0);//动画效果
                        break;
                }
            }
        });
    }

    private void setUpActionBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        /// mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);
// toolbar.setLogo(R.drawable.ic_launcher);
        mToolbar.setTitle("富友");// 标题的文字需在setSupportActionBar之前，不然会无效
// toolbar.setSubtitle("副标题");
        setSupportActionBar(mToolbar);
/* 这些通过ActionBar来设置也是一样的，注意要在setSupportActionBar(toolbar);之后，不然就报错了 */
// getSupportActionBar().setTitle("标题");
// getSupportActionBar().setSubtitle("副标题");
// getSupportActionBar().setLogo(R.drawable.ic_launcher);
        // getSupportActionBar().setHomeButtonEnabled(true);

/* 菜单的监听可以在toolbar里设置，也可以像ActionBar那样，通过Activity的onOptionsItemSelected回调方法来处理 */
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Toast.makeText(UiActionBar.this, "action_settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_share:
                        Toast.makeText(UiActionBar.this, "action_share", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
		/* ShareActionProvider配置 */
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        mShareActionProvider.setShareIntent(intent);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // switch (item.getItemId()) {
        // case R.id.action_settings:
        // Toast.makeText(MainActivity.this, "action_settings", 0).show();
        // break;
        // case R.id.action_share:
        // Toast.makeText(MainActivity.this, "action_share", 0).show();
        // break;
        // default:
        // break;
        // }
        //return super.onOptionsItemSelected(item);
        return true;
    }

    @Override
    public void onBackPressed() {
        mDrawerLayout.closeDrawers();
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
        if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
            Toast.makeText(this, "再按一次退出 富友", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }
        else{//退出程序
            this.finish();
            System.exit(0);
        }
    }

    /**
     * mPagerSlidingTabStrip默认值配置
     *
     */
    private void initTabsValue() {
        // 底部游标颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.BLUE);
        // tab的分割线颜色
        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        // tab背景
        mPagerSlidingTabStrip.setBackgroundColor(Color.parseColor("#4876FF"));
        // tab底线高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1, getResources().getDisplayMetrics()));
        // 游标高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5, getResources().getDisplayMetrics()));
        // 选中的文字颜色
        mPagerSlidingTabStrip.setSelectedTextColor(Color.WHITE);
        // 正常文字颜色
        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }

    private void InitViewPager() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip_my) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.ui_actionbar_vPager);
        fragmentsList = new ArrayList<Fragment>();
        //LayoutInflater mInflater = getLayoutInflater();
        //View activityView = mInflater.inflate(R.layout.fragment_list_speak, null);

        activityfragment = SpeakFragment.newInstance("Hello Activity.");
        findfragment = FindFragment.newInstance("Hello Activity.");
        Fragment groupFragment = new Fragment2();
        //Fragment friendsFragment=new Fragment3();
        //Fragment chatFragment=SpeakFragment.newInstance("Hello Chat.");


        fragmentsList.add(activityfragment);
        fragmentsList.add(groupFragment);
        fragmentsList.add(findfragment);
        fragmentsList.add(new Fragment3());
        //fragmentsList.add(chatFragment);

        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentsList));
        mPager.setCurrentItem(0);
        //左右预加载个数
        mPager.setOffscreenPageLimit(3);
        ///  切换动画
        //mPager.setPageTransformer(true,new DepthPageTransformer());
        //mPager.setOnPageChangeListener(new MyOnPageChangeListener());
        mPagerSlidingTabStrip.setViewPager(mPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(new MyOnPageChangeListener());
        initTabsValue();
    }

    @Override
    public void onRefresh() {
        if (isFirstOpean) {
            Log.e(TAG, "onRefresh()  = " + isFirstOpean);
            new Handler().post(new Runnable() {
                public void run() {

                    getGonggaoData();
                    getFindData();
                    //showLoadMore();
                    Log.e(TAG, "onRefresh() true = " + isFirstOpean);
                }
            });
        } else {
            Log.e(TAG, "mPager.getCurrentItem() = " + mPager.getCurrentItem());
            switch (mPager.getCurrentItem()) {
                case 0:
                    getGonggaoData();
                case 2:
                    getFindData();
                    Log.e(TAG, "onRefresh() false"+ isFirstOpean);
            }
        }
        isFirstOpean = false;
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

    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int arg0) {
            //actionBar.selectTab(actionBar.getTabAt(arg0));
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (android.os.Build.VERSION.SDK_INT >= 11) {
                // mToolbarContainer.setTranslationY(0);
                //ivLayout.setRotationY(arg1 * 180);
            }
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

                    break;
                case 1:

                    break;

                case 2:

                    break;

            }
            //}
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }

    public void getFindData() {
        // show all  list
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("typeId", "0");
        blogParams.put("pageId", "0");
        this.doTaskAsync(C.task.find, C.api.find, blogParams);

    }

    //加载更多数据
    public void loadMoreData() {
        // TODO Auto-generated method stub
        if(lastIdNum == 1){
            //activityfragment.hideLoadMore();
            //activityfragment.recyclerAdapter.setisShowBottom(false);
            activityfragment.speakRecyclerAdapter.setisEnd(true);
            activityfragment.speakRecyclerAdapter.notifyItemChanged(activityfragment.speakRecyclerAdapter.getBasicItemCount());
            Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("Id", lastId);
            blogParams.put("typeId", "0");
            blogParams.put("pageId", "0");
            this.doTaskAsync(C.task.gg1, C.api.gg, blogParams);
        }
    }

    //加载更多Find数据
    public void loadMoreFindData() {
        // TODO Auto-generated method stub
        if(lastIdNum == 1){
            //activityfragment.hideLoadMore();
            //activityfragment.recyclerAdapter.setisShowBottom(false);
            findfragment.findRecyclerAdapter.setisEnd(true);
            findfragment.findRecyclerAdapter.notifyItemChanged(findfragment.findRecyclerAdapter.getBasicItemCount());
            Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("Id", lastId);
            blogParams.put("typeId", "0");
            blogParams.put("pageId", "0");
            this.doTaskAsync(C.task.find_more, C.api.find, blogParams);
        }
    }

    //从网络获取数据
    public void getGonggaoData(){
        // show all  list
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("typeId", "0");
        blogParams.put("pageId", "0");
        this.doTaskAsync(C.task.gg, C.api.gg, blogParams);

    }

    public void getFavoriteSpeakDelete(String customerid, String speakId) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("customerid", customerid);
        blogParams.put("speakId", speakId);
        this.doTaskAsync(C.task.favorite_speak_delete, C.api.favorite_speak_delete, blogParams);
    }

    public void getFavoriteSpeakCreate(String customerid, String speakId) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("customerid", customerid);
        blogParams.put("speakId", speakId);
        this.doTaskAsync(C.task.favorite_speak_create, C.api.favorite_speak_create, blogParams);
    }

    public void getFavoriteSpeakAll(String customerid) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("customerid", customerid);
        this.doTaskAsync(C.task.favorite_speak, C.api.favorite_speak, blogParams);
    }

    //获取最后一条数据的ID
    public void getLastId(ArrayList<Gonggao> list){
        int i = list.size();
        Gonggao j = list.get(i-1);
        lastId = j.getId();
        lastIdNum =Integer.parseInt(lastId);
        Log.e("id", lastId);
    }

    //获取最后一条数据的ID
    public void getFindLastId(ArrayList<Find> list){
        int i = list.size();
        Find j = list.get(i-1);
        find_lastId = j.getId();
        find_lastIdNum =Integer.parseInt(lastId);
        Log.e("find_id",lastId);
    }

    //获取第一条数据的ID
    public void getFirstId(ArrayList<Gonggao> list){
        Gonggao j = list.get(0);
        Maxid = j.getId();
        MaxIdNum = Integer.parseInt(Maxid);
        Log.e("maxid", Maxid);
    }
    /*
        @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                doFinish();
            }
            return super.onKeyDown(keyCode, event);
        }
    */
    @Override
    @SuppressWarnings("unchecked")
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        activityfragment.swipeLayout.setRefreshing(false);
        findfragment.swipeLayout.setRefreshing(false);
        Log.e(TAG, "" + i);
        switch (taskId) {
            case C.task.gg:
                try {	//all Data
                    ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");
                    //缓存数据
                    gonggaoSqlite.delete(null, null);
                    for(Gonggao g : ggList1){
                        gonggaoSqlite.updateGonggao(g);
                    }

                    getLastId(ggList1);
                    getFirstId(ggList1);

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
                    //for (Gonggao g : ggList1) {
                    //   gonggaoSqlite.updateGonggao(g);
                    //}
                    getLastId(ggList1);
                    activityfragment.addGgList(ggList1);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            case C.task.find:
                try {	//all Data
                    ArrayList<Find> ggList1 = (ArrayList<Find>) message.getResultList("Find");
                    //blogSqlite.updateBlog(blog);
                    getFindLastId(ggList1);
                    //getFirstId(ggList1);
                    findfragment.setGgList(ggList1);

                    //activityfragment.blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;

            case C.task.find_more:
                try {	//剩余Data
                    ArrayList<Find> ggList1 = (ArrayList<Find>) message.getResultList("Find");

                    getFindLastId(ggList1);
                    findfragment.addGgList(ggList1);
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
                        //loadImage(g.getBgimage());
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

            case C.task.favorite_speak_create:
                try {
                    if (message.getCode().equals("10000")) {
                        toast(getString(R.string.favorite_ok));
                    } else {
                        toast(getString(R.string.favorite_fail));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            case C.task.favorite_speak_delete:
                try {
                    if (message.getCode().equals("10000")) {
                        toast(getString(R.string.favorite_delete_ok));
                    } else {
                        toast(getString(R.string.favorite_delete_fail));
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case C.task.favorite_speak:
                try {
                    ArrayList<FavoriteSpeak> ggList = (ArrayList<FavoriteSpeak>) message.getResultList("FavoriteSpeak");
                    for (FavoriteSpeak g : ggList) {
                           favoriteSpeakSqlite.updateFavoriteSpeak(g);
                        }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onNetworkError(int taskId) {
        super.onNetworkError(taskId);
        switch (taskId) {
            case C.task.gg:
                activityfragment.swipeLayout.setRefreshing(false);
                break;
            case C.task.find:
                findfragment.swipeLayout.setRefreshing(false);
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

    private void initWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;
//        offset = (int) ((screenW / 4.0 - bottomLineWidth) / 2);
//        Log.i("MainActivity", "offset=" + offset);

        position_one = (int) (screenW / 3.0);

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
