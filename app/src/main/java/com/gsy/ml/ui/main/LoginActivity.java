package com.gsy.ml.ui.main;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityLoginLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.LoginModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.InformationPresenter;
import com.gsy.ml.prestener.main.LoginPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.MD5;
import com.gsy.ml.ui.utils.SoftKeyboardStateHelper;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import ml.gsy.com.library.utils.CacheUtils;
import ml.gsy.com.library.utils.SharedPreferencesUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/12.
 * 登录页面
 */

public class LoginActivity extends BaseActivity implements ILoadPVListener, View.OnClickListener {
    private ActivityLoginLayoutBinding loginLayoutBinding;
    private LoginPresenter login = new LoginPresenter(this);
    private InformationPresenter mPresenter = new InformationPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_login_layout;
    }

    private boolean show_phone = false;

    @Override
    public void initData() {
        super.initData();
        show_phone = getIntent().getBooleanExtra("show_phone", false);
        loginLayoutBinding = (ActivityLoginLayoutBinding) vdb;

        loginLayoutBinding.tvRegister.setOnClickListener(this);
        loginLayoutBinding.etPhone.requestFocus();
        loginLayoutBinding.etPassword.requestFocus();
        loginLayoutBinding.tvRetrieve.setOnClickListener(this);

        loginLayoutBinding.btLogin.setOnClickListener(this);

        loginLayoutBinding.imgLogin.setAlpha(0.1f);
        loginLayoutBinding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    loginLayoutBinding.tvMark.setVisibility(View.VISIBLE);
                } else {
                    loginLayoutBinding.tvMark.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        String phone = (String) SharedPreferencesUtils.getParam(aty, Constant.PHONE, "");
        loginLayoutBinding.etPhone.setText(TextUtils.isEmpty(phone) ? "" : phone);

        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(loginLayoutBinding.getRoot());
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {//打开

                ObjectAnimator anim = ObjectAnimator.ofFloat(loginLayoutBinding.llyMl, "alpha", 0f);
                anim.setDuration(400);

                ObjectAnimator anim1 = ObjectAnimator.ofFloat(loginLayoutBinding.llyMl, "translationY", 0, -loginLayoutBinding.llyMl.getHeight());
                anim1.setDuration(400);


                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(loginLayoutBinding.llyBody, "translationY", 0, -(loginLayoutBinding.llyMl.getHeight() + 100));
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

                ObjectAnimator anim = ObjectAnimator.ofFloat(loginLayoutBinding.llyMl, "alpha", 1f);
                anim.setDuration(400);

                ObjectAnimator anim1 = ObjectAnimator.ofFloat(loginLayoutBinding.llyMl, "translationY", -loginLayoutBinding.llyMl.getHeight(), 0);
                anim1.setDuration(400);


                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(loginLayoutBinding.llyBody, "translationY", -(loginLayoutBinding.llyMl.getHeight() + 100), 0);
                circlePtAnim.setDuration(400);


                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());

                animSet.play(anim).with(anim1);
                animSet.play(circlePtAnim).with(anim1);

                animSet.start();
            }
        });


        loginLayoutBinding.flyLogin.setOnClickListener(this);
        if (show_phone) {
            UserInfoModel user = (UserInfoModel) CacheUtils.getInstance().loadCache(Constant.USERINFO);
            if (user != null && user.getUserInfo() != null) {
                loginLayoutBinding.etPhone.setText(user.getUserInfo().getPhone());
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void login() {
        String phone = loginLayoutBinding.etPhone.getText().toString().trim();
        String password = loginLayoutBinding.etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            DemoUtils.nope(loginLayoutBinding.etPhone).start();
            Toast.makeText(aty, "手机号不能为空！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (phone.length() != 11) {
            DemoUtils.nope(loginLayoutBinding.etPhone).start();
            Toast.makeText(aty, "手机号长度不正确！！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            DemoUtils.nope(loginLayoutBinding.etPassword).start();
            Toast.makeText(aty, "密码不能为空！！", Toast.LENGTH_SHORT).show();
            return;
        }
     /*   if (password.length() < 6 || password.length() > 16) {
            DemoUtils.nope(loginLayoutBinding.etPassword).start();
            Toast.makeText(aty, "请输入6-16位密码，由中英文组成!", Toast.LENGTH_SHORT).show();
            return;
        }*/

        showWaitDialog("正在登录中");
        login.LoginIn(phone, MD5.MD5Pwd(phone, password));
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {

        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            hideWaitDialog();
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof LoginModel) {
            LoginModel info = (LoginModel) object;
            loginIn(info.getRingLetter(), info.getRingLetterPwd());
        } else if (object instanceof UserInfoModel) {
            hideWaitDialog();
            UserInfoModel info = (UserInfoModel) object;
            startActivity(new Intent(aty, MainActivity.class));
            finish();
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
                String phone = loginLayoutBinding.etPhone.getText().toString().trim();
                mPresenter.getUserInfo(phone);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                break;
            case R.id.tv_retrieve:
                startActivity(new Intent(LoginActivity.this, ResetActivity.class));
                break;
            case R.id.bt_login:
                login();
                break;
            case R.id.fly_login:
                Utils.closeInputPad(aty);
                break;
        }
    }
}