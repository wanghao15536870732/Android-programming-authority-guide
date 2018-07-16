package com.example.lab.android.nuc.photogallery.service;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.lab.android.nuc.photogallery.Activity.PhotoGalleryActivity;
import com.example.lab.android.nuc.photogallery.Base.FlickrFetchr;
import com.example.lab.android.nuc.photogallery.Base.GalleryItem;
import com.example.lab.android.nuc.photogallery.Base.QueryPreferences;
import com.example.lab.android.nuc.photogallery.R;

import java.util.List;
import java.util.ResourceBundle;

public class PollService extends IntentService {

    private static final String TAG = "PollService";

//    private static final int POLL_INTERVAL = 1000 * 60;// 60 seconds

    /*
    可以邦正在kitKat之前的设备省获得胡怎么精准的重复定时器行为
     */
    private static final long POLL_INTERVAL = AlarmManager.INTERVAL_FIFTEEN_MINUTES;

    public static Intent newIntent(Context context){
        return new Intent( context,PollService.class);
    }

    public PollService(String name) {
        super( TAG );
    }


    public static void setServiceAlerm(Context context,boolean ison){
        Intent i = PollService.newIntent( context );
        PendingIntent pi = PendingIntent.getService( context,0,i,0 );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService( Context.ALARM_SERVICE );

        if (ison){
            alarmManager.setInexactRepeating( AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(),POLL_INTERVAL,pi);
        }else {
            alarmManager.cancel( pi );
            pi.cancel();
        }
    }

    /**
     * 用于判断定时器的启动状态
     * @param context
     * @return
     */

    public static boolean isServiceAlarmOn(Context context){
        Intent intent = PollService.newIntent( context );
        /*
        FLAG_CANCEL_CURRENT该标志表示如果PendingIntent不存在，则返货null,而不是创建他
         */
        PendingIntent pi = PendingIntent
                .getService( context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT );
        return pi != null;
    }




    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (isNetWOrkAvailableAndConnected()){
            return;
        }
        Log.i( TAG,"Received an intent : " + intent);

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

            /*
            Notification的通知
             */

            Resources resources = getResources();
            Intent i = PhotoGalleryActivity.newIntent( this );
            PendingIntent pi = PendingIntent.getActivity(this,0,i,0);

            Notification notification = new NotificationCompat.Builder( this )
                    .setTicker( resources.getString( R.string.new_pictures_title))
                    .setSmallIcon( android.R.drawable.ic_menu_report_image )
                    .setContentTitle(resources.getString( R.string.new_pictures_title ))
                    .setContentText( resources.getString( R.string.new_pictures_text ) )
                    .setContentIntent( pi )
                    .setAutoCancel( true )
                    .build();

            NotificationManagerCompat notificationManagerCompat =
                    NotificationManagerCompat.from( this );
            notificationManagerCompat.notify(0,notification);
        }
        QueryPreferences.setLastResulted( this,resultId );
    }



    public boolean isNetWOrkAvailableAndConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService( CONNECTIVITY_SERVICE );
        boolean isNetWorkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isnetworkConnected = isNetWorkAvailable && cm.getActiveNetworkInfo().isConnected();
        return isnetworkConnected;
//
//        if (cm != null){
//            isNetWorkAvailable = cm.getActiveNetworkInfo() != null;
//        }
//        return isNetWorkAvailable && cm.getActiveNetworkInfo().isConnected();
    }
}
