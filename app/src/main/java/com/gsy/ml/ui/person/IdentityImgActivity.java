package com.gsy.ml.ui.person;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.card_lib.IDCardScanActivity;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityIdentityImgBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.CardIdModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.FaceIdPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.liveness_lib.util.ConUtil;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.licensemanager.Manager;

import ml.gsy.com.library.utils.Utils;

public class IdentityImgActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {

    private ActivityIdentityImgBinding mBinding;
    private FaceIdPresenter mFaceIdPresenter = new FaceIdPresenter(this);
    private boolean isRegister = false;
    private int mType = 0;
    private byte[] zhen_img;
    private byte[] fan_img;


    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.ilHead.tvTitle.setText("身份证认证");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_identity_img;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityIdentityImgBinding) vdb;
        mBinding.imgZhen.setOnClickListener(this);
        mBinding.imgFan.setOnClickListener(this);
        mBinding.tvTurn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_zhen:
                mType = 0;
                if (isRegister) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    register();
                }
                break;

            case R.id.img_fan:
                mType = 1;
                if (isRegister) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    showWaitDialog("正在加载中...");
                    register();
                }
                break;
            case R.id.tv_turn://下一步
                if (mBinding.tvTurn.isSelected()) {
                    checkData();
                }
                break;
        }
    }

    private void checkData() {
        showWaitDialog("正在提交信息");
        mFaceIdPresenter.cardId(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                Utils.Bytes2Base64(zhen_img),
                Utils.Bytes2Base64(fan_img)
        );
    }

    private void register() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String uuid = ConUtil.getUUIDString(aty);
                Manager manager = new Manager(aty);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
                        aty);
                manager.registerLicenseManager(idCardLicenseManager);
                manager.takeLicenseFromNetwork(uuid);
            /*    Manager manager = new Manager(aty);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(aty);
                manager.registerLicenseManager(licenseManager);

                manager.takeLicenseFromNetwork(uuid);*/
                if (idCardLicenseManager.checkCachedLicense() > 0) {
                    //注册成功
                    mHandler.sendEmptyMessage(0);
                } else {
                    //注册失败
                    mHandler.sendEmptyMessage(1);
                }

            }
        }).start();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideWaitDialog();
            switch (msg.what) {
                case 0:
                    isRegister = true;
                    Intent intent = new Intent(aty, IDCardScanActivity.class);
                    intent.putExtra("side", mType);
                    intent.putExtra("isvertical", false);
                    startActivityForResult(intent, 1);
                    break;
                case 1:
                    Toast.makeText(MaiLiApplication.getInstance(), "注册失败!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void checkImg() {
        if (zhen_img == null || fan_img == null) {
            mBinding.tvTurn.setSelected(false);
        } else {
            mBinding.tvTurn.setSelected(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            byte[] portraitImgData = data.getByteArrayExtra("idcardImg");
            if (data.getIntExtra("side", 0) == 0) {
                zhen_img = portraitImgData;
                Bitmap img = BitmapFactory.decodeByteArray(portraitImgData, 0,
                        portraitImgData.length);
                mBinding.imgZhen.setImageBitmap(img);

            } else {
                fan_img = portraitImgData;
                Bitmap img = BitmapFactory.decodeByteArray(portraitImgData, 0,
                        portraitImgData.length);
                mBinding.imgFan.setImageBitmap(img);

            }
            checkImg();
        }
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof CardIdModel) {
            CardIdModel info = (CardIdModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), "提交成功!", Toast.LENGTH_SHORT).show();
            final Intent intent = new Intent(aty, ConfirmIdActivity.class);
            intent.putExtra("data", info);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(intent);
                    finish();
                }
            }.start();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
