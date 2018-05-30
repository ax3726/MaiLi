package com.gsy.ml.model.main;

/**
 * Created by Administrator on 2017/5/4.
 */

public class OrderTypeModel {
    private String name;
    private int type;
    private int img_res;

    public OrderTypeModel(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public OrderTypeModel(String name, int type, int img_res) {
        this.name = name;
        this.type = type;
        this.img_res = img_res;
    }

    public int getImg_res() {
        return img_res;
    }

    public void setImg_res(int img_res) {
        this.img_res = img_res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
