package com.app.demos.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;

import com.app.demos.Listener.HidingScrollListener;
import com.app.demos.Listener.OnHideOrShowListener;
import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.layout.swipeRefreshLayout.Progress_m;
import com.app.demos.layout.swipeRefreshLayout.Progress_m.OnRefreshListener;
import com.app.demos.list.RecyclerAdapter.SpeakRecyclerAdapter;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.model.FavoriteSpeak;
import com.app.demos.model.Gonggao;
import com.app.demos.sqlite.FavoriteSpeakSqlite;
import com.app.demos.sqlite.GonggaoSqlite;
import com.app.demos.ui.UiImageZoom;
import com.app.demos.ui.UiSpeakComment;
import com.app.demos.ui.authenticator.UiAuthenticator;
import com.app.demos.ui.fragment.emoji.ParseEmojiMsgUtil;
import com.app.demos.util.BaseDevice;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import static com.app.demos.util.Math_my.isEven;

public class SpeakFragment_v1 extends BaseFragment implements  OnRefreshListener {
    private static final String TAG = "speakFragment_v1----";
    private String hello;// = "hello android";
    private String defaultHello = "default value";
    //////////////////////////////////////////////

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
    private int NUM;
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
    private SharedPreferences sharedPreferences_speak;
    private FavoriteSpeakSqlite favoriteSpeakSqlite;
    private OnHideOrShowListener onHideOrShowListener;
    private Context context;
    private HidingScrollListener mHideFabFooterScrollListener;

