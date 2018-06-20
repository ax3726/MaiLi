package com.gsy.ml.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.gsy.ml.model.main.AdvModel;
import com.gsy.ml.model.main.OrderInfoModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.main.ErrorLogPresenter;
import com.gsy.ml.ui.main.MainActivity;
import com.gsy.ml.ui.main.WelcomeActivity;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.OrdersDialog;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.model.EaseNotifier;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import ml.gsy.com.library.utils.CacheUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/10.
 */

public class MaiLiApplication extends MultiDexApplication {
    public static String BASEPHOTOURL = "";//存放图片
    public static List<Activity> mList = new LinkedList<>();
    public static Context appliactionContext;
    private static MaiLiApplication instance;

    public static MaiLiApplication getInstance() {
        return instance;
    }

    public List<Activity> getActivityList() {
        return mList;
    }

    public static String RegistrationID = "";
    public static String Token = "";

    public static boolean IsWork = false;//开工和收工状态
    public static boolean IsPayPwd = false;//是否设置支付密码
    public static boolean DoingWorkType1 = false;//是否有正在配送的单

    public static OrderInfoModel mOrderInfoModel;//订单信息
    public static AMapLocation mAMapLocation;//定位信息
    public OrdersDialog mOrdersDialog = null;//抢单弹框
    public InformationDialog mInformationDialog = null;//订单状态弹框
    private UserInfoModel userInfo = null;
    public List<AdvModel.DataBean.AdvertisListBean> mMessageAdvList; //消息广告栏
    public String mHuanxin = "";//环信
    public String mHuanxinPwd = "";//环信密码

    @Override
    public void onCreate() {
        super.onCreate();

        appliactionContext = this;
        instance = this;
        Thread.setDefaultUncaughtExceptionHandler(restartHandler); // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
        BASEPHOTOURL = Utils.getCacheDirectory(this, Environment.DIRECTORY_PICTURES).getAbsolutePath();

        if (System.currentTimeMillis() >= 1529841600000L) {//大于当前时间退出APP
            exit();
            android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出

        }

        initHot();//初始化热修复
        initJpush();//初始化极光
        initKDXF();//初始化科大讯飞
        initHuanXin();//初始化环信
        //缓存初始化
        CacheUtils.getInstance().init(CacheUtils.CacheMode.CACHE_MAX,
                Utils.getCacheDirectory(this, Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {
                mList.add(activity);
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
            }

            public void onActivityPaused(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                mList.remove(activity);
            }
        });

    }


