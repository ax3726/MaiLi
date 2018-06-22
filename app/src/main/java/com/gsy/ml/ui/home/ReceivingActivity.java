package com.gsy.ml.ui.home;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityReceivingLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.ReceivingModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.ReceivingPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.ProvinceAreaHelper;
import com.gsy.ml.ui.views.ChoosePopuWindow;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/27.
 * 我要接单类
 */

public class ReceivingActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener, ChoosePopuWindow.IChooseListener {
    ActivityReceivingLayoutBinding mBinding;
    private CommonAdapter<ReceivingModel.DataBean> mAdapter;
    private List<ReceivingModel.DataBean> list = new ArrayList<>();
    private ReceivingPresenter presenter = new ReceivingPresenter(this);
    private ChoosePopuWindow mSortPop;//排序
    private ChoosePopuWindow mTypeOrderPop;//工种筛选
    private ChoosePopuWindow mAreaPop;//地区筛选

    private int mPage = 1;
    private int mPageSize = 20;
    private int mSortPosition = 0;//排序下标
    private int mTypeOrder = 0;//默认
    private String mArea = "区域不限";//地区
    private UserInfoModel.UserPlaceBean userPlace;
    private StateModel stateModel = new StateModel();


    @Override
    public int getLayoutId() {
        return R.layout.activity_receiving_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.llyLeft.setOnClickListener(this);
        mBinding.llySearch.setOnClickListener(this);
    }

