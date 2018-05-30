package com.gsy.ml.ui.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityExerciseBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.ExerciseModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.ExercisePresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.common.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

public class ExerciseActivity extends BaseActivity implements ILoadPVListener {

    private ExercisePresenter mExercisePresenter = new ExercisePresenter(this);
    private ActivityExerciseBinding mBinding;
    private List<ExerciseModel.DataBean> mDataList = new ArrayList<>();
    private CommonAdapter<ExerciseModel.DataBean> mAdapter;

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("活动中心");
        mBinding.ilHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_exercise;
    }

    StateModel stateModel = new StateModel();

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityExerciseBinding) vdb;

        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setExpandRes("还没有新的活动呢!", R.drawable.no_data1_icon);
        mBinding.setStateModel(stateModel);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getMessage();
            }
        });
        getMessage();
        mAdapter = new CommonAdapter<ExerciseModel.DataBean>(aty, R.layout.item_exercise, mDataList) {
            @Override
            protected void convert(ViewHolder holder, final ExerciseModel.DataBean dataBean, final int position) {
                ImageView img = holder.getView(R.id.img);
                Glide.with(aty).load(dataBean.getEventDetails()).into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(dataBean.getActivityUrl())) {
                            startActivity(new Intent(aty, WebViewActivity.class)
                                    .putExtra("url", dataBean.getActivityUrl()));
                        }
                    }
                });
            }
        };
        mBinding.rcBody.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcBody.setAdapter(mAdapter);
    }

    private void getMessage() {
        mExercisePresenter.getActivity(MaiLiApplication.getInstance().getUserPlace().getCity());
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof ExerciseModel) {
            ExerciseModel info = (ExerciseModel) object;
            if (info.getData() != null && info.getData().size() > 0) {
                stateModel.setEmptyState(EmptyState.NORMAL);
                mDataList.clear();
                mDataList.addAll(info.getData());
                mAdapter.notifyDataSetChanged();
            } else {
                stateModel.setEmptyState(EmptyState.EXPAND);
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
