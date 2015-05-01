package com.app.demos.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.demos.R;
import com.app.demos.base.BaseUi;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.MainActivity;
import com.app.demos.util.AppFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 15-4-29.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mItemList;
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

    public RecyclerAdapter(Context context, ArrayList<Gonggao> blogList) {
        gonggaoList = blogList;
        mImageLoader = new ImageLoader(context);
    }

    /**
     * 这个方法主要生成为每个Item inflater出一个View，但是该方法返回的是一个ViewHolder。
     * 方法是把View直接封装在ViewHolder中，然后我们面向的是ViewHolder这个实例，
     * 当然这个ViewHolder需要我们自己去编写。直接省去了当初的convertView.setTag(holder)和
     * convertView.getTag()这些繁琐的步骤。
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tpl_list_speak, parent, false);
        return RecyclerItemViewHolder.newInstance(view);
    }

    /**
     * 这个方法主要用于适配渲染数据到View中。方法提供给你了一个viewHolder，而不是原来的convertView。
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
        //String itemText = mItemList.get(position);
        holder.content.setText(AppFilter.getHtml(gonggaoList.get(position).getContent()));
        holder.type.setText(gonggaoList.get(position).getTypeAll());
        holder.likecount.setText(gonggaoList.get(position).getLikeCount());
        holder.ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //blogItem.ib.setImageResource(R.drawable.ic_card_liked);
                Log.d("ibutton", "yes");

                v.setBackgroundResource(new MainActivity().likeButtonClick(mContext));
                //new TestFragment().likeButtonClick();
            }
        });

        int weight = BaseUi.DEVICE_WIDTH;
        ViewGroup.LayoutParams params = holder.image.getLayoutParams();
        params.height=weight;
        params.width =weight;
        holder.image.setLayoutParams(params);

        // load face image
        String url = gonggaoList.get(position).getBgimage();
        holder.image.setImageResource(R.drawable.ic_launcher);
        //url="http://10.0.2.2:8002/faces/default/l_25.jpg";
        //if (!mBusy) {
        mImageLoader.DisplayImage(url, holder.image, false);
            //viewHolder.mTextView.setText("--" + position + "--IDLE ||TOUCH_SCROLL");
        //} else {
           // mImageLoader.DisplayImage(url, holder.image, true);
            //viewHolder.mTextView.setText("--" + position + "--FLING");
       // }
    }

    @Override
    public int getItemCount() {
        return gonggaoList == null ? 0 : gonggaoList.size();
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

}