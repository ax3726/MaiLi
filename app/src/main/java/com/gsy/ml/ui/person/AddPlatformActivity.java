package com.gsy.ml.ui.person;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityAddPlatformBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.AddPlatformModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.AddPlatformPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddPlatformActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityAddPlatformBinding mBinding;
    private AddPlatformPresenter presenter = new AddPlatformPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_platform;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("外卖一键发单");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private StateModel stateModel = new StateModel();

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityAddPlatformBinding) vdb;
        mBinding.rlAdd.setOnClickListener(this);
        mBinding.rlMei.setOnClickListener(this);
        mBinding.rlE.setOnClickListener(this);
        mBinding.rlBai.setOnClickListener(this);
        stateModel.setExpandRes("还没有入驻店家哦！", R.drawable.no_data1_icon);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getSubmit();
            }
        });
        mBinding.setStateModel(stateModel);
        getSubmit();
    }
    public void getSubmit() {

        presenter.referStore(MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.rl_add:
                startActivity(new Intent(aty, FastConsignmentInvoiceActivity.class));
                break;
            case R.id.rl_mei:
                startActivity(new Intent(aty, SpeedinessShipmentsActivity.class).putExtra("type", 2));
                break;
            case R.id.rl_e:
                startActivity(new Intent(aty, SpeedinessShipmentsActivity.class).putExtra("type", 3));
                break;
            case R.id.rl_bai:
                startActivity(new Intent(aty, SpeedinessShipmentsActivity.class));
                break;
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof AddPlatformModel) {
            AddPlatformModel model = (AddPlatformModel) object;
            if (model.getData() != null) {
                stateModel.setEmptyState(EmptyState.NORMAL);
                if (model.getData().isMeituan()) {
                    mBinding.rlMei.setVisibility(View.VISIBLE);
                } else {
                    mBinding.rlMei.setVisibility(View.GONE);
                }
                if (model.getData().isEleme()) {
                    mBinding.rlE.setVisibility(View.VISIBLE);
                } else {
                    mBinding.rlE.setVisibility(View.GONE);
                }
                if (model.getData().isBaidu()) {
                    mBinding.rlBai.setVisibility(View.VISIBLE);
                } else {
                    mBinding.rlBai.setVisibility(View.GONE);
                }
            } else {//
                stateModel.setEmptyState(EmptyState.EXPAND);
                Toast.makeText(aty, "还没有入驻店家哦！", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelActivity(String action) {
        if ("关闭".equals(action)) {
            getSubmit();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
