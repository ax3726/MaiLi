package com.gsy.ml.ui.person;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityCheckCodeBinding;
import com.gsy.ml.model.EventMessage.EvakuateUpdate;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.CheckCodeItemModel;
import com.gsy.ml.model.person.CheckCodeModel;
import com.gsy.ml.model.person.CheckTimeModel;
import com.gsy.ml.model.person.DeliveryModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.CheckCodePresenter;
import com.gsy.ml.prestener.person.DistributionPresenter;
import com.gsy.ml.prestener.person.FinishOrderPresenter;
import com.gsy.ml.ui.common.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

public class CheckCodeActivity extends BaseActivity implements ILoadPVListener, View.OnClickListener {
    private ActivityCheckCodeBinding mBinding;

    private FinishOrderPresenter mFinishOrderPresenter = new FinishOrderPresenter(this);
    private DistributionPresenter mDistributionPresenter = new DistributionPresenter(this);
    private CheckCodePresenter mCheckCodePresenter = new CheckCodePresenter(this);
    private CommonAdapter<CheckCodeItemModel> mAdapter;
    private List<CheckCodeItemModel> mList = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.activity_check_code;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("我已取货");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private String mOrder = "";
    private int mPage = 1;
    private int mSelectPositon = 0;//选中的下标
    private HashMap<Integer, CountDownTimer> countDownMap = new HashMap<>();

    /**
     * 清空资源
     */
    public void cancelAllTimers() {
        if (countDownMap == null) {
            return;
        }
        Log.e("TAG", "size :  " + countDownMap.size());
        for (CountDownTimer cdt : countDownMap.values()) {

            if (cdt != null) {
                cdt.cancel();
            }
        }
    }

    private void getServiceData() {
        showWaitDialog();
        mCheckCodePresenter.pickUpOrders(mOrder);
    }

    private void check(String code, int index) {

        if (TextUtils.isEmpty(code) || code.length() != 6) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入6位数的验证码!", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        mCheckCodePresenter.checkCode(mOrder, code, index + "");
    }

