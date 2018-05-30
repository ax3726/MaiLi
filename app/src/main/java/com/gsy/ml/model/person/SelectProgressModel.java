package com.gsy.ml.model.person;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/13.
 */

public class SelectProgressModel implements Serializable {

    /**
     * data : {"finishTime":null,"serviceTimeout":"1503308182773","pickupImgTime":1503305482782,"startWei":"30.301967","receivedPhone":"15170193726,15170193726","acceptPhone":"13968180429","workJing":"120.246452,120.112633","acceptStar":1,"sendtTime":1503302230703,"acceptTime":1503305412332,"acceptName":"郑德春","orderStatus":1,"acceptStatus":0,"startJing":"120.098821","workWei":"30.309777,30.287075","pickupTime":1503307212332,"phoneCode":"380773,932283,","receivedName":"黎明,黎明","acceptHeadUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/yjcs_1071B8D3-D816-4A51-A122-4DC79DBC4AEA_5773.png","workPlace":"普福家园北区,益乐新村(公交站)"}
     * status : 200
     * info : 保存成功
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

    public static class DataBean implements Serializable{
        /**
         * finishTime : null
         * serviceTimeout : 1503308182773
         * pickupImgTime : 1503305482782
         * startWei : 30.301967
         * receivedPhone : 15170193726,15170193726
         * acceptPhone : 13968180429
         * workJing : 120.246452,120.112633
         * acceptStar : 1
         * sendtTime : 1503302230703
         * acceptTime : 1503305412332
         * acceptName : 郑德春
         * orderStatus : 1
         * acceptStatus : 0
         * startJing : 120.098821
         * workWei : 30.309777,30.287075
         * pickupTime : 1503307212332
         * phoneCode : 380773,932283,
         * receivedName : 黎明,黎明
         * acceptHeadUrl : http://maili.oss-cn-shanghai.aliyuncs.com/yjcs_1071B8D3-D816-4A51-A122-4DC79DBC4AEA_5773.png
         * workPlace : 普福家园北区,益乐新村(公交站)
         */

        private long finishTime;
        private String serviceTimeout;
        private long pickupImgTime;
        private String startWei;
        private String receivedPhone;
        private String acceptPhone;
        private String workJing;
        private int acceptStar;
        private long sendtTime;
        private long acceptTime;
        private String acceptName;
        private int orderStatus;
        private int acceptStatus;
        private String startJing;
        private String workWei;
        private long pickupTime;
        private String phoneCode;
        private String receivedName;
        private String acceptHeadUrl;
        private String workPlace;

        public long getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(long finishTime) {
            this.finishTime = finishTime;
        }

        public String getServiceTimeout() {
            return serviceTimeout;
        }

        public void setServiceTimeout(String serviceTimeout) {
            this.serviceTimeout = serviceTimeout;
        }

        public long getPickupImgTime() {
            return pickupImgTime;
        }

        public void setPickupImgTime(long pickupImgTime) {
            this.pickupImgTime = pickupImgTime;
        }

        public String getStartWei() {
            return startWei;
        }

        public void setStartWei(String startWei) {
            this.startWei = startWei;
        }

        public String getReceivedPhone() {
            return receivedPhone;
        }

        public void setReceivedPhone(String receivedPhone) {
            this.receivedPhone = receivedPhone;
        }

        public String getAcceptPhone() {
            return acceptPhone;
        }

        public void setAcceptPhone(String acceptPhone) {
            this.acceptPhone = acceptPhone;
        }

        public String getWorkJing() {
            return workJing;
        }

        public void setWorkJing(String workJing) {
            this.workJing = workJing;
        }

        public int getAcceptStar() {
            return acceptStar;
        }

        public void setAcceptStar(int acceptStar) {
            this.acceptStar = acceptStar;
        }

        public long getSendtTime() {
            return sendtTime;
        }

        public void setSendtTime(long sendtTime) {
            this.sendtTime = sendtTime;
        }

        public long getAcceptTime() {
            return acceptTime;
        }

        public void setAcceptTime(long acceptTime) {
            this.acceptTime = acceptTime;
        }

        public String getAcceptName() {
            return acceptName;
        }

        public void setAcceptName(String acceptName) {
            this.acceptName = acceptName;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public int getAcceptStatus() {
            return acceptStatus;
        }

        public void setAcceptStatus(int acceptStatus) {
            this.acceptStatus = acceptStatus;
        }

        public String getStartJing() {
            return startJing;
        }

        public void setStartJing(String startJing) {
            this.startJing = startJing;
        }

        public String getWorkWei() {
            return workWei;
        }

        public void setWorkWei(String workWei) {
            this.workWei = workWei;
        }

        public long getPickupTime() {
            return pickupTime;
        }

        public void setPickupTime(long pickupTime) {
            this.pickupTime = pickupTime;
        }

        public String getPhoneCode() {
            return phoneCode;
        }

        public void setPhoneCode(String phoneCode) {
            this.phoneCode = phoneCode;
        }

        public String getReceivedName() {
            return receivedName;
        }

        public void setReceivedName(String receivedName) {
            this.receivedName = receivedName;
        }

        public String getAcceptHeadUrl() {
            return acceptHeadUrl;
        }

        public void setAcceptHeadUrl(String acceptHeadUrl) {
            this.acceptHeadUrl = acceptHeadUrl;
        }

        public String getWorkPlace() {
            return workPlace;
        }

        public void setWorkPlace(String workPlace) {
            this.workPlace = workPlace;
        }
    }
}
