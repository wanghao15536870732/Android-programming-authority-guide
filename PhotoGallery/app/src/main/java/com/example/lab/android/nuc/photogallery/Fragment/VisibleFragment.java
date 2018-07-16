package com.example.lab.android.nuc.photogallery.Fragment;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.example.lab.android.nuc.photogallery.service.PollService;

public abstract class VisibleFragment extends Fragment {

    private static final String TAG = "VisibleFragment";

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter( PollService.ACTION_SHOW_NOTIFICATION );
//        getActivity().registerReceiver(mOnShowNotification,filter);

        //这样只有PhotoGallery应用才能出发目标receiver了

        getActivity().registerReceiver( mOnShowNotification,filter,
                PollService.PERM_PRIVATE,null);
    }


    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver( mOnShowNotification );
    }

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Toast.makeText( getActivity(),
//                    "Got a broadcast:" + intent.getAction(),
//                    Toast.LENGTH_SHORT )
//                    .show();
//
            Log.i( TAG,"canceling notification" );

            //此出要的是Yes 或者 No所以只要是int即可
            setResultCode( Activity.RESULT_CANCELED );
        }
    };
}
