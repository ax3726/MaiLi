package com.gsy.ml.ui.home.WorkType;

import android.app.Dialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityArttutorLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.PriceModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.model.workType.TutorModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.SendOrdersPresenter;
import com.gsy.ml.prestener.home.TotalPricePresenter;
import com.gsy.ml.prestener.main.AliPayPresenter;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.home.BaseOrderActivity;
import com.gsy.ml.ui.home.EditAddressActivity;
import com.gsy.ml.ui.home.SendOrderSuccessActivity;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.PayHelper;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;
import com.gsy.ml.ui.views.ChooseSortPopuWindow;
import com.gsy.ml.ui.views.ChooseTimeStagePopupWindow;
import com.gsy.ml.ui.views.DownOrderDialog;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.PayPwdPopupWindow;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ml.gsy.com.library.utils.ParseJsonUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/28.
 * 艺术家教
 */

public class ArtTutorActivity extends BaseOrderActivity implements View.OnClickListener
        , ILoadPVListener
        , DownOrderDialog.IDownOrderListener
        , PayPwdPopupWindow.IPayPwdListener {
    private AliPayPresenter mAliPayPresenter = new AliPayPresenter(this);//支付
    private ActivityArttutorLayoutBinding mBinding;
    private ChooseDatePopupWindow mClassesPopupWindow;
    private ChooseDatePopupWindow mCoursePopupWindow;
    private ChooseDatePopupWindow mPricePopupWindow;
    private Map<String, ArrayList<String>> mData = new HashMap<>();//保存课程
    private List<String> mClassesData = new ArrayList<>();
    private List<String> mCourseData = new ArrayList<>();
    private SendOrdersPresenter mPresenter = new SendOrdersPresenter(this);
    private TotalPricePresenter mPricePresenter = new TotalPricePresenter(this);
    private AddressModel mStartAddress;
    private int hour = 0;
    private String classes, course, day;
    private long mHuiheTimeLong = 0;
    private ChooseTimeStagePopupWindow mChooseTimeStagePopupWindow;

    @Override
    public int getLayoutId() {
        return R.layout.activity_arttutor_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();

        if (mType == 2) {
            mBinding.ilHead.tvTitle.setText("课程家教");
        } else {
            mBinding.ilHead.tvTitle.setText("艺术家教");
        }
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.tvRight.setText(R.string.receipt_notice);
        mBinding.ilHead.llyRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(-6)));
            }
        });
    }

    @Override
    public void initData() {
        super.initData();
        mType = getIntent().getIntExtra("type", 2);
        mBinding = (ActivityArttutorLayoutBinding) vdb;
        mTimePickerDialog = new TimePickerDialog.Builder()
                .setType(Type.ALL)
                .setTitleStringId("预约工作时间")
                .setThemeColor(getResources().getColor(R.color.colorTheme))
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerDialog, long time) {
                        long curtime = System.currentTimeMillis();
                        if (time >=curtime ) {//

                            long monthtime = curtime + 1000L * 60L * 60L * 24L * 7L;
                            if (time < monthtime) {
                                mBinding.tvHuiheTime.setText(Utils.getDateToString(time, "MM月dd日HH:mm"));
                                mHuiheTimeLong = time;
                            } else {
                                showToast("不能选择大于一个星期的时间!");
                            }
                        } else {
                            showToast("不能小于当前时间!");
                        }
                    }
                })
                .build();

        initChooseTimeData1();
        initListener();
        if (mType == 2) {
            initView();
        } else {
            mBinding.llyClasses.setVisibility(View.GONE);
        }
        mChooseTimeStagePopupWindow = new ChooseTimeStagePopupWindow(aty);
        mChooseTimeStagePopupWindow.setTime(true);

        mChooseTimeStagePopupWindow.setIOccupationListener(new ChooseTimeStagePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position2, String item2, int position3, String item3, int type, int time_stage) {
                String s = mBinding.tvTimeStage.getText().toString();
                if (s.contains("全天")) {
                    hour = 0;
                    s = "";
                }
                if (TextUtils.isEmpty(s)) {
                    if (type == 2) {//全天
                        hour = 8;
                        mBinding.tvTimeStage.setText("全天");
                    } else {
                        hour = hour + time_stage;
                        mBinding.tvTimeStage.setText(item2 + " ~ " + item3);
                    }
                } else {
                    if (type == 2) {//全天
                        hour = 8;
                        mBinding.tvTimeStage.setText("全天");
                    } else {
                        hour = hour + time_stage;
                        mBinding.tvTimeStage.setText(s + "\n" + item2 + " ~ " + item3);
                    }
                }
                getTotalPrice(false);
            }
        });


        mPricePopupWindow = new ChooseDatePopupWindow(aty, 1);
        mPricePopupWindow.setTitle("请选择要加价的价格");
        List<String> data1 = new ArrayList<>();
        for (int i = 0; i <= 40; i++) {
            data1.add(String.valueOf(i * 5));
        }
        mPricePopupWindow.setData1(data1);
        mPricePopupWindow.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                if (mPrice + Float.valueOf(item1) < mDenomination) {
                    Toast.makeText(MaiLiApplication.getInstance(), "不满足该抵用劵的使用门槛！", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAddPrice = Float.valueOf(item1);
                mTotalPrice = mPrice + mAddPrice;
                mBinding.tvPrice.setText("￥" + (mTotalPrice - mKaPrice));
                mBinding.tvAddPrice.setText("加价" + mAddPrice + "元");
            }
        });
        mHuiheTime.setChooseType(1);

        mHuiheTime.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                long time = getTime2(item1);
                if (time != 0) {
                    mBinding.tvHuiheTime.setText(Utils.getDateToString(time, "MM月dd日"));
                    mHuiheTimeLong = time;
                }
            }
        });
    }

    private int mType = 2;//2 课程家教  3 艺术家教

    private void initListener() {
        mBinding.llyCourse.setOnClickListener(this);
        mBinding.llyClasses.setOnClickListener(this);
        mBinding.tvFromAdd.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        mBinding.tvNegative.setOnClickListener(this);
        mBinding.tvAddPrice.setOnClickListener(this);
        mBinding.tvReTotal.setOnClickListener(this);
        mBinding.rlyKajuan.setOnClickListener(this);
        mBinding.tvPriceInfo.setOnClickListener(this);
        mBinding.llyPreTime.setOnClickListener(this);
        mBinding.llyAddTimeStage.setOnClickListener(this);
        mDownOrderDialog.setIDownOrderListener(this);
        mBinding.etDay.addTextChangedListener(textWatcher);

    }


    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!TextUtils.isEmpty(s)) {
                getTotalPrice(false);
            }
        }
    };

    /**
     * 计算价格
     */
    private void getTotalPrice(boolean showLoading) {
        classes = mBinding.tvClasses.getText().toString().trim();
        course = mBinding.tvCourse.getText().toString().trim();


        day = mBinding.etDay.getText().toString().trim();
        if (mStartAddress == null) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请选择上课地点!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (mType == 2 && TextUtils.isEmpty(classes)) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请选择年级!", Toast.LENGTH_SHORT).show();
            }

            return;
        }
        if (TextUtils.isEmpty(course)) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请选择课程!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (hour == 0) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "请选择上课的时间段!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (TextUtils.isEmpty(day)) {
            if (showLoading) {
                Toast.makeText(MaiLiApplication.getInstance(), "需要上课的天数!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        if (showLoading) {
            showWaitDialog();
        }
        int cl = 1;
        if (mBinding.tvClasses.getTag() != null) {
            cl = (int) mBinding.tvClasses.getTag();
        }
        mPricePresenter.totalTutor(mStartAddress.getCity(), mType == 2 ? cl + "" : "", course);
    }

    private void initView() {
        ArrayList<String> xiaoxue = new ArrayList<>();
        xiaoxue.add("语文");
        xiaoxue.add("数学");
        xiaoxue.add("英语");
        xiaoxue.add("奥数");

        ArrayList<String> zhongxue = new ArrayList<>();
        zhongxue.addAll(xiaoxue);
        zhongxue.add("物理");
        zhongxue.add("化学");
        zhongxue.add("生物");
        zhongxue.add("历史");
        zhongxue.add("地理");
        zhongxue.add("政治");

        mData.put("一年级", xiaoxue);
        mData.put("二年级", xiaoxue);
        mData.put("三年级", xiaoxue);
        mData.put("四年级", xiaoxue);
        mData.put("五年级", xiaoxue);
        mData.put("六年级", xiaoxue);
        mData.put("初一", zhongxue);
        mData.put("初二", zhongxue);
        mData.put("初三", zhongxue);
        mData.put("高一", zhongxue);
        mData.put("高二", zhongxue);
        mData.put("高三", zhongxue);
        mClassesData.clear();
        mClassesData.add("一年级");
        mClassesData.add("二年级");
        mClassesData.add("三年级");
        mClassesData.add("四年级");
        mClassesData.add("五年级");
        mClassesData.add("六年级");
        mClassesData.add("初一");
        mClassesData.add("初二");
        mClassesData.add("初三");
        mClassesData.add("高一");
        mClassesData.add("高二");
        mClassesData.add("高三");


        mClassesPopupWindow = new ChooseDatePopupWindow(aty, 1);
        mClassesPopupWindow.setTitle("选择年级");

        mClassesPopupWindow.setData1(mClassesData);
        mClassesPopupWindow.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                updateCousr(item1);
                mBinding.tvClasses.setText(item1);
                mBinding.tvClasses.setTag(position1 + 1);
                getTotalPrice(false);
            }
        });
        updateCousr("一年级");
    }

    /**
     * 更新年级课程信息
     *
     * @param classes
     */
    private void updateCousr(String classes) {
        mCoursePopupWindow = new ChooseDatePopupWindow(aty, 1);
        mCourseData.clear();

        mCourseData.addAll(mData.get(classes));
        mCoursePopupWindow.setTitle("选择课程");
        mCoursePopupWindow.setData1(mCourseData);

        mCoursePopupWindow.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                mBinding.tvCourse.setText(item1);
                getTotalPrice(false);
            }
        });
    }

    private ChooseSortPopuWindow chooseSortPopuWindow;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_info://价格说明
                startActivity(new Intent(aty, WebViewActivity.class)
                        .putExtra("url", DemoUtils.TypeToPriceInfo(mType)));
                break;
            case R.id.lly_course:
                if (mType == 2) {
                    mCoursePopupWindow.showPopupWindow(mBinding.getRoot());
                } else {
                    chooseSortPopuWindow = new ChooseSortPopuWindow(aty);
                    chooseSortPopuWindow.showPopupWindow(mBinding.getRoot());
                }
                break;
            case R.id.tv_add_price:
                mPricePopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.lly_classes:
                mClassesPopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.lly_add_time_stage:
                mChooseTimeStagePopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.tv_pre_time:
                mChooseTime.showPopupWindow(mBinding.getRoot());

                break;
            case R.id.tv_from_add:
                startActivity(new Intent(aty, EditAddressActivity.class));
                break;
            case R.id.btn_ok:
                if (MaiLiApplication.getInstance().getUserInfo().getCheckStatus() == 0) {//没有实名认证
                    InformationDialog mStateDialog = new InformationDialog(aty);
                    mStateDialog.setTitle("提示");
                    mStateDialog.setMessage("请先认证身份才能继续操作哦!");
                    mStateDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                            startActivity(new Intent(aty, IdentityCardActivity.class).putExtra("type", 1));
                        }
                    });
                    mStateDialog.show();
                } else {
                    check();
                }
                break;
            case R.id.rly_kajuan://卡卷
