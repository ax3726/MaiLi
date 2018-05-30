package com.gsy.ml.model.main;

import java.util.List;

/**
 * Created by Administrator on 2017/6/15.
 */

public class AdvModel {


    /**
     * data : {"advertisList":[{"id":1,"city":"杭州市","title":"卖力VIP5折销售","content":"卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售！！！","url":"http://www.maili168.com/","createTime":1504147127000,"cTime":"2017-08-31 10:38:47","updateTime":1504147131000,"uTime":"2017-08-31 10:38:51"},{"id":2,"city":"杭州市","title":"卖力VIP5折销售","content":"卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售！！！","url":"http://www.maili168.com/","createTime":1504147182000,"cTime":"2017-08-31 10:39:42","updateTime":1504147184000,"uTime":"2017-08-31 10:39:44"}],"advertisementList":[{"id":1,"imgAddress":"http://maili-guanggao.oss-cn-shanghai.aliyuncs.com/water.png","imgUrl":"http://maili168.com/guanggaowei/index2.html","area":"","type":0},{"id":2,"imgAddress":"http://maili-guanggao.oss-cn-shanghai.aliyuncs.com/jiaju.png","imgUrl":"http://maili168.com/guanggaowei/index3.html","area":"","type":0},{"id":3,"imgAddress":"http://maili-guanggao.oss-cn-shanghai.aliyuncs.com/tao.png","imgUrl":"http://maili168.com/guanggaowei/index4.html","area":"","type":1},{"id":4,"imgAddress":"http://maili-guanggao.oss-cn-shanghai.aliyuncs.com/zhuangxiu.png","imgUrl":"http://maili168.com/guanggaowei/index5.html","area":"","type":0},{"id":5,"imgAddress":"http://maili-guanggao.oss-cn-shanghai.aliyuncs.com/jianfei.png","imgUrl":"http://maili168.com/guanggaowei/index1.html","area":"","type":0}],"advertisementMsgList":[{"id":1,"message":"好消息：卖力平台诚招城市合伙人！卖力平台诚招城市合伙人！卖力平台诚招城市合伙人！","messageUrl":"http://www.maili168.com","area":"杭州市"},{"id":2,"message":"bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb","messageUrl":"http://maili168.com","area":"杭州市"}]}
     * status : 200
     * info : 查询成功
     */

    private DataBean data;
    private String status;
    private String info;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        private List<AdvertisListBean> advertisList;
        private List<AdvertisementListBean> advertisementList;
        private List<AdvertisementMsgListBean> advertisementMsgList;

        public List<AdvertisListBean> getAdvertisList() {
            return advertisList;
        }

        public void setAdvertisList(List<AdvertisListBean> advertisList) {
            this.advertisList = advertisList;
        }

        public List<AdvertisementListBean> getAdvertisementList() {
            return advertisementList;
        }

        public void setAdvertisementList(List<AdvertisementListBean> advertisementList) {
            this.advertisementList = advertisementList;
        }

        public List<AdvertisementMsgListBean> getAdvertisementMsgList() {
            return advertisementMsgList;
        }

        public void setAdvertisementMsgList(List<AdvertisementMsgListBean> advertisementMsgList) {
            this.advertisementMsgList = advertisementMsgList;
        }

        public static class AdvertisListBean {
            /**
             * id : 1
             * city : 杭州市
             * title : 卖力VIP5折销售
             * content : 卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售，卖力VIP5折销售！！！
             * url : http://www.maili168.com/
             * createTime : 1504147127000
             * cTime : 2017-08-31 10:38:47
             * updateTime : 1504147131000
             * uTime : 2017-08-31 10:38:51
             */

            private int id;
            private String city;
            private String title;
            private String content;
            private String url;
            private long createTime;
            private String cTime;
            private long updateTime;
            private String uTime;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getCTime() {
                return cTime;
            }

            public void setCTime(String cTime) {
                this.cTime = cTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
                this.updateTime = updateTime;
            }

            public String getUTime() {
                return uTime;
            }

            public void setUTime(String uTime) {
                this.uTime = uTime;
            }
        }

        public static class AdvertisementListBean {
            /**
             * id : 1
             * imgAddress : http://maili-guanggao.oss-cn-shanghai.aliyuncs.com/water.png
             * imgUrl : http://maili168.com/guanggaowei/index2.html
             * area :
             * type : 0
             */

            private int id;
            private String imgAddress;
            private String imgUrl;
            private String area;
            private int type;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getImgAddress() {
                return imgAddress;
            }

            public void setImgAddress(String imgAddress) {
                this.imgAddress = imgAddress;
            }

            public String getImgUrl() {
                return imgUrl;
            }

            public void setImgUrl(String imgUrl) {
                this.imgUrl = imgUrl;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }

        public static class AdvertisementMsgListBean {
            /**
             * id : 1
             * message : 好消息：卖力平台诚招城市合伙人！卖力平台诚招城市合伙人！卖力平台诚招城市合伙人！
             * messageUrl : http://www.maili168.com
             * area : 杭州市
             */

            private int id;
            private String message;
            private String messageUrl;
            private String area;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }

            public String getMessageUrl() {
                return messageUrl;
            }

            public void setMessageUrl(String messageUrl) {
                this.messageUrl = messageUrl;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }
        }
    }
}
