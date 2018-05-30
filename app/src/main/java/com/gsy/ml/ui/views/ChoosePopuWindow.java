package com.gsy.ml.ui.views;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopupwindowChooseBinding;
import com.gsy.ml.ui.utils.DemoUtils;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;


/**
 * Created by Administrator on 2017/4/29.
 */

public class ChoosePopuWindow extends PopupWindow {
    private final Context mContext;

    private PopupwindowChooseBinding mBinding;

    private List<String> mData = new ArrayList<>();
    private CommonAdapter<String> mAdapter;

    public ChoosePopuWindow(Context context) {
        this.mContext = context;
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popupwindow_choose, null, false);
        int height = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        int width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(mBinding.getRoot());

        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        this.setHeight(height / 2);


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
        initView();
    }

    /**
     * 重新计算高度
     */
    private void updateHeight() {
        int height = ((Activity) mContext).getWindowManager().getDefaultDisplay().getHeight();
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        View view = View.inflate(mContext, R.layout.item_choose_pop, null);
        view.measure(h, w);
        int hh = view.getMeasuredHeight();
        hh = mData.size() * hh;
        if (hh >= height / 2) {
            //设置SelectPicPopupWindow弹出窗体的
            this.setHeight(height / 2);
        } else {
            //设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(hh);
        }

    }
    private String mCurtxt="";//当前选中的项

    public void setCurtxt(String mCurtxt) {
        this.mCurtxt = mCurtxt;
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAdapter = new CommonAdapter<String>(mContext, R.layout.item_choose_pop, mData) {
            @Override
            protected void convert(ViewHolder holder, final String s, final int position) {
                final TextView tv_name = holder.getView(R.id.tv_name);
                if (mCurtxt.equals(type == 1 ? DemoUtils.TypeToOccupation(Integer.valueOf(s)) : s)) {
                    tv_name.setSelected(true);
                } else {
                    tv_name.setSelected(false);
                }
                tv_name.setText(type == 1 ? DemoUtils.TypeToOccupation(Integer.valueOf(s)) : s);
                tv_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mIChooseListener != null) {
                            mIChooseListener.selectItem(position,  s,type);
                        }
                        dismiss();
                    }
                });
            }
        };
        mBinding.rcBody.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rcBody.setAdapter(mAdapter);
    }



    private int type = 0;

    public void setData(List<String> data,int t) {
        type = t;
        this.mData.clear();
        this.mData.addAll(data);

        mAdapter.notifyDataSetChanged();
        updateHeight();
        update();
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, 0, 1);
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

    private IChooseListener mIChooseListener;


    public void setIChooseListener(IChooseListener mIChooseListener) {
        this.mIChooseListener = mIChooseListener;
    }

    public interface IChooseListener {
        void selectItem(int position, String item,int type);
    }
}
