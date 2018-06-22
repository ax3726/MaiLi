package com.gsy.ml.ui.home.WorkType;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityComputerLayoutBinding;
import com.gsy.ml.databinding.ActivityLandTenantBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.model.workType.LandTenantModel;
import com.gsy.ml.model.workType.OtherServiceModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.SendOrdersPresenter;
import com.gsy.ml.prestener.home.TotalPricePresenter;
import com.gsy.ml.prestener.main.AliPayPresenter;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.home.BaseOrderActivity;
import com.gsy.ml.ui.home.EditAddressActivity;
import com.gsy.ml.ui.home.SendOrderSuccessActivity;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.person.VoucherActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.PayHelper;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;
import com.gsy.ml.ui.views.DownOrderDialog;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.PayPwdPopupWindow;
import com.iflytek.cloud.thirdparty.B;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;

public class LandTenantActivity extends BaseOrderActivity implements View.OnClickListener
        , ILoadPVListener
        , DownOrderDialog.IDownOrderListener
        , PayPwdPopupWindow.IPayPwdListener {
    private ActivityLandTenantBinding mBinding;
    private ChooseDatePopupWindow mPricePopupWindow;
    private AddressModel mStartAddress;
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private int mType = 39;
    private long mHuiheTimeLong = 0;
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付

    @Override
    public int getLayoutId() {
        return R.layout.activity_land_tenant;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("土地承租");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.tvReTotal.setOnClickListener(this);
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.tvRight.setText(R.string.receipt_notice);
        mBinding.ilHead.llyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(-6)));
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mType = getIntent().getIntExtra("type", 39);
        mBinding = (ActivityLandTenantBinding) vdb;

        initChooseTimeData2();
        initListener();
        initView();

        mPricePopupWindow = new ChooseDatePopupWindow(aty, 1);
        mPricePopupWindow.setTitle("请选择要加价的价格");
        List<String> data1 = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            data1.add(String.valueOf(i * 5));
        }
        mPricePopupWindow.setData1(data1);
        mPricePopupWindow.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {

                if (mPrice + Float.valueOf(item1) < mDenomination) {
                    Toast.makeText(MaiLiApplication.getInstance(), "不满足该抵用劵的使用门槛！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAddPrice = Float.valueOf(item1);
                mTotalPrice = mPrice + mAddPrice;
                mBinding.tvPrice.setText("￥" + (mTotalPrice - mKaPrice));
                mBinding.tvAddPrice.setText("加价" + mAddPrice + "元");
            }
        });
        getTotalPrice(false);
    }

    private void initListener() {
        mBinding.btnOk.setOnClickListener(this);
        mBinding.tvNegative.setOnClickListener(this);
        mBinding.llyPreTime.setOnClickListener(this);
        mBinding.tvAddPrice.setOnClickListener(this);
        mBinding.tvReTotal.setOnClickListener(this);
        mBinding.rlyKajuan.setOnClickListener(this);
        mBinding.tvPriceInfo.setOnClickListener(this);
        mDownOrderDialog.setIDownOrderListener(this);
    }

    private void initView() {
        mHuiheTime.setTitle("信息有效期");
        mHuiheTime.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                long time = getTime1(item1, item2, item3);
                if (time != 0) {
                    mBinding.tvHuiheTime.setText(Utils.getDateToString(time, "MM月dd日HH:mm"));
                    mHuiheTimeLong = time;
                }
            }
        });
    }

    private TotalPricePresenter mTotalPricePresenter = new TotalPricePresenter(this);

    /**
     * 计算价格
     */
    private void getTotalPrice(boolean showLoading) {


        if (showLoading) {
            showWaitDialog();
        }
        mTotalPricePresenter.getMoney(MaiLiApplication.getInstance().getUserPlace().getCity(), "3", mType + "");

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_info://价格说明
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(mType)));
                break;
            case R.id.tv_from_add:  //选择地址
                startActivity(new Intent(aty, EditAddressActivity.class));
                break;
            case R.id.lly_pre_time: //选择时间
                mHuiheTime.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.tv_add_price:  //加价
                mPricePopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.tv_negative:  //取消
                finish();
                break;
            case R.id.btn_ok:      //确认
                if (MaiLiApplication.getInstance().getUserInfo().getCheckStatus() == 0) {//没有实名认证
                    InformationDialog mStateDialog = new InformationDialog(aty);
                    mStateDialog.setTitle("提示");
                    mStateDialog.setMessage("请先认证身份才能继续操作哦!");
                    mStateDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                            startActivity(new Intent(aty, IdentityCardActivity.class).putExtra("type", 1));
                        }
                    });
                    mStateDialog.show();
                } else {
                    check();
                }
                break;
            case R.id.lly_left:    //返回
                finish();
                break;
            case R.id.tv_re_total:
                getTotalPrice(true);
                break;
            case R.id.rly_kajuan://卡卷
                startActivity(new Intent(aty, VoucherActivity.class)
                        .putExtra("type", 1)
                        .putExtra("workType", mType)
                        .putExtra("orderMoney", mAddPrice + mPrice)
                );
                break;
        }
    }

    private void check() {
        String landAddress = mBinding.etLandAddress.getText().toString().trim();
        String mianji = mBinding.etMianji.getText().toString().trim();
        String PriceInfo = mBinding.etPriceInfo.getText().toString().trim();
        String time = mBinding.tvHuiheTime.getText().toString().trim();    //汇合时间

        if (TextUtils.isEmpty(landAddress)) {
            DemoUtils.nope(mBinding.etLandAddress).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入土地所在地!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(time)) {
            DemoUtils.nope(mBinding.tvHuiheTime).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择截止时间!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mianji)) {
            DemoUtils.nope(mBinding.etMianji).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入面积!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(PriceInfo)) {
            DemoUtils.nope(mBinding.etPriceInfo).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入价格说明!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPrice == 0) {
            DemoUtils.nope(mBinding.tvReTotal).start();
            Toast.makeText(MaiLiApplication.getInstance(), "价格没出来？试试点击右下角的重新计算!", Toast.LENGTH_SHORT).show();
            return;
        }
        //   checkOrder();
        checkData();
    }


    public String checkOtherTag(String biaoji) {

        return TextUtils.isEmpty(biaoji) ? "" : "、";
    }

    private void checkData() {
        String landAddress = mBinding.etLandAddress.getText().toString().trim();
        String mianji = mBinding.etMianji.getText().toString().trim();
        String PriceInfo = mBinding.etPriceInfo.getText().toString().trim();
        String time = mBinding.tvHuiheTime.getText().toString().trim();
        String Content = mBinding.etSpecialContent.getText().toString().trim();

        String mudi = "";
        if (mBinding.rbSale.isChecked()) {
            mudi = mBinding.rbSale.getText().toString().trim();
        } else if (mBinding.rbToBuy.isChecked()) {
            mudi = mBinding.rbToBuy.getText().toString().trim();
        }

        String landType = "";
        if (mBinding.rb1.isChecked()) {
            landType = mBinding.rb1.getText().toString().trim();
        } else if (mBinding.rb2.isChecked()) {
            landType = mBinding.rb2.getText().toString().trim();
        } else if (mBinding.rb3.isChecked()) {
            landType = mBinding.rb3.getText().toString().trim();
        } else if (mBinding.rb4.isChecked()) {
            landType = mBinding.rb4.getText().toString().trim();
        } else if (mBinding.rb5.isChecked()) {
            landType = mBinding.rb5.getText().toString().trim();
        }

        String biaoji = "";
        if (mBinding.rb6.isChecked()) {
            biaoji = mBinding.rb6.getText().toString().trim();
        }

        if (mBinding.rb7.isChecked()) {
            biaoji = biaoji + checkOtherTag(biaoji) + mBinding.rb7.getText().toString().trim();
        }
        if (mBinding.rb8.isChecked()) {
            biaoji = biaoji + checkOtherTag(biaoji) + mBinding.rb8.getText().toString().trim();
        }
        if (mBinding.rb9.isChecked()) {
            biaoji = biaoji + checkOtherTag(biaoji) + mBinding.rb9.getText().toString().trim();
        }
        if (mBinding.rb10.isChecked()) {
            biaoji = biaoji + checkOtherTag(biaoji) + mBinding.rb10.getText().toString().trim();
        }
        if (mBinding.rb11.isChecked()) {
            biaoji = biaoji + checkOtherTag(biaoji) + mBinding.rb11.getText().toString().trim();
        }
        if (mBinding.rb12.isChecked()) {
            biaoji = biaoji + checkOtherTag(biaoji) + mBinding.rb12.getText().toString().trim();
        }


        if (TextUtils.isEmpty(landAddress)) {
            DemoUtils.nope(mBinding.etLandAddress).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入土地所在地!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(time)) {
            DemoUtils.nope(mBinding.tvHuiheTime).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择截止时间!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mianji)) {
            DemoUtils.nope(mBinding.etMianji).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入面积!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(PriceInfo)) {
            DemoUtils.nope(mBinding.etPriceInfo).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入价格说明!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPrice == 0) {
            DemoUtils.nope(mBinding.tvReTotal).start();
            Toast.makeText(MaiLiApplication.getInstance(), "价格没出来？试试点击右下角的重新计算!", Toast.LENGTH_SHORT).show();
            return;
        }
        LandTenantModel landTenantModel = new LandTenantModel();
        landTenantModel.setReleasePurpose(mudi);
        landTenantModel.setLandType(landType);
        landTenantModel.setOtherTag(biaoji);
        landTenantModel.setLandAddress(landAddress);
        landTenantModel.setLandArea(mianji);
        landTenantModel.setPriceInfo(PriceInfo);
        landTenantModel.setContent(Content);
        landTenantModel.setEndTime(mHuiheTimeLong);


        String contents = ParseJsonUtils.getjsonStr(landTenantModel);

        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.SendOrders(userInfo.getPhone(),
                userInfo.getName(),
                mType + "",
                mHuiheTimeLong + "",
                "",
                MaiLiApplication.getInstance().getUserPlace().getProvince(),
                MaiLiApplication.getInstance().getUserPlace().getCity(),
                MaiLiApplication.getInstance().getUserPlace().getArea(),
                "",
                "",
                "",
                "",
                "",
                contents,
                String.valueOf((mPrice + mAddPrice)),
                String.valueOf((mPrice + mAddPrice)),
                "1",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "0",
                mReservationTime + "",
                mMoneyType + "",
                mCashCouponId
        );

    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if (parms.length > 0) {
                int parm = parms[0];
                if (parm == 10) {//支付宝
                    PayHelper.getInstance().AliPay(aty, info.getData());
                    PayHelper.getInstance().setIPayListener(new PayHelper.IPayListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MaiLiApplication.getInstance(), "支付成功", Toast.LENGTH_SHORT).show();
                            checkData();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(MaiLiApplication.getInstance(), "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new UpdateNotice());
                    finish();
                    startActivity(new Intent(aty, SendOrderSuccessActivity.class).putExtra("type", mType));//发单成功
                }
            }
        } else if (object instanceof PriceModel) {
            PriceModel info = (PriceModel) object;
            mBinding.rlyTotalError.setVisibility(View.GONE);
            mBinding.llyPrice.setVisibility(View.VISIBLE);
            mBinding.rlyKajuan.setVisibility(View.GONE);
            mPrice = info.getData();
            mBinding.tvPrice.setText("￥" + (mPrice + mAddPrice - mKaPrice));
        } else if (object instanceof WEXModel) {//微信
            WEXModel info = (WEXModel) object;
            PayHelper.getInstance().WexPay(info);//微信支付
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(AddressModel addressModel) {
        mStartAddress = addressModel;
       /* String name = addressModel.getName();
        String door_info = addressModel.getDoor_info();
        mBinding.tvFromAdd.setText(name + door_info);
        getTotalPrice(false);*/
    }

    @Override
    public void downOrder(int type, double money, int money_type) {
        mMoneyType = money_type;
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

                    mAliPayPresenter.getAliPay(MaiLiApplication.getInstance().getUserInfo().getPhone(), "蚂蚁快服支付订单", "蚂蚁快服平台发布" + DemoUtils.TypeToOccupation(mType) + "订单所支付的费用.", String.valueOf(money));
                }
                break;
            case 3://微信
                if (money == 0) {
                    mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
                    mPayPwdPopupWindow.setIPayListener(this);
                    mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());
                } else {
                    mAliPayPresenter.getWexPay(MaiLiApplication.getInstance().getUserInfo().getPhone(), "蚂蚁快服平台发布" + DemoUtils.TypeToOccupation(mType) + "订单所支付的费用.", "蚂蚁快服支付订单", "", String.valueOf((int) (money * 100)));
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
            checkData();
        }
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
                mDenomination = dataBean.getThreshold();

                mBinding.tvKajuan.setTextColor(getResources().getColor(R.color.colorffc000));
                mBinding.tvKajuan.setText("¥\t-" + dataBean.getFaceValue());
                mBinding.tvPrice.setText("¥" + (mPrice + mAddPrice - dataBean.getFaceValue()));
            } else {
                mKaPrice = 0;
                mCashCouponId = "";
                mDenomination = 0;
                mBinding.tvKajuan.setTextColor(getResources().getColor(R.color.colortextnomal));
                mBinding.tvKajuan.setText("未使用卡卷");
                mBinding.tvPrice.setText("¥" + (mPrice + mAddPrice));
            }
        }
    }

    @Override
    public void finishCheck() {
        checkData();
    }
}