    //检测时间是否超时
    private void checkTime(int index) {
        showWaitDialog();
        mDistributionPresenter.checkCkdelveryTime(mOrder, "" + index);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityCheckCodeBinding) vdb;
        mOrder = getIntent().getStringExtra("order");
        mPage = getIntent().getIntExtra("page", 1);
        mAdapter = new CommonAdapter<CheckCodeItemModel>(aty, R.layout.item_check_code, mList) {
            @Override
            protected void convert(ViewHolder holder, final CheckCodeItemModel model, final int position) {
                holder.setIsRecyclable(false);
                TextView tv_name = holder.getView(R.id.tv_name);
                TextView tv_phone = holder.getView(R.id.tv_phone);
                TextView tv_address = holder.getView(R.id.tv_address);
                final TextView arrive = holder.getView(R.id.tv_arrive);
                final TextView tv_time = holder.getView(R.id.tv_time);
                final TextView tv_service = holder.getView(R.id.tv_service);
                final TextView et_code = holder.getView(R.id.et_code);

                tv_name.setText(model.getName());
                tv_phone.setText(model.getPhone());
                tv_address.setText(model.getAddress());

                if (model.getCode_state() == 1) {
                    et_code.setText(model.getCode());
                    tv_service.setText("已送达");
                    tv_service.setSelected(true);
                } else {
                    tv_service.setSelected(false);
                }
                CountDownTimer countDownTimer = countDownMap.get(position);
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                if (model.getService_time() > 0) {//已送达
                    long time_out = model.getService_timeout() - System.currentTimeMillis();
                    tv_time.setText("送件耗时:\t" + Utils.getTimeCha(model.getService_time())+(time_out<0?"(已超时)":""));
                    arrive.setSelected(true);
                } else {
                    long time_out = model.getService_timeout() - System.currentTimeMillis();
                    if (time_out > 0) {
                        countDownTimer = new CountDownTimer(time_out, 1000) {
                            public void onTick(long millisUntilFinished) {
                                tv_time.setText("送件倒计时:\t" + Utils.getTimeCha(millisUntilFinished));
                            }

                            public void onFinish() {
                                tv_time.setText("送件倒计时:\t已超时");
                            }
                        }.start();
                        countDownMap.put(position, countDownTimer);
                    } else {
                        tv_time.setText("送件倒计时:\t已超时");
                    }
                }

                arrive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!arrive.isSelected()) {
                            mSelectPositon = position;
                            if (mDatas.size() == 1) {
                                checkTime(6);
                            } else {
                                checkTime(position);
                            }

                        }
                    }
                });
                tv_service.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!tv_service.isSelected()) {
                            if (model.getService_time() > 0) {
                                mSelectPositon = position;
                                String code = et_code.getText().toString().trim();
                                if (mDatas.size() == 1) {
                                    check(code, 6);
                                } else {
                                    check(code, position);
                                }
                            } else {
                                Toast.makeText(aty, "请先确认到达目的地!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        };
        mBinding.rlArrive.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rlArrive.setAdapter(mAdapter);
        getServiceData();


    }


    private void confirOrder(String order) {
        showWaitDialog("正在完成订单!");
        mFinishOrderPresenter.finishOrder(order, MaiLiApplication.getInstance().getUserInfo().getPhone(), 1);
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if ("505".equals(errorModel.getStatus())) {//取件超时
                mList.get(mSelectPositon).setService_time(Long.valueOf(errorModel.getData()));
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
            }
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel model = (HttpSuccessModel) object;
            Intent intent = new Intent("ReLocationReceiver");//结束长连接和连续定位
            intent.putExtra("type", "end");
            aty.sendBroadcast(intent);
            Toast.makeText(MaiLiApplication.getInstance(), model.getInfo(), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new EvakuateUpdate("", mPage));
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } else if (object instanceof CheckCodeModel) {
            CheckCodeModel model = (CheckCodeModel) object;
            mList.get(mSelectPositon).setCode_state(1);
            mAdapter.notifyDataSetChanged();
            checkDataSubmit();
        } else if (object instanceof DeliveryModel) {
            DeliveryModel model = (DeliveryModel) object;
            setView(model);
            checkDataSubmit();
        } else if (object instanceof CheckTimeModel) {//检测是否超时
            CheckTimeModel model = (CheckTimeModel) object;
            mList.get(mSelectPositon).setService_time(Long.valueOf(model.getData()));
            mAdapter.notifyDataSetChanged();
        }
    }

    private void checkDataSubmit() {
        boolean bl = false;
        for (CheckCodeItemModel ck : mList) {
            if (ck.getCode_state() == 0) {
                bl = true;
            }
        }
        if (!bl) {
            confirOrder(mOrder);//完成订单
        }
    }

    private void setView(DeliveryModel model) {
        if (model == null) {
            return;
        }
        DeliveryModel.DataBean.AcceptOrdersBean acceptOrders = model.getData().getAcceptOrders();
        DeliveryModel.DataBean.DistributionImgBean distributionImg = model.getData().getDistributionImg();
        String[] area = acceptOrders.getWorkArea().split(",");
        String[] place = acceptOrders.getWorkPlace().split(",");
        String[] doorplate = acceptOrders.getWorkDoorplate().split(",");
        String[] name = acceptOrders.getReceiptPeopleName().split(",");
        String[] phone = acceptOrders.getReceiptPeoplePhone().split(",");
        String[] code = distributionImg.getPhoneCode().split(",");
        String[] arrice_state = distributionImg.getArriveStatus().split(",");
        String[] servicetimes = TextUtils.isEmpty(acceptOrders.getServiceTimes()) ? new String[]{"0"} : acceptOrders.getServiceTimes().split(",");//时长
        String[] servicetimeout = acceptOrders.getServiceTimeout().split(",");//时间
        mList.clear();
        for (int i = 0; i < area.length; i++) {
            CheckCodeItemModel checkCodeItemModel = new CheckCodeItemModel();
            checkCodeItemModel.setName(name[i]);
            checkCodeItemModel.setPhone(phone[i]);
            checkCodeItemModel.setCode(code[i]);
            if (servicetimes.length - 1 >= i) {
                checkCodeItemModel.setService_time(Long.valueOf(servicetimes[i]));
            }
            checkCodeItemModel.setService_timeout(Long.valueOf(servicetimeout[i]));
            checkCodeItemModel.setCode_state(Integer.valueOf(arrice_state[i]));
            checkCodeItemModel.setAddress(acceptOrders.getWorkProvince() + acceptOrders.getWorkCity() + area[i] + place[i] + doorplate[i]);
            mList.add(checkCodeItemModel);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAllTimers();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
        }
    }
}
