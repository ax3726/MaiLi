package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.ClaimGoodsModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/12.
 */

public class ClaimGoodsPresenter {
    final int CLAIMGOODS = 1, INSERTORDERIMG = 2;
    int requestType = CLAIMGOODS;
    private ILoadPVListener listener;

    public ClaimGoodsPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void selectOrder(String order) {
        requestType = CLAIMGOODS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CLAIMGOODS, map, handler);
    }

    public void insertOrderImg(String order,
                               String sendName,
                               String sendPhone,
                               String acceptPhone,
                               String receivedName,
                               String receivedPhone,
                               String goodsImg

    ) {
        requestType = INSERTORDERIMG;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("sendName", sendName);
        map.put("sendPhone", sendPhone);
        map.put("acceptPhone", acceptPhone);
        map.put("receivedName", receivedName);
        map.put("receivedPhone", receivedPhone);
        map.put("goodsImg", goodsImg);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.INSERTORDERIMG, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CLAIMGOODS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            ClaimGoodsModel modle = ParseJsonUtils.getBean((String) object, ClaimGoodsModel.class);
                            listener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case INSERTORDERIMG:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            listener.onLoadComplete(modle);
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
