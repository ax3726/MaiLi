package com.gsy.ml.prestener.message;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.message.EaseChatFragmentModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/9/11.
 */

public class EaseChatPrestener {
    final int SEESERVICECHARGE = 1;
    int requestType = SEESERVICECHARGE;
    private final ILoadPVListener mListener;

    public EaseChatPrestener(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void messageFee(String phone, String city) {
        requestType = SEESERVICECHARGE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("city", city);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SEESERVICECHARGE, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SEESERVICECHARGE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            EaseChatFragmentModel modle = ParseJsonUtils.getBean((String) object, EaseChatFragmentModel.class);
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
