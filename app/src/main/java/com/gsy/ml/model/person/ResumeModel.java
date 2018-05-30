package com.gsy.ml.model.person;

/**
 * Created by Administrator on 2017/5/5.
 */

public class ResumeModel {


    /**
     * data : {"userInfo":{"id":1,"phone":"15170193726","nickname":"黎明","name":"黎明","sex":"男","idNumber":"360734199602211310","city":"杭州市","idType":"在校学生","headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_img0343ee7daf46455a9686e7c5c8611662.jpg","headUrlStatus":1,"idcardBeforeUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/is_number_img5c33f6d530944a0cb2a7014cd26b5758.jpg","idcardBeforeUrlStatus":1,"idcardAfterUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/is_number_img88f75e3cc1164bb1a39de391337c8589.jpg","idcardAfterUrlStatus":1,"idcardHoldUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/is_number_imgc7c09e679fb342feb009fcd646865fe7.jpg","idcardHoldUrlStatus":1,"createTime":1495089512187,"remarks":"","workExp":"来来来","graduateSchool":"南昌大学","major":"软件技术","qualifications":"博士","schoolTime":"2014年,2017年","schoolLife":"是啊","personalEvaluation":"","personalHobbies":"","certificate":"","confinement":0,"confinementTime":null,"integral":80,"star":1,"status":0,"cancelNum":0},"userJob":"1,"}
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
         * userInfo : {"id":1,"phone":"15170193726","nickname":"黎明","name":"黎明","sex":"男","idNumber":"360734199602211310","city":"杭州市","idType":"在校学生","headUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/head_img0343ee7daf46455a9686e7c5c8611662.jpg","headUrlStatus":1,"idcardBeforeUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/is_number_img5c33f6d530944a0cb2a7014cd26b5758.jpg","idcardBeforeUrlStatus":1,"idcardAfterUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/is_number_img88f75e3cc1164bb1a39de391337c8589.jpg","idcardAfterUrlStatus":1,"idcardHoldUrl":"http://maili.oss-cn-shanghai.aliyuncs.com/is_number_imgc7c09e679fb342feb009fcd646865fe7.jpg","idcardHoldUrlStatus":1,"createTime":1495089512187,"remarks":"","workExp":"来来来","graduateSchool":"南昌大学","major":"软件技术","qualifications":"博士","schoolTime":"2014年,2017年","schoolLife":"是啊","personalEvaluation":"","personalHobbies":"","certificate":"","confinement":0,"confinementTime":null,"integral":80,"star":1,"status":0,"cancelNum":0}
         * userJob : 1,
         */

        private UserInfoBean userInfo;
        private String userJob;

