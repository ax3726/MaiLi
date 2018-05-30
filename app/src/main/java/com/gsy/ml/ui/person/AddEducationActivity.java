package com.gsy.ml.ui.person;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.Time;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityEducationLayoutBinding;
import com.gsy.ml.model.person.EducationModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 * 添加教育经历
 */

public class AddEducationActivity extends BaseActivity implements View.OnClickListener {
    private ActivityEducationLayoutBinding mBinding;
    private ChooseDatePopupWindow mChooseEducation;
    private ChooseDatePopupWindow mChooseSchoolTime;
    private Time mTime = new Time("GMT+8");

    @Override
    public int getLayoutId() {
        return R.layout.activity_education_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();

        mBinding.ilHead.tvTitle.setText("教育经历");
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private void initListener() {
        mBinding.llyEducation.setOnClickListener(this);
        mBinding.llySchoolTime.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        int figure = mBinding.etJingli.getText().length();
        mBinding.tvFigure.setText(figure + "");
        mBinding.etJingli.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int figure = mBinding.etJingli.getText().length();
                mBinding.tvFigure.setText(figure + "");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_education:
                mChooseEducation.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.btn_ok:
                checkData();
                break;
            case R.id.lly_school_time:
                mChooseSchoolTime.showPopupWindow(mBinding.getRoot());
                break;

        }
    }

    private void checkData() {
        String school = mBinding.etSchool.getText().toString().trim();
        String major = mBinding.etMajor.getText().toString().trim();
        String education = mBinding.tvEducation.getText().toString().trim();
        String start_time = mBinding.tvStartTime.getText().toString().trim();
        String end_time = mBinding.tvEndTime.getText().toString().trim();
        String jinglp = mBinding.etJingli.getText().toString().trim();
        if (TextUtils.isEmpty(school)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入学校名称!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(major)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入专业名称!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(education)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择学历!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(education)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择在校起始时间!", Toast.LENGTH_SHORT).show();
            return;
        }
        EventBus.getDefault().post(new EducationModel(school, major, education, start_time + "," + end_time, jinglp));
        finish();

    }

    private void initView(EducationModel educationModel) {
        if (educationModel == null) {
            return;
        }
        mBinding.etSchool.setText(TextUtils.isEmpty(educationModel.getName()) ? "" : educationModel.getName());
        mBinding.etMajor.setText(TextUtils.isEmpty(educationModel.getZhuanye()) ? "" : educationModel.getZhuanye());
        mBinding.tvEducation.setText(TextUtils.isEmpty(educationModel.getXueli()) ? "" : educationModel.getXueli());
        if (!TextUtils.isEmpty(educationModel.getTime())) {
            String[] split = educationModel.getTime().split(",");
            if (split.length == 2) {
                mBinding.tvStartTime.setText(split[0]);
                mBinding.tvEndTime.setText(split[1]);
            }
        }
        mBinding.etJingli.setText(TextUtils.isEmpty(educationModel.getContent()) ? "" : educationModel.getContent());
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityEducationLayoutBinding) vdb;
        EducationModel educationModel = (EducationModel) getIntent().getSerializableExtra("data");
        initView(educationModel);
        initListener();
        mChooseEducation = new ChooseDatePopupWindow(aty, 1);
        mChooseEducation.setTitle("选择学历");
        List<String> data = new ArrayList<>();
        data.add("大专");
        data.add("本科");
        data.add("硕士");
        data.add("博士");
        mChooseEducation.setData1(data);
        mChooseEducation.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                mBinding.tvEducation.setText(item1);
            }
        });
        mChooseSchoolTime = new ChooseDatePopupWindow(aty, 2);
        mChooseSchoolTime.setTitle("选择时间段");
        List<String> data1 = new ArrayList<>();
        mTime.setToNow();
        // 获取当前年份
        for (int i = mTime.year; i >= 1978; i--) {
            data1.add(i + "年");
        }
        mChooseSchoolTime.setData1(data1);
        mChooseSchoolTime.setData2(data1);
        mChooseSchoolTime.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                if (position1 >= position2) {
                    mBinding.tvStartTime.setText(item1);
                    mBinding.tvEndTime.setText(item2);
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "开始时间不能大于结束时间!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
