package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowOfflineOnLineBinding;
import com.gsy.ml.ui.person.PaymentActivity;

/**
 * Created by Administrator on 2017/8/31.
 */

public class OfflineOnLineDialog extends Dialog implements View.OnClickListener {
    private final Context mContext;
    private PopuwindowOfflineOnLineBinding mBinding;

    public OfflineOnLineDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);//是否可以取消、
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popuwindow_offline_on_line, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.8);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);
        initListener();
    }

    private void initListener() {
        mBinding.imgCancel.setOnClickListener(this); //关闭DIalog

        //线上线下Dialog
        mBinding.llyOffline.setOnClickListener(this); //线上
        mBinding.llyOnLine.setOnClickListener(this);  //线下
    }

    public void setText(String text) {
        mBinding.tvOriginalCost.setText(text);
        mBinding.tvOriginalCost.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);  //中间线
    }


    /**
     * 设置dialog显示的消息内容
     *
     * @param message 消息内容
     */
    public void setMessage(CharSequence message) {
        mBinding.tvCurrentPrice.setText(Html.fromHtml(message + ""));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:  //取消
                dismiss();
                break;
            case R.id.lly_offline:
                if (mOfflineOnLineListener != null) {
                    mOfflineOnLineListener.setType(2);
                }
                dismiss();
                break;
            case R.id.lly_on_line:
                if (mOfflineOnLineListener != null) {
                    mOfflineOnLineListener.setType(1);
                }

                dismiss();
                break;
        }
    }


    private OfflineOnLineListener mOfflineOnLineListener;

    public void setOfflineOnLineListener(OfflineOnLineListener mOfflineOnLineListener) {
        this.mOfflineOnLineListener = mOfflineOnLineListener;
    }

    public interface OfflineOnLineListener {
        void setType(int type);
    }
}
