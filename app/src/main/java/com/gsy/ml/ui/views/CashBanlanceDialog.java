package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.gsy.ml.R;
import com.gsy.ml.databinding.DialogCashBanlanceBinding;

/**
 * Created by Administrator on 2017/4/29.
 */

public class CashBanlanceDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DialogCashBanlanceBinding mBinding;
    private double mTotalMoney = 0;
    private double mBinging = 0;

    public CashBanlanceDialog(@NonNull Context context, double totalMoney, double binging) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        mTotalMoney = totalMoney;
        mBinging = binging;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_cash_banlance, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.8);
        params.height = params.height;
        this.getWindow().setAttributes(params);
        initListener();
        initView();
    }

    private void initListener() {
        mBinding.btnOk.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);

        mBinding.imgCancel.setOnClickListener(this);
    }

    private void initView() {

        mBinding.tvBanlance.setText("你当前提现金额为" + mTotalMoney + "元");
        if (mBinging > 0) {
            mBinding.tvHint.setText("温馨提示:提现金额包含保证金" + mBinging + "元，若要提现\n保证金必须全部提现，且提现保证金后不能再继续\n接单了！");
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                dismiss();
                break;
            case R.id.btn_ok:
                if (mICashBanlanceListener != null) {
                    mICashBanlanceListener.onOk();
                }
                dismiss();
                break;
            case R.id.btn_cancel:

                dismiss();
                break;

        }
    }

    ICashBanlanceListener mICashBanlanceListener;

    public void setICashBanlanceListener(ICashBanlanceListener mICashBanlanceListener) {
        this.mICashBanlanceListener = mICashBanlanceListener;
    }

    public interface ICashBanlanceListener {
        void onOk();
    }


}
