package com.gsy.ml.ui.views;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import ml.gsy.com.library.utils.ScreenUtils;

/**
 * Created by Administrator on 2017/8/11.
 */

public class DragFloatView extends LinearLayout {
    public DragFloatView(Context context) {
        super(context);
        init();
    }

    public DragFloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private int screenWidth;
    private int screenHeight;
    private int screenWidthHalf;
    private int statusHeight;
    private int virtualHeight;

    private void init() {

        screenWidth = ScreenUtils.getScreenWidth(getContext());
        screenWidthHalf = screenWidth / 2;
        screenHeight = ScreenUtils.getScreenHeight(getContext());
        statusHeight = ScreenUtils.getStatusHeight(getContext());
        virtualHeight = ScreenUtils.getVirtualBarHeigh(getContext());
    }

    private int lastX;
    private int lastY;
    private boolean isDrag;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取到手指处的横坐标和纵坐标
        int rawX = (int) event.getRawX();
       int rawY = (int) event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrag = false;
                lastX = rawX;
                lastY = rawY;
                break;
            case MotionEvent.ACTION_MOVE:
                isDrag = true;
                //计算手指移动了多少
                int dx = rawX - lastX;
                int dy = rawY - lastY;
                //这里修复一些手机无法触发点击事件的问题
                int distance= (int) Math.sqrt(dx*dx+dy*dy);
                Log.e("distance---->",distance+"");

                float x = getX() + dx;
                float y = getY() + dy;
                //检测是否到达边缘 左上右下
                x = x < 0 ? 0 : x > screenWidth - getWidth() ? screenWidth - getWidth() : x;
                // y = y < statusHeight ? statusHeight : (y + getHeight() >= screenHeight ? screenHeight - getHeight() : y);
                if (y<0){
                    y=0;
                }
                if (y>screenHeight-statusHeight-getHeight()){
                    y=screenHeight-statusHeight-getHeight();
                }
                setX(x);
                setY(y);
                lastX = rawX;
                lastY = rawY;

                break;
            case MotionEvent.ACTION_UP:

                float x2 = rawX - lastX;
                float y2 = rawY - lastY;
                if (x2<0) {
                    x2=-x2;
                }
                if (y2<0) {
                    y2=-y2;
                }
                Log.e("up","x2=="+x2+"     y2"+y2);
                if (x2<5&&y2<5) { // 距离较小，当作click事件来处理
                    moveView();
                } else {
                    if (isDrag) {
                        //恢复按压效果
                        setPressed(false);
                        Log.e("ACTION_UP---->", "getX=" + getX() + "；screenWidthHalf=" + screenWidthHalf);
                        if (rawX >= screenWidthHalf) {
                            animate().setInterpolator(new DecelerateInterpolator())
                                    .setDuration(500)
                                    .xBy(screenWidth - getWidth() - getX())
                                    .start();
                        } else {
                            ObjectAnimator oa = ObjectAnimator.ofFloat(this, "x", getX(), 0);
                            oa.setInterpolator(new DecelerateInterpolator());
                            oa.setDuration(500);
                            oa.start();
                        }
                    }
                }
                Log.e("up---->",isDrag+"");
                break;
        }
        return true;
    }

    public void moveView()
    {
        if (getX() > screenWidth - getWidth()) {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(this, "translationX",getWidth()/2+10, 0 );
            anim1.setInterpolator(new DecelerateInterpolator());
            anim1.setDuration(500);
            anim1.start();
        } else {
            ObjectAnimator anim1 = ObjectAnimator.ofFloat(this, "translationX", 0, getWidth()/2+10);
            anim1.setInterpolator(new DecelerateInterpolator());
            anim1.setDuration(500);
            anim1.start();
        }

    }

}
