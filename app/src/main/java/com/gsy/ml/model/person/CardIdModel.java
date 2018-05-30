package com.gsy.ml.model.person;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/5.
 */

public class CardIdModel implements Serializable{

    /**
     * name : 榛勮�垮槈
     * idNum : 445202199002108316
     * status : 200
     * info : 验证通过
     */

    private String name;
    private String idNum;
    private String status;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
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
