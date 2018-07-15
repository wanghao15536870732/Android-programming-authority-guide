package com.example.lab.android.nuc.criminallntent.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

public class PermissionUtils {

    private static final int REQUESTION_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSION_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermission(Activity activity){
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( activity,PERMISSION_STORAGE,
                    REQUESTION_EXTERNAL_STORAGE);
        }
    }


}
