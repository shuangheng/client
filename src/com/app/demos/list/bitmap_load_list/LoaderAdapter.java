package com.app.demos.list.bitmap_load_list;

import com.app.demos.R;
import com.app.demos.base.BaseUi;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.MainActivity;
import com.app.demos.util.AppFilter;
import com.app.demos.util.BaseDevice;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by tom on 15-4-19.
 * copy from " https://github.com/shuangheng/SyncLoaderBitmapDemo/ "
 */

public class LoaderAdapter extends BaseAdapter{

    private static final String TAG = "LoaderAdapter";
    private boolean mBusy = false;
    int weight ;

    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }


    private ImageLoader mImageLoader;
    private int mCount;
    private Context mContext;
    private String[] urlArrays;
    private LayoutInflater inflater;
    private ArrayList<Gonggao> gonggaoList;
    private int resourceId;


    public LoaderAdapter(int count, Context context, String []url) {
        this.mCount = count;
        this.mContext = context;
        urlArrays = url;
        mImageLoader = new ImageLoader(context, "LoaderAdapter");
    }


    public ImageLoader getImageLoader(){
        return mImageLoader;
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
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            //convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
            convertView = LayoutInflater.from(mContext).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.image = (ImageView) convertView.findViewById(R.id.tpl_list_speak_iv_bg);
            //blogItem.face = (ImageView) v.findViewById(R.id.tpl_list_blog_image_face);
//			blogItem.title = (TextView) v.findViewById(R.id.tpl_list_blog_text_title);
//			blogItem.user = (TextView) v.findViewById(R.id.tpl_list_blog_text_user);
            viewHolder.content = (TextView) convertView.findViewById(R.id.tpl_list_speak_tv_content);
//			blogItem.uptime = (TextView) v.findViewById(R.id.tpl_list_blog_text_comment);
            viewHolder.likecount = (TextView) convertView.findViewById(R.id.tpl_list_speak_tv_like);
            viewHolder.type = (TextView) convertView.findViewById(R.id.tpl_list_speak_tv_type);
            viewHolder.ib = (ImageButton) convertView.findViewById(R.id.tpl_list_speak_ib_like);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.content.setText(AppFilter.getHtml(gonggaoList.get(position).getContent()));
//		blogItem.user.setText(AppFilter.getHtml(gonggaoList.get(p).getUser()));
        viewHolder.likecount.setText(gonggaoList.get(position).getLikeCount());
        viewHolder.type.setText(gonggaoList.get(position).getTypeAll());
//        blogItem.content.setBackgroundResource(Suiji.randomNumber(1,29));
        viewHolder.ib.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //blogItem.ib.setImageResource(R.drawable.ic_card_liked);
                Log.d("ibutton", "yes");

                v.setBackgroundResource(new MainActivity().likeButtonClick(mContext));
                //new TestFragment().likeButtonClick();
            }
        });


        //Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.newscar);

        int weight = BaseUi.DEVICE_WIDTH;
        ViewGroup.LayoutParams params = viewHolder.image.getLayoutParams();
        params.height=weight;
        params.width =weight;
        viewHolder.image.setLayoutParams(params);


        /*/viewHolder.image.setAdjustViewBounds(true);
        viewHolder.image.setMinimumHeight(720);
        if (weight <= 720) {
            viewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
            Log.e("", "inside");
        } else {
            viewHolder.image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.e("", "crop");

        }
        */

        // load face image
        String url = gonggaoList.get(position).getBgimage();

        viewHolder.image.setImageResource(R.drawable.ic_launcher);



        if (!mBusy) {
            mImageLoader.DisplayImage(url, viewHolder.image, false);
            //viewHolder.mTextView.setText("--" + position + "--IDLE ||TOUCH_SCROLL");
        } else {
            mImageLoader.DisplayImage(url, viewHolder.image, true);
            //viewHolder.mTextView.setText("--" + position + "--FLING");
        }
        return convertView;
    }

    static class ViewHolder {
        TextView mTextView;
        ImageView mImageView;
        public TextView title;
        public TextView content;
        public TextView user;
        public TextView uptime;
        public TextView likecount;
        public TextView type;
        public ImageView image;
        public ImageButton ib;
    }
}