package com.example.lab.android.nuc.criminallntent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v4.app.ActivityCompat;

/**
 * Created by 王浩 on 2018/3/27.
 */

//添加照片缩放方法
public class PictureUtils {


    //保留估算值，根据屏幕的大小估算将要缩放的程度
    public static Bitmap getScaledBitmap(String path, Activity activity){
        Point size = new Point();

        //获取屏幕的宽度和高度
        activity.getWindowManager().getDefaultDisplay().getSize(size);

        return getScaledBitmap(path,size.x,size.y);
    }

    //该方法用于缩放照片
    public static Bitmap getScaledBitmap(String path,int destWidth,int deatHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = 1;
        if (srcHeight > deatHeight || srcWidth > destWidth){
            if (srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight  / deatHeight);
            }else {
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path,options);
    }
}
