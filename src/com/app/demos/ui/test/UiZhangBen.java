package com.app.demos.ui.test;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseMessage;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.base.LogMy;
import com.app.demos.layout.Button;
import com.app.demos.list.bitmap_load_list.FileCache;
import com.app.demos.model.Zhangben;
import com.app.demos.sqlite.ZhangbenSqlite;
import com.app.demos.util.AppUtil;
import com.app.demos.util.TimeUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by tom on 15-12-16.
 */
public class UiZhangBen extends BaseUi {
    private static final int MSG_DATA = 100;
    private EditText lacation;
    private EditText money;
    private EditText time;
    private EditText type;
    private AppCompatButton export;
    private AppCompatButton importBtn;
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
        initToolBar();
        lacation = (EditText) findViewById(R.id.ui_zhanben_edit_loaction);
        money = (EditText) findViewById(R.id.ui_zhanben_edit_money);
        time = (EditText) findViewById(R.id.ui_zhanben_edit_time);
        type = (EditText) findViewById(R.id.ui_zhanben_edit_type);
        importBtn = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_import);
        export = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_export);
        save = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_save);
        next = (AppCompatButton) findViewById(R.id.ui_zhanben_btn_next);

        header = (TextView) findViewById(R.id.ui_zhanben_header_tv);

        time.setText(TimeUtil.long2String(System.currentTimeMillis()));
        export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData();
                Snackbar.make(v, "exprot data ok", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        importBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mHandler = new IndexHandler();
                FileCache fileCache = new FileCache(UiZhangBen.this, "sql");
                File saveFilePath = fileCache.getFile("zhanBen.sql");
                try {
                    FileInputStream is = new FileInputStream(saveFilePath);
                    String encoding = "UTF-8";
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    String s = null;
                    while ((s = bufferedReader.readLine()) != null) {
                        toast(s);
                        Bundle bundle = new Bundle();
                        bundle.putString("data", s);
                        Message msg = new Message();
                        msg.what = MSG_DATA;
                        msg.setData(bundle);
                        mHandler.sendMessage(msg);


                    }
                    is.close();
                    reader.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Zhangben zhang = new Zhangben();
                zhang.setMoney(money.getText().toString());
                zhang.setLocation(lacation.getText().toString());
                zhang.setOne(type.getText().toString());
                zhang.setTwo(type.getText().toString());
                zhang.setThree(type.getText().toString());
                zhang.setTime(time.getText().toString());
                if (new ZhangbenSqlite(UiZhangBen.this).updateZhangben(zhang)) {
                    Snackbar.make(v, "ok", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    exportData();
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

    private void exportData() {
        String s = "";
        String douDian = ",";
        ArrayList<Zhangben> zhangbenArrayList = new ZhangbenSqlite(UiZhangBen.this).getAllZhangben();
        int size = zhangbenArrayList.size();
        for (Zhangben z : zhangbenArrayList) {
            if (z == zhangbenArrayList.get(size-1)) {
                douDian = "";
            }
            s += "{\"" + Zhangben.COL_TIME + "\":\"" + z.getTime() + "\",\"" +
                    Zhangben.COL_ONE + "\":\"" + z.getOne() + "\",\"" +
                    Zhangben.COL_TWO + "\":\"" + z.getTwo() + "\",\"" +
                    Zhangben.COL_THREE + "\":\"" + z.getThree() + "\",\"" +
                    Zhangben.COL_LOCATION + "\":\"" + z.getLocation() + "\",\"" +
                    Zhangben.COL_MONEY + "\":\"" + z.getMoney() + "\"}" + douDian;
        }
        String data = "{\"code\":10000,\"message\":\"get data ok\"," +
                "\"result\":{\"Zhangben\""+":" + "[" + s +
                "]}}";

        LogMy.e(UiZhangBen.this, data);

        FileCache fileCache = new FileCache(UiZhangBen.this, "sql");
        File saveFilePath = fileCache.getFile("zhanBen.sql");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(saveFilePath);
            fos.write(data.getBytes());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initToolBar() {
        Toolbar toolbar = getToolBar(R.id.toolbar, getString(R.string.ji_zhang_ben), true);
    }

    private class IndexHandler extends Handler {
        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {

                    case MSG_DATA:
                        String s = msg.getData().getString("data");
                        header.setText("ssssss" + s);
                        BaseMessage baseMessage = AppUtil.getMessage(s);
                        LogMy.e(UiZhangBen.this, baseMessage.toString());
                        ArrayList<Zhangben> list = (ArrayList<Zhangben>) baseMessage.getResultList("Zhangben");
                        LogMy.e(UiZhangBen.this, list.toString());
                        ZhangbenSqlite zhangbenSqlite = new ZhangbenSqlite(UiZhangBen.this);
                        for (Zhangben z : list) {
                        zhangbenSqlite.updateZhangben(z);
                        }
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                //.toast(e.getMessage());
            }
        }
    }
}
