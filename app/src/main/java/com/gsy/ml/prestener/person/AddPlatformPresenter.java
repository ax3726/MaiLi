package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.BanlanceModel;
import com.gsy.ml.model.person.AddPlatformModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddPlatformPresenter {
    final int FINDBINDING = 1;
    int requestType = FINDBINDING;
    private ILoadPVListener mListener;

    public AddPlatformPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void referStore(String phone) {
        requestType = FINDBINDING;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.FINDBINDING, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case FINDBINDING:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            AddPlatformModel modle = ParseJsonUtils.getBean((String) object, AddPlatformModel.class);
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
