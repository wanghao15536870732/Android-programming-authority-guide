package com.example.lab.android.nuc.draganddraw.activity;

import android.support.v4.app.Fragment;

import com.example.lab.android.nuc.draganddraw.fragment.DragAndDrawFragment;

public class DragAndDrawActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return DragAndDrawFragment.newInstance();
    }
}
