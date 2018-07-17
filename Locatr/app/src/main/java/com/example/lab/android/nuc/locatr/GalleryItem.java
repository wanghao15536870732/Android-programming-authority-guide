package com.example.lab.android.nuc.locatr;

import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * 相册的图片Item
 */

public class GalleryItem {

    //标题
    private String mCaption;
    private String mId;

    //图片资源标识符
    private String mUrl;
    private String mOwner;


    //添加经纬度
    private double mLat;
    private double mLon;

    public String getCaption() {
        return mCaption;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }


    public Uri getPhotoPageUri(){
        return Uri.parse( "http://www.flickr.com/photos/" )
                .buildUpon()
                .appendPath( mOwner )
                .appendPath( mId)
                .build();
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLon() {
        return mLon;
    }

    public void setLon(double lon) {
        mLon = lon;
    }

    @NonNull
    @Override
    public String toString() {
        return mCaption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }
}
