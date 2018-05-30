package com.gsy.ml.ui.person;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityShareMoneyLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.ShareMoneyModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.ShareMoneyPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/6/6.
 */

public class ShareMoneyActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityShareMoneyLayoutBinding mBinding;
    private ShareMoneyPresenter presenter = new ShareMoneyPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_share_money_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("分享佣金");
        mBinding.ilHead.vXian.setVisibility(View.GONE);
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private double mTotalMoney = 0;
    private StateModel stateModel = new StateModel();

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityShareMoneyLayoutBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getData();
            }
        });
        mBinding.setStateModel(stateModel);
        mBinding.tvWithdrawDeposit.setOnClickListener(this);
        getData();
    }

    public void getData() {
        presenter.selectMyTeam(MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_withdraw_deposit:
                if (mTotalMoney > 0) {
                    startActivity(new Intent(aty, CommissionDrawMoneyActivity.class).putExtra("totalMoney", mTotalMoney));
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "提现金额必须大于0!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(String message) {
        if ("更新佣金价格".equals(message)) {
            getData();
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof ShareMoneyModel) {
            ShareMoneyModel model = (ShareMoneyModel) object;
            stateModel.setEmptyState(EmptyState.NORMAL);
            mTotalMoney = DemoUtils.formatDoubleDOWN(model.getMoney());
            mBinding.tvEarnings.setText(mTotalMoney + "");
            mBinding.tvTotalMoney.setText(model.getPeopleNum() + "");
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
