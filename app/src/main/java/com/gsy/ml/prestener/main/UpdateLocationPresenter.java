package com.gsy.ml.prestener.main;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class UpdateLocationPresenter {
    final int UPDATELOCATION = 1;
    int requestType = UPDATELOCATION;
    private final ILoadPVListener mListener;

    public UpdateLocationPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void UpdateLocation(String phone,
                               String province,
                               String city,
                               String area,
                               String jing,
                               String wei,
                               String palce) {
        requestType = UPDATELOCATION;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("province", province);
        map.put("city", city);
        map.put("area", area);
        map.put("jing", jing);
        map.put("wei", wei);
        map.put("palce", palce);
        map.put("token", MaiLiApplication.Token);
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        userPlace.setArea(area);
        userPlace.setCity(city);
        userPlace.setJing(jing);
        userPlace.setWei(wei);
        userPlace.setPalce(palce);
        MaiLiApplication.getInstance().setUserPlace(userPlace);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATELOCATION, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case UPDATELOCATION:
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
