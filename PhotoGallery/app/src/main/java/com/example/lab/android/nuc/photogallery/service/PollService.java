package com.example.lab.android.nuc.photogallery.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lab.android.nuc.photogallery.Base.FlickrFetchr;
import com.example.lab.android.nuc.photogallery.Base.GalleryItem;
import com.example.lab.android.nuc.photogallery.Base.QueryPreferences;

import java.util.List;

public class PollService extends IntentService{

    private static final String TAG = "PollService";


    public static Intent newIntent(Context context){
        return new Intent( context,PollService.class);
    }

    public PollService(String name) {
        super( name );
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (isNetWOrkAvailableAndConnected()){
            return;
        }
        String query = QueryPreferences.getStoreQuery( this );
        String lastResultId = QueryPreferences.getLastResultId( this );
        List<GalleryItem> items;

        if (query == null){
            items = new FlickrFetchr().fetchRecentPhotos();
        }else {
            items = new FlickrFetchr().searchPhotos( query );
        }

        if (items.size() == 0){
            return;
        }
        String resultId = items.get( 0 ).getId();
        if (resultId.equals(lastResultId)){
            Log.i( TAG,"Got a old result: " + resultId);
        }else {
            Log.i( TAG,"Got a new result: " + resultId );
        }

        QueryPreferences.setLastResulted( this,resultId );
    }

    public boolean isNetWOrkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        boolean isNetWorkAvailable = false;
        if (cm != null){
            isNetWorkAvailable = cm.getActiveNetworkInfo() != null;
        }
        return isNetWorkAvailable && cm.getActiveNetworkInfo().isConnected();
    }
}
