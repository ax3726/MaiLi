package com.gsy.ml.ui.person;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.FragmentOrderreceivingLayoutBinding;
import com.gsy.ml.model.EventMessage.CancelReason;
import com.gsy.ml.model.EventMessage.EvakuateUpdate;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.model.message.OrderMessageModel;
import com.gsy.ml.model.person.MineOrderModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimePresenter;
import com.gsy.ml.prestener.person.CancelOrderPresenter;
import com.gsy.ml.prestener.person.SelectOrderPresenter;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.home.PartTime1Activity;
import com.gsy.ml.ui.home.PartTimeActivity;
import com.gsy.ml.ui.home.PartTimeJobActivity;
import com.gsy.ml.ui.message.ChatActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.CancelOrderDialog;
import com.hyphenate.easeui.EaseConstant;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2017/4/24.
 * 接单
 */

public class OrderReceivingFragment extends BaseFragment implements ILoadPVListener {
    private int type;
    private FragmentOrderreceivingLayoutBinding mBinding;
    private CommonAdapter<MineOrderModel.DataBean> mAdapter;
    private List<MineOrderModel.DataBean> mDataList = new ArrayList<>();
    private SelectOrderPresenter mPresenter = new SelectOrderPresenter(this);
    private CancelOrderPresenter mOrderPresenter = new CancelOrderPresenter(this);
    private PartTimePresenter presenter = new PartTimePresenter(this);
    private int mPage = 1;
    private int mPageSize = 10;
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_orderreceiving_layout;
    }

    private boolean is_first = false;

    @Override
    protected void initData() {
        super.initData();
        mBinding = (FragmentOrderreceivingLayoutBinding) vdb;
        EventBus.getDefault().register(this);
        type = getArguments().getInt("type", 6);
        stateModel.setExpandRes("还没有订单信息呢!", R.drawable.no_data1_icon);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                selectOrder();
            }
        });
        mBinding.setStateModel(stateModel);

        intit0();
        if (is_first) {
            mPage = 1;
            selectOrder();
        }
    }

    public void loadData() {
        if (!is_first) {
            is_first = true;
        } else {
            return;
        }
        if (mBinding != null) {
            mPage = 1;
            selectOrder();
        }
    }

    /**
     * 、
     * 更新订单信息
     *
     * @param order
     */
    private void getOrderInfo(String order) {
        showWaitDialog("正在建立沟通...");
        presenter.findOrderInfo(order);
    }

    public void intit0() {

        mAdapter = new CommonAdapter<MineOrderModel.DataBean>(aty, R.layout.item_applyfor, mDataList) {

            @Override
            protected void convert(ViewHolder holder, final MineOrderModel.DataBean dataBean, int position) {


                View under_line = holder.getView(R.id.under_line);
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                LinearLayout lly_buttom = holder.getView(R.id.lly_buttom);
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_address = holder.getView(R.id.tv_address);
                TextView tv_content = holder.getView(R.id.tv_content);
                TextView tv_state = holder.getView(R.id.tv_state);
                TextView tv_money = holder.getView(R.id.tv_money);
                TextView tv_txt1 = holder.getView(R.id.tv_txt3);
                TextView tv_txt2 = holder.getView(R.id.tv_txt2);
                TextView tv_hint = holder.getView(R.id.tv_hint);
                TextView tv_danwei = holder.getView(R.id.tv_danwei);

                if (position == 0) {
                    under_line.setVisibility(View.GONE);
                } else {
                    under_line.setVisibility(View.VISIBLE);
                }

                if (dataBean.getWorkType().equals("1")) {
                    tv_title.setText(dataBean.getStartPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                } else if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType()))) {
                    tv_title.setText(DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                } else {
                    tv_title.setText(dataBean.getWorkPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                }
                if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType())) && Integer.valueOf(dataBean.getWorkType()) != 24) {
                    tv_money.setVisibility(View.GONE);
                    tv_danwei.setVisibility(View.GONE);
                    tv_hint.setVisibility(View.VISIBLE);
                    tv_title.setText(DemoUtils.TypeToNoAddressTitle1(Integer.valueOf(dataBean.getWorkType()), dataBean.getWorkContent(),tv_hint));

                } else {
                    tv_money.setVisibility(View.VISIBLE);
                    tv_danwei.setVisibility(View.VISIBLE);
                    tv_hint.setVisibility(View.GONE);
                }
                tv_address.setText(dataBean.getStartArea());
                String con = DemoUtils.TypeToContent2(Integer.valueOf(dataBean.getWorkType()), dataBean.getWorkContent());
                tv_content.setText(con);
                tv_content.setVisibility(DemoUtils.checkEmpty(con) ? View.GONE : View.VISIBLE);

                if (type == 6) {
                    tv_money.setText(dataBean.getWorkCost() + "");
                    lly_buttom.setVisibility(View.GONE);
                    tv_txt2.setVisibility(View.INVISIBLE);
                    tv_txt1.setVisibility(View.INVISIBLE);
                    switch (dataBean.getOrderStatus()) {//-1: 接单前被取消的订单 0：正在发单 1：已接单，正在进行  2：已完成未评价   3，已评价  4.发单中，人数不足的
                        case -1://-1: 接单前被取消的订单
                            tv_state.setText("已取消");
                            lly_buttom.setVisibility(View.GONE);
                            tv_txt2.setVisibility(View.INVISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            break;
                        case 0://0：正在发单
                            tv_state.setText("发布中");
                            lly_buttom.setVisibility(View.VISIBLE);
                            tv_txt2.setVisibility(View.VISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            tv_txt2.setText("继续沟通");
                            tv_txt2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getOrderInfo(dataBean.getOrder());
                                }
                            });
                            break;
                        case 1://1：已接单，正在进行
                            tv_state.setText("进行中");
                            lly_buttom.setVisibility(View.GONE);
                            tv_txt2.setVisibility(View.INVISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            break;
                        case 2://：已完成未评价
                        case 3://：已完成未评价
                            tv_state.setText("已完成");
                            lly_buttom.setVisibility(View.GONE);
                            tv_txt2.setVisibility(View.INVISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            break;
                        case 4://4.发单中，人数不足的
                            tv_state.setText("发布中");
                            lly_buttom.setVisibility(View.VISIBLE);
                            tv_txt2.setVisibility(View.VISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            tv_txt2.setText("继续沟通");
                            tv_txt2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getOrderInfo(dataBean.getOrder());
                                }
                            });
                            break;

                    }

                    lly_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType())) && Integer.valueOf(dataBean.getWorkType()) != 24) {
                                startActivity(new Intent(aty, PartTime1Activity.class)
                                        .putExtra("order", dataBean.getOrder())
                                        .putExtra("isShowAdd", true)
                                        .putExtra("isShowPhone", false));

                            } else {
                                startActivity(new Intent(aty, PartTimeActivity.class)
                                        .putExtra("order", dataBean.getOrder())
                                        .putExtra("isShowAdd", true)
                                        .putExtra("isShowPhone", false));
                            }

                        }
                    });

                } else {
                    tv_money.setText(dataBean.getCostNum() + "");
                    switch (dataBean.getOrderStatus()) {
                        case -1:
                            tv_state.setText("已取消");
                            lly_buttom.setVisibility(View.GONE);
                            tv_txt2.setVisibility(View.INVISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            break;
                        case 0:
                            tv_state.setText("进行中");
                            if (dataBean.getWorkType().equals("1")) {
                                tv_txt1.setVisibility(View.VISIBLE);
                                if (dataBean.getPickup() == 0) {// 未取货
                                    tv_txt1.setText("我已取货");
                                    tv_txt1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(aty, ClaimGoodsActivity.class)
                                                    .putExtra("order", dataBean.getOrder())
                                                    .putExtra("page", type)
                                            );
                                        }
                                    });
                                } else {
                                    tv_txt1.setText("我已送达");
                                    tv_txt1.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            startActivity(new Intent(aty, CheckCodeActivity.class)
                                                    .putExtra("order", dataBean.getOrder())
                                                    .putExtra("service_time", dataBean.getService_time())
                                                    .putExtra("page", type)
                                            );
                                        }
                                    });
                                }

                            } else {
                                tv_txt1.setVisibility(View.INVISIBLE);
                            }

                            tv_txt2.setVisibility(View.VISIBLE);
                            tv_txt2.setSelected(false);
                            tv_txt2.setTextColor(getResources().getColor(R.color.color292929));
                            tv_txt2.setText("取消订单");
                            tv_txt2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    CancelOrderDialog cancelOrderDialog = new CancelOrderDialog(aty, dataBean.getOrder(), type, 0, Integer.valueOf(dataBean.getWorkType()));
                                    cancelOrderDialog.setCancelListener(new CancelOrderDialog.CancelListener() {
                                        @Override
                                        public void onCancel(CancelReason order) {
                                            if (type == order.getType()) {
                                                showWaitDialog();
                                                mOrderPresenter.cancelOrder2(MaiLiApplication.getInstance().getUserInfo().getPhone(), order.getOrder(), order.getReason());
                                                if (order.getWorkType() == 1) {//同城配送
                                                    Intent intent = new Intent("ReLocationReceiver");//结束长连接和连续定位
                                                    intent.putExtra("type", "end");
                                                    aty.sendBroadcast(intent);

                                                }
                                            }

                                        }
                                    });
                                    cancelOrderDialog.show();
                                }
                            });
                            break;
                        case 1:
                            tv_state.setText("待处理");
                            lly_buttom.setVisibility(View.GONE);
                            tv_txt2.setVisibility(View.INVISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);

                            break;
                        case 2:
                            tv_state.setText("待评价");
                            tv_txt2.setVisibility(View.VISIBLE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            tv_txt2.setSelected(true);
                            tv_txt2.setTextColor(getResources().getColor(R.color.colorFF6C00));
                            tv_txt2.setText("给他/她评价");
                            tv_txt2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(aty, EvaluateActivity.class).putExtra("order", dataBean.getOrder())
                                            .putExtra("type", 1)
                                            .putExtra("page", type)
                                    );
                                }
                            });
                            break;
                        case 3:
                            tv_state.setText("已评价");
                            lly_buttom.setVisibility(View.GONE);
                            tv_txt1.setVisibility(View.INVISIBLE);
                            tv_txt2.setVisibility(View.INVISIBLE);

                            break;
                        case 4:
                            tv_state.setText("审核未通过");
                            break;
                    }
                    lly_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if ("1".equals(dataBean.getWorkType())) {//同城配送
                                startActivity(new Intent(aty, PartTimeJobActivity.class)
                                        .putExtra("order", dataBean.getOrder())
                                        .putExtra("type", 1)
                                        .putExtra("isShowAdd", dataBean.getOrderStatus() == 0)
                                        .putExtra("isShowPhone", dataBean.getOrderStatus() == 0)
                                );
                            } else {
                                if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType())) && Integer.valueOf(dataBean.getWorkType()) != 24) {
                                    startActivity(new Intent(aty, PartTimejobDetails1Activity.class)
                                            .putExtra("order", dataBean.getOrder())
                                            .putExtra("type", 1)
                                            .putExtra("isShowAdd", dataBean.getOrderStatus() == 0)
                                            .putExtra("isShowPhone", dataBean.getOrderStatus() == 0)
                                    );
                                } else {
                                    startActivity(new Intent(aty, PartTimejobDetailsActivity.class)
                                            .putExtra("order", dataBean.getOrder())
                                            .putExtra("type", 1)
                                            .putExtra("isShowAdd", dataBean.getOrderStatus() == 0)
                                            .putExtra("isShowPhone", dataBean.getOrderStatus() == 0)
                                    );
                                }

                            }


                        }
                    });
                }
            }
        };
        mBinding.rvApplyfor.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rvApplyfor.setAdapter(mAdapter);
        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        selectOrder();
                    }
                }, 600);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                mPage++;
                selectOrder();
            }
        });
    }

    private void selectOrder() {
        mPresenter.selectOrder(MaiLiApplication.getInstance().getUserInfo().getPhone(), type + "", mPage + "", mPageSize + "");
    }

    private void stopRefresh() {
        mBinding.mrlBody.finishRefresh();
        mBinding.mrlBody.finishRefreshLoadMore();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        stopRefresh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof MineOrderModel) {
            MineOrderModel info = (MineOrderModel) object;
            if (mPage == 1) {
                mDataList.clear();
                if (info.getData().size() == 0) {
                    stateModel.setEmptyState(EmptyState.EXPAND);
                } else {
                    stateModel.setEmptyState(EmptyState.NORMAL);
                }
            }
            mDataList.addAll(info.getData());
            mAdapter.notifyDataSetChanged();
            if (mDataList.size() < mPageSize) {
                mBinding.mrlBody.setLoadMore(false);
            } else {
                mBinding.mrlBody.setLoadMore(true);
            }
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            update();
        } else if (object instanceof OrderMessageModel) {
            OrderMessageModel info = (OrderMessageModel) object;
            if (info.getData() != null) {
                String address = "";
                if (info.getData().getWorkType() == 24) {
                    address = DemoUtils.TypeToContent2(info.getData().getWorkType(), info.getData().getWorkContent());
                } else {
                    address = info.getData().getWorkPlace();
                }
                OrderContent orderContent = new OrderContent(
                        info.getData().getOrder(),
                        info.getData().getSendPhone(),
                        address,
                        info.getData().getWorkCost(),
                        info.getData().getWorkTotalCost(),
                        info.getData().getOrderStatus(),
                        info.getData().getNickName(),
                        info.getData().getHeadUrl(),
                        MaiLiApplication.getInstance().getUserInfo().getNickname(),
                        MaiLiApplication.getInstance().getUserInfo().getHeadUrl(),
                        MaiLiApplication.getInstance().getUserInfo().getName(),
                        MaiLiApplication.getInstance().getUserInfo().getPhone(),
                        info.getData().getWorkType()
                );

                startActivity(new Intent(aty, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, info.getData().getSendRingLetter())
                        .putExtra("nick_name", info.getData().getNickName())
                        .putExtra("order", orderContent));


            }
        }

    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }


    /**
     * 刷新数据，更新消息条目
     */
    private void update() {
        mPage = 1;
        selectOrder();
        EventBus.getDefault().post(new UpdateNotice());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancel(CancelReason cancelReason) {//关闭订单
        mPage = 1;
        selectOrder();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateData(EvakuateUpdate evakuate) {
        if (type == evakuate.getType()) {
            update();
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
