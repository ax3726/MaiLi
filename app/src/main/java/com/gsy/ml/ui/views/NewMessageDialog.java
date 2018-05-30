package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.databinding.DialogNewMessageBinding;
import com.gsy.ml.ui.common.WebViewActivity;

/**
 * Created by Administrator on 2017/4/12.
 */

public class NewMessageDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private DialogNewMessageBinding mBinding;
    private String mImageUrl = "";
    private String mTiaourl = "";

    public NewMessageDialog(@NonNull Context context, String imgurl, String tiaourl) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        mImageUrl = imgurl;
        mTiaourl = tiaourl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_new_message, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) (0.9*d.getWidth());
        params.height = params.height;
        this.getWindow().setAttributes(params);
        mBinding.imgCancel.setOnClickListener(this);
        mBinding.img.setOnClickListener(this);
        Glide.with(mContext).load(mImageUrl).asBitmap().into(mBinding.img);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                if (!TextUtils.isEmpty(mTiaourl)) {
                    mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                            .putExtra("url", mTiaourl));
                }

                dismiss();
                break;
            case R.id.img_cancel:
                dismiss();
                break;

        }
    }

}
