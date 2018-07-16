package com.example.lab.android.nuc.photogallery.Base;

import android.content.ContentValues;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;

import java.util.concurrent.ConcurrentMap;

public class QueryPreferences {
    private static final String PREF_SEARCH_QUERY = "searchQuery";
    private static final String PREF_LAST_RESULT_ID = "lastResultId";
    private static final String PREF_IS_ALARM_ON = "isAlarmOn";

    //通过SharePreference得到数据
    public static String getStoreQuery(Context context){
        return PreferenceManager.getDefaultSharedPreferences( context )
                .getString( PREF_SEARCH_QUERY,null );
    }
    //过SharePreference写入数据
    public static void setStoreQuery(Context context,String query){
        PreferenceManager.getDefaultSharedPreferences( context )
                .edit()
                .putString( PREF_SEARCH_QUERY,query )
                .apply();
    }

    public static String getLastResultId(Context context){
        return PreferenceManager.getDefaultSharedPreferences( context )
                .getString( PREF_LAST_RESULT_ID,null);
    }
    public static void setLastResulted(Context context,String lastResult){
        PreferenceManager.getDefaultSharedPreferences( context )
                .edit()
                .putString( PREF_LAST_RESULT_ID,lastResult )
                .apply();
    }

    public static boolean isAlarmOn(Context context){
        return PreferenceManager.getDefaultSharedPreferences( context )
                .getBoolean( PREF_IS_ALARM_ON,false );
    }

    public static void setAlarm(Context context,boolean isOn){
        PreferenceManager.getDefaultSharedPreferences( context )
                .edit()
                .putBoolean( PREF_LAST_RESULT_ID,isOn )
                .apply();
    }
}
