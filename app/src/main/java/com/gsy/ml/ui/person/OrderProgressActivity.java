package com.gsy.ml.ui.person;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityOrderProgressBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.SelectProgressModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.SelectProgressPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.SocaketUtils;

import ml.gsy.com.library.utils.Utils;

public class OrderProgressActivity extends BaseActivity implements ILoadPVListener {

    private ActivityOrderProgressBinding mBinding;
    private SelectProgressPresenter mPresenter = new SelectProgressPresenter(this);

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.inHead.tvTitle.setText("订单进度");
    }

    private String mOrder = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_progress;
    }

    private StateModel stateModel = new StateModel();


    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityOrderProgressBinding) vdb;
        SocaketUtils.getInstance().startConnection();
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getProgress();
            }
        });
        mBinding.setStateModel(stateModel);
        mOrder = getIntent().getStringExtra("order");
        getProgress();
        mBinding.llyLocationInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(aty, LocationInfoActivity.class);
                intent.putExtra("data", mSelectProgressModel);
                startActivity(intent);
            }
        });
        mBinding.tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(mBinding.tvPhone.getText().toString().trim());
            }
        });
        mBinding.tvResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aty, CheckActivity.class).
                                putExtra("phone", mBinding.tvPhone.getText().toString().trim())
                                .putExtra("isshow", false)
                                .putExtra("type", 1)
                           );

            }
        });
    }

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void getProgress() {
        mPresenter.selectProgress(mOrder);
    }

    private SelectProgressModel mSelectProgressModel = null;

    private void initView(SelectProgressModel model) {
        if (model == null) {
            return;
        }
        mBinding.tvTxt1.setText("支付成功，" + model.getData().getWorkPlace() + "同城配送");
        mBinding.tvTime1.setText(fromatToTime(model.getData().getSendtTime()));
        mBinding.tvTime2.setText(fromatToTime(model.getData().getSendtTime()));
        mBinding.tvTime3.setText(fromatToTime(model.getData().getAcceptTime()));

        Glide.with(aty.getApplicationContext()).load(model.getData().getAcceptHeadUrl())
                .asBitmap().placeholder(R.drawable.progress_pic).into(mBinding.imgHead);

        switch (model.getData().getAcceptStar()) {
            case 1:
                mBinding.tvStar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stars_one_icon, 0);
                break;
            case 2:
                mBinding.tvStar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stars_two_icon, 0);
                break;
            case 3:
                mBinding.tvStar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stars_three_icon, 0);
                break;
            case 4:
                mBinding.tvStar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stars_four_icon, 0);
                break;
            case 5:
                mBinding.tvStar.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.stars_five_icon, 0);
                break;
        }

        mBinding.tvPhone.setText(model.getData().getAcceptPhone());
        mBinding.tvName.setText(TextUtils.isEmpty(model.getData().getAcceptName()) ? "未知" : model.getData().getAcceptName());
        mBinding.tvPickTime.setText("预计取件时间: " + fromatToTime(model.getData().getPickupTime()));
        if (model.getData().getOrderStatus() == 0) {//未取
            mBinding.llyQu.setVisibility(View.GONE);
            mBinding.llyFinish.setVisibility(View.GONE);
        } else {//已取货
            mBinding.llyQu.setVisibility(View.VISIBLE);
            if (model.getData().getFinishTime() > 0) {//完成单了
                mBinding.tvTime4.setText(fromatToTime(model.getData().getPickupImgTime()));
                mBinding.llyFinish.setVisibility(View.VISIBLE);
                mBinding.tvTime5.setText(fromatToTime(model.getData().getFinishTime()));
            } else {

                mBinding.llyFinish.setVisibility(View.GONE);
            }
            String con = "";
            String[] code = model.getData().getPhoneCode().split(",");
            String[] name = model.getData().getReceivedName().split(",");
            String[] time = model.getData().getServiceTimeout().split(",");
            for (int i = 0; i < code.length; i++) {
                if (i == code.length - 1) {
                    con = con + name[i] + "&nbsp;&nbsp;" + "<font color= '#8A8A8A'>验证码:</font>" + code[i]
                            + "&nbsp;&nbsp;<font color= '#8A8A8A'>预计送达时间:</font><font color= '#ffe187'>" + fromatToTime(Long.valueOf(time[i])) + "</font>";
                } else {
                    con = con + name[i] + "&nbsp;&nbsp;" + "<font color= '#8A8A8A'>验证码:</font>" + code[i]
                            + "&nbsp;&nbsp;<font color= '#8A8A8A'>预计送达时间:</font><font color= '#ffe187'>" + fromatToTime(Long.valueOf(time[i])) + "</font>" + "<br/>";
                }
            }
            mBinding.tvQuContent.setText(Html.fromHtml(con));
        }
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    private String fromatToTime(long time) {
        if (time > 0) {
            if (Utils.isOneDay(time)) {
                return Utils.getDateToString(time, "HH:mm");
            } else {
                return Utils.getTimeStyle22(time);
            }
        } else {
            return "";
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
        } else if (object instanceof SelectProgressModel) {
            mSelectProgressModel = (SelectProgressModel) object;
            //  mBinding.llyBody.setVisibility(View.VISIBLE);
            stateModel.setEmptyState(EmptyState.NORMAL);

            initView(mSelectProgressModel);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    protected void onDestroy() {
        SocaketUtils.getInstance().CloseScoket();
        super.onDestroy();
    }
}
