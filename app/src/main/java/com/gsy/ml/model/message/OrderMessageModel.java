package com.gsy.ml.model.message;

/**
 * Created by Administrator on 2017/9/7.
 */

public class OrderMessageModel {

    /**
     * status : 200
     * info : OK
     * data : {"order":"1504680817778143333","workCost":30,"workTotalCost":30,"peopleNum":1,"orderStatus":2,"finishPeopleNum":1}
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
         * order : 1504680817778143333
         * workCost : 30
         * workTotalCost : 30
         * peopleNum : 1
         * orderStatus : 2
         * finishPeopleNum : 1
         */

        private String order;
        private String headUrl;
        private String nickName;
        private String sendRingLetter;
        private String sendPhone;
        private String workPlace;
        private String workContent;
        private int workType;
        private double workCost;
        private double workTotalCost;
        private int peopleNum;
        private int orderStatus;
        private int finishPeopleNum;

        public String getWorkContent() {
            return workContent;
        }

        public void setWorkContent(String workContent) {
            this.workContent = workContent;
        }

        public String getSendPhone() {
            return sendPhone;
        }

        public void setSendPhone(String sendPhone) {
            this.sendPhone = sendPhone;
        }

        public String getWorkPlace() {
            return workPlace;
        }

        public void setWorkPlace(String workPlace) {
            this.workPlace = workPlace;
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

        public String getSendRingLetter() {
            return sendRingLetter;
        }

        public void setSendRingLetter(String sendRingLetter) {
            this.sendRingLetter = sendRingLetter;
        }

        public int getWorkType() {
            return workType;
        }

        public void setWorkType(int workType) {
            this.workType = workType;
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

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public int getPeopleNum() {
            return peopleNum;
        }

        public void setPeopleNum(int peopleNum) {
            this.peopleNum = peopleNum;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getFinishPeopleNum() {
            return finishPeopleNum;
        }

        public void setFinishPeopleNum(int finishPeopleNum) {
            this.finishPeopleNum = finishPeopleNum;
        }


    }
}
