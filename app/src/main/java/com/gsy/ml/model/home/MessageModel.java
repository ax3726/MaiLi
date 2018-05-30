package com.gsy.ml.model.home;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class MessageModel {

    /**
     * data : [{"id":1,"mailNumber":"admin@1495185792760","acceptPhone":"15170193726","title":"傻逼","content":"你是傻逼吗","readStatus":0,"createTime":null},{"id":2,"mailNumber":"admin@1495185795226","acceptPhone":"15170193726","title":"傻逼","content":"你是傻逼吗","readStatus":0,"createTime":null},{"id":3,"mailNumber":"admin@1495185796993","acceptPhone":"15170193726","title":"傻逼","content":"你是傻逼吗","readStatus":0,"createTime":null}]
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
         * id : 1
         * mailNumber : admin@1495185792760
         * acceptPhone : 15170193726
         * title : 傻逼
         * content : 你是傻逼吗
         * readStatus : 0
         * createTime : null
         */

        private int id;
        private String mailNumber;
        private String acceptPhone;
        private String title;
        private String content;
        private int readStatus;
        private long createTime;
        private int imgres;

        public int getImgres() {
            return imgres;
        }

        public void setImgres(int imgres) {
            this.imgres = imgres;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMailNumber() {
            return mailNumber;
        }

        public void setMailNumber(String mailNumber) {
            this.mailNumber = mailNumber;
        }

        public String getAcceptPhone() {
            return acceptPhone;
        }

        public void setAcceptPhone(String acceptPhone) {
            this.acceptPhone = acceptPhone;
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

        public int getReadStatus() {
            return readStatus;
        }

        public void setReadStatus(int readStatus) {
            this.readStatus = readStatus;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }
    }
}
