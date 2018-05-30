package com.gsy.ml.prestener.home;


import android.text.TextUtils;

import com.gsy.ml.common.Api;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.prestener.common.ILoadPVListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/4/14.
 */

public class SendOrdersPresenter {
    final int SENDORDERS = 1, COUNTPRICE = 2, COMMITORDERS = 3;
    int requestType = SENDORDERS;
    private final ILoadPVListener mListener;

    public SendOrdersPresenter(ILoadPVListener mListener) {
        this.mListener = mListener;
    }

    /**
     * 发布订单
     *
     * @param phone
     * @param sendPeople
     * @param workType
     * @param workStartTime
     * @param workEndTime
     * @param workProvince
     * @param workCity
     * @param workArea
     * @param workJing
     * @param workWei
     * @param workPlace
     * @param workLable
     * @param workContent
     * @param workCost
     * @param workTotalCost
     * @param peopleNum
     */
    public void SendOrders(String phone,
                           String sendPeople,
                           String workType,
                           String workStartTime,
                           String workEndTime,
                           String workProvince,
                           String workCity,
                           String workArea,
                           String workJing,
                           String workWei,
                           String workPlace,
                           String workDoorplate,
                           String workLable,
                           String workContent,
                           String workCost,
                           String workTotalCost,
                           String peopleNum,
                           String startProvince,
                           String startCity,
                           String startArea,
                           String startJing,
                           String startWei,
                           String startPlace,
                           String startDoorplate,
                           String ifSingle,
                           String reservationTime,
                           String moneyType,
                           String cashCouponId//卡卷 Id
    ) {
        requestType = SENDORDERS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("sendPeople", TextUtils.isEmpty(sendPeople) ? "未知" : sendPeople);
        map.put("workType", workType);
        map.put("workStartTime", workStartTime);
        map.put("workEndTime", workEndTime);
        map.put("workProvince", workProvince);
        map.put("workCity", workCity);
        map.put("workArea", workArea);
        map.put("workJing", workJing);
        map.put("workWei", workWei);
        map.put("workPlace", workPlace);
        map.put("workDoorplate", workDoorplate);
        map.put("workLable", workLable);
        map.put("workContent", workContent);
        map.put("workCost", workCost);
        map.put("workTotalCost", workTotalCost);
        map.put("peopleNum", peopleNum);
        map.put("ifSingle", ifSingle);//是否联单  0  1
        if (!TextUtils.isEmpty(startProvince)) {
            map.put("startProvince", startProvince);
        } else {
            map.put("startProvince", workProvince);
        }
        if (!TextUtils.isEmpty(startCity)) {
            map.put("startCity", startCity);
        } else {
            map.put("startCity", workCity);
        }
        if (!TextUtils.isEmpty(startArea)) {
            map.put("startArea", startArea);
        } else {
            map.put("startArea", workArea);
        }
        if (!TextUtils.isEmpty(startJing)) {
            map.put("startJing", startJing);
        } else {
            map.put("startJing", workJing);
        }
        if (!TextUtils.isEmpty(startWei)) {
            map.put("startWei", startWei);
        } else {
            map.put("startWei", workWei);
        }
        if (!TextUtils.isEmpty(startPlace)) {
            map.put("startPlace", startPlace);
        } else {
            map.put("startPlace", workPlace);
        }
        if (!TextUtils.isEmpty(startDoorplate)) {
            map.put("startDoorplate", startDoorplate);
        } else {
            map.put("startDoorplate", workDoorplate);
        }
        if (!reservationTime.equals("0")) {
            map.put("reservationTime", reservationTime);
        }
        map.put("moneyType", moneyType);
        map.put("cashCouponId", cashCouponId);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SENDORDERS, map, callback);
    }

    /**
     * 发布订单 (同城配送)
     *
     * @param phone
     * @param sendPeople
     * @param workType
     * @param workStartTime
     * @param workEndTime
     * @param workProvince
     * @param workCity
     * @param workArea
     * @param workJing
     * @param workWei
     * @param workPlace
     * @param workLable
     * @param workContent
     * @param workCost
     * @param workTotalCost
     * @param peopleNum
     */
    public void SendOrders1(String phone,
                            String sendPeople,
                            String workType,
                            String workStartTime,
                            String workEndTime,
                            String workProvince,
                            String workCity,
                            String workArea,
                            String workJing,
                            String workWei,
                            String workPlace,
                            String workDoorplate,
                            String workLable,
                            String workContent,
                            String workCost,
                            String workTotalCost,
                            String peopleNum,
                            String startProvince,
                            String startCity,
                            String startArea,
                            String startJing,
                            String startWei,
                            String startPlace,
                            String startDoorplate,
                            String ifSingle,
                            String reservationTime,
                            String sendPeopleName,
                            String sendPeoplePhone,
                            String receiptPeopleName,
                            String receiptPeoplePhone,
                            String moneyType,
                            String cashCouponId,//卡卷 Id
                            String workDistance,//做工距离
                            String wmOrderId
    ) {
        requestType = SENDORDERS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("sendPeople", TextUtils.isEmpty(sendPeople) ? "未知" : sendPeople);
        map.put("workType", workType);
        map.put("workStartTime", workStartTime);
        map.put("workEndTime", workEndTime);
        map.put("workProvince", workProvince);
        map.put("workCity", workCity);
        map.put("workArea", workArea);
        map.put("workJing", workJing);
        map.put("workWei", workWei);
        map.put("workPlace", workPlace);
        map.put("workDoorplate", workDoorplate);
        map.put("workLable", workLable);
        map.put("workContent", workContent);
        map.put("workCost", workCost);
        map.put("workTotalCost", workTotalCost);
        map.put("peopleNum", peopleNum);
        map.put("ifSingle", ifSingle);//是否联单  0  1
        if (!TextUtils.isEmpty(startProvince)) {
            map.put("startProvince", startProvince);
        } else {
            map.put("startProvince", workProvince);
        }
        if (!TextUtils.isEmpty(startCity)) {
            map.put("startCity", startCity);
        } else {
            map.put("startCity", workCity);
        }
        if (!TextUtils.isEmpty(startArea)) {
            map.put("startArea", startArea);
        } else {
            map.put("startArea", workArea);
        }
        if (!TextUtils.isEmpty(startJing)) {
            map.put("startJing", startJing);
        } else {
            map.put("startJing", workJing);
        }
        if (!TextUtils.isEmpty(startWei)) {
            map.put("startWei", startWei);
        } else {
            map.put("startWei", workWei);
        }
        if (!TextUtils.isEmpty(startPlace)) {
            map.put("startPlace", startPlace);
        } else {
            map.put("startPlace", workPlace);
        }
        if (!TextUtils.isEmpty(startDoorplate)) {
            map.put("startDoorplate", startDoorplate);
        } else {
            map.put("startDoorplate", workDoorplate);
        }
        if (!reservationTime.equals("0")) {
            map.put("reservationTime", reservationTime);
        }
        map.put("sendPeopleName", sendPeopleName);
        map.put("sendPeoplePhone", sendPeoplePhone);
        map.put("receiptPeopleName", receiptPeopleName);
        map.put("receiptPeoplePhone", receiptPeoplePhone);
        map.put("moneyType", moneyType);
        map.put("cashCouponId", cashCouponId);
        map.put("workDistance", workDistance);
        map.put("token", MaiLiApplication.Token);
        if (!TextUtils.isEmpty(wmOrderId)) {
            map.put("wmOrderId",wmOrderId);
        }

        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.SENDORDERS, map, callback);
    }

    /**
     * 提交订单
     *
     * @param order
     * @param acceptPhone
     * @param acceptPeople
     * @param costNum
     * @param costTotalNum
     * @param confirOrder
     */
    public void CommitOrders(
            String order,
            String acceptPhone,
            String acceptPeople,
            String costNum,
            String costTotalNum,
            String confirOrder

    ) {
        requestType = COMMITORDERS;
        Map<String, String> map = new HashMap<String, String>();
        map.put("order", order);
        map.put("acceptPhone", acceptPhone);
        map.put("acceptPeople", TextUtils.isEmpty(acceptPeople) ? "未知" : acceptPeople);
        map.put("costNum", costNum);
        map.put("costTotalNum", costTotalNum);
        map.put("confirOrder", confirOrder);// confirOrder确认单，0不用确认，1需要确认，比如家教
        map.put("token", MaiLiApplication.Token);
        map.put("phone", MaiLiApplication.getInstance().getUserInfo().getPhone());
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.COMMITORDERS, map, callback);
    }

    public void getPrice(
            String phone,
            String area,
            String long1,
            String lat1,
            String long2,
            String lat2,
            String weight
    )
    /**
     * 时间计算价格
     */
    {
        requestType = COUNTPRICE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("type", "0");
        map.put("area", area);
        map.put("long1", long1);
        map.put("lat1", lat1);
        map.put("long2", long2);
        map.put("lat2", lat2);
        map.put("weight", weight);
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.COUNTPRICE, map, callback);
    }

    public void getPrice2(
            String phone,
            String area,
            String jing,
            String wei,
            String jingList,
            String weiList,
            String weight
    )
    /**
     * 地点计算价格
     */
    {
        requestType = COUNTPRICE;
        Map<String, String> map = new HashMap<String, String>();
        map.put("phone", phone);
        map.put("area", area);
        map.put("jing", jing);
        map.put("wei", wei);
        map.put("jingList", jingList);
        map.put("weiList", weiList);
        map.put("weight", weight);
        map.put("type", "1");
        map.put("token", MaiLiApplication.Token);
        Api.getInstance(MaiLiApplication.getInstance()).getData(Link.COUNTPRICE, map, callback);
    }

    Api.CustomHttpHandler callback = new Api.CustomHttpHandler() {

        @Override
        public void onFailure(HttpErrorModel errorModel) {
            mListener.onLoadComplete(errorModel);
        }

        @Override
        public void onSuccess(Object object) {
            switch (requestType) {
                case SENDORDERS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            mListener.onLoadComplete(modle, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        mListener.onLoadComplete(HttpErrorModel.createError());
                    }
                    break;
                case COUNTPRICE:
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
                case COMMITORDERS:
                    try {
                        if (object instanceof HttpErrorModel) {
                            mListener.onLoadComplete((HttpErrorModel) object);
                        } else {
                            HttpSuccessModel modle = ParseJsonUtils.getBean((String) object, HttpSuccessModel.class);
                            EventBus.getDefault().post(new UpdateNotice());
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
