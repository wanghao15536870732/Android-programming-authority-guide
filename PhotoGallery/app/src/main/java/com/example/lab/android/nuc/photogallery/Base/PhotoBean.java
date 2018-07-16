package com.example.lab.android.nuc.photogallery.Base;

import android.support.v4.widget.SwipeRefreshLayout;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PhotoBean {

    public static final String STATUS_OK = "ok",STATUS_FALSE = "fail";

    @SerializedName( "photos" )
    private PhotosInfo mPhotosInfo;
    @SerializedName( "stat" )
    private String mStatus;
    @SerializedName( "message" )
    private String mMessage;

    public class PhotosInfo{
        @SerializedName( "photo" )
        List<GalleryItem> mPhoto;

        public List<GalleryItem> getPhoto(){
            return mPhoto;
        }
    }

    public PhotosInfo getPhotosInfo() {
        return mPhotosInfo;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setPhotosInfo(PhotosInfo photosInfo) {
        mPhotosInfo = photosInfo;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }
}

