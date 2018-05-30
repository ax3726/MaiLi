package com.gsy.ml.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Administrator on 2017/4/21.
 */

public class SeekbarScrollView extends ScrollView {
    private SeekbarScrollListener mSeekbarScrollListener;

    public void setSeekbarScrollListener(SeekbarScrollListener seekbarScrollListener) {
        this.mSeekbarScrollListener = seekbarScrollListener;
    }

    public SeekbarScrollView(Context context) {
        super(context);
    }

    public SeekbarScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);

        if (mSeekbarScrollListener != null) {

            mSeekbarScrollListener.onScrollChanged(this, x, y, oldx, oldy);
        }
    }


    public interface SeekbarScrollListener {

        void onScrollChanged(SeekbarScrollView scrollView, int x, int y, int oldx, int oldy);

    }
}
