package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2018/5/31.
 */

public class LandTenantModel {
    private String releasePurpose;
    private String landType;
    private String otherTag;
    private String landAddress;
    private String landArea;
    private String priceInfo;
    private String content;
    private long endTime;

    public String getReleasePurpose() {
        return releasePurpose;
    }

    public void setReleasePurpose(String releasePurpose) {
        this.releasePurpose = releasePurpose;
    }

    public String getLandType() {
        return landType;
    }

    public void setLandType(String landType) {
        this.landType = landType;
    }

    public String getOtherTag() {
        return otherTag;
    }

    public void setOtherTag(String otherTag) {
        this.otherTag = otherTag;
    }

    public String getLandAddress() {
        return landAddress;
    }

    public void setLandAddress(String landAddress) {
        this.landAddress = landAddress;
    }

    public String getLandArea() {
        return landArea;
    }

    public void setLandArea(String landArea) {
        this.landArea = landArea;
    }

    public String getPriceInfo() {
        return priceInfo;
    }

    public void setPriceInfo(String priceInfo) {
        this.priceInfo = priceInfo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
