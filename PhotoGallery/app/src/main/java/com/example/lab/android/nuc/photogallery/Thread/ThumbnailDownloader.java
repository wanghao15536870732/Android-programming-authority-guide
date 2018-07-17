package com.example.lab.android.nuc.photogallery.Thread;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;

import com.example.lab.android.nuc.photogallery.Base.FlickrFetchr;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.BitSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 添加懒加载
 * @param <T>
 */
public class ThumbnailDownloader<T> extends HandlerThread{

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;
    private static final int MESSAGE_PRELOAD = 1;

    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    private ConcurrentMap<T,String> mRequestMap = new ConcurrentHashMap<>( );
    //这里使用一个标记下载请求的T类型对象



    //创建一个变量用于储存Handler变量的值
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

    /**
     * 挑战练习 预加载
     * @param <T>
     */
    private LruCache<String,Bitmap> mCache;


    //新增一个用来(在请求者和结果之间) 通信的监听器接口
    public interface ThumbnailDownloadListener<T>{
        void onThumbnailDownloaded(T target,Bitmap thumbnail);
    }

    //添加 ThumbnailDownloader的setter方法
    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    //新建构造方法提嗲原来的构造方法,返回线程的Handler
    public ThumbnailDownloader(Handler responseHandler){
        super(TAG);
        mResponseHandler = responseHandler;
    }


    @SuppressLint("HandlerLeak")
    @Override
    //该方法是在Looper首次检查消息队列之前调用的
    // 该方法成了建立Handler的最好地方了
    protected void onLooperPrepared() {
        mRequestHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                //先检查消息类型，在获取obj的值(T类型下载请求)
                if (msg.what == MESSAGE_DOWNLOAD){
                    T target = (T) msg.obj;
                    Log.i(TAG,"Got a request for URL:" + mRequestMap.get(target));

                    //再将其target传入handleRequest()返回给发当中
                    handleRequest(target);
            }else if (msg.what == MESSAGE_PRELOAD){
                    String urlToPreload = (String) msg.obj;
                    handlePreload(urlToPreload);
//                    Log.i(TAG, "Preload URL: " + urlToPreload);
                }
            }
        };
        int maxCacheSize = 4 * 1024 * 1024; //4MB
        mCache = new LruCache<>(maxCacheSize);
    }


    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    //创建名为queueThumbnai的存根方法
    //需要一个T类型对象
    public void queueThumbnail(T target,String url){
        Log.i(TAG,"Got a URL: " + url);

        if (url == null){
            mRequestMap.remove(target);
        }else {
            mRequestMap.put(target,url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD,target).sendToTarget();
        }
    }

    public void queuePreloadThumbnail(String url) {
//        Log.i(TAG, "queuePreloadThumbnail: Got a URL: " + url);
        mRequestHandler.obtainMessage(MESSAGE_PRELOAD, url).sendToTarget();
    }

    //添加消息清理机制
    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);
        mRequestHandler.removeMessages(MESSAGE_PRELOAD);
    }


    ////专门提供下载服务的类
    private void handleRequest(final T target){
        try{
            final String url = mRequestMap.get(target);

            //确认Url有效之后
            if (url == null){
                return;
            }
//            //传给FlickrFetchr新实例，并把返回的字节转化成位图
//            byte [] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
//            final Bitmap bitmap = BitmapFactory.
//                    decodeByteArray(bitmapBytes,0,bitmapBytes.length);
//            Log.i(TAG,"Bitmap created");
            final Bitmap bitmap;
            //如果没有之前的缓存
            if (mCache.get(url) == null) {
                byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
                bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                //重新加载
                Log.i(TAG, "Bitmap created");
                //该方法保存了第一次加载的uri
                mCache.put(url, bitmap);
            } else {
                //如果有就直接取出加载,
                bitmap = mCache.get(url);
                Log.i(TAG, "Bitmap from cache");
            }

            //Handler.post(Runnable)是一个发布Message的方法
            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (!mRequestMap.get( target ).equals( url ) || mHasQuit){
                        return;
                    }
                    mRequestMap.remove(target);
                    mThumbnailDownloadListener.onThumbnailDownloaded(target,bitmap);
                }
            });

        }catch (IOException e){
            Log.e(TAG,"Error downloading image",e);
        }
    }



    private void handlePreload(String url) {
        try {
            if (url == null) {
                return;
            }
            if (mCache.get(url) == null) {
                byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
                Bitmap bitmap = BitmapFactory
                        .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
                mCache.put(url, bitmap);
            }
        } catch (IOException ioe) {
            Log.e(TAG, "Error preloading image", ioe);
        }
    }

}
