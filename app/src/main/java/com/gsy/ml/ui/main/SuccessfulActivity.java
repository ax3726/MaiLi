package com.gsy.ml.ui.main;

import android.content.Intent;
import android.view.View;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivitySuccessfulLayoutBinding;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.person.IdentityCardActivity;

/**
 * Created by Administrator on 2017/4/14.
 * 注册成功页面
 */

public class SuccessfulActivity extends BaseActivity {
    private ActivitySuccessfulLayoutBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_successful_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivitySuccessfulLayoutBinding) vdb;
        mBinding.btPerfect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessfulActivity.this, IdentityCardActivity.class));
                finish();
            }
        });
        mBinding.tvJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuccessfulActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}
