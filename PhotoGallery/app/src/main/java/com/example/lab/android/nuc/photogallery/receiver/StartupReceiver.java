package com.example.lab.android.nuc.photogallery.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.lab.android.nuc.photogallery.Base.QueryPreferences;
import com.example.lab.android.nuc.photogallery.service.PollService;

public class StartupReceiver extends BroadcastReceiver {

    private static final String TAG = "StartupReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i( TAG,"Received broadcast intent: " + intent.getAction() );

        boolean isOn = QueryPreferences.isAlarmOn( context );
        PollService.setServiceAlerm( context,isOn );

    }
}
