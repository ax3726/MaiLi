package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.IntegerModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/6.
 */

public class IntegerPresenter {
    final int INTEGER = 1;
    int requestType = INTEGER;
    private final ILoadPVListener listener;

    public IntegerPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void selectInteger(String phone, String s, String s1) {
        requestType = INTEGER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pageNo", s);
        map.put("pageSize", s1);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.INTEGER, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case INTEGER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            IntegerModel model = ParseJsonUtils.getBean((String) object, IntegerModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
            }
        }
    };
}
