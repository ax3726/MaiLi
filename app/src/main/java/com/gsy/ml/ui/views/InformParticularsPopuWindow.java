package com.gsy.ml.ui.views;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowInformParticularsBinding;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimePresenter;

/**
 * Created by Administrator on 2017/9/2.
 */

public class InformParticularsPopuWindow extends PopupWindow implements View.OnClickListener {
    private final Context context;
    private PopuwindowInformParticularsBinding mBinding;
    private InformContentPopuwindow mInformContentPopuwindow;

    public InformParticularsPopuWindow(final Context context) {
        this.context = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_inform_particulars, null, false);
        mBinding.tvInform.setOnClickListener(this);

//        // 设置SelectPicPopupWindow的View
        this.setContentView(mBinding.getRoot());

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);


        //点击空白处时，隐藏掉pop窗口
        this.setBackgroundDrawable(new BitmapDrawable());
        backgroundAlpha(1f);

        //添加pop窗口关闭事件
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                backgroundAlpha(1f);
            }
        });


    }

    public void setListener(InformContentPopuwindow.InfromContentListener listener) {
        mInformContentPopuwindow = new InformContentPopuwindow(context);
        mInformContentPopuwindow.setInfromContentListener(listener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_inform:
                mInformContentPopuwindow.showPopu();
                dismiss();
                break;
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
            backgroundAlpha(0.7f);
            this.showAsDropDown(parent, 0, 0);
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) context).getWindow().setAttributes(lp);
    }


    //接口回调
//    private InformParticularsListener mInformParticularsListener;
//
//    public void setInformParticularsListener(InformParticularsListener mInformParticularsListener) {
//        this.mInformParticularsListener = mInformParticularsListener;
//    }
//
//    public interface InformParticularsListener {
//        void setItem(int postion, String text);
//    }
}
