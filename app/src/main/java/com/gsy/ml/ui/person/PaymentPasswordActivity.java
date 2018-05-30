package com.gsy.ml.ui.person;

import android.os.Handler;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPaymentPasswordBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.PaymentPasswordModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.LoginPresenter;
import com.gsy.ml.prestener.main.RegisterPresenter;
import com.gsy.ml.prestener.person.PaymentPasswordPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.MD5;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2017/6/20.
 */

public class PaymentPasswordActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityPaymentPasswordBinding mBinding;
    private PaymentPasswordPresenter presenter = new PaymentPasswordPresenter(this);
    private RegisterPresenter mPresenter = new RegisterPresenter(this);
    private LoginPresenter mLoginPresenter = new LoginPresenter(this);
    private String phone = MaiLiApplication.getInstance().getUserInfo().getPhone(), code = "", payment = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_payment_password;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("设置支付密码");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityPaymentPasswordBinding) vdb;
        mBinding.tvCode.setOnClickListener(this);
        mBinding.btRegistration.setOnClickListener(this);
        mBinding.ivDidian.setOnClickListener(this);
        mBinding.etPhone.setText(phone);
    }


    public void userPayPwd() {
        code = mBinding.edCode.getText().toString().trim();      //验证码
        payment = mBinding.edPayment.getText().toString().trim();  //支付密码
        if (TextUtils.isEmpty(code)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入验证码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(payment)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入支付密码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (payment.length() < 6) {
            Toast.makeText(MaiLiApplication.getInstance(), "支付密码格式不正确！", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog();
        presenter.getPayment(phone, MD5.MD5Pwd(phone, payment), code);
    }

    private boolean countDownClickable = true;
    private long countDownTime = 100000;//100秒
    private Handler countDownHandler = new Handler();
    Runnable countDownRunnable = new Runnable() {
        @Override
        public void run() {
            if (countDownTime >= 0) {
                mBinding.tvCode.setText("重新获取(" + String.valueOf(countDownTime / 1000) + ")");
                countDownTime = countDownTime - 1000;
                countDownHandler.postDelayed(this, 1000);
            } else {
                countDownClickable = true;
                countDownTime = 60000;
                mBinding.tvCode.setText("获取验证码");
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_code://获取验证码
                if (countDownClickable) {
                    getCheckCode();
                }
                break;
            case R.id.bt_registration:
                userPayPwd();
                break;
            case R.id.iv_didian:
                if (!mBinding.ivDidian.isSelected()) {
                    mBinding.ivDidian.setSelected(true);
                    mBinding.edPayment.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    mBinding.ivDidian.setSelected(false);
                    mBinding.edPayment.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }


    /**
     * 获取验证码
     */
    private void getCheckCode() {
//        String phone = mBinding.etPhone.getText().toString().trim();
        if (phone.length() < 11) {
            countDownClickable = true;
            DemoUtils.nope(mBinding.etPhone).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
        } else {
            showWaitDialog();
            mPresenter.getCode(phone);
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;

            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel successModel = (HttpSuccessModel) object;
            if (parms.length > 0) {
                int parm = parms[0];
                if (parm == 1) {
                    countDownRunnable.run();
                    countDownClickable = false;
                    Toast.makeText(MaiLiApplication.getInstance(), "验证码已发送，请注意查收！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), successModel.getInfo(), Toast.LENGTH_SHORT).show();
                    mLoginPresenter.LoginIn(phone, MD5.MD5Pwd(phone, payment));
                }
            }
        } else if (object instanceof PaymentPasswordModel) {
            PaymentPasswordModel model = (PaymentPasswordModel) object;
            MaiLiApplication.IsPayPwd = true;
            EventBus.getDefault().post(model);
            Toast.makeText(MaiLiApplication.getInstance(), "支付密码设置成功!", Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1500);
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
