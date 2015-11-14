package com.app.demos.util;

import java.text.DecimalFormat;

/**
 * Created by tom on 15-11-12.
 */
public class FileUtil {
    /**
     * 获取文件的大小
     *
     * @param fileSize
     *            文件的大小 B (字节)
     * @return  "xxxB/K/M/G"
     */
    public static String FormetFileSize(int fileSize) {// 转换文件大小
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }
}
