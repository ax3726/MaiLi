package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/5/16.
 */

public class UsualAddressModel{

    /**
     * data : [{"id":1,"phone":"18397845702","contactsPhone":"18397845702","contactsName":"李涛","province":"浙江省","city":"杭州市","area":"西湖区","place":"同人精华","jing":"120","wei":"30","doorplate":"1635"}]
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
         * contactsPhone : 18397845702
         * contactsName : 李涛
         * province : 浙江省
         * city : 杭州市
         * area : 西湖区
         * place : 同人精华
         * jing : 120
         * wei : 30
         * doorplate : 1635
         */

        private int id;
        private String phone;
        private String contactsPhone;
        private String contactsName;
        private String province;
        private String city;
        private String area;
        private String place;
        private String jing;
        private String wei;
        private String doorplate;

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

        public String getContactsPhone() {
            return contactsPhone;
        }

        public void setContactsPhone(String contactsPhone) {
            this.contactsPhone = contactsPhone;
        }

        public String getContactsName() {
            return contactsName;
        }

        public void setContactsName(String contactsName) {
            this.contactsName = contactsName;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getJing() {
            return jing;
        }

        public void setJing(String jing) {
            this.jing = jing;
        }

        public String getWei() {
            return wei;
        }

        public void setWei(String wei) {
            this.wei = wei;
        }

        public String getDoorplate() {
            return doorplate;
        }

        public void setDoorplate(String doorplate) {
            this.doorplate = doorplate;
        }
    }
}
