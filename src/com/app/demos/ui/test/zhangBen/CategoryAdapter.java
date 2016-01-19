package com.app.demos.ui.test.zhangBen;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.demos.R;

import java.util.List;


public class CategoryAdapter extends BaseAdapter {

    private List<CategoryModle> data;

    private LayoutInflater inflater;

    private int size=0;

    public CategoryAdapter(Context context, List<CategoryModle> list) {
        this.inflater=LayoutInflater.from(context);
        this.data=list;
        this.size=list.size();
    }

    public List<CategoryModle> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return this.size;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CategoryModle category=data.get(position);
        ViewHolder viewHolder=null;
        if(convertView == null) {
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.test_ui_zhangben_category_item, null);
            viewHolder.iv_category =(ImageView)convertView.findViewById(R.id.category_iv);
            viewHolder.tv_category =(TextView)convertView.findViewById(R.id.category_tv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        if(TextUtils.isEmpty(category.getCharacter())) {
            convertView.setBackgroundDrawable(null);
            viewHolder.iv_category.setImageDrawable(null);
            viewHolder.tv_category.setText("");
        } else {
            viewHolder.iv_category.setTag(category);
            viewHolder.iv_category.setImageResource(category.getId());
            viewHolder.tv_category.setText(category.getCharacter());
        }

        return convertView;
    }

    class ViewHolder {

        public ImageView iv_category;
        public TextView tv_category;
    }
}