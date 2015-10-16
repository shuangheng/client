package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.layout.ButtonFloat;
import com.app.demos.layout.ResizeLayout;
import com.app.demos.layout.materialEditText.MaterialEditText;
import com.app.demos.list.CommentList;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.model.Comment;
import com.app.demos.model.Customer;
//import com.app.demos.ui.UiBlog.BlogHandler;
import com.app.demos.model.Gonggao;
import com.app.demos.util.AppFilter;
import com.app.demos.util.ImmUtil;
import com.app.demos.util.UIUtil;

import static com.app.demos.util.Math_my.isEven;

public class UiSpeakComment extends BaseUi implements OnScrollListener, OnClickListener {
    private String speakId;
    private String customerId;
    private String content;
    private String type;
    private String likeCount;
    private String bgImageUrl;

    private Toolbar toolbar;
    private TextView tvContent;
    private TextView tvType;
    private TextView tvLikeCount;
    private TextView extra;
    private Button careBtn;
    private Button commentBtn;
    private ImageView ivBgImage;
    private ListView list;
    private ScrollView scroll;
    private LinearLayout layout;

    private ArrayList<Comment> commentList;
    private CommentList commentAdapter;
    private View moreView;
    private View tplSpeak;
    private ImageButton ibLike;
    private int lastVisibleIndex;
    private int lastIdNum;
    private String lastId;
    private String commentcount;

    private String bgColor;
    private LinearLayout containerLayout;
    private String favorite;
    private int toolcolor;

    private static final int BIGGER = 1;//about InputMode
    private static final int SMALLER = 2;
    private static final int MSG_RESIZE = 1;
    private static final int HEIGHT_THREADHOLD = 30;
    private ResizeLayout reSizelayout;

    private ButtonFloat btnFloat;
    private LinearLayout editLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_speak_comment);

        // set handler
        this.setHandler(new BlogHandler(this));

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
        loadData();

    }

    public static void actionStart(Context context,Gonggao g, String bgColor) {
        Intent intent = new Intent( context, UiSpeakComment.class);
        intent.putExtra("Gonggao", g);
        intent.putExtra("bgColor", bgColor);
        context.startActivity(intent);
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
                    //@SuppressWarnings("unchecked")
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
                    cvParams.put("customerId", customerId);
                    this.doTaskAsync(C.task.customerView, C.api.customerView, cvParams, true);
                } else {
                    toast("Add fans fail");
                }
                break;
            case C.task.customerView:
                try {
                    // update customer info
                    final Customer customer = (Customer) message.getResult("Customer");
                    TextView textInfo = (TextView) this.findViewById(R.id.app_blog_text_customer_info);
                    textInfo.setText(UIUtil.getCustomerInfo(this, customer));
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getMessage());
                }
                break;
        }
    }

    //设置默认List adapter
    public void setMyAdapter(){
        //commentList =  new ArrayList<Comment>();
        //Comment comment = new Comment("ff", "ffff");
        //commentList.add(comment);
        commentAdapter = new CommentList(this,R.layout.tpl_list_speak_comment, commentList);
        list.setAdapter(commentAdapter);


    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    // other methods
    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());// 标题的文字需在setSupportActionBar之前，不然会无效
        if (toolcolor != android.R.color.white) {
            toolbar.setBackgroundColor(toolcolor);
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
            new ImageLoader_my(this, "image").DisplayImage(C.web.bgimage + bgImageUrl + ".jpg", ivBgImage, false, true);
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
        reSizelayout = (ResizeLayout) findViewById(R.id.ui_speak_comment_ResizeLayout);
        moreView = getLayoutInflater().inflate(R.layout.load_more, null);
        tplSpeak = getLayoutInflater().inflate(R.layout.tpl_list_speak_comment_header, null);
        layout = (LinearLayout) tplSpeak.findViewById(R.id.tpl_list_speak_bottom_layout);
        containerLayout = (LinearLayout) tplSpeak.findViewById(R.id.tpl_list_speak_container);
        tvContent = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_content);
        tvType = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_type);
        extra = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_speak_extra);
        tvLikeCount = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_like);
        ivBgImage = (ImageView) tplSpeak.findViewById(R.id.tpl_list_speak_iv_bg);
        ibLike = (ImageButton) tplSpeak.findViewById(R.id.tpl_list_speak_ib_like);

        btnFloat = (ButtonFloat) findViewById(R.id.ui_speak_comment_buttonFloat);
        editLayout = (LinearLayout) findViewById(R.id.ui_speak_comment_editLayout);

        reSizelayout.setOnResizeListener(new ResizeLayout.OnResizeListener() {
            @Override
            public void OnResize(int w, int h, int oldw, int oldh) {
                int change = BIGGER;
                if (h < oldh) {
                    change = SMALLER;
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

    private void initParamData() {
    }


    //--fill content
    private void fillContent(){
        Bundle params = this.getIntent().getExtras();
        Gonggao g = getIntent().getParcelableExtra("Gonggao");
        speakId = g.getId();
        content = g.getContent();
        type = g.getType();
        commentcount = g.getCommentcount();
        likeCount = g.getLikeCount();
        bgImageUrl = g.getBgimage();
        favorite = g.getFavorite();
        bgColor = params.getString("bgColor");

        int position = Integer.parseInt(bgColor);//bg color
        toolcolor = !isEven(position) ? getResources().getColor(C.colors[position % 16]) :
                getResources().getColor(android.R.color.white);
        containerLayout.setBackgroundColor(toolcolor);

        tvContent.setText(AppFilter.getHtml(content));
        tvType.setText(type);
        extra.setText("评论 " + commentcount);
        tvLikeCount.setText(likeCount);
        ivBgImage.setBackgroundColor(getResources().getColor(R.color.white));

        if (favorite != null && favorite.equals("0")) {
            ibLike.setBackgroundResource(R.drawable.ic_card_liked);
        }
        //设置ImageView大小
        //ViewGroup.LayoutParams param = ivBgImage.getLayoutParams();
        //param.height=BaseUi.DEVICE_WIDTH;
        //param.width =BaseUi.DEVICE_WIDTH;
        //ivBgImage.setLayoutParams(param);
        /////Log.d("l_11", "yes");
    }

    // 滑到底部后自动loadData
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex >= commentAdapter.getCount()) {
            moreView.setVisibility(View.VISIBLE);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    loadMoreData();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_speak_comment_buttonFloat:
                editLayout.setVisibility(View.VISIBLE);
                MaterialEditText editText = (MaterialEditText) findViewById(R.id.ui_speak_comment_editText);
                //editText.requestFocus();
                ImmUtil.showInput2(UiSpeakComment.this, editText);

                break;
            case R.id.tpl_list_speak_iv_bg:
                UiImageZoom.actionStart(getContext(), C.web.bgimage + bgImageUrl + ".jpg", null);
                break;

        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // inner classes

    private class BlogHandler extends BaseHandler {
        public BlogHandler(BaseUi ui) {
            super(ui);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_RESIZE: {
                    if (msg.arg1 == BIGGER) {//inputMode is hidding
                        btnFloat.setVisibility(View.VISIBLE);
                        editLayout.setVisibility(View.GONE);
                    } else {
                        btnFloat.setVisibility(View.GONE);
                        editLayout.setVisibility(View.VISIBLE);
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

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
    }
}
