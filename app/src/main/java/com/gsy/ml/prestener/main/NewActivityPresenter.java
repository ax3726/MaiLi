package com.gsy.ml.prestener.main;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.NewAcitivityModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class NewActivityPresenter {
    final int NEWACTIVITY = 1;
    int requestType = NEWACTIVITY;
    private final ILoadPVListener mListener;

    public NewActivityPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void getNewActivity(String phone) {
        requestType = NEWACTIVITY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.NEWACTIVITY, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case NEWACTIVITY:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            NewAcitivityModel modle = ParseJsonUtils.getBean((String) object, NewAcitivityModel.class);
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
