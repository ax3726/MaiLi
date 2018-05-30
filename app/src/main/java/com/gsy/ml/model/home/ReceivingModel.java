package com.gsy.ml.model.home;

import java.util.List;

/**
 * Created by Administrator on 2017/4/30.
 */

public class ReceivingModel {

    /**
     * status : 200
     * data : [{"id":11,"order":"151701937261494143298958","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494143298958,"workType":"8","workLevel":"普通","workStartTime":1494284760000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.086634","startWei":"30.303638","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.086634","workWei":"30.303638","workPlace":"紫荆花路377号","workLable":"","workContent":"1494284760000^|小时^|5^|女^|传单派发","workCost":66,"workTotalCost":66,"peopleNum":10,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":896},{"id":9,"order":"151701937261494142850550","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494142850550,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.096606","startWei":"30.302239","startPlace":"古墩路673号","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.098462","workWei":"30.302716","workPlace":"三墩镇古墩路628号味膳","workLable":"","workContent":"啦咯啦咯啦咯^|","workCost":79,"workTotalCost":79,"peopleNum":1,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":87},{"id":8,"order":"151701937261494142684909","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494142684909,"workType":"8","workLevel":"普通","workStartTime":1494371040000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094894","startWei":"30.30112","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094894","workWei":"30.30112","workPlace":"余杭塘路552号","workLable":"","workContent":"1494371040000^|小时^|2.3^|女^|负责传单的派发","workCost":72,"workTotalCost":72,"peopleNum":12,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":126},{"id":7,"order":"151701937261494140374580","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494140374580,"workType":"8","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094828","startWei":"30.297811","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094828","workWei":"30.297811","workPlace":"紫荆花路216号","workLable":"","workContent":"0^|小时^|3.6^|女^|负责传单派发   啦啦JJKKK","workCost":40,"workTotalCost":40,"peopleNum":12,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":472},{"id":6,"order":"151701937261494140262637","sendPhone":"15170193726","sendPeople":"黎明","sendTime":1494140262637,"workType":"8","workLevel":"普通","workStartTime":1494248700000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094828","startWei":"30.297811","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094828","workWei":"30.297811","workPlace":"古墩路663号","workLable":"","workContent":"1494248700000^|小时^|3.5^|不限^|负责传单的派发，","workCost":54,"workTotalCost":54,"peopleNum":10,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":472},{"id":5,"order":"137358036481494136162234","sendPhone":"13735803648","sendPeople":"洋洋洋","sendTime":1494136162234,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.094894","startWei":"30.30112","startPlace":"萍水西街206号","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096103","workWei":"30.29999","workPlace":"古墩路萍水西街173号","workLable":"","workContent":"默默^|","workCost":68,"workTotalCost":68,"peopleNum":1,"acceptPeopleNum":0,"feesType":null,"orderFinishTime":null,"orderStatus":0,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0,"juli":126}]
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
         * id : 11
         * order : 151701937261494143298958
         * sendPhone : 15170193726
         * sendPeople : 黎明
         * sendTime : 1494143298958
         * workType : 8
         * workLevel : 普通
         * workStartTime : 1494284760000
         * workEndTime : 0
         * startProvince : null
         * startCity : null
         * startArea : null
         * startJing : 120.086634
         * startWei : 30.303638
         * startPlace : null
         * workProvince : 浙江省
         * workCity : 杭州市
         * workArea : 西湖区
         * workJing : 120.086634
         * workWei : 30.303638
         * workPlace : 紫荆花路377号
         * workLable :
         * workContent : 1494284760000^|小时^|5^|女^|传单派发
         * workCost : 66.0
         * workTotalCost : 66.0
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
         * juli : 896
         */

        private int id;
        private String order;
        private String sendPhone;
        private String sendPeople;
        private long sendTime;
        private String workType;
        private String workLevel;
        private long workStartTime;
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
        private String startDoorplate;
        private String workDoorplate;
        private String workLable;
        private String workContent;
        private double workCost;
        private double workTotalCost;
        private int peopleNum;
        private int acceptPeopleNum;
        private Object feesType;
        private long orderFinishTime;
        private int orderStatus;
        private int integralStatus;
        private Object deliveryTimeLength;
        private int evaluate;
        private Object evaluateContent;
        private int ifSingle;
        private long reservationTime;
        private long juli;

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

        public long getJuli() {
            return juli;
        }

        public void setJuli(long juli) {
            this.juli = juli;
        }
    }
}
