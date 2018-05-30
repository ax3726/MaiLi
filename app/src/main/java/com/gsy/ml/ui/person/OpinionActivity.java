package com.gsy.ml.ui.person;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityOpinionLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.model.person.OpinionModel;
import com.gsy.ml.prestener.person.OpinionPresenter;
import com.gsy.ml.ui.common.BaseActivity;

/**
 * Created by Administrator on 2017/5/13.
 */

public class OpinionActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityOpinionLayoutBinding mBinding;
    private OpinionPresenter presenter = new OpinionPresenter(this);
    private UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();

    @Override
    public int getLayoutId() {
        return R.layout.activity_opinion_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("意见反馈");
        mBinding.ilHead.tvRight.setText("提交");
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.llyRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityOpinionLayoutBinding) vdb;
        mBinding.etOpinion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lengths = mBinding.etOpinion.getText().length();
                mBinding.tvLengs.setText("" + lengths);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_right:
                checkData();
                break;
            case R.id.lly_left:
                finish();
                break;
        }
    }

    public void checkData() {
        String content = mBinding.etOpinion.getText().toString().trim();
        String phone = mBinding.tvPhones.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入您的意见!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入您的联系方式!", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        presenter.initContent(phone, userInfo.getName(), content);
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof OpinionModel) {
            OpinionModel models = (OpinionModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), models.getInfo(), Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }.start();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
