package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28.
 */

public class MineBillingModel {


    /**
     * status : 200
     * data : [{"id":6,"order":"151701937261494140262637","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494140262637,"workType":"8","workLevel":"普通","workStartTime":1494248700000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094828","startWei":"30.297811","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094828","workWei":"30.297811","workPlace":"古墩路663号","workLable":"","workContent":"1494248700000^|小时^|3.5^|不限^|负责传单的派发，","workCost":54,"workTotalCost":54,"peopleNum":10,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":null},{"id":7,"order":"151701937261494140374580","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494140374580,"workType":"8","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094828","startWei":"30.297811","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094828","workWei":"30.297811","workPlace":"紫荆花路216号","workLable":"","workContent":"0^|小时^|3.6^|女^|负责传单派发   啦啦JJKKK","workCost":40,"workTotalCost":40,"peopleNum":12,"acceptPeopleNum":2,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":null},{"id":8,"order":"151701937261494142684909","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494142684909,"workType":"8","workLevel":"普通","workStartTime":1494371040000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094894","startWei":"30.30112","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094894","workWei":"30.30112","workPlace":"余杭塘路552号","workLable":"","workContent":"1494371040000^|小时^|2.3^|女^|负责传单的派发","workCost":72,"workTotalCost":72,"peopleNum":12,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":null},{"id":10,"order":"151701937261494142882922","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494142882922,"workType":"8","workLevel":"普通","workStartTime":1494285000000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.098345","startWei":"30.301798","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"拱墅区","workJing":"120.098345","workWei":"30.301798","workPlace":"古墩路604号","workLable":"","workContent":"1494285000000^|小时^|263^|女^|啦咯啦咯啦咯","workCost":28,"workTotalCost":28,"peopleNum":10,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":null},{"id":11,"order":"151701937261494143298958","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494143298958,"workType":"8","workLevel":"普通","workStartTime":1494284760000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.086634","startWei":"30.303638","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.086634","workWei":"30.303638","workPlace":"紫荆花路377号","workLable":"","workContent":"1494284760000^|小时^|5^|女^|传单派发","workCost":66,"workTotalCost":66,"peopleNum":10,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":null}]
     * info : 查询成功
     */

    private String status;
    private String info;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 6
         * order : 151701937261494140262637
         * sendPhone : 15170193726
         * sendPeople : 黎明
         * sendTime : 1494140262637
         * workType : 8
         * workLevel : 普通
         * workStartTime : 1494248700000
         * workEndTime : 0
         * startProvince : null
         * startCity : null
         * startArea : null
         * startJing : 120.094828
         * startWei : 30.297811
         * startPlace : null
         * workProvince : 浙江省
         * workCity : 杭州市
         * workArea : 西湖区
         * workJing : 120.094828
         * workWei : 30.297811
         * workPlace : 古墩路663号
         * workLable :
         * workContent : 1494248700000^|小时^|3.5^|不限^|负责传单的派发，
         * workCost : 54
         * workTotalCost : 54
         * peopleNum : 10
         * acceptPeopleNum : 0
         * feesType : null
         * orderFinishTime : null
         * orderStatus : 0
         * integralStatus : 0
         * deliveryTimeLength : null
         * evaluate : 0
         * evaluateContent : null
         * ifSingle : 0
         * reservationTime : 0
         * juli : null
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
        private String startDoorplate;
        private String workDoorplate;
        private String acceptPeopleName;
        private String acceptPeoplePhone;
        private String workLable;
        private String workContent;
        private double workCost;
        private double workTotalCost;
        private int peopleNum;
        private int acceptPeopleNum;
        private String feesType;
        private String orderFinishTime;
        private int orderStatus;
        private int integralStatus;
        private String deliveryTimeLength;
        private int evaluate;
        private String evaluateContent;
        private int ifSingle;
        private long reservationTime;
        private String juli;
        private String receiptPeopleName;
        private String receiptPeoplePhone;
        private int finishPeopleNum;
        private String doingPeoplePhone;//进行中的 人        15170193726黎明，18397845702李涛
        private String onLine;//进行中的人对应的状态

        public String getOnLine() {
            return onLine;
        }

        public void setOnLine(String onLine) {
            this.onLine = onLine;
        }

        public int getFinishPeopleNum() {
            return finishPeopleNum;
        }

        public void setFinishPeopleNum(int finishPeopleNum) {
            this.finishPeopleNum = finishPeopleNum;
        }

        public String getDoingPeoplePhone() {
            return doingPeoplePhone;
        }

        public void setDoingPeoplePhone(String doingPeoplePhone) {
            this.doingPeoplePhone = doingPeoplePhone;
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

        public double getWorkCost() {
            return workCost;
        }

        public void setWorkCost(double workCost) {
            this.workCost = workCost;
        }

        public double getWorkTotalCost() {
            return workTotalCost;
        }

        public void setWorkTotalCost(double workTotalCost) {
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


        public int getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(int evaluate) {
            this.evaluate = evaluate;
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

        public String getStartPlace() {
            return startPlace;
        }

        public void setStartPlace(String startPlace) {
            this.startPlace = startPlace;
        }

        public String getFeesType() {
            return feesType;
        }

        public void setFeesType(String feesType) {
            this.feesType = feesType;
        }

        public String getOrderFinishTime() {
            return orderFinishTime;
        }

        public void setOrderFinishTime(String orderFinishTime) {
            this.orderFinishTime = orderFinishTime;
        }

        public String getDeliveryTimeLength() {
            return deliveryTimeLength;
        }

        public void setDeliveryTimeLength(String deliveryTimeLength) {
            this.deliveryTimeLength = deliveryTimeLength;
        }

        public String getEvaluateContent() {
            return evaluateContent;
        }

        public void setEvaluateContent(String evaluateContent) {
            this.evaluateContent = evaluateContent;
        }

        public String getJuli() {
            return juli;
        }

        public void setJuli(String juli) {
            this.juli = juli;
        }
    }
}
