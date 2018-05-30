package com.gsy.ml.ui.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityMessageLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.MessageModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.MessagePresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.home.NoticeInfoActivity;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/21.
 * 站内信息类
 */

public class MessageActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityMessageLayoutBinding mBinding;
    private MessagePresenter mPresenter = new MessagePresenter(this);
    private int mPage = 1;
    private int mPageSize = 20;
    private List<MessageModel.DataBean> mDataList = new ArrayList<>();
    private CommonAdapter<MessageModel.DataBean> mAdapter;
    StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_message_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("通知");
        mBinding.ilHead.llyLeft.setOnClickListener(this);

    }

    private void getMessage() {
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.selectMessage(userInfo.getPhone(), mPage + "", mPageSize + "");
    }

 private Handler mHandler=  new Handler();
    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityMessageLayoutBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setExpandRes("暂无系统通知!", R.drawable.no_data1_icon);
        mBinding.setStateModel(stateModel);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getMessage();
            }
        });
        mAdapter = new CommonAdapter<MessageModel.DataBean>(aty, R.layout.item_message, mDataList) {
            @Override
            protected void convert(ViewHolder holder, final MessageModel.DataBean dataBean, final int position) {
                ImageView img = holder.getView(R.id.img);
                TextView tv_title = holder.getView(R.id.tv_title);
                View view_notice = holder.getView(R.id.view_notice);
                TextView tv_time = holder.getView(R.id.tv_time);
                TextView tv_content = holder.getView(R.id.tv_content);
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                if (dataBean.getReadStatus() == 0) {
                    view_notice.setVisibility(View.VISIBLE);
                } else {
                    view_notice.setVisibility(View.GONE);
                }
                if (dataBean.getImgres() != 0) {
                    img.setImageResource(dataBean.getImgres());
                } else {
                    img.setImageResource(R.drawable.notice1);
                }
                tv_content.setText(TextUtils.isEmpty(dataBean.getContent()) ? "没有内容!" : dataBean.getContent());
                tv_title.setText(TextUtils.isEmpty(dataBean.getTitle()) ? "这是一个空标题" : dataBean.getTitle());
                tv_time.setText(Utils.getTimeStyle2(dataBean.getCreateTime()));
                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDataList.get(position).setReadStatus(1);
                        mAdapter.notifyDataSetChanged();
                        startActivity(new Intent(aty, NoticeInfoActivity.class).putExtra("id", dataBean.getId() + ""));
                    }
                });
            }
        };
        mBinding.rcBody.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcBody.setAdapter(mAdapter);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        getMessage();
                    }
                },1500);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                mPage++;
                getMessage();
                super.onRefreshLoadMore(materialRefreshLayout);
            }
        });

        getMessage();
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

    private void stopRefresh() {
        mBinding.mrlBody.finishRefresh();
        mBinding.mrlBody.finishRefreshLoadMore();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {

        stopRefresh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof MessageModel) {
            MessageModel info = (MessageModel) object;
            if (mPage == 1) {
                mDataList.clear();
                if (info == null || info.getData().size() == 0) {
                    stateModel.setEmptyState(EmptyState.EXPAND);
                } else {
                    stateModel.setEmptyState(EmptyState.NORMAL);
                }
            }

            if (info != null && info.getData() != null) {

                if (info.getData().size() < mPageSize) {
                    mBinding.mrlBody.setLoadMore(false);
                } else {
                    mBinding.mrlBody.setLoadMore(true);
                }
                for (int i = 0; i < info.getData().size(); i++) {
                    int max = 4;
                    int min = 1;
                    Random random = new Random();
                    int s = random.nextInt(max) % (max - min + 1) + min;
                    switch (s) {
                        case 1:
                            info.getData().get(i).setImgres(R.drawable.notice1);
                            break;
                        case 2:
                            info.getData().get(i).setImgres(R.drawable.notice2);
                            break;
                        case 3:
                            info.getData().get(i).setImgres(R.drawable.notice3);
                            break;
                        case 4:
                            info.getData().get(i).setImgres(R.drawable.notice4);
                            break;
                    }
                }
                mDataList.addAll(info.getData());
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
