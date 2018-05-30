package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/8/16.
 */

public class AddPlatformModel {

    /**
     * status : 200
     * info : OK
     * data : {"meituan":true,"eleme":false,"baidu":false}
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
         * meituan : true
         * eleme : false
         * baidu : false
         */

        private boolean meituan;
        private boolean eleme;
        private boolean baidu;

        public boolean isMeituan() {
            return meituan;
        }

        public void setMeituan(boolean meituan) {
            this.meituan = meituan;
        }

        public boolean isEleme() {
            return eleme;
        }

        public void setEleme(boolean eleme) {
            this.eleme = eleme;
        }

        public boolean isBaidu() {
            return baidu;
        }

        public void setBaidu(boolean baidu) {
            this.baidu = baidu;
        }
    }
}
