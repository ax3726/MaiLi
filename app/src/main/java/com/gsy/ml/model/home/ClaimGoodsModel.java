package com.gsy.ml.model.home;

import java.util.List;

/**
 * Created by Administrator on 2017/5/12.
 */

public class ClaimGoodsModel {


    /**
     * data : {"sendOrders":{"id":8,"order":"183978457021494640482408","sendPhone":"18397845702","sendPeople":"啊Paul","sendTime":1494640482408,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.099022","startWei":"30.30222","startPlace":"同人精华古墩路616号阿卡丽","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.152418","workWei":"30.259634","workPlace":"蒋经国旧居石山南麓石函路6号32","workLable":"","workContent":"心合理叽叽叽叽","workCost":131,"workTotalCost":131,"peopleNum":1,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":null,"orderStatus":1,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":1494712800000,"sendPeopleName":"了绝交","sendPeoplePhone":"12345678912","acceptPeopleName":"uglily","acceptPeoplePhone":"84616581"},"acceptOrders":[{"id":3,"order":"183978457021494640482408","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494640531100,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.099022","startWei":"30.30222","startPlace":"同人精华古墩路616号阿卡丽","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.152418","workWei":"30.259634","workPlace":"蒋经国旧居石山南麓石函路6号32","workContent":"心合理叽叽叽叽","costType":null,"costNum":131,"costTotalNum":131,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":1494712800000,"sendPeopleName":"了绝交","sendPeoplePhone":"12345678912","acceptPeopleName":"uglily","acceptPeoplePhone":"84616581","pickup":0}]}
     * status : 200
     * info : 查询成功
     */

