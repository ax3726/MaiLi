package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BankCardModel {

    /**
     * data : [{"id":1,"phone":"18397845702","bankNum":"18397845702","bankType":3}]
     * status : 200
     * info : 操作成功
     */

    private String status;
    private String info;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * phone : 18397845702
         * bankNum : 18397845702
         * bankType : 3
         */

        private int id;
        private String phone;
        private String bankNum;
        private String bankType;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getBankNum() {
            return bankNum;
        }

        public void setBankNum(String bankNum) {
            this.bankNum = bankNum;
        }

        public String getBankType() {
            return bankType;
        }

        public void setBankType(String bankType) {
            this.bankType = bankType;
        }
    }
}
