package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.layout.ButtonFloat;
import com.app.demos.layout.ResizeLayout;
import com.app.demos.layout.dragtoplayout.AttachUtil;
import com.app.demos.layout.dragtoplayout.DragTopLayout;
import com.app.demos.layout.materialEditText.MaterialEditText;
import com.app.demos.layout.other.CircleImageView;
import com.app.demos.list.CommentList;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.model.Comment;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.fragment.UserInfoFragment;
import com.app.demos.util.AppFilter;
import com.app.demos.util.BaseDevice;
import com.app.demos.util.ColorUtil;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import de.greenrobot.event.EventBus;

import static com.app.demos.util.Math_my.isEven;

public class UiSpeakComment extends BaseUi implements OnScrollListener, OnClickListener {
    private static final int BIGGER = 1;//about InputMode
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private static final int HEIGHT_THREADHOLD = 30;
    private ResizeLayout reSizelayout;

    private String speakId;
    private String bgImageUrl;

    private Toolbar toolbar;
    private ListView list;

    private ArrayList<Comment> commentList;
    private CommentList commentAdapter;
    private CircleImageView speakerIv;
    private DragTopLayout dragLayout;
    private View moreView;
    private ProgressBar moreView_pg;
    private TextView moreView_tv;
    private int lastVisibleIndex;
    private int lastIdNum;
    private int default_ResizeLayout_height;
    private String lastId;

    private String bgColor;
    private String favorite;
    private int toolcolor;

