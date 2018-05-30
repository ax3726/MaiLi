package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.databinding.DialogModificationOrderBinding;

/**
 * Created by Administrator on 2017/9/1.
 */

public class ModificationOrderDialog extends Dialog implements View.OnClickListener {
    private final Context mContext;
    private DialogModificationOrderBinding mBinding;
    private Double number;
    Double preserve_money; //单价
    Double preserve_totalMoney; //总价
    int preserve_people; //人数

    public ModificationOrderDialog(Context context) {
        super(context, R.style.DialogBaseStyle);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);//是否可以取消、
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.dialog_modification_order, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.8);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.getWindow().setAttributes(params);
        mBinding.tvAffirm.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        mBinding.etNumber.setText(preserve_money + "");
        mBinding.etNumber.addTextChangedListener(textWatcher);
        mBinding.tvWorkTotalCost.setText(preserve_totalMoney + "");
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                number = new Double(TextUtils.isEmpty(mBinding.etNumber.getText().toString().trim()) ? "0" : mBinding.etNumber.getText().toString().trim());
                mBinding.tvWorkTotalCost.setText(number * preserve_people + "");
            } catch (Exception ex) {
                number=new Double(0);
            }
        }
    };

    public void setMoneyAndPeople(Double money, Double totalMoney, int people) {
        preserve_money = money;
        number = money;
        preserve_totalMoney = totalMoney;
        preserve_people = people;
    }

    public void setMoneyAndPeople(Double money, Double totalMoney) {
        preserve_money = money;
        number = money;
        preserve_totalMoney = totalMoney;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_affirm:
                if (number!=null&&number < 20) {
                    Toast.makeText(mContext, "修改价格不能低于20！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mModificationOrderListener != null) {
                        mModificationOrderListener.setType(0, number, number * preserve_people);
                    }
                    dismiss();
                }
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
        }
    }


    private ModificationOrderListener mModificationOrderListener;

    public void setModificationOrderListener(ModificationOrderListener mModificationOrderListener) {
        this.mModificationOrderListener = mModificationOrderListener;
    }

    public interface ModificationOrderListener {
        void setType(int type, double cast, double totalcast);
    }
}
