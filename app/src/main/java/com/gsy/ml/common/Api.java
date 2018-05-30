package com.gsy.ml.common;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.ui.main.LoginActivity;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import ml.gsy.com.library.utils.NetWorkUtils;
import ml.gsy.com.library.utils.ParamsUtils;
import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络请求
 * Created by Administrator on 2017/4/10.
 */

public class Api {
    private static Context mContext;
    private static Api ourInstance = null;

    public static Api getInstance(Context context) {
        if (null == ourInstance) {
            mContext = context;
            ourInstance = new Api();
        }
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return ourInstance;
    }

    private static OkHttpClient okHttpClient = new OkHttpClient();    //实例话对象
    private String mtyb = android.os.Build.BRAND;//手机品牌
    private String mtype = android.os.Build.MODEL; // 手机型号
    private String version = android.os.Build.VERSION.RELEASE;//手机版本号
    private String appversion = Utils.getVersionName(mContext);//APP版本号

    static {
        okHttpClient.newBuilder().connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    public void getData(String url, Map<String, String> params, CustomHttpHandler callback) {
      /*  String keyStr = getKeyStr(params);
        if (!TextUtils.isEmpty(keyStr)) {
            params.put("sign", keyStr);
        }*/
        int connectedType = NetWorkUtils.getConnectedType(mContext);
        if (connectedType == -1) {
            Message message = new Message();
            message.what = 2;
            message.obj = HttpErrorModel.createNetWorkError();
            callback.handler.sendMessage(message);
            return;
        }
        FormBody formBody = ParamsUtils.putMap1(params);
        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .tag(url)
                .build();
        Call call = okHttpClient.newCall(request);
        callback.setCall(call);
        call.enqueue(callback);


    }

    /* public static String getUrl(String url) {
         if (!TextUtils.isEmpty(url) && url.contains("http")) {
             back_icon url;
         }
         back_icon Link.PHOTOURL + url;
     }*/
    public static abstract class CustomHttpHandler implements Callback {
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        onFailure(HttpErrorModel.createError());
                        break;
                    case 1:
                        Object object = null;
                        String responseString = (String) msg.obj;
                        if (null != responseString) {
                            if (!responseString.contains(HttpErrorModel.HTTP_ERROR_CONTAINS_STR)
                                    && !responseString.contains(HttpErrorModel.HTTP_ERROR_CONTAINS_STR2)
                                    ) {
                                HttpErrorModel errorModel = HttpErrorModel.createError();
                                try {
                                    errorModel = ParseJsonUtils.getBean(responseString, HttpErrorModel.class);
                                    if (errorModel == null) {
                                        errorModel = HttpErrorModel.createError();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    errorModel = HttpErrorModel.createError();
                                }
                                if ("401".equals(errorModel.getStatus())) {//Token失效
                                    Intent intent = new Intent(mContext, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    MaiLiApplication.backToLogin(mContext, intent);
                                }
                                object = errorModel;
                            } else {
                                object = responseString.trim();
                            }
                        } else {
                            object = HttpErrorModel.createError();
                        }
                        onSuccess(object);
                        break;
                    case 2:

                        onSuccess(msg.obj);
                        break;
                }
            }
        };

        public void onCancel() {
            if (this.call != null) {
                this.call.cancel();
            }
        }


        private Call call;

        public void setCall(Call call) {
            this.call = call;
        }

        @Override
        public void onFailure(Call call, IOException e) {
            handler.sendEmptyMessage(0);
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            if (response.code() == 404) {
                handler.sendEmptyMessage(0);
            } else {
                String responseString = response.body().string();
                Message message = new Message();
                message.what = 1;
                message.obj = responseString;
                handler.sendMessage(message);
            }
        }

        public abstract void onFailure(HttpErrorModel errorModel);

        public abstract void onSuccess(Object object);
    }
}
