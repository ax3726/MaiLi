package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2017/8/30.
 * 家教
 */

public class TutorModel {
    private String grade;
    private String  course;
    private String  stageTime;
    private String  dayNum;
    private String  sex;
    private long  startTime;
    private String  specialContent;
    private String  content;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getStageTime() {
        return stageTime;
    }

    public void setStageTime(String stageTime) {
        this.stageTime = stageTime;
    }

    public String getDayNum() {
        return dayNum;
    }

    public void setDayNum(String dayNum) {
        this.dayNum = dayNum;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getSpecialContent() {
        return specialContent;
    }

    public void setSpecialContent(String specialContent) {
        this.specialContent = specialContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
