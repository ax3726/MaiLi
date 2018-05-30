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
import com.gsy.ml.databinding.ActivityArttutorLayoutBinding;
import com.gsy.ml.databinding.ActivityComputerLayoutBinding;
import com.gsy.ml.databinding.ActivityMedicinalBinding;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.workType.FactoryModel;
import com.gsy.ml.model.workType.MedicinalModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.SendOrdersPresenter;
import com.gsy.ml.prestener.home.TotalPricePresenter;
import com.gsy.ml.prestener.main.AliPayPresenter;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.home.BaseOrderActivity;
import com.gsy.ml.ui.home.EditAddressActivity;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.person.VoucherActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;
import com.gsy.ml.ui.views.DownOrderDialog;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.PayPwdPopupWindow;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;

public class MedicinalActivity extends BaseOrderActivity implements View.OnClickListener,
    DownOrderDialog.IDownOrderListener, PayPwdPopupWindow.IPayPwdListener, ILoadPVListener{
    private long mHuiheTimeLong = 0;
    private ActivityMedicinalBinding mBinding;
    private int mType = 32;
    private TotalPricePresenter mTotalPricePresenter = new TotalPricePresenter(this);
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付
    private ChooseDatePopupWindow mPricePopupWindow;
    @Override
    public int getLayoutId() {
        return R.layout.activity_medicinal;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        if (mType == 32) {
            mBinding.ilHead.tvTitle.setText("茶叶交易");
        } else {
            mBinding.ilHead.tvTitle.setText("中药材交易");
        }
        mBinding.ilHead.llyLeft.setOnClickListener(this);
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

    @Override
    public void initData() {
        super.initData();
        mType = getIntent().getIntExtra("type", 32);
        mBinding = (ActivityMedicinalBinding) vdb;
        initChooseTimeData1();
        initListener();
        initView();
        getTotalPrice(false);

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(AddressModel addressModel) {

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

    @Override
    public void onLoadComplete(Object object, int... parms) {

    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    public void downOrder(int type, double money, int money_type) {

    }

    @Override
    public void finishCheck() {

    }
    private void check() {
        String ProductName = mBinding.etProductName.getText().toString().trim();
        String PriceInfo = mBinding.etPriceInfo.getText().toString().trim();
        String Address = mBinding.etAddress.getText().toString().trim();
        String ProductNum = mBinding.etProductNum.getText().toString().trim();


        if (TextUtils.isEmpty(ProductName)) {
            DemoUtils.nope(mBinding.etProductName).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入产品名称!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Address)) {
            DemoUtils.nope(mBinding.etAddress).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入产地!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ProductNum)) {
            DemoUtils.nope(mBinding.etProductNum).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入产品数量!", Toast.LENGTH_SHORT).show();
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
        checkData();
    }


    /**
     * 计算价格
     */
    private void getTotalPrice(boolean showLoading) {


        if (showLoading) {
            showWaitDialog();
        }
        mTotalPricePresenter.getMoney(MaiLiApplication.getInstance().getUserPlace().getCity(), "3", mType + "");

    }

    private void checkData() {

        String ProductName = mBinding.etProductName.getText().toString().trim();
        String PriceInfo = mBinding.etPriceInfo.getText().toString().trim();
        String Address = mBinding.etAddress.getText().toString().trim();
        String ProductNum = mBinding.etProductNum.getText().toString().trim();
        String Content = mBinding.etSpecialContent.getText().toString().trim();

        String mudi = "";
        if (mBinding.rbSale.isChecked()) {
            mudi = mBinding.rbSale.getText().toString().trim();
        } else if (mBinding.rbToBuy.isChecked()) {
            mudi = mBinding.rbToBuy.getText().toString().trim();
        }

        if (TextUtils.isEmpty(ProductName)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入产品名称!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(Address)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入产地!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ProductNum)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入产品数量!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(PriceInfo)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入价格说明!", Toast.LENGTH_SHORT).show();
            return;
        }


        MedicinalModel medicinalModel = new MedicinalModel();
        medicinalModel.setProductName(ProductName);
        medicinalModel.setProductNum(ProductNum);
        medicinalModel.setOrigin(Address);
        medicinalModel.setPriceInfo(PriceInfo);
        medicinalModel.setReleasePurpose(mudi);
        medicinalModel.setContent(Content);

        String content = ParseJsonUtils.getjsonStr(medicinalModel);


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
                content,
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
    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(String message) {
        if ("更新价格".equals(message)) {
            checkData();
        }
    }
}