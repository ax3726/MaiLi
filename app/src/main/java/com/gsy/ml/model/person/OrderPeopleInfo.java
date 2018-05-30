package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/9/1.
 */

public class OrderPeopleInfo {
    private String head_url;
    private String phone;
    private String huanxin;
    private int star;
    private String name;
    private String nick_name;
    private String sex;

    public OrderPeopleInfo() {
    }

    public OrderPeopleInfo(String head_url, String phone, String huanxin, int star, String name,String nick_name, String sex) {
        this.head_url = head_url;
        this.phone = phone;
        this.huanxin = huanxin;
        this.star = star;
        this.name = name;
        this.nick_name = nick_name;
        this.sex = sex;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHuanxin() {
        return huanxin;
    }

    public void setHuanxin(String huanxin) {
        this.huanxin = huanxin;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