    private ButtonFloat btnFloat;
    private LinearLayout editLayout;
    private boolean bgColorIsWhite;
    private float speakerIv_x;
    private ImageView ivBgImage;
    private View tplSpeak;
    private UserInfoFragment userInfo;
    private boolean speakerIvIsShown = true;
    private Handler handler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_speak_comment);

        // set handler
        //this.setHandler(new BlogHandler(this));
        handler = new BlogHandler();

        //--fill content
        initParamData();
        initView();

        initList();
        fillContent();
        initToolBar();
        //**load bgImage
        loadBgImage();

    }

    @Override
    public void onStart () {
        super.onStart();
        if (commentList == null) {
            LogMy.e(getContext(), "commentList == null");
            loadData();
            //userInfo.loadData();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btnFloat.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    public static void actionStart(Context context,Gonggao g, String bgColor) {
        Intent intent = new Intent( context, UiSpeakComment.class);
        intent.putExtra("Gonggao", g);
        intent.putExtra("bgColor", bgColor);
        context.startActivity(intent);
    }

    //设置默认List adapter
    public void setMyAdapter(){
        //commentList =  new ArrayList<Comment>();
        //Comment comment = new Comment("ff", "ffff");
        //commentList.add(comment);
        if (commentAdapter == null) {
            commentAdapter = new CommentList(this, R.layout.tpl_list_speak_comment, commentList);
            list.setAdapter(commentAdapter);
        }

    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    // other methods
    private void initToolBar() {

        toolbar.setTitle(getTitle());// 标题的文字需在setSupportActionBar之前，不然会无效
        if (!bgColorIsWhite) {
            toolbar.setBackgroundColor(toolcolor);
            speakerIv.setBorderColor(toolcolor);
            btnFloat.setBackgroundColor(toolcolor);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//实现左侧返回按钮
    }

    //--load bgImage
    private void loadBgImage(){
        if (!bgImageUrl.equals("null")) {
            ivBgImage.setVisibility(View.VISIBLE);
            new ImageLoader_my(this, "image").DisplayImage(C.web.bgimage + bgImageUrl + ".jpg", ivBgImage, false, false);
        }
    }

    private void initList() {
        list = (ListView) this.findViewById(R.id.ui_speak_comment_listview);
        list.addHeaderView(tplSpeak);
        list.addFooterView(moreView);

        list.setOnScrollListener(this);
        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {

            }
        });
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        reSizelayout = (ResizeLayout) findViewById(R.id.ui_speak_comment_ResizeLayout);
        speakerIv = (CircleImageView) findViewById(R.id.ui_speak_comment_circle_iv);
        userInfo = (UserInfoFragment) getSupportFragmentManager().findFragmentById(R.id.ui_speak_comment_top_view_fragment);

        moreView = getLayoutInflater().inflate(R.layout.tpl_list_speak_footer, null);
        moreView_pg = (ProgressBar) moreView.findViewById(R.id.list_speak_footer_progressbar);
        moreView_tv = (TextView) moreView.findViewById(R.id.list_speak_footer_hint_textview);

        tplSpeak = getLayoutInflater().inflate(R.layout.tpl_list_speak_comment_header, null);
        //layout = (LinearLayout) tplSpeak.findViewById(R.id.tpl_list_speak_bottom_layout);
        LinearLayout containerLayout = (LinearLayout) tplSpeak.findViewById(R.id.tpl_list_speak_container);
        TextView tvContent = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_content);
        TextView tvType = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_type);
        TextView extra = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_speak_extra);
        TextView tvLikeCount = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_like);
        ivBgImage = (ImageView) tplSpeak.findViewById(R.id.tpl_list_speak_iv_bg);
        ImageButton ibLike = (ImageButton) tplSpeak.findViewById(R.id.tpl_list_speak_ib_like);

        //////////////////////////////////////////////////////////////////////////
        Bundle params = this.getIntent().getExtras();
        Gonggao g = getIntent().getParcelableExtra("Gonggao");
        speakId = g.getId();
        bgImageUrl = g.getBgimage();
        favorite = g.getFavorite();
        bgColor = params.getString("bgColor");

        int position = Integer.parseInt(bgColor);//bg color
        bgColorIsWhite = isEven(position);
        toolcolor = bgColorIsWhite ? getResources().getColor(android.R.color.white) :
                getResources().getColor(C.colors[position % 16]);
        containerLayout.setBackgroundColor(toolcolor);

        tvContent.setText(AppFilter.getHtml(g.getContent()));
        tvContent.setTextColor(bgColorIsWhite ? Color.BLACK : Color.WHITE);
        tvType.setText(g.getType());
        extra.setText("评论 " + g.getCommentcount());
        tvLikeCount.setText(g.getLikeCount());
        ivBgImage.setBackgroundColor(getResources().getColor(R.color.white));

        if (favorite != null && favorite.equals("0")) {
            ibLike.setBackgroundResource(R.drawable.ic_card_liked);
        }
        //////////////////////////////////////////////////////////////

        btnFloat = (ButtonFloat) findViewById(R.id.ui_speak_comment_buttonFloat);
        editLayout = (LinearLayout) findViewById(R.id.ui_speak_comment_editLayout);
        dragLayout = (DragTopLayout) findViewById(R.id.ui_speak_comment_drag_layout);
        //dragLayout.toggleTopView();//关闭topView
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) speakerIv.getLayoutParams();
        speakerIv_x = lp.width;
        dragLayout.setDragListener(new DragTopLayout.PanelListener() {
            @Override
            public void onPanelStateChanged(DragTopLayout.PanelState panelState) {

            }

            @Override
            public void onSliding(float ratio) {
                OnSliding(ratio);
            }

            @Override
            public void onRefresh() {
                userInfo.loadData();
            }
        });

        reSizelayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;//default input hide
                if (h < oldh || h > oldh && h < default_ResizeLayout_height) {
                    if (h < oldh && default_ResizeLayout_height == 0) {
                        default_ResizeLayout_height = oldh;
                    }
                    change = SMALLER;//input is showing
                }

                Message msg = new Message();
                msg.what = 1;
                msg.arg1 = change;
                handler.sendMessage(msg);
            }
        });

        btnFloat.setOnClickListener(this);
        ivBgImage.setOnClickListener(this);
        ibLike.setOnClickListener(this);

    }

    private void OnSliding(float ratio) {
        if (ratio <= 0) {
            speakerIv.setTranslationX(0);//平移到原位
            speakerIv.setTranslationY(0);//平移到原位
            speakerIv.setScaleX(1);
            speakerIv.setScaleY(1);
        } else if (ratio <= 1) {
            speakerIv.setTranslationX(-(DEVICE_WIDTH / 2 - speakerIv_x) * ratio);//平移到 X 中点
            speakerIv.setTranslationY(0);//平移到 Y 原位
            speakerIv.setScaleX(1.0f + 0.5f * ratio);
            speakerIv.setScaleY(1.0f + 0.5f * ratio);
            ViewHelper.setAlpha(list, 1.5f - ratio);//半透明
            int bgColor = ColorUtil.caculateColor(toolcolor, Color.WHITE, ratio);//过渡颜色
            if (bgColorIsWhite) {
                ViewHelper.setAlpha(toolbar, 1.0f - ratio);//透明
            } else {
                toolbar.setBackgroundColor(bgColor);
            }
            dragLayout.getTopView().setBackgroundColor(bgColor);

        } else if (ratio <= 2) {
            speakerIv.setTranslationY(speakerIv_x/2 * (ratio-1));//向下平移
            speakerIv.setTranslationX(-(DEVICE_WIDTH/2 - speakerIv_x));//平移到中点
            //speakerIv.setScaleX(1.5f + 0.5f * (ratio-1));
            //speakerIv.setScaleY(1.5f + 0.5f * (ratio-1));
        } else if (ratio > 2 && ratio < 3) {

            //speakerIv.setTranslationX(-(DEVICE_WIDTH / 2 - speakerIv_x));//平移到中点
            //speakerIv.setScaleX(4 - ratio);//缩小
            //speakerIv.setScaleY(4 - ratio);
        }
    }

    private void showUser() {
        if (!speakerIvIsShown) {
            ViewPropertyAnimator.animate(speakerIv).cancel();
            ViewPropertyAnimator.animate(speakerIv).scaleX(1).scaleY(1).setDuration(200).start();
            speakerIvIsShown = true;
        }
    }

    private void hideUser() {
        if (speakerIvIsShown) {
            ViewPropertyAnimator.animate(speakerIv).cancel();
            ViewPropertyAnimator.animate(speakerIv).scaleX(0).scaleY(0).setDuration(200).start();
            speakerIvIsShown = false;
        }
    }

    private void initParamData() {
    }


    //--fill content
    private void fillContent(){

        //设置ImageView大小
        //ViewGroup.LayoutParams param = ivBgImage.getLayoutParams();
        //param.height=BaseUi.DEVICE_WIDTH;
        //param.width =BaseUi.DEVICE_WIDTH;
        //ivBgImage.setLayoutParams(param);
        /////Log.d("l_11", "yes");
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_speak_comment_buttonFloat:
                btnFloat.setVisibility(View.GONE);
                MaterialEditText editText = (MaterialEditText) findViewById(R.id.ui_speak_comment_editText);
                BaseDevice.showSoftInput(getContext(), editText);
                break;
            case R.id.tpl_list_speak_iv_bg:
                UiImageZoom.actionStart(getContext(), C.web.bgimage + bgImageUrl + ".jpg", null);
                break;

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // async task callback methods

    @SuppressWarnings("unchecked")
    @Override
    public void onTaskComplete(int taskId, BaseMessage message) {
        super.onTaskComplete(taskId, message);
        switch (taskId) {
            case C.task.commentAllList:
                try {
                    commentList = (ArrayList<Comment>) message.getResultList("Comment");
                    getLastId(commentList);
                    setMyAdapter();
                    commentAdapter.notifyDataSetChanged();// 通知listView刷新数据
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;
            case C.task.commentListMore:

                try {
                    //@SuppressWarnings("unchecked")
                    ArrayList<Comment> commentList1 = (ArrayList<Comment>) message.getResultList("Comment");
                    getLastId(commentList1);
                    commentList.addAll(commentList1);
                    commentAdapter.notifyDataSetChanged();// 通知listView刷新数据
                    //setMyAdapter();
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;
            case C.task.fansAdd:
                if (message.getCode().equals("10000")) {
                    toast("Add fans ok");
                    // refresh customer data
                    HashMap<String, String> cvParams = new HashMap<String, String>();
                    //cvParams.put("customerId", customerId);
                    this.doTaskAsync(C.task.customerView, C.api.customerView, cvParams, true);
                } else {
                    toast("Add fans fail");
                }
                break;

        }
    }

    @Override
    public void onNetworkError(int taskId) {
        super.onNetworkError(taskId);
        moreView_tv.setVisibility(View.VISIBLE);
        moreView_pg.setVisibility(View.GONE);
        moreView_tv.setText(getString(R.string.click_refresh));
        moreView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentList == null) {
                    loadData();
                } else {
                    loadMoreData();
                }
                moreView_pg.setVisibility(View.VISIBLE);
                moreView_tv.setText(getString(R.string.loading));
            }
        });
    }

    @Override
    protected void hideProgressBar() {

    }

    @Override
    protected void showProgressBar() {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // inner classes

    private class BlogHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RESIZE: {
                    if (msg.arg1 == BIGGER) {//inputMode is hidding
                        editLayout.setVisibility(View.GONE);
                        btnFloat.setVisibility(View.VISIBLE);
                        dragLayout.invalidate();
                    } else {
                        btnFloat.setVisibility(View.GONE);
                        editLayout.setVisibility(View.VISIBLE);
                        dragLayout.invalidate();
                    }
                }
                break;
                default:
                    break;
            }
        }
    }

    //获取最后一条数据的ID
    public String getLastId(ArrayList<Comment> list){
        int i = list.size();
        Comment j = list.get(i-1);
        lastId = j.getId();
        lastIdNum =Integer.parseInt(lastId);
        Log.e("id",lastId);
        return lastId;
    }

    //加载更多数据
    public void loadMoreData() {
        // TODO Auto-generated method stub
        if(lastIdNum == 1){
            list.removeFooterView(moreView);
            Toast.makeText(getContext(), "加载完成！", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> blogParams = new HashMap<String, String>();
            blogParams.put("lastId", lastId);
            this.doTaskAsync(C.task.commentListMore, C.api.commentAllList, blogParams, false);
        }
    }

    //****load data
    private void loadData(){
        HashMap<String, String> commentParams = new HashMap<String, String>();
        commentParams.put("id", speakId);
        commentParams.put("pageId", "0");
        this.doTaskAsync(C.task.commentAllList, C.api.commentAllList, true);
    }

    @Override// 滑到底部后自动loadData
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex >= commentAdapter.getCount()) {
            moreView_tv.setVisibility(View.VISIBLE);
            moreView_pg.setVisibility(View.VISIBLE);
            moreView_tv.setText(getString(R.string.loading));
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadMoreData();
                }
            });
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
        EventBus.getDefault().post(AttachUtil.isAdapterViewAttach(view));
    }

    // Handle scroll event from fragments
    public void onEvent(Boolean b){
        dragLayout.setTouchMode(b);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
}
