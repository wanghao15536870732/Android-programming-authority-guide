package com.example.lab.android.nuc.draganddraw.base;

import android.graphics.PointF;

public class Box {
    private PointF mOrigin;
    private PointF mCurrent;

    //此次按下的角度
    private float mOriginAngle;
    private float mCurrentAngle;

    public Box(PointF origin){
        mOrigin = origin;
        mCurrent = origin;
        mOriginAngle = 0;
        mCurrentAngle = 0;
    }

    public float getCurrentAngle() {
        return mCurrentAngle;
    }

    public float getOriginAngle() {
        return mOriginAngle;
    }

    public void setCurrentAngle(float currentAngle) {
        mCurrentAngle = currentAngle;
    }

    public void setOriginAngle(float originAngle) {
        mOriginAngle = originAngle;
    }

    public void setOrigin(PointF origin) {
        mOrigin = origin;
    }

    public PointF getCenter(){
        return new PointF(
                (mCurrent.x + mOrigin.x) / 2,
                (mCurrent.y + mOrigin.y) / 2
        );
    }

    public PointF getCurrent() {
        return mCurrent;
    }

    public PointF getOrigin() {
        return mOrigin;
    }

    public void setCurrent(PointF current) {
        mCurrent = current;
    }
}
