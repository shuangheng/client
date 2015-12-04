package com.app.demos.Listener;

/**
 * Interface definition for a callback to be invoked when a view is clicked.
 */
public interface OnHideOrShowListener {
    void onHide();
    void onShow();
    void onMoved(int distance);
}
