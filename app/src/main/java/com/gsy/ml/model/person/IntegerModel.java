package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/5/6.
 */

public class IntegerModel {

    /**
     * status : 200
     * data : {"list":[{"phone":"18397845702","integralNum":12,"integralType":1,"integralExplain":"ewrqewrqwer","createTime":1494056710000}],"integral":134}
     * info : 查询成功
     */

    private String status;
    private DataBean data;
    private String info;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class DataBean {
        /**
         * list : [{"phone":"18397845702","integralNum":12,"integralType":1,"integralExplain":"ewrqewrqwer","createTime":1494056710000}]
         * integral : 134
         */

        private int integral;
        private List<ListBean> list;

        public int getIntegral() {
            return integral;
        }

        public void setIntegral(int integral) {
            this.integral = integral;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * phone : 18397845702
             * integralNum : 12
             * integralType : 1
             * integralExplain : ewrqewrqwer
             * createTime : 1494056710000
             */

            private String phone;
            private int integralNum;
            private int integralType;
            private String integralExplain;
            private long createTime;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public int getIntegralNum() {
                return integralNum;
            }

            public void setIntegralNum(int integralNum) {
                this.integralNum = integralNum;
            }

            public int getIntegralType() {
                return integralType;
            }

            public void setIntegralType(int integralType) {
                this.integralType = integralType;
            }

            public String getIntegralExplain() {
                return integralExplain;
            }

            public void setIntegralExplain(String integralExplain) {
                this.integralExplain = integralExplain;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }
        }
    }
}
