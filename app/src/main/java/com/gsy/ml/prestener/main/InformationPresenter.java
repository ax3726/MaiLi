package com.gsy.ml.prestener.main;

import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.CacheUtils;
import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.SharedPreferencesUtils;


/**
 * Created by Administrator on 2017/4/14.
 * 完善资料数据提交
 */

public class InformationPresenter {

    final int UPDATEINFO = 1, GETUSERINFO = 2;
    int requestType = UPDATEINFO;
    ILoadPVListener mListener;

    public InformationPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }


//    public void updateInfo(String phone,
//                           String nickname,
//                           String name,
//                           String sex,
//                           String id_number,
//                           String city,
//                           String id_type,
//                           String head_url,
//                           String remarks
//    ) {
//        requestType = UPDATEINFO;
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("token", MaiLiApplication.Token);
//        params.put("phone", phone);
//        params.put("nickname", nickname);
//        params.put("name", name);
//        params.put("sex", sex);
//        params.put("id_number", id_number);
//        params.put("city", city);
//        params.put("id_type", id_type);
//        if (!TextUtils.isEmpty(head_url)) {
//            params.put("head_url", Link.ALIIMAGEURL + head_url);
//        }
//        params.put("remarks", remarks);
//        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATEINFO, params, customHttpHandler);
//    }


    public void updateInfo(String phone, String nick_name, String sex, String city, String occupation, String head_url) {
        requestType = UPDATEINFO;
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", MaiLiApplication.Token);
        map.put("phone", phone);
        map.put("nickname", nick_name);
        map.put("sex", sex);
        map.put("city", city);
        map.put("id_type", occupation);
        if (!TextUtils.isEmpty(head_url)) {
            map.put("head_url", Link.ALIIMAGEURL + head_url);
        }
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATEINFO, map, customHttpHandler);
    }


    public void getUserInfo(String phone) {
        requestType = GETUSERINFO;
        Map<String, String> params = new HashMap<String, String>();
        params.put("phone", phone);
        params.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.GETUSERINFO, params, customHttpHandler);
    }

    Api.CustomHttpHandler customHttpHandler = new Api.CustomHttpHandler() {
        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case UPDATEINFO:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            UserInfoModel info = ParseJsonUtils.getBean((String) object, UserInfoModel.class);
                            MaiLiApplication.getInstance().setUser(info);//保存用户信息
                            CacheUtils.getInstance().saveCache(Constant.USERINFO, info);//缓存用户信息
                            EventBus.getDefault().post(info);
                            mListener.onLoadComplete(info, 1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case GETUSERINFO:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            UserInfoModel info = ParseJsonUtils.getBean((String) object, UserInfoModel.class);
                            if (TextUtils.isEmpty(info.getUserInfo().getName())) {
                                info.getUserInfo().setName("未知");
                            }
                            SharedPreferencesUtils.setParam(MaiLiApplication.getInstance(), Constant.PHONE, info.getUserInfo().getPhone());//缓存用户账号
                            MaiLiApplication.getInstance().setUser(info);//保存用户信息
                            CacheUtils.getInstance().saveCache(Constant.USERINFO, info);//缓存用户信息
                            EaseUser easeUser = new EaseUser();

                            easeUser.setAvatar(info.getUserInfo().getHeadUrl());
                            easeUser.setInitialLetter(info.getUserInfo().getNickname());
                            easeUser.setPhone(info.getUserInfo().getPhone());
                            EaseUI.getInstance().setEaseUser(easeUser);//更新用户头像

                            EventBus.getDefault().post(info);
                            mListener.onLoadComplete(info);
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
