package com.example.lab.android.nuc.photogallery.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.lab.android.nuc.photogallery.R;

public class PhotoPageFragment extends VisibleFragment{
    private static final String ARG_URI = "photo_page_url";

    private Uri mUri;
    private WebView mWebView;
    private ProgressBar mProgressBar;

    public static PhotoPageFragment newInstance(Uri uri){
        Bundle args = new Bundle( );
        args.putParcelable( ARG_URI,uri );

        PhotoPageFragment fragment = new PhotoPageFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        mUri = getArguments().getParcelable( ARG_URI );
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_photo_page,container,false );

        mProgressBar = (ProgressBar) view.findViewById( R.id.fragment_page_progress_bar );
        mProgressBar.setMax( 100 );

        mWebView = (WebView) view.findViewById( R.id.fragment_photo_page_web_view );
        mWebView.getSettings().setJavaScriptEnabled( true );

        mWebView.setWebChromeClient( new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                                    if (newProgress == 100){
                        mProgressBar.setVisibility( View.GONE);
                    }else {
                        mProgressBar.setVisibility( View.VISIBLE );
                        mProgressBar.setProgress( newProgress );
                    }
            }

            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle( view, title );
                AppCompatActivity activity = (AppCompatActivity) getActivity();
                activity.getSupportActionBar().setSubtitle( title );
            }
        } );
        mWebView.setWebViewClient( new WebViewClient(){
            public boolean shoudOverrideUrlLoading(WebView view,String url){
                return false;
            }
        });

        if (mUri.getScheme().equals( "HTTP" ) || mUri.getScheme().equals( "HTTPS" )){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            startActivity( intent );
        }else {
            mWebView.loadUrl(mUri.toString());
        }
        return view;
    }

    public WebView getWebView(){
        return mWebView;
    }
}
