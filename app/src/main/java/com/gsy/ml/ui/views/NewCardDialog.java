package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.gsy.ml.R;
import com.gsy.ml.databinding.DialogNewCardBinding;
import com.gsy.ml.ui.person.VoucherActivity;

/**
 * Created by Administrator on 2017/4/12.
 */

public class NewCardDialog extends Dialog implements View.OnClickListener {
    private Context mContext;
    private DialogNewCardBinding mBinding;

    public NewCardDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_new_card, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) (d.getWidth());
        params.height = params.height;
        this.getWindow().setAttributes(params);
        mBinding.img.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img:
                mContext.startActivity(new Intent(mContext, VoucherActivity.class));
                dismiss();
                break;

        }
    }

}
