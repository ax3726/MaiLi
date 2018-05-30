package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 */

public class LatLonModel {

    /**
     * status : 200
     * info : OK
     * data : [{"province":"浙江省","city":"杭州市","district":"西湖区","Latitude":"30.303274","Longitude":"120.098648"},{"province":"浙江省","city":"杭州市","district":"江干区","Latitude":"30.308506","Longitude":"120.247902"}]
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
         * province : 浙江省
         * city : 杭州市
         * district : 西湖区
         * Latitude : 30.303274
         * Longitude : 120.098648
         */

        private String province;
        private String city;
        private String district;
        private String Latitude;
        private String Longitude;

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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }
    }
}
