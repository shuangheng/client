package com.app.demos.ui.test.zhangBen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseUi;
import com.app.demos.model.ZhangBenLocation;
import com.app.demos.sqlite.ZhangBenLocationSqlite;

/**
 * Created by tom on 16-1-18.
 */
public class UiAddBuyLocation extends BaseUi{
    private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_ui_add_buy_location);
        initView();
        initListener();
    }

    private void initView() {
        initToolbar();
        editText = (EditText) findViewById(R.id.editText_loctin);
    }

    private void initToolbar() {
        getToolBar(R.id.toolbar, getString(R.string.add_location), true);
        TextView view = (TextView) LayoutInflater.from(this).inflate(R.layout.action_bar_menu_text_item, null);
        view.setText(getString(R.string.save));
        setCustomViewOnToolBar(view, Gravity.END);
    }

    private void initListener() {
        getSupportActionBar().getCustomView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editText.getText().toString().trim();
                if (location.equals("")) {
                    toast(getString(R.string.please_input_location));
                    return;
                }

                ZhangBenLocation zhangBenLocation = new ZhangBenLocation(location, "0");
                ZhangBenLocationSqlite zhangBenLocationSqlite = new ZhangBenLocationSqlite(UiAddBuyLocation.this);
                if (zhangBenLocationSqlite.updateZhangBenLocation(zhangBenLocation)) {
                    returnData();
                } else {
                    toast(getString(R.string.save_fail));
                }
            }
        });
    }

    /**
     * 返回数据给上一个Activity
     */
    public void returnData() {
        Intent intent = new Intent();
        intent.putExtra("add_location_name", editText.getText().toString().trim());
        setResult(RESULT_OK, intent);
        doFinish();
    }
}
