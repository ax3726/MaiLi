package com.gsy.ml.ui.views;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class PartnerScrollView extends ScrollView {


    public PartnerScrollView(Context context) {
        super(context);
    }

    public PartnerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int y = 0;
    private int titleHeight=0;

    public void setTitleHeight(int titleHeight) {
        this.titleHeight = titleHeight;
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        this.y = y;
    }
    private int scrollHeight=100;
    //  用于 存储 上一次 滚动的Y坐标
    private int lastY = -1;
    private boolean isfinish= false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 如果 滚动 的Y 坐标 的 最后一次 滚动到的Y坐标 一致说明  滚动已经完成
                if (y == lastY) {
            //  ScrollView滚动完成  处理相应的事件

                    if (y < titleHeight) {
                        if (!isfinish) {
                            if (y > scrollHeight) {
                                smoothScrollTo(0, titleHeight);
                                isfinish = true;
                            }
                        } else {
                            smoothScrollTo(0, 0);
                            isfinish = false;
                        }
                    } else {

                    }

                } else {
                    // 滚动的距离 和 之前的不相等 那么 再发送消息 判断一次
                    // 将滚动的 Y 坐标距离 赋值给 lastY
                    lastY = y;
                    mHandler.sendEmptyMessageDelayed(0, 10);
                }
            }
        }
    };

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            mHandler.sendEmptyMessageDelayed(0, 10);
        }
        return super.onTouchEvent(ev);
    }


}
