package com.example.lab.android.nuc.draganddraw.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.PeriodicSync;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.lab.android.nuc.draganddraw.base.Box;

import java.util.ArrayList;
import java.util.List;

public class BoxDrawingView extends View{

//    private static final String TAG = "BoxDrawingView";

    private static final String
            TAG = "BoxDrawingView",
            KEY_SUPER_DATA = "key_super_data",
            KEY_BOXEN = "key_boxen";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>( );

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    //  Used when creating the view in code
    //  在代码中创建视图时使用
    public BoxDrawingView(Context context){
        this(context,null);
    }

    //  Used when inflating the view from XML
    // 从XML扩展视图时使用
    public BoxDrawingView(Context context, AttributeSet attrs){
        super(context,attrs);

        //Paint the Box a nice semitransparent red (ARGB)
        // 漆成漂亮的半透明红色(ARGB)
        mBoxPaint = new Paint( );
        mBoxPaint.setColor( 0x22ff0000 );

        //Paint the background off-white
        //  白色背景
        mBackgroundPaint = new Paint( );
        mBackgroundPaint.setColor( 0xfff8efe0 );
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF( event.getX(),event.getY());
        String action = "";

        switch (event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                action = "ACTION_DOWN";

                mCurrentBox = new Box( current);
                mBoxen.add( mCurrentBox );
                break;
            case MotionEvent.ACTION_MOVE:
                action = "ACTION_MOVE";
                if (mCurrentBox != null) {
                    if (event.getPointerCount() == 1 && mCurrentBox.getCurrentAngle() == 0) {
                        mCurrentBox.setCurrent(current);
                    }
                    if (event.getPointerCount() == 2) {
                        float angle = (float) (Math.atan((event.getY(1) - event.getY(0)) /
                                (event.getX(1) - event.getX(0))) * 180 / Math.PI);
                        Log.i(TAG, "onTouchEvent: angle:" + (angle - mCurrentBox.getOriginAngle()));
                        mCurrentBox.setCurrentAngle(mCurrentBox.getCurrentAngle() + angle
                                - mCurrentBox.getOriginAngle());
                        mCurrentBox.setOriginAngle(angle);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "POINTER_DOWN";
                if (event.getPointerCount() == 2) {
                    float angle = (float) (Math.atan((event.getY(1) - event.getY(0)) /
                            (event.getX(1) - event.getX(0))) * 180 / Math.PI);
                    mCurrentBox.setOriginAngle(angle);
                }
                break;
            case MotionEvent.ACTION_UP:
                action = "ACTION_UP";
                mCurrentBox = null;
                break;
            case MotionEvent.ACTION_CANCEL:
                action = "ACTION_CANCEL";
                mCurrentBox = null;
                break;
        }
        Log.e( TAG,action + " at x = " + current.x +
                ",y = " + current.y);
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPaint(mBackgroundPaint);

        for (Box box : mBoxen){
            float left = Math.min( box.getOrigin().x,box.getCurrent().x );
            float right = Math.max( box.getOrigin().x,box.getCurrent().x);
            float top = Math.min( box.getOrigin().y,box.getCurrent().y );
            float bottom = Math.max( box.getOrigin().y,box.getCurrent().y);
            canvas.save();
            canvas.rotate(box.getCurrentAngle(), box.getCenter().x, box.getCenter().y);
            canvas.drawRect( left,top,right,bottom,mBoxPaint);
            canvas.restore();
        }
    }



    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle( );
        // 存储父类需要存储的内容
        Parcelable superData = super.onSaveInstanceState();
        bundle.putParcelable(KEY_SUPER_DATA, superData);
        // 存储所有的矩形
        bundle.putSerializable(KEY_BOXEN, (ArrayList) mBoxen);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable( KEY_SUPER_DATA );
        mBoxen = (List<Box>) bundle.getSerializable( KEY_BOXEN );
        super.onRestoreInstanceState( superData );
        invalidate();
    }
}
