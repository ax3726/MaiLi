package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.gsy.ml.R;
import com.gsy.ml.databinding.DialogCancelOrderBinding;
import com.gsy.ml.model.EventMessage.CancelReason;

/**
 * Created by Administrator on 2017/4/29.
 */

public class CancelOrderDialog extends Dialog {

    private Context mContext;
    private DialogCancelOrderBinding mBinding;
    private String mReason = "";
    private String mOrder = "";
    private int mType;
    private int mWorkType = 0;
    private int mPage = 0;//0 接单人取消 1 发单人取消

    public CancelOrderDialog(@NonNull Context context, String order, int type, int page) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        mOrder = order;
        mType = type;
        mPage = page;

    }

    public CancelOrderDialog(@NonNull Context context, String order, int type, int page, int workType) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        mOrder = order;
        mType = type;
        mPage = page;
        mWorkType = workType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_cancel_order, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.8);
        params.height = params.height;
        this.getWindow().setAttributes(params);
        initView();

    }


    private void initView() {
        if (mPage != 0) {
            mBinding.rbTxt1.setText("找人代做了");
            mBinding.rbTxt2.setText("不想发了");
            mBinding.rbTxt3.setText("发单信息错误");
            mBinding.rbTxt4.setText("其他原因");
            mBinding.tvHint.setText("注意: 接单途中取消订单会扣除15%的违约金哦~老板请慎重!");

        }

        mBinding.rgCheck.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {
                    case R.id.rb_txt1:
                        mReason = mBinding.rbTxt1.getText().toString().trim();
                        break;
                    case R.id.rb_txt2:
                        mReason = mBinding.rbTxt2.getText().toString().trim();
                        break;
                    case R.id.rb_txt3:
                        mReason = mBinding.rbTxt3.getText().toString().trim();
                        break;
                    case R.id.rb_txt4:
                        mReason = mBinding.rbTxt4.getText().toString().trim();
                        break;
                }
            }
        });
        mReason = mBinding.rbTxt1.getText().toString().trim();
        mBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancelListener!=null) {
                    mCancelListener.onCancel(new CancelReason(mOrder, mReason, mType, mWorkType));

                }


                dismiss();
            }
        });
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private CancelListener mCancelListener;


    public void setCancelListener(CancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public interface CancelListener {
        void onCancel(CancelReason cancelReason);
    }
}
