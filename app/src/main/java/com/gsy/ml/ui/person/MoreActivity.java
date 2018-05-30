package com.gsy.ml.ui.person;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityMoreLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.WalletPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/5/13.
 */

public class MoreActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityMoreLayoutBinding mBinding;
    private WalletPresenter wPresenter = new WalletPresenter(this);
    private CommonAdapter<WalletModel.DataBean.FinancialRecordBean> adapter;
    private List<WalletModel.DataBean.FinancialRecordBean> list = new ArrayList<>();
    private StateModel stateModel = new StateModel();

    private int pageNo = 1;
    private int pageSize = 20;


    @Override
    public int getLayoutId() {
        return R.layout.activity_more_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("金额明细");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityMoreLayoutBinding) vdb;

        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                wPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
            }
        });
        mBinding.setStateModel(stateModel);
        initFinancial();
    }

    public void initFinancial() {
        adapter = new CommonAdapter<WalletModel.DataBean.FinancialRecordBean>(aty, R.layout.item_more, list) {
            @Override
            protected void convert(ViewHolder holder, WalletModel.DataBean.FinancialRecordBean fBean, int position) {
                TextView location = holder.getView(R.id.tv_location);   //地址
                TextView date = holder.getView(R.id.tv_date);           // 日期
                TextView detail = holder.getView(R.id.tv_detail);   //进账支出

                location.setText(fBean.getMoneyAbstract().replaceAll(",", "\n"));
                date.setText(Utils.getTimeStyle22(fBean.getTime()));

                switch (fBean.getMoneyType()) {
                    case 0: //系统扣除的金额
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 1:   //发单 扣除的金额
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 2:   //接单得到的金额
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 3:   //充值得到的钱
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 4:   //提现扣除的钱
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 5:   //发单人取消订单,没人接单退还全部的金额
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 6:   //发单人取消订单，接单人获得的金额
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 7:   //有人接单 发单人取消订单扣除的金额
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 8:   //有人接单 发单人取消单返回的金额
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 9:   //提现扣除的金额
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 10:  //提现不通过返回金额
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 11:   //提现团队奖金
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 12:  //团队奖金返还
                        detail.setText("+" + fBean.getMoney());
                        break;
                    case 13:  //接单人取消单扣除的金额
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 14:  //接单人第一单扣除保险金
                        detail.setText("-" + fBean.getMoney());
                        break;
                    case 15:  //一人接单时，接单人取消订单，返回发单人全部金额
                        detail.setText("+" + fBean.getMoney());
                        break;
                }
            }
        };

        mBinding.rvApplyfor.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rvApplyfor.setAdapter(adapter);

        wPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");

        mBinding.mrlBody.setSunStyle(true);

        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pageNo = 1;
                wPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                pageNo++;
                wPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
        }
    }

    private void setWallet(WalletModel model) {
        if (model == null || model.getData() == null) {
            if (pageNo == 1) {
                stateModel.setEmptyState(EmptyState.EMPTY);
            }
            return;
        }

        if (pageNo == 1) {
            list.clear();
            if (model.getData().getFinancialRecord().size() == 0) {
                stateModel.setEmptyState(EmptyState.EMPTY);
            } else {
                stateModel.setEmptyState(EmptyState.NORMAL);
            }
        }


        list.addAll(model.getData().getFinancialRecord());
        adapter.notifyDataSetChanged();
        if (list.size() < pageSize) {
            mBinding.mrlBody.setLoadMore(false);
        } else {
            mBinding.mrlBody.setLoadMore(true);
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
        } else if (object instanceof WalletModel) {
            WalletModel models = (WalletModel) object;
            setWallet(models);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
