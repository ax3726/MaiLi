package com.gsy.ml.model.person;

import java.util.List;

/**
 * Created by Administrator on 2017/9/2.
 */

public class OrderChatListModel {


    /**
     * status : 200
     * info : OK
     * data : {"total":2,"rows":[{"id":1,"orderId":"1504252685092683333","sendPhone":15122223333,"sendRingLetter":"ml15122223333649","acceptPhone":15170193726,"acceptRingLetter":"ml15170193726300","createtime":1504253174986,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg","nickName":"哈喽","name":"黎明","sex":"男","star":1},{"id":2,"orderId":"1504252685092683333","sendPhone":15122223333,"sendRingLetter":"ml15122223333649","acceptPhone":15170193726,"acceptRingLetter":"ml15170193726300","createtime":1504333911973,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg","nickName":"哈喽","name":"黎明","sex":"男","star":1}]}
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
         * total : 2
         * rows : [{"id":1,"orderId":"1504252685092683333","sendPhone":15122223333,"sendRingLetter":"ml15122223333649","acceptPhone":15170193726,"acceptRingLetter":"ml15170193726300","createtime":1504253174986,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg","nickName":"哈喽","name":"黎明","sex":"男","star":1},{"id":2,"orderId":"1504252685092683333","sendPhone":15122223333,"sendRingLetter":"ml15122223333649","acceptPhone":15170193726,"acceptRingLetter":"ml15170193726300","createtime":1504333911973,"headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg","nickName":"哈喽","name":"黎明","sex":"男","star":1}]
         */

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean {
            /**
             * id : 1
             * orderId : 1504252685092683333
             * sendPhone : 15122223333
             * sendRingLetter : ml15122223333649
             * acceptPhone : 15170193726
             * acceptRingLetter : ml15170193726300
             * createtime : 1504253174986
             * headUrl : http://maili.oss-cn-shanghai.aliyuncs.com/head_imga8f853cca2bc40d995a22a3674c7037a.jpg
             * nickName : 哈喽
             * name : 黎明
             * sex : 男
             * star : 1
             */

            private int id;
            private String orderId;
            private String sendPhone;
            private String sendRingLetter;
            private String acceptPhone;
            private String acceptRingLetter;
            private long createtime;
            private String headUrl;
            private String nickName;
            private String name;
            private String sex;
            private int star;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getSendPhone() {
                return sendPhone;
            }

            public void setSendPhone(String sendPhone) {
                this.sendPhone = sendPhone;
            }

            public String getSendRingLetter() {
                return sendRingLetter;
            }

            public void setSendRingLetter(String sendRingLetter) {
                this.sendRingLetter = sendRingLetter;
            }

            public String getAcceptPhone() {
                return acceptPhone;
            }

            public void setAcceptPhone(String acceptPhone) {
                this.acceptPhone = acceptPhone;
            }

            public String getAcceptRingLetter() {
                return acceptRingLetter;
            }

            public void setAcceptRingLetter(String acceptRingLetter) {
                this.acceptRingLetter = acceptRingLetter;
            }

            public long getCreatetime() {
                return createtime;
            }

            public void setCreatetime(long createtime) {
                this.createtime = createtime;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }
        }
    }
}
