package com.gsy.ml.ui.main;

import android.content.Intent;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityWelcomeLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.LoginModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.InformationPresenter;
import com.gsy.ml.prestener.main.LoginPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

import ml.gsy.com.library.utils.CacheUtils;
import ml.gsy.com.library.utils.SharedPreferencesUtils;

public class WelcomeActivity extends BaseActivity implements ILoadPVListener {

    private ActivityWelcomeLayoutBinding mBinding;
    private LoginPresenter mPresenter = new LoginPresenter(this);
    private UserInfoModel user;
    private InformationPresenter mInformationPresenter = new InformationPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityWelcomeLayoutBinding) vdb;
        setTheme(R.style.AppTheme);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(500);
                    Boolean is_first = (Boolean) SharedPreferencesUtils.getParam(aty, Constant.ISFIRST, false);
                    if (!is_first) {
                        SharedPreferencesUtils.setParam(aty, Constant.ISFIRST, true);
                        Intent in = new Intent(aty, GuideActivity.class);
                        startActivity(in);
                        finish();
                    } else {
                        user = (UserInfoModel) CacheUtils.getInstance().loadCache(Constant.USERINFO);
                        if (user != null && user.getUserInfo() != null) {
                            mPresenter.LoginIn(user.getUserInfo().getPhone(), user.getPwd());
                        } else {
                            startActivity(new Intent(aty, LoginActivity.class));
                            finish();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(aty, LoginActivity.class));
                    finish();
                }
            }.start();
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
                mInformationPresenter.getUserInfo(user.getUserInfo().getPhone());

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
                                    startActivity(new Intent(aty, LoginActivity.class));
                                    finish();
                                }
                            });
                        }
                    });
                } else {
                    hideWaitDialog();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                            startActivity(new Intent(aty, LoginActivity.class));
                            finish();
                        }
                    });
                }


            }
        });
    }
}
