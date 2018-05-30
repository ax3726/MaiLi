package com.gsy.ml.ui.person;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityVoucherLayoutBinding;
import com.gsy.ml.databinding.ItemVoucherHeadBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.VoucherPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/6/7.
 */

public class VoucherActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityVoucherLayoutBinding mBinding;
    private CommonAdapter<VoucherModel.DataBean> mAdapter;
    private List<VoucherModel.DataBean> mDataList = new ArrayList<>();
    private VoucherPresenter mVoucherPresenter = new VoucherPresenter(this);
    private StateModel stateModel = new StateModel();
    private int type = 0;  //0  查询全部   1 按工种查询
    private int mWorkType = 0;
    private double mOrderMoney = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_voucher_layout;
    }

    private int mPage = 1;
    private int mPageSize = 20;

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("优惠劵");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityVoucherLayoutBinding) vdb;
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        mWorkType = intent.getIntExtra("workType", 0);
        mOrderMoney = intent.getDoubleExtra("orderMoney", 0);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setExpandRes("暂时没有可用的优惠劵!", R.drawable.no_data_ml_icon);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getVoucher();
            }
        });

        mBinding.setStateModel(stateModel);

        if (type == 1) {
            ItemVoucherHeadBinding headBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_voucher_head, null, false);
            headBinding.llyHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventBus.getDefault().post(new VoucherModel.DataBean());
                    finish();
                }
            });
            mBinding.rvVoucher.addHeaderView(headBinding.getRoot());
        }

        mAdapter = new CommonAdapter<VoucherModel.DataBean>(aty, R.layout.item_voucher_message, mDataList) {
            @Override
            protected void convert(ViewHolder holder, final VoucherModel.DataBean data, int position) {
                TextView tv_face_value = holder.getView(R.id.tv_face_value);
                TextView tv_maney = holder.getView(R.id.tv_maney);
                TextView tv_data = holder.getView(R.id.tv_data);
                TextView tv_employ = holder.getView(R.id.tv_employ);
                TextView tv_gold_securities = holder.getView(R.id.tv_gold_securities);
                if (data.getWorkType() == 0) {
                    tv_gold_securities.setText("通用优惠劵");
                } else {
                    tv_gold_securities.setText(DemoUtils.TypeToOccupation(data.getWorkType()) + "优惠劵");
                }
                tv_face_value.getPaint().setFakeBoldText(true);
                tv_face_value.setText("¥\t" + data.getFaceValue());
                tv_maney.setText("满" + data.getThreshold() + "元使用");

                tv_data.setText("使用日期从" + Utils.getDateToString(data.getStartTime(), "yyyy-MM-dd") + "\n至" + Utils.getDateToString(data.getEndTime(), "yyyy-MM-dd"));
                if (type == 0) {
                    tv_employ.setVisibility(View.GONE);
                } else {
                    tv_employ.setVisibility(View.VISIBLE);
                    tv_employ.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().post(data);
                            finish();
                        }
                    });

                    if (mOrderMoney >= data.getThreshold()) {
                        tv_employ.setTextColor(getResources().getColor(R.color.colorFF0000));
                        tv_employ.setEnabled(true);
                    } else {
                        tv_employ.setTextColor(getResources().getColor(R.color.colortextnomal));
                        tv_employ.setEnabled(false);

                    }
                }

            }
        };
        mBinding.rvVoucher.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rvVoucher.setAdapter(mAdapter);
        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        getVoucher();
                    }
                }, 600);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                mPage++;
                getVoucher();
            }
        });
        getVoucher();
    }

    private void getVoucher() {
        String phone = MaiLiApplication.getInstance().getUserInfo().getPhone();
        if (type == 0) {
            mVoucherPresenter.selectAllVocher(phone, mPage + "", mPageSize + "");
        } else {
            mVoucherPresenter.selectVocherByWork(phone, mWorkType + "", mPage + "", mPageSize + "");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
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
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof VoucherModel) {
            VoucherModel models = (VoucherModel) object;
            if (mPage == 1) {
                mDataList.clear();
                if (models.getData() == null || models.getData().size() == 0) {
                    stateModel.setEmptyState(EmptyState.EXPAND);
                } else {
                    stateModel.setEmptyState(EmptyState.NORMAL);
                }
            }
            mDataList.addAll(models.getData());
            mBinding.rvVoucher.notifyDataSetChanged();
            if (mDataList.size() < mPageSize) {
                mBinding.mrlBody.setLoadMore(false);
            } else {
                mBinding.mrlBody.setLoadMore(true);
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
