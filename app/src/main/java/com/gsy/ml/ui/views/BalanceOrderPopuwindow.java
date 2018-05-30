package com.gsy.ml.ui.views;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowBanlanceOrderBinding;
import com.gsy.ml.model.person.BanlancePriceModel;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2017/5/16.
 * 订单结算页面
 */

public class BalanceOrderPopuwindow implements View.OnClickListener {
    private Context mContext;

    private String mDoingPeoplePhone = "";//接单人和号码
    private String mDoingOnline = "";//进行中的状态

    private double mPrice = 0;
    private double mCountPrice = 0;
    private List<BanlancePriceModel> mList = new ArrayList<>();
    private CommonAdapter<BanlancePriceModel> mAdapter;
    private boolean isOne = false;

    public BalanceOrderPopuwindow(Context context, String doingPeoplePhone, double price,String doingOnline) {
        mContext = context;
        mDoingPeoplePhone = doingPeoplePhone;
        mPrice = price;
        mDoingOnline = doingOnline;
        String[] name = mDoingPeoplePhone.split(",");
        String[] online = mDoingOnline.split(",");

        for (int i = 0; i < name.length; i++) {
            mList.add(new BanlancePriceModel(name[i].substring(11, name[i].length()), false, name[i].substring(0, 11),"true".equals(online[i])));
        }
        isOne = name.length == 1;

    }

    private PopuwindowBanlanceOrderBinding mBinding;
    /**
     * PopupWindow对象
     */
    private PopupWindow mPopupWindow;

    /**
     * 弹出Popupwindow
     */
    public void showPopupWindow() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.popuwindow_banlance_order, null, false);

        mBinding.tvChooseAll.setOnClickListener(this);
        mBinding.tvNegative.setOnClickListener(this);
        mBinding.tvPositive.setOnClickListener(this);
        mPopupWindow = new PopupWindow(mBinding.getRoot(),
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
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
        if (!isOne) {
            initAdapter();
            updateHeight();
        } else {
            mBinding.tvChooseAll.setVisibility(View.INVISIBLE);
            mBinding.rcContent.setVisibility(View.GONE);
            mList.get(0).setIs_choose(true);
            updatePrice();
        }
    }

    private void updateHeight() {
        WindowManager m = ((Activity) mContext).getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        View item = View.inflate(mContext, R.layout.item_phone_list, null);

        int w = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        int h = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                ViewGroup.MeasureSpec.UNSPECIFIED);
        item.measure(w, h);

        int height = item.getMeasuredHeight();
        if (mList.size() >= 6) {
            mBinding.rcContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * 6));
        } else {
            mBinding.rcContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height * mList.size()));
        }
    }

    private void updatePrice() {
        int cout = 0;
        for (BanlancePriceModel b : mList) {
            if (b.getIs_choose()&&b.isOnline()) {
                cout++;
            }
        }
        mCountPrice = cout * mPrice;
        mBinding.tvPrice.setText(mCountPrice + "");
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<BanlancePriceModel>(mContext, R.layout.item_phone_list, mList) {
            @Override
            protected void convert(ViewHolder holder, final BanlancePriceModel model, final int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                final ImageView cb_name = holder.getView(R.id.cb_name);
                tv_name.setText(model.getName());
                cb_name.setSelected(model.getIs_choose());
                cb_name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (cb_name.isSelected()) {
                            mList.get(position).setIs_choose(false);
                            cb_name.setSelected(false);
                        } else {
                            mList.get(position).setIs_choose(true);
                            cb_name.setSelected(true);
                        }
                        mAdapter.notifyDataSetChanged();
                        updatePrice();
                    }
                });

            }
        };
        mBinding.rcContent.setLayoutManager(new LinearLayoutManager(mContext));
        mBinding.rcContent.setAdapter(mAdapter);

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
        window.setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
            case R.id.tv_negative://取消
                dismiss();
                break;
            case R.id.tv_positive:
                if (mIBanlanceOrderListener != null) {
                    String phone_list = "";
                    for (BanlancePriceModel b : mList) {
                        if (b.getIs_choose()) {
                            phone_list = phone_list + b.getPhone() + ",";
                        }
                    }

                    if (TextUtils.isEmpty(phone_list)) {
                        Toast.makeText(mContext, "请选择结算人！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mIBanlanceOrderListener.onPositive(phone_list);
                    dismiss();
                }
                break;

            case R.id.tv_choose_all:
                if (!isOne) {
                    if (mBinding.tvChooseAll.isSelected()) {
                        mBinding.tvChooseAll.setSelected(false);
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setIs_choose(false);
                        }
                    } else {
                        mBinding.tvChooseAll.setSelected(true);
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setIs_choose(true);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    updatePrice();
                }
                break;
        }
    }

    private IBanlanceOrderListener mIBanlanceOrderListener;

    public void setIBanlanceOrderListener(IBanlanceOrderListener mIBanlanceOrderListener) {
        this.mIBanlanceOrderListener = mIBanlanceOrderListener;
    }

    public interface IBanlanceOrderListener

    {
        void onPositive(String phone_list);
    }

}
