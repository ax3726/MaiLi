package com.gsy.ml.ui.home;

import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityChooseOccupationLayoutBinding;
import com.gsy.ml.model.main.OrderTypeModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;


/**
 * Created by Administrator on 2017/6/6.
 */

public class ChooseOccupationActivity extends BaseActivity {
    private ActivityChooseOccupationLayoutBinding mBinding;
    private List<OrderTypeModel> mDataList = new ArrayList<>();
    private CommonAdapter<OrderTypeModel> mAdapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_occupation_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("选择工种");
        mBinding.ilHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityChooseOccupationLayoutBinding) vdb;
        initMessage();
        mAdapter = new CommonAdapter<OrderTypeModel>(aty, R.layout.item_occupation_message, mDataList) {
            @Override
            protected void convert(ViewHolder holder, OrderTypeModel model, int position) {
                ImageView img = holder.getView(R.id.img);
                TextView tv_type_name = holder.getView(R.id.tv_type_name);
                LinearLayout linearLayout = holder.getView(R.id.lly_occupation);
                img.setImageResource(model.getImg_res());
                tv_type_name.setText(model.getName());
                final OrderTypeModel orderTypeModel = mDataList.get(position);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DemoUtils.TypeToActivity(aty, orderTypeModel.getType());
                        finish();
                    }
                });
            }
        };
        mBinding.rcOccupation.setLayoutManager(new GridLayoutManager(aty, 5));
        mBinding.rcOccupation.setAdapter(mAdapter);

    }

    public void initMessage() {
        mDataList.add(new OrderTypeModel("同城配送", 1, R.drawable.work_type1));
        mDataList.add(new OrderTypeModel("课程家教", 2, R.drawable.work_type2));
        mDataList.add(new OrderTypeModel("艺术家教", 3, R.drawable.work_type3));
        mDataList.add(new OrderTypeModel("钟点工", 4, R.drawable.work_type4));
        mDataList.add(new OrderTypeModel("保洁", 23, R.drawable.work_type5));
        mDataList.add(new OrderTypeModel("保姆", 6, R.drawable.work_type6));
        mDataList.add(new OrderTypeModel("搬运工", 21, R.drawable.work_type7));
        mDataList.add(new OrderTypeModel("电器清洗", 17, R.drawable.work_type8));
        mDataList.add(new OrderTypeModel("护工", 5, R.drawable.work_type9));
        mDataList.add(new OrderTypeModel("导游", 9, R.drawable.work_type10));
        mDataList.add(new OrderTypeModel("运动陪练", 12, R.drawable.work_type11));
        mDataList.add(new OrderTypeModel("车辆服务", 14, R.drawable.work_type12));
        mDataList.add(new OrderTypeModel("传单派发", 8, R.drawable.work_type13));
        mDataList.add(new OrderTypeModel("促销导购", 22, R.drawable.work_type14));
        mDataList.add(new OrderTypeModel("电脑维修", 18, R.drawable.work_type15));
        mDataList.add(new OrderTypeModel("服务员", 10, R.drawable.work_type16));
        mDataList.add(new OrderTypeModel("礼仪模特", 11, R.drawable.work_type17));
        mDataList.add(new OrderTypeModel("美妆美甲", 15, R.drawable.work_type18));
        //mDataList.add(new OrderTypeModel("代驾",13));
        mDataList.add(new OrderTypeModel("宠物服务", 16, R.drawable.work_type19));
        mDataList.add(new OrderTypeModel("技工", 19, R.drawable.work_type20));
        mDataList.add(new OrderTypeModel("工厂用工", 20, R.drawable.work_type21));

    }

}
