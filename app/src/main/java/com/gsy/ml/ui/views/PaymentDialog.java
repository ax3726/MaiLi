package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.gsy.ml.databinding.PopuwindowPaymentBinding;

/**
 * Created by Administrator on 2017/8/30.
 * 支付Dialog和线上线下Dialog
 */

public class PaymentDialog extends Dialog implements View.OnClickListener {
    private PopuwindowPaymentBinding mBinding;
    private Context mContext;

    public PaymentDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);//是否可以取消、
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popuwindow_payment, null, false);
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

        //支付Dialog
        mBinding.llyRemainingSum.setOnClickListener(this);//余额
        mBinding.tvWeixinPayment.setOnClickListener(this);//微信
        mBinding.tvAlipayPayment.setOnClickListener(this);//支付包

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:  //取消
                dismiss();
                break;
            case R.id.lly_remaining_sum:  //余额支付
                break;
            case R.id.tv_weixin_payment:  //微信支付
                break;
            case R.id.tv_alipay_payment:  //支付宝支付
                break;
        }
    }


    private PaymentListener mPaymentListener;

    public void setmPaymentListener(PaymentListener mPaymentListener) {
        this.mPaymentListener = mPaymentListener;
    }

    public interface PaymentListener {
        void setType(int type, int money, int moeny_type);//1 余额  2 支付宝  3 微信  4 银联   type  1  钱够  0 钱不够
    }
}
