package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/6/7.
 */

public class ShareMoneyModel {

    /**
     * money : 0
     * peopleNum : 4
     * status : 200
     * info : 操作成功
     */

    private double money;
    private int peopleNum;
    private String status;
    private String info;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getPeopleNum() {
        return peopleNum;
    }

    public void setPeopleNum(int peopleNum) {
        this.peopleNum = peopleNum;
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
}
