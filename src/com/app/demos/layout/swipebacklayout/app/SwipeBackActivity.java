
package com.app.demos.layout.swipebacklayout.app;

import android.os.Bundle;
import android.view.View;

import com.app.demos.base.BaseUi;
import com.app.demos.layout.swipebacklayout.SwipeBackLayout;
import com.app.demos.layout.swipebacklayout.Utils;

public class SwipeBackActivity extends BaseUi implements SwipeBackActivityBase {
    private SwipeBackActivityHelper mHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public void onBackPressed() {
        scrollToFinishActivity();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucentMy(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }
}
