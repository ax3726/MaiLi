package com.gsy.ml.ui.person;

import android.view.View;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityElemeBinding;
import com.gsy.ml.databinding.ActivityMeituanBinding;
import com.gsy.ml.ui.common.BaseActivity;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ElemeActivity extends BaseActivity implements View.OnClickListener {
    private ActivityElemeBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_eleme;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding = (ActivityElemeBinding) vdb;
        mBinding.ilHead.tvTitle.setText("外卖一键发单");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
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
        }
    }
}
