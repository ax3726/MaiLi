package com.gsy.ml.ui.home;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsy.ml.R;
import com.gsy.ml.databinding.Viewpager1Binding;
import com.gsy.ml.model.main.OrderTypeModel;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.utils.DemoUtils;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;


/**
 * Created by Administrator on 2017/5/17.
 */

public class ViewPager1 extends BaseFragment {
    private Viewpager1Binding mBinding;
    private List<OrderTypeModel> mDataList = new ArrayList<>();
    private CommonAdapter<OrderTypeModel> mAdapter;
    private boolean isWork = true;

    @Override
    public int getLayoutId() {
        return R.layout.viewpager1;
    }

    @Override
    protected void initWidget(View parentView) {
        super.initWidget(parentView);
    }

    public void setWorkStatus(boolean is_work) {
        this.isWork = is_work;
    }

    @Override
    protected void initData() {
        super.initData();
        mBinding = (Viewpager1Binding) vdb;
        int page = getArguments().getInt("page", 0);
        mDataList.clear();

        if (page == 0) {
            mDataList.add(new OrderTypeModel("同城配送", 1, R.drawable.work_type1));
            mDataList.add(new OrderTypeModel("课程家教", 2, R.drawable.work_type2));
            mDataList.add(new OrderTypeModel("艺术家教", 3, R.drawable.work_type3));
            mDataList.add(new OrderTypeModel("游戏陪练", 24, R.drawable.work_type29));
            mDataList.add(new OrderTypeModel("运动陪练", 12, R.drawable.work_type11));
            mDataList.add(new OrderTypeModel("钟点工", 4, R.drawable.work_type4));
            mDataList.add(new OrderTypeModel("保姆", 6, R.drawable.work_type6));
            mDataList.add(new OrderTypeModel("保洁", 23, R.drawable.work_type5));
            mDataList.add(new OrderTypeModel("护工", 5, R.drawable.work_type9));
            mDataList.add(new OrderTypeModel("厨师", 7, R.drawable.work_type22));
        } else if (page == 1) {
            mDataList.add(new OrderTypeModel("导游", 9, R.drawable.work_type10));
            mDataList.add(new OrderTypeModel("传单派发", 8, R.drawable.work_type13));
            mDataList.add(new OrderTypeModel("促销导购", 22, R.drawable.work_type14));
            mDataList.add(new OrderTypeModel("衣物干洗", 25, R.drawable.work_type23));
            mDataList.add(new OrderTypeModel("宠物服务", 16, R.drawable.work_type19));
            mDataList.add(new OrderTypeModel("电脑维修", 18, R.drawable.work_type15));
            mDataList.add(new OrderTypeModel("电器服务", 17, R.drawable.work_type8));
            mDataList.add(new OrderTypeModel("车辆服务", 14, R.drawable.work_type12));
            mDataList.add(new OrderTypeModel("手机维修", 27, R.drawable.work_type25));
            mDataList.add(new OrderTypeModel("电动车维修", 26, R.drawable.work_type24));
            //mDataList.add(new OrderTypeModel("代驾",13));
        } else if (page == 2) {
            mDataList.add(new OrderTypeModel("服务员", 10, R.drawable.work_type16));
            mDataList.add(new OrderTypeModel("保安", 28, R.drawable.work_type26));
            mDataList.add(new OrderTypeModel("技工", 19, R.drawable.work_type20));
            mDataList.add(new OrderTypeModel("搬货搬家", 21, R.drawable.work_type7));
            mDataList.add(new OrderTypeModel("工厂用工", 20, R.drawable.work_type21));
            mDataList.add(new OrderTypeModel("礼仪模特", 11, R.drawable.work_type17));
            mDataList.add(new OrderTypeModel("美妆美甲", 15, R.drawable.work_type18));
            mDataList.add(new OrderTypeModel("上门开锁", 29, R.drawable.work_type27));
            mDataList.add(new OrderTypeModel("下水道疏通", 30, R.drawable.work_type28));
            mDataList.add(new OrderTypeModel("充场", 31, R.drawable.work_type30));
        } else if (page==3){
            mDataList.add(new OrderTypeModel("果蔬粮油", 33, R.drawable.home_icon_gsly));
            mDataList.add(new OrderTypeModel("禽畜水产", 38, R.drawable.home_icon_qxsc));
            mDataList.add(new OrderTypeModel("茶叶交易", 32, R.drawable.home_icon_cyjy));
            mDataList.add(new OrderTypeModel("中药材交易", 41, R.drawable.home_icon_zycjy));
            mDataList.add(new OrderTypeModel("花卉苗木", 34, R.drawable.home_icon_hhmm));
            mDataList.add(new OrderTypeModel("林业产品", 35, R.drawable.home_icon_lycp));
            mDataList.add(new OrderTypeModel("土地承租", 39, R.drawable.home_icon_tdcd));
            mDataList.add(new OrderTypeModel("农用物资", 37, R.drawable.home_icon_nywz));
            mDataList.add(new OrderTypeModel("民宿", 36, R.drawable.home_icon_ms));
            mDataList.add(new OrderTypeModel("文化艺术", 40, R.drawable.home_iocn_whys));
        }


        mAdapter = new CommonAdapter<OrderTypeModel>(aty, R.layout.item_occupation_message, mDataList) {
            @Override
            protected void convert(ViewHolder viewHolder, final OrderTypeModel item, int position) {
                ImageView img = viewHolder.getView(R.id.img);
                TextView tv_type_name = viewHolder.getView(R.id.tv_type_name);
                LinearLayout lly_item = viewHolder.getView(R.id.lly_occupation);
                // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight / 2);
                // lly_item.setLayoutParams(layoutParams);
                tv_type_name.setText(item.getName());
                img.setImageResource(item.getImg_res());
                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isWork) {//接单
                            startActivity(new Intent(aty, ReceivingActivity.class).putExtra("work_type", item.getType()));
                        } else {
                            DemoUtils.TypeToActivity(aty, item.getType());
                        }
                    }
                });
            }
        };
        mBinding.rcBody.setLayoutManager(new GridLayoutManager(aty, 5) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        mBinding.rcBody.setAdapter(mAdapter);
    }
}
