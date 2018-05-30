package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/6/7.
 */

public class VoucherModel {

    /**
     * data : [{"id":3,"phone":"15170193726","faceValue":1,"threshold":10,"workType":1,"startTime":1496798626000,"endTime":1499798626000,"status":0,"area":null},{"id":4,"phone":"15170193726","faceValue":1,"threshold":20,"workType":0,"startTime":1496798626000,"endTime":1499798626000,"status":0,"area":null}]
     * status : 200
     * info : 查询成功
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
         * id : 3
         * phone : 15170193726
         * faceValue : 1
         * threshold : 10
         * workType : 1
         * startTime : 1496798626000
         * endTime : 1499798626000
         * status : 0
         * area : null
         */

        private String id;
        private String phone;
        private int faceValue;
        private int threshold;
        private int workType;
        private long startTime;
        private long endTime;
        private int status;
        private String area;
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public int getFaceValue() {
            return faceValue;
        }

        public void setFaceValue(int faceValue) {
            this.faceValue = faceValue;
        }

        public int getThreshold() {
            return threshold;
        }

        public void setThreshold(int threshold) {
            this.threshold = threshold;
        }

        public int getWorkType() {
            return workType;
        }

        public void setWorkType(int workType) {
            this.workType = workType;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }
    }
}
