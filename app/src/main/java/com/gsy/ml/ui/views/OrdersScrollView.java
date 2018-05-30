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

public class OrdersScrollView extends ScrollView {
    private ScrollViewListener mScrollViewListener;

    public void setScrollViewListener(ScrollViewListener mScrollViewListener) {
        this.mScrollViewListener = mScrollViewListener;
    }

    public OrdersScrollView(Context context) {
        super(context);
    }

    public OrdersScrollView(Context context, AttributeSet attrs) {
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
        if (mScrollViewListener != null) {
            boolean show_del=false;
            if (y == 0 || y == getHeight()-titleHeight) {
                show_del = true;
            } else {
                show_del=false;
            }

            mScrollViewListener.onScrollChanged(this, x, y, oldx, oldy,show_del);
        }
    }

    //  用于 存储 上一次 滚动的Y坐标
    private int lastY = -1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                // 如果 滚动 的Y 坐标 的 最后一次 滚动到的Y坐标 一致说明  滚动已经完成
                if (y == lastY) {
            //  ScrollView滚动完成  处理相应的事件

                    if (y < getHeight() / 2) {
                        if (y >= getHeight() / 4) {
                            smoothScrollTo(0, getHeight());
                            if (mScrollViewListener != null) {
                                mScrollViewListener.onDownPage();
                            }
                        } else {

                            smoothScrollTo(0, 0);
                            if (mScrollViewListener != null) {
                                mScrollViewListener.onUpPage();
                            }
                        }
                    } else {
                        if (y <= getHeight() / 4 * 3) {
                            smoothScrollTo(0, 0);
                            if (mScrollViewListener != null) {
                                mScrollViewListener.onUpPage();
                            }
                        } else {

                            smoothScrollTo(0, getHeight());
                            if (mScrollViewListener != null) {
                                mScrollViewListener.onDownPage();
                            }
                        }
                    }
                   /*
                    if (y >= getHeight() / 4 && y < getHeight() / 2) {
                        smoothScrollTo(0, getHeight());
                    } else if (y > getHeight() / 2 && y <= getHeight() / 4 * 3) {
                        smoothScrollTo(0, 0);
                    }*/
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

    public interface ScrollViewListener {

        void onScrollChanged(OrdersScrollView scrollView, int x, int y, int oldx, int oldy,boolean show_del);
        void onUpPage();
        void onDownPage();
    }
}
