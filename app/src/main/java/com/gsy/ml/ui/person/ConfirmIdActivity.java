package com.gsy.ml.ui.person;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityConfirmIdBinding;
import com.gsy.ml.model.person.CardIdModel;
import com.gsy.ml.ui.common.BaseActivity;

public class ConfirmIdActivity extends BaseActivity {

    private ActivityConfirmIdBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_id;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.inHead.tvTitle.setText("身份验证");
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private CardIdModel mCardIdModel = null;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityConfirmIdBinding) vdb;
        mCardIdModel = (CardIdModel) getIntent().getSerializableExtra("data");
        mBinding.etName.addTextChangedListener(mTextWatcher);
        mBinding.etId.addTextChangedListener(mTextWatcher);
        mBinding.tvTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.tvTurn.isSelected()) {
                    checkData();
                }
            }
        });
        mBinding.tvReStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aty,IdentityImgActivity.class));
                finish();
            }
        });
    }

    private void checkData() {
        if (mCardIdModel == null)
            return;
        String name = mBinding.etName.getText().toString().trim();
        String card_id = mBinding.etId.getText().toString().trim();
        if (!name.equals(mCardIdModel.getName())) {
            Toast.makeText(MaiLiApplication.getInstance(), "身份证信息不匹配!", Toast.LENGTH_SHORT).show();
            mBinding.tvReStart.setVisibility(View.VISIBLE);
            return;
        }
        if (!card_id.equals(mCardIdModel.getIdNum())) {
            mBinding.tvReStart.setVisibility(View.VISIBLE);
            Toast.makeText(MaiLiApplication.getInstance(), "身份证信息不匹配!", Toast.LENGTH_SHORT).show();
            return;
        }

        startActivity(new Intent(aty,FaceActivity.class).putExtra("data", mCardIdModel));
        finish();


    }

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            checkEmpty();
        }
    };

    private void checkEmpty() {
        String name = mBinding.etName.getText().toString().trim();
        String id = mBinding.etId.getText().toString().trim();
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(id)) {
            mBinding.tvTurn.setSelected(true);
        } else {
            mBinding.tvTurn.setSelected(false);
        }
    }

}
