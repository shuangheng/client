package com.app.demos.ui.test.draglayoutdemo;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.app.demos.R;
import com.nineoldandroids.view.ViewHelper;

/**
 * copy from "http://www.w2bc.com/Article/54439"
 * <p>at 15-10-11</p>
 */
public class MainActivity extends ActionBarActivity implements OnClickListener {
	private DragLayout mDragLayout;
	private ImageView topbarLeftImage;
	private ViewPager mViewPager;
	private List<Fragment> fragmentList;
	private MainContentLayout mMainContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draglayoutdemo_main);

		mDragLayout = (DragLayout) findViewById(R.id.dl);
		topbarLeftImage = (ImageView) findViewById(R.id.topbar_left_button);
		mViewPager = ((ViewPager) findViewById(R.id.pager_view));
		mMainContent = (MainContentLayout) findViewById(R.id.mainContent);

		mMainContent.setDragLayout(mDragLayout);
		
		
		fragmentList = new ArrayList<Fragment>();
		PagerFragment1 mPage1 = new PagerFragment1();
		PagerFragment2 mPage2 = new PagerFragment2();
		PagerFragment3 mPage3 = new PagerFragment3();
		fragmentList.add(mPage1);
		fragmentList.add(mPage2);
		fragmentList.add(mPage3);

		topbarLeftImage.setOnClickListener(this);
		mViewPager.setAdapter(new ChartTabAdapter(getSupportFragmentManager()));
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// StaticVariable.setmDragLayout(mDragLayout);
		mDragLayout.setOnLayoutDragingListener(new DragLayout.OnLayoutDragingListener() {

			@Override
			public void onOpen() {

			}

			@Override
			public void onDraging(float percent) {
				ViewHelper.setAlpha(topbarLeftImage, 1 - percent);
			}

			@Override
			public void onClose() {

			}
		});
	}

	private class ChartTabAdapter extends FragmentStatePagerAdapter {

		public ChartTabAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int postion) {
			switch (postion) {
			case 0:
				mDragLayout.setDrag(true);
				break;
			case 1:
				mDragLayout.setDrag(false);
				break;
			case 2:
				mDragLayout.setDrag(false);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.topbar_left_button:
			mDragLayout.open();
			break;
		}
	}
}
