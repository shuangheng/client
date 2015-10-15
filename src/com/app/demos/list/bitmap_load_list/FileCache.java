package com.app.demos.list.bitmap_load_list;

/**
 * Created by tom on 15-4-19.
 */


import android.content.Context;

public class FileCache extends AbstractFileCache {

    private String file;

    public FileCache(Context context, String file) {
        super(context, file);
        this.file = file;
    }


    @Override
    public String getSavePath(String url) {
        String filename = String.valueOf(url.hashCode());
        return getCacheDir(file) + filename;
    }

    @Override
    public String getCacheDir(String file) {

        return FileManager.getSaveFilePath(file);
    }
}

