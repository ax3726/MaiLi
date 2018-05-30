package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 * 同城配送
 */

public class CityServiceModel {
    private long subscribeTime;
    private String  weight;
    private String  content;

    public long getSubscribeTime() {
        return subscribeTime;
    }

    public void setSubscribeTime(long subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
