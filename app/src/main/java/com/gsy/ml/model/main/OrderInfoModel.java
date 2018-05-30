package com.gsy.ml.model.main;

/**
 * Created by Administrator on 2017/4/27.
 */

public class OrderInfoModel {


    /**
     * status : 1001
     * info : 卖力提醒您,您有新的订单!
     * data : {"acceptPeopleName":"","acceptPeopleNum":0,"acceptPeoplePhone":"","cashCoupon":0,"deliveryTimeLength":0,"distributionImg":null,"doingPeoplePhone":"","evaluate":0,"evaluateContent":"","feesType":"","finishPeopleNum":0,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg","id":0,"ifSingle":0,"integralStatus":0,"maxCommunicateNum":0,"name":"黎明","nickName":"哈喽","onLine":false,"order":"1504677433786363726","orderFinishTime":0,"orderStatus":0,"peopleNum":1,"receiptPeopleName":"","receiptPeoplePhone":"","reservationTime":0,"sendPeople":"黎明","sendPeopleName":"","sendPeoplePhone":"","sendPhone":"15170193726","sendRingLetter":"ml15170193726300","sendTime":1504677433786,"sex":"男","star":1,"startArea":"富阳区","startCity":"杭州市","startDoorplate":"哈喽","startJing":"119.95267","startPlace":"洗乐门汽车服务中心(公园路)公园路38号","startProvince":"浙江省","startWei":"30.069748","teamStatus":0,"wmOrderId":"","workArea":"富阳区","workCity":"杭州市","workContent":"{\"content\":\"来来来\",\"startTime\":1504677480000}","workCost":50,"workDistance":"","workDoorplate":"哈喽","workEndTime":0,"workJing":"119.95267","workLable":"","workLevel":"高级","workPlace":"洗乐门汽车服务中心(公园路)公园路38号","workProvince":"浙江省","workStartTime":1504677480000,"workTotalCost":50,"workType":"17","workWei":"30.069748"}
     */

    private String status;
    private String info;
    private long pushTime;//推送时间
    private DataBean data;

    public long getPushTime() {
        return pushTime;
    }

