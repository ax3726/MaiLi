package com.gsy.ml.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class MyScrollView1 extends ScrollView {

    public MyScrollView1(Context context) {
        super(context);

    }

    public MyScrollView1(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public MyScrollView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            this.getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            this.getParent().requestDisallowInterceptTouchEvent(false);
        }
        return super.onTouchEvent(ev);
    }
}
