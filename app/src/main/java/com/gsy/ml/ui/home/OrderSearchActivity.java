package com.gsy.ml.ui.home;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityOrderSearchBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.ReceivingModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.ReceivingPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

public class OrderSearchActivity extends BaseActivity implements ILoadPVListener {
    private ReceivingPresenter mPresenter = new ReceivingPresenter(this);
    private List<ReceivingModel.DataBean> list = new ArrayList<>();
    private CommonAdapter<ReceivingModel.DataBean> mAdapter;
    private ActivityOrderSearchBinding mBinding;
    private int mPage = 1;
    private int mPageSize = 20;
    private String mContent = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_search;
    }

    private StateModel stateModel = new StateModel();

    private void search() {
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        mPresenter.fuzzyOrder(mContent, userPlace.getJing(), userPlace.getWei(), mPage + "", mPageSize + "");
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityOrderSearchBinding) vdb;
        mBinding.idLeftBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        stateModel.setEmptyState(EmptyState.NORMAL);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                mPage = 1;
                stateModel.setEmptyState(EmptyState.PROGRESS);
                search();
            }
        });
        mBinding.setStateModel(stateModel);
        mBinding.etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // 输入法中点击搜索
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    //这里调用搜索方法

                    String con = mBinding.etSearch.getText().toString().trim();
                    Utils.closeInputPad(aty);
                    if (!TextUtils.isEmpty(con)) {
                        mContent = con;
                        mPage = 1;
                        stateModel.setEmptyState(EmptyState.PROGRESS);
                        search();
                    } else {
                        Toast.makeText(MaiLiApplication.getInstance(), "请输入要搜索订单的关键字/地址/建筑物", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }

                return false;
            }
        });
        mBinding.tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String con = mBinding.etSearch.getText().toString().trim();
                Utils.closeInputPad(aty);
                if (!TextUtils.isEmpty(con)) {
                    mContent = con;
                    mPage = 1;
                    stateModel.setEmptyState(EmptyState.PROGRESS);
                    search();
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "请输入要搜索订单的关键字/地址/建筑物", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mAdapter = new CommonAdapter<ReceivingModel.DataBean>(this, R.layout.item_receiving_message, list) {
            @Override
            protected void convert(ViewHolder holder, final ReceivingModel.DataBean dataBean, int position) {
                LinearLayout lly_body = holder.getView(R.id.lly_body);
                TextView tv_title = holder.getView(R.id.tv_title);  //名字
                TextView tv_location = holder.getView(R.id.tv_location);  //地点
                TextView tv_grade = holder.getView(R.id.tv_grade);     //兼职等级
                TextView tv_money = holder.getView(R.id.tv_money);    // 金钱
                TextView tv_distance = holder.getView(R.id.tv_distance);    // 金钱
                TextView tv_time = holder.getView(R.id.tv_time);    // 时间
                ImageView img_type = holder.getView(R.id.img_type);
                ImageView img_yuyue = holder.getView(R.id.img_yuyue);
                img_type.setImageResource(DemoUtils.TypeToImage(Integer.valueOf(dataBean.getWorkType())));
                tv_title.getPaint().setFakeBoldText(true);//字体加粗
                tv_title.setText(dataBean.getStartPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                tv_location.setText(dataBean.getStartArea());
                tv_money.setText(dataBean.getWorkCost() + "");
                tv_grade.setText(dataBean.getWorkLevel() + "兼职");
                tv_time.setText(Utils.getTimeStyle2(dataBean.getSendTime()));
                tv_distance.setText(DemoUtils.countDistance1(dataBean.getJuli()));
                img_yuyue.setVisibility(dataBean.getReservationTime() > 0 ? View.VISIBLE : View.GONE);

                lly_body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(aty, PartTimeJobActivity.class)
                                .putExtra("order", dataBean.getOrder())
                                .putExtra("isShowAdd", true)
                                .putExtra("isShowPhone", false)
                        );
                    }
                });
            }
        };
        mBinding.rcContent.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcContent.setAdapter(mAdapter);
        mBinding.mrlContent.setSunStyle(true);
        mBinding.mrlContent.setLoadMore(true);
        mBinding.mrlContent.setLoadMore(false);
        mBinding.mrlContent.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        search();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                mPage++;
                search();
            }
        });
    }

    private void stopRefresh() {
        mBinding.mrlContent.finishRefresh();
        mBinding.mrlContent.finishRefreshLoadMore();
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

        } else if (object instanceof ReceivingModel) {
            ReceivingModel info = (ReceivingModel) object;
            if (mPage == 1) {
                list.clear();
                if (info != null && info.getData().size() > 0) {
                    stateModel.setEmptyState(EmptyState.NORMAL);
                } else {
                    stateModel.setEmptyState(EmptyState.EMPTY);
                }
            }
            if (info != null && info.getData() != null) {

                if (info.getData().size() < mPageSize) {
                    mBinding.mrlContent.setLoadMore(false);
                } else {
                    mBinding.mrlContent.setLoadMore(true);
                }
                list.addAll(info.getData());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
