package com.gsy.ml.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityResetLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.RegisterPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.MD5;
import com.gsy.ml.ui.utils.SoftKeyboardStateHelper;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/17.
 * 重置密码类
 */

public class ResetActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityResetLayoutBinding mBinding;
    private RegisterPresenter mPresenter = new RegisterPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityResetLayoutBinding) vdb;
        mBinding.imgRegister.setAlpha(0.1f);
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(mBinding.flyBody);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {//打开
                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(mBinding.llyBody, "translationY", 0, - (Utils.dip2px(aty,140)));
                circlePtAnim.setDuration(400);


                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());
                animSet.play(circlePtAnim);

                animSet.start();

            }

            @Override
            public void onSoftKeyboardClosed() {//关闭

                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(mBinding.llyBody, "translationY",  - (Utils.dip2px(aty,140)),0);
                circlePtAnim.setDuration(400);

                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());

                animSet.play(circlePtAnim);

                animSet.start();
            }
        });

    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("忘记密码");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ivConcea.setOnClickListener(this);
        mBinding.btRegistration.setOnClickListener(this);
        mBinding.tvCode.setOnClickListener(this);
        mBinding.flyBody.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.iv_concea://
                if (!mBinding.ivConcea.isSelected()) {
                    mBinding.ivConcea.setSelected(true);
                    mBinding.etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mBinding.ivConcea.setSelected(false);
                    mBinding.etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码为不可见。
                }
                mBinding.etPwd.postInvalidate();
                CharSequence text = mBinding.etPwd.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }

                break;
            case R.id.tv_code://获取验证码
                if (countDownClickable) {
                    getCheckCode();
                }
                break;
            case R.id.bt_registration://重置密码
                checkData();
                break;
            case R.id.fly_body:
                Utils.closeInputPad(aty);
                break;
        }
    }

    private boolean countDownClickable = true;
    private long countDownTime = 100000;//100秒
    private Handler countDownHandler = new Handler();
    Runnable countDownRunnable = new Runnable() {
        @Override
        public void run() {
            if (countDownTime >= 0) {
                mBinding.tvCode.setText("重新获取(" + String.valueOf(countDownTime / 1000) + ")");
                countDownTime = countDownTime - 1000;
                countDownHandler.postDelayed(this, 1000);
            } else {
                countDownClickable = true;
                countDownTime = 60000;
                mBinding.tvCode.setText("获取验证码");
            }
        }
    };

    private void checkData() {
        String phone = mBinding.etPhone.getText().toString().trim();
        String pwd = mBinding.etPwd.getText().toString().trim();
        String code = mBinding.etCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            DemoUtils.nope(mBinding.etPhone).start();
            Toast.makeText(aty, "请输入手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            DemoUtils.nope(mBinding.etPhone).start();
            Toast.makeText(aty, "请输入正确的手机号码!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            DemoUtils.nope(mBinding.etPwd).start();
            Toast.makeText(aty, "请输入密码!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(code)) {
            DemoUtils.nope(mBinding.etCode).start();
            Toast.makeText(aty, "请输入验证码!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (code.length() != 6) {
            DemoUtils.nope(mBinding.etCode).start();
            Toast.makeText(aty, "请输入正确的验证码!", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        mPresenter.updatePwd(phone, MD5.MD5Pwd(phone, pwd), code);
    }

    /**
     * 获取验证码
     */
    private void getCheckCode() {
        String phone = mBinding.etPhone.getText().toString().trim();
        if (phone.length() < 11) {
            countDownClickable = true;
            Toast.makeText(aty, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            showWaitDialog();
            mPresenter.getCode(phone);
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel successModel = (HttpSuccessModel) object;
            if (parms.length > 0) {
                int parm = parms[0];
                if (parm == 1) {
                    countDownRunnable.run();
                    countDownClickable = false;
                    Toast.makeText(aty, "验证码已发送，请注意查收！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(aty, successModel.getInfo(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(aty, LoginActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
