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
import com.gsy.ml.databinding.ActivityDrawMoneyLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.BankCardModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.CashPresenter;
import com.gsy.ml.prestener.person.WalletPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.CashBanlanceDialog;
import com.gsy.ml.ui.views.PayPwdPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ml.gsy.com.library.utils.ArithUtil;

/**
 * Created by Administrator on 2017/5/19.
 * 提现
 */

public class DrawMoneyActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener, PayPwdPopupWindow.IPayPwdListener {
    private ActivityDrawMoneyLayoutBinding mBinding;
    public WalletPresenter mWalletPresenter = new WalletPresenter(this);
    public CashPresenter mCashPresenter = new CashPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_draw_money_layout;
    }

    private String mBankNo = "";
    private double mTotalMoney = 0;
    private double mBinging = 0;
    private PayPwdPopupWindow mPayPwdPopupWindow;
    private int type = 1; //  1  不提保证金  2 全部提现 包括保证金

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("提现");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.tvAllMoney.setOnClickListener(this);
        mBinding.tvYaMoney.setOnClickListener(this);
        mBinding.llyBank.setOnClickListener(this);
        mBinding.btnCashMoney.setOnClickListener(this);
    }

    private void cashMoney() {
        String money = mBinding.etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入提现金额!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (Double.valueOf(money) == 0) {
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

    private StateModel stateModel = new StateModel();

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityDrawMoneyLayoutBinding) vdb;
        mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
        mPayPwdPopupWindow.setIPayListener(this);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                checkOrder();
            }
        });
        mBinding.setStateModel(stateModel);
        checkOrder();
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

                    if (type == 1) {
                        double banlance = ArithUtil.sub(mTotalMoney, mBinging) < 0 ? 0 : ArithUtil.sub(mTotalMoney, mBinging);
                        if (money > banlance) {
                            String txt = mBinding.etMoney.getText().toString();
                            mBinding.etMoney.setText(txt.substring(0, txt.length() - 1));
                            Selection.setSelection(mBinding.etMoney.getText(), mBinding.etMoney.getText().length());
                        }
                    } else {
                        if (money > mTotalMoney) {
                            String txt = mBinding.etMoney.getText().toString();
                            mBinding.etMoney.setText(txt.substring(0, txt.length() - 1));
                            Selection.setSelection(mBinding.etMoney.getText(), mBinding.etMoney.getText().length());
                        }
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

    public void checkOrder() {

        mWalletPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), 1 + "", 20 + "");
    }

    private CashBanlanceDialog mCashBanlanceDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_all_money:
             /*   if (mBinging > 0) {
                    mCashBanlanceDialog = new CashBanlanceDialog(aty, mTotalMoney, mBinging);
                    mCashBanlanceDialog.setICashBanlanceListener(new CashBanlanceDialog.ICashBanlanceListener() {
                        @Override
                        public void onOk() {
                            type = 2;
                            mBinding.etMoney.setText(mTotalMoney + "");
                        }
                    });
                    mCashBanlanceDialog.show();
                } else {

                }*/
                double banlance = ArithUtil.sub(mTotalMoney, mBinging) < 0 ? 0 : ArithUtil.sub(mTotalMoney, mBinging);
                mBinding.etMoney.setText(banlance + "");
                break;
            case R.id.lly_bank:
                startActivity(new Intent(aty, BankCardActivity.class).putExtra("page", 1));
                break;
            case R.id.btn_cash_money:
                cashMoney();
                break;
            case R.id.tv_ya_money://保证金提现
                double yajin = mTotalMoney > mBinging ? mBinging : mTotalMoney;
                if (yajin > 0) {
                    startActivity(new Intent(aty, CompelCashPledgeActivity.class).putExtra("rechargeReward", yajin));
                    finish();
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "提现金额必须大于0!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    private void setWallet(WalletModel model) {
        if (model == null || model.getData() == null) {
            return;
        }
        mTotalMoney = DemoUtils.formatDoubleDOWN(model.getData().getTotalMoney());//总余额
        mBinging = model.getData().getDeposit();//保证金

        mBinding.tvTotalMoney.setText(mTotalMoney + "");
        if (mBinging > 0) {
            mBinding.tvTotalMoneyHint.setText("总金额(保证金" + mBinging + ")");
        } else {
            mBinding.tvTotalMoneyHint.setText("总金额");
        }

        double banlance = ArithUtil.sub(mTotalMoney, mBinging) < 0 ? 0 : ArithUtil.sub(mTotalMoney, mBinging);
        mBinding.tvMoney.setText(banlance + "");

    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof WalletModel) {
            WalletModel models = (WalletModel) object;
            stateModel.setEmptyState(EmptyState.NORMAL);
            setWallet(models);
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
        mCashPresenter.cashBanlance(MaiLiApplication.getInstance().getUserInfo().getPhone(), mBankNo, money, type + "");
    }
}
