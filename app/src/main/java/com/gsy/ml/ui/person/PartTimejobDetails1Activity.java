package com.gsy.ml.ui.person;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPartTimeJobDetails1Binding;
import com.gsy.ml.model.EventMessage.CancelReason;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.PartTimeJobModel;
import com.gsy.ml.model.message.EditPriceModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.model.person.OrderPeopleInfo;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimeJobPresenter;
import com.gsy.ml.prestener.home.PartTimePresenter;
import com.gsy.ml.prestener.person.CancelOrderPresenter;
import com.gsy.ml.prestener.person.FinishOrderPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.message.ChatActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.AccomplishOrCancelPopuWindow;
import com.gsy.ml.ui.views.BalanceOrderPopuwindow;
import com.gsy.ml.ui.views.CancelOrderDialog;
import com.gsy.ml.ui.views.ChooseMapPopuwindow;
import com.gsy.ml.ui.views.FullyLinearLayoutManager;
import com.gsy.ml.ui.views.ModificationOrderDialog;
import com.hyphenate.easeui.EaseConstant;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;
import ml.gsy.com.library.utils.glide.GlideCircleTransform;

/**
 * Created by Administrator on 2017/9/1.
 */

public class PartTimejobDetails1Activity extends BaseActivity implements ILoadPVListener, View.OnClickListener {
    private ActivityPartTimeJobDetails1Binding mBinding;
    private PartTimeJobPresenter pPresenter = new PartTimeJobPresenter(this);
    private PartTimePresenter presenter = new PartTimePresenter(this);
    private ModificationOrderDialog mModificationOrderDialog;
    private AccomplishOrCancelPopuWindow mAccomplishOrCancelPopuWindow;

