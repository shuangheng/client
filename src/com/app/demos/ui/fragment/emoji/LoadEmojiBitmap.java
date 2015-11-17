package com.app.demos.ui.fragment.emoji;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.demos.layout.Utils;
import com.app.demos.list.bitmap_load_list.MemoryCache;

/**
 * Created by tom on 15-11-17.
 */
public class LoadEmojiBitmap {

    private static LoadEmojiBitmap loadEmojiBitmap;
    private MemoryCache memoryCache = new MemoryCache();
    private Context context;

    private LoadEmojiBitmap(Context mContext) {
        this.context = mContext;
    }

    public static LoadEmojiBitmap getInstance(Context mContext) {
        if (loadEmojiBitmap == null) {
            loadEmojiBitmap = new LoadEmojiBitmap(mContext);
        }
        return loadEmojiBitmap;
    }

    // 最主要的方法
    public Bitmap getBitmap(int resId) {
        // 先从内存缓存中查找
        Bitmap bitmap = memoryCache.get(String.valueOf(resId));
        if (bitmap != null)
            return bitmap;
        else {
            //bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
            bitmap = decodeResource(context, resId);
            memoryCache.put(String.valueOf(resId), bitmap);
            return bitmap;
        }
    }

    /**
     * decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
     */
    public Bitmap decodeResource(Context context, int resId) {
        // decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resId, o);

        // Find the correct scale value. It should be the power of 2.
        final int REQUIRED_SIZE = Utils.dpToPx(19, context.getResources());;
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeResource(context.getResources(), resId, o2);
    }
}
