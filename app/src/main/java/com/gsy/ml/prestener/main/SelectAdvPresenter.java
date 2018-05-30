package com.gsy.ml.prestener.main;


import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.AdvModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SelectAdvPresenter {
    final int SELECTADV = 1;
    int requestType = SELECTADV;
    private final ILoadPVListener mListener;

    public SelectAdvPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void selectAdv() {
        requestType = SELECTADV;
        Map<String, String> map = new HashMap<String, String>();
        String area = MaiLiApplication.getInstance().getUserPlace().getCity();
        if (!TextUtils.isEmpty(area)) {
            map.put("area", area);
        }
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTADV, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTADV:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            AdvModel modle = ParseJsonUtils.getBean((String) object, AdvModel.class);
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
