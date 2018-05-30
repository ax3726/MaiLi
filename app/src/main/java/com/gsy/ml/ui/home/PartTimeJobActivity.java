package com.gsy.ml.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityParttimejobLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.PartTimeJobModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.PartTimeJobPresenter;
import com.gsy.ml.prestener.home.SendOrdersPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.views.ChooseMapPopuwindow;
import com.gsy.ml.ui.views.InformationDialog;

import org.greenrobot.eventbus.EventBus;

import ml.gsy.com.library.utils.Utils;

import static com.gsy.ml.ui.utils.DemoUtils.TypeToContent;

/**
 * Created by Administrator on 2017/4/27.
 * 兼职详情类
 */

public class PartTimeJobActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityParttimejobLayoutBinding mBinding;
    private PartTimeJobPresenter pPresenter = new PartTimeJobPresenter(this);
    private SendOrdersPresenter mSendOrdersPresenter = new SendOrdersPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_parttimejob_layout;
    }

    private int mType = 0;// 查询  1 ：我的接单  2：我的发单
    private boolean misShowAdd = true;
    private boolean misShowPhone = false;

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("兼职详情");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private void initLinstener() {
        mBinding.tvCancel.setOnClickListener(this);
        mBinding.tvCommitOrder.setOnClickListener(this);
    }

    private String mOrder;
    private StateModel stateModel = new StateModel();

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityParttimejobLayoutBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                getData();
            }
        });
        mBinding.setStateModel(stateModel);
        mType = getIntent().getIntExtra("type", 0);
        mOrder = getIntent().getStringExtra("order");
        misShowAdd = getIntent().getBooleanExtra("isShowAdd", true);
        misShowPhone = getIntent().getBooleanExtra("isShowPhone", true);
        mBinding.tvTitle.getPaint().setFakeBoldText(true);//字体加粗
        initLinstener();
        getData();
    }

    /**
     * 获取订单详情信息
     */
    private void getData() {
        pPresenter.selectOrder(mOrder);
    }

    private void commitOrder() {
        if (mPartTimeJobModel == null) {
            Toast.makeText(MaiLiApplication.getInstance(), "没有订单信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        mSendOrdersPresenter.CommitOrders(mOrder,
                userInfo.getPhone(),
                userInfo.getName(),
                mPartTimeJobModel.getData().getSendOrders().getWorkCost() + "",
                mPartTimeJobModel.getData().getSendOrders().getWorkTotalCost() + "",
                "0"
        );
    }

    private InformationDialog mStateDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_commit_order:
                if (MaiLiApplication.getInstance().getUserInfo().getCheckStatus() == 0) {//没有实名认证
                    mStateDialog = new InformationDialog(aty);
                    mStateDialog.setTitle("提示");
                    mStateDialog.setMessage("请先认证身份才能接单哦!");
                    mStateDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                            startActivity(new Intent(aty, IdentityCardActivity.class).putExtra("type", 1));
                        }
                    });
                    mStateDialog.show();
                } else {
                    commitOrder();
                }


                break;
        }
    }

    public void setModel(PartTimeJobModel model) {
        if (model == null || model.getData() == null || model.getData().getSendOrders() == null) {
            return;
        }

        mWorkType = Integer.valueOf(model.getData().getSendOrders().getWorkType());
        mBinding.tvTitle.setText(model.getData().getSendOrders().getStartPlace()
                + DemoUtils.TypeToOccupation(Integer.parseInt(model.getData().getSendOrders().getWorkType())));
        mBinding.tvCategory.setText(DemoUtils.TypeToOccupation(Integer.parseInt(model.getData().getSendOrders().getWorkType())));
        int peopleNum = model.getData().getSendOrders().getPeopleNum();  //派发总人数
        int acceptPeopleNum = model.getData().getSendOrders().getAcceptPeopleNum();  //已接单人数
        int finishPeopleNum = model.getData().getSendOrders().getFinishPeopleNum();  //已完成人数
        if (mType == 0) {
            mBinding.tvPeople.setText(peopleNum - acceptPeopleNum - finishPeopleNum + "人");
        } else {
            mBinding.tvPeople.setText(peopleNum + "人");
        }

        mBinding.tvWorktime.setText(Utils.getDateToString(model.getData().getSendOrders().getSendTime(), "yyyy年MM月dd日HH:mm"));

        mBinding.tvLocation.setText(model.getData().getSendOrders().getStartArea());
        mBinding.tvGrade.setText(model.getData().getSendOrders().getWorkLevel() + "兼职");
        mBinding.tvMoneys.setText(model.getData().getSendOrders().getWorkCost() + "");
        PartTimeJobModel.DataBean.SendOrdersBean sendOrders = model.getData().getSendOrders();

        String address = model.getData().getSendOrders().getStartProvince()
                + model.getData().getSendOrders().getStartCity()
                + model.getData().getSendOrders().getStartArea()
                + model.getData().getSendOrders().getStartPlace()
                + model.getData().getSendOrders().getStartDoorplate();

        mBinding.tvWorkplace.setText(address);
        final AddressModel addressModel = new AddressModel();
        addressModel.setName(model.getData().getSendOrders().getStartPlace());
        addressModel.setAddress(model.getData().getSendOrders().getStartProvince()
                + model.getData().getSendOrders().getStartCity()
                + model.getData().getSendOrders().getStartArea()
                + model.getData().getSendOrders().getStartPlace());
        addressModel.setPoint(new LatLonPoint(Float.valueOf(model.getData().getSendOrders().getStartWei()), Float.valueOf(model.getData().getSendOrders().getStartJing())));

        mBinding.tvWorkplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ChooseMapPopuwindow(aty, addressModel).showPopupWindow();
            }
        });

        if (sendOrders.getWorkType().equals("1")) {

            if (mType != 0) {
                mBinding.llySend.setVisibility(View.VISIBLE);
                mBinding.tvSendName.setText(sendOrders.getSendPeopleName());
                mBinding.tvSendPhone.setText(sendOrders.getSendPeoplePhone());
            }

            mBinding.tvMe.setText("寄件地址:");
            String workProvince = sendOrders.getWorkProvince();
            String workCity = sendOrders.getWorkCity();
            String[] area = sendOrders.getWorkArea().split(",");
            String[] place = sendOrders.getWorkPlace().split(",");
            String[] door = null;
            if (!TextUtils.isEmpty(sendOrders.getWorkDoorplate())) {
                door = sendOrders.getWorkDoorplate().split(",");
            } else {
                door = new String[]{" "};
            }

            String[] jin = sendOrders.getWorkJing().split(",");
            String[] wei = sendOrders.getWorkWei().split(",");
            String[] people = sendOrders.getReceiptPeopleName().split(",");
            String[] phone = sendOrders.getReceiptPeoplePhone().split(",");
            mBinding.llyAddresss.removeAllViews();
            for (int i = 0; i < area.length; i++) {
                View item = View.inflate(aty, R.layout.item_address_info, null);
                TextView tv_me = (TextView) item.findViewById(R.id.tv_me);
                TextView tv_name = (TextView) item.findViewById(R.id.tv_name);
                TextView tv_phone = (TextView) item.findViewById(R.id.tv_phone);
                LinearLayout lly_name = (LinearLayout) item.findViewById(R.id.lly_name);
                TextView tv_name_hint = (TextView) item.findViewById(R.id.tv_name_hint);
                TextView tv_workplace = (TextView) item.findViewById(R.id.tv_workplace);
                String num = area.length > 1 ? String.valueOf(i + 1) + ":" : ":";
                tv_me.setText("配送地址" + num);
                tv_workplace.setText(workProvince + workCity + area[i] + place[i] + door[i]);
                if (mType != 0) {
                    lly_name.setVisibility(View.VISIBLE);
                    tv_phone.setText(phone[i]);
                    tv_name_hint.setText("收件人" + num);
                    tv_name.setText(people[i]);
                } else {
                    lly_name.setVisibility(View.GONE);
                }

                final AddressModel addressModel1 = new AddressModel();
                addressModel1.setName(place[i]);
                addressModel1.setAddress(workProvince + workCity + area[i] + place[i] + door[i]);
                addressModel1.setPoint(new LatLonPoint(Float.valueOf(wei[i]), Float.valueOf(jin[i])));
                tv_workplace.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ChooseMapPopuwindow(aty, addressModel1).showPopupWindow();
                    }
                });
                mBinding.llyAddresss.addView(item);
            }
        } else {
            mBinding.tvMe.setText("工作地点：");

        }
        String[] con = TypeToContent(Integer.valueOf(model.getData().getSendOrders().getWorkType()), model.getData().getSendOrders().getWorkContent());
        mBinding.tvPost.setText(Html.fromHtml(con[0] + "<br/>" + con[1]));
        mBinding.tvName.setText(model.getData().getSendOrders().getSendPeople());


        mBinding.tvPhones.setText(model.getData().getSendOrders().getSendPhone() + "");
        boolean isAccept = false;
        if (model.getData().getAcceptOrdersList() != null && model.getData().getAcceptOrdersList().size() > 0) {
            for (int i = 0; i < model.getData().getAcceptOrdersList().size(); i++) {
                if (MaiLiApplication.getInstance().getUserInfo().getPhone()
                        .equals(model.getData().getAcceptOrdersList().get(i).getAcceptPhone())) {
                    isAccept = true;
                }

            }
        }

        if (mType == 0 && !model.getData().getSendOrders().getSendPhone().equals(MaiLiApplication.getInstance().getUserInfo().getPhone()) && !isAccept) {
            mBinding.llyBottom.setVisibility(View.VISIBLE);
            if (model.getData().getSendOrders().getWorkType().equals("2") || model.getData().getSendOrders().getWorkType().equals("3")) {
                mBinding.tvCommitOrder.setText("提交申请");
            } else {
                mBinding.tvCommitOrder.setText("立即接单");
            }
        }
        mBinding.llyAddress.setVisibility(misShowAdd ? View.VISIBLE : View.GONE);
        mBinding.llyAddresss.setVisibility(misShowAdd ? View.VISIBLE : View.GONE);
        mBinding.rlyPhone.setVisibility(misShowPhone ? View.VISIBLE : View.GONE);

    }

    private PartTimeJobModel mPartTimeJobModel = null;
    private int mWorkType = 0;

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel model = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(model.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), model.getInfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof PartTimeJobModel) {
            mPartTimeJobModel = (PartTimeJobModel) object;
            setModel(mPartTimeJobModel);
            stateModel.setEmptyState(EmptyState.NORMAL);
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if (mWorkType == 1) {
                Intent intent = new Intent("SocaketReceiver");//开始长连接和连续定位
                sendBroadcast(intent);
            }

            Toast.makeText(MaiLiApplication.getInstance(), "接单成功！", Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new UpdateNotice());
            EventBus.getDefault().post("update_order");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }.start();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
