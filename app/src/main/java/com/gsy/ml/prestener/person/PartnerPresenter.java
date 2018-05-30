package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.BanlanceModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.ui.person.PartnerActivity;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/24.
 */

public class PartnerPresenter {
    final int INSERTCOOPERATIVE = 1;
    int requestType = INSERTCOOPERATIVE;
    private final ILoadPVListener listener;

    public PartnerPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void insertCooperative(String phone, String name, String company, String wechatNumber, String qqNumber, String email, String province, String city, String area, String message, String industry) {
        requestType = INSERTCOOPERATIVE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("name", name);
        map.put("company", company);
        map.put("wechatNumber", wechatNumber);
        map.put("qqNumber", qqNumber);
        map.put("email", email);
        map.put("province", province);
        map.put("city", city);
        map.put("area", area);
        map.put("message", message);
        map.put("industry", industry);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.INSERTCOOPERATIVE, map, handle);
    }

    Api.CustomHttpHandler handle = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case INSERTCOOPERATIVE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete(object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            listener.onLoadComplete(modle);
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
