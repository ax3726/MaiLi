package com.gsy.ml.ui.person;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityCommissionDrawMoneyLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.BankCardModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.CashPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.views.PayPwdPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/5/19.
 * 提现
 */

public class CommissionDrawMoneyActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener, PayPwdPopupWindow.IPayPwdListener {
    private ActivityCommissionDrawMoneyLayoutBinding mBinding;

    public CashPresenter mCashPresenter = new CashPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_commission_draw_money_layout;
    }

    private String mBankNo = "";
    private double mTotalMoney = 0;
    private PayPwdPopupWindow mPayPwdPopupWindow;
    private int type = 3; //  1  不提保证金  2 全部提现 包括保证金 3佣金提现

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("提现");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.tvAllMoney.setOnClickListener(this);
        mBinding.llyBank.setOnClickListener(this);
        mBinding.btnCashMoney.setOnClickListener(this);
    }

    private void cashMoney() {
        String money = mBinding.etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入提现金额!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Float.valueOf(money) == 0) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入提现金额!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mBankNo)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择要提现的银行卡号!", Toast.LENGTH_SHORT).show();
            return;
        }
        mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityCommissionDrawMoneyLayoutBinding) vdb;
        mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
        mPayPwdPopupWindow.setIPayListener(this);
        mTotalMoney = getIntent().getDoubleExtra("totalMoney", 0);
        mBinding.tvMoney.setText(mTotalMoney + "");
        mBinding.etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    double money = 0;
                    try {
                        money = Double.valueOf(s.toString().trim());
                    } catch (Exception e) {
                        money = 0;
                    }
                    if (money > mTotalMoney) {
                        String txt = mBinding.etMoney.getText().toString();
                        mBinding.etMoney.setText(txt.substring(0, txt.length() - 1));
                        Selection.setSelection(mBinding.etMoney.getText(), mBinding.etMoney.getText().length());
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mBinding.rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_yin://银行卡
                        mBinding.rbYin.setChecked(false);
                        startActivity(new Intent(aty, BankCardActivity.class).putExtra("page", 1));
                        break;
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_all_money:
                double banlance = mTotalMoney < 0 ? 0 : mTotalMoney;
                mBinding.etMoney.setText(banlance + "");
                break;
            case R.id.lly_bank:
                startActivity(new Intent(aty, BankCardActivity.class).putExtra("page", 1));
                break;
            case R.id.btn_cash_money:
                cashMoney();
                break;

        }
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post("更新佣金价格");//提现成功 更新佣金价格
            finish();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(BankCardModel.DataBean data) {
        mBinding.rgType.setVisibility(View.GONE);
        mBinding.llyBank.setVisibility(View.VISIBLE);
        mBinding.tvBankName.setText(data.getBankType());
        mBankNo = data.getBankNum();
        if (data.getBankNum().length() > 4) {
            String num = data.getBankNum().substring(data.getBankNum().length() - 4, data.getBankNum().length());
            mBinding.tvBankNo.setText("尾号" + num);
        }
    }

    @Override
    public void finishCheck() {
        showWaitDialog();
        String money = mBinding.etMoney.getText().toString().trim();
        mCashPresenter.cashBanlance(MaiLiApplication
                .getInstance().getUserInfo().getPhone(), mBankNo, money, type + "");
    }
}
