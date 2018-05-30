package com.gsy.ml.model.main;

/**
 * Created by Administrator on 2017/5/18.
 */

public class VersionModel {

    /**
     * data : {"id":1,"versionNumber":"1","versionName":"1.0","packageAddress":"http://mailiapk.oss-cn-shanghai.aliyuncs.com/maili.apk","updateContent":"更新了"}
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
         * id : 1
         * versionNumber : 1
         * versionName : 1.0
         * packageAddress : http://mailiapk.oss-cn-shanghai.aliyuncs.com/maili.apk
         * updateContent : 更新了
         */

        private int id;
        private int versionNumber;
        private String versionName;
        private String packageAddress;
        private String updateContent;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(int versionNumber) {
            this.versionNumber = versionNumber;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getPackageAddress() {
            return packageAddress;
        }

        public void setPackageAddress(String packageAddress) {
            this.packageAddress = packageAddress;
        }

        public String getUpdateContent() {
            return updateContent;
        }

        public void setUpdateContent(String updateContent) {
            this.updateContent = updateContent;
        }
    }
}
