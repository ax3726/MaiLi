package com.gsy.ml.prestener.person;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class EvaluatePresenter {
    final int EVALUATE = 1;
    int requestType = EVALUATE;
    private final ILoadPVListener mListener;

    public EvaluatePresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }


    /**
     * 添加评价
     */
    public void addEvaluate(String order,
                            String phone,
                            String evaluateLevle,
                            String evaluateContent,
                            String type) {
        requestType = EVALUATE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("phone", phone);
        map.put("evaluateLevle", evaluateLevle);//评价类型 ： 0未评价 1 人工好评   2人工中评  3人工差评  4 系统好评
        map.put("evaluateContent", evaluateContent);
        map.put("type", type);//type   1是接单人对发单人评价，2是发单人对接单人的评价
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.EVALUATE, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case EVALUATE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
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
