package com.gsy.ml.ui.views;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowAccomplishOrCancelBinding;

/**
 * Created by Administrator on 2017/9/2.
 */

public class AccomplishOrCancelPopuWindow extends PopupWindow implements View.OnClickListener {
    private final Context context;
    private PopuwindowAccomplishOrCancelBinding mBinding;

    public AccomplishOrCancelPopuWindow(Context context) {
        this.context = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_accomplish_or_cancel, null, false);
        mBinding.tvAccomplish.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        mBinding.tvPush.setOnClickListener(this);

        // 设置SelectPicPopupWindow的View
        this.setContentView(mBinding.getRoot());

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        //点击空白处时，隐藏掉pop窗口
        this.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(1f);

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
     * 是否显示推送订单
     *
     * @param bl
     */
    public void isPushShow(boolean bl) {
        mBinding.tvPush.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否显示完成订单
     *
     * @param bl
     */
    public void isFinishShow(boolean bl) {
        mBinding.tvAccomplish.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    /**
     * 是否显示取消订单
     *
     * @param bl
     */
    public void isCancelShow(boolean bl) {
        mBinding.tvCancel.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_accomplish:
                if (mAccomplishOrCancelListener != null) {
                    mAccomplishOrCancelListener.onClick(2);
                }
                dismiss();
                break;
            case R.id.tv_cancel:
                if (mAccomplishOrCancelListener != null) {
                    mAccomplishOrCancelListener.onClick(3);
                }
                dismiss();
                break;
            case R.id.tv_push:
                if (mAccomplishOrCancelListener != null) {
                    mAccomplishOrCancelListener.onClick(1);
                }
                dismiss();
                break;
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            backgroundAlpha(0.7f);
            this.showAsDropDown(parent, -100, 0);
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
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }


    private AccomplishOrCancelListener mAccomplishOrCancelListener;

    public void setAccomplishOrCancelListener(AccomplishOrCancelListener mAccomplishOrCancelListener) {
        this.mAccomplishOrCancelListener = mAccomplishOrCancelListener;
    }

    public interface AccomplishOrCancelListener {
        void onClick(int type);//1  推送  2 完成  3取消
    }
}
