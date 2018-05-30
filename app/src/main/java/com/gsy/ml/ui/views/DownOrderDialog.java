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
import com.gsy.ml.databinding.DialogDownOrderBinding;

import ml.gsy.com.library.utils.ArithUtil;

/**
 * Created by Administrator on 2017/4/29.
 */

public class DownOrderDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private DialogDownOrderBinding mBinding;
    private double mTotalMoney = 0;
    private double mBinging = 0;
    private double mOrderMoney = 0;

    public DownOrderDialog(@NonNull Context context) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
    }

    public void initMoney(double totalMoney, double binging, double order_money) {
        mTotalMoney = totalMoney;
        mBinging = binging;
        mOrderMoney = order_money;
        if (mBinding != null) {
            initView();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_down_order, null, false);
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
        mBinding.btnYue.setOnClickListener(this);
        mBinding.btnAlipay.setOnClickListener(this);
        mBinding.btnWexpay.setOnClickListener(this);
        mBinding.btnYinlianpay.setOnClickListener(this);
        mBinding.imgCancel.setOnClickListener(this);
    }

    private void initView() {
        if (mBinging > 0) {
            mBinding.tvBanlance.setText("你当前余额为" + mTotalMoney + "元" + "\n(" + mBinging + "元保证金)");
        } else {
            mBinding.tvBanlance.setText("你当前余额为" + mTotalMoney + "元");
        }
        if (ArithUtil.sub(mTotalMoney, mBinging) < mOrderMoney) {//余额不足
            mBinding.btnYue.setBackgroundResource(R.drawable.btn_down_hui_icon);
            mBinding.imgHead.setBackgroundResource(R.drawable.down_order_yue_icon);
            mBinding.btnYue.setClickable(false);
            mBinding.tvHint.setText("当前所剩余额不足以支付该订单!");
        } else {
            mBinding.btnYue.setBackgroundResource(R.drawable.btn_down_huang_icon);
            mBinding.imgHead.setBackgroundResource(R.drawable.down_order_ri_icon);
            mBinding.btnYue.setClickable(true);
            mBinding.tvHint.setText("当前所剩余额可以支付该订单!");
        }

    }

    private IDownOrderListener mIDownOrderListener;

    public void setIDownOrderListener(IDownOrderListener mIDownOrderListener) {
        this.mIDownOrderListener = mIDownOrderListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_cancel:
                dismiss();
                break;
            case R.id.btn_yue://余额支付
                if (mIDownOrderListener != null) {
                    mIDownOrderListener.downOrder(1, 0, 1);
                }
                dismiss();
                break;
            case R.id.btn_alipay://支付宝
                if (mIDownOrderListener != null) {
                    if (mTotalMoney > mBinging) {//余额大于保证金
                        checkData(2);
                    } else {
                        mIDownOrderListener.downOrder(2, mOrderMoney, 0);
                    }

                }
                dismiss();
                break;
            case R.id.btn_wexpay://微信
                if (mIDownOrderListener != null) {
                    if (mTotalMoney > mBinging) {//余额大于保证金
                        checkData(3);
                    } else {
                        mIDownOrderListener.downOrder(3, mOrderMoney, 0);
                    }
                }
                dismiss();
                break;
            case R.id.btn_yinlianpay://银联
                if (mIDownOrderListener != null) {
                    if (mTotalMoney > mBinging) {//余额大于保证金
                        checkData(4);
                    } else {
                        mIDownOrderListener.downOrder(4, mOrderMoney, 0);
                    }
                }
                dismiss();
                break;
        }
    }

    private InformationDialog informationDialog;

    private void checkData(final int type) {

        informationDialog = new InformationDialog(mContext);
        informationDialog.setTitle("温馨提示");
        informationDialog.setMessage("您当前可用余额还剩" + (ArithUtil.sub(mTotalMoney, mBinging)) + "元,是否混合支付?");
        informationDialog.setPositiveButton("是的", new InformationDialog.IDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dlg, View view) {
                dlg.dismiss();
                if (mOrderMoney > ArithUtil.sub(mTotalMoney, mBinging)) {
                    double money = ArithUtil.sub(mOrderMoney, ArithUtil.sub(mTotalMoney, mBinging));
                    mIDownOrderListener.downOrder(type, money, 1);
                } else {
                    mIDownOrderListener.downOrder(type, 0, 1);
                }
            }
        });
        informationDialog.setNegativeButton("不用", new InformationDialog.IDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dlg, View view) {
                dlg.dismiss();
                mIDownOrderListener.downOrder(type, mOrderMoney, 1);

            }
        });
        informationDialog.show();
    }

    public interface IDownOrderListener {
        void downOrder(int type, double money, int money_type);//1 余额  2 支付宝  3 微信  4 银联   type  1  钱够  0 钱不够
    }
}
