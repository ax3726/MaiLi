package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/8/18.
 */

public class CheckTimeModel {

    /**
     * status : 200
     * info : OK
     * data : 取件耗时：08时00分15秒
     */

    private String status;
    private String info;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
