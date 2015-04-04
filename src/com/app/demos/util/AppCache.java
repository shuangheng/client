package com.app.demos.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.app.demos.base.BaseDevice;

public class AppCache {
	
	// tag for log
	private static String TAG = AppCache.class.getSimpleName();
	
	public static Bitmap getCachedImage (Context ctx, String url) {
		String cacheKey = AppUtil.md5(url);
		Bitmap cachedImage = SDUtil.getImage(cacheKey);
		if (cachedImage != null) {
			Log.w(TAG, "get cached image");
			return cachedImage;
		} else {
			Bitmap newImage = IOUtil.getBitmapRemote(ctx, url);

            int weidth = BaseDevice.getScreenWidth(ctx);
            Bitmap b = Bitmap.createScaledBitmap(newImage, weidth, weidth, true);
			SDUtil.saveImage(b, cacheKey);
			return b;
		}
	}
	
	public static Bitmap getImage (String url) {
		String cacheKey = AppUtil.md5(url);
		return SDUtil.getImage(cacheKey);
	}
}