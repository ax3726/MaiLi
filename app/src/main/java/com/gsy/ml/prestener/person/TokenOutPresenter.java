package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.TakenOutModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/8/16.
 */

public class TokenOutPresenter {
    final int STOREAUTHORIZATION = 1,ADDELESTOREBIN=2;
    int requestType = STOREAUTHORIZATION;
    private ILoadPVListener mListener;

    public TokenOutPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void setakenOut(String phone) {
        requestType = STOREAUTHORIZATION;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.STOREAUTHORIZATION, map, callback);
    }
    public void addEleStoreBin(String phone,String elemUserName) {
        requestType = ADDELESTOREBIN;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("elemUserName", elemUserName);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.ADDELESTOREBIN, map, callback);
    }
    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case STOREAUTHORIZATION:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            TakenOutModel modle = ParseJsonUtils.getBean((String) object, TakenOutModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case ADDELESTOREBIN:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            TakenOutModel modle = ParseJsonUtils.getBean((String) object, TakenOutModel.class);
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
