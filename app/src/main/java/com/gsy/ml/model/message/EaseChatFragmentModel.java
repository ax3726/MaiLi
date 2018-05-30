package com.gsy.ml.model.message;

/**
 * Created by Administrator on 2017/9/11.
 */

public class EaseChatFragmentModel {

    /**
     * status : 200
     * info : OK
     * data : {"id":1,"serviceMoney":10,"discountPrice":1,"city":"杭州市","createTime":1504861840000}
     */

    private String status;
    private String info;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * serviceMoney : 10
         * discountPrice : 1
         * city : 杭州市
         * createTime : 1504861840000
         */

        private int id;
        private Double serviceMoney;
        private Double discountPrice;
        private String city;
        private long createTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public Double getServiceMoney() {
            return serviceMoney;
        }

        public void setServiceMoney(Double serviceMoney) {
            this.serviceMoney = serviceMoney;
        }

        public Double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(Double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
