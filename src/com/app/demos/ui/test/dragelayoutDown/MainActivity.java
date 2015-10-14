package com.app.demos.ui.test.dragelayoutDown;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.app.demos.R;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * copy from "http://www.w2bc.com/Article/54439"
 * <p>at 15-10-11</p>
 */
public class MainActivity extends ActionBarActivity implements OnClickListener {
	private DragDownLayout mDragLayout;
	private ImageView topbarLeftImage;
	private View mViewPager;
	private ListViewDrag listView;
	private RelativeLayout mMainContent;
    private boolean isOpen;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draglayout_down_main);

        mDragLayout = (DragDownLayout) findViewById(R.id.dl);
		topbarLeftImage = (ImageView) findViewById(R.id.topbar_left_button);
		mViewPager = ((View) findViewById(R.id.pager_view));
		listView = (ListViewDrag) findViewById(R.id.activity_draglayout_down_main_lv);
		mMainContent = (RelativeLayout) findViewById(R.id.mainContent);

		//mMainContent.setDragLayout(mDragLayout);
        listView.setDragLayout(mDragLayout);

		List<String> data = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			data.add(String.valueOf(i));
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.activity_list_item, android.R.id.text1, data);
		listView.setAdapter(adapter);
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                mDragLayout.setDrag(firstVisibleItem == 0);
            }
        });

        mDragLayout.setViewCanDrag(listView);

		topbarLeftImage.setOnClickListener(this);
		// StaticVariable.setmDragLayout(mDragLayout);
		mDragLayout.setOnLayoutDragingListener(new DragDownLayout.OnLayoutDragingListener() {

			@Override
			public void onOpen() {
                isOpen = true;
			}

			@Override
			public void onDraging(float percent) {
				ViewHelper.setAlpha(topbarLeftImage, 1 - percent);
			}

			@Override
			public void onClose() {
                isOpen = false;
			}
		});
        isOpen = false;
	}

    public boolean isListViewReachTopEdge(final ListView listView) {
        boolean result=false;
        if(listView.getFirstVisiblePosition()==0){
            final View topChildView = listView.getChildAt(0);
            result=topChildView.getTop()==0;
        }
        return result ;
    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            mDragLayout.close();
        } else {
            finish();
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
