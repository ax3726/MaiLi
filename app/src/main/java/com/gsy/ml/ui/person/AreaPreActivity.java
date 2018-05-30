package com.gsy.ml.ui.person;

import android.text.TextPaint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityJobPreBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.WorkAreaModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.WorkAreaPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作局域预选
 */

public class AreaPreActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {

    private List<WorkAreaModel.ChinaListBean> mDataList = new ArrayList<>();
    private ActivityJobPreBinding mBinding;
    private WorkAreaPresenter mPresenter = new WorkAreaPresenter(this);
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_job_pre;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("区域预选");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private void getWorkArea() {

        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.selectWorkArea(userInfo.getPhone());
    }


    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityJobPreBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getWorkArea();
            }
        });
        mBinding.setStateModel(stateModel);
        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String work = "";
                for (TextView txt : mTxts) {
                    if (txt.isSelected()) {
                        String tx = txt.getText().toString().trim();
                        work = work + tx + ",";
                    }
                }
                saveWorkArea(work);
            }
        });
        getWorkArea();
    }

    public void saveWorkArea(String work) {
        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.saveWorkArea(userInfo.getPhone(), work);
    }


    private List<TextView> mTxts = new ArrayList<>();

    private void setLables() {
        mBinding.flyType.removeAllViews();
        mBinding.flyType.setGravity(Gravity.CENTER);
        for (int i = 0; i < mDataList.size(); i++) {
            View view = View.inflate(aty, R.layout.item_type_text, null);
            final TextView tv = (TextView) view.findViewById(R.id.txt);
            final WorkAreaModel.ChinaListBean orderTypeModel = mDataList.get(i);
            tv.setText(orderTypeModel.getFullname());
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
            if (orderTypeModel.is_select()) {
                tv.setSelected(true);
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (tv.isSelected()) {
                        tv.setSelected(false);
                    } else {
                        tv.setSelected(true);
                    }
                }
            });
            mBinding.flyType.addView(view);
            mTxts.add(tv);
        }
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
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            EventBus.getDefault().post(new UpdateNotice());
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (object instanceof WorkAreaModel) {
            WorkAreaModel info = (WorkAreaModel) object;
            stateModel.setEmptyState(EmptyState.NORMAL);
            initView(info);

        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    private void initView(WorkAreaModel info) {
        if (info == null) {
            return;
        }
        mDataList.clear();
        mDataList.addAll(info.getChinaList());
        if (info.getUserPreselectedPlace() != null) {
            String place = info.getUserPreselectedPlace().getPlace();
            if (!TextUtils.isEmpty(place)) {

                String[] split = place.split(",");
                for (String s : split) {
                    for (int i = 0; i < mDataList.size(); i++) {
                        if (mDataList.get(i).getFullname().equals(s)) {
                            mDataList.get(i).setIs_select(true);
                        }
                    }
                }
            }
        }
        setLables();

    }
}
