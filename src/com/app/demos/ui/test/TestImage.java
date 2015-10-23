package com.app.demos.ui.test;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.util.AppCache;
import com.app.demos.util.IOUtil;

/**
 * Created by Administrator on 15-2-24.
 */
public class TestImage extends BaseUi {

    private TextView tv;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private ImageView iv;
    private ImageView iv2;
    private ImageView iv3;
    private ImageView iv4;
    public static String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_image_layout);

        url = "http://10.0.2.2:8002/faces/default/l_25.jpg";

        this.setHandler(new IndexHandler(this));

        new Handler().postDelayed(new Runnable() {
            public void run() {
                loadImage(url);
            }
        },100);
            // Bitmap b = IOUtil.getBitmapRemote(this, "http://10.0.2.2:8002/faces/default/l_25.jpg");

        }

        private class IndexHandler extends BaseHandler {
        public IndexHandler(BaseUi ui) {
            super(ui);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case BaseTask.LOAD_IMAGE:
                        //blogListAdapter.notifyDataSetChanged();
                        Bitmap b = AppCache.getImage(url);
                        iv.setImageBitmap(b);
                        iv2.setImageBitmap(b);
                        iv3.setImageBitmap(b);
                        iv4.setImageBitmap(b);
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                ui.toast(e.getMessage());
            }
        }
    }
}
