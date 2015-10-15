package com.app.demos.list.RecyclerAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.C;
import com.app.demos.layout.other.CircleImageView;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.model.Find;
import com.app.demos.util.AppFilter;
import com.app.demos.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by tom on 15-5-13.
 */
public class FindRecycAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //added view types
    private static final int TYPE_HEADER = 2;
    private static final int TYPE_ITEM = 1;
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
    private ArrayList<Find> gonggaoList;

     final Html.ImageGetter imageGetter = new Html.ImageGetter() {

        public Drawable getDrawable(String source) {
            Drawable drawable=null;
            int rId=Integer.parseInt(source);
            drawable=mContext.getResources().getDrawable(rId);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            return drawable;
        }
    };

    public FindRecycAdapter(Context context, ArrayList<Find> blogList) {
        mContext = context;
        gonggaoList = blogList;
        mImageLoader = new ImageLoader(context, "image");
    }

    public static interface OnRecyclerViewListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }

    private OnRecyclerViewListener onRecyclerViewListener;

    public void setOnRecyclerViewListener(OnRecyclerViewListener onRecyclerViewListener) {
        this.onRecyclerViewListener = onRecyclerViewListener;
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
            final View view = LayoutInflater.from(context).inflate(R.layout.tpl_list_find_card, parent, false);
            return new RecyclerItemViewHolder(view);
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
            //positionn = position;
            Find f = gonggaoList.get(position);
            holder.where.setText(AppFilter.getHtml(f.getWhere()));
            holder.uptime.setText(TimeUtil.getDailyDate(f.getUptime()));
            //holder.clue.setText("线索:" + f.getClue_count());
            holder.clue.setText(f.getClue_count());
            //set holder.images

                String sb = "";
                String str = f.getLost_item().substring(2);//去掉是首位@@
                String item_summary = f.getSummary().substring(2);
                Log.e("null",str+"\n"+item_summary+f.getId());
                //String[] strArr = str.split("\\s+");
                String[] strArr = str.split("@@");
                String[] item_summaryArr = item_summary.split("@@");
            for (String s: strArr) {
                Log.e("", s + "\n");
            }

                if (strArr.length > 0 ) {
                    for (int i = 0; i < strArr.length; i++) {
                    //for (String sting : strArr) {
                        int w = Integer.parseInt(strArr[i]);
                        sb = sb + "<img src=\""+ C.find_imageIds[w]+"\" /> \t " + item_summaryArr[i] +"<p>";
                    }
                    holder.images.setText(Html.fromHtml(sb, new Html.ImageGetter() {

                        public Drawable getDrawable(String source) {
                            Drawable drawable=null;
                            int rId=Integer.parseInt(source);
                            drawable = mContext.getResources().getDrawable(rId);
                            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                            return drawable;
                        }
                    }, null));

                }

            /*
            final int t = R.drawable.s_4;
            final String sText1 = " 测试：\n<img src=\""+R.drawable.s_2+"\" />nbsp<p>\t<img src=\""+t+"\" />dggdgdggdgdgdhhgdhhhjjkl;;;<p><img src=\""+t+"\" />dggdgd";

            //int i = holder.images.getHeight();
            holder.images.setText(Html.fromHtml(sText1, new Html.ImageGetter() {

                public Drawable getDrawable(String source) {
                    Drawable drawable=null;
                    int rId=Integer.parseInt(source);
                    drawable=mContext.getResources().getDrawable(rId);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    return drawable;
                }
            }, null));
*/
            //mImageLoader.DisplayImage("http://127.0.0.1:8002/faces/default/face_2.png", imageViews[0], false);

            // load face image
            //String url = "http://pic004.cnblogs.com/news/201211/20121108_091749_1.jpg";
            //String url = gonggaoList.get(position).getFace();
            String url="http://10.0.2.2:8002/faces/default/l_25.jpg";
            if (!mBusy && url != null) {
                mImageLoader.DisplayImage(url, holder.image, false);
                //viewHolder.mTextView.setText("--" + position + "--IDLE ||TOUCH_SCROLL");
            } else {
                mImageLoader.DisplayImage(url, holder.image, true);
                //viewHolder.mTextView.setText("--" + position + "--FLING");
            }
        } else {
            RecyclerFooterViewHolder footerHolder = (RecyclerFooterViewHolder) viewHolder;

            if (isEnd) {
                footerHolder.pg.setVisibility(View.GONE);
                footerHolder.tv.setText("没有了！点击发表");
            } else {
                footerHolder.pg.setVisibility(View.VISIBLE);
                footerHolder.tv.setText("正在加载···");
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

    class RecyclerItemViewHolder extends RecyclerView.ViewHolder {
        //private final TextView mItemTextView;
        //private final TextView title;
        public final TextView where;
        //private final TextView user;
        public TextView uptime;
        public TextView images;
        public final TextView clue;
        public final CircleImageView image;

        public RecyclerItemViewHolder(View parent) {
            super(parent);
            where = (TextView) parent.findViewById(R.id.tpl_list_find_card_where);
            clue = (TextView) parent.findViewById(R.id.tpl_list_find_card_clue);
            uptime = (TextView) parent.findViewById(R.id.tpl_list_find_card_time);
            image = (CircleImageView) parent.findViewById(R.id.tpl_list_find_card_image_face);
            images = (TextView) parent.findViewById(R.id.tpl_list_find_card_images);


        }

    }

    class RecyclerFooterViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar pg;
        public TextView tv;
        public RecyclerFooterViewHolder( final View itemView) {
            super(itemView);
            pg = (ProgressBar) itemView.findViewById(R.id.load_more_pg);
            tv = (TextView) itemView.findViewById(R.id.load_more_tv);
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