package com.gsy.ml.ui.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.amap.api.location.AMapLocation;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.main.JingWeiModel;
import com.gsy.ml.ui.utils.LocationSocaketHelper;
import com.gsy.ml.ui.utils.SocaketUtils;

import ml.gsy.com.library.utils.ParseJsonUtils;

/**
 * Created by Administrator on 2017/6/27.
 */

public class ReLocationReceiver extends BroadcastReceiver implements LocationSocaketHelper.ILocationListener {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("ReLocationReceiver".equals(intent.getAction())) {
            String type = intent.getStringExtra("type");
            if ("start".equals(type)) {//开始定位
                LocationSocaketHelper.getInstance().setILocationListener(this);
                if (MaiLiApplication.mList.size() > 0) {
                    LocationSocaketHelper.getInstance().startLocation(MaiLiApplication.mList.get(MaiLiApplication.mList.size() - 1));
                } else {
                    LocationSocaketHelper.getInstance().startLocation();
                }
            } else if ("end".equals(type)) {//关闭
                SocaketUtils.getInstance1().CloseScoket();
                LocationSocaketHelper.getInstance().releaseLocation();//停止定位
            }
        }
    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location == null || location.getErrorCode() != 0) {
            return;
        }
        final String bingStr = ParseJsonUtils.getjsonStr(new JingWeiModel(location.getLongitude() + "",
                location.getLatitude() + "",
                MaiLiApplication.getInstance().getUserInfo().getPhone()));
        new Thread() {
            @Override
            public void run() {
                super.run();
                SocaketUtils.getInstance1().sendMsg(bingStr);
            }
        }.start();

    }
}
