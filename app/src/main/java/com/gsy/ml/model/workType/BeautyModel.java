package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 * 美妆美甲
 */

public class BeautyModel {

    private long startTime;
    private String sex;
    private String content;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
