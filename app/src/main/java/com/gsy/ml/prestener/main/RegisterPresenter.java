package com.gsy.ml.prestener.main;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class RegisterPresenter {

    final int GETCODE = 1, GETREGISTER = 2, UPDATEPWD = 3;
    int requestType = GETCODE;
    ILoadPVListener mListener;

    public RegisterPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void getCode(String phone) {
        requestType = GETCODE;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETCODE, params, customHttpHandler);
    }

    public void getRegister(String phone, String pwd, String code) {
        requestType = GETREGISTER;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("pwd", pwd);
        params.put("code", code);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETREGISTER, params, customHttpHandler);
    }

    /*
     * 重置密码
     * @param phone
     * @param password
     * @param code
     */
    public void updatePwd(String phone, String password, String code) {
        requestType = UPDATEPWD;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pwd", password);
        map.put("code", code);

        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATEPWD, map, customHttpHandler);
    }


    Api.CustomHttpHandler customHttpHandler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case GETCODE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel info = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(info, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case GETREGISTER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel info = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(info, 2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case UPDATEPWD:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel info = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(info, 3);
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
