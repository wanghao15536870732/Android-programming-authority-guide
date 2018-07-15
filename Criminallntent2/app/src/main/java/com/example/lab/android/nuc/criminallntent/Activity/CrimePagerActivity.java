package com.example.lab.android.nuc.criminallntent.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.lab.android.nuc.criminallntent.Fragment.CrimeFragment;
import com.example.lab.android.nuc.criminallntent.R;
import com.example.lab.android.nuc.criminallntent.crime.Crime;
import com.example.lab.android.nuc.criminallntent.crime.CrimeLab;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity implements CrimeFragment.Callbacks{


    private static final String EXTRA_CRIME_ID =
            "com.example.lab.android.nuc.criminallntent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;



    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        //获取crimeId
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);

        mViewPager = (ViewPager) findViewById(R.id.activity_crime_pager_view_pager);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        //设置adapter为FragmentStatePagerAdapter的一个匿名实例
        //将返回的fragment添加给托管activity，并帮助ViewPager找到fragmnet的视图并一一对应
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                //返回fragment
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        //循环查找criem的Id,找到多选crime在数组中过的位置
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    //每个托管CrimeFragment的activity都必须实现CrimeFragment.Cllbacks接口a
    //因此创建一个空的接口
    @Override
    public void onCrimeUpdated(Crime crime) {

    }
}
