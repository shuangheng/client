package com.app.demos.list.bitmap_load_list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogRecord;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.app.demos.base.BaseTask;
import com.app.demos.util.AppFileDownUtils;
import com.app.demos.util.BaseDevice;

/**
 * Created by tom on 15-4-19.
 * copy from " https://github.com/shuangheng/SyncLoaderBitmapDemo/ "
 */

public class ImageLoader_my {

    private MemoryCache memoryCache = new MemoryCache();
    private AbstractFileCache fileCache;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    // 线程池
    private ExecutorService executorService;
    private String TAG = ImageLoader_my.class.getName();

    public ImageLoader_my(Context context) {
        fileCache = new FileCache(context);
        executorService = Executors.newFixedThreadPool(5);
    }

    // 最主要的方法
    public void DisplayImage(String url, ImageView imageView, boolean isLoadOnlyFromCache, boolean showThumb) {
        imageViews.put(imageView, url);
        // 先从内存缓存中查找

        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else if (!isLoadOnlyFromCache){
            // 若没有的话则开启新线程加载图片
            queuePhoto(url, imageView, showThumb);
        }
    }


    private void queuePhoto(String url, ImageView imageView, boolean showThumb) {
        PhotoToLoad p = new PhotoToLoad(url, imageView, showThumb);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(Context context, String url, boolean showThumb) {
        File f = fileCache.getFile(url);
        // 先从文件缓存中查找是否有
        Bitmap b = null;
        if (f != null && f.exists()){
            if (showThumb) {
                b = decodeFile(f);//get thumb image
                if (b != null) {
                    return b;
                }
            } else {
                try {
                    InputStream is = new FileInputStream(f);
                    b = BitmapFactory.decodeStream(is);//get normal image
                    return b;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

        // 最后从指定的url中下载图片
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(10000);//毫秒(ms)  1000ms = 1s
            conn.setReadTimeout(10000);
            conn.setInstanceFollowRedirects(true);//设置这个连接是否遵循重定向。
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            CopyStream(is, os);//save image
            os.close();
            //读取图片
            if (showThumb) {
                bitmap = decodeFile(f);//get thumb image
                return bitmap;
            } else {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(f));//get normal image
                return bitmap;
            }
        } catch (Exception ex) {
            Log.e(TAG, "getBitmap catch Exception...\nmessage = " + ex.getMessage());
            //msg.what = BaseTask.IMAGE_LOAD_FAIL;
            //handler.sendMessage(msg);
            return null;
        }
    }

    // decode这个图片并且按比例缩放以减少内存消耗，虚拟机对每张图片的缓存大小也是有限制的
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 100;
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
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public boolean showThumb;

        public PhotoToLoad(String u, ImageView i,boolean b) {
            url = u;
            imageView = i;
            showThumb = b;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;

        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            Bitmap bmp = getBitmap(photoToLoad.imageView.getContext(), photoToLoad.url, photoToLoad.showThumb);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
            // 更新的操作放在UI线程中
            Activity a = (Activity) photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    /**
     * 防止图片错位
     *
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    // 用于在UI线程中更新界面
    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad))
                return;
            if (bitmap != null)
                photoToLoad.imageView.setImageBitmap(bitmap);

        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    //保存原图片
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
                Log.e("ImageLoader.copyStream", "CopyStream catch OK");
            }
        } catch (Exception ex) {
            Log.e("ImageLoader.copyStream", "CopyStream catch Exception...");
        }
    }

    //chou's 保存图片
    public static void saveImage(Context context, InputStream is, File f) {
        try {
            Bitmap bitmap;
            bitmap = BitmapFactory.decodeStream(is);

            int weidth = BaseDevice.getScreenWidth(context);
            Bitmap b = Bitmap.createScaledBitmap(bitmap, weidth, weidth, true);
            //回收
            bitmap.recycle();

            OutputStream outStream = new FileOutputStream(f);
            b.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            b.recycle();
            outStream.flush();
            outStream.close();
            System.gc();
            Log.i("ImageLoader.saveImage", "Image saved to sd");
        } catch (FileNotFoundException e) {
            Log.w("ImageLoader.saveImage", "FileNotFoundException");
        } catch (IOException e) {
            Log.w("ImageLoader.saveImage", "IOException");
        }
    }
}

