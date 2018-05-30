package com.gsy.ml.ui.person;

import android.content.Intent;
import android.net.Uri;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityCheckLayoutBinding;
import com.gsy.ml.model.EventMessage.EvakuateUpdate;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.EducationModel;
import com.gsy.ml.model.person.ResumeModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.ConfirOrderPresenter;
import com.gsy.ml.prestener.person.ResumePresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.PhotoPreviewActivity;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DemoUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 * 查看简历
 */

public class CheckActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityCheckLayoutBinding mBinding;
    private ResumePresenter rPresenter = new ResumePresenter(this);
    private EducationModel mEducationModel = new EducationModel("", "", "", "", "");
    private String mOrders = "";
    private ConfirOrderPresenter mConfirOrderPresenter = new ConfirOrderPresenter(this);
    private int mType = 0;
    private StateModel stateModel = new StateModel();
    private ResumeModel.DataBean data;

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_layout;
    }

    private String mPhone = "";
    private boolean mIsShow = false;
    private boolean mIsPhone = false;
    private String mName = "";
    private String mId;

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("查看简历");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        mBinding.tvOk.setOnClickListener(this);
        mBinding.tvPhone.setOnClickListener(this);
        mBinding.imgHomeHead.setOnClickListener(this);
    }


    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityCheckLayoutBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                initInformation();
            }
        });
        mBinding.setStateModel(stateModel);
        mOrders = getIntent().getStringExtra("order");
        mPhone = getIntent().getStringExtra("phone");
        mType = getIntent().getIntExtra("type", 0);
        mIsShow = getIntent().getBooleanExtra("isshow", false);
        mIsPhone = getIntent().getBooleanExtra("isphone", false);
        mName = getIntent().getStringExtra("name");
        mId = getIntent().getStringExtra("id");
        mBinding.llyButtom.setVisibility(mIsShow ? View.VISIBLE : View.GONE);
        mBinding.tvPhone.setVisibility(mType == 1 ? View.VISIBLE : View.GONE);
        initInformation();
    }

    public void initInformation() {
        rPresenter.updataInformation(mPhone);    ////------------
    }

    List<TextView> mTxts = new ArrayList<>();
    private String head_url = "";

    public void setResume(ResumeModel info) {
        if (info == null || info.getData() == null) {
            return;
        }
        data = info.getData();

        switch (data.getUserInfo().getStar()) {
            case 1:
                mBinding.imgStar.setImageResource(R.drawable.stars_one_icon);
                break;
            case 2:
                mBinding.imgStar.setImageResource(R.drawable.stars_two_icon);
                break;
            case 3:
                mBinding.imgStar.setImageResource(R.drawable.stars_three_icon);
                break;
            case 4:
                mBinding.imgStar.setImageResource(R.drawable.stars_four_icon);
                break;
            case 5:
                mBinding.imgStar.setImageResource(R.drawable.stars_five_icon);
                break;
        }

        Glide.with(MaiLiApplication.appliactionContext).load(data.getUserInfo().getHeadUrl()).asBitmap()
                .placeholder(R.drawable.home_head_img_icon).into(mBinding.imgHomeHead);
        mBinding.tvName.setText(TextUtils.isEmpty(info.getData().getUserInfo().getNickname()) ? "未知" : info.getData().getUserInfo().getNickname());    //名字
        mBinding.tvPhone.setText(info.getData().getUserInfo().getPhone());   //电话
        mBinding.tvSex.setText(info.getData().getUserInfo().getSex());       //男

        String jobs = data.getUserJob();
        int lenth = TextUtils.isEmpty(jobs) ? 0 : jobs.split(",").length;
        mBinding.flyType.removeAllViews();
        head_url = data.getUserInfo().getHeadUrl();
        if (lenth > 0) {
            String[] split = jobs.split(",");
            for (int i = 0; i < lenth; i++) {
                View view = View.inflate(aty, R.layout.item_check, null);
                final TextView tv = (TextView) view.findViewById(R.id.txt);
                tv.setText(DemoUtils.TypeToOccupation(Integer.valueOf(split[i])));
                TextPaint tp = tv.getPaint();
               // tp.setFakeBoldText(true);
                mBinding.flyType.addView(view);
                mTxts.add(tv);
            }
        } else {
            mBinding.llyOccupation.setVisibility(View.GONE);
        }

        String name = TextUtils.isEmpty(data.getUserInfo().getGraduateSchool()) ? "" : data.getUserInfo().getGraduateSchool();
        String xueli = TextUtils.isEmpty(data.getUserInfo().getQualifications()) ? "" : data.getUserInfo().getQualifications();
        String zhuanye = TextUtils.isEmpty(data.getUserInfo().getMajor()) ? "" : data.getUserInfo().getMajor();
        String time = TextUtils.isEmpty(data.getUserInfo().getSchoolTime()) ? "" : data.getUserInfo().getSchoolTime().replace(",", "-");
        String contents = TextUtils.isEmpty(data.getUserInfo().getSchoolLife()) ? "" : data.getUserInfo().getSchoolLife();
        mEducationModel.setContent(contents);
        mEducationModel.setName(name);
        mEducationModel.setZhuanye(zhuanye);
        mEducationModel.setXueli(xueli);
        mEducationModel.setTime(data.getUserInfo().getSchoolTime());
        contents = TextUtils.isEmpty(contents) ? "" : "\n学校经历:" + contents;
        if (!contents.equals("")) {
            mBinding.tvEducate.setText(name + "\t\t" + time + "\t\t" + zhuanye + "\t\t" + xueli + contents);  //教育经历
        } else {
            mBinding.llyEducate.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(data.getUserInfo().getWorkExp())) {
            mBinding.tvWorkEx.setText(data.getUserInfo().getWorkExp());  //工作经历
        } else {
            mBinding.llyWorkEx.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_phone:
                String phone = mBinding.tvPhone.getText().toString().trim();
                call(phone);
                break;
            case R.id.img_home_head:
                if (!TextUtils.isEmpty(head_url)) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class)
                            .putExtra("img", head_url));
                }
                break;
            case R.id.tv_ok:
                showWaitDialog();
                mConfirOrderPresenter.confirOrder(mId, mOrders, mPhone, mName);
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
        } else if (object instanceof ResumeModel) {
            ResumeModel info = (ResumeModel) object;
            stateModel.setEmptyState(EmptyState.NORMAL);
            setResume(info);
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new EvakuateUpdate("跳转", 1));
            EventBus.getDefault().post(new EvakuateUpdate("", 2));
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