    private void initListener() {
        mBinding.tvCity.setOnClickListener(this);
        mBinding.tvOrderType.setOnClickListener(this);
        mBinding.tvSort.setOnClickListener(this);
        mBinding.tvCity.setOnClickListener(this);

    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityReceivingLayoutBinding) vdb;
        mTypeOrder = getIntent().getIntExtra("work_type", 0);
        mBinding.tvOrderType.setText(DemoUtils.TypeToOccupation(mTypeOrder));
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setExpandRes("还没有发布信息呢!", R.drawable.no_data1_icon);
        mBinding.setStateModel(stateModel);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                inquireOrder();
            }
        });
        userPlace = MaiLiApplication.getInstance().getUserPlace();
        mBinding.tvCity.setText(mArea);
        initListener();
        initView();
    }

    private void initView() {
        mSortPop = new ChoosePopuWindow(aty);
        List<String> data = new ArrayList<>();
        data.add("推荐排序");
        data.add("离我最近");
        data.add("最新发布");
        data.add("工资最高");
        mSortPop.setData(data, 0);
        mSortPop.setIChooseListener(this);

        mTypeOrderPop = new ChoosePopuWindow(aty);
        List<String> data1 = new ArrayList<>();
        for (int i = 0; i <= 41; i++) {
            if (i != 13) {
                data1.add(i + "");
            }
        }
        mTypeOrderPop.setData(data1, 1);
        mTypeOrderPop.setIChooseListener(this);

        mAreaPop = new ChoosePopuWindow(aty);
        ProvinceAreaHelper mAreaHelper = new ProvinceAreaHelper(aty);
        mAreaHelper.initProvinceData();
        String[] strings = mAreaHelper.updateAreas(userPlace.getCity());
        List<String> data2 = new ArrayList<>();
        data2.add("区域不限");
        if (strings != null) {
            for (String s : strings) {
                data2.add(s);
            }
        }
        mAreaPop.setData(data2, 2);
        mAreaPop.setIChooseListener(this);
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
                if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType()))) {
                    if (Integer.valueOf(dataBean.getWorkType()) == 24) {
                        tv_distance.setText("");
                        tv_title.setText(DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())) + "\t"
                                + DemoUtils.TypeToContent2(Integer.valueOf(dataBean.getWorkType()), dataBean.getWorkContent()));
                    } else {
                        tv_distance.setText("");
                        tv_title.setText(DemoUtils.TypeToNoAddressTitle(Integer.valueOf(dataBean.getWorkType()), dataBean.getWorkContent()));
                    }

                } else {
                    tv_title.setText(dataBean.getStartPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                    tv_distance.setText(DemoUtils.countDistance1(dataBean.getJuli()));
                }

                tv_location.setText(dataBean.getStartArea());
                tv_money.setText(dataBean.getWorkCost() + "");
                tv_grade.setText(dataBean.getWorkLevel() +"");
                tv_time.setText(Utils.getTimeStyle2(dataBean.getSendTime()));


                img_yuyue.setVisibility(dataBean.getReservationTime() > 0 ? View.VISIBLE : View.GONE);

                /*lly_body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(aty, PartTimeJobActivity.class)
                                .putExtra("order", dataBean.getOrder())
                                .putExtra("isShowAdd", true)
                                .putExtra("isShowPhone", false)
                        );
                    }
                });*/

                lly_body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(dataBean.getWorkType())) {//同城配送
                            startActivity(new Intent(aty, PartTimeJobActivity.class)
                                    .putExtra("order", dataBean.getOrder())
                                    .putExtra("isShowAdd", true)
                                    .putExtra("isShowPhone", false));
                        } else {
                            if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType())) && Integer.valueOf(dataBean.getWorkType()) != 24) {
                                startActivity(new Intent(aty, PartTime1Activity.class)
                                        .putExtra("order", dataBean.getOrder())
                                );
                            } else {
                                startActivity(new Intent(aty, PartTimeActivity.class)
                                        .putExtra("order", dataBean.getOrder())
                                );
                            }

                        }
                    }
                });
            }
        };
        mBinding.rvReceiving.setLayoutManager(new LinearLayoutManager(aty));


        mBinding.rvReceiving.setAdapter(mAdapter);
        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setLoadMore(true);


        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        inquireOrder();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                mPage++;
                inquireOrder();
            }
        });
        mPage = 1;
        inquireOrder();
    }

    public void inquireOrder() {
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        int order = 2;
        if (mSortPosition == 2 || mSortPosition == 0) {
            order = 2;
        } else {
            order = mSortPosition;
        }
        presenter.inquire(userPlace.getCity(),
                mArea,
                mTypeOrder + "",
                userPlace.getJing(),
                userPlace.getWei(),
                order + "",
                mPage + "",
                mPageSize + ""
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_sort:
                mSortPop.setCurtxt(mBinding.tvSort.getText().toString().trim());
                mSortPop.showPopupWindow(mBinding.llyHead);
                break;
            case R.id.tv_order_type:
                mTypeOrderPop.setCurtxt(mBinding.tvOrderType.getText().toString().trim());
                mTypeOrderPop.showPopupWindow(mBinding.llyHead);
                break;
            case R.id.tv_city:
                mAreaPop.setCurtxt(mBinding.tvCity.getText().toString().trim());
                mAreaPop.showPopupWindow(mBinding.llyHead);
                break;
            case R.id.lly_search://搜索
                startActivity(new Intent(aty, OrderSearchActivity.class));
                break;
        }
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
        } else if (object instanceof ReceivingModel) {
            ReceivingModel info = (ReceivingModel) object;
            if (mPage == 1) {
                list.clear();
                if (info == null || info.getData() == null || info.getData().size() == 0) {
                    stateModel.setEmptyState(EmptyState.EXPAND);
                } else {
                    stateModel.setEmptyState(EmptyState.NORMAL);
                }
            }
            if (info != null && info.getData() != null) {

                if (info.getData().size() < mPageSize) {
                    if (mPage != 1) {
                        Toast.makeText(MaiLiApplication.getInstance(), "已经到底了!", Toast.LENGTH_SHORT).show();
                    }
                    mBinding.mrlBody.setLoadMore(false);
                } else {
                    mBinding.mrlBody.setLoadMore(true);
                }
                list.addAll(info.getData());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(String message) {
        if (message.equals("update_order")) {
            mPage = 1;
            inquireOrder();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }

    @Override
    public void selectItem(int position, String item, int type) {

        if (type == 0) {
            mSortPosition = position;
            mBinding.tvSort.setText(item);
        } else if (type == 1) {
            mTypeOrder = Integer.valueOf(item);
            mBinding.tvOrderType.setText(DemoUtils.TypeToOccupation(mTypeOrder));
        } else if (type == 2) {
            mArea = item;
            mBinding.tvCity.setText(mArea);
        }
        mPage = 1;
        inquireOrder();
    }
}
