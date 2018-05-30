package com.gsy.ml.prestener.home;

import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.ReceivingModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/30.
 */

public class ReceivingPresenter {
    final int SENDORDERS = 1, SELECTORDERSFUZZY = 2;
    int requestType = SENDORDERS;
    private final ILoadPVListener listener;

    public ReceivingPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void fuzzyOrder(String param,
                           String workJing,
                           String workWei,
                           String pageNo,
                           String pageSize
    ) {
        requestType = SELECTORDERSFUZZY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("param", param);

        map.put("pageNo", pageNo);
        if (!TextUtils.isEmpty(workJing)) {
            map.put("jing", workJing);
        }
        if (!TextUtils.isEmpty(workWei)) {
            map.put("wei", workWei);
        }
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTORDERSFUZZY, map, callback);
    }

    public void inquire(String city,
                        String area,
                        String workType,
                        String workJing,
                        String workWei,
                        String orderByType,
                        String pageNo,
                        String pageSize
    ) {
        requestType = SENDORDERS;
        Map<String, String> map = new HashMap<String, String>();
        if (!TextUtils.isEmpty(city)) {
            map.put("city", city);
        }
        if (!"区域不限".equals(area)) {
            map.put("area", area);
        }
        if (!workType.equals("0")) {
            map.put("workType", workType);
        }
        if (!TextUtils.isEmpty(workJing)) {
            map.put("jing", workJing);
        }
        if (!TextUtils.isEmpty(workWei)) {
            map.put("wei", workWei);
        }
        map.put("orderByType", orderByType);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTORDERS, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SENDORDERS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            ReceivingModel model = ParseJsonUtils.getBean((String) object, ReceivingModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case SELECTORDERSFUZZY:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            ReceivingModel model = ParseJsonUtils.getBean((String) object, ReceivingModel.class);
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
