package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 *
 传单派发：
 促销导购：
 钟点工：
 护工：
 保洁:
 服务员：
 保姆：
 */

public class PartJobModel {
    private long startTime;
    private String  timeUnit;
    private String  timeDuration;
    private String  sex;
    private String  content;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
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
