package com.gsy.ml.model.EventMessage;

/**
 * Created by Administrator on 2017/4/29.
 */

public class CancelReason {
    private String order;
    private String reason;
    private int type;
    private int workType;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getType() {
        return type;
    }

    public int getWorkType() {
        return workType;
    }

    public void setWorkType(int workType) {
        this.workType = workType;
    }

    public void setType(int type) {
        this.type = type;
    }
    public CancelReason(){}
    public CancelReason(String order, String reason, int type, int workType) {
        this.order = order;
        this.reason = reason;
        this.type = type;
        this.workType = workType;
    }
}
