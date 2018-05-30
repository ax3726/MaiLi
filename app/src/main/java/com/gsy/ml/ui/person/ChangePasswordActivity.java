package com.gsy.ml.ui.person;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityChangepasswordLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.PasswoedModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.ChangePasswordPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.MD5;
import com.gsy.ml.ui.utils.SoftKeyboardStateHelper;

import ml.gsy.com.library.utils.CacheUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/25.
 * 修改密码类
 */

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityChangepasswordLayoutBinding mBinding;
    private ChangePasswordPresenter pPresenter = new ChangePasswordPresenter(this);
    private UserInfoModel model;

    @Override
    public int getLayoutId() {
        return R.layout.activity_changepassword_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("修改密码");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.imgRegister.setAlpha(0.1f);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityChangepasswordLayoutBinding)   vdb;
        model = MaiLiApplication.getInstance().getUser();

        mBinding.btChangepassword.setOnClickListener(this);
//        mBinding.ivConcea.setOnClickListener(this);
        mBinding.flyBody.setOnClickListener(this);

        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(mBinding.flyBody);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {//打开
                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(mBinding.llyBody, "translationY", 0, -(Utils.dip2px(aty, 140)));
                circlePtAnim.setDuration(400);


                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());
                animSet.play(circlePtAnim);

                animSet.start();

            }

            @Override
            public void onSoftKeyboardClosed() {//关闭

                ObjectAnimator circlePtAnim = ObjectAnimator.ofFloat(mBinding.llyBody, "translationY", -(Utils.dip2px(aty, 140)), 0);
                circlePtAnim.setDuration(400);

                AnimatorSet animSet = new AnimatorSet();

                animSet.setInterpolator(new LinearInterpolator());

                animSet.play(circlePtAnim);

                animSet.start();
            }
        });

    }

    private String mNewPwd = "";//新密码

    public void checkData() {
        String originalPwd = mBinding.etOriginalPaw.getText().toString().trim();   //旧密码
        String newPwd = mBinding.etNewPwd.getText().toString().trim();              //新密码
        String tryagainPwd = mBinding.etTryagainPaw.getText().toString().trim();    //确认新密码
        if (TextUtils.isEmpty(originalPwd)) {
            DemoUtils.nope(mBinding.etOriginalPaw).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入原来密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!model.getPwd().equals(MD5.MD5Pwd(model.getUserInfo().getPhone(), originalPwd))) {
            DemoUtils.nope(mBinding.etOriginalPaw).start();
            Toast.makeText(MaiLiApplication.getInstance(), "原密码输入错误！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(newPwd)) {
            DemoUtils.nope(mBinding.etNewPwd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入新的密码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPwd.length() < 6 || newPwd.length() > 16) {
            DemoUtils.nope(mBinding.etNewPwd).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请输入6-16位密码，由中英文组成!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tryagainPwd)) {
            DemoUtils.nope(mBinding.etTryagainPaw).start();
            Toast.makeText(MaiLiApplication.getInstance(), "请确认新的密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!newPwd.equals(tryagainPwd)) {
            DemoUtils.nope(mBinding.etTryagainPaw).start();
            Toast.makeText(MaiLiApplication.getInstance(), "两次密码不相同", Toast.LENGTH_SHORT).show();
            return;
        }
        if (originalPwd.equals(newPwd)) {

            Toast.makeText(MaiLiApplication.getInstance(), "原密码和新密码相同", Toast.LENGTH_SHORT).show();
            return;
        }

        showWaitDialog();
        mNewPwd = MD5.MD5Pwd(model.getUserInfo().getPhone(), newPwd);
        pPresenter.AmendPwd(model.getUserInfo().getPhone(), model.getPwd(), mNewPwd);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.bt_changepassword:
                checkData();
                break;
//            case R.id.iv_concea://
//                if (!mBinding.ivConcea.isSelected()) {
//                    mBinding.ivConcea.setSelected(true);
//                    mBinding.etNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//                } else {
//                    mBinding.ivConcea.setSelected(false);
//                    mBinding.etNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());//设置密码为不可见。
//                }
//                break;
            case R.id.fly_body:
                Utils.closeInputPad(aty);
                break;
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof PasswoedModel) {
            PasswoedModel model = (PasswoedModel) object;
            MaiLiApplication.getInstance().getUser().setPwd(mNewPwd);
            this.model.setPwd(mNewPwd);
            CacheUtils.getInstance().saveCache(Constant.USERINFO, this.model);//重新缓存 新的用户信息
            Toast.makeText(MaiLiApplication.getInstance(), model.getInfo(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
