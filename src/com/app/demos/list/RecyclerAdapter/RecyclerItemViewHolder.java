package com.app.demos.list.RecyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;

/**
 * Created by tom on 15-4-29.
 */
public class RecyclerItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    //private final TextView mItemTextView;
    //private final TextView title;
    public final TextView content;
    //private final TextView user;
    //public TextView uptime;
    public final TextView likecount;
    public final TextView type;
    public final ImageView image;
    public final ImageButton ib;

    public RecyclerItemViewHolder(final View parent, TextView itemContent, TextView itemType, TextView itemLikecount,
                                  ImageView itemImage,
                                  ImageButton itemIb) {
        super(parent);
        content = itemContent;
        type = itemType;
        likecount = itemLikecount;
        image = itemImage;
        ib = itemIb;
        parent.setOnClickListener(this);
        ib.setOnClickListener(this);
    }

    public static RecyclerItemViewHolder newInstance(View parent) {
        TextView itemContent = (TextView) parent.findViewById(R.id.tpl_list_speak_tv_content);
        TextView itemType = (TextView) parent.findViewById(R.id.tpl_list_speak_tv_type);
        TextView itemLikecount = (TextView) parent.findViewById(R.id.tpl_list_speak_tv_like);
        ImageView itemImage = (ImageView) parent.findViewById(R.id.tpl_list_speak_iv_bg);
        ImageButton itemIb = (ImageButton) parent.findViewById(R.id.tpl_list_speak_ib_like);
        return new RecyclerItemViewHolder(parent, itemContent, itemType, itemLikecount, itemImage, itemIb);
    }

    public void setItemText(CharSequence text) {
        //mItemTextView.setText(text);
    }

    @Override
    public void onClick(View v) {
        if(v == itemView)
        {
            Toast.makeText(v.getContext(), "content", Toast.LENGTH_SHORT).show();
        }

        if(v == ib)
        {
            Toast.makeText(v.getContext(), "ib", Toast.LENGTH_SHORT).show();
        }


    }
}
