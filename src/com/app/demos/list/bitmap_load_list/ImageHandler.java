package com.app.demos.list.bitmap_load_list;

import android.os.Handler;
import android.os.Message;

import com.app.demos.base.BaseTask;
import com.app.demos.base.C;
import com.app.demos.util.AppUtil;

/**
 * Created by tom on 15-11-23.
 */
public abstract class ImageHandler extends Handler {
    public static final int COMPLETE = 0;

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case COMPLETE:
                onComplete();
                break;
            default:
                break;
        }
    }

    public abstract void onComplete();
}
