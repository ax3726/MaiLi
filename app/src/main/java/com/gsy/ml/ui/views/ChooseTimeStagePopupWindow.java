package com.gsy.ml.ui.views;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowChooseTimeStageBinding;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ChooseTimeStagePopupWindow extends PopupWindow {
    private Context mContext;
    private PopuwindowChooseTimeStageBinding mBinding;

    private List<String> mData2 = new ArrayList<>();
    private List<String> mData3 = new ArrayList<>();



    private int mType = 1;//1 时间段  2 全天
    private IOccupationListener mIOccupationListener;

    public ChooseTimeStagePopupWindow(Context context) {
        this.mContext = context;

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_choose_time_stage, null, false);

        int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mBinding.getRoot());

        int w = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        int h = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);

        mBinding.getRoot().measure(w, h);

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(mBinding.getRoot().getMeasuredHeight());

        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //点击空白处时，隐藏掉pop窗口

        this.setBackgroundDrawable(new BitmapDrawable());

        //添加pop窗口关闭事件
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismiss();
                backgroundAlpha(1f);
            }
        });
        setAnimationStyle(R.style.lib_popwindow_anim_style);

        mBinding.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBinding.tvTitle.isSelected()) {
                    mBinding.tvTitle.setSelected(false);
                    mType = 1;
                } else {
                    mBinding.tvTitle.setSelected(true);
                    mType = 2;
                }

            }
        });
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int seletedIndex2 = mBinding.wvChoose2.getSeletedIndex();
                seletedIndex2 = seletedIndex2 == -1 ? 0 : seletedIndex2;
                String seletedItem2 = mBinding.wvChoose2.getSeletedItem();
                seletedItem2 = TextUtils.isEmpty(seletedItem2) ? mData2.size() > 0 ? mData2.get(0) : "" : seletedItem2;

                int seletedIndex3 = mBinding.wvChoose3.getSeletedIndex();
                seletedIndex3 = seletedIndex3 == -1 ? 0 : seletedIndex3;
                String seletedItem3 = mBinding.wvChoose3.getSeletedItem();
                seletedItem3 = TextUtils.isEmpty(seletedItem3) ? mData3.size() > 0 ? mData3.get(0) : "" : seletedItem3;


                if (mType==1&&seletedIndex2 >= seletedIndex3) {
                    Toast.makeText(mContext, "开始时间段不能小于等于结束时间段!", Toast.LENGTH_SHORT).show();
                    return;
                }
                int time_stage =seletedIndex3 -seletedIndex2;
                if (mIOccupationListener != null) {


                    mIOccupationListener.selectItem(

                            seletedIndex2,
                            seletedItem2,
                            seletedIndex3,
                            seletedItem3,
                            mType,
                            time_stage
                    );
                }

                dismiss();
            }
        });
        setType(width, 3);
        this.update();


    }

    private List<String> getTime() {
        final List<String> data = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                data.add("0" + i + "\t:\t00");
            } else {
                data.add(i + "\t:\t00");
            }
        }
        return data;
    }


    public void setTime(boolean is_time) {
        if (!is_time) {
            return;
        }
        setData2(getTime());
        setData3(getTime());
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

    private void setType(int width, int num) {
        switch (num) {

            case 3:

                mBinding.wvChoose2.setLayoutParams(new LinearLayout.LayoutParams( (width- (int)Utils.dip2px(mContext,40)) / 2, ViewGroup.LayoutParams.MATCH_PARENT));
                mBinding.wvChoose3.setLayoutParams(new LinearLayout.LayoutParams( (width- (int)Utils.dip2px(mContext,40)) / 2,ViewGroup.LayoutParams.MATCH_PARENT));
                break;
        }
    }

    public void setIOccupationListener(IOccupationListener mIOccupationListener) {
        this.mIOccupationListener = mIOccupationListener;
    }

    public void setTitle(String title) {
        mBinding.tvTitle.setText(title);
    }



    public void setData2(List<String> data) {
        mData2.clear();
        mData2.addAll(data);
        mBinding.wvChoose2.setOffset(2);
        mBinding.wvChoose2.setItems(mData2);

    }

    public void setData3(List<String> data) {
        mData3.clear();
        mData3.addAll(data);
        mBinding.wvChoose3.setOffset(2);
        mBinding.wvChoose3.setItems(mData3);
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
            this.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            // this.dismiss();
        }
    }


    public interface IOccupationListener {
        void selectItem( int position2, String item2, int position3, String item3, int type, int time_stage);
    }
}
