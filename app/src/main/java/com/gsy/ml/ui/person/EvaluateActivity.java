package com.gsy.ml.ui.person;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityEvaluateLayoutBinding;
import com.gsy.ml.model.EventMessage.EvakuateUpdate;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.EvaluatePresenter;
import com.gsy.ml.ui.common.BaseActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/4/29.
 */

public class EvaluateActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityEvaluateLayoutBinding mBinding;
    private EvaluatePresenter mPresenter = new EvaluatePresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_evaluate_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("发表评价");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.tvRight.setText("发布");
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.tvRight.setTextColor(getResources().getColor(R.color.colorFF6C00));
        mBinding.ilHead.llyMore.setBackgroundResource(R.color.colorFFFFFF);
        mBinding.ilHead.tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkData();
            }
        });
    }

    private void checkData() {
        //评价类型 ： 0未评价 1 人工好评   2人工中评  3人工差评  4 系统好评
        int level = 1;
        if (mBinding.rbFull.isChecked()) {//好评
            level = 1;
        } else if (mBinding.rbDehiscence.isChecked()) {//中评
            level = 2;
        } else if (mBinding.rbBroken.isChecked()) {//差评
            level = 3;
        }
        String content = mBinding.etEvaluate.getText().toString().trim();
        showWaitDialog();
        mPresenter.addEvaluate(mOrder, MaiLiApplication.getInstance().getUserInfo().getPhone(), level + "", content, mType + "");
    }

    private String mOrder = "";
    private int mType = 1;//type   1是接单人对发单人评价，2是发单人对接单人的评价
    private int mPage = 1;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityEvaluateLayoutBinding) vdb;
        mOrder = getIntent().getStringExtra("order");
        mType = getIntent().getIntExtra("type", 1);
        mPage = getIntent().getIntExtra("page", 1);
        mBinding.rbBroken.setOnClickListener(this);
        mBinding.rbDehiscence.setOnClickListener(this);
        mBinding.rbFull.setOnClickListener(this);
        mBinding.etEvaluate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int lengths = mBinding.etEvaluate.getText().length();
                mBinding.tvLengs.setText("" + lengths);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.rb_broken:
                mBinding.rbBroken.setChecked(true);
                mBinding.rbFull.setChecked(false);
                mBinding.rbDehiscence.setChecked(false);
                mBinding.rbBroken.setTextColor(getResources().getColor(R.color.colorffc000));
                mBinding.rbFull.setTextColor(getResources().getColor(R.color.colorafafaf));
                mBinding.rbDehiscence.setTextColor(getResources().getColor(R.color.colorafafaf));
                break;
            case R.id.rb_dehiscence:
                mBinding.rbBroken.setChecked(false);
                mBinding.rbFull.setChecked(false);
                mBinding.rbDehiscence.setChecked(true);
                mBinding.rbBroken.setTextColor(getResources().getColor(R.color.colorafafaf));
                mBinding.rbFull.setTextColor(getResources().getColor(R.color.colorafafaf));
                mBinding.rbDehiscence.setTextColor(getResources().getColor(R.color.colorffc000));
                break;
            case R.id.rb_full:
                mBinding.rbBroken.setChecked(false);
                mBinding.rbFull.setChecked(true);
                mBinding.rbDehiscence.setChecked(false);
                mBinding.rbBroken.setTextColor(getResources().getColor(R.color.colorafafaf));
                mBinding.rbFull.setTextColor(getResources().getColor(R.color.colorffc000));
                mBinding.rbDehiscence.setTextColor(getResources().getColor(R.color.colorafafaf));
                break;

        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new EvakuateUpdate("", mPage));
            finish();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
