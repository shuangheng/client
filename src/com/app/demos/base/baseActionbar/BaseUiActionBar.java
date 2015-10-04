package com.app.demos.base.baseActionbar;

import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.app.demos.R;
import com.app.demos.base.BaseUi;

/**
 * Created by tom on 15-9-30.
 */
public class BaseUiActionBar extends BaseUi {

    protected InputMethodManager inputMethodManager;
    private ActionBarUnitl barUnitl;

    private void initActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {

            actionBar.setCustomView(R.layout.action_bar_view_wumi);
            this.barUnitl = new ActionBarUnitl(actionBar.getCustomView(), this, this.inputMethodManager);
            this.barUnitl.a(getTitle().toString());
            //this.barUnitl.a(new t(this));//onclik
            //if (!(ao.a(1, actionBar.getDisplayOptions())))
            //  return;
            //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
           // actionBar.show();
        }
    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initActionBar();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        initActionBar();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        initActionBar();
    }
}
