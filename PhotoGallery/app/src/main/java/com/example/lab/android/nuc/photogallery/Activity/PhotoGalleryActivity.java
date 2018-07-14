package com.example.lab.android.nuc.photogallery.Activity;

import android.support.v4.app.Fragment;

import com.example.lab.android.nuc.photogallery.Fragment.PhotoGalleryFragment;

public class PhotoGalleryActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

}
