package com.gsy.ml.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.main.OrderInfoModel;
import com.gsy.ml.ui.main.LoginActivity;
import com.gsy.ml.ui.main.MainActivity;
import com.gsy.ml.ui.main.MessageActivity;
import com.gsy.ml.ui.main.WelcomeActivity;
import com.gsy.ml.ui.person.OrderBillingActivity;
import com.gsy.ml.ui.person.OrderReceivingActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.SpeedHelper;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.OrdersDialog;
import com.hyphenate.chat.EMClient;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.data.JPushLocalNotification;
import ml.gsy.com.library.utils.ParseJsonUtils;

import static com.gsy.ml.common.MaiLiApplication.mList;

/**
 * Created by Administrator on 2017/4/24.
 */

public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));

            String result = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            OrderInfoModel info = ParseJsonUtils.getBean((String) result, OrderInfoModel.class);

            if (info == null) {
                return;
            }
            //唤醒屏幕
            DemoUtils.wakeUpAndUnlock(context);
                /*
                1001: 发单推送
                1002：接单推送，告知发单人
                1003：确认单推送，告知接单人的申请通过
                1004：确认单推送，告知接单人的申请已经被退回
                1005：完成单，告知接单人，发单人已经确认完成该订单
                1006：发单人取消订单，告知接单人
                1007：接单人取消订单，告知发单人
                case "1000":// 审核信息
                case "1008":// 邮件信息
                 */
            switch (info.getStatus()) {
                case "1001"://发单推送
                    if (mList.size() > 0 && isMainActivity()) {
                        long cur = System.currentTimeMillis();
                        if (info.getData() != null) {
                            if ((cur - info.getPushTime() < 1000 * 60 * 5)) {//大于等于15分钟后    抢单不显示
                                StartSpeed(info.getInfo());
                                OrdersDialog dialog = new OrdersDialog(mList.get(mList.size() - 1), info);
                                MaiLiApplication.getInstance().mOrdersDialog = dialog;
                                dialog.show();
                            }
                            Intent intent1 = new Intent(context, mList.get(mList.size() - 1).getClass());
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            context.startActivity(intent1);
                        }
                    } else {
                        long cur = System.currentTimeMillis();
                        if (info.getData() != null) {

                            if ((cur - info.getPushTime() < 1000 * 60 * 5)) {//大于等于5分钟后    抢单不显示
                                if (mList == null || mList.size() == 0) {
                                    StartSpeed(info.getInfo());
                                    MaiLiApplication.mOrderInfoModel = info;
                                    Intent intent2 = new Intent(context, WelcomeActivity.class);
                                    intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent2);
                                }
                            }
                        }
                    }
                    break;
                case "1002"://接单推送，告知发单人
                    if (mList.size() > 0 && isMainActivity()) {
                        InformationDialog informationDialog = new InformationDialog(mList.get(mList.size() - 1));
                        informationDialog.setTitle("您的订单信息");
                        informationDialog.setMessage("您发布的订单有最新情况！");
                        informationDialog.setPositiveButton("去看看", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                mList.get(mList.size() - 1)
                                        .startActivity(new Intent(mList.get(mList.size() - 1), OrderBillingActivity.class).putExtra("type", 1));
                                dlg.dismiss();
                            }
                        });
                        informationDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                dlg.dismiss();
                            }
                        });
                        MaiLiApplication.getInstance().mInformationDialog = informationDialog;
                        informationDialog.show();
                        EventBus.getDefault().post(new UpdateNotice());

                    } else {
                        JPushLocalNotification ln1 = new JPushLocalNotification();
                        ln1.setBuilderId(0);
                        ln1.setContent(info.getInfo());
                        ln1.setTitle("您的订单信息");
                        ln1.setNotificationId(System.currentTimeMillis());
                        ln1.setBroadcastTime(System.currentTimeMillis());
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("status", "1002");
                        JSONObject json = new JSONObject(map);
                        ln1.setExtras(json.toString());
                        JPushInterface.addLocalNotification(context.getApplicationContext(), ln1);
                    }

                    break;
                case "1003"://确认单推送，告知接单人的申请通过

                    if (mList.size() > 0 && isMainActivity()) {
                        InformationDialog informationDialog = new InformationDialog(mList.get(mList.size() - 1));
                        informationDialog.setTitle("您的订单信息");
                        informationDialog.setMessage(info.getInfo());
                        informationDialog.setPositiveButton("去看看", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                mList.get(mList.size() - 1)
                                        .startActivity(new Intent(mList.get(mList.size() - 1), OrderReceivingActivity.class).putExtra("type", 1));
                                dlg.dismiss();
                            }
                        });
                        informationDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                dlg.dismiss();
                            }
                        });
                        MaiLiApplication.getInstance().mInformationDialog = informationDialog;
                        informationDialog.show();

                        EventBus.getDefault().post(new UpdateNotice());
                    } else {

                        JPushLocalNotification ln3 = new JPushLocalNotification();
                        ln3.setBuilderId(0);
                        ln3.setContent(info.getInfo());
                        ln3.setTitle("您的订单信息");
                        ln3.setNotificationId(System.currentTimeMillis());
                        ln3.setBroadcastTime(System.currentTimeMillis());
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("status", "1003");
                        JSONObject json = new JSONObject(map);
                        ln3.setExtras(json.toString());
                        JPushInterface.addLocalNotification(context.getApplicationContext(), ln3);

                    }
                    EventBus.getDefault().post(new UpdateNotice());
                    break;
                case "1004"://确认单推送，告知接单人的申请已经被退回
                    JPushLocalNotification ln4 = new JPushLocalNotification();
                    ln4.setBuilderId(0);
                    ln4.setContent(info.getInfo());
                    ln4.setTitle("您的订单信息");
                    ln4.setNotificationId(System.currentTimeMillis());
                    ln4.setBroadcastTime(System.currentTimeMillis());
                    JPushInterface.addLocalNotification(context.getApplicationContext(), ln4);

                    break;
                case "1005"://完成单，告知接单人，发单人已经确认完成该订单
                    JPushLocalNotification ln5 = new JPushLocalNotification();
                    ln5.setBuilderId(0);
                    ln5.setContent(info.getInfo());
                    ln5.setTitle("您的订单信息");
                    ln5.setNotificationId(System.currentTimeMillis());
                    ln5.setBroadcastTime(System.currentTimeMillis());
                    Map<String, Object> map5 = new HashMap<String, Object>();
                    map5.put("status", "1005");
                    JSONObject json5 = new JSONObject(map5);
                    ln5.setExtras(json5.toString());
                    JPushInterface.addLocalNotification(context.getApplicationContext(), ln5);
                    EventBus.getDefault().post(new UpdateNotice());
                    break;
                case "1006"://发单人取消订单，告知接单人
                    if (mList.size() > 0 && isMainActivity()) {
                        InformationDialog informationDialog = new InformationDialog(mList.get(mList.size() - 1));
                        informationDialog.setTitle("您的订单信息");
                        informationDialog.setMessage("对方已取消订单！");
                        informationDialog.setPositiveButton("去看看", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {

                                mList.get(mList.size() - 1)
                                        .startActivity(new Intent(mList.get(mList.size() - 1), OrderReceivingActivity.class).putExtra("type", 3));
                                dlg.dismiss();
                            }
                        });
                        informationDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                dlg.dismiss();
                            }
                        });
                        MaiLiApplication.getInstance().mInformationDialog = informationDialog;
                        informationDialog.show();

                        EventBus.getDefault().post(new UpdateNotice());
                    } else {

                        JPushLocalNotification ln6 = new JPushLocalNotification();
                        ln6.setBuilderId(0);
                        ln6.setContent(info.getInfo());
                        ln6.setTitle("您的订单信息");
                        ln6.setNotificationId(System.currentTimeMillis());
                        ln6.setBroadcastTime(System.currentTimeMillis());
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("status", "1006");
                        JSONObject json = new JSONObject(map);
                        ln6.setExtras(json.toString());
                        JPushInterface.addLocalNotification(context.getApplicationContext(), ln6);

                    }

                    EventBus.getDefault().post(new UpdateNotice());
                    break;
                case "1007"://接单人取消订单，告知发单人
                    if (mList.size() > 0 && isMainActivity()) {
                        InformationDialog informationDialog = new InformationDialog(mList.get(mList.size() - 1));
                        informationDialog.setTitle("您的订单信息");
                        informationDialog.setMessage("对方已取消订单！");
                        informationDialog.setPositiveButton("去看看", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {

                                mList.get(mList.size() - 1)
                                        .startActivity(new Intent(mList.get(mList.size() - 1), OrderBillingActivity.class).putExtra("type", 0));
                                dlg.dismiss();
                            }
                        });
                        informationDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                dlg.dismiss();
                            }
                        });
                        MaiLiApplication.getInstance().mInformationDialog = informationDialog;
                        informationDialog.show();

                        EventBus.getDefault().post(new UpdateNotice());
                    } else {
                        JPushLocalNotification ln7 = new JPushLocalNotification();
                        ln7.setBuilderId(0);
                        ln7.setContent(info.getInfo());
                        ln7.setTitle("您的订单信息");
                        ln7.setNotificationId(System.currentTimeMillis());
                        ln7.setBroadcastTime(System.currentTimeMillis());
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("status", "1007");
                        JSONObject json = new JSONObject(map);
                        ln7.setExtras(json.toString());
                        JPushInterface.addLocalNotification(context.getApplicationContext(), ln7);
                    }
                    EventBus.getDefault().post(new UpdateNotice());
                    break;
                case "1000":// 审核信息
                    JPushLocalNotification ln0 = new JPushLocalNotification();
                    ln0.setBuilderId(0);
                    ln0.setContent(info.getInfo());
                    ln0.setTitle("您的审核信息");
                    ln0.setNotificationId(System.currentTimeMillis());
                    ln0.setBroadcastTime(System.currentTimeMillis());
                    JPushInterface.addLocalNotification(context.getApplicationContext(), ln0);
                    EventBus.getDefault().post(new UpdateNotice());
                    break;
                case "1008":// 邮件信息
                    JPushLocalNotification ln8 = new JPushLocalNotification();
                    ln8.setBuilderId(0);
                    ln8.setContent(info.getInfo());
                    ln8.setTitle("蚂蚁快服通知");
                    ln8.setNotificationId(System.currentTimeMillis());
                    ln8.setBroadcastTime(System.currentTimeMillis());
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("status", "1008");
                    JSONObject json = new JSONObject(map);
                    ln8.setExtras(json.toString());
                    JPushInterface.addLocalNotification(context.getApplicationContext(), ln8);
                    EventBus.getDefault().post(new UpdateNotice());
                    break;
                case "1009":// 另一台设备登陆
                    if (mList.size() > 0 && isMainActivity()) {
                        InformationDialog informationDialog = new InformationDialog(mList.get(mList.size() - 1));
                        informationDialog.setCancelable(false);
                        informationDialog.setTitle("账号异常");
                        informationDialog.setMessage("你的账号已在另一台设备登录！");
                        informationDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                EMClient.getInstance().logout(true);//退出登录
                                Intent intent = new Intent(context, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                MaiLiApplication.backToLogin(context, intent);
                                dlg.dismiss();
                            }
                        });
                        MaiLiApplication.getInstance().mInformationDialog = informationDialog;
                        informationDialog.show();
                    } else {
                        JPushLocalNotification ln9 = new JPushLocalNotification();
                        ln9.setBuilderId(0);
                        ln9.setContent(info.getInfo());
                        ln9.setTitle("账号异常");
                        ln9.setNotificationId(System.currentTimeMillis());
                        ln9.setBroadcastTime(System.currentTimeMillis());
                        JPushInterface.addLocalNotification(context.getApplicationContext(), ln9);
                    }


                    break;
            }


        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

            JSONObject json = null;
            String status = "";
            if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                return;
            }
            try {
                json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                status = json == null ? "" : json.optString("status");
            } catch (JSONException e) {
                e.printStackTrace();
                status = "";
            }
            switch (status) {
                case "1002"://接单推送，告知发单人
                    Intent in = new Intent(context, OrderBillingActivity.class);
                    in.putExtra("type", 1);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in);
                    break;
                case "1003"://确认单推送，告知接单人的申请通过
                    Intent in3 = new Intent(context, OrderReceivingActivity.class);
                    in3.putExtra("type", 1);
                    in3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in3);
                    break;
                case "1005"://完成单，告知接单人，发单人已经确认完成该订单
                    Intent in5 = new Intent(context, OrderReceivingActivity.class);
                    in5.putExtra("type", 2);
                    in5.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in5);
                    break;
                case "1006"://发单人取消订单，告知接单人
                    Intent in6 = new Intent(context, OrderReceivingActivity.class);
                    in6.putExtra("type", 3);
                    in6.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in6);
                    break;
                case "1007"://接单人取消订单，告知发单人
                    Intent in7 = new Intent(context, OrderBillingActivity.class);
                    in7.putExtra("type", 0);
                    in7.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in7);
                    break;
                case "1008":// 邮件信息
                    Intent in8 = new Intent(context, MessageActivity.class);
                    in8.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(in8);
                    break;
            }

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
        } else {
            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    private boolean isMainActivity() {
        for (Activity aty : MaiLiApplication.mList) {
            if (aty instanceof MainActivity) {
                return true;
            }
        }
        return false;
    }

    private void StartSpeed(String content) {
        SpeedHelper.getInstance().startSpeed(content, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }

    };

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
