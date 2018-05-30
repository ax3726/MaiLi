package com.gsy.ml.ui.home;

import android.widget.Toast;

import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.WalletPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;
import com.gsy.ml.ui.views.DownOrderDialog;
import com.gsy.ml.ui.views.PayPwdPopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/5/6.
 * 订单页面基类
 */

public class BaseOrderActivity extends BaseActivity {
    public long mReservationTime = 0;//预约时间
    public WalletPresenter mWalletPresenter;
    public double mPrice = 0;//价格
    public double mAddPrice = 0;//加价
    public double mKaPrice = 0;//卡卷
    public double mDenomination = 0; //面额
    public double mTotalPrice = 0;// 总价格
    public int mMoneyType = 0;//type  1  钱够  0 钱不够
    public ChooseDatePopupWindow mChooseTime; //预约时间弹框
    public ChooseDatePopupWindow mHuiheTime; //回合时间
    public String mCashCouponId = "";//卡卷ID
    public PayPwdPopupWindow mPayPwdPopupWindow;//验证密码

    public long getTime(String item1, String item2, String item3) {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期

        if (item1.equals("今天")) {
            long stringToDate = Utils.getStringToDate(mYear + "年" + mMonth + "月" + mDay + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
            long curtime = System.currentTimeMillis();
            if (curtime > stringToDate) {
                Toast.makeText(MaiLiApplication.getInstance(), "预约时间不能小于当前时间!", Toast.LENGTH_SHORT).show();
                return 0;
            } else {
                return stringToDate;
            }
        } else if (item1.equals("明天")) {
            c.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        }
        return 0;
    }

    public void initChooseTimeData() {
        mChooseTime = new ChooseDatePopupWindow(aty, 3);
        mChooseTime.setTitle("请选择预约时间");
        mChooseTime.setTime(true);
        List<String> data1 = new ArrayList<>();
        data1.add("立即发送");
        data1.add("今天");
        data1.add("明天");
        mChooseTime.setData1(data1);
    }

    public ILoadPVListener mILoadPVListener = new ILoadPVListener() {
        @Override
        public void onLoadComplete(Object object, int... parms) {
            hideWaitDialog();
            if (object instanceof HttpErrorModel) {
                HttpErrorModel errorModel = (HttpErrorModel) object;
                Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
            } else if (object instanceof WalletModel) {
                WalletModel models = (WalletModel) object;
                setWallet(models);
            }
        }

        @Override
        public void onLoadComplete(Object object, Class itemClass) {

        }
    };

    public void checkOrder() {
        showWaitDialog();
        mWalletPresenter.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), 1 + "", 20 + "");
    }

    public DownOrderDialog mDownOrderDialog = null;

    private void setWallet(WalletModel model) {
        if (model == null || model.getData() == null) {
            return;
        }
        double totalMoney = DemoUtils.formatDoubleDOWN(model.getData().getTotalMoney());//总余额
        double binging = model.getData().getDeposit();//保证金
        mDownOrderDialog.initMoney(totalMoney, binging, mPrice + mAddPrice - mKaPrice);
        mDownOrderDialog.show();
    }


    private String getDay(int num) {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份
        int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        if (num == 0) {
            return mMonth + "月" + mDay + "日";
        } else {
            c.add(Calendar.DATE, num);//把日期往后增加一天.整数往后推,负数往前移动
            mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
            return mMonth + "月" + mDay + "日";
        }
    }

    public void initChooseTimeData1() {
        mHuiheTime = new ChooseDatePopupWindow(aty, 3);
        mHuiheTime.setTitle("请选择预约工作时间");
        mHuiheTime.setTime(true);
        List<String> data1 = new ArrayList<>();
        data1.add("今天");
        data1.add("明天");
        data1.add(getDay(2));
        data1.add(getDay(3));
        data1.add(getDay(4));
        data1.add(getDay(5));
        data1.add(getDay(6));
        mHuiheTime.setData1(data1);
    }

    public long getTime1(String item1, String item2, String item3) {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份

        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        if (item1.equals("今天")) {
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            long stringToDate = Utils.getStringToDate(mYear + "年" + mMonth + "月" + mDay + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
            long curtime = System.currentTimeMillis();
            if (curtime > stringToDate) {
                Toast.makeText(MaiLiApplication.getInstance(), "预约时间不能小于当前时间!", Toast.LENGTH_SHORT).show();
                return 0;
            } else {
                return stringToDate;
            }
        } else if (item1.equals("明天")) {
            c.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        } else if (item1.equals(getDay(2))) {
            c.add(Calendar.DATE, 2);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        } else if (item1.equals(getDay(3))) {
            c.add(Calendar.DATE, 3);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        } else if (item1.equals(getDay(4))) {
            c.add(Calendar.DATE, 4);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        } else if (item1.equals(getDay(5))) {
            c.add(Calendar.DATE, 5);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        } else if (item1.equals(getDay(6))) {
            c.add(Calendar.DATE, 6);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" + item2 + ":" + item3, "yyyy年MM月dd日HH:mm");
        }
        return 0;
    }

    public long getTime2(String item1) {
        Calendar c = Calendar.getInstance();//首先要获取日历对象
        int mYear = c.get(Calendar.YEAR); // 获取当前年份

        int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        if (item1.equals("今天")) {
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            long stringToDate = Utils.getStringToDate(mYear + "年" + mMonth + "月" + mDay + "日", "yyyy年MM月dd日");
            return stringToDate;
        } else if (item1.equals("明天")) {
            c.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日", "yyyy年MM月dd日");
        } else if (item1.equals(getDay(2))) {
            c.add(Calendar.DATE, 2);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日", "yyyy年MM月dd日");
        } else if (item1.equals(getDay(3))) {
            c.add(Calendar.DATE, 3);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日", "yyyy年MM月dd日");
        } else if (item1.equals(getDay(4))) {
            c.add(Calendar.DATE, 4);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日", "yyyy年MM月dd日");
        } else if (item1.equals(getDay(5))) {
            c.add(Calendar.DATE, 5);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日", "yyyy年MM月dd日");
        } else if (item1.equals(getDay(6))) {
            c.add(Calendar.DATE, 6);//把日期往后增加一天.整数往后推,负数往前移动
            int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
            return Utils.getStringToDate(mYear + "年" + mMonth + "月" + c.get(Calendar.DAY_OF_MONTH) + "日", "yyyy年MM月dd日");
        }
        return 0;
    }


    @Override
    public void initData() {
        EventBus.getDefault().register(aty);
        mDownOrderDialog = new DownOrderDialog(aty);
        mWalletPresenter = new WalletPresenter(mILoadPVListener);
        super.initData();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
