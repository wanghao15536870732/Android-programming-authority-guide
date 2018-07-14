package com.example.lab.android.nuc.criminallntent.Activity;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.lab.android.nuc.criminallntent.Fragment.CrimeFragment;
import com.example.lab.android.nuc.criminallntent.Fragment.CrimeListFragment;
import com.example.lab.android.nuc.criminallntent.R;
import com.example.lab.android.nuc.criminallntent.crime.Crime;


public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks{
            @Override
            protected Fragment createFragment () {
            return new CrimeListFragment();
        }

            //在该类中 覆盖getLayoutResId方法，返回双布局
            protected int getLayoutResId () {
                return R.layout.activity_twopane;

//            //再次切换布局
//            return R.layout.activity_masterdetail;
        }


    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null){
            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        }else {
            //创建一个Fragment事务
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());


            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container,newDetail)
                    .commit();
        }
    }



}
