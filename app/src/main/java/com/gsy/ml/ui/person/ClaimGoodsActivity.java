package com.gsy.ml.ui.person;

import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityClaimGoodsLayoutBinding;
import com.gsy.ml.model.EventMessage.EvakuateUpdate;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.home.ClaimGoodsModel;
import com.gsy.ml.model.person.AcceptPeopleModel;
import com.gsy.ml.model.person.CheckTimeModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.ClaimGoodsPresenter;
import com.gsy.ml.prestener.person.DistributionPresenter;
import com.gsy.ml.ui.common.PhotoActivity;
import com.gsy.ml.ui.utils.UpdataImgUtils;
import com.gsy.ml.ui.views.FullyLinearLayoutManager;
import com.gsy.ml.ui.views.InformationDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.MyUUID;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/5/12.
 */

public class ClaimGoodsActivity extends PhotoActivity implements View.OnClickListener, ILoadPVListener, UpdataImgUtils.UpdataImgCallBack {
    private ActivityClaimGoodsLayoutBinding mBinding;
    private ClaimGoodsPresenter presenter = new ClaimGoodsPresenter(this);
    private DistributionPresenter mDistributionPresenter = new DistributionPresenter(this);
    private CommonAdapter<AcceptPeopleModel> adapter;
    private List<AcceptPeopleModel> list = new ArrayList<>();

    private String mOrder = "";
    private int mPage = 1;
    private int mCurPosition = 0;
    private UpdataImgUtils mUpdataImgUtils;

