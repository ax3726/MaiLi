package com.gsy.ml.prestener.home;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.NoReadModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/7.
 */

public class NoReadPresenter {
    final int NOREAD = 3;
    int requestType = NOREAD;
    private final ILoadPVListener listener;

    public NoReadPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void getNoRead(String phone) {
        requestType = NOREAD;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.NOREAD, map, handler);
    }




    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case NOREAD:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            NoReadModel model = ParseJsonUtils.getBean((String) object, NoReadModel.class);
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
