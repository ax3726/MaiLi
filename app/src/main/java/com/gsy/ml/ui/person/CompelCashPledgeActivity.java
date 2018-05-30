package com.gsy.ml.ui.person;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityCompelCashPledgeLayoutBinding;
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
 * Created by Administrator on 2017/6/6.
 */

public class CompelCashPledgeActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener, PayPwdPopupWindow.IPayPwdListener {
    private ActivityCompelCashPledgeLayoutBinding mBinding;
    public CashPresenter mCashPresenter = new CashPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_compel_cash_pledge_layout;
    }

    private double mRechargeReward = 0;
    private String mBankNo = "";
    private PayPwdPopupWindow mPayPwdPopupWindow;

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("提取保证金");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
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
        mBinding = (ActivityCompelCashPledgeLayoutBinding) vdb;
        mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
        mPayPwdPopupWindow.setIPayListener(this);
        mBinding.llyBank.setOnClickListener(this);
        mBinding.btnCashMoney.setOnClickListener(this);
        mRechargeReward = getIntent().getDoubleExtra("rechargeReward", 0);
        mBinding.tvTotalMoney.setText(mRechargeReward + "");
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
            case R.id.lly_bank:
                startActivity(new Intent(aty, BankCardActivity.class).putExtra("page", 1));
                break;
            case R.id.btn_cash_money:
                cashMoney();
                break;
        }
    }

    private void cashMoney() {
        if (mRechargeReward <= 0) {
            Toast.makeText(MaiLiApplication.getInstance(), "提现金额必须大于0!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mBankNo)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择要提现的银行卡号!", Toast.LENGTH_SHORT).show();
            return;
        }

        mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());

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
            EventBus.getDefault().post("更新价格");//充值成功 更新价格
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
        mBankNo = data.getBankNum();
        if (data.getBankNum().length() > 4) {
            String num = data.getBankNum().substring(data.getBankNum().length() - 4, data.getBankNum().length());
            mBinding.tvBankNo.setText("尾号" + num);
        }
    }

    @Override
    public void finishCheck() {
        showWaitDialog();
        mCashPresenter.cashBanlance(MaiLiApplication
                .getInstance().getUserInfo().getPhone(), mBankNo, mRechargeReward + "", "2");
    }
}
