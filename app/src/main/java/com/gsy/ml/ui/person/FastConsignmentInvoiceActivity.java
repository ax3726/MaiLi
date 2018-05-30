package com.gsy.ml.ui.person;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityFastConsignmentInvoiceBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.BankCardModel;
import com.gsy.ml.model.person.TakenOutModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.TokenOutPresenter;
import com.gsy.ml.ui.common.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/8/4.
 */

public class FastConsignmentInvoiceActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityFastConsignmentInvoiceBinding mBinding;
    private TokenOutPresenter presenter = new TokenOutPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_fast_consignment_invoice;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("外卖一键发单");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityFastConsignmentInvoiceBinding) vdb;
        EventBus.getDefault().register(aty);
        mBinding.tvMeituan.setOnClickListener(this);
        mBinding.tvEleme.setOnClickListener(this);
        mBinding.tvBaidu.setOnClickListener(this);
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_eleme:
                startActivity(new Intent(aty, TakenOutActivity.class).putExtra("parameter", 1));
                break;
            case R.id.tv_meituan:
                //美团
                showWaitDialog();
                presenter.setakenOut(MaiLiApplication.getInstance().getUserInfo().getPhone());
                break;
            case R.id.tv_baidu:
                startActivity(new Intent(aty, TakenOutActivity.class).putExtra("parameter", 3));
                break;
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;

            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof BankCardModel) {

        } else if (object instanceof TakenOutModel) {
            TakenOutModel model = (TakenOutModel) object;
            startActivity(new Intent(aty, MeiTuanWebActivity.class).putExtra("url", model.getData()));

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelActivity(String action) {
        if ("关闭".equals(action)) {
          finish();
        }
    }
    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
