package com.gsy.ml.ui.views;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivitySortLayoutBinding;


/**
 * Created by Administrator on 2017/4/29.
 */

public class ChooseSortPopuWindow extends PopupWindow {
    private final Context mContext;

    private ActivitySortLayoutBinding mBinding;



    public ChooseSortPopuWindow(Context context) {
        this.mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.activity_sort_layout, null, false);
        int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mBinding.getRoot());

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);


        int w = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        int h = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);

        mBinding.getRoot().measure(w, h);
        int hh = mBinding.getRoot().getMeasuredHeight();

        //设置SelectPicPopupWindow弹出窗体的高
       this.setHeight(hh);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //点击空白处时，隐藏掉pop窗口

        this.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.8f);
        //添加pop窗口关闭事件
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                backgroundAlpha(1f);
            }
        });

    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, Gravity.TOP, 0, 0);
        } else {
            // this.dismiss();
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }


}