    private DataBean data;
    private String status;
    private String info;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * sendOrders : {"id":8,"order":"183978457021494640482408","sendPhone":"18397845702","sendPeople":"啊Paul","sendTime":1494640482408,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.099022","startWei":"30.30222","startPlace":"同人精华古墩路616号阿卡丽","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.152418","workWei":"30.259634","workPlace":"蒋经国旧居石山南麓石函路6号32","workLable":"","workContent":"心合理叽叽叽叽","workCost":131,"workTotalCost":131,"peopleNum":1,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":null,"orderStatus":1,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":1494712800000,"sendPeopleName":"了绝交","sendPeoplePhone":"12345678912","acceptPeopleName":"uglily","acceptPeoplePhone":"84616581"}
         * acceptOrders : [{"id":3,"order":"183978457021494640482408","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494640531100,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.099022","startWei":"30.30222","startPlace":"同人精华古墩路616号阿卡丽","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.152418","workWei":"30.259634","workPlace":"蒋经国旧居石山南麓石函路6号32","workContent":"心合理叽叽叽叽","costType":null,"costNum":131,"costTotalNum":131,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":1494712800000,"sendPeopleName":"了绝交","sendPeoplePhone":"12345678912","acceptPeopleName":"uglily","acceptPeoplePhone":"84616581","pickup":0}]
         */

        private SendOrdersBean sendOrders;
        private long pickupTime;//取件时长
        private List<AcceptOrdersBean> acceptOrders;

        public SendOrdersBean getSendOrders() {
            return sendOrders;
        }

        public void setSendOrders(SendOrdersBean sendOrders) {
            this.sendOrders = sendOrders;
        }

        public List<AcceptOrdersBean> getAcceptOrders() {
            return acceptOrders;
        }

        public void setAcceptOrders(List<AcceptOrdersBean> acceptOrders) {
            this.acceptOrders = acceptOrders;
        }

        public long getPickupTime() {
            return pickupTime;
        }

        public void setPickupTime(long pickupTime) {
            this.pickupTime = pickupTime;
        }

        public static class SendOrdersBean {
            /**
             * id : 8
             * order : 183978457021494640482408
             * sendPhone : 18397845702
             * sendPeople : 啊Paul
             * sendTime : 1494640482408
             * workType : 1
             * workLevel : 普通
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : 浙江省
             * startCity : 杭州市
             * startArea : 西湖区
             * startJing : 120.099022
             * startWei : 30.30222
             * startPlace : 同人精华古墩路616号阿卡丽
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 西湖区
             * workJing : 120.152418
             * workWei : 30.259634
             * workPlace : 蒋经国旧居石山南麓石函路6号32
             * workLable :
             * workContent : 心合理叽叽叽叽
             * workCost : 131
             * workTotalCost : 131
             * peopleNum : 1
             * acceptPeopleNum : 1
             * feesType : null
             * orderFinishTime : null
             * orderStatus : 1
             * integralStatus : 0
             * deliveryTimeLength : null
             * evaluate : 0
             * evaluateContent : null
             * ifSingle : 0
             * reservationTime : 1494712800000
             * sendPeopleName : 了绝交
             * sendPeoplePhone : 12345678912
             * acceptPeopleName : uglily
             * acceptPeoplePhone : 84616581
             */

            private int id;
            private String order;
            private String sendPhone;
            private String sendPeople;
            private long sendTime;
            private String workType;
            private String workLevel;
            private long workStartTime;
            private long workEndTime;
            private String startProvince;
            private String startCity;
            private String startArea;
            private String startJing;
            private String startWei;
            private String startPlace;
            private String workProvince;
            private String workCity;
            private String workArea;
            private String workJing;
            private String workWei;
            private String workPlace;
            private String workLable;
            private String workContent;
            private int workCost;
            private int workTotalCost;
            private int peopleNum;
            private int acceptPeopleNum;
            private Object feesType;
            private Object orderFinishTime;
            private int orderStatus;
            private int integralStatus;
            private Object deliveryTimeLength;
            private int evaluate;
            private Object evaluateContent;
            private int ifSingle;
            private long reservationTime;
            private String sendPeopleName;
            private String sendPeoplePhone;
            private String receiptPeopleName;
            private String receiptPeoplePhone;
            private String startDoorplate;
            private String workDoorplate;

            public String getStartDoorplate() {
                return startDoorplate;
            }

            public void setStartDoorplate(String startDoorplate) {
                this.startDoorplate = startDoorplate;
            }

            public String getWorkDoorplate() {
                return workDoorplate;
            }

            public void setWorkDoorplate(String workDoorplate) {
                this.workDoorplate = workDoorplate;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrder() {
                return order;
            }

            public void setOrder(String order) {
                this.order = order;
            }

            public String getSendPhone() {
                return sendPhone;
            }

            public void setSendPhone(String sendPhone) {
                this.sendPhone = sendPhone;
            }

            public String getSendPeople() {
                return sendPeople;
            }

            public void setSendPeople(String sendPeople) {
                this.sendPeople = sendPeople;
            }

            public long getSendTime() {
                return sendTime;
            }

            public void setSendTime(long sendTime) {
                this.sendTime = sendTime;
            }

            public String getWorkType() {
                return workType;
            }

            public void setWorkType(String workType) {
                this.workType = workType;
            }

            public String getWorkLevel() {
                return workLevel;
            }

            public void setWorkLevel(String workLevel) {
                this.workLevel = workLevel;
            }

            public long getWorkStartTime() {
                return workStartTime;
            }

            public void setWorkStartTime(long workStartTime) {
                this.workStartTime = workStartTime;
            }

            public long getWorkEndTime() {
                return workEndTime;
            }

            public void setWorkEndTime(long workEndTime) {
                this.workEndTime = workEndTime;
            }

            public String getStartProvince() {
                return startProvince;
            }

            public void setStartProvince(String startProvince) {
                this.startProvince = startProvince;
            }

            public String getStartCity() {
                return startCity;
            }

            public void setStartCity(String startCity) {
                this.startCity = startCity;
            }

            public String getStartArea() {
                return startArea;
            }

            public void setStartArea(String startArea) {
                this.startArea = startArea;
            }

            public String getStartJing() {
                return startJing;
            }

            public void setStartJing(String startJing) {
                this.startJing = startJing;
            }

            public String getStartWei() {
                return startWei;
            }

            public void setStartWei(String startWei) {
                this.startWei = startWei;
            }

            public String getStartPlace() {
                return startPlace;
            }

            public void setStartPlace(String startPlace) {
                this.startPlace = startPlace;
            }

            public String getWorkProvince() {
                return workProvince;
            }

            public void setWorkProvince(String workProvince) {
                this.workProvince = workProvince;
            }

            public String getWorkCity() {
                return workCity;
            }

            public void setWorkCity(String workCity) {
                this.workCity = workCity;
            }

            public String getWorkArea() {
                return workArea;
            }

            public void setWorkArea(String workArea) {
                this.workArea = workArea;
            }

            public String getWorkJing() {
                return workJing;
            }

            public void setWorkJing(String workJing) {
                this.workJing = workJing;
            }

            public String getWorkWei() {
                return workWei;
            }

            public void setWorkWei(String workWei) {
                this.workWei = workWei;
            }

            public String getWorkPlace() {
                return workPlace;
            }

            public void setWorkPlace(String workPlace) {
                this.workPlace = workPlace;
            }

            public String getWorkLable() {
                return workLable;
            }

            public void setWorkLable(String workLable) {
                this.workLable = workLable;
            }

            public String getWorkContent() {
                return workContent;
            }

            public void setWorkContent(String workContent) {
                this.workContent = workContent;
            }

            public int getWorkCost() {
                return workCost;
            }

            public void setWorkCost(int workCost) {
                this.workCost = workCost;
            }

            public int getWorkTotalCost() {
                return workTotalCost;
            }

            public void setWorkTotalCost(int workTotalCost) {
                this.workTotalCost = workTotalCost;
            }

            public int getPeopleNum() {
                return peopleNum;
            }

            public void setPeopleNum(int peopleNum) {
                this.peopleNum = peopleNum;
            }

            public int getAcceptPeopleNum() {
                return acceptPeopleNum;
            }

            public void setAcceptPeopleNum(int acceptPeopleNum) {
                this.acceptPeopleNum = acceptPeopleNum;
            }

            public Object getFeesType() {
                return feesType;
            }

            public void setFeesType(Object feesType) {
                this.feesType = feesType;
            }

            public Object getOrderFinishTime() {
                return orderFinishTime;
            }

            public void setOrderFinishTime(Object orderFinishTime) {
                this.orderFinishTime = orderFinishTime;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public int getIntegralStatus() {
                return integralStatus;
            }

            public void setIntegralStatus(int integralStatus) {
                this.integralStatus = integralStatus;
            }

            public Object getDeliveryTimeLength() {
                return deliveryTimeLength;
            }

            public void setDeliveryTimeLength(Object deliveryTimeLength) {
                this.deliveryTimeLength = deliveryTimeLength;
            }

            public int getEvaluate() {
                return evaluate;
            }

            public void setEvaluate(int evaluate) {
                this.evaluate = evaluate;
            }

            public Object getEvaluateContent() {
                return evaluateContent;
            }

            public void setEvaluateContent(Object evaluateContent) {
                this.evaluateContent = evaluateContent;
            }

            public int getIfSingle() {
                return ifSingle;
            }

            public void setIfSingle(int ifSingle) {
                this.ifSingle = ifSingle;
            }

            public long getReservationTime() {
                return reservationTime;
            }

            public void setReservationTime(long reservationTime) {
                this.reservationTime = reservationTime;
            }

            public String getSendPeopleName() {
                return sendPeopleName;
            }

            public void setSendPeopleName(String sendPeopleName) {
                this.sendPeopleName = sendPeopleName;
            }

            public String getSendPeoplePhone() {
                return sendPeoplePhone;
            }

            public void setSendPeoplePhone(String sendPeoplePhone) {
                this.sendPeoplePhone = sendPeoplePhone;
            }

            public String getReceiptPeopleName() {
                return receiptPeopleName;
            }

            public void setReceiptPeopleName(String receiptPeopleName) {
                this.receiptPeopleName = receiptPeopleName;
            }

            public String getReceiptPeoplePhone() {
                return receiptPeoplePhone;
            }

            public void setReceiptPeoplePhone(String receiptPeoplePhone) {
                this.receiptPeoplePhone = receiptPeoplePhone;
            }
        }

        public static class AcceptOrdersBean {
            /**
             * id : 3
             * order : 183978457021494640482408
             * acceptPhone : 15170193726
             * acceptPeople : 黎明
             * acceptTime : 1494640531100
             * workType : 1
             * workLevel : 普通
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : 浙江省
             * startCity : 杭州市
             * startArea : 西湖区
             * startJing : 120.099022
             * startWei : 30.30222
             * startPlace : 同人精华古墩路616号阿卡丽
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 西湖区
             * workJing : 120.152418
             * workWei : 30.259634
             * workPlace : 蒋经国旧居石山南麓石函路6号32
             * workContent : 心合理叽叽叽叽
             * costType : null
             * costNum : 131
             * costTotalNum : 131
             * orderFinishTime : null
             * orderStatus : 0
             * deliveryTimeLength : null
             * integralStatus : null
             * evaluate : 0
             * evaluateContent : null
             * ifSingle : 0
             * reservationTime : 1494712800000
             * sendPeopleName : 了绝交
             * sendPeoplePhone : 12345678912
             * acceptPeopleName : uglily
             * acceptPeoplePhone : 84616581
             * pickup : 0
             */

            private int id;
            private String order;
            private String acceptPhone;
            private String acceptPeople;
            private long acceptTime;
            private String workType;
            private String workLevel;
            private int workStartTime;
            private int workEndTime;
            private String startProvince;
            private String startCity;
            private String startArea;
            private String startJing;
            private String startWei;
            private String startPlace;
            private String workProvince;
            private String workCity;
            private String workArea;
            private String workJing;
            private String workWei;
            private String workPlace;
            private String workContent;
            private Object costType;
            private int costNum;
            private int costTotalNum;
            private Object orderFinishTime;
            private int orderStatus;
            private Object deliveryTimeLength;
            private Object integralStatus;
            private int evaluate;
            private Object evaluateContent;
            private int ifSingle;
            private long reservationTime;
            private String sendPeopleName;
            private String sendPeoplePhone;
            private String acceptPeopleName;
            private String acceptPeoplePhone;
            private int pickup;
            private long pickupTime;

            public long getPickup_time() {
                return pickupTime;
            }

            public void setPickup_time(long pickup_time) {
                this.pickupTime = pickup_time;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrder() {
                return order;
            }

            public void setOrder(String order) {
                this.order = order;
            }

            public String getAcceptPhone() {
                return acceptPhone;
            }

            public void setAcceptPhone(String acceptPhone) {
                this.acceptPhone = acceptPhone;
            }

            public String getAcceptPeople() {
                return acceptPeople;
            }

            public void setAcceptPeople(String acceptPeople) {
                this.acceptPeople = acceptPeople;
            }

            public long getAcceptTime() {
                return acceptTime;
            }

            public void setAcceptTime(long acceptTime) {
                this.acceptTime = acceptTime;
            }

            public String getWorkType() {
                return workType;
            }

            public void setWorkType(String workType) {
                this.workType = workType;
            }

            public String getWorkLevel() {
                return workLevel;
            }

            public void setWorkLevel(String workLevel) {
                this.workLevel = workLevel;
            }

            public int getWorkStartTime() {
                return workStartTime;
            }

            public void setWorkStartTime(int workStartTime) {
                this.workStartTime = workStartTime;
            }

            public int getWorkEndTime() {
                return workEndTime;
            }

            public void setWorkEndTime(int workEndTime) {
                this.workEndTime = workEndTime;
            }

            public String getStartProvince() {
                return startProvince;
            }

            public void setStartProvince(String startProvince) {
                this.startProvince = startProvince;
            }

            public String getStartCity() {
                return startCity;
            }

            public void setStartCity(String startCity) {
                this.startCity = startCity;
            }

            public String getStartArea() {
                return startArea;
            }

            public void setStartArea(String startArea) {
                this.startArea = startArea;
            }

            public String getStartJing() {
                return startJing;
            }

            public void setStartJing(String startJing) {
                this.startJing = startJing;
            }

            public String getStartWei() {
                return startWei;
            }

            public void setStartWei(String startWei) {
                this.startWei = startWei;
            }

            public String getStartPlace() {
                return startPlace;
            }

            public void setStartPlace(String startPlace) {
                this.startPlace = startPlace;
            }

            public String getWorkProvince() {
                return workProvince;
            }

            public void setWorkProvince(String workProvince) {
                this.workProvince = workProvince;
            }

            public String getWorkCity() {
                return workCity;
            }

            public void setWorkCity(String workCity) {
                this.workCity = workCity;
            }

            public String getWorkArea() {
                return workArea;
            }

            public void setWorkArea(String workArea) {
                this.workArea = workArea;
            }

            public String getWorkJing() {
                return workJing;
            }

            public void setWorkJing(String workJing) {
                this.workJing = workJing;
            }

            public String getWorkWei() {
                return workWei;
            }

            public void setWorkWei(String workWei) {
                this.workWei = workWei;
            }

            public String getWorkPlace() {
                return workPlace;
            }

            public void setWorkPlace(String workPlace) {
                this.workPlace = workPlace;
            }

            public String getWorkContent() {
                return workContent;
            }

            public void setWorkContent(String workContent) {
                this.workContent = workContent;
            }

            public Object getCostType() {
                return costType;
            }

            public void setCostType(Object costType) {
                this.costType = costType;
            }

            public int getCostNum() {
                return costNum;
            }

            public void setCostNum(int costNum) {
                this.costNum = costNum;
            }

            public int getCostTotalNum() {
                return costTotalNum;
            }

            public void setCostTotalNum(int costTotalNum) {
                this.costTotalNum = costTotalNum;
            }

            public Object getOrderFinishTime() {
                return orderFinishTime;
            }

            public void setOrderFinishTime(Object orderFinishTime) {
                this.orderFinishTime = orderFinishTime;
            }

            public int getOrderStatus() {
                return orderStatus;
            }

            public void setOrderStatus(int orderStatus) {
                this.orderStatus = orderStatus;
            }

            public Object getDeliveryTimeLength() {
                return deliveryTimeLength;
            }

            public void setDeliveryTimeLength(Object deliveryTimeLength) {
                this.deliveryTimeLength = deliveryTimeLength;
            }

            public Object getIntegralStatus() {
                return integralStatus;
            }

            public void setIntegralStatus(Object integralStatus) {
                this.integralStatus = integralStatus;
            }

            public int getEvaluate() {
                return evaluate;
            }

            public void setEvaluate(int evaluate) {
                this.evaluate = evaluate;
            }

            public Object getEvaluateContent() {
                return evaluateContent;
            }

            public void setEvaluateContent(Object evaluateContent) {
                this.evaluateContent = evaluateContent;
            }

            public int getIfSingle() {
                return ifSingle;
            }

            public void setIfSingle(int ifSingle) {
                this.ifSingle = ifSingle;
            }

            public long getReservationTime() {
                return reservationTime;
            }

            public void setReservationTime(long reservationTime) {
                this.reservationTime = reservationTime;
            }

            public String getSendPeopleName() {
                return sendPeopleName;
            }

            public void setSendPeopleName(String sendPeopleName) {
                this.sendPeopleName = sendPeopleName;
            }

            public String getSendPeoplePhone() {
                return sendPeoplePhone;
            }

            public void setSendPeoplePhone(String sendPeoplePhone) {
                this.sendPeoplePhone = sendPeoplePhone;
            }

            public String getAcceptPeopleName() {
                return acceptPeopleName;
            }

            public void setAcceptPeopleName(String acceptPeopleName) {
                this.acceptPeopleName = acceptPeopleName;
            }

            public String getAcceptPeoplePhone() {
                return acceptPeoplePhone;
            }

            public void setAcceptPeoplePhone(String acceptPeoplePhone) {
                this.acceptPeoplePhone = acceptPeoplePhone;
            }

            public int getPickup() {
                return pickup;
            }

            public void setPickup(int pickup) {
                this.pickup = pickup;
            }
        }
    }
}
