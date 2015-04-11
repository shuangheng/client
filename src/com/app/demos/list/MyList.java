package com.app.demos.list;

import java.util.ArrayList;
import com.app.demos.R;
import com.app.demos.base.BaseDevice;
import com.app.demos.base.BaseUi;
import com.app.demos.base.BaseList;
import com.app.demos.fragment.SpeakFragment;
import com.app.demos.model.Blogg;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.MainActivity;
import com.app.demos.util.AppCache;
import com.app.demos.util.AppFilter;
import com.app.demos.util.IOUtil;
import com.app.demos.util.Suiji;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MyList extends BaseList {

	private Context ui;
	private LayoutInflater inflater;
	private ArrayList<Gonggao> gonggaoList;
	private int resourceId;
	
	public final class BlogListItem {
		//public ImageView face;
		public TextView title;
		public TextView content;
		public TextView user;
		public TextView uptime;
        public TextView likecount;
        public TextView type;
        public ImageView image;
        public ImageButton ib;
	}
	
	public MyList (Context context,int resourceId, ArrayList<Gonggao> blogList) {
		this.ui = context;
		this.resourceId =resourceId;
		this.inflater = LayoutInflater.from(this.ui);
		this.gonggaoList = blogList;
	}
	
	@Override
	public int getCount() {
		return gonggaoList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int p, View v, ViewGroup parent) {
		// init tpl
		BlogListItem  blogItem = null;
		// if cached expired
		if (v == null) {
			v = inflater.inflate(resourceId, null);
			blogItem = new BlogListItem();
            blogItem.image = (ImageView) v.findViewById(R.id.tpl_list_speak_iv_bg);
			//blogItem.face = (ImageView) v.findViewById(R.id.tpl_list_blog_image_face);
//			blogItem.title = (TextView) v.findViewById(R.id.tpl_list_blog_text_title);
//			blogItem.user = (TextView) v.findViewById(R.id.tpl_list_blog_text_user);
			blogItem.content = (TextView) v.findViewById(R.id.tpl_list_speak_tv_content);
//			blogItem.uptime = (TextView) v.findViewById(R.id.tpl_list_blog_text_comment);
            blogItem.likecount = (TextView) v.findViewById(R.id.tpl_list_speak_tv_like);
            blogItem.type = (TextView) v.findViewById(R.id.tpl_list_speak_tv_type);
            blogItem.ib = (ImageButton) v.findViewById(R.id.tpl_list_speak_ib_like);

			v.setTag(blogItem);
		} else {
			blogItem = (BlogListItem) v.getTag();
		}
//        blogItem.content.setHeight(MainActivity.getScreenWidth(ui));
		// fill data
//		blogItem.uptime.setText(gonggaoList.get(p).getUptime().substring(0, 10));
		// fill html data
		//blogItem.title.setText(AppFilter.getHtml(gonggaoList.get(p).getTitle()));
//		blogItem.content.setText(String.valueOf(MainActivity.getScreenWidth(ui)));
        blogItem.content.setText(AppFilter.getHtml(gonggaoList.get(p).getContent()));
//		blogItem.user.setText(AppFilter.getHtml(gonggaoList.get(p).getUser()));
        blogItem.likecount.setText(gonggaoList.get(p).getLikeCount());
        blogItem.type.setText(gonggaoList.get(p).getTypeAll());
//        blogItem.content.setBackgroundResource(Suiji.randomNumber(1,29));
        blogItem.ib.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
        		//blogItem.ib.setImageResource(R.drawable.ic_card_liked);
        		Log.d("ibutton", "yes");
        		
        		v.setBackgroundResource(new MainActivity().likeButtonClick(ui));
        		//new TestFragment().likeButtonClick();
        	}
        });
        

		// load face image

		String bgUrl = gonggaoList.get(p).getBgimage();
//        Log.e("bgimage", bgUrl);
//        int weidth = BaseDevice.getScreenWidth(ui);
//		Bitmap bgImage = Bitmap.createScaledBitmap(AppCache.
//                        getImage(bgUrl),weidth , weidth, true);
//
        Bitmap bgImage = AppCache.getImage(bgUrl);
        // it's OK
        //Bitmap bgImage = AppCache.getCachedImage(blogItem.image.getContext(),bgUrl);
        if (bgImage != null) {
        	int w = bgImage.getWidth();
            int h = bgImage.getHeight();

            Log.e("bgimage", ""+w+":"+h);
            
			blogItem.image.setImageBitmap(bgImage);
            
		}
		return v;
	}
}