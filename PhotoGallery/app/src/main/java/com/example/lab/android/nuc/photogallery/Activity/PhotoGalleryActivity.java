package com.example.lab.android.nuc.photogallery.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.lab.android.nuc.photogallery.Fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity{

    public static Intent newIntent(Context context){
        return new Intent( context,PhotoGalleryActivity.class );
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

}
