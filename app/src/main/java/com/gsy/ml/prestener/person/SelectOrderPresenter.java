package com.gsy.ml.prestener.person;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.MineBillingModel;
import com.gsy.ml.model.person.MineOrderModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SelectOrderPresenter {
    final int SELECTORDER = 1, SELECTORDER1 = 2, SELECTORDER2 = 3;
    int requestType = SELECTORDER;
    private final ILoadPVListener mListener;

    public SelectOrderPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void selectOrder(String phone, String type, String pageNo, String pageSize) {
        requestType = SELECTORDER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", type);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTORDER, map, callback);
    }



    public void selectOrder1(String phone, String type, String pageNo, String pageSize) {
        requestType = SELECTORDER1;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", type);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTORDER, map, callback);
    }

    public void selectOrder2(String phone, String type, String pageNo, String pageSize) {
        requestType = SELECTORDER2;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", type);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTORDER, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTORDER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            MineOrderModel modle = ParseJsonUtils.getBean((String) object, MineOrderModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case SELECTORDER1:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            MineBillingModel modle = ParseJsonUtils.getBean((String) object, MineBillingModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case SELECTORDER2:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
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
