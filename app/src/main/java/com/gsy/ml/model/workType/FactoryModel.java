package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 * 工厂用工：
 技工：
 搬运工：
 */

public class FactoryModel {
    private long startTime;
    private String timeUnit;
    private String timeDuration;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
