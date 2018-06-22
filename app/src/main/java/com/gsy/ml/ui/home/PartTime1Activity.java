package com.gsy.ml.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPartTime1Binding;
import com.gsy.ml.databinding.ActivityPartTimeBinding;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.PartTimeJobModel;
import com.gsy.ml.model.home.PartTimeModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimeJobPresenter;
import com.gsy.ml.prestener.home.PartTimePresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.message.ChatActivity;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.ChooseMapPopuwindow;
import com.gsy.ml.ui.views.InformContentPopuwindow;
import com.gsy.ml.ui.views.InformParticularsPopuWindow;
import com.gsy.ml.ui.views.InformationDialog;
import com.hyphenate.easeui.EaseConstant;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/9/1.
 * 兼职详情
 */

public class PartTime1Activity extends BaseActivity implements ILoadPVListener, View.OnClickListener {
    private ActivityPartTime1Binding mBinding;
    private PartTimeJobPresenter mPresenter = new PartTimeJobPresenter(this);
    private PartTimePresenter presenter = new PartTimePresenter(this);
    private String mOrder; //订单号
    private StateModel stateModel = new StateModel();
    private InformParticularsPopuWindow mInformParticularsPopuWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_part_time1;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("兼职详情");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.imgRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.imgRight.setImageResource(R.drawable.more_icon);
        mBinding.ilHead.imgRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityPartTime1Binding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getData();
            }
        });
        mBinding.setStateModel(stateModel);
        mOrder = getIntent().getStringExtra("order");
        getData();
        initLocation(savedInstanceState);
        mBinding.tvIncrease.setOnClickListener(this);
        mInformParticularsPopuWindow = new InformParticularsPopuWindow(aty);  //举报窗口
        mInformParticularsPopuWindow.setListener(new InformContentPopuwindow.InfromContentListener() {//举报内容接口
            @Override
            public void onItem(int postion, String text) {
                showWaitDialog();
                switch (postion) {
                    case 1:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 2:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 3:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 4:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 5:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 6:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 7:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 8:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;
                    case 9:
                        presenter.reportMessage(mOrder,
                                mPartTimeJobModel.getData().getSendOrders().getSendPhone(),
                                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                text);
                        break;

                }
            }
        });
    }

    /**
     * 获取订单详情信息
     */
    private void getData() {
        mPresenter.selectOrder(mOrder);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.img_right:
                mInformParticularsPopuWindow.showPopupWindow(mBinding.ilHead.imgRight);
                break;
            case R.id.tv_increase:
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
                    setEstablish();
                }


                break;
        }
    }

    public void setEstablish() {
        if (TextUtils.isEmpty(mOrder)) {
            Toast.makeText(aty, "订单号为空！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mPartTimeJobModel.getData().getSendOrders().getSendRingLetter())) {
            Toast.makeText(aty, "发单人账号为空！", Toast.LENGTH_SHORT).show();
            return;
        }


        showWaitDialog("正在建立沟通！！！");
        presenter.responsePart(mOrder,
                mPartTimeJobModel.getData().getSendOrders().getSendRingLetter(),
                MaiLiApplication.getInstance().mHuanxin,
                MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    //初始化地图控制器对象+
    AMap aMap;

    private void initLocation(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mvAdd.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mvAdd.getMap();
        }
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    private String fromatToTime(long time) {
        if (time > 0) {
            if (Utils.isOneDay(time)) {
                return Utils.getDateToString(time, "HH:mm");
            } else {
                return Utils.getTimeStyle22(time);
            }
        } else {
            return "";
        }
    }

    public void setModel(PartTimeJobModel model) {
        if (model == null || model.getData() == null || model.getData().getSendOrders() == null) {
            return;
        }

        final PartTimeJobModel.DataBean.SendOrdersBean sendOrders = model.getData().getSendOrders();

        mWorkType = Integer.valueOf(model.getData().getSendOrders().getWorkType());

//        mBinding.tvMoneys.setText("¥ " + model.getData().getSendOrders().getWorkCost() + "");//金额
        //  mBinding.tvLocation.setText(model.getData().getSendOrders().getStartArea());//地区
        mBinding.tvWorktime.setText(fromatToTime(model.getData().getSendOrders().getSendTime()));//时间
        // mBinding.tvTypeOfWork.setText(DemoUtils.TypeToOccupation(Integer.parseInt(model.getData().getSendOrders().getWorkType())));//工种
        //人数
        int peopleNum = model.getData().getSendOrders().getPeopleNum();  //订单需要的总人数
        int acceptPeopleNum = model.getData().getSendOrders().getAcceptPeopleNum();  //已接单人数
        int finishPeopleNum = model.getData().getSendOrders().getFinishPeopleNum();  //已完成人数
        // mBinding.tvPeople.setText(peopleNum - acceptPeopleNum - finishPeopleNum + "人");

        String[] con = DemoUtils.TypeToContent(Integer.parseInt(sendOrders.getWorkType()), sendOrders.getWorkContent());
        mBinding.tvContent.setText(Html.fromHtml(con[0]));//工作内容
        mBinding.tvTimes.setText(Html.fromHtml(con[1]));//工作时间

        if (!DemoUtils.TypeToNoAddress(Integer.valueOf(sendOrders.getWorkType()))) {
            mBinding.llyAdds.setVisibility(View.VISIBLE);


            //地址加工种
            mBinding.tvTitle.setText(model.getData().getSendOrders().getStartPlace() + DemoUtils.TypeToOccupation(Integer.parseInt(model.getData().getSendOrders().getWorkType())));
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(sendOrders.getWorkWei()),
                    Double.valueOf(sendOrders.getWorkJing())), 15));

            String workProvince = TextUtils.isEmpty(sendOrders.getWorkProvince()) ? "" : sendOrders.getWorkProvince();
            String workCity = TextUtils.isEmpty(sendOrders.getWorkCity()) ? "" : sendOrders.getWorkCity();
            String workArea = TextUtils.isEmpty(sendOrders.getWorkArea()) ? "" : sendOrders.getWorkArea();
            String workPlace = TextUtils.isEmpty(sendOrders.getWorkPlace()) ? "" : sendOrders.getWorkPlace();
            String workDoorplate = TextUtils.isEmpty(sendOrders.getWorkDoorplate()) ? "" : sendOrders.getWorkDoorplate();

            String location = workProvince + workCity + workArea + workPlace + workDoorplate;
            mBinding.tvAddress.setText(location.substring(0, location.length() < 6 ? location.length() : location.length() - 6) + "****");

            final AddressModel addressModel = new AddressModel();
            addressModel.setName(model.getData().getSendOrders().getStartPlace());
            addressModel.setAddress(model.getData().getSendOrders().getStartProvince()
                    + model.getData().getSendOrders().getStartCity()
                    + model.getData().getSendOrders().getStartArea()
                    + model.getData().getSendOrders().getStartPlace());
            addressModel.setPoint(new LatLonPoint(Float.valueOf(model.getData().getSendOrders().getStartWei()), Float.valueOf(model.getData().getSendOrders().getStartJing())));


            mBinding.llyAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ChooseMapPopuwindow(aty, addressModel).showPopupWindow();
                }
            });
        } else {
            mBinding.llyAdds.setVisibility(View.GONE);
            mBinding.viewLine.setVisibility(View.GONE);
            if (Integer.valueOf(sendOrders.getWorkType()) == 24) {
                //地址加工种
                mBinding.tvTitle.setText(DemoUtils.TypeToOccupation(Integer.parseInt(model.getData().getSendOrders().getWorkType())));
            } else {
                mBinding.tvTitle.setText(DemoUtils.TypeToNoAddressTitle1(Integer.valueOf(sendOrders.getWorkType()), sendOrders.getWorkContent(),mBinding.tvType));
            }



        }

        boolean isAccept = false;
        if (model.getData().getAcceptOrdersList() != null && model.getData().getAcceptOrdersList().size() > 0) {
            for (int i = 0; i < model.getData().getAcceptOrdersList().size(); i++) {
                if (MaiLiApplication.getInstance().getUserInfo().getPhone()
                        .equals(model.getData().getAcceptOrdersList().get(i).getAcceptPhone())) {
                    isAccept = true;
                }

            }
        }

        if (!sendOrders.getSendPhone().equals(MaiLiApplication.getInstance().getUserInfo().getPhone()) && !isAccept
                && (sendOrders.getOrderStatus() == 0 || sendOrders.getOrderStatus() == 4)) {//显示底部
            mBinding.llyBottom.setVisibility(View.VISIBLE);
        }
    }

    private PartTimeJobModel mPartTimeJobModel = null;
    private PartTimeModel mPartTimeModel = null;
    private int mWorkType = 0;

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel model = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(model.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), model.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof PartTimeJobModel) {
            mPartTimeJobModel = (PartTimeJobModel) object;
            setModel(mPartTimeJobModel);
            stateModel.setEmptyState(EmptyState.NORMAL);
        } else if (object instanceof PartTimeModel) {
            mPartTimeModel = (PartTimeModel) object;
            if (mPartTimeJobModel != null) {
                PartTimeJobModel.DataBean.SendOrdersBean sendOrders = mPartTimeJobModel.getData().getSendOrders();
                String address = "";
                if (DemoUtils.TypeToNoAddress(Integer.valueOf(sendOrders.getWorkType()))) {
                    address = DemoUtils.TypeToContent2(Integer.valueOf(sendOrders.getWorkType()), sendOrders.getWorkContent());
                } else {
                    address = sendOrders.getWorkPlace();
                }

                OrderContent orderContent = new OrderContent(
                        sendOrders.getOrder(),
                        sendOrders.getSendPhone(),
                        address,
                        sendOrders.getWorkCost(),
                        sendOrders.getWorkTotalCost(),
                        sendOrders.getOrderStatus(),
                        sendOrders.getNickName(),
                        sendOrders.getHeadUrl(),
                        MaiLiApplication.getInstance().getUserInfo().getNickname(),
                        MaiLiApplication.getInstance().getUserInfo().getHeadUrl(),
                        MaiLiApplication.getInstance().getUserInfo().getName(),
                        MaiLiApplication.getInstance().getUserInfo().getPhone(),
                        Integer.valueOf(sendOrders.getWorkType())
                );
                startActivity(new Intent(aty, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, sendOrders.getSendRingLetter())
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        .putExtra("nick_name", sendOrders.getNickName())
                        .putExtra("is_welcome", true)
                        .putExtra("order", orderContent));
                finish();
            }
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel model = (HttpSuccessModel) object;
            Toast.makeText(aty, "举报成功！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mvAdd.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mvAdd.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mvAdd.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mvAdd.onSaveInstanceState(outState);
    }
}
