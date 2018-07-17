package com.example.lab.android.nuc.sunset;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

/**
 * 完成挑战联系一
 */
public class SunsetFragment extends Fragment {

    private View mSceneView;
    private View mSunView;
    private View mSkyView;
    private int mBlueSkyColor;
    private int mSunsetSkyColor;
    private int mNightSkyColor;
    private boolean isSunDown = true; // 是否落日

    private boolean isFirstClick = true; // 是否第一次点击

    private ObjectAnimator downAnimatior;
    private AnimatorSet downAnimatorSet;
    private ObjectAnimator upAnimatior;
    private AnimatorSet upAnimatorSet;

    private ObjectAnimator sunsetSkyAnimator;
    private ObjectAnimator nightSkyAnimator;


    private float sunYFirstStart;
    private float sunYFirstEnd;

    public static SunsetFragment newInstance(){
        return new SunsetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_sunset,container,false );
        mSceneView = view;
        mSunView = view.findViewById( R.id.sun );
        mSkyView = view.findViewById( R.id.sky );

        Resources resources = getResources();
        mBlueSkyColor = resources.getColor( R.color.blue_sky );
        mSunsetSkyColor = resources.getColor( R.color.sunset_sky );
        mNightSkyColor = resources.getColor( R.color.night_sky );

        //先定义天空颜色

        initSkyAnimation();
        mSceneView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第一次 点击  初始化
                if (isFirstClick){
                    sunYFirstStart = mSunView.getTop();
                    sunYFirstEnd = mSkyView.getHeight();
                    initUpDownAnimation();
                }
                isFirstClick = false;
                //如果没有在执行的话，是可以改变的
                if (!downAnimatorSet.isRunning() && !upAnimatorSet.isRunning()){
                    //判断太让是否在上方
                    if (isSunDown){
                        downAnimatorSet.start();
                    }else {
                        upAnimatorSet.start();
                    }
                    isSunDown = !isSunDown;
                }
//                startAnimation();
            }
        } );
        return view;
    }

//    private void startAnimation(){
//        float sunYStart = mSunView.getTop();
//        float sunYEnd = mSkyView.getHeight();
//
//        ObjectAnimator heightAnimator = ObjectAnimator
//                    .ofFloat( mSunView,"y",sunYStart,sunYEnd )
//                    .setDuration( 3000 );
//        heightAnimator.setRepeatCount(2 );
//        //加速效果
//        heightAnimator.setInterpolator( new AccelerateDecelerateInterpolator( ));
//
//        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
//                .ofInt( mSkyView,"backgroundColor",mBlueSkyColor,mSunsetSkyColor )
//                .setDuration( 3000 );
//        //优化色彩的过度
//        sunsetSkyAnimator.setEvaluator( new ArgbEvaluator() );
//
//        ObjectAnimator nightAnimator = ObjectAnimator
//                .ofInt( mSkyView,"backgroundColor",mSunsetSkyColor,mNightSkyColor );
//        nightAnimator.setEvaluator( new ArgbEvaluator() );
//
//        //创建动画集
//        AnimatorSet animatorSet = new AnimatorSet();
//        animatorSet
//                .play( heightAnimator )
//                .with( sunsetSkyAnimator )
//                .before( nightAnimator );
//        animatorSet.start();
//
////        AnimatorSet animatorSet1 = new AnimatorSet();
////        animatorSet1
////                .play(  )
////        heightAnimator.start();
////        sunsetSkyAnimator.start();
//    }


    private void initUpDownAnimation() {
        //太阳升起的动画
        downAnimatior = ObjectAnimator.
                ofFloat(mSunView, "y", sunYFirstStart, sunYFirstEnd).
                setDuration(3000);
        downAnimatior.setInterpolator(new AccelerateInterpolator());
        downAnimatorSet = new AnimatorSet();
        downAnimatorSet.
                play(downAnimatior).
                with(sunsetSkyAnimator);

        //太阳降落的动画
        upAnimatior = ObjectAnimator.
                ofFloat(mSunView, "y", sunYFirstEnd, sunYFirstStart).
                setDuration(3000);
        upAnimatior.setInterpolator(new AccelerateInterpolator());
        upAnimatorSet = new AnimatorSet();
        upAnimatorSet.
                play(upAnimatior).
                with(nightSkyAnimator);
    }


    private void initSkyAnimation() {
        //落日的天空颜色变化
        sunsetSkyAnimator = ObjectAnimator.
                ofInt(mSkyView, "backgroundColor", mBlueSkyColor, mSunsetSkyColor).
                setDuration(3000);
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        //日出的天空颜色变化
        nightSkyAnimator = ObjectAnimator.
                ofInt(mSkyView, "backgroundColor", mSunsetSkyColor, mBlueSkyColor).
                setDuration(3000);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator()); }


}
