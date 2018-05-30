package com.gsy.ml.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.gsy.ml.common.MaiLiApplication;

import ml.gsy.com.library.utils.runtimepermission.PermissionsManager;
import ml.gsy.com.library.utils.runtimepermission.PermissionsResultAction;

/**
 * Created by Administrator on 2017/4/19.
 * 长连接定位帮助类
 */

public class LocationSocaketHelper {


    private static LocationSocaketHelper mLocationHelper = null;

    public static LocationSocaketHelper getInstance() {
        if (mLocationHelper == null) {
            mLocationHelper = new LocationSocaketHelper();
        }
        return mLocationHelper;
    }

    private LocationSocaketHelper() {
        initLocation();
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (mILocationListener != null) {
                mILocationListener.onLocationChanged(aMapLocation);
            }

        }
    };
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private ILocationListener mILocationListener;

    public void setILocationListener(ILocationListener mILocationListener) {
        this.mILocationListener = mILocationListener;
    }

    /**
     * 初始化定位
     */
    private void initLocation() {

        //初始化定位
        mLocationClient = new AMapLocationClient(MaiLiApplication.getInstance());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationCacheEnable(true);
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        // mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

        //设置定位模式为AMapLocationMode.Device_Sensors，仅设备模式。
        //  mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);

        //获取一次定位结果：
        //该方法默认为false。
        //  mLocationOption.setOnceLocation(true);
        mLocationOption.setInterval(8000);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        //mLocationOption.setOnceLocationLatest(true);


        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);

        //设置是否强制刷新WIFI，默认为true，强制刷新。
        //  mLocationOption.setWifiActiveScan(false);

        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);

        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        //mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);


    }

    public void startLocation() {
        if (mLocationClient != null) {
        //启动定位
            mLocationClient.startLocation();
        }
    }

    public int startLocation(final Activity activity) {
        if (mLocationClient == null) {
            return 0;
        }


        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, new PermissionsResultAction() {
            @Override
            public void onGranted() {
                //启动定位
                mLocationClient.startLocation();
            }

            @Override
            public void onDenied(String permission) {
                Toast.makeText(MaiLiApplication.appliactionContext, "还没有开启定位权限呢!", Toast.LENGTH_SHORT).show();

            }
        });
        return 0;
    }

    public void releaseLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationHelper = null;
        }
    }

    public interface ILocationListener {
        void onLocationChanged(AMapLocation location);
    }

}
