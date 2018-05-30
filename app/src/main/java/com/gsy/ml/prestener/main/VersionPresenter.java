package com.gsy.ml.prestener.main;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.VersionModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class VersionPresenter {
    final int VERSIONUPDATE = 1;
    int requestType = VERSIONUPDATE;
    private final ILoadPVListener mListener;

    public VersionPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void versionUpdate() {
        requestType = VERSIONUPDATE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "1");
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.VERSIONUPDATE, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case VERSIONUPDATE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            VersionModel modle = ParseJsonUtils.getBean((String) object, VersionModel.class);
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
