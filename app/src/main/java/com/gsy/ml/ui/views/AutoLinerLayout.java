package com.gsy.ml.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/6/3.
 */

public class AutoLinerLayout extends LinearLayout {
    public AutoLinerLayout(Context context) {
        super(context);
    }

    public AutoLinerLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        int width = getWidth();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w1 = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        int h1 = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        int measuredWidth = getMeasuredWidth();
        //获取该ViewGroup的实际长和宽  涉及到MeasureSpec类的使用
        int specSize_Widht = MeasureSpec.getSize(widthMeasureSpec);
        int specSize_Heigth = MeasureSpec.getSize(heightMeasureSpec);
        View childAt = getChildAt(0);
        if (childAt != null&&measuredWidth!=0) {
            childAt.measure(w1, h1);
            int width = childAt.getMeasuredWidth();
            if (width + 10 > measuredWidth) {
                Drawable background = getBackground();
                Rect bounds = background.getBounds();
                background.setBounds(bounds.left,bounds.top,bounds.right+10,bounds.bottom+10);
                setBackgroundDrawable(background);
                setMeasuredDimension(measuredWidth + 10, measuredWidth + 10);
                return;
            }
        }


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);

    }
}
