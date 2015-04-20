package com.app.demos.list.bitmap_load_list;

/**
 * Created by tom on 15-4-19.
 */
public class FileManager {

    public static String getSaveFilePath() {
        if (CommonUtil.hasSDCard()) {
            return CommonUtil.getRootFilePath() + "com.geniusgithub/files/";
        } else {
            return CommonUtil.getRootFilePath() + "com.geniusgithub/files/";
        }
    }
}