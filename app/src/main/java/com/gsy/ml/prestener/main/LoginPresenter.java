package com.gsy.ml.prestener.main;


import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.LoginModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import ml.gsy.com.library.utils.ParseJsonUtils;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by Administrator on 2017/4/14.
 */

public class LoginPresenter {
    final int LOGININ = 1;
    int requestType = LOGININ;
    private final ILoadPVListener mListener;

    public LoginPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void LoginIn(String phone, String password) {
        requestType = LOGININ;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pwd", password);
        map.put("registrationId",
                TextUtils.isEmpty(MaiLiApplication.RegistrationID) ?
                        JPushInterface.getRegistrationID(MaiLiApplication.appliactionContext)
                        : MaiLiApplication.RegistrationID);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.LOGIN, map, callback);
    }

    private String getDeviceId() {
        TelephonyManager TelephonyMgr = (TelephonyManager) MaiLiApplication.getInstance().getSystemService(TELEPHONY_SERVICE);
        return TelephonyMgr.getDeviceId();
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case LOGININ:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            LoginModel modle = ParseJsonUtils.getBean((String) object, LoginModel.class);

                            if (modle != null) {
                                MaiLiApplication.DoingWorkType1 = modle.isDoingWorkType1();
                                MaiLiApplication.IsPayPwd = modle.isPayPwd();
                                MaiLiApplication.Token = modle.getToken();
                                MaiLiApplication.IsWork = modle.isIfWork();
                                MaiLiApplication.getInstance().mHuanxin = modle.getRingLetter();
                                MaiLiApplication.getInstance().mHuanxinPwd = modle.getRingLetterPwd();
                            }
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
            }
        }
    };
}
