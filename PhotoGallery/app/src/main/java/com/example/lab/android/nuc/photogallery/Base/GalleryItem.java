package com.example.lab.android.nuc.photogallery.Base;

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
