package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.CheckCodeModel;
import com.gsy.ml.model.person.DeliveryModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/29.
 */

public class CheckCodePresenter {
    final int CHECKCODE = 1,PICKUPTIMEODERS = 2;
    int requestType = CHECKCODE;
    private ILoadPVListener mListener;

    public CheckCodePresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void checkCode(String order, String code,String index) {
        requestType = CHECKCODE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("code", code);
        map.put("index", index);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CHECKCODE, map, callback);
    } public void pickUpOrders(String order) {
        requestType = PICKUPTIMEODERS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.PICKUPTIMEODERS, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CHECKCODE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            CheckCodeModel modle = ParseJsonUtils.getBean((String) object, CheckCodeModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case PICKUPTIMEODERS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            DeliveryModel modle = ParseJsonUtils.getBean((String) object, DeliveryModel.class);
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
