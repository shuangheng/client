package com.app.demos.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import com.app.demos.R;
import com.app.demos.layout.dragtoplayout.AttachUtil;
import com.app.demos.layout.swipebacklayout.app.SwipeBackActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by chenupt@gmail.com on 2015/1/25.
 * Description TODO
 */
public class AboutActivity extends SwipeBackActivity {

    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.about));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