    public void setPushTime(long pushTime) {
        this.pushTime = pushTime;
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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * acceptPeopleName :
         * acceptPeopleNum : 0
         * acceptPeoplePhone :
         * cashCoupon : 0
         * deliveryTimeLength : 0
         * distributionImg : null
         * doingPeoplePhone :
         * evaluate : 0
         * evaluateContent :
         * feesType :
         * finishPeopleNum : 0
         * headUrl : http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg
         * id : 0
         * ifSingle : 0
         * integralStatus : 0
         * maxCommunicateNum : 0
         * name : 黎明
         * nickName : 哈喽
         * onLine : false
         * order : 1504677433786363726
         * orderFinishTime : 0
         * orderStatus : 0
         * peopleNum : 1
         * receiptPeopleName :
         * receiptPeoplePhone :
         * reservationTime : 0
         * sendPeople : 黎明
         * sendPeopleName :
         * sendPeoplePhone :
         * sendPhone : 15170193726
         * sendRingLetter : ml15170193726300
         * sendTime : 1504677433786
         * sex : 男
         * star : 1
         * startArea : 富阳区
         * startCity : 杭州市
         * startDoorplate : 哈喽
         * startJing : 119.95267
         * startPlace : 洗乐门汽车服务中心(公园路)公园路38号
         * startProvince : 浙江省
         * startWei : 30.069748
         * teamStatus : 0
         * wmOrderId :
         * workArea : 富阳区
         * workCity : 杭州市
         * workContent : {"content":"来来来","startTime":1504677480000}
         * workCost : 50
         * workDistance :
         * workDoorplate : 哈喽
         * workEndTime : 0
         * workJing : 119.95267
         * workLable :
         * workLevel : 高级
         * workPlace : 洗乐门汽车服务中心(公园路)公园路38号
         * workProvince : 浙江省
         * workStartTime : 1504677480000
         * workTotalCost : 50
         * workType : 17
         * workWei : 30.069748
         */

        private String acceptPeopleName;
        private int acceptPeopleNum;
        private String acceptPeoplePhone;
        private int cashCoupon;
        private int deliveryTimeLength;

        private String doingPeoplePhone;
        private int evaluate;
        private String evaluateContent;
        private String feesType;
        private int finishPeopleNum;
        private String headUrl;
        private int id;
        private int ifSingle;
        private int integralStatus;
        private int maxCommunicateNum;
        private String name;
        private String nickName;
        private boolean onLine;
        private String order;
        private long orderFinishTime;
        private int orderStatus;
        private int peopleNum;
        private String receiptPeopleName;
        private String receiptPeoplePhone;
        private long reservationTime;
        private String sendPeople;
        private String sendPeopleName;
        private String sendPeoplePhone;
        private String sendPhone;
        private String sendRingLetter;
        private long sendTime;
        private String sex;
        private int star;
        private String startArea;
        private String startCity;
        private String startDoorplate;
        private String startJing;
        private String startPlace;
        private String startProvince;
        private String startWei;
        private int teamStatus;
        private String wmOrderId;
        private String workArea;
        private String workCity;
        private String workContent;
        private double workCost;
        private String workDistance;
        private String workDoorplate;
        private long workEndTime;
        private String workJing;
        private String workLable;
        private String workLevel;
        private String workPlace;
        private String workProvince;
        private long workStartTime;
        private double workTotalCost;
        private String workType;
        private String workWei;

        public String getAcceptPeopleName() {
            return acceptPeopleName;
        }

        public void setAcceptPeopleName(String acceptPeopleName) {
            this.acceptPeopleName = acceptPeopleName;
        }

        public int getAcceptPeopleNum() {
            return acceptPeopleNum;
        }

        public void setAcceptPeopleNum(int acceptPeopleNum) {
            this.acceptPeopleNum = acceptPeopleNum;
        }

        public String getAcceptPeoplePhone() {
            return acceptPeoplePhone;
        }

        public void setAcceptPeoplePhone(String acceptPeoplePhone) {
            this.acceptPeoplePhone = acceptPeoplePhone;
        }

        public int getCashCoupon() {
            return cashCoupon;
        }

        public void setCashCoupon(int cashCoupon) {
            this.cashCoupon = cashCoupon;
        }

        public int getDeliveryTimeLength() {
            return deliveryTimeLength;
        }

        public void setDeliveryTimeLength(int deliveryTimeLength) {
            this.deliveryTimeLength = deliveryTimeLength;
        }



        public String getDoingPeoplePhone() {
            return doingPeoplePhone;
        }

        public void setDoingPeoplePhone(String doingPeoplePhone) {
            this.doingPeoplePhone = doingPeoplePhone;
        }

        public int getEvaluate() {
            return evaluate;
        }

        public void setEvaluate(int evaluate) {
            this.evaluate = evaluate;
        }

        public String getEvaluateContent() {
            return evaluateContent;
        }

        public void setEvaluateContent(String evaluateContent) {
            this.evaluateContent = evaluateContent;
        }

        public String getFeesType() {
            return feesType;
        }

        public void setFeesType(String feesType) {
            this.feesType = feesType;
        }

        public int getFinishPeopleNum() {
            return finishPeopleNum;
        }

        public void setFinishPeopleNum(int finishPeopleNum) {
            this.finishPeopleNum = finishPeopleNum;
        }

        public String getHeadUrl() {
            return headUrl;
        }

        public void setHeadUrl(String headUrl) {
            this.headUrl = headUrl;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getIfSingle() {
            return ifSingle;
        }

        public void setIfSingle(int ifSingle) {
            this.ifSingle = ifSingle;
        }

        public int getIntegralStatus() {
            return integralStatus;
        }

        public void setIntegralStatus(int integralStatus) {
            this.integralStatus = integralStatus;
        }

        public int getMaxCommunicateNum() {
            return maxCommunicateNum;
        }

        public void setMaxCommunicateNum(int maxCommunicateNum) {
            this.maxCommunicateNum = maxCommunicateNum;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public boolean isOnLine() {
            return onLine;
        }

        public void setOnLine(boolean onLine) {
            this.onLine = onLine;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
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


        public String getSendPeople() {
            return sendPeople;
        }

        public void setSendPeople(String sendPeople) {
            this.sendPeople = sendPeople;
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

        public String getSendPhone() {
            return sendPhone;
        }

        public void setSendPhone(String sendPhone) {
            this.sendPhone = sendPhone;
        }

        public String getSendRingLetter() {
            return sendRingLetter;
        }

        public void setSendRingLetter(String sendRingLetter) {
            this.sendRingLetter = sendRingLetter;
        }

        public long getSendTime() {
            return sendTime;
        }

        public void setSendTime(long sendTime) {
            this.sendTime = sendTime;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public int getStar() {
            return star;
        }

        public void setStar(int star) {
            this.star = star;
        }

        public String getStartArea() {
            return startArea;
        }

        public void setStartArea(String startArea) {
            this.startArea = startArea;
        }

        public String getStartCity() {
            return startCity;
        }

        public void setStartCity(String startCity) {
            this.startCity = startCity;
        }

        public String getStartDoorplate() {
            return startDoorplate;
        }

        public void setStartDoorplate(String startDoorplate) {
            this.startDoorplate = startDoorplate;
        }

        public String getStartJing() {
            return startJing;
        }

        public void setStartJing(String startJing) {
            this.startJing = startJing;
        }

        public String getStartPlace() {
            return startPlace;
        }

        public void setStartPlace(String startPlace) {
            this.startPlace = startPlace;
        }

        public String getStartProvince() {
            return startProvince;
        }

        public void setStartProvince(String startProvince) {
            this.startProvince = startProvince;
        }

        public String getStartWei() {
            return startWei;
        }

        public void setStartWei(String startWei) {
            this.startWei = startWei;
        }

        public int getTeamStatus() {
            return teamStatus;
        }

        public void setTeamStatus(int teamStatus) {
            this.teamStatus = teamStatus;
        }

        public String getWmOrderId() {
            return wmOrderId;
        }

        public void setWmOrderId(String wmOrderId) {
            this.wmOrderId = wmOrderId;
        }

        public String getWorkArea() {
            return workArea;
        }

        public void setWorkArea(String workArea) {
            this.workArea = workArea;
        }

        public String getWorkCity() {
            return workCity;
        }

        public void setWorkCity(String workCity) {
            this.workCity = workCity;
        }

        public String getWorkContent() {
            return workContent;
        }

        public void setWorkContent(String workContent) {
            this.workContent = workContent;
        }



        public String getWorkDistance() {
            return workDistance;
        }

        public void setWorkDistance(String workDistance) {
            this.workDistance = workDistance;
        }

        public String getWorkDoorplate() {
            return workDoorplate;
        }

        public void setWorkDoorplate(String workDoorplate) {
            this.workDoorplate = workDoorplate;
        }



        public String getWorkJing() {
            return workJing;
        }

        public void setWorkJing(String workJing) {
            this.workJing = workJing;
        }

        public String getWorkLable() {
            return workLable;
        }

        public void setWorkLable(String workLable) {
            this.workLable = workLable;
        }

        public String getWorkLevel() {
            return workLevel;
        }

        public void setWorkLevel(String workLevel) {
            this.workLevel = workLevel;
        }

        public String getWorkPlace() {
            return workPlace;
        }

        public void setWorkPlace(String workPlace) {
            this.workPlace = workPlace;
        }

        public String getWorkProvince() {
            return workProvince;
        }

        public void setWorkProvince(String workProvince) {
            this.workProvince = workProvince;
        }

        public long getWorkStartTime() {
            return workStartTime;
        }

        public void setWorkStartTime(long workStartTime) {
            this.workStartTime = workStartTime;
        }


        public String getWorkType() {
            return workType;
        }

        public void setWorkType(String workType) {
            this.workType = workType;
        }

        public String getWorkWei() {
            return workWei;
        }

        public void setWorkWei(String workWei) {
            this.workWei = workWei;
        }

        public long getOrderFinishTime() {
            return orderFinishTime;
        }

        public void setOrderFinishTime(long orderFinishTime) {
            this.orderFinishTime = orderFinishTime;
        }

        public long getReservationTime() {
            return reservationTime;
        }

        public void setReservationTime(long reservationTime) {
            this.reservationTime = reservationTime;
        }

        public double getWorkCost() {
            return workCost;
        }

        public void setWorkCost(double workCost) {
            this.workCost = workCost;
        }

        public long getWorkEndTime() {
            return workEndTime;
        }

        public void setWorkEndTime(long workEndTime) {
            this.workEndTime = workEndTime;
        }

        public double getWorkTotalCost() {
            return workTotalCost;
        }

        public void setWorkTotalCost(double workTotalCost) {
            this.workTotalCost = workTotalCost;
        }
    }
}
