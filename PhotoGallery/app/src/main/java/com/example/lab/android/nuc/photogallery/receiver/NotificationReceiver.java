package com.example.lab.android.nuc.photogallery.receiver;

import android.app.Activity;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.lab.android.nuc.photogallery.service.PollService;

/**
 * 用于接受PollService当中发出的有序broadcast
 */

public class NotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "NotificationReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i( TAG,"received result: " + getResultData() );

        if (getResultCode() != Activity.RESULT_OK){
            return;
        }

        int requestCode = intent.getIntExtra( PollService.REQUEST_CODE,0 );
        Notification notification = (Notification)
                intent.getParcelableExtra( PollService.NOTIFICATION );
        NotificationManagerCompat notificationManagerCompat =
                NotificationManagerCompat.from( context );
        notificationManagerCompat.notify( requestCode,notification );

    }
}
