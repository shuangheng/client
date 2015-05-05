package com.app.demos.list.RecyclerAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

/**
 * Created by tom on 15-5-4.
 */
public class RecyclerFooterViewHolder extends RecyclerView.ViewHolder {
    public RecyclerFooterViewHolder(final View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(itemView.getContext(),"item Click" , Toast.LENGTH_SHORT).show();

            }
        });
    }
}