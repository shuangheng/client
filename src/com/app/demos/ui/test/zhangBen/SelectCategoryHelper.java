package com.app.demos.ui.test.zhangBen;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.app.demos.R;
import com.app.demos.base.BaseApp;
import com.app.demos.base.LogMy;
import com.app.demos.ui.fragment.emoji.MsgFaceUtils;
import com.app.demos.ui.fragment.emoji.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryHelper implements OnItemClickListener {
	private static final String TAG = SelectCategoryHelper.class.getSimpleName();
	private Context context;
	private View mFaceView;
	private ViewPager mViewPager;
	private LinearLayout mIndexContainer;
	private LayoutInflater mInflater;
	private int pageSize = 10;//每页表情个数 = pageSize + 1
	/** 保存于内存中的表情集合 */
	private List<CategoryModle> mMsgCategoryData = new ArrayList<CategoryModle>();
	/** 表情分页的结果集合 */
	public List<List<CategoryModle>> mPageCategoryDatas = new ArrayList<List<CategoryModle>>();

	/** 表情页界面集合 */
	private ArrayList<View> pageViews;

	/** 表情数据填充器 */
	private List<CategoryAdapter> categoryAdapters;

	/** 当前表情页 */
	private int current = 0;
	/** 游标点集合 */
	private ArrayList<ImageView> pointViews;

	private OnFaceOprateListener mOnFaceOprateListener;

	public SelectCategoryHelper(Context context, View toolView) {
		this.context = context;
		mInflater = LayoutInflater.from(this.context);
		mFaceView = toolView;
		mViewPager = (ViewPager) mFaceView.findViewById(R.id.face_viewpager);
		mIndexContainer = (LinearLayout) mFaceView.findViewById(R.id.msg_face_index_view);
		ParseData();
		initView();

	}

	private void initView() {
		Init_viewPager();
		Init_Point();
		Init_Data();
	}

	private void Init_viewPager() {
		pageViews = new ArrayList<View>();
		// 左侧添加空页
		View nullView1 = new View(context);
		// 设置透明背景
		nullView1.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView1);

		// 中间添加表情页
		categoryAdapters = new ArrayList<CategoryAdapter>();
		for (int i = 0; i < mPageCategoryDatas.size(); i++) {
			GridView view = (GridView) mInflater.inflate(R.layout.test_ui_zhangben_category_gridview, null);
			CategoryAdapter adapter = new CategoryAdapter(context, mPageCategoryDatas.get(i));
			view.setSelector(R.drawable.item_background_holo_light);
			view.setAdapter(adapter);
			categoryAdapters.add(adapter);
			view.setOnItemClickListener(this);
			pageViews.add(view);
		}

		// 右侧添加空页面
		View nullView2 = new View(context);
		// 设置透明背景
		nullView2.setBackgroundColor(Color.TRANSPARENT);
		pageViews.add(nullView2);
	}

	/**
	 * 初始化游标
	 */
	private void Init_Point() {

		pointViews = new ArrayList<ImageView>();
		ImageView imageView;
		for (int i = 0; i < pageViews.size(); i++) {
			imageView = new ImageView(context);
			imageView.setBackgroundResource(R.drawable.icon_jw_face_index_nor);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			mIndexContainer.addView(imageView, layoutParams);
			if (i == 0 || i == pageViews.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.icon_jw_face_index_prs);
			}
			pointViews.add(imageView);

		}
	}

	/**
	 * 填充数据
	 */
	private void Init_Data() {
		mViewPager.setAdapter(new ViewPagerAdapter(pageViews));
		mViewPager.setCurrentItem(1);
		current = 0;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				current = arg0 - 1;
				// 描绘分页点
				draw_Point(arg0);
				// 如果是第一屏或者是最后一屏禁止滑动，其实这里实现的是如果滑动的是第一屏则跳转至第二屏，如果是最后一屏则跳转到倒数第二屏.
				if (arg0 == pointViews.size() - 1 || arg0 == 0) {
					if (arg0 == 0) {
						mViewPager.setCurrentItem(arg0 + 1);// 第二屏 会再次实现该回调方法实现跳转.
						pointViews.get(1).setBackgroundResource(R.drawable.icon_jw_face_index_prs);
					} else {
						mViewPager.setCurrentItem(arg0 - 1);// 倒数第二屏
						pointViews.get(arg0 - 1).setBackgroundResource(R.drawable.icon_jw_face_index_prs);
					}
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	/**
	 * 绘制游标背景
	 */
	public void draw_Point(int index) {
		for (int i = 1; i < pointViews.size(); i++) {
			if (index == i) {
				pointViews.get(i).setBackgroundResource(R.drawable.icon_jw_face_index_prs);
				if (index == 1)
					pointViews.get(i).setBackgroundResource(R.drawable.list_focused_holo);
			} else {
				pointViews.get(i).setBackgroundResource(R.drawable.icon_jw_face_index_nor);
			}
		}
	}

	/**
	 * 解析字符
	 * 
	 * @param //data
	 */
	private void ParseData() {
		CategoryModle emojEentry;
		try {
			int len = MsgFaceUtils.faceImgs.length;
			for (int i = 0; i < len; i++) {
				int resID = MsgFaceUtils.faceImgs[i];
				if (resID != 0) {
					emojEentry = new CategoryModle();
					emojEentry.setId(resID);
					emojEentry.setCharacter(MsgFaceUtils.faceImgNames[i]);
					mMsgCategoryData.add(emojEentry);
				}
			}
			int pageCount = (int) Math.ceil(mMsgCategoryData.size() / pageSize + 0.1);
			for (int i = 0; i < pageCount; i++) {
				mPageCategoryDatas.add(getData(i));
			}
		} catch (Exception e) {
			LogMy.e(BaseApp.getContext(), TAG+e.toString());
		}
	}

	/**
	 * 获取分页数据
	 * 
	 * @param page
	 * @return
	 */
	private List<CategoryModle> getData(int page) {
		int startIndex = page * pageSize;
		int endIndex = startIndex + pageSize;
		if (endIndex > mMsgCategoryData.size()) {
			endIndex = mMsgCategoryData.size();
		}
		List<CategoryModle> list = new ArrayList<CategoryModle>();
		list.addAll(mMsgCategoryData.subList(startIndex, endIndex));
		if (list.size() <= pageSize) {
			for (int i = list.size(); i <= pageSize; i++) {
				CategoryModle object = new CategoryModle();
				list.add(object);
			}
		}

		return list;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		CategoryModle item = (CategoryModle) categoryAdapters.get(current).getItem(position);

		if (item.getCharacter()!=null) {
			//LogMy.e(BaseApp.getContext(), TAG+ spannableString.toString());
			if (null != mOnFaceOprateListener) {
				mOnFaceOprateListener.onFaceSelected(item);
			}
		}
	}

	public void setFaceOpreateListener(OnFaceOprateListener mOnFaceOprateListener) {
		this.mOnFaceOprateListener = mOnFaceOprateListener;
	}

	public interface OnFaceOprateListener {

		void onFaceSelected(CategoryModle modle);

		void onFaceDeleted();
	}
}