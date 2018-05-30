package com.gsy.ml.prestener.person;

import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.LatLonModel;
import com.gsy.ml.model.person.SpeedinessShipmentsModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/8/16.
 */

public class SpeedinessShipmentsPresenter {
    final int FINDVALIDORDER = 1,GETLATLON=2;
    int requestType = FINDVALIDORDER;
    private ILoadPVListener mListener;

    public SpeedinessShipmentsPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void getListGoods(String phone, String typr, String page, String row) {
        requestType = FINDVALIDORDER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", typr);
        map.put("page", page);
        map.put("row", row);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.FINDVALIDORDER, map, callback);
    }
    //地理编码获取经纬度
    public void getLatLon(String address) {
        requestType= GETLATLON;
        Map<String, String> map = new HashMap<String, String>();
        map.put("address", address);
      map.put("city", TextUtils.isEmpty(MaiLiApplication.getInstance().getUserPlace().getCity())?
                "":MaiLiApplication.getInstance().getUserPlace().getCity());

        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETLATLON, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case FINDVALIDORDER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            SpeedinessShipmentsModel modle = ParseJsonUtils.getBean((String) object, SpeedinessShipmentsModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case GETLATLON:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            LatLonModel modle = ParseJsonUtils.getBean((String) object, LatLonModel.class);
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
