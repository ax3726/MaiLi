package com.gsy.ml.ui.home.WorkType;

import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityElectricClearLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.model.person.WEXModel;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/28.
 * 电器清洗
 */

public class ElectricClearActivity extends BaseOrderActivity implements View.OnClickListener
        , ILoadPVListener
        , DownOrderDialog.IDownOrderListener
        , PayPwdPopupWindow.IPayPwdListener {
    private ActivityElectricClearLayoutBinding mBinding;
    private ChooseDatePopupWindow mPricePopupWindow;
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private AddressModel mStartAddress;
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付
    private long mHuiheTimeLong = 0;
    private int mType = 17;


    @Override
    public int getLayoutId() {
        return R.layout.activity_electric_clear_layout;
    }

    private String to = "";

    @Override
    public void initActionBar() {
        super.initActionBar();
        if (mType == 17) {
            mBinding.tvContent.setText("服务地点");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写电器服务人员的工作内容及要求");
            mBinding.ilHead.tvTitle.setText("电器服务");
            mBinding.tvNames.setText("电器描述及故障描述");
            to = "请输入服务地点!";
        } else if (mType == 14) {
            mBinding.tvContent.setText("车辆地址");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写车辆服务的工作内容及要求");
            mBinding.ilHead.tvTitle.setText("车辆服务");
            mBinding.tvNames.setText("车辆描述");
            to = "请输入服务地点!";
        } else if (mType == 16) {
            mBinding.tvContent.setText("宠物地址");
            mBinding.tvMake.setText("预约服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写宠物特点及要求");
            mBinding.ilHead.tvTitle.setText("宠物服务");
            mBinding.tvNames.setText("宠物描述及工作内容");
            to = "请输入服务地点!";
        } else if (mType == 30) {  //下水道疏通
            mBinding.tvContent.setText("服务地址");
            mBinding.tvMake.setText("服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写额外要求,若没有写无即可。");
            mBinding.ilHead.tvTitle.setText("下水道疏通");
            mBinding.tvNames.setText("工作内容");
            to = "请输入服务地点!";
        } else if (mType == 29) {  //上门开锁
            mBinding.tvContent.setText("服务地址");
            mBinding.tvMake.setText("服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写额外要求,若没有写无即可。");
            mBinding.ilHead.tvTitle.setText("上门开锁");
            mBinding.tvNames.setText("工作内容");
            to = "请输入服务地点!";
        } else if (mType == 27) {  //手机维修
            mBinding.tvContent.setText("服务地址");
            mBinding.tvMake.setText("服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写额外要求,若没有写无即可。");
            mBinding.ilHead.tvTitle.setText("手机维修");
            mBinding.tvNames.setText("工作内容");
            to = "请输入服务地点!";
        } else if (mType == 26) {  //电动车维修
            mBinding.tvContent.setText("服务地址");
            mBinding.tvMake.setText("服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写额外要求,若没有写无即可。");
            mBinding.ilHead.tvTitle.setText("电动车维修");
            mBinding.tvNames.setText("工作内容");
            to = "请输入服务地点!";
        } else if (mType == 25) {  //衣物干洗
            mBinding.tvContent.setText("服务地址");
            mBinding.tvMake.setText("服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写额外要求,若没有写无即可。");
            mBinding.ilHead.tvTitle.setText("衣物干洗");
            mBinding.tvNames.setText("工作内容");
            to = "请输入服务地点!";
        } else if (mType == 7) {  //厨师
            mBinding.tvContent.setText("服务地址");
            mBinding.tvMake.setText("服务时间");
            mBinding.tvFromAdd.setHint("请输入服务地点");
            mBinding.etSpecialContent.setHint("请填写额外要求,若没有写无即可。");
            mBinding.ilHead.tvTitle.setText("厨师");
            mBinding.tvNames.setText("工作内容");
            to = "请输入服务地点!";
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

    @Override
    public void initData() {
        super.initData();
        mType = getIntent().getIntExtra("type", 17);
        mBinding = (ActivityElectricClearLayoutBinding) vdb;

        initChooseTimeData1();
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
    }

    private void initListener() {
        mBinding.btnOk.setOnClickListener(this);
        mBinding.tvNegative.setOnClickListener(this);
        mBinding.llyPreTime.setOnClickListener(this);
        mBinding.tvAddPrice.setOnClickListener(this);
        mBinding.tvFromAdd.setOnClickListener(this);
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

    private TotalPricePresenter mTotalPricePresenter = new TotalPricePresenter(this);

    /**
     * 计算价格
     */
    private void getTotalPrice(boolean showLoading) {
        if (mStartAddress == null) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "" + to, Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (showLoading) {
            showWaitDialog();
        }
        mTotalPricePresenter.getMoney(mStartAddress.getCity(), "3", mType + "");
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
        String tiem = mBinding.tvHuiheTime.getText().toString().trim();    //汇合时间
        String content = mBinding.etSpecialContent.getText().toString().trim();  //电脑故障内容

        if (mStartAddress == null) {
            DemoUtils.nope(mBinding.tvFromAdd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "" + to, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tiem)) {
            DemoUtils.nope(mBinding.tvHuiheTime).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请确认时间!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {
            DemoUtils.nope(mBinding.etSpecialContent).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入具体描述内容!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPrice == 0) {
            DemoUtils.nope(mBinding.tvReTotal).start();
            Toast.makeText(MaiLiApplication.getInstance(), "价格没出来？试试点击右下角的重新计算!", Toast.LENGTH_SHORT).show();
            return;
        }
//        checkOrder();
        checkData();
    }

    private void checkData() {
        String tiem = mBinding.tvHuiheTime.getText().toString().trim();    //汇合时间
        String content = mBinding.etSpecialContent.getText().toString().trim();  //电脑故障内容

        if (mStartAddress == null) {

            Toast.makeText(MaiLiApplication.getInstance(), "" + to, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tiem)) {

            Toast.makeText(MaiLiApplication.getInstance(), "请确认时间!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(content)) {

            Toast.makeText(MaiLiApplication.getInstance(), "请输入具体描述内容!", Toast.LENGTH_SHORT).show();
            return;
        }


        OtherServiceModel otherServiceModel = new OtherServiceModel();
        otherServiceModel.setContent(content);
        otherServiceModel.setStartTime(mHuiheTimeLong);
        String contents = ParseJsonUtils.getjsonStr(otherServiceModel);


        String name = mStartAddress.getName() + mStartAddress.getAddress();
        String door_info = mStartAddress.getDoor_info();

        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.SendOrders(userInfo.getPhone(),
                userInfo.getName(),
                mType + "",
                mHuiheTimeLong + "",
                "",
                mStartAddress.getProvince(),
                mStartAddress.getCity(),
                mStartAddress.getDistrict(),
                mStartAddress.getPoint().getLongitude() + "",
                mStartAddress.getPoint().getLatitude() + "",
                name,
                door_info,
                "",
                contents,
                String.valueOf(mPrice + mAddPrice),
                String.valueOf(mPrice + mAddPrice),
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
            mBinding.tvPrice.setText("￥" + (mPrice + mAddPrice));
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
        String name = addressModel.getName();
        String door_info = addressModel.getDoor_info();
        mBinding.tvFromAdd.setText(name + door_info);
        getTotalPrice(false);
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
                mDenomination = 0;
                mCashCouponId = "";
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
