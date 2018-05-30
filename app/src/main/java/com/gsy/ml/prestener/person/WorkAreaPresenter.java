package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.WorkAreaModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/29.
 */

public class WorkAreaPresenter {

    final int SELECTMYAREA = 1,SAVEMYAREA = 2;
    int requestType = SELECTMYAREA;
    private ILoadPVListener mListener;

    public WorkAreaPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }


    public void selectWorkArea(String phone) {
        requestType = SELECTMYAREA;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTMYAREA, map, callback);
    }

    /**
     * 更新职位预选
     * @param phone
     */
    public void saveWorkArea(String phone,String place) {
        requestType = SAVEMYAREA;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("place", place);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SAVEMYAREA, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTMYAREA:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            WorkAreaModel modle = ParseJsonUtils.getBean((String) object, WorkAreaModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case SAVEMYAREA:
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
