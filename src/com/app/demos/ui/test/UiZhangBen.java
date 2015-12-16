package com.app.demos.ui.test;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.layout.Button;
import com.app.demos.model.Zhangben;
import com.app.demos.sqlite.ZhangbenSqlite;
import com.app.demos.util.TimeUtil;

import java.util.ArrayList;

/**
 * Created by tom on 15-12-16.
 */
public class UiZhangBen extends AppCompatActivity {
    private EditText lacation;
    private EditText money;
    private EditText time;
    private EditText type;
    private AppCompatButton save;
    private AppCompatButton next;
    private TextView header;
    //private TextView time;
    //private TextView type;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui_zhangben);

        initView();
    }

    private void initView() {
        lacation = (EditText) findViewById(R.id.ui_zhanben_edit_loaction);
        money = (EditText) findViewById(R.id.ui_zhanben_edit_money);
        time = (EditText) findViewById(R.id.ui_zhanben_edit_time);
        type = (EditText) findViewById(R.id.ui_zhanben_edit_type);
        save = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_save);
        next = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_next);

        header = (TextView) findViewById(R.id.ui_zhanben_header_tv);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Zhangben zhang = new Zhangben();
                zhang.setId( TimeUtil.long2String2(System.currentTimeMillis()));
                zhang.setMoney(money.getText().toString());
                zhang.setLocation(lacation.getText().toString());
                zhang.setOne(type.getText().toString());
                zhang.setTwo(type.getText().toString());
                zhang.setThree(type.getText().toString());
                zhang.setTime(time.getText().toString());
                if (new ZhangbenSqlite(UiZhangBen.this).updateZhangben(zhang)) {
                    Snackbar.make(v, "ok", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(v, "fail", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = "";
                ArrayList<Zhangben> zList = new ZhangbenSqlite(UiZhangBen.this).getAllZhangben();
                for (Zhangben z : zList) {
                    s = s + "\n" + z.getMoney() + "\n" + z.getThree() + "\n" + z.getTime() + "\n" +
                            z.getLocation() + "\n" + z.getId() + "--------------";
                }
                header.setText(s);
            }
        });
    }
}
