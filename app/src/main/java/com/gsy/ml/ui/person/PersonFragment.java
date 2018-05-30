package com.gsy.ml.ui.person;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.FragmentPersonLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.InformationPresenter;
import com.gsy.ml.prestener.person.SelectOrderPresenter;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.common.PhotoPreviewActivity;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/18.
 * “我的”类
 */

public class PersonFragment extends BaseFragment implements View.OnClickListener, ILoadPVListener {
    public FragmentPersonLayoutBinding mBinding;
    private SelectOrderPresenter mSelectOrderPresenter = new SelectOrderPresenter(this);
    private InformationPresenter mPresenter = new InformationPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.fragment_person_layout;
    }

    protected void initData() {
        super.initData();
        EventBus.getDefault().register(this);
        mBinding = (FragmentPersonLayoutBinding) vdb;
        initListener();
        initUser();
        selectNoice();
    }


    public void initUser() {
        if (mBinding == null) {
            return;
        }
        UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUserInfo();
        Glide.with(MaiLiApplication.appliactionContext)
                .load(userInfo.getHeadUrl())
                .asBitmap()
                .placeholder(R.drawable.home_head_img_icon)
                .into(mBinding.imgHead);
        mBinding.tvName.setText(TextUtils.isEmpty(userInfo.getNickname()) ? "未知" : userInfo.getNickname());
        mBinding.tvPhone.setText(userInfo.getPhone());


        if (userInfo.getConfinement() == 1) { //冻结
            mBinding.tvState.setText("已冻结");
            mBinding.tvState.setTextColor(getResources().getColor(R.color.colorFF0000));
            mBinding.tvDongjie.setVisibility(View.VISIBLE);
            mBinding.tvDongjie.setText(Utils.getTimeStyle222(userInfo.getConfinementTime()));
        } else {
            mBinding.tvDongjie.setVisibility(View.GONE);
            if (userInfo.getCheckStatus() == 1) {
                mBinding.tvState.setText("已认证");
                mBinding.tvState.setTextColor(getResources().getColor(R.color.colorFF6C00));
            } else {
                mBinding.tvState.setText("未认证");
                mBinding.tvState.setTextColor(getResources().getColor(R.color.click_bg));
            }
        }
    }

    private void initListener() {
        mBinding.tvSetting.setOnClickListener(this);
        mBinding.tvResume.setOnClickListener(this);
        mBinding.tvWallet.setOnClickListener(this);
        mBinding.rlyMore.setOnClickListener(this);
        mBinding.rlyUserinfo.setOnClickListener(this);
        mBinding.llyBillingShenqin.setOnClickListener(this);
        mBinding.llyBillingJinxing.setOnClickListener(this);
        mBinding.llyBillingWancheng.setOnClickListener(this);
        mBinding.llyBillingPingjia.setOnClickListener(this);
        mBinding.llyBillingQuxiao.setOnClickListener(this);
        mBinding.llyOrdersShenqin.setOnClickListener(this);
        mBinding.llyOrdersJinxing.setOnClickListener(this);
        mBinding.llyOrdersWancheng.setOnClickListener(this);
        mBinding.llyOrdersPingjia.setOnClickListener(this);
        mBinding.llyOrdersQuxiao.setOnClickListener(this);
        mBinding.tvIntegral.setOnClickListener(this);
        mBinding.tvLocation.setOnClickListener(this);
        mBinding.llyMore.setOnClickListener(this);
        mBinding.tvPartner.setOnClickListener(this);
        mBinding.imgHead.setOnClickListener(this);
        mBinding.tvSpeediness.setOnClickListener(this);

        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectNoice();
                        mPresenter.getUserInfo(MaiLiApplication.getInstance().getUserInfo().getPhone());
                    }
                }, 600);
            }
        });
    }

    /**
     * 查找订单条数
     */
    private void selectNoice() {
        mSelectOrderPresenter.selectOrder2(MaiLiApplication.getInstance().getUserInfo().getPhone(), "0", "1", "10");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_setting:
                startActivity(new Intent(aty, SettingsActivity.class));
                break;
            case R.id.rly_userinfo:
                startActivity(new Intent(aty, IdentityCardActivity.class).putExtra("type", 1));
                break;
            case R.id.tv_resume:
                startActivity(new Intent(aty, ResumeActivity.class));
                break;
            case R.id.tv_wallet:
                startActivity(new Intent(aty, WalletActivity.class));
                break;
            case R.id.rly_more:
                startActivity(new Intent(aty, OrderReceivingActivity.class));
                break;
            case R.id.lly_more:
                startActivity(new Intent(aty, OrderBillingActivity.class));
                break;
            case R.id.lly_billing_shenqin:
                startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 0));
                break;
            case R.id.lly_billing_jinxing:
                startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 1));
                break;
            case R.id.lly_billing_wancheng:
                startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 2));
                break;
            case R.id.lly_billing_pingjia:
                startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 4));
                break;
            case R.id.lly_billing_quxiao:
                startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 3));
                break;
            case R.id.lly_orders_shenqin:
                startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 0));
                break;
            case R.id.lly_orders_jinxing:
                startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 1));
                break;
            case R.id.lly_orders_wancheng:
                startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 2));
                break;
            case R.id.lly_orders_pingjia:
                startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 4));
                break;
            case R.id.lly_orders_quxiao:
                startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 3));
                break;
            case R.id.tv_integral:
                startActivity(new Intent(aty, IntegerActivity.class));
                break;
            case R.id.tv_location:
                startActivity(new Intent(aty, MoreMessageActivity.class));
                break;
            case R.id.tv_partner:
                startActivity(new Intent(aty, PartnerActivity.class));
                break;
            case R.id.img_head:
                if (!TextUtils.isEmpty(MaiLiApplication.getInstance().getUserInfo().getHeadUrl())) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class).putExtra("img", MaiLiApplication.getInstance().getUserInfo().getHeadUrl()));
                }
                break;
            case R.id.tv_speediness:
