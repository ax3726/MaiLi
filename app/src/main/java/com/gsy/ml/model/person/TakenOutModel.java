package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/8/16.
 */

public class TakenOutModel {

    /**
     * data : https://open-erp.meituan.com/storemap?developerId=100995&businessId=2&ePoiId=18397846538&signKey=y9epht6meusil20w&netStore=1&callbackUrl=https%3A%2F%2Fmaili.s1.natapp.cc%2Fjingxian%2Fmaili%2FauthorizationIsOk.shtml
     * info : success
     * status : 200
     */

    private String data;
    private String info;
    private int status;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}