package com.example.lab.android.nuc.crizyandroid;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 王浩 on 2018/3/16.
 */

public class DrawView extends View {


    public float currentX = 40;
    public float currentY = 50;

    //自定义并创建画笔

    Paint p = new Paint();



    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置画笔的颜色
        p.setColor(Color.RED);
        //绘制一个小球
        canvas.drawCircle(currentX,currentY,105,p);

    }

    //为该组件的触碰事件重写事件处理方法

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //修改 currentX,currentY两个属性
        currentX = event.getX();
        currentY = event.getY();
        //通知当前组件重绘自己
        invalidate();
        //返回true表明该处理方法已经处理该事件
        return true;
    }


}
