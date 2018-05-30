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
import com.gsy.ml.databinding.PopuwindowInformShieldBinding;

/**
 * Created by Administrator on 2017/8/30.
 */

public class InformShieldPopuWindow extends PopupWindow implements View.OnClickListener {
    private final Context context;
    PopuwindowInformShieldBinding mBinding;
    private InformContentPopuwindow mInformContentPopuwindow;

    public void setText2(String txt) {
        mBinding.tvShield.setText(txt);
    }

    public InformShieldPopuWindow(Context context) {
        this.context = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_inform_shield, null, false);
        mInformContentPopuwindow = new InformContentPopuwindow(context);
        mBinding.tvInform.setOnClickListener(this);
        mBinding.tvShield.setOnClickListener(this);
        mBinding.tvLookOver.setOnClickListener(this);

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
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public void setListener(InformContentPopuwindow.InfromContentListener listener) {
        mInformContentPopuwindow.setInfromContentListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_inform:  //举报
                if (mInformShieldListener != null) {
                    mInformShieldListener.onItem(0);
                }
//              new InformPopuWindow(context).showWindow();
                mInformContentPopuwindow.showPopu();
                dismiss();
                break;
            case R.id.tv_shield:  //屏蔽消息
                if (mInformShieldListener != null) {
                    mInformShieldListener.onItem(1);
                }
                dismiss();
                break;
            case R.id.tv_look_over:  //查看简历
                if (mInformShieldListener != null) {
                    mInformShieldListener.onItem(2);
                }
                dismiss();
                break;
        }
    }



    private InformShieldListener mInformShieldListener;

    public void setInformShieldListener(InformShieldListener mInformShieldListener) {
        this.mInformShieldListener = mInformShieldListener;
    }

    public interface InformShieldListener {
        void onItem(int position);
    }
}
