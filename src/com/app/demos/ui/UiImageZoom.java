package com.app.demos.ui;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.app.demos.R;
import com.app.demos.base.BaseHandler;
import com.app.demos.base.BaseTask;
import com.app.demos.base.BaseUi;
import com.app.demos.base.C;
import com.app.demos.layout.RoundProgressBar;
import com.app.demos.layout.widget.photoview.PhotoView;
import com.app.demos.layout.widget.photoview.PhotoViewAttacher;
import com.app.demos.list.bitmap_load_list.FileCache;
import com.app.demos.list.bitmap_load_list.ImageLoader;
import com.app.demos.list.bitmap_load_list.ImageLoader_my;
import com.app.demos.util.AppFileDownUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by tom on 15-9-2.
 */
public class UiImageZoom extends Activity implements View.OnClickListener {
    private  RoundProgressBar progressBar;
    private PhotoView photoView;
    private View container;
    private Context context;
    private ImageLoader_my imageLoader;
    private ImageView imageReload;
    private String bgImageUrl;
    private String thumbUrl;
    private ImageDownUtils downUtils;
    private IndexHandler handler;
    private FileCache fileCache;
    private File saveFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.ui_image_zoom);
        handler = new IndexHandler();
        initView();
        displayImage();
        downUtils = new ImageDownUtils(this, handler,
                "http://f.hiphotos.baidu.com/image/h%3D200/sign=5bd83cff0c7b020813c938e152d8f25f/37d3d539b6003af30f59c83a332ac65c1138b68c.jpg");

    }

    protected static Intent setIntent(Context paramContext, Class<?> paramClass, String url, String thumbUrl)
    {
        Intent localIntent = new Intent(paramContext, paramClass);
        localIntent.putExtra("imageUrl", url);
        localIntent.putExtra("thumbnailUrl", thumbUrl);
        return localIntent;
    }

    public static void actionStart(Context paramContext, String url, String thumbUrl)
    {
        paramContext.startActivity(setIntent(paramContext, UiImageZoom.class, url, thumbUrl));
        //((Activity)paramContext).overridePendingTransition(2130968581, 0);//anim
    }

    /**
     * init view
     */
    private void initView() {
        container = findViewById(R.id.ui_image_container);
        imageReload = (ImageView) findViewById(R.id.ui_image_reload);
        photoView = (PhotoView) findViewById(R.id.ui_image_photoview);
        progressBar = (RoundProgressBar) findViewById(R.id.ui_image_progress_bar);
        progressBar.setSize(90);//设置大小

        //photoView.setImageDrawable(getResources().getDrawable(R.drawable.l_25));
        photoView.setMaxScale(10.0F);
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                finish();
            }
        });
        container.setOnClickListener(this);
        imageReload.setOnClickListener(this);
    }

    /**
     * load image
     */
    private void displayImage() {
        String url = "";
        imageLoader = new ImageLoader_my(context);
            //bgImageUrl = params.getString("bgImageUrl");
            bgImageUrl = getIntent().getStringExtra("imageUrl");
            thumbUrl = getIntent().getStringExtra("thumbnailUrl");
        if (thumbUrl != null) {
            imageLoader.DisplayImage(thumbUrl, photoView, true, false);
        }
            if (bgImageUrl != null) {
                Log.e("UiImageZoom >>url", bgImageUrl);
                url = bgImageUrl;
                progressBar.setVisibility(View.VISIBLE);


            } else {
                url = "http://f.hiphotos.baidu.com/image/h%3D200/sign=5bd83cff0c7b020813c938e152d8f25f/37d3d539b6003af30f59c83a332ac65c1138b68c.jpg";
            }
        fileCache = new FileCache(context);
        saveFilePath = fileCache.getFile(url);
    }


    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                downUtils.start();
            }
        }).start();//load image
    }

    //private void getParam

    @Override
    public void finish()
    {
        super.finish();
        //overridePendingTransition(0, R.anim.img_zoom_out_center);//动画效果
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ui_image_container:
                finish();
                break;
            case R.id.ui_image_reload:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downUtils.start();
                    }
                }).start();//load image
                imageReload.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
        }
    }

    // inner classes
    class ImageDownUtils extends Thread {

        private Context mContext;
        private Handler mHandler;
        private String mDownloadUrl; // 文件下载url，已做非空检查
        //private String mFileName;
        //private Message msg;

        //private final String APP_FOLDER = "FuYou"; // sd卡应用目录
        //private final String Down_FOLDER = "fuyouFile"; // 下载apk文件目录

        public static final int MSG_UNDOWN = 10; //未开始下载
        public static final int MSG_DOWNING = 13; // 下载中
        public static final int MSG_FINISH = 11; // 下载完成
        public static final int MSG_FAILURE = 12;// 下载失败
        private FileCache fileCache;

        public ImageDownUtils(Context context, Handler handler,
                                String downloadUrl) {
            mContext = context;
            mHandler = handler;
            mDownloadUrl = downloadUrl;
            //mFileName = fileName;
        }

        @Override
        public void run() {
            try {
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    Message downingMsg = new Message();
                    downingMsg.what = MSG_DOWNING;
                    mHandler.sendMessage(downingMsg);
                    /*/ SD卡准备好
                    File sdcardDir = Environment.getExternalStorageDirectory();
                    // 文件存放路径： sdcard/FuYou/fuyouFile
                    File folder = new File(sdcardDir + File.separator + APP_FOLDER
                            + File.separator + Down_FOLDER + File.separator);
                    Log.e(UiActionBar.TAG, "download11");
                    if (!folder.exists()) {
                        //创建存放目录
                        folder.mkdirs();
                    }
                    File saveFilePath = new File(folder, mFileName);
                    */
                    // 先从文件缓存中查找是否有
                    boolean downSuc = false;
                    if (saveFilePath != null && saveFilePath.exists()){
                        // 下载完成
                        Message msg = new Message();
                        msg.what = MSG_FINISH;
                        mHandler.sendMessage(msg);
                        downSuc = true;
                        Log.e(UiActionBar.TAG, "down");
                    } else {
                        Log.e(UiActionBar.TAG, "download");
                        downSuc = downloadFile(mDownloadUrl, saveFilePath);
                    }


                    if (downSuc) {
                        Message msg = new Message();
                        msg.what = MSG_FINISH;
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = MSG_FAILURE;
                        mHandler.sendMessage(msg);
                    }

                } else {
                    Toast.makeText(mContext, Environment.getExternalStorageState(),
                            Toast.LENGTH_SHORT).show();
                    Message msg = new Message();
                    msg.what = MSG_FAILURE;
                    mHandler.sendMessage(msg);
                }
            } catch (Exception e) {
                Log.e(UiActionBar.TAG, "AppFileDownUtils catch Exception:", e);
                Message msg = new Message();
                msg.what = MSG_FAILURE;
                mHandler.sendMessage(msg);
            } finally {
                Message msg = new Message();
                mHandler.sendMessage(msg);
            }
        }

        /**
         *
         * Desc:文件下载
         *
         * @param downloadUrl
         *            下载URL
         * @param saveFilePath
         *            保存文件路径
         * @return ture:下载成功 false:下载失败
         */
        public boolean downloadFile(String downloadUrl, File saveFilePath) {
            int fileSize = -1;
            int downFileSize = 0;
            boolean result = false;
            int progress = 0;

            try {
                URL url = new URL(downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (null == conn) {
                    return false;
                }
                // 读取超时时间 毫秒级
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    fileSize = conn.getContentLength();
                    InputStream is = conn.getInputStream();
                    FileOutputStream fos = new FileOutputStream(saveFilePath);
                    byte[] buffer = new byte[1024];
                    int i = 0;
                    int tempProgress = -1;
                    while ((i = is.read(buffer)) != -1) {
                        downFileSize = downFileSize + i;
                        // 下载进度
                        progress = (int) (downFileSize * 100.0 / fileSize);
                        fos.write(buffer, 0, i);

                        synchronized (this) {
                            if (downFileSize == fileSize) {
                                // 下载完成
                                Message msg = new Message();
                                msg.what = MSG_FINISH;
                                mHandler.sendMessage(msg);
                            } else if (tempProgress != progress) {
                                // 下载进度发生改变，则发送Message
                                Bundle bundle = new Bundle();
                                bundle.putInt("progress", progress);
                                Message msg = new Message();
                                msg.what = MSG_DOWNING;
                                msg.setData(bundle);
                                mHandler.sendMessage(msg);
                                Log.e(UiActionBar.TAG, "downloading");
                                tempProgress = progress;
                            }
                        }
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
                Log.e(UiActionBar.TAG, "downloadFile catch Exception:", e);
            }
            return result;
        }
    }

    private class IndexHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                switch (msg.what) {
                    case AppFileDownUtils.MSG_DOWNING:
                        progressBar.setProgress(msg.getData().getInt("progress"));
                        break;
                    case AppFileDownUtils.MSG_FAILURE:
                        imageReload.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        progressBar.setProgress(0);
                        break;
                    case AppFileDownUtils.MSG_FINISH:
                        progressBar.setVisibility(View.GONE);
                        InputStream is = new FileInputStream(saveFilePath);
                        Bitmap b = BitmapFactory.decodeStream(is);
                        photoView.setImageBitmap(b);
                        break;

                }
            } catch (Exception e) {
                e.printStackTrace();
                //.toast(e.getMessage());
            }
        }
    }
}
