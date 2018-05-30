package com.gsy.ml.prestener.person;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/29.
 */

public class VoucherPresenter {
    final int ALLVOUCHER = 1, VOUCHERBYWORK = 2;
    int requestType = ALLVOUCHER;
    private ILoadPVListener mListener;

    public VoucherPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    public void selectAllVocher(String phone, String pageNo, String pageSize) {
        requestType = ALLVOUCHER;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.ALLVOUCHER, map, callback);
    }

    public void selectVocherByWork(String phone, String workType, String pageNo, String pageSize) {
        requestType = VOUCHERBYWORK;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("workType", workType);
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.VOUCHERBYWORK, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case ALLVOUCHER:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            VoucherModel modle = ParseJsonUtils.getBean((String) object, VoucherModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case VOUCHERBYWORK:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete(object);
                        } else {
                            VoucherModel modle = ParseJsonUtils.getBean((String) object, VoucherModel.class);
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
