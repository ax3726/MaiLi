package com.gsy.ml.model.main;

/**
 * Created by Administrator on 2017/4/25.
 */

public class LoginModel {

    /**
     * token : 54dffc38c497460ab9bc9a68e7b934f3
     * status : 200
     * ifWork : true
     * info : 登录成功
     */

    private String token;
    private String status;
    private boolean ifWork;//是否开工
    private boolean payPwd;//是否设置支付密码
    private boolean doingWorkType1;//是否有正在配送的单
    private String info;
    private int noReadCount;//邮件未读数量
    private int sendCount;//发单进行中数量
    private int acceptCount;//发单进行中数量
    private String ringLetter;//环信账号
    private String ringLetterPwd;//环信账号密码

    public String getRingLetter() {
        return ringLetter;
    }

    public void setRingLetter(String ringLetter) {
        this.ringLetter = ringLetter;
    }

    public String getRingLetterPwd() {
        return ringLetterPwd;
    }

    public void setRingLetterPwd(String ringLetterPwd) {
        this.ringLetterPwd = ringLetterPwd;
    }

    public boolean isPayPwd() {
        return payPwd;
    }

    public boolean isDoingWorkType1() {
        return doingWorkType1;
    }

    public void setDoingWorkType1(boolean doingWorkType1) {
        this.doingWorkType1 = doingWorkType1;
    }

    public void setPayPwd(boolean payPwd) {
        this.payPwd = payPwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isIfWork() {
        return ifWork;
    }

    public void setIfWork(boolean ifWork) {
        this.ifWork = ifWork;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getNoReadCount() {
        return noReadCount;
    }

    public void setNoReadCount(int noReadCount) {
        noReadCount = noReadCount;
    }

    public int getSendCount() {
        return sendCount;
    }

    public void setSendCount(int sendCount) {
        this.sendCount = sendCount;
    }

    public int getAcceptCount() {
        return acceptCount;
    }

    public void setAcceptCount(int acceptCount) {
        this.acceptCount = acceptCount;
    }
}
