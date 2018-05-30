package com.gsy.ml.ui.home;

import android.content.Intent;
import android.view.View;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivitySendOrderSuccessBinding;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.person.OrderBillingActivity;
import com.gsy.ml.ui.utils.DemoUtils;

public class SendOrderSuccessActivity extends BaseActivity implements View.OnClickListener {

    ActivitySendOrderSuccessBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_send_order_success;
    }

    private int mType = 1;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivitySendOrderSuccessBinding) vdb;
        mType = getIntent().getIntExtra("type", 1);
        mBinding.inHead.tvTitle.setText("发单成功");
        mBinding.inHead.llyLeft.setOnClickListener(this);
        mBinding.tvAgainOrder.setOnClickListener(this);
        mBinding.tvMyOrder.setOnClickListener(this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_again_order://继续单
                DemoUtils.TypeToActivity(aty, mType);
                finish();
                break;
            case R.id.tv_my_order://我的发单
                startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 0));
                finish();
                break;
        }
    }
}
