package com.app.demos.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Message;
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
import com.app.demos.list.CommentList;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.model.Comment;
import com.app.demos.model.Customer;
//import com.app.demos.ui.UiBlog.BlogHandler;
import com.app.demos.util.AppFilter;
import com.app.demos.util.UIUtil;

import static com.app.demos.util.Math_my.isEven;

public class UiSpeakComment extends BaseUi implements OnScrollListener{
    private String speakId;
    private String customerId;
    private String content;
    private String type;
    private String likeCount;
    private String bgImageUrl;

    private TextView tvContent;
    private TextView tvType;
    private TextView tvLikeCount;
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
    private TextView extra;
    private String bgColor;
    private LinearLayout containerLayout;
    private String favorite;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_speak_comment);

        // set handler
        this.setHandler(new BlogHandler(this));
        layout = (LinearLayout) this.findViewById(R.id.tpl_list_speak_bottom_layout);
        //--fill content
        fillContent();

        //**load bgImage
        loadBgImage();

		/*/ do add care
		//careBtn = (Button) this.findViewById(R.id.);
		//careBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// prepare blog data
				HashMap<String, String> urlParams = new HashMap<String, String>();
				urlParams.put("customerId", customerId);
				doTaskAsync(C.task.fansAdd, C.api.fansAdd, urlParams);
			}
		});

		// do add comment
		commentBtn = (Button) this.findViewById(R.id.app_blog_btn_comment);
		commentBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle data = new Bundle();
				data.putInt("action", C.action.edittext.COMMENT);
				data.putString("blogId", blogId);
				doEditText(data);
			}
		});

		// prepare speak data
		HashMap<String, String> blogParams = new HashMap<String, String>();
		blogParams.put("blogId", blogId);
		this.doTaskAsync(C.task.blogView, C.api.blogView, blogParams);
		*/
    }

    @Override
    public void onStart () {
        super.onStart();
        loadData();

		/*
		commentList =  new ArrayList<Comment>();
		for(int i = 0;i<110;i++){
			Comment c = new Comment();
			c.setContent(""+i);
			commentList.add(c);

		}
		setMyAdapter();
		*/
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
                    //commentAdapter.notifyDataSetChanged();// 通知listView刷新数据
                    setMyAdapter();
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
                    this.doTaskAsync(C.task.customerView, C.api.customerView, cvParams);
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
        list = (ListView) this.findViewById(R.id.ui_speak_comment_listview);
        commentAdapter = new CommentList(this,R.layout.tpl_list_speak_comment, commentList);
        list.addHeaderView(tplSpeak);
        list.addFooterView(moreView);
        list.setAdapter(commentAdapter);
        list.setOnScrollListener(this);
        list.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int postion, long id) {

            }
        });

    }

    @Override
    public void onNetworkError (int taskId) {
        super.onNetworkError(taskId);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // other methods

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            doFinish();
        }
        return super.onKeyDown(keyCode, event);
    }

    //--load bgImage
    private void loadBgImage(){
        if (!bgImageUrl.equals("null")) {
            ivBgImage.setVisibility(View.VISIBLE);
            new ImageLoader_my(this).DisplayImage(C.web.bgimage + bgImageUrl + ".jpg", ivBgImage, false, true);
        }
    }

    //--fill content
    private void fillContent(){
        Bundle params = this.getIntent().getExtras();
        speakId = params.getString("speakId");
        content = params.getString("content");
        type = params.getString("type");
        commentcount = params.getString("commentcount");
        likeCount = params.getString("likeCount");
        bgImageUrl = params.getString("bgImageUrl");
        favorite = params.getString("favorite");
        bgColor = params.getString("bgColor");

        moreView = getLayoutInflater().inflate(R.layout.load_more, null);
        tplSpeak = getLayoutInflater().inflate(R.layout.tpl_list_speak_comment_header, null);

        // fill content
        layout = (LinearLayout) tplSpeak.findViewById(R.id.tpl_list_speak_bottom_layout);
        containerLayout = (LinearLayout) tplSpeak.findViewById(R.id.tpl_list_speak_container);
        tvContent = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_content);
        tvType = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_type);
        extra = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_speak_extra);
        tvLikeCount = (TextView) tplSpeak.findViewById(R.id.tpl_list_speak_tv_like);
        ivBgImage = (ImageView) tplSpeak.findViewById(R.id.tpl_list_speak_iv_bg);
        ibLike = (ImageButton) tplSpeak.findViewById(R.id.tpl_list_speak_ib_like);
        //mlayout = getLayout(R.id.tpl_list_speak_bottom_layout);
        ivBgImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle params = new Bundle();
                //params.putString("bgImageUrl", bgImageUrl);
                //overlay(UiImageZoom.class, params);
                UiImageZoom.actionStart(getContext(), C.web.bgimage + bgImageUrl + ".jpg", null);
            }
        });

        if (favorite.equals("0")) {
            ibLike.setBackgroundResource(R.drawable.ic_card_liked);
        }
        ibLike.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //blogItem.ib.setImageResource(R.drawable.ic_card_liked);
                Log.d("ibutton", "yes");

                v.setBackgroundResource(new MainActivity().likeButtonClick(getContext()));
                //new TestFragment().likeButtonClick();
            }
        });

        int position = Integer.parseInt(bgColor);
        containerLayout.setBackgroundColor(!isEven(position) ?
                containerLayout.getResources().getColor(C.colors[position % 16]) :
                containerLayout.getResources().getColor(R.color.white));

        tvContent.setText(AppFilter.getHtml(content));
        tvType.setText(type);
        extra.setText("评论 " + commentcount);
        tvLikeCount.setText(likeCount);
        ivBgImage.setBackgroundColor(getResources().getColor(R.color.white));
        //设置ImageView大小
        ViewGroup.LayoutParams param = ivBgImage.getLayoutParams();
        param.height=BaseUi.DEVICE_WIDTH;
        param.width =BaseUi.DEVICE_WIDTH;
        ivBgImage.setLayoutParams(param);
        /////Log.d("l_11", "yes");
    }

    // 滑到底部后自动loadData
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // 滑到底部后自动加载，判断listview已经停止滚动
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && lastVisibleIndex >= commentAdapter.getCount()) {
            Log.e("ScrollView","bottom");
            moreView.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadMoreData();
                }
            }, 2000);
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
            try {
                switch (msg.what) {
                    case BaseTask.LOAD_IMAGE:
                        //Bitmap face = AppCache.getImage(faceImageUrl);
                        //faceImage.setImageBitmap(face);

                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ui.toast(e.getMessage());
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
            //blogParams.put("typeId", "0");
            //blogParams.put("pageId", "0");
            this.doTaskAsync(C.task.commentListMore, C.api.commentAllList, blogParams);
        }
    }

    //****load data
    private void loadData(){
        // prepare comment data
        HashMap<String, String> commentParams = new HashMap<String, String>();
        commentParams.put("id", speakId);
        commentParams.put("pageId", "0");
        //this.doTaskAsync(C.task.commentList, C.api.commentList, commentParams);
        this.doTaskAsync(C.task.commentAllList, C.api.commentAllList);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stub
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;
    }
}
