package com.gsy.ml.ui.person;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityOrderListChatBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.message.OrderContent;
import com.gsy.ml.model.person.OrderChatListModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimePresenter;
import com.gsy.ml.prestener.person.OrderChatListPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.message.ChatActivity;
import com.hyphenate.easeui.EaseConstant;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;
import ml.gsy.com.library.utils.glide.GlideCircleTransform;

public class OrderListChatActivity extends BaseActivity implements ILoadPVListener {
    private OrderChatListPresenter mOrderChatListPresenter = new OrderChatListPresenter(this);
    private ActivityOrderListChatBinding mBinding;
    private StateModel stateModel = new StateModel();
    private List<OrderChatListModel.DataBean.RowsBean> mDataList = new ArrayList<>();
    private CommonAdapter<OrderChatListModel.DataBean.RowsBean> mAdapter;
    private int mPage = 1;
    private int mPageSize = 20;
    private PartTimePresenter presenter = new PartTimePresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list_chat;
    }

    private String mOrder = "";

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

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.inHead.tvTitle.setText("查看所有已沟通人数");
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private OrderContent orderContent = null;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityOrderListChatBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setExpandRes("还没有沟通人数呢!", R.drawable.no_data1_icon);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                findChatList();
            }
        });
        mBinding.setStateModel(stateModel);
        mOrder = getIntent().getStringExtra("order");
        orderContent = (OrderContent) getIntent().getSerializableExtra("order_data");
        findChatList();
        mAdapter = new CommonAdapter<OrderChatListModel.DataBean.RowsBean>(aty, R.layout.order_list_chat_item, mDataList) {
            @Override
            protected void convert(ViewHolder holder, final OrderChatListModel.DataBean.RowsBean rowsBean, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                TextView tv_time = holder.getView(R.id.tv_time);
                ImageView img_head = holder.getView(R.id.img_head);
                ImageView img_chat = holder.getView(R.id.img_chat);
                ImageView img_star = holder.getView(R.id.img_star);
                tv_time.setText(fromatToTime(rowsBean.getCreatetime()));//时间
                switch (rowsBean.getStar()) {
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


                Glide.with(aty).load(rowsBean.getHeadUrl()).transform(new GlideCircleTransform(aty))
                        .placeholder(R.drawable.ease_default_avatar).into(img_head);
                tv_name.setText(rowsBean.getNickName());
                if ("男".equals(rowsBean.getSex())) {
                    tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.man_icon, 0);
                } else {
                    tv_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.girl_icon, 0);
                }
                img_chat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        orderContent.setAcceptName(rowsBean.getName());
                        orderContent.setAcceptNickName(rowsBean.getNickName());
                        orderContent.setAcceptHeadUrl(rowsBean.getHeadUrl());
                        orderContent.setAcceptPhone(rowsBean.getAcceptPhone());
                        startActivity(new Intent(aty, ChatActivity.class)
                                .putExtra(EaseConstant.EXTRA_USER_ID, rowsBean.getAcceptRingLetter())
                                .putExtra("nick_name", rowsBean.getNickName())
                                .putExtra("order", orderContent));
                    }
                });
                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(aty, CheckActivity.class).
                                putExtra("phone", rowsBean.getAcceptPhone())
                                .putExtra("isshow", false)
                                .putExtra("type", 2)
                        );
                    }
                });
            }
        };
        mBinding.rcChatList.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcChatList.setAdapter(mAdapter);
        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        findChatList();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                mPage++;
                findChatList();
            }
        });

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

    private void findChatList() {
        mOrderChatListPresenter.findChatList(mOrder, MaiLiApplication.getInstance().mHuanxin, mPage + "", mPageSize + "");
    }

    private void stopRefresh() {
        mBinding.mrlBody.finishRefresh();
        mBinding.mrlBody.finishRefreshLoadMore();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        stopRefresh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof OrderChatListModel) {

            OrderChatListModel info = (OrderChatListModel) object;
            if (mPage == 1) {
                mDataList.clear();
                if (info == null || info.getData() == null || info.getData().getRows() == null || info.getData().getRows().size() == 0) {
                    stateModel.setEmptyState(EmptyState.EXPAND);
                } else {
                    stateModel.setEmptyState(EmptyState.NORMAL);
                }
            }
            if (info != null && info.getData() != null) {

                if (info.getData().getRows().size() < mPageSize) {
                    if (mPage != 1) {
                        Toast.makeText(MaiLiApplication.getInstance(), "已经到底了!", Toast.LENGTH_SHORT).show();
                    }
                    mBinding.mrlBody.setLoadMore(false);
                } else {
                    mBinding.mrlBody.setLoadMore(true);
                }
                mDataList.addAll(info.getData().getRows());
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
