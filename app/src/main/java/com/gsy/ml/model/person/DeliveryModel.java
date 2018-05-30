package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/8/18.
 */

public class DeliveryModel {

    /**
     * status : 200
     * info : OK
     * data : {"AcceptOrders":{"id":67,"order":"1503046240462543726","acceptPhone":"18397845702","acceptPeople":"李涛","acceptTime":1503046303665,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.09889221191406","startWei":"30.30215835571289","startPlace":"同人精华","startDoorplate":"1635","workProvince":"浙江省","workCity":"杭州市","workArea":"江干区,江干区","workJing":"120.24588775634766,120.24588775634766","workWei":"30.309650421142578,30.309650421142578","workPlace":"普福家园北区8幢二单元702,普福家园北区8幢二单元702","workDoorplate":"　,　","workContent":"0^|5^|美食^|","costType":null,"costNum":50,"costTotalNum":50,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":1,"reservationTime":0,"sendPeopleName":"黎明","sendPeoplePhone":"15170193726","receiptPeopleName":"黎明(先生),黎明(先生)","receiptPeoplePhone":"15170193726,15170193726","pickup":1,"teamStatus":0,"pickupTime":1503048103665,"serviceTime":0,"serviceTimeout":"","serviceTimes":"","pickupTimes":30716},"DistributionImg":{"id":21,"orders":"1503046240462543726","sendName":"黎明","sendPhone":"15170193726","receivedName":"黎明(先生),黎明(先生)","receivedPhone":"15170193726,15170193726","goodsImg":"http://maili.oss-cn-shanghai.aliyuncs.com/certificate_img267e68c75d154096862810400a98e853.jpg,http://maili.oss-cn-shanghai.aliyuncs.com/certificate_img49a19689b1e74fba8189579c669f5c5d.jpg","phoneCode":"602993,770842,","arriveStatus":"0,0,","createTime":1503046530785}}
     */

    private String status;
    private String info;
    private DataBean data;

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
         * AcceptOrders : {"id":67,"order":"1503046240462543726","acceptPhone":"18397845702","acceptPeople":"李涛","acceptTime":1503046303665,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.09889221191406","startWei":"30.30215835571289","startPlace":"同人精华","startDoorplate":"1635","workProvince":"浙江省","workCity":"杭州市","workArea":"江干区,江干区","workJing":"120.24588775634766,120.24588775634766","workWei":"30.309650421142578,30.309650421142578","workPlace":"普福家园北区8幢二单元702,普福家园北区8幢二单元702","workDoorplate":"　,　","workContent":"0^|5^|美食^|","costType":null,"costNum":50,"costTotalNum":50,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":1,"reservationTime":0,"sendPeopleName":"黎明","sendPeoplePhone":"15170193726","receiptPeopleName":"黎明(先生),黎明(先生)","receiptPeoplePhone":"15170193726,15170193726","pickup":1,"teamStatus":0,"pickupTime":1503048103665,"serviceTime":0,"serviceTimeout":"","serviceTimes":"","pickupTimes":30716}
         * DistributionImg : {"id":21,"orders":"1503046240462543726","sendName":"黎明","sendPhone":"15170193726","receivedName":"黎明(先生),黎明(先生)","receivedPhone":"15170193726,15170193726","goodsImg":"http://maili.oss-cn-shanghai.aliyuncs.com/certificate_img267e68c75d154096862810400a98e853.jpg,http://maili.oss-cn-shanghai.aliyuncs.com/certificate_img49a19689b1e74fba8189579c669f5c5d.jpg","phoneCode":"602993,770842,","arriveStatus":"0,0,","createTime":1503046530785}
         */

        private AcceptOrdersBean AcceptOrders;
        private DistributionImgBean DistributionImg;

        public AcceptOrdersBean getAcceptOrders() {
            return AcceptOrders;
        }

        public void setAcceptOrders(AcceptOrdersBean AcceptOrders) {
            this.AcceptOrders = AcceptOrders;
        }

        public DistributionImgBean getDistributionImg() {
            return DistributionImg;
        }

        public void setDistributionImg(DistributionImgBean DistributionImg) {
            this.DistributionImg = DistributionImg;
        }

