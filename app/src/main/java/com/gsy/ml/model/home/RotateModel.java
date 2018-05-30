package com.gsy.ml.model.home;

import java.util.List;

/**
 * Created by Administrator on 2017/5/19.
 */

public class RotateModel {


    /**
     * data : [{"id":1,"rotateImg":"http://maili-sysem-imgs.oss-cn-shanghai.aliyuncs.com/1.jpg","rotateImgUrl":null},{"id":2,"rotateImg":"http://maili-sysem-imgs.oss-cn-shanghai.aliyuncs.com/2.jpg","rotateImgUrl":""},{"id":3,"rotateImg":"http://maili-sysem-imgs.oss-cn-shanghai.aliyuncs.com/3.jpg","rotateImgUrl":null}]
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
         * rotateImg : http://maili-sysem-imgs.oss-cn-shanghai.aliyuncs.com/1.jpg
         * rotateImgUrl : null
         */

        private int id;
        private String rotateImg;
        private String rotateImgUrl;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getRotateImg() {
            return rotateImg;
        }

        public void setRotateImg(String rotateImg) {
            this.rotateImg = rotateImg;
        }

        public String getRotateImgUrl() {
            return rotateImgUrl;
        }

        public void setRotateImgUrl(String rotateImgUrl) {
            this.rotateImgUrl = rotateImgUrl;
        }
    }
}