    public static SpeakFragment_v1 newInstance(String s) {
        SpeakFragment_v1 newFragment = new SpeakFragment_v1();
        Bundle bundle = new Bundle();
        bundle.putString("hello", s);
        newFragment.setArguments(bundle);
        return newFragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogMy.d(activity, TAG + "-----onCreate");
        Bundle args = getArguments();
        hello = args != null ? args.getString("hello") : defaultHello;
        sharedPreferences_speak = activity.getSharedPreferences("fragment_speak", 0);

        context = getActivity();
        gonggaoSqlite = new GonggaoSqlite(activity);
        favoriteSpeakSqlite = new FavoriteSpeakSqlite(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogMy.d(activity, TAG + "-----onCreateView");
        View view = inflater.inflate(R.layout.fragment_list_speak_recycler, container, false);
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        ggList = new ArrayList<Gonggao>();
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
                String thumburl = C.web.thumb_image + imagePath;//thumb image path
                UiImageZoom.actionStart(activity, C.web.bgimage + imagePath, thumburl);
                //activity.overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);//动画效果
            }

            @Override
            public void onFavoriteClick(View v, int position) {
                onFavoriteClickk(v, position);
            }
        });
        recyclerView.setAdapter(speakRecyclerAdapter);
        //recyclerView.setOnScrollListener(new RecyclerView.OnScrollListe
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int height = BaseDevice.getToolbarHeight(context) + (int)context.getResources().getDimension(R.dimen.tabsHeight);
        mHideFabFooterScrollListener = new HideFabFooterScrollListener(height);
        recyclerView.setOnScrollListener(mHideFabFooterScrollListener);

        //下拉更新Layout
        swipeLayout = (Progress_m) view.findViewById(R.id.speak_swipe_refresh);
        swipeLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.YELLOW, Color.GREEN);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setProgressViewOffset(false, 0, (int)getResources().getDimension(R.dimen.tabsHeight));
    }

    private void onFavoriteClickk(View v, int position) {
        if (!sharedPreferences_speak.getBoolean("isLogined", false)) {
            UiAuthenticator.actionStart(activity, 1);
            return;
        }
        String empno = sharedPreferences_speak.getString("empno", null);
        SharedPreferences.Editor editor = sharedPreferences_speak.edit();//获取编辑器
        editor.putBoolean("isLatest_favorite", false);
        editor.commit();//提交修改

        Gonggao g = ggList.get(position);
        if (g.getFavorite() != null && g.getFavorite().equals("0")) {
            v.setBackgroundResource(!isEven(position) ? R.drawable.ic_card_like : R.drawable.ic_card_like_grey);
            g.setFavorite("1");
            g.setLikecount(String.valueOf(Integer.parseInt(g.getLikeCount()) - 1));
            gonggaoSqlite.updateGonggao(g);
            favoriteSpeakSqlite.delete(FavoriteSpeak.COL_SPEAKID + "=?", new String[]{g.getId()});
            getFavoriteSpeakDelete(empno, g.getId());//delete on server
        } else {
            v.setBackgroundResource(R.drawable.ic_card_liked);
            g.setFavorite("0");
            g.setLikecount(String.valueOf(Integer.parseInt(g.getLikeCount()) + 1));
            gonggaoSqlite.updateGonggao(g);

            ContentValues values = new ContentValues();
            values.put(FavoriteSpeak.COL_SPEAKID, g.getId());
            favoriteSpeakSqlite.create(values);
            getFavoriteSpeakCreate(empno, g.getId());
        }
        speakRecyclerAdapter.notifyDataSetChanged();
        LogMy.e(activity, TAG + g.getId() + "---onFavoriteClick");
    }

    private void getGonggaoData() {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("typeId", "0");
        blogParams.put("pageId", "0");
        this.doTaskAsync(C.task.gg, C.api.gg, blogParams, true);
    }

    /**
     * 加载更多数据
     */
    public void loadMoreData() {
        if (lastIdNum == 1) {
            //activityfragment.hideLoadMore();
            //activityfragment.recyclerAdapter.setisShowBottom(false);
            speakRecyclerAdapter.setisEnd(true);
            speakRecyclerAdapter.notifyItemChanged(speakRecyclerAdapter.getBasicItemCount());
            Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
        } else {
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("Id", lastId);
            blogParams.put("typeId", "0");
            blogParams.put("pageId", "0");
            this.doTaskAsync(C.task.gg1, C.api.gg, blogParams, false);
        }
    }

    private void getFavoriteSpeakCreate(String customerid, String speakId) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("customerid", customerid);
        blogParams.put("speakId", speakId);
        this.doTaskAsync(C.task.favorite_speak_create, C.api.favorite_speak_create, blogParams, false);
    }

    private void getFavoriteSpeakDelete(String customerid, String speakId) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("customerid", customerid);
        blogParams.put("speakId", speakId);
        this.doTaskAsync(C.task.favorite_speak_delete, C.api.favorite_speak_delete, blogParams, false);
    }

    private void initData() {
        final ArrayList<Gonggao> gList = gonggaoSqlite.getAllGonggao();
        if (!gList.isEmpty()) {
            getLastId(gList);
            getFirstId(gList);
            for (Gonggao g : gList) {//转换成表情
                SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, g.getContent());
                g.setEmojiString(spannableString);
            }
            setGgList(gList);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().post(new Runnable() {
            public void run() {
                //long loginTime = System.currentTimeMillis();

                checkFavoriteSpeak();
                //String lastTime = sharedPreferences.getString("time", "0");
                //long time0 = Long.valueOf(lastTime);
                //long jgtime = loginTime - time0;

                //Log.w("LoginTime", "" + loginTime);
                //Log.w("Time0", "" + time0);
                //Log.w("jgtime", "" + jgtime);
                //if (jgtime > 30000) {
                //从网路获取列表

                //onRefresh();

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
        Boolean isLatest_favorite = sharedPreferences_speak.getBoolean("isLatest_favorite", false);
        if (!isLatest_favorite) {
            getFavoriteSpeakAll(String.valueOf(10));
        }
    }

    private void getFavoriteSpeakAll(String customerid) {
        HashMap<String, String> blogParams = new HashMap<String, String>();
        blogParams.put("customerid", customerid);
        this.doTaskAsync(C.task.favorite_speak, C.api.favorite_speak, blogParams, false);
    }

    @Override
    public void onDestroy() {
        ImageLoader_my imageLoader = speakRecyclerAdapter.getImageLoader();
        if (imageLoader != null) {
            imageLoader.clearCache();
        }
        super.onDestroy();
        LogMy.d(activity, TAG + "-----onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //更新ListView数据
    public void setGgList(ArrayList<Gonggao> g) {
        if (ggList != null) {
            ggList.clear();
            ggList.addAll(g);
            speakRecyclerAdapter.notifyDataSetChanged();// 通知listView刷新数据
        }
    }

    //更新ListView数据
    public void addGgList(ArrayList<Gonggao> g) {
        int i = speakRecyclerAdapter.getBasicItemCount();
        speakRecyclerAdapter.notifyItemInserted(i);//显示动画
        ggList.addAll(g);
        speakRecyclerAdapter.notifyItemChanged(i);

        //recyclerAdapter.notifyDataSetChanged();// 通知listView刷新数据
    }


    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            public void run() {
                getGonggaoData();
                speakRecyclerAdapter.setisEnd(false);
            }
        });
    }


    /**
     * 获取最后一条数据的ID
     */
    public void getLastId(ArrayList<Gonggao> list){
        int i = list.size();
        Gonggao j = list.get(i - 1);
        lastId = j.getId();
        lastIdNum =Integer.parseInt(lastId);
        LogMy.e(activity, TAG + "lastId--" + lastId);
    }

    /**
     * 获取第一条数据的ID
     */
    public void getFirstId(ArrayList<Gonggao> list){
        Gonggao j = list.get(0);
        Maxid = j.getId();
        MaxIdNum = Integer.parseInt(Maxid);
        LogMy.e(activity, TAG + "maxid" + Maxid);
    }

    //private class HideFabFooterScrollListener extends HideFabScrollListener {
    private class HideFabFooterScrollListener extends HidingScrollListener {
        public HideFabFooterScrollListener(Context context) {
            super(context);
        }

        public HideFabFooterScrollListener(int viewHeight) {
            super(viewHeight);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            swipeLayout.setEnabled(layoutManager.findFirstCompletelyVisibleItemPosition() == 0);

        }

        // 滑到底部后自动加载
        @Override
        public void onScrollStateChanged(RecyclerView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);

            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_FLING:
                    speakRecyclerAdapter.setFlagBusy(true);
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    speakRecyclerAdapter.setFlagBusy(false);
                    speakRecyclerAdapter.notifyDataSetChanged();
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    speakRecyclerAdapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            //speakRecyclerAdapter.notifyDataSetChanged();//频繁使用notifyDataSetChanged() case listView滑动卡顿
            // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
            if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                    && isBottom(recyclerView)) {
                // 当滑到底部时自动加载
                //pg.setVisibility(View.VISIBLE);
                //bt.setVisibility(View.VISIBLE);
                //moreView.setVisibility(View.VISIBLE);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        loadMoreData();
                        //bt.setVisibility(View.GONE);
                        //pg.setVisibility(View.GONE);
                    }
                });

            }
        }


        @Override
        public void onMoved(int distance) {
            if (onHideOrShowListener != null) {
                onHideOrShowListener.onMoved(distance);
            }
        }

        @Override
        public void onHide() {
            if (onHideOrShowListener != null) {
                onHideOrShowListener.onHide();
            }
        }

        @Override
        public void onShow() {
            if (onHideOrShowListener != null) {
                onHideOrShowListener.onShow();
            }
        }
    }

    public void setScrollDistance(int scrollDistance) {
        this.mHideFabFooterScrollListener.setmToolbarOffset(scrollDistance);
    }


    @Override
    public void onNetworkError(int taskId) {
        super.onNetworkError(taskId);
        switch (taskId) {
            case C.task.gg:
                speakRecyclerAdapter.setNeworkError(true);
                break;
        }
    }

    @Override
    public void showProgressBar() {
        super.showProgressBar();
        swipeLayout.setRefreshing(true);
    }

    @Override
    public void hideProgressBar() {
        super.hideProgressBar();
        swipeLayout.setRefreshing(false);
    }

    public boolean isBottom(RecyclerView recyclerView) {
        //int lastVisiblePosition = findLastCompletelyVisibleItemPosition();
        int lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition();
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;
        return lastVisiblePosition == lastPosition;
    }

    public void setOnHideOrShowListener(OnHideOrShowListener h) {
        onHideOrShowListener = h;
    }

    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch (taskId) {
            case C.task.gg:
                try {    //all Data
                    ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");
                    //缓存数据
                    if (ggList1 != null) {
                        gonggaoSqlite.delete(null, null);
                        for (Gonggao g : ggList1) {
                            if (favoriteSpeakSqlite.exists(FavoriteSpeak.COL_SPEAKID + "=?", new String[]{g.getId()})) {
                                g.setFavorite("0");
                            } else {
                                g.setFavorite("1");
                            }
                            gonggaoSqlite.updateGonggao(g);
                        }
                        getLastId(ggList1);
                        getFirstId(ggList1);
                    }

                    ArrayList<Gonggao> gg = gonggaoSqlite.getAllGonggao();
                    for (Gonggao g : gg) {//转换成表情
                        SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, g.getContent());
                        g.setEmojiString(spannableString);
                    }
                    setGgList(gg);

                    //activityfragment.blogListAdapter.notifyDataSetChanged();// 通知listView刷新数据
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;

            case C.task.gg1:
                try {    //剩余Data
                    ArrayList<Gonggao> ggList1 = (ArrayList<Gonggao>) message.getResultList("Gonggao");
                    if (ggList1 != null) {
                        //gonggaoSqlite.delete(null, null);
                        for (Gonggao g : ggList1) {
                            if (favoriteSpeakSqlite.exists(FavoriteSpeak.COL_SPEAKID + "=?", new String[]{g.getId()})) {
                                g.setFavorite("0");
                            } else {
                                g.setFavorite("1");
                            }
                            gonggaoSqlite.updateGonggao(g);
                        }
                        getLastId(ggList1);
                    }

                    ArrayList<Gonggao> gg = gonggaoSqlite.getAllGonggao();
                    for (Gonggao g : gg) {//转换成表情
                        SpannableString spannableString = ParseEmojiMsgUtil.getExpressionString(context, g.getContent());
                        g.setEmojiString(spannableString);
                    }
                    setGgList(gg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
