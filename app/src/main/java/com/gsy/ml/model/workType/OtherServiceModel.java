package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 *
 * 电器服务：
 车辆服务：
 宠物服务：
 电脑维修：
 */

public class OtherServiceModel {
    private long startTime;

    private String  content;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
