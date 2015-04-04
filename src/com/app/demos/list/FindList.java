package com.app.demos.list;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseList;

import com.app.demos.model.Find;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.MainActivity;
import com.app.demos.util.AppCache;
import com.app.demos.util.AppFilter;

public class FindList extends BaseList{
	private Context ui;
	private LayoutInflater inflater;
	private ArrayList<Find> findList;
	private int resourceId;
	
	public final class BlogListItem {
		public TextView content;		
		public TextView uptime;        
        public ImageView image;
        
	}
	
	public FindList (Context context,int resourceId, ArrayList<Find> blogList) {
		this.ui = context;
		this.resourceId =resourceId;
		this.inflater = LayoutInflater.from(this.ui);
		this.findList = blogList;
	}
	
	@Override
	public int getCount() {
		return findList.size();
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
//            blogItem.image = (ImageView) v.findViewById(R.id.tpl_list_speak_iv_bg);			
			blogItem.content = (TextView) v.findViewById(R.id.tpl_list_find_tvContent);
			blogItem.uptime = (TextView) v.findViewById(R.id.tpl_list_find_tvUptime);
            
			v.setTag(blogItem);
		} else {
			blogItem = (BlogListItem) v.getTag();
		}

		blogItem.uptime.setText(findList.get(p).getUptime().substring(0, 10));		
        blogItem.content.setText(AppFilter.getHtml(findList.get(p).getContent()));

		/*/ load face image

		String bgUrl = findList.get(p).getBgimage();
//        Log.e("bgimage", bgUrl);
//        int weidth = BaseDevice.getScreenWidth(ui);
//		Bitmap bgImage = Bitmap.createScaledBitmap(AppCache.
//                        getImage(bgUrl),weidth , weidth, true);
//
        Bitmap bgImage = AppCache.getImage(bgUrl);
        int w = bgImage.getWidth();
        int h = bgImage.getHeight();

        Log.e("bgimage", ""+w+":"+h);
        if (bgImage != null) {
            Log.e("bgimage", bgUrl+3);
			blogItem.image1.setImageBitmap(bgImage);
            Log.e("bgimage", bgUrl+4);
		}
		*/
		return v;
	}
}
