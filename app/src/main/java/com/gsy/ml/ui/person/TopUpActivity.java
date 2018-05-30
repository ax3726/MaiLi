package com.gsy.ml.ui.person;

import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityTopUpActivityBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.WEXModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.AliPayPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.PayHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by Administrator on 2017/5/19.
 * 充值
 */
public class TopUpActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityTopUpActivityBinding mBinding;
    private AliPayPresenter mPresenter = new AliPayPresenter(this);
    private UserInfoModel.UserInfoBean user;

    @Override
    public int getLayoutId() {
        return R.layout.activity_top_up_activity;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("充值");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    private void initListener() {
        mBinding.btnPay.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();

    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityTopUpActivityBinding) vdb;
        user = MaiLiApplication.getInstance().getUserInfo();
        mBinding.rbZhi.setChecked(true);
        initListener();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.btn_pay:
                checkPay();
                break;
        }
    }

    private void checkPay() {
        String money = mBinding.etMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入需要充值的金额!", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        if (mBinding.rbZhi.isChecked()) {//支付宝
            mPresenter.getAliPay(user.getPhone(), "蚂蚁快服充值", "蚂蚁快服充值", String.valueOf(Float.valueOf(money)));
        } else if (mBinding.rbWei.isChecked()) {//微信
            mPresenter.getWexPay(user.getPhone(), "蚂蚁快服充值", "充值", "", String.valueOf((int) (Float.valueOf(money) * 100)));
        } else if (mBinding.rbYin.isChecked()) {//银联

        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {

            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            PayHelper.getInstance().AliPay(aty, info.getData());
            PayHelper.getInstance().setIPayListener(new PayHelper.IPayListener() {
                @Override
                public void onSuccess() {
                    EventBus.getDefault().post("更新价格");//充值成功 更新价格
                    finish();
                    Toast.makeText(MaiLiApplication.getInstance(), "支付成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFail() {
                    Toast.makeText(MaiLiApplication.getInstance(), "支付失败", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (object instanceof WEXModel) {//微信
            WEXModel info = (WEXModel) object;
            PayHelper.getInstance().WexPay(info);//微信支付
        }
    }

    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(String message) {
        if ("更新价格".equals(message)) {
            finish();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
