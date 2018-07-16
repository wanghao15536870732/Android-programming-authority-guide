package com.example.lab.android.nuc.photogallery.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

/**
 * 挑战练习  在Android Lollipop上使用JobScheduler
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class JobSchedulerService extends JobService {
    @Override
    /*
     *如果返回值是false，该系统假定任何任务运行不需要很长时间并且到方法返回时已经完成。
     * 如果返回值是true，那么系统假设任务是需要一些时间并且负担落到你（开发者）的身上，
     * 当给定的任务完成时通过调用jobFinished(JobParameters params, boolean needsRescheduled)告知系统。
     */
    public boolean onStartJob(JobParameters params) {
        mJobHandler.sendMessage( Message.obtain(mJobHandler,1,params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return false;
    }

    private Handler mJobHandler = new Handler( new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            Toast.makeText( getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT ).show();
            jobFinished( (JobParameters) msg.obj,false );
            return true;
        }
    } );
}
