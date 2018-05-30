package com.gsy.ml.model.home;

import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */

public class PartTimeJobModel {

    /**
     * data : {"acceptOrdersList":[{"id":9,"order":"14993992444205702","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1499399254700,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.1102523803711","startWei":"30.283018112182617","startPlace":"益乐新村南区","startDoorplate":"3425","workProvince":"浙江省","workCity":"杭州市","workArea":"萧山区,江干区,江干区","workJing":"120.298792,120.247084,120.347175","workWei":"30.229021,30.309129,30.316601","workPlace":"杭州路德行汽车有限公司高新三路333号,普福家园老红普路东150米,福雷德广场文泽路99号(近金沙数码港)","workDoorplate":"125,2154,21345","workContent":"0^|5^| ^|","costType":null,"costNum":110,"costTotalNum":110,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":1,"reservationTime":0,"sendPeopleName":"李涛","sendPeoplePhone":"18397845702","receiptPeopleName":"李涛,黎明,卢金鑫","receiptPeoplePhone":"18379485702,15170193726,15990172074","pickup":0,"teamStatus":0,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_img97cd221e2d844278b940bb8797dd1b10.jpg"}],"sendOrders":{"id":21,"order":"14993992444205702","sendPhone":"18397845702","sendPeople":"李涛","sendTime":1499399244420,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.1102523803711","startWei":"30.283018112182617","startPlace":"益乐新村南区","startDoorplate":"3425","workProvince":"浙江省","workCity":"杭州市","workArea":"萧山区,江干区,江干区","workJing":"120.298792,120.247084,120.347175","workWei":"30.229021,30.309129,30.316601","workPlace":"杭州路德行汽车有限公司高新三路333号,普福家园老红普路东150米,福雷德广场文泽路99号(近金沙数码港)","workDoorplate":"125,2154,21345","workLable":"","workContent":"0^|5^| ^|","workCost":110,"workTotalCost":110,"peopleNum":1,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":null,"orderStatus":1,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":1,"reservationTime":0,"sendPeopleName":"李涛","sendPeoplePhone":"18397845702","acceptPeopleName":"黎明","acceptPeoplePhone":"15170193726","receiptPeopleName":"李涛,黎明,卢金鑫","receiptPeoplePhone":"18379485702,15170193726,15990172074","teamStatus":0,"cashCoupon":0,"finishPeopleNum":0,"doingPeoplePhone":"15170193726黎明,"}}
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
         * acceptOrdersList : [{"id":9,"order":"14993992444205702","acceptPhone":"15170193726","acceptPeople":"黎明","acceptTime":1499399254700,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.1102523803711","startWei":"30.283018112182617","startPlace":"益乐新村南区","startDoorplate":"3425","workProvince":"浙江省","workCity":"杭州市","workArea":"萧山区,江干区,江干区","workJing":"120.298792,120.247084,120.347175","workWei":"30.229021,30.309129,30.316601","workPlace":"杭州路德行汽车有限公司高新三路333号,普福家园老红普路东150米,福雷德广场文泽路99号(近金沙数码港)","workDoorplate":"125,2154,21345","workContent":"0^|5^| ^|","costType":null,"costNum":110,"costTotalNum":110,"orderFinishTime":null,"orderStatus":0,"deliveryTimeLength":null,"integralStatus":null,"evaluate":0,"evaluateContent":null,"ifSingle":1,"reservationTime":0,"sendPeopleName":"李涛","sendPeoplePhone":"18397845702","receiptPeopleName":"李涛,黎明,卢金鑫","receiptPeoplePhone":"18379485702,15170193726,15990172074","pickup":0,"teamStatus":0,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_img97cd221e2d844278b940bb8797dd1b10.jpg"}]
         * sendOrders : {"id":21,"order":"14993992444205702","sendPhone":"18397845702","sendPeople":"李涛","sendTime":1499399244420,"workType":"1","workLevel":"普通","workStartTime":0,"workEndTime":0,"startProvince":"浙江省","startCity":"杭州市","startArea":"西湖区","startJing":"120.1102523803711","startWei":"30.283018112182617","startPlace":"益乐新村南区","startDoorplate":"3425","workProvince":"浙江省","workCity":"杭州市","workArea":"萧山区,江干区,江干区","workJing":"120.298792,120.247084,120.347175","workWei":"30.229021,30.309129,30.316601","workPlace":"杭州路德行汽车有限公司高新三路333号,普福家园老红普路东150米,福雷德广场文泽路99号(近金沙数码港)","workDoorplate":"125,2154,21345","workLable":"","workContent":"0^|5^| ^|","workCost":110,"workTotalCost":110,"peopleNum":1,"acceptPeopleNum":1,"feesType":null,"orderFinishTime":null,"orderStatus":1,"integralStatus":0,"deliveryTimeLength":null,"evaluate":0,"evaluateContent":null,"ifSingle":1,"reservationTime":0,"sendPeopleName":"李涛","sendPeoplePhone":"18397845702","acceptPeopleName":"黎明","acceptPeoplePhone":"15170193726","receiptPeopleName":"李涛,黎明,卢金鑫","receiptPeoplePhone":"18379485702,15170193726,15990172074","teamStatus":0,"cashCoupon":0,"finishPeopleNum":0,"doingPeoplePhone":"15170193726黎明,"}
         */
        private int total;//沟通总人数

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        private SendOrdersBean sendOrders;
        private List<AcceptOrdersListBean> acceptOrdersList;

