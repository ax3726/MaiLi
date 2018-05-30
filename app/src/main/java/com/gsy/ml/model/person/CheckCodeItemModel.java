package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/8/18.
 */

public class CheckCodeItemModel {
    private String name;
    private String phone;
    private String address;
    private long service_time;//送件时长
    private long service_timeout;//送件时间
    private String code;//验证码
    private int code_state;//验证码状态

    public int getCode_state() {
        return code_state;
    }

    public void setCode_state(int code_state) {
        this.code_state = code_state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getService_time() {
        return service_time;
    }

    public void setService_time(long service_time) {
        this.service_time = service_time;
    }

    public long getService_timeout() {
        return service_timeout;
    }

    public void setService_timeout(long service_timeout) {
        this.service_timeout = service_timeout;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
