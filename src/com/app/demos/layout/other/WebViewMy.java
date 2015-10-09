package com.app.demos.layout.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

/**
 * Created by tom on 15-10-5.
 */
public class WebViewMy extends WebView {

    private OnViewTopListener onViewTopListener;

    public WebViewMy(Context context) {
        super(context);
    }

    public WebViewMy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebViewMy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int newX, int newY, int oldX, int oldY) {
        super.onScrollChanged(newX, newY, oldX, oldY);
        onViewTopListener.onScroll(newY, oldY);
        if (newY != oldY) {
            float contentHeight = getContentHeight() * getScale();//整个网页height
            float mCurrContentHeight = getHeight() + getScrollY();// 当前内容高度
            int mThreshold = 300;//in pixels
            // 当前内容高度下从未触发过, 浏览器存在滚动条且滑动到将抵底部位置
            if (mCurrContentHeight != contentHeight && newY > 0 && contentHeight <= newY + getHeight() + mThreshold) {
                mCurrContentHeight = contentHeight;
                onViewTopListener.onViewVicinityBottom();
            }
            if (contentHeight == newY + getHeight()) {
                onViewTopListener.onViewBottom();
                //Toast.makeText(getContext(), "到底部", Toast.LENGTH_SHORT).show();
            }
            if (newY == 0) {
                onViewTopListener.onViewTop();
                //Toast.makeText(getContext(), "到", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static interface OnViewTopListener {
        void onScroll(int newY, int oldY);
        void onViewTop();
        void onViewBottom();
        void onViewVicinityBottom();
    }

    public void setOnViewScrollChangedListener(OnViewTopListener onViewTopListener) {
        this.onViewTopListener = onViewTopListener;
    }
}
