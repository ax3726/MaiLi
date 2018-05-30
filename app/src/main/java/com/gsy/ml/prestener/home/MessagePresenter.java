package com.gsy.ml.prestener.home;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.MessageInfoModel;
import com.gsy.ml.model.home.MessageModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/6.
 */

public class MessagePresenter {
    final int SELECTMESSAGE = 1,SELECTMESSAGEINFO = 2;
    int requestType = SELECTMESSAGE;
    private final ILoadPVListener listener;

    public MessagePresenter(ILoadPVListener listener) {
        this.listener = listener;
    }


    public void selectMessage(String phone,
                              String pageNo,
                              String pageSize
    ) {
        requestType = SELECTMESSAGE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTMESSAGE, map, handler);
    }

    public void selectMessageInfo(String id
    ) {
        requestType = SELECTMESSAGEINFO;
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", id);
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTMESSAGEINFO, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTMESSAGE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            MessageModel model = ParseJsonUtils.getBean((String) object, MessageModel.class);
                            listener.onLoadComplete(model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        listener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case SELECTMESSAGEINFO:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            MessageInfoModel model = ParseJsonUtils.getBean((String) object, MessageInfoModel.class);
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
