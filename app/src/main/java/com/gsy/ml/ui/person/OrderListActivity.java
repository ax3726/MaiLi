package com.gsy.ml.ui.person;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityOrderListBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.PartTimeJobModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimeJobPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

public class OrderListActivity extends BaseActivity implements ILoadPVListener {
    private PartTimeJobPresenter mPresenter = new PartTimeJobPresenter(this);
    private ActivityOrderListBinding mBinding;
    private List<PartTimeJobModel.DataBean.AcceptOrdersListBean> mDataList = new ArrayList<>();
    private CommonAdapter<PartTimeJobModel.DataBean.AcceptOrdersListBean> mAdapter;
    private String mOrder = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_list;
    }

    private StateModel stateModel = new StateModel();
    private int mType = 0;//0  查看所有申请   查看所有接单人

    @Override
    public void initActionBar() {
        super.initActionBar();
        if (mType == 0) {
            mBinding.inHead.tvTitle.setText("查看所有申请人");
        } else {
            mBinding.inHead.tvTitle.setText("查看所有接单人");
        }
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
    public void initData() {
        super.initData();
        mBinding = (ActivityOrderListBinding) vdb;
        mOrder = getIntent().getStringExtra("order");
        mType = getIntent().getIntExtra("type", 0);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getData();
            }
        });
        mBinding.setStateModel(stateModel);
        mAdapter = new CommonAdapter<PartTimeJobModel.DataBean.AcceptOrdersListBean>(aty, R.layout.item_order_list, mDataList) {
            @Override
            protected void convert(ViewHolder holder, final PartTimeJobModel.DataBean.AcceptOrdersListBean acceptOrdersListBean, int position) {
                ImageView img = holder.getView(R.id.img_head);
                TextView txt_name = holder.getView(R.id.tv_name);
                TextView txt_phone = holder.getView(R.id.tv_phones);
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                String name = TextUtils.isEmpty(acceptOrdersListBean.getAcceptPeople()) ? "未知" : acceptOrdersListBean.getAcceptPeople();
                if (mType == 1) {
                    txt_phone.setVisibility(View.VISIBLE);
                    String acceptPhone = TextUtils.isEmpty(acceptOrdersListBean.getAcceptPhone()) ? "未知" : acceptOrdersListBean.getAcceptPhone();
                    txt_phone.setText(acceptPhone);
                } else {
                    txt_phone.setVisibility(View.GONE);
                    if (acceptOrdersListBean.getOrderStatus() == 0) {
                        holder.setVisible(R.id.img_yiluyong, true);
                    } else {
                        holder.setVisible(R.id.img_yiluyong, false);
                    }
                }
                txt_name.setText(name);
                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(aty, CheckActivity.class).
                                        putExtra("phone", acceptOrdersListBean.getAcceptPhone())
                                        .putExtra("order", acceptOrdersListBean.getOrder())
                                        .putExtra("isshow", acceptOrdersListBean.getOrderStatus() != 0)
                                        .putExtra("type", mType)
                                        .putExtra("name", acceptOrdersListBean.getAcceptPeople())
                                        .putExtra("id", String.valueOf(acceptOrdersListBean.getId()))
                                , 1);
                    }
                });
                Glide.with(aty.getApplicationContext())
                        .load(acceptOrdersListBean.getHeadUrl())
                        .placeholder(R.drawable.head_icon)
                        .into(img);
            }
        };
        mBinding.reOrderList.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.reOrderList.setAdapter(mAdapter);
        getData();
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof PartTimeJobModel) {
            PartTimeJobModel info = (PartTimeJobModel) object;
            mDataList.clear();
            if (info != null && info.getData() != null && info.getData().getAcceptOrdersList() != null && info.getData().getAcceptOrdersList().size() > 0) {
                stateModel.setEmptyState(EmptyState.NORMAL);

                if (mType != 0) {
                    for (int i = 0; i < info.getData().getAcceptOrdersList().size(); i++) {
                        if (info.getData().getAcceptOrdersList().get(i).getOrderStatus() == 0) {//未录取
                            mDataList.add(info.getData().getAcceptOrdersList().get(i));
                        }
                    }
                } else {
                    mDataList.addAll(info.getData().getAcceptOrdersList());
                }

                mAdapter.notifyDataSetChanged();
                if (mType == 0) {
                    mBinding.inHead.tvTitle.setText("共有" + info.getData().getAcceptOrdersList().size() + "人申请");
                } else {
                    mBinding.inHead.tvTitle.setText("共有" + mDataList.size() + "人接单");
                }

            } else {
                stateModel.setEmptyState(EmptyState.EMPTY);
            }

        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            finish();
        }
    }
}
