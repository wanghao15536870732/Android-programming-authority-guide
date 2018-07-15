package com.example.lab.android.nuc.criminallntent.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.lab.android.nuc.criminallntent.R;
import com.example.lab.android.nuc.criminallntent.utils.PictureUtils;

import java.io.File;

import static android.icu.text.DateTimePatternGenerator.PatternInfo.OK;

/**
 * 挑战联系 优化照片显示
 */

public class PhotoFragment extends DialogFragment {


    public static final String EXTRA_PHOTO_PATH = "file";
    private ImageView imageview;

    public static PhotoFragment newInstance(File file){
        Bundle args = new Bundle(  );
        args.putSerializable( EXTRA_PHOTO_PATH,file );
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        File file = (File) getArguments().getSerializable( EXTRA_PHOTO_PATH);
        View view = LayoutInflater.from( getActivity() ).inflate( R.layout.dialog_photo,null);
        imageview = (ImageView) view.findViewById( R.id.crime_photo );
        Bitmap bitmap = PictureUtils.getScaledBitmap( file.getPath(),getActivity() );
        imageview.setImageBitmap( bitmap );
        return new AlertDialog.Builder( getActivity() )
                .setView( view )
                .setPositiveButton(android.R.string.ok,null )
                .create();
    }
}
