package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.PaymentPasswordModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/6/20.
 */

public class PaymentPasswordPresenter {
    final int USERPAYPWD = 1,CHECKPAYPWD = 2;
    int requestType = USERPAYPWD;
    private final ILoadPVListener mListener;

    public PaymentPasswordPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void checkPaypwd(String phone,String payPwd) {
        requestType = CHECKPAYPWD;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("payPwd", payPwd);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CHECKPAYPWD, map, handler);
    }
    public void getPayment(String phone, String s, String code) {
        requestType = USERPAYPWD;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("payPwd", s);
        map.put("code", code);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.USERPAYPWD, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case USERPAYPWD:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            PaymentPasswordModel info = ParseJsonUtils.getBean((String) object, PaymentPasswordModel.class);
                            mListener.onLoadComplete(info, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case CHECKPAYPWD:
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
            }
        }
    };
}