//                startActivity(new Intent(aty, SpeedinessShipmentsActivity.class));
//                startActivity(new Intent(aty, FastConsignmentInvoiceActivity.class));
                startActivity(new Intent(aty, AddPlatformActivity.class));
                break;
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        mBinding.mrlBody.finishRefresh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            if (!TextUtils.isEmpty(info.getData())) {
                String[] split = info.getData().split(",");
                if (split.length == 6) {
                    int integer1 = Integer.valueOf(split[0]);
                    int integer2 = Integer.valueOf(split[1]);
                    int integer3 = Integer.valueOf(split[2]);
                    int integer4 = Integer.valueOf(split[3]);
                    int integer5 = Integer.valueOf(split[4]);
                    int integer6 = Integer.valueOf(split[5]);

                    mBinding.llyNotice1.setVisibility(integer1 > 0 ? View.VISIBLE : View.GONE);
                    mBinding.llyNotice2.setVisibility(integer2 > 0 ? View.VISIBLE : View.GONE);
                    mBinding.llyNotice3.setVisibility(integer3 > 0 ? View.VISIBLE : View.GONE);
                    // mBinding.llyNotice4.setVisibility(integer4 > 0 ? View.VISIBLE : View.GONE);
                    mBinding.llyNotice5.setVisibility(integer5 > 0 ? View.VISIBLE : View.GONE);
                    mBinding.llyNotice6.setVisibility(integer6 > 0 ? View.VISIBLE : View.GONE);

                    mBinding.tvNotice1.setText(integer1 > 99 ? "99+" : integer1 + "");
                    mBinding.tvNotice2.setText(integer2 > 99 ? "99+" : integer2 + "");
                    mBinding.tvNotice3.setText(integer3 > 99 ? "99+" : integer3 + "");
                    //  mBinding.tvNotice4.setText(integer4 > 99 ? "99+" : integer4 + "");
                    mBinding.tvNotice5.setText(integer5 > 99 ? "99+" : integer5 + "");
                    mBinding.tvNotice6.setText(integer6 > 99 ? "99+" : integer6 + "");
                }
            }
        } else if (object instanceof UserInfoModel) {
            UserInfoModel info = (UserInfoModel) object;
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    /**
     * 更新消息条数
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(UpdateNotice message) {
        selectNoice();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }
}