    @Override
    public int getLayoutId() {
        return R.layout.activity_claim_goods_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("我要取货");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        mBinding.tvArrive.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityClaimGoodsLayoutBinding) vdb;
        mOrder = getIntent().getStringExtra("order");
        mPage = getIntent().getIntExtra("page", 1);
        mUpdataImgUtils = new UpdataImgUtils(this, aty);
        initClaimGoods();
        getClaimGoods();

    }

    //检测时间是否超时
    private void checkTime() {
        showWaitDialog();
        mDistributionPresenter.checkPickUpTime(mOrder);
    }


    private List<String> objectKey = new ArrayList<>();

    private int updataNum = 0;
    private int updataSusNum = 0;
    private boolean isupdataAllSus = true;

    /**
     * 上传图片
     */
    private void submitImg() {
        boolean bl = true;
        for (AcceptPeopleModel s : list) {
            if (TextUtils.isEmpty(s.getPhoto())) {
                bl = false;
            }
        }
        if (bl) {
            objectKey.clear();
            List<String> mImgList = new ArrayList<>();
            for (AcceptPeopleModel s : list) {
                objectKey.add(Link.CERTIFICATE + MyUUID.nolineUUID() + ".jpg");
                mImgList.add(s.getPhoto());

            }
            isupdataAllSus = true;
            updataNum = mImgList.size();
            updataSusNum = 0;
            showWaitDialog();
            mUpdataImgUtils.updataImgs(Link.bucketName, objectKey, mImgList);
        } else {
            Toast.makeText(MaiLiApplication.getInstance(), "请上传物品照片！", Toast.LENGTH_SHORT).show();
        }

    }

    public void initClaimGoods() {

        adapter = new CommonAdapter<AcceptPeopleModel>(aty, R.layout.item_claim_goods, list) {

            @Override
            protected void convert(ViewHolder holder, AcceptPeopleModel acceptPeopleModel, final int position) {
                TextView tv_txt = holder.getView(R.id.tv_txt);
                TextView tv_shou_name = holder.getView(R.id.tv_shou_name);
                TextView tv_shou_phone = holder.getView(R.id.tv_shou_phone);
                TextView tv_shou_address = holder.getView(R.id.tv_shou_address);
                ImageView img_add = holder.getView(R.id.img_add);
                tv_txt.setText("收货人" + (position + 1) + ":");
                tv_shou_name.setText(acceptPeopleModel.getName());
                tv_shou_phone.setText(acceptPeopleModel.getPhone());
                tv_shou_address.setText(acceptPeopleModel.getAddress());
                Glide.with(aty.getApplication()).load(acceptPeopleModel.getPhoto()).asBitmap().placeholder(R.drawable.add_photo_icon).into(img_add);
                img_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doPhoto();
                        mCurPosition = position;
                    }
                });
            }
        };

        FullyLinearLayoutManager linearLayoutManager = new FullyLinearLayoutManager(aty, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mBinding.rvClaim.setLayoutManager(linearLayoutManager);
        mBinding.rvClaim.setAdapter(adapter);
    }

    private InformationDialog informationDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.btn_ok:
                if (mArriveTime > 0) {
                    submitImg();
                } else {
                    Toast.makeText(aty,"请先确认到达取货地点!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_Arrive:
                if (!mBinding.tvArrive.isSelected()) {
                    informationDialog = new InformationDialog(aty);
                    informationDialog.setTitle("温馨提示");
                    informationDialog.setMessage("请确保<font color= '#FF0000'>您已到达目的地,提前暂停</font>会降低你的信用积分,<font color= '#FF0000'>积分过低会直接影响您的接单!</font>");
                    informationDialog.setPositiveButton("确认到达", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            checkTime();
                            dlg.dismiss();
                        }
                    });
                    informationDialog.setNegativeButton("取消", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                        }
                    });
                    informationDialog.show();
                }
                break;
        }
    }

    public void getClaimGoods() {
        showWaitDialog();
        presenter.selectOrder(mOrder);
    }

    private ClaimGoodsModel mClaimGoodsModel;
    private long mArriveTime=0;//取件时间

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if ("505".equals(errorModel.getStatus())) {//取件超时
                mArriveTime=Integer.valueOf(errorModel.getData());
                mTimeHandler.removeCallbacks(mRunnable);
                mBinding.tvTime.setText("取货倒计时:已超时!");
                mBinding.tvArrive.setSelected(true);
            } else {
                Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
            }

        } else if (object instanceof ClaimGoodsModel) {
            mClaimGoodsModel = (ClaimGoodsModel) object;
            initView(mClaimGoodsModel);
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel model = (HttpSuccessModel) object;
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
        } else if (object instanceof CheckTimeModel) {
            CheckTimeModel model = (CheckTimeModel) object;
            mArriveTime=Integer.valueOf(model.getData());
            mTimeHandler.removeCallbacks(mRunnable);
            mBinding.tvTime.setText("取件耗时:\t"+Utils.getTimeCha(Long.valueOf(model.getData())));
            mBinding.tvArrive.setSelected(true);
        }
    }

    private long mTime = 0;
    private Handler mTimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mTime > 0) {
                mBinding.tvTime.setText("取货倒计时:\t" + Utils.getTimeCha(mTime));

            } else {
                mBinding.tvTime.setText("取货倒计时:\t" + Utils.getTimeCha(mTime));

            }
            postDelayed(mRunnable, 1000);
        }
    };
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mTime = mTime - 1000;
            if (mTime > 0) {
                mTimeHandler.sendEmptyMessage(0);
            }
        }
    };

    private void initView(ClaimGoodsModel model) {
        if (model == null) {
            return;
        }


            if (model.getData().getSendOrders() != null) {
                ClaimGoodsModel.DataBean.SendOrdersBean sendOrders = model.getData().getSendOrders();
                if (model.getData().getAcceptOrders().size() > 0) {
                    ClaimGoodsModel.DataBean.AcceptOrdersBean acceptOrdersBean = model.getData().getAcceptOrders().get(0);
                    long pickup_time = acceptOrdersBean.getPickup_time();//预计取货时间
                    long time_cha = pickup_time - System.currentTimeMillis();
                    long pickupTime = model.getData().getPickupTime();
                    if (pickupTime != 0) {
                        mTimeHandler.removeCallbacks(mRunnable);
                        mBinding.tvTime.setText("取件耗时:\t" + Utils.getTimeCha(pickupTime));
                        mBinding.tvArrive.setSelected(true);
                    } else {
                        if (time_cha >= 0) {
                            mTime = time_cha;
                            mTimeHandler.sendEmptyMessage(0);
                        } else {
                            mBinding.tvTime.setText("取货倒计时:\t已超时");
                        }
                    }


                }

                mBinding.tvName.setText(TextUtils.isEmpty(sendOrders.getSendPeopleName()) ? "未知" : sendOrders.getSendPeopleName());
                mBinding.tvPhone.setText(TextUtils.isEmpty(sendOrders.getSendPeoplePhone()) ? "未知" : sendOrders.getSendPeoplePhone());
                mBinding.tvAddress.setText(sendOrders.getStartProvince() + sendOrders.getStartCity() + sendOrders.getStartArea() + sendOrders.getStartPlace());

                if (sendOrders.getIfSingle() == 1) {//联单
                    String acceptPeopleName = sendOrders.getReceiptPeopleName();
                    int lenth = TextUtils.isEmpty(acceptPeopleName) ? 0 : acceptPeopleName.split(",").length;
                    if (lenth > 0) {
                        String[] name = acceptPeopleName.split(",");
                        String[] phone = sendOrders.getReceiptPeoplePhone().split(",");
                        String[] workArea = sendOrders.getWorkArea().split(",");
                        String[] workPlace = sendOrders.getWorkPlace().split(",");
                        String[] workDoorplate = sendOrders.getWorkDoorplate().split(",");
                        list.clear();
                        for (int i = 0; i < lenth; i++) {
                            AcceptPeopleModel acceptPeopleModel = new AcceptPeopleModel();
                            acceptPeopleModel.setName(name[i]);
                            acceptPeopleModel.setPhone(phone[i]);
                            acceptPeopleModel.setAddress(sendOrders.getWorkProvince() + sendOrders.getWorkCity() + workArea[i] + workPlace[i] + workDoorplate[i]);
                            list.add(acceptPeopleModel);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    list.clear();
                    AcceptPeopleModel acceptPeopleModel = new AcceptPeopleModel();
                    acceptPeopleModel.setName(sendOrders.getReceiptPeopleName());
                    acceptPeopleModel.setPhone(sendOrders.getReceiptPeoplePhone());
                    acceptPeopleModel.setAddress(sendOrders.getWorkProvince() + sendOrders.getWorkCity() + sendOrders.getWorkArea() + sendOrders.getWorkPlace());
                    list.add(acceptPeopleModel);
                    adapter.notifyDataSetChanged();
                }

            }





    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    public void photoSuccess(String path, File file, int... queue) {
        list.get(mCurPosition).setPhoto(path);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void photoFaild() {
        Toast.makeText(MaiLiApplication.getInstance(), "图片加载失败！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdataImgResult(String key, boolean state) {
        if (!state) {
            isupdataAllSus = false;
            hideWaitDialog();
            Toast.makeText(MaiLiApplication.getInstance(), "图片上传失败", Toast.LENGTH_SHORT).show();
            mUpdataImgUtils.cancelUpdata();
        } else {
            updataSusNum++;
        }
        if (updataSusNum >= updataNum && isupdataAllSus) {
            submitData();
            isupdataAllSus = false;
        }
    }

    /**
     * 提交图片
     */
    private void submitData() {
        if (mClaimGoodsModel == null) {
            Toast.makeText(MaiLiApplication.getInstance(), "订单数据异常!", Toast.LENGTH_SHORT).show();
            return;
        }
        String sendPeopleName = mClaimGoodsModel.getData().getSendOrders().getSendPeopleName();
        String sendPeoplePhone = mClaimGoodsModel.getData().getSendOrders().getSendPeoplePhone();
        String acceptName = "";
        String acceptPhone = "";
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                acceptName = acceptName + list.get(i).getName();
                acceptPhone = acceptPhone + list.get(i).getPhone();
            } else {
                acceptName = acceptName + list.get(i).getName() + ",";
                acceptPhone = acceptPhone + list.get(i).getPhone() + ",";
            }
        }
        String imgs = "";
        for (int i = 0; i < objectKey.size(); i++) {
            if (i == objectKey.size() - 1) {
                imgs = imgs + Link.ALIIMAGEURL + objectKey.get(i);

            } else {
                imgs = imgs + Link.ALIIMAGEURL + objectKey.get(i) + ",";

            }
        }
        presenter.insertOrderImg(mOrder,
                sendPeopleName,
                sendPeoplePhone,
                MaiLiApplication.getInstance().getUserInfo().getPhone(),
                acceptName,
                acceptPhone,
                imgs);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTimeHandler.removeCallbacks(mRunnable);
    }
}
