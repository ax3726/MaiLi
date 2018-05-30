package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.OpinionModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/18.
 */

public class OpinionPresenter {
    final int FEEDBACK = 1;
    int requestType = FEEDBACK;
    private final ILoadPVListener listener;

    public OpinionPresenter(ILoadPVListener listenre) {
        this.listener = listenre;
    }

    public void initContent(String phone, String name, String content) {
        requestType = FEEDBACK;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("name", name);
        map.put("content", content);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.FEEDBACK, map, handel);
    }

    Api.CustomHttpHandler handel = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case FEEDBACK:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            OpinionModel model = ParseJsonUtils.getBean((String) object, OpinionModel.class);
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
