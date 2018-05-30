package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/5/17.
 */

public class BanlancePriceModel {
    private String name;
    private boolean is_choose;
    private String phone;
    private boolean online;

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public BanlancePriceModel(String name, boolean is_choose, String phone, boolean online) {
        this.name = name;
        this.is_choose = is_choose;
        this.phone = phone;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIs_choose() {
        return is_choose;
    }

    public void setIs_choose(boolean is_choose) {
        this.is_choose = is_choose;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
