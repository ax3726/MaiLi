package com.gsy.ml.ui.person;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
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
import com.gsy.ml.model.person.MineBillingModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.CancelOrderPresenter;
import com.gsy.ml.prestener.person.FinishOrderPresenter;
import com.gsy.ml.prestener.person.SelectOrderPresenter;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.home.PartTimeJobActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.BalanceOrderPopuwindow;
import com.gsy.ml.ui.views.CancelOrderDialog;
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
 * 发单
 */

public class OrderBillingFragment extends BaseFragment implements ILoadPVListener {
    private int type;
    private FragmentOrderreceivingLayoutBinding mBinding;
    private CommonAdapter<MineBillingModel.DataBean> mAdapter;
    private List<MineBillingModel.DataBean> mDataList = new ArrayList<>();
    private SelectOrderPresenter mPresenter = new SelectOrderPresenter(this);
    private CancelOrderPresenter mOrderPresenter = new CancelOrderPresenter(this);
    private FinishOrderPresenter mFinishOrderPresenter = new FinishOrderPresenter(this);
    private int mPage = 1;
    private int mPageSize = 10;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_orderreceiving_layout;
    }

    private StateModel stateModel = new StateModel();
    private boolean is_first = false;

    @Override
    protected void initData() {
        super.initData();

        mBinding = (FragmentOrderreceivingLayoutBinding) vdb;
        EventBus.getDefault().register(this);
        type = getArguments().getInt("type", 1);
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

    private CancelOrderDialog mCancelOrderDialog;
    private BalanceOrderPopuwindow mBalanceOrderPopuwindow;

    public void intit0() {

        mAdapter = new CommonAdapter<MineBillingModel.DataBean>(aty, R.layout.item_applyfor, mDataList) {

            @Override
            protected void convert(ViewHolder holder, final MineBillingModel.DataBean dataBean, int position) {
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_address = holder.getView(R.id.tv_address);
                TextView tv_content = holder.getView(R.id.tv_content);
                TextView tv_state = holder.getView(R.id.tv_state);
                TextView tv_money = holder.getView(R.id.tv_money);
                TextView tv_txt1 = holder.getView(R.id.tv_txt3);
                tv_txt1.setVisibility(View.INVISIBLE);
                View under_line = holder.getView(R.id.under_line);
                if (position == 0) {
                    under_line.setVisibility(View.GONE);
                } else {
                    under_line.setVisibility(View.VISIBLE);
                }
                holder.setVisible(R.id.lly_buttom, true);
                TextView tv_txt2 = holder.getView(R.id.tv_txt2);
                TextView tv_txt = holder.getView(R.id.tv_txt1);
                tv_txt.setVisibility(View.INVISIBLE);
                if (dataBean.getWorkType().equals("1")) {
                    tv_title.setText(dataBean.getStartPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                } else if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType()))) {
                    tv_title.setText(DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                } else {
                    tv_title.setText(dataBean.getWorkPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                }
                tv_address.setText(dataBean.getStartArea());
                String con = DemoUtils.TypeToContent2(Integer.valueOf(dataBean.getWorkType()), dataBean.getWorkContent());
                tv_content.setText(con);
                tv_content.setVisibility(DemoUtils.checkEmpty(con) ? View.GONE : View.VISIBLE);
                tv_money.setText(dataBean.getWorkTotalCost() + "");
                switch (dataBean.getOrderStatus()) {
                    case -1:
                        tv_state.setText("已关闭");
                        holder.setVisible(R.id.lly_buttom, false);
                        tv_txt2.setVisibility(View.INVISIBLE);
                        tv_txt.setVisibility(View.INVISIBLE);
                        break;
                    case 0:
                        tv_state.setText("待接单");
                        tv_txt2.setVisibility(View.VISIBLE);
                        tv_txt.setVisibility(View.INVISIBLE);
                        tv_txt2.setSelected(false);
                        tv_txt2.setTextColor(getResources().getColor(R.color.color292929));
                        tv_txt2.setText("关闭订单");
                        tv_txt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCancelOrderDialog = new CancelOrderDialog(aty, dataBean.getOrder(),
                                        type, 1);

                                mCancelOrderDialog.setCancelListener(new CancelOrderDialog.CancelListener() {
                                    @Override
                                    public void onCancel(CancelReason order) {
                                        if (type == order.getType()) {
                                            showWaitDialog();
                                            mOrderPresenter.cancelOrder1(MaiLiApplication.getInstance().getUserInfo().getPhone(), order.getOrder(), order.getReason());
                                        }

                                    }
                                });
                                mCancelOrderDialog.show();
                            }
                        });

                        tv_txt1.setVisibility(View.INVISIBLE);

                        break;
                    case 1:
                        tv_state.setText("正在进行");
                        tv_txt2.setVisibility(View.VISIBLE);
                        tv_txt1.setVisibility(View.VISIBLE);
                        tv_txt.setVisibility(View.VISIBLE);

                        if (dataBean.getWorkType().equals("1")) {
                            tv_txt1.setText("查看订单进度");
                            tv_txt1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(aty, OrderProgressActivity.class)
                                            .putExtra("order", dataBean.getOrder())
                                    );
                                }
                            });
                        } else {
                            tv_txt1.setText("完成订单");
                            tv_txt1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(dataBean.getDoingPeoplePhone())) {
                                        mBalanceOrderPopuwindow = new BalanceOrderPopuwindow(aty, dataBean.getDoingPeoplePhone(), dataBean.getWorkCost(),dataBean.getOnLine());
                                        mBalanceOrderPopuwindow.setIBanlanceOrderListener(new BalanceOrderPopuwindow.IBanlanceOrderListener() {
                                            @Override
                                            public void onPositive(String phone_list) {
                                                confirOrder(dataBean.getOrder(), phone_list);
                                            }
                                        });
                                        mBalanceOrderPopuwindow.showPopupWindow();
                                        if (mCancelOrderDialog != null) {
                                            mCancelOrderDialog.dismiss();
                                        }
                                    } else {
                                        Toast.makeText(mContext, "该订单目前没有接单人！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        tv_txt2.setSelected(false);
                        tv_txt2.setTextColor(getResources().getColor(R.color.color292929));
                        tv_txt2.setText("取消订单");
                        tv_txt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCancelOrderDialog = new CancelOrderDialog(aty, dataBean.getOrder(),
                                        type, 1);

                                mCancelOrderDialog.setCancelListener(new CancelOrderDialog.CancelListener() {
                                    @Override
                                    public void onCancel(CancelReason order) {
                                        if (type == order.getType()) {
                                            showWaitDialog();
                                            mOrderPresenter.cancelOrder1(MaiLiApplication.getInstance().getUserInfo().getPhone(), order.getOrder(), order.getReason());
                                        }

                                    }
                                });
                                mCancelOrderDialog.show();
                                if (mBalanceOrderPopuwindow != null) {
                                    mBalanceOrderPopuwindow.dismiss();
                                }
                            }
                        });
                        tv_txt.setText("查看接单人");
                        tv_txt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(aty, OrderListActivity.class)
                                        .putExtra("order", dataBean.getOrder())
                                        .putExtra("type", 1)
                                );
                            }
                        });
                        break;
                    case 2:
                        tv_state.setText("待评价");
                        tv_txt2.setSelected(true);
                        tv_txt2.setTextColor(getResources().getColor(R.color.colorFF6C00));
                        tv_txt2.setText("给他/她评价");
                        tv_txt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(aty, EvaluateActivity.class).putExtra("order", dataBean.getOrder()).
                                        putExtra("type", 2).
                                        putExtra("page", type));
                            }
                        });
                        break;
                    case 3:
                        tv_state.setText("已评价");
                        holder.setVisible(R.id.lly_buttom, false);
                        break;
                    case 4://发单中，人数不足的
                        tv_state.setText("正在进行");
                        tv_txt2.setVisibility(View.VISIBLE);
                        tv_txt1.setVisibility(View.VISIBLE);
                        tv_txt.setVisibility(View.VISIBLE);

                        if (dataBean.getWorkType().equals("1")) {
                            tv_txt1.setText("查看订单进度");
                            tv_txt1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(aty, OrderProgressActivity.class)
                                            .putExtra("order", dataBean.getOrder())
                                    );
                                }
                            });
                        } else {
                            tv_txt1.setText("完成订单");
                            tv_txt1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!TextUtils.isEmpty(dataBean.getDoingPeoplePhone())) {
                                        mBalanceOrderPopuwindow = new BalanceOrderPopuwindow(aty, dataBean.getDoingPeoplePhone(), dataBean.getWorkCost(),dataBean.getOnLine());
                                        mBalanceOrderPopuwindow.setIBanlanceOrderListener(new BalanceOrderPopuwindow.IBanlanceOrderListener() {
                                            @Override
                                            public void onPositive(String phone_list) {
                                                confirOrder(dataBean.getOrder(), phone_list);
                                            }
                                        });
                                        mBalanceOrderPopuwindow.showPopupWindow();
                                        if (mCancelOrderDialog != null) {
                                            mCancelOrderDialog.dismiss();
                                        }
                                    } else {
                                        Toast.makeText(mContext, "该订单目前没有接单人！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        tv_txt2.setSelected(false);
                        tv_txt2.setTextColor(getResources().getColor(R.color.color292929));
                        tv_txt2.setText("取消订单");
                        tv_txt2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mCancelOrderDialog = new CancelOrderDialog(aty, dataBean.getOrder(),
                                        type, 1);

                                mCancelOrderDialog.setCancelListener(new CancelOrderDialog.CancelListener() {
                                    @Override
                                    public void onCancel(CancelReason order) {
                                        if (type == order.getType()) {
                                            showWaitDialog();
                                            mOrderPresenter.cancelOrder1(MaiLiApplication.getInstance().getUserInfo().getPhone(), order.getOrder(), order.getReason());
                                        }

                                    }
                                });
                                mCancelOrderDialog.show();
                                if (mBalanceOrderPopuwindow != null) {
                                    mBalanceOrderPopuwindow.dismiss();
                                }

                            }
                        });
                        tv_txt.setText("查看接单人");
                        tv_txt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(aty, OrderListActivity.class)
                                        .putExtra("order", dataBean.getOrder())
                                        .putExtra("type", 1)
                                );
                            }
                        });
                        break;
                }


                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(dataBean.getWorkType())) {//同城配送
                            startActivity(new Intent(aty, PartTimeJobActivity.class)
                                    .putExtra("order", dataBean.getOrder())
                                    .putExtra("type", 2)
                                    .putExtra("isShowAdd", true)
                                    .putExtra("isShowPhone", false)
                            );
                        } else {
                            startActivity(new Intent(aty, PartTimejobDetailsActivity.class)
                                    .putExtra("order", dataBean.getOrder())
                                    .putExtra("type", 2)
                                    .putExtra("isShowAdd", true)
                                    .putExtra("isShowPhone", false)
                            );
                        }


                    }
                });
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

    private void confirOrder(String order, String phone_list) {
        showWaitDialog();
        mFinishOrderPresenter.finishOrder(order, phone_list, 0);
    }

    private void selectOrder() {
        mPresenter.selectOrder1(MaiLiApplication.getInstance().getUserInfo().getPhone(), type + "", mPage + "", mPageSize + "");
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
        } else if (object instanceof MineBillingModel) {
            MineBillingModel info = (MineBillingModel) object;
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
            if ("跳转".equals(evakuate.getMessage())) {//跳转到进行中
                ((OrderBillingActivity) aty).setPosition(1);
            }
        }
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
