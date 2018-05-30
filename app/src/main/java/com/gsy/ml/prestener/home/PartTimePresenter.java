package com.gsy.ml.prestener.home;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.PartTimeModel;
import com.gsy.ml.model.message.OrderMessageModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/9/2.
 */

public class PartTimePresenter {
    final int CREATECOMMUNICATE = 1, USERREPORT = 2, UPDATEWORKTOTALCOST = 3, FINDORDERINFO = 4;
    int requestType = CREATECOMMUNICATE;
    private final ILoadPVListener listener;

    public PartTimePresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void responsePart(String mOrder, String sendRingLetter, String mHuanxin, String phone) {
        requestType = CREATECOMMUNICATE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", mOrder);
        map.put("sendRingLetter", sendRingLetter);
        map.put("acceptRingLetter", mHuanxin);
        map.put("acceptPhone", phone);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CREATECOMMUNICATE, map, handler);
    }

    public void reportMessage(String orderId, String phone, String acceptPhone, String content) {
        requestType = USERREPORT;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", orderId);
        map.put("phone", phone);
        map.put("acceptPhone", acceptPhone);
        map.put("content", content);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.USERREPORT, map, handler);
    }

    public void reportModification(String orderId, String workTotalCost, String num) {
        requestType = UPDATEWORKTOTALCOST;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", orderId);
        map.put("workTotalCost", workTotalCost);
        map.put("num", num);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATEWORKTOTALCOST, map, handler);
    }

    public void findOrderInfo(String orderId) {
        requestType = FINDORDERINFO;
        Map<String, String> map = new HashMap<String, String>();
        map.put("orderId", orderId);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.FINDORDERINFO, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CREATECOMMUNICATE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            PartTimeModel model = ParseJsonUtils.getBean((String) object, PartTimeModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case USERREPORT:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel model = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case UPDATEWORKTOTALCOST:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel model = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case FINDORDERINFO:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            OrderMessageModel model = ParseJsonUtils.getBean((String) object, OrderMessageModel.class);
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
