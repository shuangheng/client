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

import com.app.demos.model.Comment;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.MainActivity;
import com.app.demos.util.AppCache;
import com.app.demos.util.AppFilter;

public class CommentList extends BaseList {
	private Context ui;
	private LayoutInflater inflater;
	private ArrayList<Comment> commentList;
	private int resourceId;
	
	public final class CommentListItem {
		public ImageView face;		
		public TextView content;        
        public TextView type;
        public TextView reply;
	}
        
	
	public CommentList (Context context,int resourceId, ArrayList<Comment> commentList) {
		this.ui = context;
		this.resourceId =resourceId;
		this.inflater = LayoutInflater.from(this.ui);
		this.commentList = commentList;
	}
	
	@Override
	public int getCount() {
		return commentList.size();
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
		Log.e("faceimage", "faceUrl+3");
		// init tpl
		CommentListItem  commentItem = null;
		// if cached expired
		if (v == null) {
			v = inflater.inflate(resourceId, null);
			commentItem = new CommentListItem();
			commentItem.face = (ImageView) v.findViewById(R.id.tpl_list_comment_ivFace);
			commentItem.content = (TextView) v.findViewById(R.id.tpl_list_comment_tvContent);            
			commentItem.type = (TextView) v.findViewById(R.id.tpl_list_comment_tvType);
			commentItem.reply = (TextView) v.findViewById(R.id.tpl_list_comment_tvReply);

			v.setTag(commentItem);
		} else {
			commentItem = (CommentListItem) v.getTag();
		}

		
		commentItem.content.setText(AppFilter.getHtml(commentList.get(p).getContent()));

        //commentItem.type.setText(commentList.get(p).getType());

        commentItem.reply.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View v) {
        		
        		Log.d("ibutton", "yes");        		
        	}
        });
        

		/*/ load face image

		String faceUrl = commentList.get(p).getFace();
//        Log.e("bgimage", bgUrl);
        Bitmap faceImage = AppCache.getImage(faceUrl);
        
        
        if (faceImage != null) {
            Log.e("faceimage", faceUrl+3);
			commentItem.face.setImageBitmap(faceImage);
            Log.e("bgimage", faceUrl+4);
		}
		*/
		return v;
	}
}
