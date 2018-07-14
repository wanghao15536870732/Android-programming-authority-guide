package com.example.lab.android.nuc.photogallery.Base;

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
