package com.app.demos.ui.test.zhangBen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseList;
import com.app.demos.base.BaseUi;
import com.app.demos.model.Blogg;
import com.app.demos.model.Zhangben;
import com.app.demos.sqlite.ZhangbenSqlite;
import com.app.demos.util.AppCache;
import com.app.demos.util.AppFilter;

import java.util.ArrayList;

/**
 * Created by tom on 16-1-22.
 */
public class UiZhangBenHistory extends BaseUi {
    private TextView tvDate;
    private TextView tvPay;
    private TextView tvIncome;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui_zhangben_history);

        initView();
        initData();
    }

    /**
     * start UiZhangBenHistory
     * @param context
     * @param  (
     */
    public static void startAction(Context context)
    {
        Intent localIntent = new Intent( context, UiZhangBenHistory.class);
        //localIntent.putExtra("authenticatorType", paramInt);
        context.startActivity(localIntent);
    }

    private void initData() {
        double money = 0;
        ZhangbenSqlite zhangbenSqlite = new ZhangbenSqlite(this);
        ArrayList<Zhangben> zhangbenArrayList = zhangbenSqlite.getAllZhangben(null, Zhangben.COL_TIME + " desc");
        for (Zhangben zb : zhangbenArrayList) {
            money += Double.parseDouble(zb.getMoney());
        }
        tvPay.setText("pay(RMB)\n" + money);
        LiuShuiAdapter adapter = new LiuShuiAdapter(this, zhangbenArrayList);
        listView.setAdapter(adapter);
    }

    private void initView() {
        iniToolBar();
        tvIncome = (TextView) findViewById(R.id.ui_zhangben_history_income);
        tvPay = (TextView) findViewById(R.id.ui_zhangben_history_pay);
        tvDate = (TextView) findViewById(R.id.ui_zhangben_history_date);
        listView = (ListView) findViewById(R.id.ui_zhangben_history_listView);
    }

    private void iniToolBar() {
        getToolBar(R.id.toolbar, getString(R.string.liu_shui), true);
    }


    //////////////////******** inner class *******//////////////////////////////////////////////
    class LiuShuiAdapter extends BaseList {

    private  BaseUi ui;
    private LayoutInflater inflater;
        private ArrayList<Zhangben> zhangbens;

        public final class LiuShuiListItem {
            public ImageView face;
            public TextView two;
            public TextView three;
            public TextView uptime;
            public TextView money;
            public TextView location;
        }

        public LiuShuiAdapter (BaseUi ui, ArrayList<Zhangben> zhangbens) {
            this.ui = ui;
            this.inflater = LayoutInflater.from(this.ui);
            this.zhangbens = zhangbens;
        }

        @Override
        public int getCount() {
            return zhangbens.size();
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
            // init tpl
            LiuShuiListItem  blogItem = null;
            // if cached expired
            if (v == null) {
                v = inflater.inflate(R.layout.test_zhangben_history_item, null);
                blogItem = new LiuShuiListItem();
                blogItem.face = (ImageView) v.findViewById(R.id.zhanben_history_item_face);
                blogItem.two = (TextView) v.findViewById(R.id.zhanben_history_item_two);
                blogItem.three = (TextView) v.findViewById(R.id.zhanben_history_item_three);
                blogItem.uptime = (TextView) v.findViewById(R.id.zhanben_history_item_time);
                blogItem.money = (TextView) v.findViewById(R.id.zhanben_history_item_money);
                blogItem.location = (TextView) v.findViewById(R.id.zhanben_history_item_location);
                v.setTag(blogItem);
            } else {
                blogItem = (LiuShuiListItem) v.getTag();
            }
            // fill data
            blogItem.uptime.setText(zhangbens.get(p).getTime());
            // fill html data
            blogItem.two.setText(zhangbens.get(p).getTwo());
            blogItem.three.setText(zhangbens.get(p).getThree());
            blogItem.money.setText(zhangbens.get(p).getMoney());
            blogItem.location.setText(zhangbens.get(p).getLocation());
            //blogItem.face.setImageResource();
            return v;
        }
    }
}
