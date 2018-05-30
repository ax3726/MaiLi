package com.gsy.ml.prestener.person;

import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.UsualAddressModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.ui.person.UsualAddressActivity;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/5/16.
 */

public class UsualAddressPresenter {
    final int COMMONADDRESS = 1;
    int requestType = COMMONADDRESS;
    private final ILoadPVListener listener;

    public UsualAddressPresenter(ILoadPVListener listener) {
        this.listener = listener;
    }

    public void setMessage(String phone, String s, String size, String add_name, String add_phone, String province, String city, String district, String name, String longitude, String latitude, String mStartAddressName) {
        requestType = COMMONADDRESS;
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("type", s);
        map.put("id", size);
        map.put("contactsName", add_name);
        map.put("contactsPhone", add_phone);
        map.put("province", province);
        map.put("city", city);
        map.put("area", district);
        map.put("place", name);
        map.put("jing", longitude);
        map.put("wei", latitude);
        map.put("doorplate", mStartAddressName);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.COMMONADDRESS, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            listener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case COMMONADDRESS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            listener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            UsualAddressModel model = ParseJsonUtils.getBean((String) object, UsualAddressModel.class);
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
