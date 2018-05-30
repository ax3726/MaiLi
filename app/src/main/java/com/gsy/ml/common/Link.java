package com.gsy.ml.common;

/**
 * Http 地址 接口路径
 * Created by Administrator on 2017/4/10.
 */

public class Link {
    public static final String HEADPHOTOURL = "head_img";
    public static final String ISNUMBERPHOTOURL = "is_number_img";
    public static final String CERTIFICATE = "certificate_img";

    //阿里云服务器上传参数
    public static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    public static final String accessKeyId = "LTAIxyVWftgP7fR1";
    public static final String accessKeySecret = "198H4pgnQ6K8aYDF8ALNiYOjgHDban";
    public static final String bucketName = "maili";
    public static final String PRICEINFO = "http://211.155.232.67:8888/jingxian/explain/workMoney/";//工种价格
    //  public static final String PRICEINFO = "https://maili.s1.natapp.cc/jingxian/explain/workMoney/";//工种价格
    public static final String AGREEINFO = "http://211.155.232.67:8888/jingxian/explain/";  //协议  积分

    // public static final String SEREVE = "http://192.168.1.188:8080/jingxian/maili/";
     //public static final String SEREVE = "https://maili.s1.natapp.cc/jingxian/maili/";
    public static final String SEREVE = "http://211.155.232.67:8888/jingxian/maili/";


    public static final String FINDCHATLIST = SEREVE + "findCommunicateList.shtml";// 订单聊天沟通人数列表
    public static final String CREATECOMMUNICATE = SEREVE + "createCommunicate.shtml";// 建立沟通关系
    public static final String USERREPORT = SEREVE + "userReport.shtml";// 举报
    public static final String UPDATEWORKTOTALCOST = SEREVE + "updateWorkTotalCost.shtml";// 修改订单价格
    public static final String CHOOSEPAY = SEREVE + "choiceO2O.shtml";// 线上线下支付
    public static final String GETACTIVITYLIST = SEREVE + "activityList.shtml";// 活动列表接口
    public static final String GETORDERPUSH = SEREVE + "orderPush.shtml";// 推送订单接口
    public static final String FINDORDERINFO = SEREVE + "findOrderInfo.shtml";// 查看订单详情
    public static final String SEESERVICECHARGE = SEREVE + "seeServiceCharge.shtml";// 用户的查看信息费


    public static final String STOREAUTHORIZATION = SEREVE + "storeAuthorization.shtml";// 美团商户授权
    public static final String ADDELESTOREBIN = SEREVE + "eleme/elemeToken.shtml";// 饿了么商户授权
    public static final String FINDBINDING = SEREVE + "findBinding.shtml";// 查询用户是否绑定门店
    public static final String FINDVALIDORDER = SEREVE + "findValidOrder.shtml";// 查询用户的有效订单
    public static final String GETLATLON = SEREVE + "latitudeLongitude.shtml";// 根据地址地理编码出经纬度
    public static final String CHECKPICKUPTIME = SEREVE + "pickupOrder.shtml";// 取件超时
    public static final String CHECKDELVERYTIME = SEREVE + "sendTimeout.shtml";// 送件超时
    public static final String PICKUPTIMEODERS = SEREVE + "pickupOrders.shtml";// 送件查询接口
    public static final String NEWACTIVITY = SEREVE + "userActivity.shtml";// 活动接口

    public static final String GETCODE = SEREVE + "getPhoneCode.shtml";//获取验证码
    public static final String GETREGISTER = SEREVE + "register.shtml";//注册
    public static final String LOGIN = SEREVE + "login.shtml";//登陆
    public static final String UPDATEPWD = SEREVE + "userUpdatePwd.shtml";//忘记密码
    public static final String UPDATEPWD1 = SEREVE + "userUpdatePwd1.shtml";//修改密码

    public static final String UPDATEINFO = SEREVE + "userUpdateInfo.shtml";//完善信息
    public static final String GETUSERINFO = SEREVE + "selectUserInfo.shtml";//获取用户信息
    public static final String SENDORDERS = SEREVE + "sendOrder.shtml";//发布订单
    public static final String COMMITORDERS = SEREVE + "acceptOrder.shtml";//提交订单
    public static final String COUNTPRICE = SEREVE + "gistributionMoney.shtml";//计算价格
    public static final String UPDATELOCATION = SEREVE + "updateUserPlace.shtml";//更新用户地理位置
    public static final String SELECTORDER = SEREVE + "selectMyOrder.shtml";//查询个人订单接口名
    public static final String CANCELORDER1 = SEREVE + "cancelOrder1.shtml";//发单人取消订单
    public static final String CANCELORDER2 = SEREVE + "cancelOrder2.shtml";//接单人取消订单
    public static final String EVALUATE = SEREVE + "evaluate.shtml";//评论
    public static final String CONFIRORDER = SEREVE + "confirOrder.shtml"; //确认订单
    public static final String FINISHORDER = SEREVE + "finishOrder.shtml"; //完成订单

