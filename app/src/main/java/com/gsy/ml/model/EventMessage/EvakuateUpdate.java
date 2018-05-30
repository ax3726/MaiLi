package com.gsy.ml.model.EventMessage;

/**
 * Created by Administrator on 2017/4/29.
 */

public class EvakuateUpdate {
    String message;
    int  type;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public EvakuateUpdate(String message, int type) {
        this.message = message;
        this.type = type;
    }
}