    /**
     * 初始化热修复
     */
    private void initHot() {
        SophixManager.getInstance().setContext(this)
                .setAppVersion(Utils.getVersionName(this))
                .setAesKey(null)
                .setEnableDebug(false)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            //  Toast.makeText(appliactionContext, "补丁加载成功！", Toast.LENGTH_SHORT).show();
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            //Toast.makeText(appliactionContext, "补丁加载成功,需要重启才可生效！", Toast.LENGTH_SHORT).show();
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                        } else {
                            // Toast.makeText(appliactionContext, "补丁加载错误！" + info, Toast.LENGTH_SHORT).show();
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        // queryAndLoadNewPatch不可放在attachBaseContext 中，否则无网络权限，建议放在后面任意时刻，如onCreate中
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    /**
     * 初始化环信
     */
    private void initHuanXin() {
        EaseUI.getInstance().init(this, null, new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getDisplayedText(EMMessage message) {
                return null;
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
            }

            @Override
            public String getTitle(EMMessage message) {
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                return 0;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                Intent intent1 = null;
                if (mList.size() > 0) {
                    if (isMainActivity()) {
                        intent1 = new Intent(appliactionContext, MainActivity.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    }
                } else {
                    intent1 = new Intent(appliactionContext, WelcomeActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                }
                return intent1;
            }
        });

    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private Thread.UncaughtExceptionHandler restartHandler = new Thread.UncaughtExceptionHandler() {
        public void uncaughtException(Thread thread, Throwable ex) {
            final String message = thread.getName() + ":" + ex.getMessage();
            Log.e("restartHandler", message);

            TelephonyManager mTm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
            String imei = mTm.getDeviceId();//手机设备号
            // String imsi = mTm.getSubscriberId();
            String mtyb = android.os.Build.BRAND;//手机品牌
            String version = android.os.Build.VERSION.RELEASE;//手机版本号
            String mtype = android.os.Build.MODEL; // 手机型号
            // String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
            String appversion = Utils.getVersionName(getApplicationContext());//APP版本号
            String networkType = Utils.GetNetworkType(appliactionContext);//手机网络类型
            new ErrorLogPresenter().getNewCard(getUserInfo().getPhone(), mtyb, mtype, networkType, message);
            exit();
            android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
        }
    };

    public void initJpush() {
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
        // 通过极光推送，推送了很多通知到客户端时，如果用户不去处理，就会有很多保留在那里。
//        新版本 SDK (v1.3.0) 增加此功能，限制保留的通知条数。默认为保留最近 5 条通知。
        JPushInterface.setLatestNotificationNumber(appliactionContext, 0);
        RegistrationID = JPushInterface.getRegistrationID(appliactionContext);

    }

    private void initKDXF() {
        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=592247f8");
    }

    public void exit() {
        try {
            for (Activity activity : mList)
                if (activity != null)
                    activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    /**
     * 保存用户信息
     *
     * @param user
     */
    public void setUser(UserInfoModel user) {
        this.userInfo = user;
    }

    /**
     * 保存地理位置
     *
     * @param userPlaceBean
     */
    public void setUserPlace(UserInfoModel.UserPlaceBean userPlaceBean) {
        this.userInfo.setUserPlace(userPlaceBean);
    }


    /**
     * 读取用户信息
     *
     * @return
     */
    public UserInfoModel getUser() {
        return userInfo;
    }

    /**
     * 获取用户地理位置
     *
     * @return
     */
    public UserInfoModel.UserPlaceBean getUserPlace() {
        if (userInfo.getUserPlace() == null) {
            userInfo.setUserPlace(new UserInfoModel.UserPlaceBean());
        }
        return userInfo.getUserPlace();
    }

    /**
     * 获取用户表信息
     *
     * @return
     */
    public UserInfoModel.UserInfoBean getUserInfo() {
        if (userInfo.getUserInfo() == null) {
            userInfo.setUserInfo(new UserInfoModel.UserInfoBean());
        }
        return userInfo.getUserInfo();
    }

    /**
     * 获取用户余额信息
     *
     * @return
     */
    public UserInfoModel.UserBalanceBean getUserBalance() {
        if (userInfo.getUserBalance() == null) {
            userInfo.setUserBalance(new UserInfoModel.UserBalanceBean());
        }
        return userInfo.getUserBalance();
    }

    public static void backToLogin(Context context, Intent intent) {
        context.startActivity(intent);
        try {
            for (Activity activity : mList)
                if (activity != null)
                    activity.finish();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    public boolean isMainActivity() {
        for (Activity aty : MaiLiApplication.mList) {
            if (aty instanceof MainActivity) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    /**
     * 内存不够时\
     * TRIM_MEMORY_COMPLETE：内存不足，并且该进程在后台进程列表最后一个，马上就要被清理
     * TRIM_MEMORY_MODERATE：内存不足，并且该进程在后台进程列表的中部。
     * TRIM_MEMORY_BACKGROUND：内存不足，并且该进程是后台进程。
     * TRIM_MEMORY_UI_HIDDEN：内存不足，并且该进程的UI已经不可见了
     * TRIM_MEMORY_COMPLETE这个监听的时候有时候监听不到，建议监听TRIM_MEMORY_MODERATE，在这个里面处理退出程序操作。
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_MODERATE) {
            exit();
            android.os.Process.killProcess(android.os.Process.myPid());    //获取PID
            System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
        }
    }
}
