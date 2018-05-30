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
import com.gsy.ml.model.main.OrderTypeModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.WorkJobPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 职位预选
 */

public class JobPreActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {

    private List<OrderTypeModel> mDataList = new ArrayList<>();
    private ActivityJobPreBinding mBinding;
    private WorkJobPresenter mPresenter = new WorkJobPresenter(this);
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_job_pre;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("职位预选");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private void getWorkJob() {

        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.selectWorkJob(userInfo.getPhone());
    }

    private String orders = "";

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityJobPreBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getWorkJob();
            }
        });
        mBinding.setStateModel(stateModel);
        orders = getIntent().getStringExtra("orders");

        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String work = "";
                for (TextView txt : mTxts) {
                    if (txt.isSelected()) {
                        int i = (int) txt.getTag();
                        work = work + i + ",";
                    }
                }
                saveWorkJob(work);
            }
        });
        getWorkJob();
    }

    public void saveWorkJob(String work) {
        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.saveWorkJob(userInfo.getPhone(), work);
    }

    private void initView() {
        mDataList.clear();
        for (int i = 1; i <= 41; i++) {
            if (i != 13) {
                mDataList.add(new OrderTypeModel(DemoUtils.TypeToOccupation(i), i));
            }
        }
    }

    private List<TextView> mTxts = new ArrayList<>();

    private void setLables() {
        String[] split = null;
        if (!TextUtils.isEmpty(orders)) {
            split = orders.split(",");
        }
        mBinding.flyType.removeAllViews();
        mBinding.flyType.setGravity(Gravity.CENTER);
        for (int i = 0; i < mDataList.size(); i++) {
            View view = View.inflate(aty, R.layout.item_type_text, null);
            final TextView tv = (TextView) view.findViewById(R.id.txt);
            final OrderTypeModel orderTypeModel = mDataList.get(i);
            tv.setText(orderTypeModel.getName());
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
            tv.setTag(orderTypeModel.getType());
            if (split != null) {
                for (String s : split) {
                    if (s.equals(orderTypeModel.getType() + "")) {
                        tv.setSelected(true);
                    }
                }
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
            stateModel.setEmptyState(EmptyState.NORMAL);
            if (parms.length > 0) {
                int parm = parms[0];
                if (parm == 1) {//查询
                    orders = info.getData();
                    initView();
                    setLables();
                } else if (parm == 2) {//更新
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
                }
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
