package com.gsy.ml.ui.person;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPaymentBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.AliPayPresenter;
import com.gsy.ml.prestener.person.PayOrderPresenter;
import com.gsy.ml.prestener.person.WalletPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.PayHelper;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.PayPwdPopupWindow;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ml.gsy.com.library.utils.ArithUtil;

/**
 * Created by Administrator on 2017/9/2.
 */

public class PaymentActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener, PayPwdPopupWindow.IPayPwdListener {
    private ActivityPaymentBinding mBinding;
    public WalletPresenter mWalletPresenter = new WalletPresenter(this);
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付
    private PayOrderPresenter mPayOrderPresenter = new PayOrderPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment;
    }

    private OrderContent mOrderContent;//订单数据
    public double mKaPrice = 0;//卡卷
    public String mCashCouponId = "";//卡卷ID
    private double mTotalMoney = 0;
    private double mBinging = 0;
    private double mOrderMoney = 0;
    private boolean isYuPay = true;//是否可以余额支付
    private int mType = 0;


    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("支付中心");
        mBinding.ilHead.llyLeft.setOnClickListener(this);


    }

    private void getMoney() {
        mWalletPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), 1 + "", 20 + "");
    }

    private void addMoney() {
        showWaitDialog();
        mPayOrderPresenter.getPayOrder(mOrderContent.getOrder(), mOrderContent.getAcceptPhone()
                , mOrderContent.getAcceptName(), mOrderContent.getWorkCost() + "", mCashCouponId, mPayType + "");
    }

    private double mServiceMoney = 0;//信息费
    private StateModel stateModel = new StateModel();
    private int mPayType = 1;//1线上支付  0线下支付

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityPaymentBinding) vdb;
        mBinding.llyBalance.setOnClickListener(this);
        mBinding.llyZhi.setOnClickListener(this);
        mBinding.llyWei.setOnClickListener(this);
        mBinding.cvPay.setOnClickListener(this);
        mBinding.llyAuchor.setOnClickListener(this);

        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setExpandRes("获取数据失败!", R.drawable.no_data1_icon);
        mBinding.setStateModel(stateModel);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getMoney();
            }
        });
        mOrderContent = (OrderContent) getIntent().getSerializableExtra("order");
        mPayType = getIntent().getIntExtra("pay_type", 0);
        mServiceMoney = getIntent().getDoubleExtra("service_money", 0);
        mOrderMoney = mOrderContent.getWorkCost();//支付价格
        mType = mOrderContent.getWorkType();//工种

        if (mPayType == 1) {
            mBinding.tvMoney.setText(mOrderMoney + "");
        } else {
            mOrderMoney = mServiceMoney;
            mBinding.tvMoney.setText(mServiceMoney + "");
        }

        getMoney();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_balance:
                if (isYuPay) {
                    mBinding.rbnBalance.setSelected(true);
                    mBinding.rbnZhi.setSelected(false);
                    mBinding.rbnWei.setSelected(false);
                }
                break;
            case R.id.lly_zhi:
                mBinding.rbnBalance.setSelected(false);
                mBinding.rbnZhi.setSelected(true);
                mBinding.rbnWei.setSelected(false);
                break;
            case R.id.lly_wei:
                mBinding.rbnBalance.setSelected(false);
                mBinding.rbnZhi.setSelected(false);
                mBinding.rbnWei.setSelected(true);
                break;
            case R.id.lly_auchor://卡卷
                startActivity(new Intent(aty, VoucherActivity.class)
                        .putExtra("type", 1)
                        .putExtra("workType", mType)
                        .putExtra("orderMoney", mOrderMoney)
                );
                break;
            case R.id.cv_pay:

                if (mBinding.rbnBalance.isSelected()) {//余额支付
                    downOrder(1, 0, 1);

                } else if (mBinding.rbnZhi.isSelected())//支付宝
                {
                    if (mTotalMoney > mBinging) {//余额大于保证金
                        checkData(2);
                    } else {
                        downOrder(2, mOrderMoney-mKaPrice, 0);
                    }
                } else if (mBinding.rbnWei.isSelected()) {//微信
                    if (mTotalMoney > mBinging) {//余额大于保证金
                        checkData(3);
                    } else {
                        downOrder(3, mOrderMoney-mKaPrice, 0);
                    }
                }

        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof WalletModel) {
            WalletModel models = (WalletModel) object;
            setWallet(models);
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if (parms.length > 0) {

                PayHelper.getInstance().AliPay(aty, info.getData());
                PayHelper.getInstance().setIPayListener(new PayHelper.IPayListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MaiLiApplication.getInstance(), "支付成功", Toast.LENGTH_SHORT).show();
                        addMoney();
                    }

                    @Override
                    public void onFail() {
                        Toast.makeText(MaiLiApplication.getInstance(), "支付失败", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                EventBus.getDefault().post(mOrderContent);//交易  接单成功!
                finish();

            }
        } else if (object instanceof WEXModel) {//微信
            WEXModel info = (WEXModel) object;
            PayHelper.getInstance().WexPay(info);//微信支付
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }


    private void setWallet(WalletModel model) {
        if (model == null || model.getData() == null) {
            stateModel.setEmptyState(EmptyState.EXPAND);
            return;
        }
        stateModel.setEmptyState(EmptyState.NORMAL);
        mTotalMoney = DemoUtils.formatDoubleDOWN(model.getData().getTotalMoney());//总余额
        mBinging = model.getData().getDeposit();//保证金


        if (mBinging > 0) {
            mBinding.tvBinding.setVisibility(View.VISIBLE);

            mBinding.tvBinding.setText("包含保证金" + mBinging + "元");

        } else {
            mBinding.tvBinding.setVisibility(View.GONE);

        }
        if (ArithUtil.sub(mTotalMoney, mBinging) < mOrderMoney) {//余额不足
            mBinding.tvBanlance.setText("余额不足 (¥" + mTotalMoney + ")");
            mBinding.tvBanlance.setTextColor(getResources().getColor(R.color.colorFF0400));
            isYuPay = false;
        } else {
            isYuPay = true;
            mBinding.tvBanlance.setText("余额支付 (¥" + mTotalMoney + ")");
        }


    }

    private PayPwdPopupWindow mPayPwdPopupWindow;

    public void downOrder(int type, double money, int money_type) {
        // mMoneyType = money_type;
        switch (type) {
            case 1://余额
                mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
                mPayPwdPopupWindow.setIPayListener(this);
                mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case 2://支付包
                if (money == 0) {
                    mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
                    mPayPwdPopupWindow.setIPayListener(this);
                    mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());
                } else {
                    mAliPayPresenter.getAliPay(MaiLiApplication.getInstance().getUserInfo().getPhone(), "蚂蚁快服支付订单", "蚂蚁快服发布" + DemoUtils.TypeToOccupation(mType) + "订单所支付的费用.", String.valueOf(money));
                }
                break;
            case 3://微信
                if (money == 0) {
                    mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
                    mPayPwdPopupWindow.setIPayListener(this);
                    mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());
                } else {
                    mAliPayPresenter.getWexPay(MaiLiApplication.getInstance().getUserInfo().getPhone(), "蚂蚁快服发布" + DemoUtils.TypeToOccupation(mType) + "订单所支付的费用.", "蚂蚁快服订单", "", String.valueOf((int) (money * 100)));
                }
                break;
            case 4://银联
                break;
        }
    }

    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(String message) {
        if ("更新价格".equals(message)) {
            addMoney();
        }
    }

    private InformationDialog informationDialog;

    private void checkData(final int type) {

        informationDialog = new InformationDialog(aty);
        informationDialog.setTitle("温馨提示");
        informationDialog.setMessage("您当前可用余额还剩" + (ArithUtil.sub(mTotalMoney, mBinging)) + "元,是否混合支付?");
        informationDialog.setPositiveButton("是的", new InformationDialog.IDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dlg, View view) {
                dlg.dismiss();
                if ((mOrderMoney-mKaPrice) >  ArithUtil.sub(mTotalMoney, mBinging)) {
                    double money = ArithUtil.sub((mOrderMoney-mKaPrice), ArithUtil.sub(mTotalMoney, mBinging));
                    downOrder(type, money, 1);
                } else {
                    downOrder(type, 0, 1);
                }
            }
        });
        informationDialog.setNegativeButton("不用", new InformationDialog.IDialogClickListener() {
            @Override
            public void onDialogClick(Dialog dlg, View view) {
                dlg.dismiss();
                downOrder(type, (mOrderMoney-mKaPrice), 1);

            }
        });
        informationDialog.show();
    }


    /**
     * 卡卷
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(VoucherModel.DataBean dataBean) {
        if (dataBean != null) {
            if (!TextUtils.isEmpty(dataBean.getId())) {
                mKaPrice = dataBean.getFaceValue();
                mCashCouponId = dataBean.getId();
                mBinding.tvJianMoney.setTextColor(getResources().getColor(R.color.colorFF0400));
                mBinding.tvJianMoney.setText("优惠" + dataBean.getFaceValue() + "元");
            } else {
                mKaPrice = 0;
                mCashCouponId = "";
                mBinding.tvJianMoney.setTextColor(getResources().getColor(R.color.color5E5E5E));
                mBinding.tvJianMoney.setText("未使用优惠券");
            }
            double money = (mOrderMoney - mKaPrice) < 0 ? 0 : mOrderMoney - mKaPrice;
            mBinding.tvMoney.setText(money + "");
            if (ArithUtil.sub(mTotalMoney, mBinging) < money) {//余额不足
                mBinding.tvBanlance.setText("余额不足 (¥" + mTotalMoney + ")");
                mBinding.tvBanlance.setTextColor(getResources().getColor(R.color.colorFF0400));
                isYuPay = false;
            } else {
                isYuPay = true;
                mBinding.tvBanlance.setText("余额支付 (¥" + mTotalMoney + ")");
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }

    @Override
    public void finishCheck() {
        addMoney();
    }
}
