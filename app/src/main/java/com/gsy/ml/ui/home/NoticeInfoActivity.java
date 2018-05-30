package com.gsy.ml.ui.home;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityNoticeInfoBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.MessageInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.MessagePresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;

import org.greenrobot.eventbus.EventBus;

import ml.gsy.com.library.utils.Utils;

public class NoticeInfoActivity extends BaseActivity implements ILoadPVListener {

    private ActivityNoticeInfoBinding mBinding;
    private MessagePresenter mPresenter = new MessagePresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_notice_info;
    }
    private StateModel stateModel = new StateModel();
    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.inHead.tvTitle.setText("通知详情");
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String mId = "";

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityNoticeInfoBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                mPresenter.selectMessageInfo(mId);
            }
        });
        mBinding.setStateModel(stateModel);
        mId = getIntent().getStringExtra("id");
        mPresenter.selectMessageInfo(mId);
        EventBus.getDefault().post(new UpdateNotice());
    }

    private void initView(MessageInfoModel info) {
        if (info == null) {
            return;
        }
        MessageInfoModel.DataBean data = info.getData();
        mBinding.tvTitle.setText(TextUtils.isEmpty(data.getTitle()) ? "这是一个空标题" : data.getTitle());
        mBinding.tvContent.setText(TextUtils.isEmpty(data.getContent()) ? "" : data.getContent());
        mBinding.tvTime.setText("发布时间:" + Utils.getDateToString(data.getCreateTime(), "yyyy年MM月dd日 HH:mm"));
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {

        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState()==EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof MessageInfoModel) {
            MessageInfoModel info = (MessageInfoModel) object;
            initView(info);
            stateModel.setEmptyState(EmptyState.NORMAL);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
