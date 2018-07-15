package com.example.lab.android.nuc.criminallntent.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.example.lab.android.nuc.criminallntent.R;

public class TimePickerFragment extends DialogFragment {

    public static final String EXTRA_HOUR =
            "om.example.lab.android.nuc.criminallntent.hour";
    public static final String EXTRA_MINUTE =
            "om.example.lab.android.nuc.criminallntent.minute";

    public static final String ARG_Hour = "hour";
    public static final String ARG_Minute = "minute";
    private TimePicker timePicker;

    public static TimePickerFragment newInstance(int hour,int minute){
        Bundle arge = new Bundle( );
        arge.putSerializable(ARG_Hour,hour);
        arge.putSerializable( ARG_Minute,minute );

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments( arge );
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //转换成小时跟分钟
        int hour = (int) getArguments().getSerializable( ARG_Hour );
        int minute = (int) getArguments().getSerializable( ARG_Minute );
        View view = LayoutInflater.from( getActivity() ).inflate( R.layout.dialog_time,null);

        //找到布局控件
        timePicker = (TimePicker) view.findViewById( R.id.dialog_time_picker);
        timePicker.setCurrentHour( hour );
        timePicker.setCurrentMinute( minute );



        return new AlertDialog.Builder(getActivity())
                .setView( view )
                .setTitle( "Time of Crime" )
                .setPositiveButton( android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int hour = timePicker.getCurrentHour();
                        int minute = timePicker.getCurrentMinute();
                        sendResult( Activity.RESULT_OK,hour,minute );
                    }
                } )
                .create();
    }

    private void sendResult(int resultCode,int hour,int minute){
        if (getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent( );
        intent.putExtra( EXTRA_HOUR ,hour);
        intent.putExtra( EXTRA_MINUTE,minute);
        getTargetFragment()
                .onActivityResult( getTargetRequestCode(),resultCode,intent );

    }
}
