package com.gsy.ml.ui.person;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityOrderReceivingLayoutBinding;
import com.gsy.ml.ui.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/24.
 * 接单
 */

public class OrderReceivingActivity extends BaseActivity implements View.OnClickListener {
    private List<String> title = new ArrayList<>();
    OrderReceivingFragment mShenQingFragment;
    OrderReceivingFragment mJinXinggFragment;
    OrderReceivingFragment mWanChengFragment;
    OrderReceivingFragment mQuXiaoFragment;
    OrderReceivingFragment mPingJiaFragment;
    private List<Fragment> fragments = new ArrayList<>();
    private ActivityOrderReceivingLayoutBinding mBinding;
    private MyPagerAdapter mAdapter;
    private int type = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_receiving_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("我的接单");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
        }
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityOrderReceivingLayoutBinding) vdb;
        type = getIntent().getIntExtra("type", 0);
        title.add("已沟通");
        title.add("进行中");
        title.add("已完成");
        title.add("已取消");
        title.add("待评价");
        mShenQingFragment = new OrderReceivingFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 6);
        mShenQingFragment.setArguments(bundle);
        mJinXinggFragment = new OrderReceivingFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("type", 7);
        mJinXinggFragment.setArguments(bundle1);
        mWanChengFragment = new OrderReceivingFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("type", 8);
        mWanChengFragment.setArguments(bundle2);
        mQuXiaoFragment = new OrderReceivingFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("type", 9);
        mQuXiaoFragment.setArguments(bundle3);
        mPingJiaFragment = new OrderReceivingFragment();
        Bundle bundle4 = new Bundle();
        bundle4.putInt("type", 10);
        mPingJiaFragment.setArguments(bundle4);


        fragments.add(mShenQingFragment);
        fragments.add(mJinXinggFragment);
        fragments.add(mWanChengFragment);
        fragments.add(mQuXiaoFragment);
        fragments.add(mPingJiaFragment);
        initView();
        setPosition();
    }

    private void initView() {
        mBinding.psHead.setUnderlineColorResource(R.color.colorE7E7E7);
        mBinding.psHead.setIndicatorColorResource(R.color.colorFF6C00);
        mBinding.psHead.setTextSize(getResources().getDimensionPixelSize(R.dimen.PX28));

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());

        mBinding.vpOrders.setAdapter(mAdapter);
        mBinding.psHead.setViewPager(mBinding.vpOrders);

        mBinding.vpOrders.setOffscreenPageLimit(5);
        mBinding.psHead.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPage(int position) {
        switch (position) {
            case 0:
                mShenQingFragment.loadData();
                break;
            case 1:
                mJinXinggFragment.loadData();
                break;
            case 2:
                mWanChengFragment.loadData();
                break;
            case 3:
                mQuXiaoFragment.loadData();
                break;
            case 4:
                mPingJiaFragment.loadData();
                break;
        }
    }

    private void setPosition() {
        mBinding.vpOrders.setCurrentItem(type);
        setPage(type);
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title.get(position);
        }

        @Override
        public int getCount() {
            return title.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
