package com.gsy.ml.model.home;

/**
 * Created by Administrator on 2017/5/22.
 */

public class MessageInfoModel {


    /**
     * data : {"id":161,"mailNumber":"admin@1495422464823","acceptPhone":"15170193726","title":"标题","content":"在这里输入发送内容...","readStatus":0,"createTime":1495422464826}
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
        /**
         * id : 161
         * mailNumber : admin@1495422464823
         * acceptPhone : 15170193726
         * title : 标题
         * content : 在这里输入发送内容...
         * readStatus : 0
         * createTime : 1495422464826
         */

        private int id;
        private String mailNumber;
        private String acceptPhone;
        private String title;
        private String content;
        private int readStatus;
        private long createTime;

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
