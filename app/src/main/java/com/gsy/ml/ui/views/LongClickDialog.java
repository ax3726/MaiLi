package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.gsy.ml.R;
import com.gsy.ml.databinding.DailogLongClickBinding;

/**
 * Created by Administrator on 2017/8/31.
 */

public class LongClickDialog extends Dialog implements View.OnClickListener {
    private DailogLongClickBinding mBinding;
    private final Context mContext;

    public LongClickDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dailog_long_click, null, false);
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
        mBinding.tvStick.setOnClickListener(this);
        mBinding.tvDelete.setOnClickListener(this);
    }

    public void setTextName(String name) {
        mBinding.tvName.setText(name);
    }
    public void setTextName1(String name) {
        mBinding.tvStick.setText(name);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_stick:
                if (mLongClickListener != null) {
                    mLongClickListener.setStyle(0, 0);
                }

                dismiss();
                break;
            case R.id.tv_delete:
                if (mLongClickListener != null) {
                    mLongClickListener.setStyle(1, 0);
                }
                dismiss();
                break;
        }
    }


    private LongClickListener mLongClickListener;

    public void setLongClickListener(LongClickListener mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }

    public interface LongClickListener {
        void setStyle(int type, int postion);
    }
}
