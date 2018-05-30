package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 */

public class MoTeModel {
    private long startTime;
    private String timeUnit;
    private String timeDuration;
    private String sex;
    private String statureContent;
    private String content;

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

    public String getStatureContent() {
        return statureContent;
    }

    public void setStatureContent(String statureContent) {
        this.statureContent = statureContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
