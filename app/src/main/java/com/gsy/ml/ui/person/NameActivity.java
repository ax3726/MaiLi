package com.gsy.ml.ui.person;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityNameBinding;
import com.gsy.ml.ui.common.BaseActivity;

/**
 * Created by Administrator on 2017/7/4.
 */

public class NameActivity extends BaseActivity implements View.OnClickListener {
    private ActivityNameBinding mBindinig;

    @Override
    public int getLayoutId() {
        return R.layout.activity_name;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBindinig.ilHead.tvTitle.setText("昵称");
        mBindinig.ilHead.llyLeft.setOnClickListener(this);

        mBindinig.ilHead.tvRight.setText("保存");
        mBindinig.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBindinig.ilHead.tvRight.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBindinig = (ActivityNameBinding) vdb;
        mBindinig.imgDelete.setOnClickListener(this);
        mBindinig.etName.setText(TextUtils.isEmpty(MaiLiApplication.getInstance().getUserInfo().getNickname()) ? "" : MaiLiApplication.getInstance().getUserInfo().getNickname());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.img_delete:
                mBindinig.etName.setText("");
                break;
            case R.id.tv_right:
                if (TextUtils.isEmpty(mBindinig.etName.getText().toString().trim())) {
                    Toast.makeText(MaiLiApplication.getInstance(), "名字不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mBindinig.etName.getText().toString().trim().length() > 6) {
                    Toast.makeText(aty, "昵称长度不能超过6个字！", Toast.LENGTH_SHORT).show();
                    return;
                }
                setResult(RESULT_OK, new Intent().putExtra("name", mBindinig.etName.getText().toString()));
                finish();
                break;
        }
    }
}
