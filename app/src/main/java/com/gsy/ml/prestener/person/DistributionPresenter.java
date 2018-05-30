package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.CheckTimeModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/8/16.
 */

public class DistributionPresenter {
    final int CHECKPICKUPTIME = 1,CHECKDELVERYTIME=2;
    int requestType = CHECKPICKUPTIME;
    private ILoadPVListener mListener;

    public DistributionPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void checkPickUpTime(String order) {
        requestType = CHECKPICKUPTIME;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CHECKPICKUPTIME, map, callback);
    }

    public void checkCkdelveryTime(String order,String index) {
        requestType= CHECKDELVERYTIME;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("index", index);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CHECKDELVERYTIME, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CHECKPICKUPTIME:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            CheckTimeModel modle = ParseJsonUtils.getBean((String) object, CheckTimeModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case CHECKDELVERYTIME:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            CheckTimeModel modle = ParseJsonUtils.getBean((String) object, CheckTimeModel.class);
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
