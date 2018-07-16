package com.example.lab.android.nuc.photogallery.Activity;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;

import com.example.lab.android.nuc.photogallery.Fragment.PhotoGalleryFragment;
import com.example.lab.android.nuc.photogallery.service.JobSchedulerService;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PhotoGalleryActivity extends SingleFragmentActivity{

    public static Intent newIntent(Context context){
        return new Intent( context,PhotoGalleryActivity.class );
    }

    @Override
    protected Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }

//    JobScheduler mJobScheduler = (JobScheduler) getSystemService( Context.JOB_SCHEDULER_SERVICE );
//
//    JobInfo.Builder builder = new JobInfo.Builder( 1,
//            new ComponentName( getPackageName(),
//                    JobSchedulerService.class.getName()));


}
