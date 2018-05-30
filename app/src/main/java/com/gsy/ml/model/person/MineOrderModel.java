package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/4/27.
 */

public class MineOrderModel {


    /**
     * status : 200
     * data : [{"id":7,"order":"151701937261494142850550","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494144003118,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.096606","startWei":"30.302239","startPlace":"古墩路673号","workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.098462","workWei":"30.302716","workPlace":"三墩镇古墩路628号味膳","workContent":"啦咯啦咯啦咯^|","costType":null,"costNum":79,"costTotalNum":79,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":8,"order":"151701937261494140374580","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494144041187,"workType":"8","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.094828","startWei":"30.297811","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.094828","workWei":"30.297811","workPlace":"紫荆花路216号","workContent":"0^|小时^|3.6^|女^|负责传单派发   啦啦JJKKK","costType":null,"costNum":40,"costTotalNum":40,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":42,"order":"183978457021494213590880","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494213669241,"workType":"9","workLevel":"高级","workStartTime":0,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.098564","startWei":"30.302201","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.098564","workWei":"30.302201","workPlace":"三墩镇同人精华3号楼内","workContent":"^|2^|女^|商2招商服务中心^|2","costType":null,"costNum":60,"costTotalNum":60,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":51,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233417389,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":52,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233418578,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":53,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233423627,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":54,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233425375,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":55,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233425585,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":56,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233427577,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0},{"id":57,"order":"137358036481494233405372","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1494233428785,"workType":"17","workLevel":"高级","workStartTime":1494432360000,"workEndTime":0,"startProvince":null,"startCity":null,"startArea":null,"startJing":"120.096935","startWei":"30.299982","startPlace":null,"workProvince":"浙江省","workCity":"杭州市","workArea":"西湖区","workJing":"120.096935","workWei":"30.299982","workPlace":"萍水西街151-249号","workContent":"1494432360000^|ggggg","costType":null,"costNum":74,"costTotalNum":74,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":0,"reservationTime":0}]
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
         * : 7
         * order : 151701937261494142850550
         * acceptPhone : 15170193726
         * acceptPeople : 黎明
         * id * acceptTime : 1494144003118
         * workType : 1
         * workLevel : 普通
         * workStartTime : 0
         * workEndTime : 0
         * startProvince : 浙江省
         * startCity : 杭州市
         * startArea : 西湖区
         * startJing : 120.096606
         * startWei : 30.302239
         * startPlace : 古墩路673号
         * workProvince : 浙江省
         * workCity : 杭州市
         * workArea : 西湖区
         * workJing : 120.098462
         * workWei : 30.302716
         * workPlace : 三墩镇古墩路628号味膳
         * workContent : 啦咯啦咯啦咯^|
         * costType : null
         * costNum : 79.0
         * costTotalNum : 79.0
         * orderFinishTime : null
         * orderStatus : 0
         * deliveryTimeLength : null
         * integralStatus : null
         * evaluate : 0
         * evaluateContent : null
         * ifSingle : 0
         * reservationTime : 0
         */

        private int id;
        private String order;
        private String acceptPhone;
        private String acceptPeople;
        private long acceptTime;
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
        private String workContent;
        private String costType;
        private double costNum;
        private double costTotalNum;
        private double workCost;
        private double workTotalCost;
        private long orderFinishTime;//fff
        private int orderStatus;
        private String deliveryTimeLength;
        private String integralStatus;
        private int evaluate;
        private String evaluateContent;
        private int ifSingle;
        private int pickup;// 0 未取货   1 已取货
        private long reservationTime;
        private long pickupTime;//预计取货时间
        private long serviceTime;//预计送货时间
        private String receiptPeopleName;
        private String receiptPeoplePhone;
        private String sendRingLetter;

        public String getSendRingLetter() {
            return sendRingLetter;
        }

        public void setSendRingLetter(String sendRingLetter) {
            this.sendRingLetter = sendRingLetter;
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

        public long getPickupTime() {
            return pickupTime;
        }

        public void setPickupTime(long pickupTime) {
            this.pickupTime = pickupTime;
        }

        public long getServiceTime() {
            return serviceTime;
        }

        public void setServiceTime(long serviceTime) {
            this.serviceTime = serviceTime;
        }

        public long getPickup_time() {
            return pickupTime;
        }

        public void setPickup_time(long pickup_time) {
            this.pickupTime = pickup_time;
        }

        public long getService_time() {
            return serviceTime;
        }

        public void setService_time(long service_time) {
            this.serviceTime = service_time;
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

        public int getPickup() {
            return pickup;
        }

        public void setPickup(int pickup) {
            this.pickup = pickup;
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

        public String getWorkContent() {
            return workContent;
        }

        public void setWorkContent(String workContent) {
            this.workContent = workContent;
        }


        public double getCostNum() {
            return costNum;
        }

        public void setCostNum(double costNum) {
            this.costNum = costNum;
        }

        public double getCostTotalNum() {
            return costTotalNum;
        }

        public void setCostTotalNum(double costTotalNum) {
            this.costTotalNum = costTotalNum;
        }


        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
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

        public String getCostType() {
            return costType;
        }

        public void setCostType(String costType) {
            this.costType = costType;
        }

        public long getOrderFinishTime() {
            return orderFinishTime;
        }

        public void setOrderFinishTime(long orderFinishTime) {
            this.orderFinishTime = orderFinishTime;
        }

        public String getDeliveryTimeLength() {
            return deliveryTimeLength;
        }

        public void setDeliveryTimeLength(String deliveryTimeLength) {
            this.deliveryTimeLength = deliveryTimeLength;
        }

        public String getIntegralStatus() {
            return integralStatus;
        }

        public void setIntegralStatus(String integralStatus) {
            this.integralStatus = integralStatus;
        }

        public String getEvaluateContent() {
            return evaluateContent;
        }

        public void setEvaluateContent(String evaluateContent) {
            this.evaluateContent = evaluateContent;
        }
    }
}
