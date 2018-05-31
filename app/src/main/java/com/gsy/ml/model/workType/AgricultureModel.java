package com.gsy.ml.model.workType;

/**
 * Created by Administrator on 2018/5/31.
 */

public class AgricultureModel {
    private String releasePurpose;
    private String productType;
    private String productName;
    private String origin;
    private String productNum;
    private String priceInfo;
    private String content;
    private long endTime;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getReleasePurpose() {
        return releasePurpose;
    }

    public void setReleasePurpose(String releasePurpose) {
        this.releasePurpose = releasePurpose;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
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