        public SendOrdersBean getSendOrders() {
            return sendOrders;
        }

        public void setSendOrders(SendOrdersBean sendOrders) {
            this.sendOrders = sendOrders;
        }

        public List<AcceptOrdersListBean> getAcceptOrdersList() {
            return acceptOrdersList;
        }

        public void setAcceptOrdersList(List<AcceptOrdersListBean> acceptOrdersList) {
            this.acceptOrdersList = acceptOrdersList;
        }

        public static class SendOrdersBean {
            /**
             * id : 21
             * order : 14993992444205702
             * sendPhone : 18397845702
             * sendPeople : 李涛
             * sendTime : 1499399244420
             * workType : 1
             * workLevel : 普通
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : 浙江省
             * startCity : 杭州市
             * startArea : 西湖区
             * startJing : 120.1102523803711
             * startWei : 30.283018112182617
             * startPlace : 益乐新村南区
             * startDoorplate : 3425
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 萧山区,江干区,江干区
             * workJing : 120.298792,120.247084,120.347175
             * workWei : 30.229021,30.309129,30.316601
             * workPlace : 杭州路德行汽车有限公司高新三路333号,普福家园老红普路东150米,福雷德广场文泽路99号(近金沙数码港)
             * workDoorplate : 125,2154,21345
             * workLable :
             * workContent : 0^|5^| ^|
             * workCost : 110
             * workTotalCost : 110
             * peopleNum : 1
             * acceptPeopleNum : 1
             * feesType : null
             * orderFinishTime : null
             * orderStatus : 1
             * integralStatus : 0
             * deliveryTimeLength : null
             * evaluate : 0
             * evaluateContent : null
             * ifSingle : 1
             * reservationTime : 0
             * sendPeopleName : 李涛
             * sendPeoplePhone : 18397845702
             * acceptPeopleName : 黎明
             * acceptPeoplePhone : 15170193726
             * receiptPeopleName : 李涛,黎明,卢金鑫
             * receiptPeoplePhone : 18379485702,15170193726,15990172074
             * teamStatus : 0
             * cashCoupon : 0
             * finishPeopleNum : 0
             * doingPeoplePhone : 15170193726黎明,
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
            private String startDoorplate;
            private String workProvince;
            private String workCity;
            private String workArea;
            private String workJing;
            private String workWei;
            private String workPlace;
            private String workDoorplate;
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
            private String sendPeopleName;
            private String sendPeoplePhone;
            private String acceptPeopleName;
            private String acceptPeoplePhone;
            private String receiptPeopleName;
            private String receiptPeoplePhone;
            private int teamStatus;
            private double cashCoupon;
            private int finishPeopleNum;
            private String doingPeoplePhone;
            private String headUrl;
            private String nickName;
            private String name;
            private String sex;
            private int star;
            private String sendRingLetter;
            private boolean onLine;

            public boolean isOnLine() {
                return onLine;
            }

            public void setOnLine(boolean onLine) {
                this.onLine = onLine;
            }

            public String getSendRingLetter() {
                return sendRingLetter;
            }

            public void setSendRingLetter(String sendRingLetter) {
                this.sendRingLetter = sendRingLetter;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public int getTeamStatus() {
                return teamStatus;
            }

            public void setTeamStatus(int teamStatus) {
                this.teamStatus = teamStatus;
            }

            public double getCashCoupon() {
                return cashCoupon;
            }

            public void setCashCoupon(double cashCoupon) {
                this.cashCoupon = cashCoupon;
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

            public long getReservationTime() {
                return reservationTime;
            }

            public void setReservationTime(long reservationTime) {
                this.reservationTime = reservationTime;
            }
        }

        public static class AcceptOrdersListBean {
            /**
             * id : 9
             * order : 14993992444205702
             * acceptPhone : 15170193726
             * acceptPeople : 黎明
             * acceptTime : 1499399254700
             * workType : 1
             * workLevel : 普通
             * workStartTime : 0
             * workEndTime : 0
             * startProvince : 浙江省
             * startCity : 杭州市
             * startArea : 西湖区
             * startJing : 120.1102523803711
             * startWei : 30.283018112182617
             * startPlace : 益乐新村南区
             * startDoorplate : 3425
             * workProvince : 浙江省
             * workCity : 杭州市
             * workArea : 萧山区,江干区,江干区
             * workJing : 120.298792,120.247084,120.347175
             * workWei : 30.229021,30.309129,30.316601
             * workPlace : 杭州路德行汽车有限公司高新三路333号,普福家园老红普路东150米,福雷德广场文泽路99号(近金沙数码港)
             * workDoorplate : 125,2154,21345
             * workContent : 0^|5^| ^|
             * costType : null
             * costNum : 110
             * costTotalNum : 110
             * orderFinishTime : null
             * orderStatus : 0
             * deliveryTimeLength : null
             * integralStatus : null
             * evaluate : 0
             * evaluateContent : null
             * ifSingle : 1
             * reservationTime : 0
             * sendPeopleName : 李涛
             * sendPeoplePhone : 18397845702
             * receiptPeopleName : 李涛,黎明,卢金鑫
             * receiptPeoplePhone : 18379485702,15170193726,15990172074
             * pickup : 0
             * teamStatus : 0
             * headUrl : http://maili.oss-cn-shanghai.aliyuncs.com/head_img97cd221e2d844278b940bb8797dd1b10.jpg
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
            private String startDoorplate;
            private String workProvince;
            private String workCity;
            private String workArea;
            private String workJing;
            private String workWei;
            private String workPlace;
            private String workDoorplate;
            private String workContent;
            private int costType;
            private double costNum;
            private double costTotalNum;
            private long orderFinishTime;
            private int orderStatus;
            private String deliveryTimeLength;
            private int integralStatus;
            private int evaluate;
            private String evaluateContent;
            private int ifSingle;
            private long reservationTime;
            private String sendPeopleName;
            private String sendPeoplePhone;
            private String receiptPeopleName;
            private String receiptPeoplePhone;
            private long pickup;
            private int teamStatus;
            private String headUrl;
            private String nickName;
            private String name;
            private String sex;
            private int star;
            private String acceptRingLetter;
            private boolean onLine;

            public boolean isOnLine() {
                return onLine;
            }

            public void setOnLine(boolean onLine) {
                this.onLine = onLine;
            }

            public String getAcceptRingLetter() {
                return acceptRingLetter;
            }

            public void setAcceptRingLetter(String acceptRingLetter) {
                this.acceptRingLetter = acceptRingLetter;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
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

            public int getCostType() {
                return costType;
            }

            public void setCostType(int costType) {
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

            public int getIntegralStatus() {
                return integralStatus;
            }

            public void setIntegralStatus(int integralStatus) {
                this.integralStatus = integralStatus;
            }

            public String getEvaluateContent() {
                return evaluateContent;
            }

            public void setEvaluateContent(String evaluateContent) {
                this.evaluateContent = evaluateContent;
            }

            public long getReservationTime() {
                return reservationTime;
            }

            public void setReservationTime(long reservationTime) {
                this.reservationTime = reservationTime;
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

            public long getPickup() {
                return pickup;
            }

            public void setPickup(long pickup) {
                this.pickup = pickup;
            }

            public int getTeamStatus() {
                return teamStatus;
            }

            public void setTeamStatus(int teamStatus) {
                this.teamStatus = teamStatus;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }
        }
    }
}
