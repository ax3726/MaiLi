package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.ShareMoneyModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.ui.person.ShareMoneyActivity;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShareMoneyPresenter {
    final int SELECTMYTEAM = 1;
    int requestType = SELECTMYTEAM;
    private final ILoadPVListener listener;

    public ShareMoneyPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void selectMyTeam(String phone) {
        requestType = SELECTMYTEAM;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTMYTEAM, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTMYTEAM:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            ShareMoneyModel model = ParseJsonUtils.getBean((String) object, ShareMoneyModel.class);
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
