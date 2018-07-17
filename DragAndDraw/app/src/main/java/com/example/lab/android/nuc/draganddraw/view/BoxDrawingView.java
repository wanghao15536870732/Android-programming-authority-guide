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

/**
 * 优化view
 */
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
                        //如果只有一只手指按下，而且还未曾旋转过的话，就进行大小的缩放
                        mCurrentBox.setCurrent(current);
                    }
                    // 如果按下了两根手指
                    if (event.getPointerCount() == 2) {
                        //获取角度
                        float angle = (float) (Math.atan((event.getY(1) - event.getY(0)) /
                                (event.getX(1) - event.getX(0))) * 180 / Math.PI);
                        Log.i(TAG, "onTouchEvent: angle:" + (angle - mCurrentBox.getOriginAngle()));
                        // 已旋转的角度 = 之前旋转的角度 + 新旋转的角度
                        // 新旋转的角度 = 本次 move 到的角度 - 手指按下的角度
                        mCurrentBox.setCurrentAngle(mCurrentBox.getCurrentAngle() + angle
                                - mCurrentBox.getOriginAngle());
                        mCurrentBox.setOriginAngle(angle);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                action = "POINTER_DOWN";
                //两根手指同时按下
                if (event.getPointerCount() == 2) {
                    // 首先获取按下时的角度（有一个弧度转角度的过程）
                    // 每次按下的时候将角度存入现在矩形的原始角度
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
            //旋转画布
            canvas.rotate(box.getCurrentAngle(), box.getCenter().x, box.getCenter().y);
            //确定矩形四个顶点的位置配上画笔即可
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
        invalidate();
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle = (Bundle) state;
        Parcelable superData = bundle.getParcelable( KEY_SUPER_DATA );
        mBoxen = (ArrayList<Box>) bundle.getSerializable( KEY_BOXEN );
        super.onRestoreInstanceState( superData );
        invalidate();
    }
}
