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

public class ConfirOrderPresenter {
    final int CONFIRORDER = 1;
    int requestType = CONFIRORDER;
    private final ILoadPVListener mListener;

    public ConfirOrderPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }


    /**
     * 确认订单
     */
    public void confirOrder(String id,
                            String order,
                            String acceptPhone,
                            String acceptPeople
    ) {
        requestType = CONFIRORDER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("order", order);
        map.put("acceptPhone", acceptPhone);
        map.put("acceptPeople", acceptPeople);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CONFIRORDER, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CONFIRORDER:
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
