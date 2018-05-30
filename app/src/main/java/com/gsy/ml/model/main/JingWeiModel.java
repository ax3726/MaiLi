package com.gsy.ml.model.main;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/27.
 */

public class JingWeiModel implements Serializable{
    private String jing;
    private String wei;
    private String phone;
    private int status;

    public JingWeiModel(String jing, String wei, String phone) {
        this.jing = jing;
        this.wei = wei;
        this.phone = phone;
        this.status = 0;
    }

    public JingWeiModel(String phone) {
        this.phone = phone;
        this.status = 1;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getJing() {
        return jing;
    }

    public void setJing(String jing) {
        this.jing = jing;
    }

    public String getWei() {
        return wei;
    }

    public void setWei(String wei) {
        this.wei = wei;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
