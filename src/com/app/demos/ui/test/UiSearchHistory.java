package com.app.demos.ui.test;

import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.model.DromInfo;
import com.app.demos.sqlite.DromInfoSqlite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 15-8-13.
 */
public class UiSearchHistory extends ExpandableListActivity {

    List<String> group;           //组列表
    List<List<String>> child;     //子列表
    ContactsInfoAdapter adapter;  //数据适配器
    DromInfoSqlite dromInfoSqlite;
    private ArrayList<DromInfo> drominfoList;
    private Button button;
    private SharedPreferences show;
    Toolbar toolbar;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置为无标题
        setContentView(R.layout.ui_share_history);
        //getExpandableListView().setBackgroundResource(R.drawable.default_bg);
        show = getSharedPreferences("show_all", MODE_PRIVATE);
        group = new ArrayList<String>();
        child = new ArrayList<List<String>>();
        adapter = new ContactsInfoAdapter();
        initToolBar();
        button = (Button) findViewById(R.id.ui_share_history_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = show.edit();
                if (show.getBoolean("all", false)) {
                    editor.putBoolean("all", false);
                    editor.commit();
                    initializeData(false);
                    button.setText("hide");
                    adapter.notifyDataSetChanged();
                } else {
                    editor.putBoolean("all", true);
                    editor.commit();
                    initializeData(true);
                    button.setText("all");
                    adapter.notifyDataSetChanged();
                }
            }
        });



        dromInfoSqlite = new DromInfoSqlite(this);

        initializeData(show.getBoolean("all", false));
        getExpandableListView().setAdapter(adapter);
        getExpandableListView().setCacheColorHint(0);  //设置拖动列表的时候防止出现黑色背景
        getExpandableListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                DromInfo d = drominfoList.get(position);
                d.setDisplay("1");
                dromInfoSqlite.updateDromInfo(d);
                //group.remove(position);
                //child.remove(position);
                initializeData(false);
                adapter.notifyDataSetChanged();
                String s = "hide " + drominfoList.get(position).getName() + " 成功！";
                Toast.makeText(view.getContext(), s, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    private void initToolBar() {
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 初始化组、子列表数据
     * <p>i=0    show all
     * <p>i=1    hide
     */
    private void initializeData(boolean b){

        drominfoList = dromInfoSqlite.getAll();
        if (b) {
            group.clear();
            child.clear();
            for (DromInfo drom : drominfoList) {
                addInfo(drom.getName() + "\n" + drom.getNum(), new String[]{drom.getContent()});
            }
        } else {
            group.clear();
            child.clear();
            for (DromInfo drom : drominfoList) {
                if (drom.getDisplay().equals("0")) {
                    addInfo(drom.getName() + "\n" + drom.getNum(), new String[]{drom.getContent()});
                }
            }
        }

/*
        addInfo("Andy",new String[]{"male"});
        addInfo("Fairy",new String[]{"female","138123***","GuangZhou"});
        addInfo("Jerry",new String[]{"male","138123***","ShenZhen"});
        addInfo("Tom",new String[]{"female","138123***","ShangHai"});
        addInfo("Bill",new String[]{"male","138231***","ZhanJiang"});
*/
    }

    /**
     * 模拟给组、子列表添加数据
     * @param g-group
     * @param c-child
     */
    private void addInfo(String g,String[] c){
        group.add(g);
        List<String> childitem = new ArrayList<String>();
        for(int i=0;i<c.length;i++){
            childitem.add(c[i]);
        }
        child.add(childitem);
    }

    private void hide() {

    }

    class ContactsInfoAdapter extends BaseExpandableListAdapter {


        //-----------------Child----------------//
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return child.get(groupPosition).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return child.get(groupPosition).size();
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            String string = child.get(groupPosition).get(childPosition);
            return getGenericView(string);
        }

        //----------------Group----------------//
        @Override
        public Object getGroup(int groupPosition) {
            return group.get(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public int getGroupCount() {
            return group.size();
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String string = group.get(groupPosition);
            return getGenericView(string);
        }

        //创建组/子视图
        public TextView getGenericView(String s) {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            TextView text = new TextView(UiSearchHistory.this);
            text.setLayoutParams(lp);
            // Center the text vertically
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            text.setPadding(100, 0, 0, 0);

            text.setText(s);
            return text;
        }


        @Override
        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return true;
        }

    }
}