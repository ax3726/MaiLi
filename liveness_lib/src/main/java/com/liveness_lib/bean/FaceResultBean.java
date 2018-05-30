package com.liveness_lib.bean;

import java.io.Serializable;

/**
 * Describe: 人脸识别后返回结果
 * User: lyj
 * Date: 2016-10-26
 */
public class FaceResultBean implements Serializable {
    /**
     * imgs : []
     * result : 验证成功
     * resultcode : 2131165363
     */

    private String result;
    private int resultcode;
    private String delta;
    private byte[] imageBestData;
    private byte[] imageEnvData;
    private byte[] imageAction1;
    private byte[] imageAction2;
    private byte[] imageAction3;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDelta() {
        return delta;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public byte[] getImageAction1() {
        return imageAction1;
    }

    public void setImageAction1(byte[] imageAction1) {
        this.imageAction1 = imageAction1;
    }

    public byte[] getImageAction2() {
        return imageAction2;
    }

    public void setImageAction2(byte[] imageAction2) {
        this.imageAction2 = imageAction2;
    }

    public byte[] getImageAction3() {
        return imageAction3;
    }

    public void setImageAction3(byte[] imageAction3) {
        this.imageAction3 = imageAction3;
    }

    public byte[] getImageBestData() {
        return imageBestData;
    }

    public void setImageBestData(byte[] imageBestData) {
        this.imageBestData = imageBestData;
    }

    public byte[] getImageEnvData() {
        return imageEnvData;
    }

    public void setImageEnvData(byte[] imageEnvData) {
        this.imageEnvData = imageEnvData;
    }

}
