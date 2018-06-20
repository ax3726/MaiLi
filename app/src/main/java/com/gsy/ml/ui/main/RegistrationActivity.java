package com.gsy.ml.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityRegistrationLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.LoginModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.InformationPresenter;
import com.gsy.ml.prestener.main.LoginPresenter;
import com.gsy.ml.prestener.main.RegisterPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.MD5;
import com.gsy.ml.ui.utils.SoftKeyboardStateHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/13.
 * 注册页面
 */

public class RegistrationActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityRegistrationLayoutBinding mBinding;
    private RegisterPresenter mPresenter = new RegisterPresenter(this);
    private LoginPresenter mLoginPresenter = new LoginPresenter(this);
    private InformationPresenter mInformationPresenter = new InformationPresenter(this);
    private String phone = "", pwd = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_registration_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityRegistrationLayoutBinding) vdb;
        mBinding.imgRegister.setAlpha(0.1f);
        mBinding.btRegistration.setOnClickListener(this);
        mBinding.tvCode.setOnClickListener(this);
        mBinding.flyBody.setOnClickListener(this);
        mBinding.flyBody.setOnClickListener(this);
        mBinding.tvRegisterWeb.setOnClickListener(this);
        mBinding.etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)){
                    mBinding.tvMark.setVisibility(View.VISIBLE);
                }else{
                    mBinding.tvMark.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(mBinding.flyBody);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {//打开

                ObjectAnimator anim = ObjectAnimator.ofFloat(mBinding.llyMl, "alpha", 0f);
                anim.setDuration(400);

                ObjectAnimator anim1 = ObjectAnimator.ofFloat(mBinding.llyMl, "translationY", 0, - mBinding.llyMl.getHeight());
                anim1.setDuration(400);


                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(mBinding.llyBody, "translationY", 0, - (mBinding.llyMl.getHeight()+100));
                circlePtAnim.setDuration(400);


                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());
                //两个动画同时执行
                animSet.playTogether(anim, circlePtAnim);
                animSet.play(anim).with(anim1);
                animSet.play(circlePtAnim).with(anim1);

                animSet.start();

            }

            @Override
            public void onSoftKeyboardClosed() {//关闭

                ObjectAnimator anim = ObjectAnimator.ofFloat(mBinding.llyMl, "alpha", 1f);
                anim.setDuration(400);

                ObjectAnimator anim1 = ObjectAnimator.ofFloat(mBinding.llyMl, "translationY",  - mBinding.llyMl.getHeight(),0);
                anim1.setDuration(400);


                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(mBinding.llyBody, "translationY",  - (mBinding.llyMl.getHeight()+100),0);
                circlePtAnim.setDuration(400);


                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());

                animSet.play(anim).with(anim1);
                animSet.play(circlePtAnim).with(anim1);

                animSet.start();
            }
        });

}
    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("注册");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_registration:
                register();
                break;
            case R.id.tv_code://获取验证码
                if (countDownClickable) {
                    getCheckCode();
                }
                break;
            case R.id.lly_left:
                finish();
                break;
            case R.id.fly_body:
                Utils.closeInputPad(aty);
                break;
            case R.id.tv_register_web:
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(-2)));
                break;

        }
    }

    /**
     * 获取验证码
     */
    private void getCheckCode() {
        String phone = mBinding.etPhone.getText().toString().trim();
        if (phone.length() < 11) {
            countDownClickable = true;
            DemoUtils.nope(mBinding.etPhone).start();
            Toast.makeText(aty, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            showWaitDialog();
            mPresenter.getCode(phone);
        }
    }

    private void register() {
        phone = mBinding.etPhone.getText().toString().trim();
        pwd = mBinding.etPwd.getText().toString().trim();
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
       /* if (pwd.length()<6||pwd.length()>16) {
            DemoUtils.nope(mBinding.etPwd).start();
            Toast.makeText(aty, "请输入6-16位密码，由中英文组成!", Toast.LENGTH_SHORT).show();
            return;
        }*/

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
        if (!mBinding.checkBox.isChecked()) {
            Toast.makeText(aty, "请阅读蚂蚁快服平台注册协议!", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        mPresenter.getRegister(phone, MD5.MD5Pwd(phone, pwd), code);
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
                    mLoginPresenter.LoginIn(phone, MD5.MD5Pwd(phone, pwd));
                }
            }
        } else if (object instanceof UserInfoModel) {
            UserInfoModel userInfoModel = (UserInfoModel) object;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(RegistrationActivity.this, SuccessfulActivity.class));
                    finish();
                }
            }.start();
        } else if (object instanceof LoginModel) {
            LoginModel info = (LoginModel) object;
            loginIn(info.getRingLetter(), info.getRingLetterPwd());
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }



    //**登陆环信
    private void loginIn(final String huanxin, final String pwd) {

        EMClient.getInstance().login(huanxin, pwd, new EMCallBack() {

            @Override
            public void onSuccess() {
                mInformationPresenter.getUserInfo(phone);
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, final String error) {

                if (code == 200) {//用户已登录
                    EMClient.getInstance().logout(true, new EMCallBack() {//退出登录

                        @Override
                        public void onSuccess() {
                            loginIn(huanxin, pwd);
                        }

                        @Override
                        public void onProgress(int progress, String status) {
                            // TODO Auto-generated method stub

                        }

                        @Override
                        public void onError(int code, final String message) {
                            // TODO Auto-generated method stub
                            hideWaitDialog();
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    });
                } else {
                    hideWaitDialog();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                        }
                    });
                }


            }
        });
    }

}
