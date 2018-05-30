package com.gsy.ml.prestener.person;

import android.text.TextUtils;

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
 * Created by Administrator on 2017/4/29.
 */

public class PayOrderPresenter {
    final int CHOOSEPAY = 1;
    int requestType = CHOOSEPAY;
    private ILoadPVListener mListener;

    public PayOrderPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void getPayOrder(String OrderId,
                            String AcceptPhone,
                            String AcceptPeople,
                            String CostNum,
                            String cashCouponId,
                            String OnLine
    ) {
        requestType = CHOOSEPAY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", OrderId);
        map.put("acceptPhone", AcceptPhone);
        map.put("acceptPeople", TextUtils.isEmpty(AcceptPeople) ? "" : AcceptPeople);
        map.put("costNum", CostNum);
        map.put("cashCouponId", cashCouponId);
        map.put("onLine", OnLine);//是否是线上支付 0：线下支付；1：线上支付
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CHOOSEPAY, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CHOOSEPAY:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
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
