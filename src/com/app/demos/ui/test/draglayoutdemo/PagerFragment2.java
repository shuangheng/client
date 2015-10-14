package com.app.demos.ui.test.draglayoutdemo;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.demos.R;

public class PagerFragment2 extends Fragment {
	public Activity mActivity;
	public LayoutInflater inflater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = getActivity();
		inflater = (LayoutInflater) mActivity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
		View view = initView();
		return view;
	}

	private View initView() {
		View localView = inflater.inflate(R.layout.pager2, null);
		return localView;
	}
}
