package com.gsy.ml.prestener.main;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class AliPayPresenter {
    final int ALIPAY = 1,WEXPAY = 2;
    int requestType = ALIPAY;
    private final ILoadPVListener mListener;

    public AliPayPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void getAliPay(String phone,String body, String subject,String totalAmount) {
        requestType = ALIPAY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("body", body);
        map.put("subject", subject);
        map.put("totalAmount", totalAmount);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.ALIPAY, map, callback);
    }
    public void getWexPay(String phone,String body, String detail,String attach,String total_fee) {
        requestType = WEXPAY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("body", body);
        map.put("detail", detail);
        map.put("attach", phone);
        map.put("total_fee", total_fee);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.WEXPAY, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case ALIPAY:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle,10);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case WEXPAY:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            WEXModel modle = ParseJsonUtils.getBean((String) object, WEXModel.class);
                            mListener.onLoadComplete(modle,2);
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
