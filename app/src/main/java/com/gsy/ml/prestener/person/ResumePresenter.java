package com.gsy.ml.prestener.person;


import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.ResumeModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

import static com.gsy.ml.common.Link.SELECTSESUME;

/**
 * Created by Administrator on 2017/4/14.
 */

public class ResumePresenter {
    final int GETRESUME = 1, UPDATESESUME = 2, GETRESUME2 = 3;
    int requestType = GETRESUME;
    private final ILoadPVListener mListener;

    public ResumePresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }


    /**
     * 获取简历信息
     */
    public void getResume(String phone) {
        requestType = GETRESUME;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("selectPhone", phone);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(SELECTSESUME, map, callback);
    }

    /**
     * 更新简历信息
     */
    public void updateResume(
            String phone,
            String personalEvaluation,
            String personalHobbies,
            String graduateSchool,
            String major,
            String qualifications,
            String schoolTime,
            String schoolLife,
            String certificate,
            String workExp
    ) {
        requestType = UPDATESESUME;
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", MaiLiApplication.Token);
        map.put("phone", phone);
        map.put("personalEvaluation", personalEvaluation);//个人评价
        map.put("personalHobbies", personalHobbies);//个人兴趣
        map.put("graduateSchool", graduateSchool);//教育经历
        map.put("major", major);//专业
        map.put("qualifications", qualifications);//学历
        map.put("schoolTime", schoolTime);//在校时间
        map.put("schoolLife", schoolLife);//在校经历
        map.put("certificate", certificate);//资质证书
        map.put("workExp", workExp);//工作经历
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.UPDATESESUME, map, callback);
    }

    public void updataInformation(String phone) {
        requestType = GETRESUME2;
        Map<String, String> map = new HashMap<String, String>();
        map.put("selectPhone", phone);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SELECTSESUME, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case UPDATESESUME:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }

                    break;
                case GETRESUME:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            ResumeModel modle = ParseJsonUtils.getBean((String) object, ResumeModel.class);
                            mListener.onLoadComplete(modle);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case GETRESUME2:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            ResumeModel model = ParseJsonUtils.getBean((String) object, ResumeModel.class);
                            mListener.onLoadComplete(model);
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
