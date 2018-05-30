package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.CardIdModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/29.
 */

public class FaceIdPresenter {
    final int CARDRENZHEN = 1, CARDRENZHEN1 = 2;
    int requestType = CARDRENZHEN;
    private ILoadPVListener mListener;

    public FaceIdPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void cardId(String phone,
                       String positive,
                       String opposite
    ) {
        requestType = CARDRENZHEN;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("positive", positive);
        map.put("opposite", opposite);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CARDRENZHEN, map, callback);
    }

    public void cardId1(String phone,
                        String IdNum,
                        String IdName,
                        String image_best,
                        String image_action1,
                        String image_action2,
                        String image_action3,
                        String delta
    ) {
        requestType = CARDRENZHEN1;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("IdNum", IdNum);
        map.put("IdName", IdName);
        map.put("image_best", image_best);
        map.put("image_action1", image_action1);
        map.put("image_action2", image_action2);
        map.put("image_action3", image_action3);
        map.put("delta", delta);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.CARDRENZHEN1, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case CARDRENZHEN:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            CardIdModel modle = ParseJsonUtils.getBean((String) object, CardIdModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case CARDRENZHEN1:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
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
