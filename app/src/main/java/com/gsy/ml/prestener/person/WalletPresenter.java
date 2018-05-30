package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.BanlanceModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/4.
 */

public class WalletPresenter {
    final int SELECTORDER = 1;
    int requestType = SELECTORDER;
    private final ILoadPVListener listener;

    public WalletPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void selectWaller(String phone, String s, String s1) {
        requestType = SELECTORDER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pageNo", s);
        map.put("pageSize", s1);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTWALLET, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SELECTORDER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            WalletModel model = ParseJsonUtils.getBean((String) object, WalletModel.class);
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
