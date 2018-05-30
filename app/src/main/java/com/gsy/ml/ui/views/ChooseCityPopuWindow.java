package com.gsy.ml.ui.views;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.PopuwindowChooseCityBinding;
import com.gsy.ml.ui.utils.ProvinceAreaHelper;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2017/4/28.
 */

public class ChooseCityPopuWindow extends PopupWindow {
    private Context mContext;
    private PopuwindowChooseCityBinding mBinding;
    private List<String> mAreaData = new ArrayList<>();
    private ProvinceAreaHelper mProvinceAreaHelper;
    private IChooseCityListener mIChooseCityListener;

    public void setIChooseCityListener(IChooseCityListener mIChooseCityListener) {
        this.mIChooseCityListener = mIChooseCityListener;
    }

    public ChooseCityPopuWindow(Context context) {
        this.mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_choose_city, null, false);

        int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mBinding.getRoot());

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(height / 2);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //点击空白处时，隐藏掉pop窗口

        this.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(0.8f);
        //添加pop窗口关闭事件
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                backgroundAlpha(1f);
            }
        });
        this.update();
        initData();
        setLables();
    }

    private void initData() {
        mProvinceAreaHelper = new ProvinceAreaHelper(mContext);
        mProvinceAreaHelper.initProvinceData();
        mAreaData.clear();
        String[] areas = mProvinceAreaHelper.updateAreas(MaiLiApplication.getInstance().getUserPlace().getCity());
        for (String s : areas) {
            mAreaData.add(s);
        }
    }

    private void setLables() {
        mBinding.flyAreas.removeAllViews();
        mBinding.flyAreas.setGravity(Gravity.CENTER);
        for (int i = 0; i < mAreaData.size(); i++) {
            TextView tv = new TextView(mContext);
            tv.setText(mAreaData.get(i));
            tv.setTextSize(15);
            tv.setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.PX15),
                    mContext.getResources().getDimensionPixelSize(R.dimen.PX6),
                    mContext.getResources().getDimensionPixelSize(R.dimen.PX15),
                    mContext.getResources().getDimensionPixelSize(R.dimen.PX6)
            );
            tv.setTextColor(mContext.getResources().getColor(R.color.color_choose_occupation));
            tv.setBackgroundResource(R.drawable.selector_choose_occupation);
            TextPaint tp = tv.getPaint();
            tp.setFakeBoldText(true);
            tv.setClickable(true);
            tv.setFocusable(true);

            FluidLayout.LayoutParams params = new FluidLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            params.setMargins(mContext.getResources().getDimensionPixelSize(R.dimen.PX10), mContext.getResources().getDimensionPixelSize(R.dimen.PX10), mContext.getResources().getDimensionPixelSize(R.dimen.PX10), mContext.getResources().getDimensionPixelSize(R.dimen.PX10));
            final int finalI = i;
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mIChooseCityListener != null) {
                        mIChooseCityListener.chooseItem(finalI, mAreaData.get(finalI));
                    }
                    dismiss();
                }
            });

            mBinding.flyAreas.addView(tv, params);
        }
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 0);
        } else {
            // this.dismiss();
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public interface IChooseCityListener {
        void chooseItem(int position, String item);
    }
}
