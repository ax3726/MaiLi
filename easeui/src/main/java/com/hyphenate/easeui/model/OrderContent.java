package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/31.
 */

public class OrderContent implements Serializable {
    private String order;
    private String orderPhone;
    private String orderAddress;
    private double workCost;
    private double workTotalCost;
    private double orderStatus;
    private String sendNickName;
    private String sendHeadUrl;
    private String acceptNickName;
    private String acceptHeadUrl;
    private String acceptName;
    private String acceptPhone;

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public String getAcceptPhone() {
        return acceptPhone;
    }

    public void setAcceptPhone(String acceptPhone) {
        this.acceptPhone = acceptPhone;
    }

    public String getSendNickName() {
        return sendNickName;
    }

    public void setSendNickName(String sendNickName) {
        this.sendNickName = sendNickName;
    }

    public String getSendHeadUrl() {
        return sendHeadUrl;
    }

    public void setSendHeadUrl(String sendHeadUrl) {
        this.sendHeadUrl = sendHeadUrl;
    }

    public String getAcceptNickName() {
        return acceptNickName;
    }

    public void setAcceptNickName(String acceptNickName) {
        this.acceptNickName = acceptNickName;
    }

    public String getAcceptHeadUrl() {
        return acceptHeadUrl;
    }

    public void setAcceptHeadUrl(String acceptHeadUrl) {
        this.acceptHeadUrl = acceptHeadUrl;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    private int workType;

    public OrderContent() {
    }

    public OrderContent(String order, String orderPhone, String orderAddress, double workCost, double workTotalCost,
                        double orderStatus, String sendNickName, String sendHeadUrl, String acceptNickName,
                        String acceptHeadUrl,String acceptName, String acceptPhone,  int workType) {
        this.order = order;
        this.orderPhone = orderPhone;
        this.orderAddress = orderAddress;
        this.workCost = workCost;
        this.workTotalCost = workTotalCost;
        this.orderStatus = orderStatus;
        this.sendNickName = sendNickName;
        this.sendHeadUrl = sendHeadUrl;
        this.acceptNickName = acceptNickName;
        this.acceptHeadUrl = acceptHeadUrl;
        this.workType = workType;
        this.acceptName = acceptName;
        this.acceptPhone = acceptPhone;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
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

    public double getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(double orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getWorkTpye() {
        return workType;
    }

    public void setWorkTpye(int workTpye) {
        this.workType = workTpye;
    }
}
