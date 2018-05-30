package com.gsy.ml.model.main;

import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 */

public class BanlanceModel {


    /**
     * status : 200
     * data : {"count":2,"money":64,"acceptOrdersList":[{"id":28,"order":"183978457021493867942530","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1493867952208,"workType":"2","workLevel":"高级","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":null,"startWei":null,"startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.098517","workWei":"30.301988","workPlace":"古墩路606-2号","workContent":"二年级^|奥数^|2^|30^|女^|考虑考虑^|考虑考虑","costType":null,"costNum":64,"costTotalNum":64,"orderFinishTime":1493867973933,"orderStatus":2,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null}],"teamBonus":0,"sendOrdersList":[{"id":58,"order":"151701937261493867873269","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1493867873269,"workType":"2","workLevel":"高级","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":null,"startWei":null,"startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096606","workWei":"30.302239","workPlace":"古墩路673号","workLable":"","workContent":"高三^|物理^|2^|10^|不限^|啥特殊要求^|什么情况","workCost":60,"workTotalCost":60,"peopleNum":1,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":1493867889818,"orderStatus":2,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null}],"totalMoney":9999956.4}
     * info : 查询成功
     */

    private String status;
    private DataBean data;
    private String info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * count : 2
         * money : 64
         * acceptOrdersList : [{"id":28,"order":"183978457021493867942530","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1493867952208,"workType":"2","workLevel":"高级","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":null,"startWei":null,"startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.098517","workWei":"30.301988","workPlace":"古墩路606-2号","workContent":"二年级^|奥数^|2^|30^|女^|考虑考虑^|考虑考虑","costType":null,"costNum":64,"costTotalNum":64,"orderFinishTime":1493867973933,"orderStatus":2,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null}]
         * teamBonus : 0
         * sendOrdersList : [{"id":58,"order":"151701937261493867873269","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1493867873269,"workType":"2","workLevel":"高级","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":null,"startWei":null,"startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096606","workWei":"30.302239","workPlace":"古墩路673号","workLable":"","workContent":"高三^|物理^|2^|10^|不限^|啥特殊要求^|什么情况","workCost":60,"workTotalCost":60,"peopleNum":1,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":1493867889818,"orderStatus":2,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null}]
         * totalMoney : 9999956.4
         */

        private int count;
        private int money;
        private int teamBonus;
        private double totalMoney;
        private List<AcceptOrdersListBean> acceptOrdersList;
        private List<SendOrdersListBean> sendOrdersList;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getMoney() {
            return money;
        }

        public void setMoney(int money) {
            this.money = money;
        }

        public int getTeamBonus() {
            return teamBonus;
        }

        public void setTeamBonus(int teamBonus) {
            this.teamBonus = teamBonus;
        }

        public double getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(double totalMoney) {
            this.totalMoney = totalMoney;
        }

        public List<AcceptOrdersListBean> getAcceptOrdersList() {
            return acceptOrdersList;
        }

        public void setAcceptOrdersList(List<AcceptOrdersListBean> acceptOrdersList) {
            this.acceptOrdersList = acceptOrdersList;
        }

        public List<SendOrdersListBean> getSendOrdersList() {
            return sendOrdersList;
        }

        public void setSendOrdersList(List<SendOrdersListBean> sendOrdersList) {
            this.sendOrdersList = sendOrdersList;
        }

        public static class AcceptOrdersListBean {
            /**
             * id : 28
             * order : 183978457021493867942530
             * acceptPhone : 15170193726
             * acceptPeople : 黎明
             * acceptTime : 1493867952208
             * workType : 2
             * workLevel : 高级
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : null
             * startCity : null
             * startArea : null
             * startJing : null
             * startWei : null
             * startPlace : null
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 西湖区
             * workJing : 120.098517
             * workWei : 30.301988
             * workPlace : 古墩路606-2号
             * workContent : 二年级^|奥数^|2^|30^|女^|考虑考虑^|考虑考虑
             * costType : null
             * costNum : 64
             * costTotalNum : 64
             * orderFinishTime : 1493867973933
             * orderStatus : 2
             * deliveryTimeLength : null
             * integralStatus : null
             * evaluate : 0
             * evaluateContent : null
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
            private Object startProvince;
            private Object startCity;
            private Object startArea;
            private Object startJing;
            private Object startWei;
            private Object startPlace;
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
            private long orderFinishTime;
            private int orderStatus;
            private Object deliveryTimeLength;
            private Object integralStatus;
            private int evaluate;
            private Object evaluateContent;

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

            public Object getStartProvince() {
                return startProvince;
            }

            public void setStartProvince(Object startProvince) {
                this.startProvince = startProvince;
            }

            public Object getStartCity() {
                return startCity;
            }

            public void setStartCity(Object startCity) {
                this.startCity = startCity;
            }

            public Object getStartArea() {
                return startArea;
            }

            public void setStartArea(Object startArea) {
                this.startArea = startArea;
            }

            public Object getStartJing() {
                return startJing;
            }

            public void setStartJing(Object startJing) {
                this.startJing = startJing;
            }

            public Object getStartWei() {
                return startWei;
            }

            public void setStartWei(Object startWei) {
                this.startWei = startWei;
            }

            public Object getStartPlace() {
                return startPlace;
            }

            public void setStartPlace(Object startPlace) {
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

            public long getOrderFinishTime() {
                return orderFinishTime;
            }

            public void setOrderFinishTime(long orderFinishTime) {
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
        }

        public static class SendOrdersListBean {
            /**
             * id : 58
             * order : 151701937261493867873269
             * sendPhone : 15170193726
             * sendPeople : 黎明
             * sendTime : 1493867873269
             * workType : 2
             * workLevel : 高级
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : null
             * startCity : null
             * startArea : null
             * startJing : null
             * startWei : null
             * startPlace : null
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 西湖区
             * workJing : 120.096606
             * workWei : 30.302239
             * workPlace : 古墩路673号
             * workLable :
             * workContent : 高三^|物理^|2^|10^|不限^|啥特殊要求^|什么情况
             * workCost : 60
             * workTotalCost : 60
             * peopleNum : 1
             * acceptPeopleNum : 1
             * feesType : null
             * orderFinishTime : 1493867889818
             * orderStatus : 2
             * integralStatus : 0
             * deliveryTimeLength : null
             * evaluate : 0
             * evaluateContent : null
             */

            private int id;
            private String order;
            private String sendPhone;
            private String sendPeople;
            private long sendTime;
            private String workType;
            private String workLevel;
            private int workStartTime;
            private int workEndTime;
            private Object startProvince;
            private Object startCity;
            private Object startArea;
            private Object startJing;
            private Object startWei;
            private Object startPlace;
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
            private long orderFinishTime;
            private int orderStatus;
            private int integralStatus;
            private Object deliveryTimeLength;
            private int evaluate;
            private Object evaluateContent;

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

            public Object getStartProvince() {
                return startProvince;
            }

            public void setStartProvince(Object startProvince) {
                this.startProvince = startProvince;
            }

            public Object getStartCity() {
                return startCity;
            }

            public void setStartCity(Object startCity) {
                this.startCity = startCity;
            }

            public Object getStartArea() {
                return startArea;
            }

            public void setStartArea(Object startArea) {
                this.startArea = startArea;
            }

            public Object getStartJing() {
                return startJing;
            }

            public void setStartJing(Object startJing) {
                this.startJing = startJing;
            }

            public Object getStartWei() {
                return startWei;
            }

            public void setStartWei(Object startWei) {
                this.startWei = startWei;
            }

            public Object getStartPlace() {
                return startPlace;
            }

            public void setStartPlace(Object startPlace) {
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

            public long getOrderFinishTime() {
                return orderFinishTime;
            }

            public void setOrderFinishTime(long orderFinishTime) {
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
        }
    }
}
