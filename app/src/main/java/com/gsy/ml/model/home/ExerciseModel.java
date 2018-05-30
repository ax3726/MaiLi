package com.gsy.ml.model.home;

import java.util.List;

/**
 * Created by Administrator on 2017/9/5.
 */

public class ExerciseModel {

    /**
     * status : 200
     * info : OK
     * data : [{"id":1,"title":"活动","eventDetails":"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503651490931&di=6fc1b35d23e605e60e68cbb1f418d3e0&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F17%2F18%2F13%2F37x58PICumD_1024.jpg","activityUrl":"http://www.maili168.com","status":0,"city":"全国","activityStartTime":1503641471000,"activityEndTime":1506272461000}]
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
         * title : 活动
         * eventDetails : https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1503651490931&di=6fc1b35d23e605e60e68cbb1f418d3e0&imgtype=0&src=http%3A%2F%2Fpic.qiantucdn.com%2F58pic%2F17%2F18%2F13%2F37x58PICumD_1024.jpg
         * activityUrl : http://www.maili168.com
         * status : 0
         * city : 全国
         * activityStartTime : 1503641471000
         * activityEndTime : 1506272461000
         */

        private int id;
        private String title;
        private String eventDetails;
        private String activityUrl;
        private int status;
        private String city;
        private long activityStartTime;
        private long activityEndTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getEventDetails() {
            return eventDetails;
        }

        public void setEventDetails(String eventDetails) {
            this.eventDetails = eventDetails;
        }

        public String getActivityUrl() {
            return activityUrl;
        }

        public void setActivityUrl(String activityUrl) {
            this.activityUrl = activityUrl;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public long getActivityStartTime() {
            return activityStartTime;
        }

        public void setActivityStartTime(long activityStartTime) {
            this.activityStartTime = activityStartTime;
        }

        public long getActivityEndTime() {
            return activityEndTime;
        }

        public void setActivityEndTime(long activityEndTime) {
            this.activityEndTime = activityEndTime;
        }
    }
}