    private int mType = 0;// 查询  1 ：我的接单  2：我的发单
    private String mOrder; //订单号
    private StateModel stateModel = new StateModel();
    private CommonAdapter<OrderPeopleInfo> mAdapter;
    private List<OrderPeopleInfo> mOrderPeopleInfo = new ArrayList<>();
    private int people;
    private boolean is_price_edit = false;//是否修改价格
    private double mTotalCast = 0;//总价
    private double mCast = 0;//单价
    private CancelOrderPresenter mOrderPresenter = new CancelOrderPresenter(this);
    private FinishOrderPresenter mFinishOrderPresenter = new FinishOrderPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_part_time_job_details1;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("信息详情");
        mBinding.ilHead.llyLeft.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityPartTimeJobDetails1Binding) vdb;
        mModificationOrderDialog = new ModificationOrderDialog(aty);
        mModificationOrderDialog.setModificationOrderListener(new ModificationOrderDialog.ModificationOrderListener() {
            @Override
            public void setType(int type, double cast, double totalcast) {
                if (type == 0) {
                    showWaitDialog();
                    mCast = cast;
                    mTotalCast = totalcast;
                    presenter.reportModification(mOrder, totalcast + "", people + "");
                }
            }
        });

        mAccomplishOrCancelPopuWindow = new AccomplishOrCancelPopuWindow(aty);
        mAccomplishOrCancelPopuWindow.setAccomplishOrCancelListener(new AccomplishOrCancelPopuWindow.AccomplishOrCancelListener() {
            @Override
            public void onClick(int type) {
                if (type == 1) {//推单
                    showWaitDialog();
                    pPresenter.getOrderPush(mPartTimeJobModel.getData().getSendOrders().getOrder());
                } else if (type == 2) {//完成

                    final PartTimeJobModel.DataBean.SendOrdersBean sendOrders = mPartTimeJobModel.getData().getSendOrders();

                    if (!TextUtils.isEmpty(sendOrders.getDoingPeoplePhone())) {
                        List<PartTimeJobModel.DataBean.AcceptOrdersListBean> acceptOrdersList = mPartTimeJobModel.getData().getAcceptOrdersList();
                        String online = "";
                        for (int i = 0; i < acceptOrdersList.size(); i++) {
                            String on = acceptOrdersList.get(i).isOnLine() ? "true" : "false";
                            online = online + on + ",";
                        }
                        BalanceOrderPopuwindow mBalanceOrderPopuwindow = new BalanceOrderPopuwindow(aty, sendOrders.getDoingPeoplePhone(), sendOrders.getWorkCost(), online);
                        mBalanceOrderPopuwindow.setIBanlanceOrderListener(new BalanceOrderPopuwindow.IBanlanceOrderListener() {
                            @Override
                            public void onPositive(String phone_list) {
                                confirOrder(sendOrders.getOrder(), phone_list);
                            }
                        });
                        mBalanceOrderPopuwindow.showPopupWindow();

                    } else {
                        showToast("该订单目前没有接单人！");
                    }
                } else if (type == 3) {//取消
                    CancelOrderDialog mCancelOrderDialog = new CancelOrderDialog(aty, mPartTimeJobModel.getData().getSendOrders().getOrder(),
                            type, 1);

                    mCancelOrderDialog.setCancelListener(new CancelOrderDialog.CancelListener() {
                        @Override
                        public void onCancel(CancelReason order) {
                            if (mType == 1) {//接单人
                                mOrderPresenter.cancelOrder2(MaiLiApplication.getInstance().getUserInfo().getPhone(), order.getOrder(), order.getReason());
                                if (order.getWorkType() == 1) {//同城配送
                                    Intent intent = new Intent("ReLocationReceiver");//结束长连接和连续定位
                                    intent.putExtra("type", "end");
                                    aty.sendBroadcast(intent);
                                }
                            } else {
                                showWaitDialog();
                                mOrderPresenter.cancelOrder1(MaiLiApplication.getInstance().getUserInfo().getPhone(), order.getOrder(), order.getReason());
                            }


                        }
                    });
                    mCancelOrderDialog.show();
                }
            }
        });
      //  mBinding.tvModification.setOnClickListener(this);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getData();
            }
        });
        mBinding.setStateModel(stateModel);
        mType = getIntent().getIntExtra("type", 1);
        mOrder = getIntent().getStringExtra("order");
        is_price_edit = getIntent().getBooleanExtra("is_price_edit", false);

        getData();
        initLocation(savedInstanceState);


    }

    private void confirOrder(String order, String phone_list) {
        showWaitDialog();
        mFinishOrderPresenter.finishOrder(order, phone_list, 0);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
     /*       case R.id.tv_modification:

                mModificationOrderDialog.show();
                break;*/
            case R.id.img_right:
                mAccomplishOrCancelPopuWindow.showPopupWindow(mBinding.ilHead.imgRight);
                break;
        }
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

    /**
     * 获取订单详情信息
     */
    private void getData() {
        pPresenter.selectOrder(mOrder);
    }

    public void setModel(PartTimeJobModel model) {
        if (model == null || model.getData() == null || model.getData().getSendOrders() == null) {
            return;
        }
        PartTimeJobModel.DataBean.SendOrdersBean sendOrders = model.getData().getSendOrders();
        mModificationOrderDialog.setMoneyAndPeople(sendOrders.getWorkCost(), sendOrders.getWorkTotalCost(), sendOrders.getPeopleNum());
        people = sendOrders.getPeopleNum();
        String place = TextUtils.isEmpty(sendOrders.getStartPlace()) ? "" : sendOrders.getStartPlace();
        //地址加工种
        mBinding.tvTitle.setText(place + DemoUtils.TypeToOccupation(Integer.parseInt(sendOrders.getWorkType())));
        if (sendOrders.getOrderStatus() != 0 && sendOrders.getOrderStatus() != -1) {
            if (model.getData().getAcceptOrdersList() != null && model.getData().getAcceptOrdersList().size() > 0) {

                PartTimeJobModel.DataBean.AcceptOrdersListBean accept = null;
                for (int i = 0; i < model.getData().getAcceptOrdersList().size(); i++) {
                    if (model.getData().getAcceptOrdersList().get(i).getAcceptPhone().equals(MaiLiApplication.getInstance().getUserInfo().getPhone())) {
                        accept = model.getData().getAcceptOrdersList().get(i);
                    }
                }
                if (accept != null) {
                    mBinding.llyPayType.setVisibility(View.VISIBLE);
                    mBinding.tvPayType.setText(accept.isOnLine() ? "线上支付" : "线下支付");
                }

            }
        }
        if (mType == 1) {//1 ：我的接单  2：我的发单
            if (sendOrders.getOrderStatus() == 1 || sendOrders.getOrderStatus() == 4) {
                mBinding.ilHead.imgRight.setVisibility(View.VISIBLE);
                mBinding.ilHead.imgRight.setImageResource(R.drawable.more_icon);
                mBinding.ilHead.imgRight.setOnClickListener(this);
                mAccomplishOrCancelPopuWindow.isCancelShow(true);
                mAccomplishOrCancelPopuWindow.isFinishShow(false);
                mAccomplishOrCancelPopuWindow.isPushShow(false);
            }
          //  mBinding.tvMoneys.setText("¥ " + sendOrders.getWorkCost() + "");//金额}
        } else {
            switch (sendOrders.getOrderStatus()) {
                case -1://取消
                    break;
                case 0://f发布中
                    mBinding.ilHead.imgRight.setVisibility(View.VISIBLE);
                    mBinding.ilHead.imgRight.setImageResource(R.drawable.more_icon);
                    mBinding.ilHead.imgRight.setOnClickListener(this);
                    mAccomplishOrCancelPopuWindow.isCancelShow(true);
                    mAccomplishOrCancelPopuWindow.isPushShow(true);
                    mAccomplishOrCancelPopuWindow.isFinishShow(false);
                    break;
                case 1://进行中
                    mBinding.ilHead.imgRight.setVisibility(View.VISIBLE);
                    mBinding.ilHead.imgRight.setImageResource(R.drawable.more_icon);
                    mBinding.ilHead.imgRight.setOnClickListener(this);
                    mAccomplishOrCancelPopuWindow.isCancelShow(true);
                    mAccomplishOrCancelPopuWindow.isPushShow(false);
                    mAccomplishOrCancelPopuWindow.isFinishShow(true);
                    break;
                case 4://发单中，人数不足的
                    mBinding.ilHead.imgRight.setVisibility(View.VISIBLE);
                    mBinding.ilHead.imgRight.setImageResource(R.drawable.more_icon);
                    mBinding.ilHead.imgRight.setOnClickListener(this);
                    mAccomplishOrCancelPopuWindow.isCancelShow(true);
                    mAccomplishOrCancelPopuWindow.isPushShow(true);
                    mAccomplishOrCancelPopuWindow.isFinishShow(true);
                    break;
            }
          //  mBinding.tvMoneys.setText("¥ " + sendOrders.getWorkTotalCost() + "");//金额
        }

       // mBinding.tvLocation.setText(sendOrders.getStartArea());//地区}}
        mBinding.tvWorktime.setText(fromatToTime(sendOrders.getSendTime()));//时间
      //  mBinding.tvTypeOfWork.setText(DemoUtils.TypeToOccupation(Integer.parseInt(sendOrders.getWorkType())));//工种
        //人数
        int peopleNum = sendOrders.getPeopleNum();  //订单需要的总人数
        int acceptPeopleNum = sendOrders.getAcceptPeopleNum();  //已接单人数
        int finishPeopleNum = sendOrders.getFinishPeopleNum();  //已完成人数
       // mBinding.tvPeople.setText(peopleNum + "人");

        String[] con = DemoUtils.TypeToContent(Integer.parseInt(sendOrders.getWorkType()), sendOrders.getWorkContent());
        mBinding.tvContent.setText(Html.fromHtml(con[0]));//工作内容
        mBinding.tvTimes.setText(Html.fromHtml(con[1]));//工作时间
        mBinding.tvType.setVisibility(View.VISIBLE);


        mBinding.tvTitle.setText(DemoUtils.TypeToNoAddressTitle1(Integer.valueOf(sendOrders.getWorkType()), sendOrders.getWorkContent(),mBinding.tvType));





       // mBinding.tvModification.setVisibility(sendOrders.getOrderStatus() == 0 ? View.VISIBLE : View.GONE);
        if (mType == 2 && (sendOrders.getOrderStatus() == 0 || sendOrders.getOrderStatus() == 4)) {//订单发单中
            mBinding.rlyChat.setVisibility(View.VISIBLE);
            mBinding.tvChatNum.setText("已沟通\n(" + model.getData().getTotal() + ")人");
            mBinding.rlyChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
                                "", "", "", "",
                                Integer.valueOf(sendOrders.getWorkType()));
                        startActivity(new Intent(aty, OrderListChatActivity.class).putExtra("order",
                                mPartTimeJobModel.getData().getSendOrders().getOrder()).putExtra("order_data", orderContent));
                    }
                }
            });
        } else {
            mBinding.rlyChat.setVisibility(View.GONE);
        }

        if (!DemoUtils.TypeToNoAddress(Integer.valueOf(sendOrders.getWorkType())) && (sendOrders.getOrderStatus() == 0 || sendOrders.getOrderStatus() == 1 || sendOrders.getOrderStatus() == 4)) {//显示地址
            mBinding.llyWorkAddress.setVisibility(View.VISIBLE);
            mBinding.viewWorkAddressLine.setVisibility(View.VISIBLE);
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(sendOrders.getWorkWei()),
                    Double.valueOf(sendOrders.getWorkJing())), 15));

            String workProvince = TextUtils.isEmpty(sendOrders.getWorkProvince()) ? "" : sendOrders.getWorkProvince();
            String workCity = TextUtils.isEmpty(sendOrders.getWorkCity()) ? "" : sendOrders.getWorkCity();
            String workArea = TextUtils.isEmpty(sendOrders.getWorkArea()) ? "" : sendOrders.getWorkArea();
            String workPlace = TextUtils.isEmpty(sendOrders.getWorkPlace()) ? "" : sendOrders.getWorkPlace();
            String workDoorplate = TextUtils.isEmpty(sendOrders.getWorkDoorplate()) ? "" : sendOrders.getWorkDoorplate();
            mBinding.tvAddress.setText(workProvince + workCity + workArea + workPlace + workDoorplate);

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
            mBinding.llyWorkAddress.setVisibility(View.GONE);
            mBinding.viewWorkAddressLine.setVisibility(View.GONE);

        }
        if (sendOrders.getOrderStatus() == 1 || sendOrders.getOrderStatus() == 4) {//订单进行中
            if (mType == 1) {//1 ：我的接单  2：我的发单
                mBinding.llyOrderTypeInfo.setVisibility(View.VISIBLE);
                mBinding.tvOrderTypeInfo.setText("发单人信息：");
                mOrderPeopleInfo.clear();
                mOrderPeopleInfo.add(new OrderPeopleInfo(sendOrders.getHeadUrl(), sendOrders.getSendPhone(), sendOrders.getSendRingLetter()
                        , sendOrders.getStar(), sendOrders.getName(), sendOrders.getNickName(), sendOrders.getSex()));
                initAdapter();
            } else {
                if (model.getData().getAcceptOrdersList() != null && model.getData().getAcceptOrdersList().size() > 0) {
                    mBinding.llyOrderTypeInfo.setVisibility(View.VISIBLE);
                    mBinding.tvOrderTypeInfo.setText("接单人信息：");
                    mOrderPeopleInfo.clear();
                    for (PartTimeJobModel.DataBean.AcceptOrdersListBean ac : model.getData().getAcceptOrdersList()) {
                        mOrderPeopleInfo.add(new OrderPeopleInfo(ac.getHeadUrl(), ac.getAcceptPhone(), ac.getAcceptRingLetter(), ac.getStar(), ac.getName(), ac.getNickName(), ac.getSex()));
                    }
                    initAdapter();
                } else {
                    mBinding.llyOrderTypeInfo.setVisibility(View.GONE);
                }
            }
        } else {
            mBinding.llyOrderTypeInfo.setVisibility(View.GONE);
        }


        if (is_price_edit) {
            mModificationOrderDialog.show();
        }
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<OrderPeopleInfo>(aty, R.layout.order_people_item, mOrderPeopleInfo) {
            @Override
            protected void convert(ViewHolder holder, final OrderPeopleInfo orderPeopleInfo, int position) {
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                TextView tv_name = holder.getView(R.id.tv_name);
                ImageView img_call = holder.getView(R.id.img_call);
                ImageView img_head = holder.getView(R.id.img_head);
                ImageView img_chat = holder.getView(R.id.img_chat);
                ImageView img_star = holder.getView(R.id.img_star);

                switch (orderPeopleInfo.getStar()) {
                    case 1:
                        img_star.setImageResource(R.drawable.stars_one_icon);
                        break;
                    case 2:
                        img_star.setImageResource(R.drawable.stars_two_icon);
                        break;
                    case 3:
                        img_star.setImageResource(R.drawable.stars_three_icon);
                        break;
                    case 4:
                        img_star.setImageResource(R.drawable.stars_four_icon);
                        break;
                    case 5:
                        img_star.setImageResource(R.drawable.stars_five_icon);
                        break;
                }


                Glide.with(aty).load(orderPeopleInfo.getHead_url()).transform(new GlideCircleTransform(aty))
                        .placeholder(R.drawable.ease_default_avatar).into(img_head);
                tv_name.setText(orderPeopleInfo.getNick_name());
                if ("男".equals(orderPeopleInfo.getSex())) {
                    tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.man_icon, 0);
                } else {
                    tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.girl_icon, 0);
                }
                img_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        call(orderPeopleInfo.getPhone());
                    }
                });
                img_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mType == 1) {//我的接单  发单人
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
                            startActivity(new Intent(aty, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, orderPeopleInfo.getHuanxin())
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .putExtra("nick_name", orderPeopleInfo.getNick_name())
                                    .putExtra("order", orderContent));
                        } else {
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
                                    orderPeopleInfo.getNick_name(),
                                    orderPeopleInfo.getHead_url(),
                                    orderPeopleInfo.getName(),
                                    orderPeopleInfo.getPhone(),
                                    Integer.valueOf(sendOrders.getWorkType())
                            );
                            startActivity(new Intent(aty, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, orderPeopleInfo.getHuanxin())
                                    .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    .putExtra("nick_name", orderPeopleInfo.getNick_name())
                                    .putExtra("order", orderContent));
                        }
                    }
                });
                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(aty, CheckActivity.class).
                                putExtra("phone", orderPeopleInfo.getPhone())
                                .putExtra("isshow", false)
                                .putExtra("type", 2)
                        );
                    }
                });
            }
        };
        mBinding.rcTypeInfo.setLayoutManager(new FullyLinearLayoutManager(aty, LinearLayoutManager.VERTICAL));
        mBinding.rcTypeInfo.setAdapter(mAdapter);
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

    private PartTimeJobModel mPartTimeJobModel = null;
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
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel model = (HttpSuccessModel) object;
            if (parms.length > 0) {
                if (parms[0] == 1) {//推送
                    Toast.makeText(aty, "推送成功！", Toast.LENGTH_SHORT).show();
                } else if (parms[0] == 2) {//取消
                    showToast(model.getInfo());
                    EventBus.getDefault().post(new CancelReason());
                    EventBus.getDefault().post(new UpdateNotice());
                    finish();
                } else if (parms[0] == 3) {//完成
                    showToast(model.getInfo());
                    EventBus.getDefault().post(new CancelReason());
                    EventBus.getDefault().post(new UpdateNotice());
                    finish();
                }
            } else {
                mModificationOrderDialog.setMoneyAndPeople(mCast, mTotalCast);
                mPartTimeJobModel.getData().getSendOrders().setWorkCost(mCast);
                mPartTimeJobModel.getData().getSendOrders().setWorkTotalCost(mTotalCast);
              //  mBinding.tvMoneys.setText("¥ " + mTotalCast + "");//金额
                Toast.makeText(aty, "修改成功！", Toast.LENGTH_SHORT).show();
                if (is_price_edit) {
                    EventBus.getDefault().post(new EditPriceModel(mCast, mTotalCast));
                    finish();

                }
            }

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
