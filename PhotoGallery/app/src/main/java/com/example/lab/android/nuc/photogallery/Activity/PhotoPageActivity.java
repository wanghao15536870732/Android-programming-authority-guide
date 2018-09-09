package com.example.lab.android.nuc.photogallery.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.webkit.WebView;

import com.example.lab.android.nuc.photogallery.Fragment.PhotoPageFragment;

public class PhotoPageActivity extends SingleFragmentActivity {

    private PhotoPageFragment mPhotoPageFragment;

    public static Intent newIntent(Context context, Uri photoPageUri){
        Intent i = new Intent( context,PhotoPageActivity.class );
        i.setData( photoPageUri );
        return i;
    }


    @Override
    protected Fragment createFragment() {
        mPhotoPageFragment = PhotoPageFragment.newInstance( getIntent().getData() );
        return mPhotoPageFragment;
    }

    /**
     * 挑战练习    WebView使用后退键浏览历史网页
     *
     * 重写WebView所在Fragment绑定的Activity的onBackPressed方法
     */
    @Override
    public void onBackPressed() {
        WebView webView = mPhotoPageFragment.getWebView();
        //canGoBack判断是否有历史信息
        if (webView.canGoBack()){
            //如果有的话，就利用goBack回到前一个历史网页
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
