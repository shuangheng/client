package com.app.demos.list.RecyclerAdapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.model.Gonggao;
import com.app.demos.ui.MainActivity;
import com.app.demos.util.AppFilter;

import java.util.ArrayList;
import java.util.List;

import static com.app.demos.util.Math_my.isEven;

/**
 * Created by tom on 15-4-29.
 */
public class SpeakRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //added view types
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 3;
    private boolean mBusy = false;
    private boolean isShowBottom = false;
    private boolean isEnd = false;
    private int positionn;


    public void setFlagBusy(boolean busy) {
        this.mBusy = busy;
    }
    public void setisShowBottom(boolean isShow) {
        this.isShowBottom = isShow;
    }
    public void setisEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }


    private ImageLoader mImageLoader;
    private int positonn;
    private Context mContext;
    private String[] urlArrays;
    private LayoutInflater inflater;
    private ArrayList<Gonggao> gonggaoList;

    public SpeakRecyclerAdapter(Context context, ArrayList<Gonggao> blogList) {
        mContext = context;
        gonggaoList = blogList;
        mImageLoader = new ImageLoader(context);
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);

        void onImageClick(int Position);

        void onFavoriteClick(View v, int Position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
    }

    public static interface OnRecyclerGetView {
        void onGetView();
    }

    private OnRecyclerGetView onRecyclerGetView;

    /**
     * get item view
     */
    public void getItemView (OnRecyclerGetView onRecyclerGetView) {
        this.onRecyclerGetView = onRecyclerGetView;
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
            final View view = LayoutInflater.from(context).inflate(R.layout.tpl_list_speak_8_27, parent, false);
            return new RecyclerItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            final View view = LayoutInflater.from(context).inflate(R.layout.tpl_list_speak_footer, parent, false);
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
            //positionn = position;
            Gonggao g = gonggaoList.get(position);
            holder.content.setText(AppFilter.getHtml(g.getContent()));

            holder.type.setText(g.getType());
            holder.extra.setText(g.getTypeAll());
            holder.likecount.setText(g.getLikeCount());

            //set background color
            holder.content.setTextColor(holder.content.getResources().getColor(!isEven(position) ? R.color.white : R.color.black));
            holder.ib.setBackgroundResource(!isEven(position) ? R.drawable.ic_card_like : R.drawable.ic_card_like_grey);
            //if (g.getFavorite() != null) {
                if (g.getFavorite() != null && g.getFavorite().equals("0")) {
                    holder.ib.setBackgroundResource(R.drawable.ic_card_liked);
                }
           // }
            holder.containerLayout.setBackgroundColor(!isEven(position) ?
                            holder.containerLayout.getResources().getColor(C.colors[position % 16]) :
                            holder.containerLayout.getResources().getColor(R.color.white));

            /*
            int weight = BaseUi.DEVICE_WIDTH;
            ViewGroup.LayoutParams params = holder.image.getLayoutParams();
            params.height = weight;
            params.width = weight;
            holder.image.setLayoutParams(params);
            */

            // load face image
            String imagePath = gonggaoList.get(position).getBgimage();
            String url = C.web.thumb_image + imagePath + ".jpg";//thumb image path

            //url="http://10.0.2.2:8002/faces/default/l_25.jpg";
                holder.image.setVisibility(gonggaoList.get(position).getBgimage().equals("null") ? View.GONE : View.VISIBLE);
                holder.image.setImageResource(R.drawable.ic_launcher);

            mImageLoader.DisplayImage(url, holder.image, mBusy);

        } else {
            RecyclerFooterViewHolder footerHolder = (RecyclerFooterViewHolder) viewHolder;
            if (position > 4) {
                footerHolder.pg.setVisibility(View.VISIBLE);
                footerHolder.tv.setVisibility(View.VISIBLE);
                if (isEnd) {
                    footerHolder.pg.setVisibility(View.GONE);
                    footerHolder.tv.setText("点我发表");
                } else {
                    footerHolder.pg.setVisibility(View.VISIBLE);
                    footerHolder.tv.setText("正在加载···");
                }
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
            return TYPE_FOOTER;
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

    class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        //private final TextView mItemTextView;
        public final TextView extra;
        public final TextView content;
        //private final TextView user;
        public TextView seeAll;
        public final LinearLayout containerLayout;
        public final TextView likecount;
        public final TextView type;
        public final ImageView image;
        public final ImageView ib;

        public RecyclerItemViewHolder(View parent) {
            super(parent);
            content = (TextView) parent.findViewById(R.id.tpl_list_speak_tv_content);
            containerLayout = (LinearLayout) parent.findViewById(R.id.tpl_list_speak_container);
            type = (TextView) parent.findViewById(R.id.tpl_list_speak_tv_type);
            extra = (TextView) parent.findViewById(R.id.tpl_list_speak_speak_extra);
            seeAll = (TextView) parent.findViewById(R.id.tpl_list_speak_speak_all_content_tips);
            likecount = (TextView) parent.findViewById(R.id.tpl_list_speak_tv_like);
            image = (ImageView) parent.findViewById(R.id.tpl_list_speak_iv_bg);
            ib = (ImageView) parent.findViewById(R.id.tpl_list_speak_ib_like);
            parent.setOnClickListener(this);
            parent.setOnLongClickListener(this);
            ib.setOnClickListener(this);
            image.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == itemView) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onItemClick(getAdapterPosition());
                }
                Toast.makeText(v.getContext(), "content" + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                Bundle params = new Bundle();
            }
            if(v == ib) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onFavoriteClick(ib, getAdapterPosition());
                }
                Toast.makeText(v.getContext(), "ib", Toast.LENGTH_SHORT).show();
            }
            if (v == image) {
                if (null != onRecyclerViewListener) {
                    onRecyclerViewListener.onImageClick(getAdapterPosition());
                }
            }


        }

        @Override
        public boolean onLongClick(View v) {
            if(null != onRecyclerViewListener){
                return onRecyclerViewListener.onItemLongClick(getAdapterPosition());
            }
            return false;
        }
    }

    class RecyclerFooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pg;
        public TextView tv;
        public RecyclerFooterViewHolder( final View itemView) {
            super(itemView);
            pg = (ProgressBar) itemView.findViewById(R.id.list_speak_footer_progressbar);
            tv = (TextView) itemView.findViewById(R.id.list_speak_footer_hint_textview);
            if (!isEnd) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(itemView.getContext(), "more Click", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(itemView.getContext(), "end", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}