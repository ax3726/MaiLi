package com.gsy.ml.prestener.main;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ErrorLogPresenter {
    final int ERRORLOG = 1;
    int requestType = ERRORLOG;


    public ErrorLogPresenter() {

    }

    public void getNewCard(String phone,
                           String phoneBrand,
                           String phoneModel,
                           String networkStatus,
                           String errorInfo
                           ) {
        requestType = ERRORLOG;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("phoneBrand", phoneBrand);
        map.put("phoneModel", phoneModel);
        map.put("networkStatus", networkStatus);
        map.put("errorInfo", errorInfo);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.ERRORLOG, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
           // mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case ERRORLOG:
                    try {
                        if (object instanceof HttpErrorModel) {
                      //     mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                       //     mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                      //  mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
            }
        }
    };
}
