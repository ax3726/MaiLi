package com.gsy.ml.ui.home.WorkType;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityTourguideLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.model.workType.GuideContentModel;
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
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/29.
 * 导游类
 */

public class TourGuideActivity extends BaseOrderActivity implements View.OnClickListener
        , ILoadPVListener
        , DownOrderDialog.IDownOrderListener
        , PayPwdPopupWindow.IPayPwdListener {
    private ActivityTourguideLayoutBinding mBinding;
    private ChooseDatePopupWindow mPricePopupWindow;
    private AddressModel mStartAddress;
    private AddressModel mEndAddress;
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付
    private long mHuiheTimeLong = 0;
    private int mType = 9;


    @Override
    public int getLayoutId() {
        return R.layout.activity_tourguide_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("导游");
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
        mType = getIntent().getIntExtra("type", 9);
        mBinding = (ActivityTourguideLayoutBinding) vdb;
        mTimePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.ALL)
                .setTitleStringId("预约工作时间")
                .setThemeColor(getResources().getColor(R.color.colorTheme))
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerDialog, long time) {
                        long curtime = System.currentTimeMillis();
                        if (time >=curtime ) {//

                            long monthtime = curtime + 1000L * 60L * 60L * 24L * 7L;
                            if (time < monthtime) {
                                mBinding.tvHuiheTime.setText(Utils.getDateToString(time, "MM月dd日HH:mm"));
                                mHuiheTimeLong = time;
                            } else {
                                showToast("不能选择大于一个星期的时间!");
                            }
                        } else {
                            showToast("不能小于当前时间!");
                        }
                    }
                })
                .build();
        mBinding.rgTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                getTotalPrice(false);
            }
        });
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
        mBinding.tvFromAdds.setOnClickListener(this);   //景点
        mBinding.tvFromAdd.setOnClickListener(this);    //汇合地点
        mBinding.llyPreTime.setOnClickListener(this);
        mBinding.tvAddPrice.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        mBinding.tvNegative.setOnClickListener(this);
        mBinding.tvReTotal.setOnClickListener(this);
        mBinding.rlyKajuan.setOnClickListener(this);
        mBinding.tvPriceInfo.setOnClickListener(this);
        mBinding.etDay.addTextChangedListener(textWatcher);
        mDownOrderDialog.setIDownOrderListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            getTotalPrice(false);
        }
    };

    private TotalPricePresenter mTotalPricePresenter = new TotalPricePresenter(this);

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

    /**
     * 计算价格
     */
    private void getTotalPrice(boolean showLoading) {
        String time = mBinding.etDay.getText().toString();

        if (mStartAddress == null) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请选择景点!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (TextUtils.isEmpty(time)) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请输入需要的导游工作时间!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (showLoading) {
            showWaitDialog();
        }

        mTotalPricePresenter.getMoney(mStartAddress.getCity(), mBinding.rbTian.isChecked() ? "2" : "1", mType + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_info://价格说明
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(mType)));
                break;
            case R.id.tv_from_adds:  //景点
                startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", -2));
                break;
            case R.id.tv_from_add:  // 汇合地点
                startActivity(new Intent(aty, EditAddressActivity.class));
                break;
            case R.id.btn_ok:
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
            case R.id.lly_pre_time: //选择时间
               // mHuiheTime.showPopupWindow(mBinding.getRoot());
                mTimePickerDialog.show(getSupportFragmentManager(), "all");
                break;
            case R.id.tv_negative:
                finish();
                break;
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_re_total:
                getTotalPrice(true);
                break;
            case R.id.tv_add_price:  //加价
                mPricePopupWindow.showPopupWindow(mBinding.getRoot());
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
        String pickadd = mBinding.tvFromAdds.getText().toString().trim();  //景点地址
        String subscribe = mBinding.tvFromAdd.getText().toString();  //汇合地址
        String makeAnAppointment = mBinding.tvHuiheTime.getText().toString().trim();  //预约时间
        String day = mBinding.etDay.getText().toString().trim();     //时间


        if (TextUtils.isEmpty(pickadd)) {
            DemoUtils.nope(mBinding.tvFromAdds).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入景点地址！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(makeAnAppointment)) {
            DemoUtils.nope(mBinding.tvFromAdd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请确认时间！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(subscribe)) {
            DemoUtils.nope(mBinding.tvFromAdd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入会合地点!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(day)) {
            DemoUtils.nope(mBinding.etDay).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入工作天数或小时!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPrice == 0) {
            DemoUtils.nope(mBinding.tvReTotal).start();
            Toast.makeText(MaiLiApplication.getInstance(), "价格没出来？试试点击右下角的重新计算!", Toast.LENGTH_SHORT).show();
            return;
        }
        // checkOrder();
        checkData();
    }

    private void checkData() {
        String pickadd = mBinding.tvFromAdds.getText().toString().trim();  //景点地址
        String subscribe = mBinding.tvFromAdd.getText().toString();  //汇合地址
        String day = mBinding.etDay.getText().toString().trim();     //时间
        String jobContent = mBinding.etContent.getText().toString().trim();  //工作内容


        String danwei = "";
        if (mBinding.rbTian.isChecked()) {
            danwei = "天";
        } else if (mBinding.rbShi.isChecked()) {
            danwei = "小时";
        }

        String sex = "";      //选择性别
        if (mBinding.rbMan.isChecked()) {
            sex = mBinding.rbMan.getText().toString().trim();
        } else if (mBinding.rbWoman.isChecked()) {
            sex = mBinding.rbWoman.getText().toString().trim();
        } else if (mBinding.rbNoSex.isChecked()) {
            sex = mBinding.rbNoSex.getText().toString().trim();
        }
        if (TextUtils.isEmpty(pickadd)) {
            Toast.makeText(aty, "请输入景点地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(subscribe)) {
            Toast.makeText(aty, "请输入会合地点!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(day)) {
            Toast.makeText(aty, "请输入工作天数或小时!", Toast.LENGTH_SHORT).show();
            return;
        }


        String huihe = mEndAddress.getProvince()
                + mEndAddress.getCity()
                + mEndAddress.getDistrict()
                + mEndAddress.getName() + mEndAddress.getAddress()
                + mEndAddress.getDoor_info();
        jobContent = TextUtils.isEmpty(jobContent) ? " " : jobContent;


        GuideContentModel guideContentModel = new GuideContentModel();

        guideContentModel.setContent(jobContent);
        guideContentModel.setSex(sex);
        guideContentModel.setTimeUnit(danwei);
        guideContentModel.setTimeDuration(day);
        guideContentModel.setStartTime(mHuiheTimeLong);
        guideContentModel.setAddress(huihe);
        guideContentModel.setSpotAddress(pickadd);

        String content = ParseJsonUtils.getjsonStr(guideContentModel);

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
                mStartAddress.getName(),
                "",
                "",
                content,
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
                            Toast.makeText(aty, "支付成功", Toast.LENGTH_SHORT).show();
                            checkData();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(aty, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(aty, info.getInfo(), Toast.LENGTH_SHORT).show();
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
            String trim = mBinding.etDay.getText().toString().trim();
            if (!TextUtils.isEmpty(trim)) {
                mPrice = Float.valueOf(trim) * info.getData();
                mBinding.tvPrice.setText("￥" + (mPrice + mAddPrice - mKaPrice));
            }
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
        if (addressModel.getType() == -2) {
            mStartAddress = addressModel;
            String name = addressModel.getName();
            mBinding.tvFromAdds.setText(name);
        } else {
            mEndAddress = addressModel;
            String name = addressModel.getName();
            String door_info = addressModel.getDoor_info();
            mBinding.tvFromAdd.setText(name + door_info);
        }
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
