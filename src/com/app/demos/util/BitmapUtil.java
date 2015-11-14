package com.app.demos.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by tom on 15-11-14.
 */
public class BitmapUtil {

    /**
     * bitmap 质量压缩
     * @param image
     * @param maxSize
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int maxSize) {
        InputStream inputStream = compressImage2(image, maxSize);

        return BitmapFactory.decodeStream(inputStream, null, null);//把ByteArrayInputStream数据生成图片
    }

    /**
     * bitmap 质量压缩
     * @param image
     * @param maxSize
     * @return
     */
    public static InputStream compressImage2(Bitmap image, int maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024 > maxSize) {  //循环判断如果压缩后图片是否大于maxSize,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        return isBm;
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     * @param srcPath
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap getCompressImage(String srcPath, float maxWidth, float maxHeight) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > maxWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / maxWidth);
        } else if (w < h && h > maxHeight) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / maxHeight);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        //return compressImage(bitmap, maxSize);//压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     * @param image
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap getCompressImage(Bitmap image, float maxWidth, float maxHeight) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if( baos.toByteArray().length / 1024>1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 50, baos);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > maxWidth) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / maxWidth);
        } else if (w < h && h > maxHeight) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / maxHeight);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        //return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
        return bitmap;
    }
}
