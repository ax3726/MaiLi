package com.gsy.ml.ui.person;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityMoreMessageBinding;
import com.gsy.ml.ui.common.BaseActivity;

public class MoreMessageActivity extends BaseActivity implements View.OnClickListener {
    private ActivityMoreMessageBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_more_message;
    }

    private String type = "";

    @Override
    public void initActionBar() {
        super.initActionBar();


        mBinding.inHead.tvTitle.setText(TextUtils.isEmpty(type) ? "个人地址及开工设置" : "开工设置");

        mBinding.inHead.llyLeft.setOnClickListener(this);
        mBinding.inHead.vXian.setVisibility(View.GONE);
    }

    private void initListener() {
        mBinding.tvLocation.setOnClickListener(this);
        mBinding.tvWorkAddress.setOnClickListener(this);
        mBinding.tvWorkPre.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityMoreMessageBinding) vdb;
        type = getIntent().getStringExtra("type");
        if (!TextUtils.isEmpty(type)) {
            mBinding.tvLocation.setVisibility(View.GONE);
        }
        initListener();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_location:
                startActivity(new Intent(aty, UsualAddressActivity.class));
                break;
            case R.id.tv_work_address:
                startActivity(new Intent(aty, AreaPreActivity.class));
                break;
            case R.id.tv_work_pre:
                startActivity(new Intent(aty, JobPreActivity.class));
                break;
            case R.id.lly_left:
                finish();
                break;
        }
    }
}
