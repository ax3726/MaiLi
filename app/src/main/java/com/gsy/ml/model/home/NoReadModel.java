package com.gsy.ml.model.home;

/**
 * Created by Administrator on 2017/5/24.
 */

public class NoReadModel {


    /**
     * userJobBool : false
     * userPlace : true
     * acceptCount : 0
     * noReadCount : 0
     * sendCount : 0
     * status : 200
     * info : 查询成功
     */

    private boolean userJobBool;
    private boolean userPlace;
    private int acceptCount;
    private int noReadCount;
    private int sendCount;
    private String status;
    private String info;

    public boolean isUserJobBool() {
        return userJobBool;
    }

    public void setUserJobBool(boolean userJobBool) {
        this.userJobBool = userJobBool;
    }

    public boolean isUserPlace() {
        return userPlace;
    }

    public void setUserPlace(boolean userPlace) {
        this.userPlace = userPlace;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        this.acceptCount = acceptCount;
    }

    public int getNoReadCount() {
        return noReadCount;
    }

    public void setNoReadCount(int noReadCount) {
        this.noReadCount = noReadCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
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