//                startActivity(new Intent(aty, VoucherActivity.class)
//                        .putExtra("type", 1)
//                        .putExtra("workType", mType)
//                        .putExtra("orderMoney", mAddPrice + mPrice)
//                );
                break;
            case R.id.tv_negative:
                finish();
                break;
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_re_total:
                getTotalPrice(true);
                break;
            case R.id.lly_pre_time: //选择时间
               // mHuiheTime.showPopupWindow(mBinding.getRoot());
                mTimePickerDialog.show(getSupportFragmentManager(), "all");
                break;
        }
    }

    public void sortClick(View view) {
        TextView txt = (TextView) view;
        String content = txt.getText().toString().trim();
        mBinding.tvCourse.setText(content);
        chooseSortPopuWindow.dismiss();
        getTotalPrice(false);
    }

    private void check() {

        classes = mBinding.tvClasses.getText().toString();
        course = mBinding.tvCourse.getText().toString();

        day = mBinding.etDay.getText().toString().trim();

        if (mStartAddress == null) {
            DemoUtils.nope(mBinding.tvFromAdd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择上课地点!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mType == 2 && TextUtils.isEmpty(classes)) {
            DemoUtils.nope(mBinding.tvClasses).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择年级!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(course)) {
            DemoUtils.nope(mBinding.tvCourse).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请选择课程!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (hour == 0) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择上课的时间段!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(day)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择天数!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPrice == 0) {
            DemoUtils.nope(mBinding.tvReTotal).start();
            Toast.makeText(MaiLiApplication.getInstance(), "价格没出来？试试点击右下角的重新计算!", Toast.LENGTH_SHORT).show();
            return;
        }
        //checkOrder();
        checkData();
    }

    private void checkData() {
        String Content = mBinding.etContent.getText().toString().trim();
        String SpecialContent = mBinding.etSpecialContent.getText().toString().trim();
        String classes = mBinding.tvClasses.getText().toString();
        String course = mBinding.tvCourse.getText().toString();
        String time_stage = mBinding.tvTimeStage.getText().toString();
        String day = mBinding.etDay.getText().toString();
        String sex = "";
        if (mBinding.rbMan.isChecked()) {
            sex = mBinding.rbMan.getText().toString().trim();
        } else if (mBinding.rbWoman.isChecked()) {
            sex = mBinding.rbWoman.getText().toString().trim();
        } else if (mBinding.rbNoSex.isChecked()) {
            sex = mBinding.rbNoSex.getText().toString().trim();
        }
        if (mStartAddress == null) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择上课地点!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mType == 2 && TextUtils.isEmpty(classes)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择年级!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(course)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择课程!", Toast.LENGTH_SHORT).show();
            return;
        }
        String name = mStartAddress.getName() + mStartAddress.getAddress();
        String door_info = mStartAddress.getDoor_info();
        classes = TextUtils.isEmpty(classes) ? " " : classes;
        course = TextUtils.isEmpty(course) ? " " : course;
        time_stage = TextUtils.isEmpty(time_stage) ? " " : time_stage;
        day = TextUtils.isEmpty(day) ? " " : day;
        sex = TextUtils.isEmpty(sex) ? " " : sex;
        SpecialContent = TextUtils.isEmpty(SpecialContent) ? " " : SpecialContent;
        Content = TextUtils.isEmpty(Content) ? " " : Content;
        String content = "";


        TutorModel tutorModel = new TutorModel();
        if (mType == 2) {
            tutorModel.setGrade(classes);
        }
        tutorModel.setCourse(course);
        tutorModel.setStageTime(time_stage);
        tutorModel.setDayNum(day);
        tutorModel.setSex(sex);
        tutorModel.setStartTime(mHuiheTimeLong);
        tutorModel.setSpecialContent(SpecialContent);
        tutorModel.setContent(Content);

        content = ParseJsonUtils.getjsonStr(tutorModel);

        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mPresenter.SendOrders(userInfo.getPhone(),
                userInfo.getName(),
                mType + "",
                mHuiheTimeLong + "",
                "",
                mStartAddress.getProvince(),
                mStartAddress.getCity(),
                mStartAddress.getDistrict(),
                mStartAddress.getPoint().getLongitude() + "",
                mStartAddress.getPoint().getLatitude() + "",
                name,
                door_info,
                "",
                content,
                String.valueOf(mPrice + mAddPrice),
                String.valueOf(mPrice + mAddPrice),
                1 + "",
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                "0",
                mReservationTime + "",
                mMoneyType + "",
                mCashCouponId
        );
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if (parms.length > 0) {
                int parm = parms[0];
                if (parm == 10) {//支付宝
                    PayHelper.getInstance().AliPay(aty, info.getData());
                    PayHelper.getInstance().setIPayListener(new PayHelper.IPayListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(MaiLiApplication.getInstance(), "支付成功", Toast.LENGTH_SHORT).show();
                            checkData();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(MaiLiApplication.getInstance(), "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new UpdateNotice());
                    finish();
                    startActivity(new Intent(aty, SendOrderSuccessActivity.class).putExtra("type", mType));//发单成功
                }
            }
        } else if (object instanceof PriceModel) {
            PriceModel info = (PriceModel) object;
            mBinding.rlyTotalError.setVisibility(View.GONE);
            mBinding.llyPrice.setVisibility(View.VISIBLE);
            mBinding.rlyKajuan.setVisibility(View.GONE);


            String day = mBinding.etDay.getText().toString().trim();
            if (hour > 0 && !TextUtils.isEmpty(day)) {
                mPrice = info.getData() * hour * Double.valueOf(day);
                mBinding.tvPrice.setText("￥" + (mPrice + mAddPrice - mKaPrice));
            }
        } else if (object instanceof WEXModel) {//微信
            WEXModel info = (WEXModel) object;
            PayHelper.getInstance().WexPay(info);//微信支付
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(AddressModel addressModel) {
        mStartAddress = addressModel;
        String name = addressModel.getName();
        String door_info = addressModel.getDoor_info();
        mBinding.tvFromAdd.setText(name + door_info);
    }


    @Override
    public void downOrder(int type, double money, int money_type) {
        mMoneyType = money_type;
        switch (type) {
            case 1://余额
                mPayPwdPopupWindow = new PayPwdPopupWindow(aty);
                mPayPwdPopupWindow.setIPayListener(this);
                mPayPwdPopupWindow.showPopupWindow(mBinding.getRoot());

                break;
            case 2://支付包
                if (money == 0) {
                    checkData();
                } else {
                    mAliPayPresenter.getAliPay(MaiLiApplication.getInstance().getUserInfo().getPhone(), "蚂蚁快服支付订单", "蚂蚁快服发布" + DemoUtils.TypeToOccupation(mType) + "订单所支付的费用.", String.valueOf(money));
                }
                break;
            case 3://微信
                if (money == 0) {
                    checkData();
                } else {
                    mAliPayPresenter.getWexPay(MaiLiApplication.getInstance().getUserInfo().getPhone(), "蚂蚁快服发布" + DemoUtils.TypeToOccupation(mType) + "订单所支付的费用.", "蚂蚁快服支付订单", "", String.valueOf((int) (money * 100)));
                }

                break;
            case 4://银联

                break;
        }
    }

    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(String message) {
        if ("更新价格".equals(message)) {
            checkData();
        }
    }

    /**
     * 卡卷
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void UpdateNotice(VoucherModel.DataBean dataBean) {
//        if (dataBean != null) {
//            if (!TextUtils.isEmpty(dataBean.getId())) {
//                mKaPrice = dataBean.getFaceValue();
//                mCashCouponId = dataBean.getId();
//                mDenomination = dataBean.getThreshold();
//
//                mBinding.tvKajuan.setTextColor(getResources().getColor(R.color.colorffc000));
//                mBinding.tvKajuan.setText("¥\t-" + dataBean.getFaceValue());
//                mBinding.tvPrice.setText("¥" + (mPrice + mAddPrice - dataBean.getFaceValue()));
//            } else {
//                mKaPrice = 0;
//                mCashCouponId = "";
//                mDenomination = 0;
//                mBinding.tvKajuan.setTextColor(getResources().getColor(R.color.colortextnomal));
//                mBinding.tvKajuan.setText("未使用卡卷");
//                mBinding.tvPrice.setText("¥" + (mPrice + mAddPrice));
//            }
//        }
//    }

    @Override
    public void finishCheck() {
        checkData();
    }
}
