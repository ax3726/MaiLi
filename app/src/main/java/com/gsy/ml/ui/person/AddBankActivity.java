package com.gsy.ml.ui.person;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityAddBankLayoutBinding;
import com.gsy.ml.model.common.AddBankModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DataUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/5/31.
 */

public class AddBankActivity extends BaseActivity implements View.OnClickListener {
    private ActivityAddBankLayoutBinding mBinding;

    private String name;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add_bank_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("添加银行卡");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityAddBankLayoutBinding) vdb;
        name = MaiLiApplication.getInstance().getUserInfo().getName();
        mBinding.etName.setHint(name);
        mBinding.btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBank();
            }
        });
    }

    public void addBank() {

        String num = mBinding.etBank.getTextWithoutSpace();

        if (TextUtils.isEmpty(num)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入银行卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        /*String jiequ = num.substring(0, 6);
        char[] banks = jiequ.toCharArray();
        String bankName = BankUtils.getNameOfBank(banks, 0);//获取银行卡的信息*//*
        Toast.makeText(aty, bankName, Toast.LENGTH_SHORT).show();
*/
        if (!DataUtils.checkBankCard(num)) {
            Toast.makeText(MaiLiApplication.getInstance(), "银行卡号不正确!", Toast.LENGTH_SHORT).show();
            return;
        }
        AddBankModel addBankModel = new AddBankModel();
        addBankModel.setName(name);
        addBankModel.setBank("招商银行");
        addBankModel.setNum(num);
        addBankModel.setBankType("3");
        EventBus.getDefault().post(addBankModel);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.btn_ok:
                finish();
                break;
        }
    }
}
