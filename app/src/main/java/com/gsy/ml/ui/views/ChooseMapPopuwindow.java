package com.gsy.ml.ui.views;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowChooseMapBinding;
import com.gsy.ml.model.common.AddressModel;

import java.io.File;

/**
 * Created by Administrator on 2017/5/16.
 */

public class ChooseMapPopuwindow implements View.OnClickListener {
    private Context mContext;
    private AddressModel mAddressModel;

    public ChooseMapPopuwindow(Context context, AddressModel addressModel) {
        mContext = context;
        mAddressModel = addressModel;
    }

    private PopuwindowChooseMapBinding mBinding;
    /**
     * PopupWindow对象
     */
    private PopupWindow mPopupWindow;

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popuwindow_choose_map, null, false);

        mBinding.llyBaidu.setOnClickListener(this);
        mBinding.llyGaode.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
        mPopupWindow = new PopupWindow(mBinding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setAnimationStyle(R.style.lib_popwindow_anim_style);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setWindowAlpa(false);
            }
        });
        show(mBinding.getRoot());
    }

    /**
     * 动态设置Activity背景透明度
     *
     * @param isopen
     */
    public void setWindowAlpa(boolean isopen) {
        if (Build.VERSION.SDK_INT < 11) {
            return;
        }
        final Window window = ((Activity) mContext).getWindow();
        final WindowManager.LayoutParams lp = window.getAttributes();
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ValueAnimator animator;
        if (isopen) {
            animator = ValueAnimator.ofFloat(1.0f, 0.5f);
        } else {
            animator = ValueAnimator.ofFloat(0.5f, 1.0f);
        }
        animator.setDuration(400);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float) Float.parseFloat(animation
                        .getAnimatedValue().toString());
                lp.alpha = alpha;
                window.setAttributes(lp);
            }
        });
        animator.start();
    }

    /**
     * 显示PopupWindow
     */
    private void show(View v) {
        if (mPopupWindow != null && !mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        }
        setWindowAlpa(true);
    }

    /**
     * 消失PopupWindow
     */
    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_gaode://唤起高德地图
                initGaode();
                break;
            case R.id.lly_baidu://唤起百度地图
                initBaidu();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
        }
    }


    private void initBaidu() {
        if (isBaiduMapInstalled()) {
            Intent i1 = new Intent();
            i1.setData(Uri.parse("baidumap://map/geocoder?address=" + mAddressModel.getAddress()));
            // 反向地址解析
            //i1.setData(Uri.parse("baidumap://map/geocoder?location=39.98871,116.43234"));
            mContext.startActivity(i1);
        } else {
            Toast.makeText(mContext, "你没有安装百度地图!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initGaode() {

        if (isGdMapInstalled()) {
            Intent i1 = new Intent();
            i1.setData(Uri.parse("androidamap://viewMap?sourceApplication=蚂蚁快服&poiname=" + mAddressModel.getAddress() +
                    "&lat=" + mAddressModel.getPoint().getLatitude() + "&lon=" + mAddressModel.getPoint().getLongitude() + "&dev=0"));
            mContext.startActivity(i1);
        } else {
            Toast.makeText(mContext, "你没有安装高德地图!", Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 地图应用是否安装
     *
     * @return
     */
    public boolean isGdMapInstalled() {
        return isInstallPackage("com.autonavi.minimap");
    }

    public boolean isBaiduMapInstalled() {
        return isInstallPackage("com.baidu.BaiduMap");
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallPackage(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
