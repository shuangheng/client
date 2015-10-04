package com.app.demos.layout.materialEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.app.demos.R;

/**
 * Created by tom on 15-9-29.
 */
public class AutoCompleteEditText extends AutoCompleteTextView {
    private String color = "#009688";//email textcolor
    boolean rs = false;
    private final String[] emailSuffix = { "@<font color=\""+color+"\">qq</font>.com",
            "@<font color=\""+color+"\">163</font>.com",
            "@<font color=\""+color+"\">126</font>.com",
            "@<font color=\""+color+"\">gmail</font>.com",
            "@<font color=\""+color+"\">sina</font>.com",
            "@<font color=\""+color+"\">aliyun</font>.com",
            "@<font color=\""+color+"\">hotmail</font>.com",
            "@<font color=\""+color+"\">outlook</font>.com",
            "@<font color=\""+color+"\">yahoo</font>.cn",
            "@<font color=\""+color+"\">sohu</font>.com",
            "@<font color=\""+color+"\">foxmail</font>.com",
            "@<font color=\""+color+"\">139</font>.com",
            "@<font color=\""+color+"\">189</font>.com",
            "@<font color=\""+color+"\">tom</font>.com",
            "@<font color=\""+color+"\">21cn</font>.com",
            "@<font color=\""+color+"\">21cn</font>.net",
            "@<font color=\""+color+"\">vip.21cn</font>.net",
            "@<font color=\""+color+"\">vip.21cn</font>.com",
            "@<font color=\""+color+"\">vip.qq</font>.com",
            "@<font color=\""+color+"\">vip.163</font>.com",
            "@<font color=\""+color+"\">vip.sina</font>.com"};
    public AutoCompleteEditText(Context context){
        super(context);
        init(context);
    }
    public AutoCompleteEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoCompleteEditText(Context context, AttributeSet attrs,int style) {
        super(context, attrs, style);
        init(context);
    }

    private void init(Context context){
        final MyAdatper adapter = new MyAdatper(context);
        setAdapter(adapter);
        addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                adapter.mList.clear();
                if (input.length() > 0) {
                    Pattern pat = Pattern.compile("@");
                    Matcher mat = pat.matcher(input);//判断if have “@”
                    rs = mat.find();
                    for (int i = 0; i < emailSuffix.length; ++i) {
                        if (rs) {
                            String[] strings = input.split("@");
                            adapter.mList.add(strings[0] + emailSuffix[i]);
                        } else {
                            adapter.mList.add(input + emailSuffix[i]);
                        }
                    }
                }
                adapter.getFilter().filter(s);
            }
        });
        // default=2 当输入一个字符的时候就开始检测
        //setThreshold(1);
    }

    class MyAdatper extends BaseAdapter implements Filterable {
        List<String> mList;
        private Context mContext;
        private LayoutInflater inflater;
        private MyFilter mFilter;

        public MyAdatper(Context context, int resource) {
            mContext = context;
            mList = new ArrayList<String>();
        }

        public MyAdatper(Context context) {
            mContext = context;
            mList = new ArrayList<String>();
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList == null ? null : Html.fromHtml(mList.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(mContext).inflate(android.R.layout.simple_list_item_1, null);
            if (convertView == null) {

                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextSize(20);

                convertView = tv;
            }

            TextView txt = (TextView) convertView;
            txt.setText(Html.fromHtml(mList.get(position)));
            return txt;
        }

        @Override
        public Filter getFilter() {
            if (mFilter == null) {
                mFilter = new MyFilter();
            }
            return mFilter;
        }

        private class MyFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (mList == null) {
                    mList = new ArrayList<String>();
                }
                if (constraint == null || constraint.length() == 0) {
                    results.values = mList;
                    results.count = mList.size();
                } else {
                    String s = constraint.toString().toLowerCase();
                    List<String> list = new ArrayList<>();
                    for (String p: mList) {
                        if (Html.fromHtml(p).toString().startsWith(s)) {
                            list.add(p);
                        }
                    }
                    results.values = list;
                    results.count = list.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mList = (ArrayList<String>) results.values;
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }
}