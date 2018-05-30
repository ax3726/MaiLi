package com.gsy.ml.prestener.person;


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

public class CancelOrderPresenter {
    final int CANCELORDER1 = 1, CANCELORDER2 = 2;
    int requestType = CANCELORDER1;
    private final ILoadPVListener mListener;

    public CancelOrderPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 接单人取消
     *
     * @param acceptPhone
     * @param order
     * @param cancelReason
     */
    public void cancelOrder2(String acceptPhone, String order, String cancelReason) {
        requestType = CANCELORDER2;
        Map<String, String> map = new HashMap<String, String>();
        map.put("acceptPhone", acceptPhone);
        map.put("order", order);
        map.put("cancelReason", cancelReason);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CANCELORDER2, map, callback);
    }

    /**
     * 发单人取消
     *
     * @param sendPhone
     * @param order
     * @param cancelReason
     */
    public void cancelOrder1(String sendPhone, String order, String cancelReason) {
        requestType = CANCELORDER1;
        Map<String, String> map = new HashMap<String, String>();
        map.put("sendPhone", sendPhone);
        map.put("order", order);
        map.put("cancelReason", cancelReason);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CANCELORDER1, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CANCELORDER1:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle, 2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case CANCELORDER2:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle, 2);
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
