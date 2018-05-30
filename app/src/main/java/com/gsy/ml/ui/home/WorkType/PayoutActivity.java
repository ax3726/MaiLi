package com.gsy.ml.ui.home.WorkType;

import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPayoutLayoutBinding;
import com.gsy.ml.databinding.ItemAddAddressBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.SpeedinessShipmentsModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.model.workType.CityServiceModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.SendOrdersPresenter;
import com.gsy.ml.prestener.main.AliPayPresenter;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.home.BaseOrderActivity;
import com.gsy.ml.ui.home.EditAddressActivity;
import com.gsy.ml.ui.home.SendOrderSuccessActivity;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.person.UsualAddressActivity;
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
 * Created by Administrator on 2017/4/26.
 * 同城配送类
 */

public class PayoutActivity extends BaseOrderActivity implements View.OnClickListener
        , ChooseDatePopupWindow.IOccupationListener
        , DownOrderDialog.IDownOrderListener
        , ILoadPVListener
        , PayPwdPopupWindow.IPayPwdListener {
    private ActivityPayoutLayoutBinding mBinding;
    private ChooseDatePopupWindow mOccupationPopupWindow;
    private ChooseDatePopupWindow mPricePopupWindow;
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private String mDistance = "";//配送距离
    private UserInfoModel.UserInfoBean user;
    private int mType = 1;
    private List<ItemAddAddressBinding> mBindings = new ArrayList<>();
    private List<AddressModel> mAddressModel = new ArrayList<>();
    private AddressModel mStartAddress;
    private AddressModel mEndAddress;
    private String wmOrderId="";
    private double mAddPrice = 0;//加价

    @Override
    public int getLayoutId() {
        return R.layout.activity_payout_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("同城配送");
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

    /**
     * 处理传递过来的数据
     */
    private void preData() {
        SpeedinessShipmentsModel.DataBean.RowsBean data_one = getIntent().getParcelableExtra("data_one");
        ArrayList<SpeedinessShipmentsModel.DataBean.RowsBean> data_list = getIntent().getParcelableArrayListExtra("data_list");
        if (data_one != null) {
            AddressModel addressModel = new AddressModel();
            addressModel.setProvince(data_one.getProvince());
            addressModel.setCity(data_one.getCity());
            addressModel.setDistrict(data_one.getDistrict());

            addressModel.setName(data_one.getRecipientAddress());
            addressModel.setPoint(new LatLonPoint(Float.valueOf(data_one.getLatitude()), Float.valueOf(data_one.getLongitude())));
            addressModel.setAdd_name(data_one.getRecipientName());
            addressModel.setDoor_info("\u3000");

            addressModel.setAdd_phone(String.valueOf(data_one.getRecipientPhone()));
            mEndAddress = addressModel;
            String name = addressModel.getName();
            mBinding.tvToAdd.setText(name + "\n" + addressModel.getAdd_name() + "\t\t" + addressModel.getAdd_phone());

            mBinding.tvFood.setSelected(true);
            mBinding.etContent.setText(mBinding.tvFood.getText().toString().toString());
            wmOrderId=data_one.getOrderId()+"";
        }
        if (data_list != null && data_list.size() > 0) {
            for (int i = 0; i < data_list.size(); i++) {
                SpeedinessShipmentsModel.DataBean.RowsBean rowsBean = data_list.get(i);
                AddressModel addressModel = new AddressModel();
                addressModel.setProvince(rowsBean.getProvince());
                addressModel.setCity(rowsBean.getCity());
                addressModel.setDistrict(rowsBean.getDistrict());
                addressModel.setName(rowsBean.getRecipientAddress());
                addressModel.setPoint(new LatLonPoint(Float.valueOf(rowsBean.getLatitude()), Float.valueOf(rowsBean.getLongitude())));
                addressModel.setAdd_name(rowsBean.getRecipientName());
                addressModel.setDoor_info("\u3000");
                addressModel.setAdd_phone(String.valueOf(rowsBean.getRecipientPhone()));
                wmOrderId= wmOrderId+rowsBean.getOrderId()+",";
                if (i == 0) {
                    mEndAddress = addressModel;
                    String name = addressModel.getName();
                    mBinding.tvToAdd.setText(name + "\n" + addressModel.getAdd_name() + "\t\t" + addressModel.getAdd_phone());

                } else {
                    final ItemAddAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(aty), R.layout.item_add_address, null, false);
                    binding.tvToAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", (Integer) binding.llyToAdd.getTag()));
                        }
                    });
                    binding.llyToAdd.setTag(curReponse);
                    binding.tvAddTitle.setText("送达地点" + (curReponse - 1));
                    curReponse++;
                    mBindings.add(binding);
                    mAddressModel.add(null);
                    mBinding.llyAddAddress.addView(binding.getRoot());

                    mAddressModel.add(i - 1, addressModel);

                    String name = addressModel.getName();
                    mBindings.get(i - 1).tvToAdd.setText(name + "\n" + addressModel.getAdd_name() + "\t\t" + addressModel.getAdd_phone());

                }

            }

            mBinding.tvFood.setSelected(true);
            mBinding.etContent.setText(mBinding.tvFood.getText().toString().toString());

        }


    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityPayoutLayoutBinding) vdb;
        mType = getIntent().getIntExtra("type", 1);
        user = MaiLiApplication.getInstance().getUserInfo();
        initChooseTimeData();
        initView();
        initListener();
        preData();
    }

    private void initListener() {
        mBinding.rlWeight.setOnClickListener(this);
        mBinding.tvFromAdd.setOnClickListener(this);
        mBinding.llyToAdd.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        mBinding.tvAddAddress.setOnClickListener(this);
        mBinding.tvPreTime.setOnClickListener(this);
        mBinding.tvToAdd.setOnClickListener(this);
        mBinding.tvAddPrice.setOnClickListener(this);
        mBinding.tvNegative.setOnClickListener(this);
        mBinding.tvReTotal.setOnClickListener(this);
        mBinding.rlyKajuan.setOnClickListener(this);
        mBinding.tvPriceInfo.setOnClickListener(this);
        mDownOrderDialog.setIDownOrderListener(this);

    }


    private void initView() {
        mBinding.tvKg.setTag(5);
        mOccupationPopupWindow = new ChooseDatePopupWindow(aty, 1);
        mOccupationPopupWindow.setTitle("请选择物品重量");
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 95; i++) {
            if (i == 0) {
                data.add("5kg以内");
            } else {
                data.add((5 + i) + "kg");
            }
        }
        mOccupationPopupWindow.setData1(data);
        mOccupationPopupWindow.setIOccupationListener(this);

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

        mChooseTime.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                if ("立即发送".equals(item1)) {
                    mReservationTime = 0;
                    mBinding.tvPreTime.setText("立即发送");
                } else {
                    long time = getTime(item1, item2, item3);
                    if (time != 0) {
                        mBinding.tvPreTime.setText(Utils.getDateToString(time, "MM月dd日HH:mm"));
                        mReservationTime = time;
                    }
                }
            }
        });
    }


    private void countPrice(boolean showLoading) {
        String tag = mBinding.tvKg.getText().toString().trim();

        if (mStartAddress == null) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请选择取货地点!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (mEndAddress == null) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "至少要选择一个取货地点!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        if (showLoading) {
            showWaitDialog();
        }
        if (mAddressModel.size() > 0) {
            String jinlist = mEndAddress.getPoint().getLongitude() + ",";
            String weilist = mEndAddress.getPoint().getLatitude() + ",";
            for (int i = 0; i < mAddressModel.size(); i++) {
                if (mAddressModel.get(i) != null) {
                    if (i != mAddressModel.size() - 1) {
                        jinlist = jinlist + mAddressModel.get(i).getPoint().getLongitude() + ",";
                        weilist = weilist + mAddressModel.get(i).getPoint().getLatitude() + ",";
                    } else {
                        jinlist = jinlist + mAddressModel.get(i).getPoint().getLongitude();
                        weilist = weilist + mAddressModel.get(i).getPoint().getLatitude();
                    }
                }
            }
            mPresenter.getPrice2(
                    user.getPhone(),
                    mStartAddress.getCity(),
                    mStartAddress.getPoint().getLongitude() + "",
                    mStartAddress.getPoint().getLatitude() + "",
                    jinlist,
                    weilist,
                    mBinding.tvKg.getTag() + ""
            );
        } else {
            mPresenter.getPrice(
                    user.getPhone(),
                    mStartAddress.getCity(),
                    mStartAddress.getPoint().getLongitude() + "",
                    mStartAddress.getPoint().getLatitude() + "",
                    mEndAddress.getPoint().getLongitude() + "",
                    mEndAddress.getPoint().getLatitude() + "",
                    mBinding.tvKg.getTag() + ""
            );
        }
    }


    private void check() {
        String PickAdd = mBinding.tvFromAdd.getText().toString().trim();
        String ToAdd = mBinding.tvToAdd.getText().toString().trim();

        if (TextUtils.isEmpty(PickAdd)) {
            DemoUtils.nope(mBinding.tvFromAdd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择取货地点!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ToAdd)) {
            DemoUtils.nope(mBinding.tvToAdd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择送货地点!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPrice == 0) {
            DemoUtils.nope(mBinding.tvReTotal).start();
            Toast.makeText(MaiLiApplication.getInstance(), "价格没出来？试试点击右下角的重新计算!", Toast.LENGTH_SHORT).show();
            return;
        }
        checkOrder();
    }


    private void checkData() {
        String PickAdd = mBinding.tvFromAdd.getText().toString().trim();
        String ToAdd = mBinding.tvToAdd.getText().toString().trim();
        String Content = mBinding.etContent.getText().toString().trim();

        if (TextUtils.isEmpty(PickAdd)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择取货地点!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(ToAdd)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择送货地点!", Toast.LENGTH_SHORT).show();
            return;
        }
      /*  if (TextUtils.isEmpty(Content)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入物品描述!", Toast.LENGTH_SHORT).show();
            return;
        }*/
        showWaitDialog();
        String district = mEndAddress.getDistrict();
        String jin = mEndAddress.getPoint().getLongitude() + "";
        String wei = mEndAddress.getPoint().getLatitude() + "";
        String add_name = mEndAddress.getName();
        String address = TextUtils.isEmpty(mEndAddress.getAddress()) ? "" : mEndAddress.getAddress();
        String door_info = mEndAddress.getDoor_info();
        String add_name2 = mStartAddress.getName();
        String address2 = TextUtils.isEmpty(mStartAddress.getAddress()) ? "" : mStartAddress.getAddress();
        String door_info2 = mStartAddress.getDoor_info();
        String end_addr = add_name + address;
        String end_door = door_info;
        String start_addr = add_name2 + address2;
        String end_name = mEndAddress.getAdd_name();
        String end_phone = mEndAddress.getAdd_phone();
        if (mAddressModel.size() > 0) {
            for (int i = 0; i < mAddressModel.size(); i++) {
                if (mAddressModel.get(i) != null) {
                    district = district + "," + mAddressModel.get(i).getDistrict();
                    jin = jin + "," + mAddressModel.get(i).getPoint().getLongitude();
                    wei = wei + "," + mAddressModel.get(i).getPoint().getLatitude();
                    String add_name1 = mAddressModel.get(i).getName();
                    String address1 = TextUtils.isEmpty(mAddressModel.get(i).getAddress()) ? "" : mAddressModel.get(i).getAddress();
                    String door_info1 = mAddressModel.get(i).getDoor_info();
                    end_addr = end_addr + "," + add_name1 + address1;
                    end_door = end_door + "," + door_info1;
                    end_name = end_name + "," + mAddressModel.get(i).getAdd_name();
                    end_phone = end_phone + "," + mAddressModel.get(i).getAdd_phone();
                }
            }
        }


        CityServiceModel cityServiceModel = new CityServiceModel();
        cityServiceModel.setSubscribeTime(mReservationTime);
        cityServiceModel.setWeight(mBinding.tvKg.getTag()+"");
        cityServiceModel.setContent(Content);
        String content = ParseJsonUtils.getjsonStr(cityServiceModel);

        mPresenter.SendOrders1(user.getPhone(),
                user.getName(),
                mType + "",
                "",
                "",
                mEndAddress.getProvince(),
                mEndAddress.getCity(),
                district,
                jin,
                wei,
                end_addr,
                end_door,
                "",
                content,
                String.valueOf(mPrice + mAddPrice),
                String.valueOf(mPrice + mAddPrice),
                1 + "",
                mStartAddress.getProvince(),
                mStartAddress.getCity(),
                mStartAddress.getDistrict(),
                mStartAddress.getPoint().getLongitude() + "",
                mStartAddress.getPoint().getLatitude() + "",
                start_addr,
                door_info2,
                mBindings.size() > 0 ? "1" : "0",
                mReservationTime + "",
                mStartAddress.getAdd_name(),
                mStartAddress.getAdd_phone(),
                end_name,
                end_phone,
                mMoneyType + "",
                mCashCouponId,
                mDistance + "",
                wmOrderId
        );

    }

    public void labelClick(View view) {
        TextView txt = (TextView) view;

        if (!txt.isSelected()) {
            txt.setSelected(true);
            String con = mBinding.etContent.getText().toString().trim();

            if (TextUtils.isEmpty(con)) {
                mBinding.etContent.setText(con + txt.getText().toString().trim());
            } else {
                mBinding.etContent.setText(con + "\t\t" + txt.getText().toString().trim());
            }

        } else {
            txt.setSelected(false);
            String txt_con = txt.getText().toString().trim();
            String con = mBinding.etContent.getText().toString().trim().replace(txt_con, "");
            mBinding.etContent.setText(con);
        }
    }

    private int curReponse = 3;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_info://价格说明
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(mType)));
                break;
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_negative:
                finish();
                break;
            case R.id.tv_re_total:
                countPrice(true);
                break;
            case R.id.tv_add_price:
                mPricePopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.rl_weight:
                mOccupationPopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.tv_from_add://取货地点
                //  startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", 1));
                startActivity(new Intent(aty, UsualAddressActivity.class).putExtra("page", 1));
                break;
            case R.id.tv_to_add://送货地点
                startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", 2));
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
            case R.id.tv_pre_time:
                mChooseTime.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.rly_kajuan://卡卷
                startActivity(new Intent(aty, VoucherActivity.class)
                        .putExtra("type", 1)
                        .putExtra("workType", mType)
                        .putExtra("orderMoney", mAddPrice + mPrice)
                );
                break;

            case R.id.tv_add_address://添加地址
                if (mBindings.size() < 4) {
                    final ItemAddAddressBinding binding = DataBindingUtil.inflate(LayoutInflater.from(aty), R.layout.item_add_address, null, false);
                    binding.tvToAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", (Integer) binding.llyToAdd.getTag()));
                        }
                    });
                    binding.llyToAdd.setTag(curReponse);
                    binding.tvAddTitle.setText("送达地点" + (curReponse - 1));
                    curReponse++;
                    mBindings.add(binding);
                    mAddressModel.add(null);
                    mBinding.llyAddAddress.addView(binding.getRoot());
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "最多只能联五个单哦!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    @Override
    public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
        mBinding.tvKg.setText(item1);
        int kg = 5;
        if (position1 == 0) {
            kg = 5;
        } else {
            kg = position1 + 5;
        }
        mBinding.tvKg.setTag(kg);
        countPrice(false);
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
            mBinding.rlyTotalError.setVisibility(View.GONE);
            mBinding.llyPrice.setVisibility(View.VISIBLE);
            mBinding.rlyKajuan.setVisibility(View.VISIBLE);
            PriceModel info = (PriceModel) object;
            mBinding.tvPrice.setText("￥" + (info.getData() + mAddPrice));
            mDistance = info.getDistance();
            mPrice = info.getData();
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
        if (addressModel.getType() == 1) {
            mStartAddress = addressModel;
            String name = addressModel.getName();
            String door_info = addressModel.getDoor_info();
            mBinding.tvFromAdd.setText(name + door_info + "\n" + addressModel.getAdd_name() + "\t\t" + addressModel.getAdd_phone());
        } else if (addressModel.getType() == 2) {
            mEndAddress = addressModel;
            String name = addressModel.getName();
            String door_info = addressModel.getDoor_info();
            mBinding.tvToAdd.setText(name + door_info + "\n" + addressModel.getAdd_name() + "\t\t" + addressModel.getAdd_phone());
        } else {
            String name = addressModel.getName();
            String door_info = addressModel.getDoor_info();
            if (mAddressModel.size() - 1 >= addressModel.getType() - 3) {
                mAddressModel.set(addressModel.getType() - 3, addressModel);
            } else {
                mAddressModel.add(addressModel.getType() - 3, addressModel);
            }

            mBindings.get(addressModel.getType() - 3).tvToAdd.setText(name + door_info + "\n" + addressModel.getAdd_name() + "\t\t" + addressModel.getAdd_phone());
        }
        countPrice(false);
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
