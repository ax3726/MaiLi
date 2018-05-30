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
 * Created by Administrator on 2017/4/29.
 */

public class WorkJobPresenter {
    final int SELECTWORKJOB = 1,SAVEWORKJOB = 2;
    int requestType = SELECTWORKJOB;
    private ILoadPVListener mListener;

    public WorkJobPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void selectWorkJob(String phone) {
        requestType = SELECTWORKJOB;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTWORKJOB, map, callback);
    }

    /**
     * 更新职位预选
     * @param phone
     * @param work
     */
    public void saveWorkJob(String phone,String work) {
        requestType = SAVEWORKJOB;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("work", work);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SAVEWORKJOB, map, callback);
    }
    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTWORKJOB:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle,1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError(e.getMessage()));
                    }

                    break;
                case SAVEWORKJOB:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle,2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError(e.getMessage()));
                    }

                    break;
            }
        }
    };


}
