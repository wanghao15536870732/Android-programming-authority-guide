package com.example.lab.android.nuc.nerdlauncher.activity;

import android.support.v4.app.Fragment;

import com.example.lab.android.nuc.nerdlauncher.fragment.NerdLauncherFragment;

public class NerdLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NerdLauncherFragment.newInstance();
    }
}
