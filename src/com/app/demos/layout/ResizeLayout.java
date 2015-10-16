package com.app.demos.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 侦听软键盘的显示隐藏
 * <P>有时候，借助系统本身的机制来实现主窗口的调整并非我们想要的结果，我们可能希望在软键盘显示隐藏的时候，
 * <p>手动的对布局进行修改，以便使软键盘弹出时更加美观。这时就需要对软键盘的显示隐藏进行侦听。
 * <P>直接对软键盘的显示隐藏侦听的方法本人没有找到，如果哪位找到的方法请务必告诉本人一声。
 * <p>还有本方法针对压缩模式，平移模式不一定有效。
 * <P>我们可以借助软键盘显示和隐藏时，对主窗口进行了重新布局这个特性来进行侦听。如果我们设置的模式为压缩模式，
 * <p>那么我们可以对布局的onSizeChanged函数进行跟踪，如果为平移模式，那么该函数可能不会被调用。
 * <P>我们可以重写根布局，因为根布局的高度一般情况下是不发生变化的。
 * <P>假设跟布局为线性布局，模式为压缩模式，我们写一个例子，当输入法弹出时隐藏某个view，输入法隐藏时显示某个view。
 * <p>>Created by tom on 15-10-16.
 */
public class ResizeLayout  extends RelativeLayout {
    private OnResizeListener mListener;

    public interface OnResizeListener {
        void OnResize(int w, int h, int oldw, int oldh);
    }

    public void setOnResizeListener(OnResizeListener l) {
        mListener = l;
    }

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (mListener != null) {
            mListener.OnResize(w, h, oldw, oldh);
        }
    }
}