    public static final String SELECTODAY = SEREVE + "selectMyToday.shtml";//查询余额接口
    public static final String SELECTORDERS = SEREVE + "selectSendOrders.shtml";//查询订单接口
    public static final String SELECTORDERSFUZZY = SEREVE + "sendOrdersFuzzyQuery.shtml";//模糊查询订单接口
    public static final String SELECTWALLET = SEREVE + "selectMyWallet.shtml";//我的钱包接口
    public static final String UPDATESESUME = SEREVE + "saveResume.shtml";//更新简历信息
    public static final String PARTICULARS = SEREVE + "selectOrderInfo.shtml";//接单详情借口
    public static final String SELECTSESUME = SEREVE + "selectResume.shtml";//获取简历信息
    public static final String INTEGER = SEREVE + "selectMyIntegral.shtml";//积分详情借口
    public static final String LABOUR = SEREVE + "updateUserIfWork.shtml";//开工收工状态接口
    public static final String NOREAD = SEREVE + "mailOrderCount.shtml";//获取首页未读
    public static final String TOTALTUTOR = SEREVE + "getTutorMoney.shtml";//家教计算价格
    public static final String GETMONEY = SEREVE + "getMoney.shtml";//计算价格
    public static final String CLAIMGOODS = SEREVE + "selectDistribution.shtml";//取货接口
    public static final String SELECTPROGRESS = SEREVE + "distributionSchedule.shtml";//查询同城配送 进度接口
    public static final String INSERTORDERIMG = SEREVE + "insertDistributionImg.shtml";//插入配送图片表
    public static final String CHECKCODE = SEREVE + "distributionImgCode.shtml";//同城配送图片  验证接口
    public static final String COMMONADDRESS = SEREVE + "commonAddress.shtml";//常用地址
    public static final String VERSIONUPDATE = SEREVE + "versionUpdate.shtml";//版本检测
    public static final String FEEDBACK = SEREVE + "feedback.shtml";//常用地址
    public static final String SELECTROTATE = SEREVE + "selectRotate.shtml";//轮播图
    public static final String SELECTMESSAGE = SEREVE + "selectMyMail.shtml";//查询通知
    public static final String SELECTMESSAGEINFO = SEREVE + "selectMailInfo.shtml";//查询通知详情
    public static final String ALIPAY = SEREVE + "app_alipay.shtml";//支付宝支付
    public static final String WEXPAY = SEREVE + "app_wxpay.shtml";//微信支付
    public static final String SELECTWORKJOB = SEREVE + "selectWorkJob.shtml";//查询职位预选
    public static final String SAVEWORKJOB = SEREVE + "saveWorkJob.shtml";//保存职位预选
    public static final String SELECTMYAREA = SEREVE + "selectMyArea.shtml";//查询预选地区接口
    public static final String SAVEMYAREA = SEREVE + "insertMyArea.shtml";//新增或者修改预选地区接口。
    public static final String INSERTCOOPERATIVE = SEREVE + "insertCooperative.shtml";//招商加盟
    public static final String MYBANK = SEREVE + "myBank.shtml";//招商加盟
    public static final String CASHBANLANCE = SEREVE + "insertCashOut.shtml";//金额提现
    public static final String RECHARGEREWARD = SEREVE + "rechargeReward.shtml";//充值活动说明
    public static final String SELECTMYTEAM = SEREVE + "selectMyTeam.shtml";//查询团队奖金和自己儿子孙子的人数
    public static final String ALLVOUCHER = SEREVE + "myNoUseCashCoupon.shtml";//查询所有未使用的卡卷
    public static final String VOUCHERBYWORK = SEREVE + "myNoUseCashCouponByWork.shtml";//查询工作未使用的卡卷
    public static final String NEWCARD = SEREVE + "myNoReadCashCoupon.shtml";//查询是否有新的卡卷
    public static final String ERRORLOG = SEREVE + "insertAndroidLog.shtml";//捕获异常信息
    public static final String SELECTADV = SEREVE + "selectAdvertisement.shtml";//查询广告信息
    public static final String USERPAYPWD = SEREVE + "userPayPwd.shtml";//支付密码
    public static final String CHECKPAYPWD = SEREVE + "userPayPwdJudge.shtml";//验证支付密码
    public static final String CARDRENZHEN = SEREVE + "faceRecognition.shtml";//身份认证
    public static final String CARDRENZHEN1 = SEREVE + "faceRecognition1.shtml";//活体认证
    public static final String ALIIMAGEURL = "http://maili.oss-cn-shanghai.aliyuncs.com/";


}
