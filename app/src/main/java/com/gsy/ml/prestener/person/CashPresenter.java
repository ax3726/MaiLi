package com.gsy.ml.prestener.person;

import android.graphics.BitmapFactory;

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

public class CashPresenter {
    final int CASHBANLANCE = 1;
    int requestType = CASHBANLANCE;
    private ILoadPVListener mListener;

    public CashPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void cashBanlance(String phone,
                            String cardNumber,
                            String cashMoney,
                            String cashType  //提现类型，1，不含保证金的钱，2，含提现保证金
    ) {
        requestType = CASHBANLANCE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("cardNumber", cardNumber);
        map.put("cashMoney", cashMoney);
        map.put("cashType", cashType);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CASHBANLANCE, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CASHBANLANCE:
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
