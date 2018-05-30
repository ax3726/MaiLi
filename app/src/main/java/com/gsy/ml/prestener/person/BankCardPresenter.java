package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.BankCardModel;
import com.gsy.ml.model.person.UsualAddressModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.ui.person.BankCardActivity;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BankCardPresenter {
    final int MYBANK = 1;
    int requestType = MYBANK;
    private final ILoadPVListener listener;

    public BankCardPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void myBank(String phone, String id, String bankNum, String bankType, String type) {
        requestType = MYBANK;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("id", id);
        map.put("bankNum", bankNum);
        map.put("bankType", bankType);
        map.put("type", type);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.MYBANK, map, handle);
    }

    Api.CustomHttpHandler handle = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case MYBANK:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            BankCardModel model = ParseJsonUtils.getBean((String) object, BankCardModel.class);
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
