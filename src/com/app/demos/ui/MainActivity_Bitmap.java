package com.app.demos.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


import com.app.demos.R;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.list.bitmap_load_list.LoaderAdapter;

/**
 * Created by tom on 15-4-19.
 */
public class MainActivity_Bitmap extends Activity {


    /** Called when the activity is first created. */
    private ListView mListview;
    private LoaderAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViews();
    }

    @Override
    protected void onDestroy() {


        ImageLoader imageLoader = adapter.getImageLoader();
        if (imageLoader != null){
            imageLoader.clearCache();
        }

        super.onDestroy();
    }

    private void setupViews() {
        mListview = (ListView) findViewById(R.id.main_lv_list);
        adapter = new LoaderAdapter(449, this, URLS);
        mListview.setAdapter(adapter);
        mListview.setOnScrollListener(mScrollListener);
    }

    OnScrollListener mScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            switch (scrollState) {
                case OnScrollListener.SCROLL_STATE_FLING:
                    adapter.setFlagBusy(true);
                    break;
                case OnScrollListener.SCROLL_STATE_IDLE:
                    adapter.setFlagBusy(false);
                    break;
                case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                    adapter.setFlagBusy(false);
                    break;
                default:
                    break;
            }
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {

        }
    };




    private static final String[] URLS = {
            "http://10.0.2.2:8002/faces/default/l_25.jpg",
            "http://10.0.2.2:8002/faces/default/l_15.jpg",
            "http://10.0.2.2:8002/faces/default/l_13.jpg",
            "http://10.0.2.2:8002/faces/default/l_14.jpg",
            "http://10.0.2.2:8002/faces/default/l_12.jpg",
            "http://10.0.2.2:8002/faces/default/l_11.jpg",
            "http://10.0.2.2:8002/faces/default/l_26.jpg",
            "http://10.0.2.2:8002/faces/default/l_24.jpg",
            "http://10.0.2.2:8002/faces/default/l_23.jpg",
            "http://10.0.2.2:8002/faces/default/l_22.jpg",
            "http://10.0.2.2:8002/faces/default/l_21.jpg",
            "http://10.0.2.2:8002/faces/default/l_20.jpg",
            "http://10.0.2.2:8002/faces/default/l_19.jpg",
            "http://10.0.2.2:8002/faces/default/l_18.jpg",
            "http://10.0.2.2:8002/faces/default/l_9.jpg",
            "http://10.0.2.2:8002/faces/default/l_8.jpg",
            "http://10.0.2.2:8002/faces/default/l_7.jpg",
            "http://10.0.2.2:8002/faces/default/l_6.jpg",
            "http://10.0.2.2:8002/faces/default/l_5.jpg",
            "http://10.0.2.2:8002/faces/default/l_4.jpg",
            "http://10.0.2.2:8002/faces/default/l_3.jpg",
            "http://10.0.2.2:8002/faces/default/l_1.jpg",
            "http://10.0.2.2:8002/faces/default/l_2.jpg"
    };
}
