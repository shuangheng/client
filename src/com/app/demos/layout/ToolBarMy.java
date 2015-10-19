package com.app.demos.layout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.app.demos.R;
import com.app.demos.layout.swipebacklayout.ViewDragHelper;

/**
 * Created by tom on 15-10-19.
 */
public class ToolBarMy extends Toolbar {
    private Drawable mShadowBottom;
    private Rect mTmpRect = new Rect();

    public ToolBarMy(Context context) {
        super(context);
    }

    public ToolBarMy(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable shadowBottom = getResources().getDrawable(R.drawable.shadow_bottom);
        setShadow(shadowBottom);
    }

    public ToolBarMy(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Drawable shadowBottom = getResources().getDrawable(R.drawable.shadow_bottom);
        setShadow(shadowBottom);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawShadow(canvas);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final boolean drawContent = child == this;

        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (drawContent) {
            drawShadow(canvas);
        }
        return ret;
    }

    public void setShadow(Drawable shadow) {
            mShadowBottom = shadow;
        invalidate();
    }

    private void drawShadow(Canvas canvas) {
        final Rect childRect = mTmpRect;
        //getHitRect(childRect);
getDrawingRect(childRect);
        if (mShadowBottom != null) {
            mShadowBottom.setBounds(childRect.left, childRect.bottom, childRect.right,
                    childRect.bottom + mShadowBottom.getIntrinsicHeight());
            mShadowBottom.setAlpha((int) (25));
            mShadowBottom.draw(canvas);
        }
    }
}