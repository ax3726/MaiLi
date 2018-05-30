package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.DialogOccupationBinding;

/**
 * Created by Administrator on 2017/4/12.
 */

public class OccupationDialog extends Dialog implements View.OnClickListener {
    private Context mContext;

    DialogOccupationBinding mBinding;
    private int type;//1 学生  2 家庭主妇

    public OccupationDialog(@NonNull Context context, int type) {
        super(context, R.style.DialogBaseStyle);
        mContext = context;
        this.type = type;
    }

    private IDialogClickListener mIDialogClickListener;

    public void setIDialogClickListener(IDialogClickListener mIDialogClickListener) {
        this.mIDialogClickListener = mIDialogClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.dialog_occupation, null, false);
        this.setContentView(mBinding.getRoot());
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.width = (int) ((d.getWidth()) * 0.9);
        params.height = params.height;
        this.getWindow().setAttributes(params);
        mBinding.btnCancel.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        mBinding.llyStu.setOnClickListener(this);
        mBinding.llyWoman.setOnClickListener(this);
        mBinding.llyGoing.setOnClickListener(this);
        mBinding.llyFree.setOnClickListener(this);
//        if (MaiLiApplication.getInstance().getUser().getUserInfo().getIdType().equals("在校学生")) {
//
//        }
        if (MaiLiApplication.getInstance().getUser().getUserInfo().getIdType() == null) {
            type = 1;
        } else {
            switch (MaiLiApplication.getInstance().getUser().getUserInfo().getIdType()) {
                case "在校学生":
                    type = 1;
                    break;
                case "家庭主妇":
                    type = 2;
                    break;
                case "上班族":
                    type = 3;
                    break;
                case "自由职业":
                    type = 4;
                    break;
            }
        }
        setSelect(type);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel://关闭
                if (mIDialogClickListener != null) {
                    mIDialogClickListener.onCancel();
                }
                dismiss();
                break;
            case R.id.btn_ok://确定
                if (mIDialogClickListener != null) {
                    mIDialogClickListener.onOk(type);
                }
                dismiss();
                break;
            case R.id.lly_stu:
                setSelect(1);
                break;
            case R.id.lly_woman:
                setSelect(2);
                break;
            case R.id.lly_going:
                setSelect(3);
                break;
            case R.id.lly_free:
                setSelect(4);
                break;
        }
    }

    private void setSelect(int type) {
        switch (type) {
            case 1:
                mBinding.llyStu.setSelected(true);
                mBinding.llyWoman.setSelected(false);
                mBinding.llyGoing.setSelected(false);
                mBinding.llyFree.setSelected(false);
                break;
            case 2:
                mBinding.llyWoman.setSelected(true);
                mBinding.llyStu.setSelected(false);
                mBinding.llyGoing.setSelected(false);
                mBinding.llyFree.setSelected(false);
                break;
            case 3:
                mBinding.llyWoman.setSelected(false);
                mBinding.llyStu.setSelected(false);
                mBinding.llyGoing.setSelected(true);
                mBinding.llyFree.setSelected(false);
                break;
            case 4:
                mBinding.llyWoman.setSelected(false);
                mBinding.llyStu.setSelected(false);
                mBinding.llyGoing.setSelected(false);
                mBinding.llyFree.setSelected(true);
                break;
        }
        this.type = type;
    }

    public interface IDialogClickListener {
        void onCancel();

        void onOk(int type);
    }
}
