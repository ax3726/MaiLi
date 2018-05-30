package com.gsy.ml.prestener.person;


import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class FinishOrderPresenter {
    final int FINISHORDER = 1;
    int requestType = FINISHORDER;
    private final ILoadPVListener mListener;

    public FinishOrderPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    private int type = 0; //0 发单人完成  1 接单人完成

    /**
     * 确认订单
     */
    public void finishOrder(String order, String phone, int type) {
        requestType = FINISHORDER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("finishPhone", phone);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        map.put("token", MaiLiApplication.Token);
        this.type = type;
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.FINISHORDER, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case FINISHORDER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);

                            EventBus.getDefault().post(new UpdateNotice());
                            mListener.onLoadComplete(modle, 3);
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
