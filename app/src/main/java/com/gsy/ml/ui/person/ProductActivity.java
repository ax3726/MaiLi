package com.gsy.ml.ui.person;

import android.content.Intent;
import android.view.View;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityProductLayoutBinding;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.utils.DemoUtils;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/5/19.
 */

public class ProductActivity extends BaseActivity implements View.OnClickListener {
    private ActivityProductLayoutBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_product_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("关于蚂蚁快服");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityProductLayoutBinding) vdb;
        mBinding.tvVersions.setOnClickListener(this);
        mBinding.tvMianze.setOnClickListener(this);
        mBinding.tvFadan.setOnClickListener(this);
        mBinding.tvJiedan.setOnClickListener(this);
        mBinding.tvVersions.setText("v" + Utils.getVersionName(aty));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mianze:
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(-4)));
                break;
            case R.id.tv_fadan:
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(-6)));
                break;
            case R.id.tv_jiedan:
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(-5)));
                break;
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_versions:

                break;
        }
    }


}
