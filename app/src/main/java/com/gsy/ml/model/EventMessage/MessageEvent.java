package com.gsy.ml.model.EventMessage;

/**
 * Created by Administrator on 2017/4/29.
 */

public class MessageEvent {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MessageEvent(String message) {
        this.message = message;
    }
}
