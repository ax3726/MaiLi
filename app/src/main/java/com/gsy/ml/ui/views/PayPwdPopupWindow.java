package com.gsy.ml.ui.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.PopuwindowPayPwdBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.PaymentPasswordPresenter;
import com.gsy.ml.ui.person.PaymentPasswordActivity;
import com.gsy.ml.ui.utils.MD5;

import ml.gsy.com.library.common.LoadingDialog;

/**
 * Created by Administrator on 2017/4/26.
 */

public class PayPwdPopupWindow extends PopupWindow implements ILoadPVListener {
    private Context mContext;
    private PopuwindowPayPwdBinding mBinding;
    private PaymentPasswordPresenter presenter = new PaymentPasswordPresenter(this);
    private IPayPwdListener mIPayListener;

    public PayPwdPopupWindow(Context context) {
        this.mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_pay_pwd, null, false);
        setContentView(mBinding.getRoot());
        int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();


        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);

        setWidth(width);
        setHeight(height - statusBarHeight());
        this.setBackgroundDrawable(new BitmapDrawable());

        //添加pop窗口关闭事件
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                backgroundAlpha(1f);
            }
        });
        setAnimationStyle(R.style.lib_popwindow_anim_style);
        this.update();
        setView();
    }

    private void setView() {
        mBinding.pvPwd.setOnFinishInput(new OnPasswordInputFinish() {
            @Override
            public void inputFinish() {
                checkPayPwd(mBinding.pvPwd.getStrPassword());
            }
        });
        mBinding.pvPwd.getCancelImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        mBinding.pvPwd.getForgetTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mContext.startActivity(new Intent(mContext, PaymentPasswordActivity.class));
            }
        });
    }

    private void checkPayPwd(String pwd) {
        showWaitDialog();
        String phone = MaiLiApplication.getInstance().getUserInfo().getPhone();
        presenter.checkPaypwd(phone, MD5.MD5Pwd(phone, pwd));
    }

    private int statusBarHeight() {
        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
//获取status_bar_height资源的ID
        int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = mContext.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight1;
    }

    public void setIPayListener(IPayPwdListener mIPayListener) {
        this.mIPayListener = mIPayListener;
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            if (mBinding != null) {
                mBinding.pvPwd.clearPwd();//置空输入的支付密码}
            }
            if (!MaiLiApplication.IsPayPwd) {//没有设置支付密码
                InformationDialog mRoleDialog = new InformationDialog(mContext);
                mRoleDialog.setTitle("温馨提示");
                mRoleDialog.setMessage("检测到你的账户未设置支付密码,安全系数较低!建议立即设置支付密码!");
                mRoleDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
                    @Override
                    public void onDialogClick(Dialog dlg, View view) {
                        dlg.dismiss();
                    }
                });
                mRoleDialog.setPositiveButton("去设置", new InformationDialog.IDialogClickListener() {
                    @Override
                    public void onDialogClick(Dialog dlg, View view) {
                        dlg.dismiss();
                        mContext.startActivity(new Intent(mContext, PaymentPasswordActivity.class));
                    }
                });
                mRoleDialog.show();


            } else {
                backgroundAlpha(0.7f);
                this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            }


        } else {
            // this.dismiss();
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if ("500".equals(errorModel.getStatus())) {//支付密码错误
                mBinding.pvPwd.clearPwd();//置空输入的支付密码
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getInfo(), Toast.LENGTH_SHORT).show();

        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if (mIPayListener != null) {
                mIPayListener.finishCheck();
            }
            dismiss();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    public interface IPayPwdListener {
        void finishCheck();
    }

    private LoadingDialog mLoadingDialog;

    public void showWaitDialog() {

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(mContext, "正在验证...请稍后!");
        }
        mLoadingDialog.show();

    }

    public void hideWaitDialog() {
        if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }
}
