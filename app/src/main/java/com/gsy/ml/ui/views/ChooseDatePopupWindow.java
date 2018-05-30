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

import com.gsy.ml.R;
import com.gsy.ml.databinding.PopuwindowChooseDateBinding;
import com.gsy.ml.ui.utils.ProvinceAreaHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Administrator on 2017/4/26.
 */

public class ChooseDatePopupWindow extends PopupWindow {
    private Context mContext;
    private PopuwindowChooseDateBinding mBinding;
    private List<String> mData1 = new ArrayList<>();
    private List<String> mData2 = new ArrayList<>();
    private List<String> mData3 = new ArrayList<>();
    private IOccupationListener mIOccupationListener;
    private int width = 0;

    public ChooseDatePopupWindow(Context context, int num) {
        this.mContext = context;

        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popuwindow_choose_date, null, false);

        int height = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
        width = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
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


        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBinding.tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIOccupationListener != null) {
                    int seletedIndex1 = mBinding.wvChoose1.getSeletedIndex();
                    seletedIndex1 = seletedIndex1 == -1 ? 0 : seletedIndex1;
                    String seletedItem1 = mBinding.wvChoose1.getSeletedItem();
                    seletedItem1 = TextUtils.isEmpty(seletedItem1) ? mData1.size() > 0 ? mData1.get(0) : "" : seletedItem1;

                    int seletedIndex2 = mBinding.wvChoose2.getSeletedIndex();
                    seletedIndex2 = seletedIndex2 == -1 ? 0 : seletedIndex2;
                    String seletedItem2 = mBinding.wvChoose2.getSeletedItem();
                    seletedItem2 = TextUtils.isEmpty(seletedItem2) ? mData2.size() > 0 ? mData2.get(0) : "" : seletedItem2;

                    int seletedIndex3 = mBinding.wvChoose3.getSeletedIndex();
                    seletedIndex3 = seletedIndex3 == -1 ? 0 : seletedIndex3;
                    String seletedItem3 = mBinding.wvChoose3.getSeletedItem();
                    seletedItem3 = TextUtils.isEmpty(seletedItem3) ? mData3.size() > 0 ? mData3.get(0) : "" : seletedItem3;


                    mIOccupationListener.selectItem(
                            seletedIndex1,
                            seletedItem1,
                            seletedIndex2,
                            seletedItem2,
                            seletedIndex3,
                            seletedItem3
                    );
                }
                dismiss();
            }
        });
        setType(width, num);
        this.update();


    }

    /*
     * 选择城市区域
     * @param is_Area
     */
    public void setArea(boolean is_Area) {
        if (!is_Area) {
            return;
        }
        final ProvinceAreaHelper provinceAreaHelper = new ProvinceAreaHelper(mContext);
        provinceAreaHelper.initProvinceData();
        mBinding.wvChoose1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                String[] citys = provinceAreaHelper.updateCities(item);
                List<String> data2 = new ArrayList<>();
                if (citys != null) {
                    data2.add("全省");
                    for (int i = 0; i < citys.length; i++) {
                        data2.add(citys[i]);
                    }
                }
                setData2(data2);
                super.onSelected(selectedIndex, item);
            }
        });
        mBinding.wvChoose2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                String[] areas = provinceAreaHelper.updateAreas(item);
                List<String> data2 = new ArrayList<>();
                if (areas != null) {
                    data2.add("全市");
                    for (int i = 0; i < areas.length; i++) {
                        data2.add(areas[i]);
                    }
                }
                setData3(data2);
                super.onSelected(selectedIndex, item);
            }
        });
    }

    public void setTime(boolean is_time) {
        if (!is_time) {
            return;
        }
        final List<String> data22 = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            if (i < 10) {
                data22.add("0" + i);
            } else {
                data22.add(i + "");
            }
        }

        final List<String> data33 = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            if (i < 10) {
                data33.add("0" + i);
            } else {
                data33.add(i + "");
            }
        }
        mBinding.wvChoose1.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                if ("立即发送".equals(item)) {
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add("00");
                    setData2(strings);
                    setData3(strings);
                } else if ("今天".equals(item)) {
                    Calendar c = Calendar.getInstance();//首先要获取日历对象
                    int hour = c.get(Calendar.HOUR_OF_DAY);// 获取当小时
                    int min = c.get(Calendar.MINUTE) + 1;// 获取当分钟
                    List<String> data2 = new ArrayList<>();

                    for (int i = hour; i < 24; i++) {
                        if (i < 10) {
                            data2.add("0" + i);
                        } else {
                            data2.add(i + "");
                        }
                    }
                    setData2(data2);
                    List<String> data3 = new ArrayList<>();
                    for (int i = min; i < 60; i++) {
                        if (i < 10) {
                            data3.add("0" + i);
                        } else {
                            data3.add(i + "");
                        }
                    }
                    setData3(data3);
                } else {

                    setData2(data22);
                    setData3(data33);
                }
                super.onSelected(selectedIndex, item);
            }
        });

        mBinding.wvChoose2.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Calendar c = Calendar.getInstance();//首先要获取日历对象
                int hour = c.get(Calendar.HOUR_OF_DAY);// 获取当小时
                int min = c.get(Calendar.MINUTE) + 1;// 获取当分钟
                if (item.equals(String.valueOf(hour))) {
                    List<String> data3 = new ArrayList<>();
                    for (int i = min; i < 60; i++) {
                        if (i < 10) {
                            data3.add("0" + i);
                        } else {
                            data3.add(i + "");
                        }
                    }
                    setData3(data3);
                } else {
                    setData3(data33);
                }
                super.onSelected(selectedIndex, item);
            }
        });

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

    public void setWvVisibility1(boolean bl) {
        mBinding.wvChoose1.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    public void setWvVisibility2(boolean bl) {
        mBinding.wvChoose2.setVisibility(bl ? View.VISIBLE : View.GONE);
    }

    public void setChooseType(int num) {
        setType(width, num);
    }

    private void setType(int width, int num) {
        switch (num) {
            case 1:
                mBinding.wvChoose1.setLayoutParams(new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT));
                mBinding.wvChoose2.setVisibility(View.GONE);
                mBinding.wvChoose3.setVisibility(View.GONE);
                break;
            case 2:
                mBinding.wvChoose1.setLayoutParams(new LinearLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.MATCH_PARENT));
                mBinding.wvChoose2.setLayoutParams(new LinearLayout.LayoutParams(width / 2, ViewGroup.LayoutParams.MATCH_PARENT));
                mBinding.wvChoose3.setVisibility(View.GONE);
                break;
            case 3:
                mBinding.wvChoose1.setLayoutParams(new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT));
                mBinding.wvChoose2.setLayoutParams(new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT));
                mBinding.wvChoose3.setLayoutParams(new LinearLayout.LayoutParams(width / 3, ViewGroup.LayoutParams.MATCH_PARENT));
                break;
        }
    }

    public void setIOccupationListener(IOccupationListener mIOccupationListener) {
        this.mIOccupationListener = mIOccupationListener;
    }

    public void setTitle(String title) {
        mBinding.tvTitle.setText(title);
    }

    public void setData1(List<String> data) {
        mData1.clear();
        mData1.addAll(data);
        mBinding.wvChoose1.setOffset(2);
        mBinding.wvChoose1.setItems(mData1);
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
        void selectItem(int position1, String item1, int position2, String item2, int position3, String item3);
    }
}
