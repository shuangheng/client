package com.app.demos.list.RecyclerAdapter;

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
    //added view types
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private boolean mBusy = false;


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
        Context context = parent.getContext();
        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(context).inflate(R.layout.tpl_list_speak, parent, false);
            return RecyclerItemViewHolder.newInstance(view);
        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.load_more, parent, false);
            return new RecyclerFooterViewHolder(view);
        }
        throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    /**
     * 这个方法主要用于适配渲染数据到View中。方法提供给你了一个viewHolder，而不是原来的convertView。
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (!isPositionFooter(position)) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            //String itemText = mItemList.get(position);
            holder.content.setText(AppFilter.getHtml(gonggaoList.get(position).getContent()));
            holder.type.setText(gonggaoList.get(position).getTypeAll());
            holder.likecount.setText(gonggaoList.get(position).getLikeCount());


            int weight = BaseUi.DEVICE_WIDTH;
            ViewGroup.LayoutParams params = holder.image.getLayoutParams();
            params.height = weight;
            params.width = weight;
            holder.image.setLayoutParams(params);

            // load face image
            String url = gonggaoList.get(position).getBgimage();
            holder.image.setImageResource(R.drawable.ic_launcher);
            //url="http://10.0.2.2:8002/faces/default/l_25.jpg";
            if (!mBusy) {
            mImageLoader.DisplayImage(url, holder.image, false);
            //viewHolder.mTextView.setText("--" + position + "--IDLE ||TOUCH_SCROLL");
            } else {
             mImageLoader.DisplayImage(url, holder.image, true);
            //viewHolder.mTextView.setText("--" + position + "--FLING");
             }
        }
    }

    //our old getItemCount()
    public int getBasicItemCount() {
        return gonggaoList == null ? 0 : gonggaoList.size();
    }
    //our new getItemCount() that includes header View
    @Override
    public int getItemCount() {
        return getBasicItemCount() + 1; // header
    }
    //added a method that returns viewType for a given position
    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }
    //added a method to check if given position is a header
    private boolean isPositionFooter (int position) {
        return position == getBasicItemCount();
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

}