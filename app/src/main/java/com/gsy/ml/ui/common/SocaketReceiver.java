package com.gsy.ml.ui.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gsy.ml.model.main.JingWeiModel;
import com.gsy.ml.ui.utils.SocaketUtils;

/**
 * Created by Administrator on 2017/6/27.
 * 接收到广播  开始启动socaket长连接
 */

public class SocaketReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if ("SocaketReceiver".equals(intent.getAction())) {
            SocaketUtils.getInstance1().startConnection();
            SocaketUtils.getInstance1().setISocaketListener(new SocaketUtils.ISocaketListener() {
                @Override
                public void onSuccess() {//socaket连接成功 开启连续定位
                    Intent intent = new Intent("ReLocationReceiver");
                    intent.putExtra("type", "start");
                    context.sendBroadcast(intent);
                }

                @Override
                public void onLocation(JingWeiModel jingWeiModel) {//读取到位置信息

                }
            });
        }
    }


}
