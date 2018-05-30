package com.gsy.ml.prestener.person;

import android.util.Log;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.LoginModel;
import com.gsy.ml.model.person.PasswoedModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ChangePasswordPresenter {
    final int GETCODE = 1;
    int requestType = GETCODE;
    ILoadPVListener iListener;

    public ChangePasswordPresenter(ILoadPVListener iListener) {
        this.iListener = iListener;
    }

    public void AmendPwd(String phone, String originalPaw, String newPwd) {
        requestType = GETCODE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pwd", originalPaw);
        map.put("newPwd", newPwd);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATEPWD1, map, handler);
    }

    Api.CustomHttpHandler handler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            iListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case GETCODE:
                    try {
                        if (object instanceof HttpErrorModel) {
                            iListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            PasswoedModel modle = ParseJsonUtils.getBean((String) object, PasswoedModel.class);
                            iListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        iListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
            }
        }
    };
}
