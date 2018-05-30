package com.gsy.ml.prestener.home;

import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/29.
 */

public class TotalPricePresenter {
    final int TOTALTUTOR = 1, GETMONEY = 2;
    int requestType = TOTALTUTOR;
    private ILoadPVListener mListener;

    public TotalPricePresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 家教计算价格
     *
     * @param area
     * @param grade
     * @param courseName
     */
    public void totalTutor(String area,
                           String grade,
                           String courseName
    ) {
        requestType = TOTALTUTOR;
        Map<String, String> map = new HashMap<String, String>();
        map.put("area", area);
        if (!TextUtils.isEmpty(grade)) {
            map.put("grade", grade);
        }
        map.put("courseName", courseName);//@参数： 年级可以为空，就是艺术家教，年级的参数必须是1-12，代表小学一年级到高中3年级
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.TOTALTUTOR, map, callback);
    }

    /**
     * 计算价格
     *
     * @param area
     * @param costType//计费方式，1.按小时计费，2，按天计费，3，按次数计费
     * @param workType
     */
    public void getMoney(String area,String costType,String workType) {
        requestType = GETMONEY;
        Map<String, String> map = new HashMap<String, String>();
        map.put("area", area);
        map.put("costType", costType);
        map.put("workType", workType);//@参数： 年级可以为空，就是艺术家教，年级的参数必须是1-12，代表小学一年级到高中3年级
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETMONEY, map, callback);
    }


    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case TOTALTUTOR:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            PriceModel modle = ParseJsonUtils.getBean((String) object, PriceModel.class);
                            mListener.onLoadComplete(modle, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case GETMONEY:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            PriceModel modle = ParseJsonUtils.getBean((String) object, PriceModel.class);
                            mListener.onLoadComplete(modle, 1);
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
