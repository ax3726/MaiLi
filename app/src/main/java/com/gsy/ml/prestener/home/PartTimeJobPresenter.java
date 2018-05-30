package com.gsy.ml.prestener.home;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.PartTimeJobModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/6.
 */

public class PartTimeJobPresenter {
    final int PARTICULARS = 1,GETORDERPUSH=2;
    int requestType = PARTICULARS;
    private final ILoadPVListener listener;

    public PartTimeJobPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }
    public void selectOrder(String order) {
        requestType = PARTICULARS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());

        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.PARTICULARS, map, handler);
    }


    public void getOrderPush(String orderId) {
        requestType = GETORDERPUSH;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", orderId);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETORDERPUSH, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case PARTICULARS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            PartTimeJobModel model = ParseJsonUtils.getBean((String) object, PartTimeJobModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case GETORDERPUSH:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel model = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            listener.onLoadComplete(model,1);
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
