package com.app.demos.Listener;

import android.support.v7.widget.RecyclerView;

/**
 * Created by tom on 15-5-3.
 * <p>hide FAB Button
 */
public abstract class HideFabScrollListener extends RecyclerView.OnScrollListener {
    private static final int HIDE_THRESHOLD = 200;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;
    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
            onHide();
            controlsVisible = false;
            scrolledDistance = 0;
        } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
            onShow();
            controlsVisible = true;
            scrolledDistance = 0;
        }
        if((controlsVisible && dy>0) || (!controlsVisible && dy<0)) {
            scrolledDistance += dy;
        }
    }
    public abstract void onHide();
    public abstract void onShow();
    public abstract void onMoved(int distance);

}
