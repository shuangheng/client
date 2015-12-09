package com.app.demos.list.bitmap_load_list;

/**
 * Created by tom on 15-4-19.
 */

import java.io.File;




import android.content.Context;
import android.util.Log;

import com.app.demos.base.BaseApp;
import com.app.demos.base.LogMy;


public abstract class AbstractFileCache {

    private String dirString;

    public AbstractFileCache(Context context, String file) {

        dirString = getCacheDir(file);
        boolean ret = FileHelper.createDirectory(dirString);
        LogMy.e(BaseApp.getContext(),"FileHelper.createDirectory:" + dirString + ", ret = " + ret);
    }

    /**
     * return fileName File
     * @param url
     * @return
     */
    public File getFile(String url) {
        File f = new File(getSavePath(url));
        return f;
    }

    /**
     * return file File
     * @return
     */
    public File getFile2() {
        File f = new File(getSavePath());
        return f;
    }

    public abstract String  getSavePath();
    public abstract String  getSavePath(String url);
    public abstract String  getCacheDir(String file);

    public void clear() {
        FileHelper.deleteDirectory(dirString);
    }

}