package com.gsy.ml.ui.views;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.PopuwindowInformBinding;
import com.gsy.ml.databinding.PopuwindowInformContentBinding;
import com.gsy.ml.model.home.PartTimeJobModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimePresenter;

/**
 * Created by Administrator on 2017/9/2.
 */

public class InformContentPopuwindow implements View.OnClickListener {
    PopuwindowInformContentBinding mBinding;
    private Context mContext;
    PopupWindow mPopupWindow;

    public InformContentPopuwindow(Context context) {
        mContext = context;
    }

    public void showPopu() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popuwindow_inform_content, null, false);
        mPopupWindow = new PopupWindow(mBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.lib_popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpa(false);
            }
        });

        //取消
        mBinding.tvQuxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        showPopupWindow(mBinding.getRoot());
        control();
    }

    public void control() {
        mBinding.tvSoldOut.setOnClickListener(this);
        mBinding.tvSham.setOnClickListener(this);
        mBinding.tvPolitics.setOnClickListener(this);
        mBinding.tvReal.setOnClickListener(this);
        mBinding.tvAttack.setOnClickListener(this);
        mBinding.tvAskFor.setOnClickListener(this);
        mBinding.tvAdvertising.setOnClickListener(this);
        mBinding.tvEroticism.setOnClickListener(this);
        mBinding.tvRests.setOnClickListener(this);
        mBinding.tvQuxiao.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sold_out:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(1, "该兼职已招满，还没下架");
                }
                dismiss();
                break;
            case R.id.tv_sham:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(2, "该兼职是虚假骗人的");
                }
                dismiss();
                break;
            case R.id.tv_politics:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(3, "违法-政治敏感内容");
                }
                dismiss();
                break;
            case R.id.tv_real:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(4, "发布职位不真实");
                }
                dismiss();
                break;
            case R.id.tv_attack:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(5, "人身攻击");
                }
                dismiss();
                break;
            case R.id.tv_ask_for:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(6, "索取隐私");
                }
                dismiss();
                break;
            case R.id.tv_advertising:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(7, "广告");
                }
                dismiss();
                break;
            case R.id.tv_eroticism:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(8, "色情");
                }
                dismiss();
                break;
            case R.id.tv_rests:
                if (mInfromContentListener != null) {
                    mInfromContentListener.onItem(9, "其他");
                }
                dismiss();
                break;
            case R.id.tv_quxiao:  //取消
                dismiss();
                break;
        }
    }


    /**
     * 动态设置Activity背景透明度
     *
     * @param isopen
     */
    public void setWindowAlpa(boolean isopen) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final Window window = ((Activity) mContext).getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ValueAnimator animator;
        if (isopen) {
            animator = ValueAnimator.ofFloat(1.0f, 1.0f);
        } else {
            animator = ValueAnimator.ofFloat(1.0f, 1.0f);
        }
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) Float.parseFloat(animation.getAnimatedValue().toString());
                lp.alpha = alpha;
                window.setAttributes(lp);
            }
        });
        animator.start();
    }


    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        }
        setWindowAlpa(true);
    }


    //消失PopupWindow
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }


    //接口回调
    private InfromContentListener mInfromContentListener;

    public void setInfromContentListener(InfromContentListener mInfromContentListener) {
        this.mInfromContentListener = mInfromContentListener;
    }

    public interface InfromContentListener {
        void onItem(int postion, String text);
    }
}