        public static class AcceptOrdersBean {
            /**
             * id : 67
             * order : 1503046240462543726
             * acceptPhone : 18397845702
             * acceptPeople : 李涛
             * acceptTime : 1503046303665
             * workType : 1
             * workLevel : 普通
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : 浙江省
             * startCity : 杭州市
             * startArea : 西湖区
             * startJing : 120.09889221191406
             * startWei : 30.30215835571289
             * startPlace : 同人精华
             * startDoorplate : 1635
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 江干区,江干区
             * workJing : 120.24588775634766,120.24588775634766
             * workWei : 30.309650421142578,30.309650421142578
             * workPlace : 普福家园北区8幢二单元702,普福家园北区8幢二单元702
             * workDoorplate : 　,　
             * workContent : 0^|5^|美食^|
             * costType : null
             * costNum : 50
             * costTotalNum : 50
             * orderFinishTime : null
             * orderStatus : 0
             * deliveryTimeLength : null
             * integralStatus : null
             * evaluate : 0
             * evaluateContent : null
             * ifSingle : 1
             * reservationTime : 0
             * sendPeopleName : 黎明
             * sendPeoplePhone : 15170193726
             * receiptPeopleName : 黎明(先生),黎明(先生)
             * receiptPeoplePhone : 15170193726,15170193726
             * pickup : 1
             * teamStatus : 0
             * pickupTime : 1503048103665
             * serviceTime : 0
             * serviceTimeout :
             * serviceTimes :
             * pickupTimes : 30716
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
            private String startDoorplate;
            private String workProvince;
            private String workCity;
            private String workArea;
            private String workJing;
            private String workWei;
            private String workPlace;
            private String workDoorplate;
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
            private int reservationTime;
            private String sendPeopleName;
            private String sendPeoplePhone;
            private String receiptPeopleName;
            private String receiptPeoplePhone;
            private int pickup;
            private int teamStatus;
            private long pickupTime;
            private int serviceTime;
            private String serviceTimeout;
            private String serviceTimes;
            private int pickupTimes;

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

            public String getStartDoorplate() {
                return startDoorplate;
            }

            public void setStartDoorplate(String startDoorplate) {
                this.startDoorplate = startDoorplate;
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

            public String getWorkDoorplate() {
                return workDoorplate;
            }

            public void setWorkDoorplate(String workDoorplate) {
                this.workDoorplate = workDoorplate;
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

            public int getReservationTime() {
                return reservationTime;
            }

            public void setReservationTime(int reservationTime) {
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

            public int getPickup() {
                return pickup;
            }

            public void setPickup(int pickup) {
                this.pickup = pickup;
            }

            public int getTeamStatus() {
                return teamStatus;
            }

            public void setTeamStatus(int teamStatus) {
                this.teamStatus = teamStatus;
            }

            public long getPickupTime() {
                return pickupTime;
            }

            public void setPickupTime(long pickupTime) {
                this.pickupTime = pickupTime;
            }

            public int getServiceTime() {
                return serviceTime;
            }

            public void setServiceTime(int serviceTime) {
                this.serviceTime = serviceTime;
            }

            public String getServiceTimeout() {
                return serviceTimeout;
            }

            public void setServiceTimeout(String serviceTimeout) {
                this.serviceTimeout = serviceTimeout;
            }

            public String getServiceTimes() {
                return serviceTimes;
            }

            public void setServiceTimes(String serviceTimes) {
                this.serviceTimes = serviceTimes;
            }

            public int getPickupTimes() {
                return pickupTimes;
            }

            public void setPickupTimes(int pickupTimes) {
                this.pickupTimes = pickupTimes;
            }
        }

        public static class DistributionImgBean {
            /**
             * id : 21
             * orders : 1503046240462543726
             * sendName : 黎明
             * sendPhone : 15170193726
             * receivedName : 黎明(先生),黎明(先生)
             * receivedPhone : 15170193726,15170193726
             * goodsImg : http://maili.oss-cn-shanghai.aliyuncs.com/certificate_img267e68c75d154096862810400a98e853.jpg,http://maili.oss-cn-shanghai.aliyuncs.com/certificate_img49a19689b1e74fba8189579c669f5c5d.jpg
             * phoneCode : 602993,770842,
             * arriveStatus : 0,0,
             * createTime : 1503046530785
             */

            private int id;
            private String orders;
            private String sendName;
            private String sendPhone;
            private String receivedName;
            private String receivedPhone;
            private String goodsImg;
            private String phoneCode;
            private String arriveStatus;
            private long createTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrders() {
                return orders;
            }

            public void setOrders(String orders) {
                this.orders = orders;
            }

            public String getSendName() {
                return sendName;
            }

            public void setSendName(String sendName) {
                this.sendName = sendName;
            }

            public String getSendPhone() {
                return sendPhone;
            }

            public void setSendPhone(String sendPhone) {
                this.sendPhone = sendPhone;
            }

            public String getReceivedName() {
                return receivedName;
            }

            public void setReceivedName(String receivedName) {
                this.receivedName = receivedName;
            }

            public String getReceivedPhone() {
                return receivedPhone;
            }

            public void setReceivedPhone(String receivedPhone) {
                this.receivedPhone = receivedPhone;
            }

            public String getGoodsImg() {
                return goodsImg;
            }

            public void setGoodsImg(String goodsImg) {
                this.goodsImg = goodsImg;
            }

            public String getPhoneCode() {
                return phoneCode;
            }

            public void setPhoneCode(String phoneCode) {
                this.phoneCode = phoneCode;
            }

            public String getArriveStatus() {
                return arriveStatus;
            }

            public void setArriveStatus(String arriveStatus) {
                this.arriveStatus = arriveStatus;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
