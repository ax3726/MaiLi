package com.gsy.ml.model.person;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/5.
 */

public class EducationModel implements Serializable{
    private String name;
    private String zhuanye;
    private String xueli;
    private String time;
    private String content;

    public EducationModel(String name, String zhuanye, String xueli, String time, String content) {
        this.name = name;
        this.zhuanye = zhuanye;
        this.xueli = xueli;
        this.time = time;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZhuanye() {
        return zhuanye;
    }

    public void setZhuanye(String zhuanye) {
        this.zhuanye = zhuanye;
    }

    public String getXueli() {
        return xueli;
    }

    public void setXueli(String xueli) {
        this.xueli = xueli;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