        public UserInfoBean getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfoBean userInfo) {
            this.userInfo = userInfo;
        }

        public String getUserJob() {
            return userJob;
        }

        public void setUserJob(String userJob) {
            this.userJob = userJob;
        }

        public static class UserInfoBean {
            /**
             * id : 1
             * phone : 15170193726
             * nickname : 黎明
             * name : 黎明
             * sex : 男
             * idNumber : 360734199602211310
             * city : 杭州市
             * idType : 在校学生
             * headUrl : http://maili.oss-cn-shanghai.aliyuncs.com/head_img0343ee7daf46455a9686e7c5c8611662.jpg
             * headUrlStatus : 1
             * idcardBeforeUrl : http://maili.oss-cn-shanghai.aliyuncs.com/is_number_img5c33f6d530944a0cb2a7014cd26b5758.jpg
             * idcardBeforeUrlStatus : 1
             * idcardAfterUrl : http://maili.oss-cn-shanghai.aliyuncs.com/is_number_img88f75e3cc1164bb1a39de391337c8589.jpg
             * idcardAfterUrlStatus : 1
             * idcardHoldUrl : http://maili.oss-cn-shanghai.aliyuncs.com/is_number_imgc7c09e679fb342feb009fcd646865fe7.jpg
             * idcardHoldUrlStatus : 1
             * createTime : 1495089512187
             * remarks :
             * workExp : 来来来
             * graduateSchool : 南昌大学
             * major : 软件技术
             * qualifications : 博士
             * schoolTime : 2014年,2017年
             * schoolLife : 是啊
             * personalEvaluation :
             * personalHobbies :
             * certificate :
             * confinement : 0
             * confinementTime : null
             * integral : 80
             * star : 1
             * status : 0
             * cancelNum : 0
             */

            private int id;
            private String phone;
            private String nickname;
            private String name;
            private String sex;
            private String idNumber;
            private String city;
            private String idType;
            private String headUrl;
            private int headUrlStatus;
            private String idcardBeforeUrl;
            private int idcardBeforeUrlStatus;
            private String idcardAfterUrl;
            private int idcardAfterUrlStatus;
            private String idcardHoldUrl;
            private int idcardHoldUrlStatus;
            private long createTime;
            private String remarks;
            private String workExp;
            private String graduateSchool;
            private String major;
            private String qualifications;
            private String schoolTime;
            private String schoolLife;
            private String personalEvaluation;
            private String personalHobbies;
            private String certificate;
            private int confinement;
            private Object confinementTime;
            private int integral;
            private int star;
            private int status;
            private int cancelNum;

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

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
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

            public String getIdNumber() {
                return idNumber;
            }

            public void setIdNumber(String idNumber) {
                this.idNumber = idNumber;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getIdType() {
                return idType;
            }

            public void setIdType(String idType) {
                this.idType = idType;
            }

            public String getHeadUrl() {
                return headUrl;
            }

            public void setHeadUrl(String headUrl) {
                this.headUrl = headUrl;
            }

            public int getHeadUrlStatus() {
                return headUrlStatus;
            }

            public void setHeadUrlStatus(int headUrlStatus) {
                this.headUrlStatus = headUrlStatus;
            }

            public String getIdcardBeforeUrl() {
                return idcardBeforeUrl;
            }

            public void setIdcardBeforeUrl(String idcardBeforeUrl) {
                this.idcardBeforeUrl = idcardBeforeUrl;
            }

            public int getIdcardBeforeUrlStatus() {
                return idcardBeforeUrlStatus;
            }

            public void setIdcardBeforeUrlStatus(int idcardBeforeUrlStatus) {
                this.idcardBeforeUrlStatus = idcardBeforeUrlStatus;
            }

            public String getIdcardAfterUrl() {
                return idcardAfterUrl;
            }

            public void setIdcardAfterUrl(String idcardAfterUrl) {
                this.idcardAfterUrl = idcardAfterUrl;
            }

            public int getIdcardAfterUrlStatus() {
                return idcardAfterUrlStatus;
            }

            public void setIdcardAfterUrlStatus(int idcardAfterUrlStatus) {
                this.idcardAfterUrlStatus = idcardAfterUrlStatus;
            }

            public String getIdcardHoldUrl() {
                return idcardHoldUrl;
            }

            public void setIdcardHoldUrl(String idcardHoldUrl) {
                this.idcardHoldUrl = idcardHoldUrl;
            }

            public int getIdcardHoldUrlStatus() {
                return idcardHoldUrlStatus;
            }

            public void setIdcardHoldUrlStatus(int idcardHoldUrlStatus) {
                this.idcardHoldUrlStatus = idcardHoldUrlStatus;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public String getWorkExp() {
                return workExp;
            }

            public void setWorkExp(String workExp) {
                this.workExp = workExp;
            }

            public String getGraduateSchool() {
                return graduateSchool;
            }

            public void setGraduateSchool(String graduateSchool) {
                this.graduateSchool = graduateSchool;
            }

            public String getMajor() {
                return major;
            }

            public void setMajor(String major) {
                this.major = major;
            }

            public String getQualifications() {
                return qualifications;
            }

            public void setQualifications(String qualifications) {
                this.qualifications = qualifications;
            }

            public String getSchoolTime() {
                return schoolTime;
            }

            public void setSchoolTime(String schoolTime) {
                this.schoolTime = schoolTime;
            }

            public String getSchoolLife() {
                return schoolLife;
            }

            public void setSchoolLife(String schoolLife) {
                this.schoolLife = schoolLife;
            }

            public String getPersonalEvaluation() {
                return personalEvaluation;
            }

            public void setPersonalEvaluation(String personalEvaluation) {
                this.personalEvaluation = personalEvaluation;
            }

            public String getPersonalHobbies() {
                return personalHobbies;
            }

            public void setPersonalHobbies(String personalHobbies) {
                this.personalHobbies = personalHobbies;
            }

            public String getCertificate() {
                return certificate;
            }

            public void setCertificate(String certificate) {
                this.certificate = certificate;
            }

            public int getConfinement() {
                return confinement;
            }

            public void setConfinement(int confinement) {
                this.confinement = confinement;
            }

            public Object getConfinementTime() {
                return confinementTime;
            }

            public void setConfinementTime(Object confinementTime) {
                this.confinementTime = confinementTime;
            }

            public int getIntegral() {
                return integral;
            }

            public void setIntegral(int integral) {
                this.integral = integral;
            }

            public int getStar() {
                return star;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getCancelNum() {
                return cancelNum;
            }

            public void setCancelNum(int cancelNum) {
                this.cancelNum = cancelNum;
            }
        }
    }
}
