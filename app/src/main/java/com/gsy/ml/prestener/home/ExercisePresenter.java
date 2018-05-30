package com.gsy.ml.prestener.home;

import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.ExerciseModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/7.
 */

public class ExercisePresenter {
    final int GETACTIVITYLIST = 1;
    int requestType = GETACTIVITYLIST;
    private final ILoadPVListener listener;

    public ExercisePresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void getActivity(String city) {
        requestType = GETACTIVITYLIST;
        Map<String, String> map = new HashMap<String, String>();
        map.put("city", TextUtils.isEmpty(city)?"":city);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());

        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETACTIVITYLIST, map, handler);
    }




    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case GETACTIVITYLIST:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            ExerciseModel model = ParseJsonUtils.getBean((String) object, ExerciseModel.class);
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
