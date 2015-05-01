package com.app.demos.Listener;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.app.demos.util.Utils;

/**
 * Created by tom on 15-5-1.
 * 其dx, dy参数分别是横向和纵向的滚动距离，准确是的是两个滚动事件之间的偏移量，而不是总的滚动距离。
 *基本的思路如下：
 *1.计算出滚动的总距离（deltas相加），但是只在Toolbar隐藏且上滚或者Toolbar未隐藏且下滚的时候，因为我们只关心这两种情况。
 *if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
 *scrolledDistance += dy; }
 *
 *2.如果总的滚动距离超多了一定值（这个值取决于你自己的设定，越大，需要滑动的距离越长才能显示或者隐藏），我们就根据其方向显示或者隐藏Toolbar（dy>0意味着下滚，dy<0意味着上滚）。
 *if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
 *onHide();
 *controlsVisible = false;
 *scrolledDistance = 0;
 *} else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
 *onShow();
 *controlsVisible = true;
 *scrolledDistance = 0;
 *}
 */
public abstract class HidingScrollListener extends RecyclerView.OnScrollListener {
    private static final float HIDE_THRESHOLD = 10;
    private static final float SHOW_THRESHOLD = 70;

    private int mToolbarOffset = 0;
    private boolean mControlsVisible = true;
    private int mToolbarHeight;

    public HidingScrollListener(Context context) {
        mToolbarHeight = Utils.getToolbarHeight(context);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if(newState == RecyclerView.SCROLL_STATE_IDLE) {
            if (mControlsVisible) {
                if (mToolbarOffset > HIDE_THRESHOLD) {
                    setInvisible();
                } else {
                    setVisible();
                }
            } else {
                if ((mToolbarHeight - mToolbarOffset) > SHOW_THRESHOLD) {
                    setVisible();
                } else {
                    setInvisible();
                }
            }
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        clipToolbarOffset();
        onMoved(mToolbarOffset);

        if((mToolbarOffset <mToolbarHeight && dy>0) || (mToolbarOffset >0 && dy<0)) {
            mToolbarOffset += dy;
        }

    }

    /**
     * 虽然上面的代码已经有了限制，但是在很短的时间内（比如fling的时候），还是有可能超过这个区间，
     * 因此需要调用clipToolbarOffset()方法来做二次限制，确保mToolbarOffset在0到Toolbar高度的范围内，
     * 否则会出现抖动闪烁的情况。
     * 我们还定义了一个在scroll期间被调用的抽象方法onMoved()。
     */
    private void clipToolbarOffset() {
        if(mToolbarOffset > mToolbarHeight) {
            mToolbarOffset = mToolbarHeight;
        } else if(mToolbarOffset < 0) {
            mToolbarOffset = 0;
        }
    }

    private void setVisible() {
        if(mToolbarOffset > 0) {
            onShow();
            mToolbarOffset = 0;
        }
        mControlsVisible = true;
    }

    private void setInvisible() {
        if(mToolbarOffset < mToolbarHeight) {
            onHide();
            mToolbarOffset = mToolbarHeight;
        }
        mControlsVisible = false;
    }

    public abstract void onMoved(int distance);
    public abstract void onShow();
    public abstract void onHide();
}



