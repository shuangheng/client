package com.app.demos.ui.test.dragelayoutDown;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * 通过ViewDragHelper实现的侧滑控件
 * @author liujing
 * 
 */
public class DragDownLayout extends RelativeLayout {

	private View mUpContent;
	private View mMainContent;
	private View mListView;
	private int mWidth;
	private int mDragRange;//拖动范围
	private ViewDragHelper mDragHelper;
	private int mMainLeft;
	private int mMainTop;
	private int mHeight;

	private Status mStatus = Status.Close;
	private GestureDetectorCompat mDetectorCompat;
    private View paramView;
    private int mUpWidth;
    private int mUpHeight;

    public DragDownLayout(Context context) {
		this(context, null);
	}

	public DragDownLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DragDownLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//ViewDragHelper.create(forParent, sensitivity, cb);
		//对应参数：父布局、敏感度、回调
		mDragHelper = ViewDragHelper.create(this, mCallBack);
		mDetectorCompat = new GestureDetectorCompat(getContext(),
				mGestureListener);

	}
	
	private boolean isDrag = true;
	
	public void setDrag(boolean isDrag) {
		this.isDrag = isDrag;
		if(isDrag){
			//这里有个Bug,当isDrag从false变为true是，mDragHelper的mCallBack在
			//首次滑动时不响应，再次滑动才响应，自能在此调用下，让mDragHelper恢复下状态
			mDragHelper.abort();
		}
	}

	SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			if((Math.abs(distanceX) > Math.abs(distanceY))&&distanceX<0&&isDrag!=false&&mStatus== Status.Close){
				return true;
			}else if((Math.abs(distanceX) > Math.abs(distanceY))&&distanceX>0&&isDrag!=false&&mStatus== Status.Open){
				return true;
			}else {
				return false;
			}
		}
	};

    public void setViewCanDrag(View view) {
        paramView = view;
    }

	ViewDragHelper.Callback mCallBack = new ViewDragHelper.Callback() {
		public void onEdgeTouched(int edgeFlags, int pointerId) {
			
			
		}
		
		public void onEdgeDragStarted(int edgeFlags, int pointerId) {
			 mDragHelper.captureChildView(mMainContent, pointerId); 
		}
		// 决定child是否可被拖拽。返回true则进行拖拽。
		@Override
		public boolean tryCaptureView(View child, int pointerId) {
            if (paramView == null) {
                paramView = new View(getContext());
            }
			return child == mMainContent || child == mUpContent || child == mListView;
            //return true;
		}

		// 当capturedChild被拖拽时
		@Override
		public void onViewCaptured(View capturedChild, int activePointerId) {
			super.onViewCaptured(capturedChild, activePointerId);
		}

		// 横向拖拽的范围，大于0时可拖拽，等于0无法拖拽
		// 此方法只用于计算如view释放速度，敏感度等
		// 实际拖拽范围由clampViewPositionHorizontal方法设置
		@Override
		public int getViewVerticalDragRange(View child) {
			return mDragRange;
		}

        @Override// 此处设置view的拖拽范围。（实际移动还未发生）
        public int clampViewPositionVertical(View child, int top, int dy) {
            // 拖动前oldLeft + 变化量dx == left
            if (mMainTop + dy < 0) {
                return 0;
            } else if (mMainTop + dy > mDragRange) {
                return mDragRange;
            }
            return top;
        }

        // 决定了当View位置改变时，希望发生的其他事情。（此时移动已经发生）
		// 高频实时的调用，在这里设置左右面板的联动
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			//如果拖动的是主面板
			if (changedView == mMainContent) {
				mMainTop = top;
			} else {
				mMainTop += dy;
			}

			// 进行值的修正
			if (mMainTop < 0) {
                mMainTop = 0;
			} else if (mMainTop > mDragRange) {
                mMainTop = mDragRange;
			}
			// 如果拖拽的是左面板，强制在指定位置绘制Content
			if (changedView == mUpContent) {
				layoutContent();
			}

			dispatchDragEvent(mMainTop);

		}

		// View被释放时,侧滑打开或恢复
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			if (yvel > 0) {
				open();
			} else if (yvel == 0 && mMainTop > mDragRange * 0.5f) {
				open();
			} else {
				close();
			}

		}

		//当拖拽状态改变的时，IDLE/DRAGGING/SETTLING
		@Override
		public void onViewDragStateChanged(int state) {
			super.onViewDragStateChanged(state);
		}

	};

	private void layoutContent() {
		//mMainContent.layout(0, mMainTop, mWidth, mMainTop + mHeight);
		//mUpContent.layout(mWidth - mUpWidth, 0, mWidth, mMainTop + mUpHeight);
	}

	/**
	 * 每次更新都会调用 根据当前执行的位置计算百分比percent
	 */
	protected void dispatchDragEvent(int mainLeft) {
		float percent = mainLeft / (float) mDragRange;
		animViews(percent);

		if (mListener != null) {
			mListener.onDraging(percent);
		}

		Status lastStatus = mStatus;
		if (updateStatus(mainLeft) != lastStatus) {
			if (mListener == null) {
				return;
			}
			if (lastStatus == Status.Draging) {
				if (mStatus == Status.Close) {
					mListener.onClose();
				} else if (mStatus == Status.Open) {
					mListener.onOpen();
				}

			}
		}
	}

	public static interface OnLayoutDragingListener {
		void onOpen();

		void onClose();

		void onDraging(float percent);
	}

	private OnLayoutDragingListener mListener;

	public void setOnLayoutDragingListener(OnLayoutDragingListener l) {
		mListener = l;
	}

	private Status updateStatus(int mainLeft) {
		if (mainLeft == 0) {
			mStatus = Status.Close;
		} else if (mainLeft == mDragRange) {
			mStatus = Status.Open;
		} else {
			mStatus = Status.Draging;
		}
		return mStatus;
	}

	public static enum Status {
		Open, Close, Draging
	}

	public Status getStatus() {
		return mStatus;
	}

	public void setStatus(Status mStatus) {
		this.mStatus = mStatus;
	}
	
	/**
	 * 伴随动画：
	 * @param percent
	 */
	private void animViews(float percent) {
		// 主面板：缩放
		float inverse = 1 - percent * 0.3f;
		//ViewHelper.setScaleX(mMainContent, inverse);
		//ViewHelper.setScaleY(mMainContent, inverse);
        ViewHelper.setAlpha(mMainContent, inverse);

		// up面板：缩放、平移、透明度
		ViewHelper.setScaleX(mUpContent, 1.0f + 0.5f * percent);
		ViewHelper.setScaleY(mUpContent, 1.0f + 0.5f * percent);

		ViewHelper.setTranslationY(mUpContent, 0.0f + mWidth / 5.0f
                * percent);
        ViewHelper.setTranslationX(mUpContent, 0.0f - (mWidth - mUpWidth) / 2.0f
                * percent);
		//ViewHelper.setAlpha(mUpContent, percent);
		// 背景：颜色渐变
		getBackground().setColorFilter(
				evaluate(percent, Color.BLACK, Color.TRANSPARENT),
				PorterDuff.Mode.SRC_OVER);
	}

	private int evaluate(float fraction, int startValue, int endValue) {
		int startInt = (Integer) startValue;
		int startA = (startInt >> 24) & 0xff;
		int startR = (startInt >> 16) & 0xff;
		int startG = (startInt >> 8) & 0xff;
		int startB = startInt & 0xff;

		int endInt = (Integer) endValue;
		int endA = (endInt >> 24) & 0xff;
		int endR = (endInt >> 16) & 0xff;
		int endG = (endInt >> 8) & 0xff;
		int endB = endInt & 0xff;

		return (int) ((startA + (int) (fraction * (endA - startA))) << 24)
				| (int) ((startR + (int) (fraction * (endR - startR))) << 16)
				| (int) ((startG + (int) (fraction * (endG - startG))) << 8)
				| (int) ((startB + (int) (fraction * (endB - startB))));
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean onTouchEvent = mDetectorCompat.onTouchEvent(ev);
		//将Touch事件传递给ViewDragHelper
		return mDragHelper.shouldInterceptTouchEvent(ev) & onTouchEvent;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		try {
			//将Touch事件传递给ViewDragHelper
			mDragHelper.processTouchEvent(event);
		} catch (Exception e) {
		}
		return true;
	}

	public void close() {
		close(true);
	}

	public void open() {
		open(true);
	}

	public void close(boolean isSmooth) {
		mMainLeft = 0;
		mMainTop = 0;
		if (isSmooth) {
			// 执行动画，返回true代表有未完成的动画, 需要继续执行
			if (mDragHelper.smoothSlideViewTo(mMainContent, 0, mMainTop)) {
				// 注意：参数传递根ViewGroup
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			layoutContent();
		}
	}

	public void open(boolean isSmooth) {
		mMainTop = mDragRange;
		if (isSmooth) {
			if (mDragHelper.smoothSlideViewTo(mMainContent, 0, mMainTop)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			layoutContent();
		}
	}

	@Override
	public void computeScroll() {
		// 高频率调用，决定是否有下一个变动等待执行
		if (mDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //mMainContent.layout(0, mMainTop, mWidth, mMainTop + mHeight);
        //mUpContent.layout(0, 0, mWidth, mHeight);
    }


    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		//拿到宽高
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
        mUpWidth = mUpContent.getMeasuredWidth();
        mUpHeight = mUpContent.getMeasuredHeight();
		//设置拖动范围
		mDragRange = (int) (mWidth * 0.5f);
	}

	/**
	 * 填充结束时获得两个子布局的引用
	 */
	@Override
	protected void onFinishInflate() {

		int childCount = getChildCount();
		// 必要的检验
		if (childCount < 2) {
			throw new IllegalStateException(
					"You need two childrens in your content");
		}

		if (!(getChildAt(0) instanceof ViewGroup)
				|| !(getChildAt(1) instanceof ViewGroup)) {
			throw new IllegalArgumentException(
					"Your childrens must be an instance of ViewGroup");
		}

		mUpContent = getChildAt(2);
        mMainContent = getChildAt(0);
        mListView = ((ViewGroup) getChildAt(0)).getChildAt(0);

	}

}
