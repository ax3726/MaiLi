package com.gsy.ml.ui.person;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityFaceBinding;
import com.gsy.ml.model.EventMessage.MessageEvent;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.person.CardIdModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.FaceIdPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.liveness_lib.LivenessActivity;
import com.liveness_lib.bean.FaceResultBean;
import com.liveness_lib.util.ConUtil;
import com.megvii.licensemanager.Manager;
import com.megvii.livenessdetection.LivenessLicenseManager;

import org.greenrobot.eventbus.EventBus;

import ml.gsy.com.library.utils.Utils;

public class FaceActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {

    private ActivityFaceBinding mBinding;
    private boolean isRegister = false;
    private FaceResultBean mFaceResultBean;
    private FaceIdPresenter mFaceIdPresenter = new FaceIdPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_face;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.inHead.tvTitle.setText("人脸识别认证");
    }

    private CardIdModel mCardIdModel = null;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityFaceBinding) vdb;
        mBinding.tvTurn.setOnClickListener(this);
        mBinding.tvTurn.setSelected(true);
        mCardIdModel = (CardIdModel) getIntent().getSerializableExtra("data");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_turn:
                String trun = mBinding.tvTurn.getText().toString().trim();
                if (!trun.equals("完成并提交信息")) {//
                    if (!isRegister) {
                        showWaitDialog("正在加载中...");
                        register();
                    } else {
                        mHandler.sendEmptyMessage(0);
                    }
                } else {
                    CheckData();
                }
                break;
        }
    }

    /**
     * 提交信息
     */
    private void CheckData() {
        if (mFaceResultBean == null || mCardIdModel == null) {
            Toast.makeText(MaiLiApplication.getInstance(), "信息有误!", Toast.LENGTH_SHORT).show();
            return;
        }
        showWaitDialog("正在验证中...");
        mFaceIdPresenter.cardId1(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                mCardIdModel.getIdNum(),
                mCardIdModel.getName(),
                Utils.Bytes2Base64(mFaceResultBean.getImageBestData()),
                Utils.Bytes2Base64(mFaceResultBean.getImageAction1()),
                Utils.Bytes2Base64(mFaceResultBean.getImageAction2()),
                Utils.Bytes2Base64(mFaceResultBean.getImageAction3()),
                mFaceResultBean.getDelta()
        );
    }


    private void register() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                String uuid = ConUtil.getUUIDString(aty);
                Manager manager = new Manager(aty);
                LivenessLicenseManager licenseManager = new LivenessLicenseManager(aty);
                manager.registerLicenseManager(licenseManager);

                manager.takeLicenseFromNetwork(uuid);
                if (licenseManager.checkCachedLicense() > 0) {
                    isRegister = true;
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
                    Intent intent = new Intent(aty, LivenessActivity.class);
                    startActivityForResult(intent, 1);
                    break;
                case 1:
                    Toast.makeText(MaiLiApplication.getInstance(), "注册失败!", Toast.LENGTH_SHORT).show();
                    break;
            }


        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            FaceResultBean data_result = (FaceResultBean) data.getSerializableExtra("faceResultBean");
            if (data_result != null) {

                int resID = data_result.getResultcode();
                if (resID == R.string.verify_success) {
                    doPlay(R.raw.meglive_success);
                } else if (resID == R.string.liveness_detection_failed_not_video) {
                    doPlay(R.raw.meglive_failed);
                } else if (resID == R.string.liveness_detection_failed_timeout) {
                    doPlay(R.raw.meglive_failed);
                } else if (resID == R.string.liveness_detection_failed) {
                    doPlay(R.raw.meglive_failed);
                } else {
                    doPlay(R.raw.meglive_failed);
                }
                boolean isSuccess = data_result.getResult().equals(
                        getResources().getString(R.string.verify_success));

                if (!isSuccess) {//失败
                    mBinding.llyHint.setVisibility(View.VISIBLE);
                    mBinding.imgHint.setImageResource(R.drawable.loser_icon);
                    mBinding.tvFailLint.setVisibility(View.VISIBLE);
                    mBinding.tvFailLint.setText("验证失败");
                    mBinding.tvTurn.setText("重新识别");
                    mBinding.tvSaoHint.setText("可以通过以下方式提高");
                } else {
                    mFaceResultBean = data_result;
                    mBinding.imgHint.setImageResource(R.drawable.face_ok_icon);
                    mBinding.llyHint.setVisibility(View.INVISIBLE);
                    mBinding.tvFailLint.setVisibility(View.VISIBLE);
                    mBinding.tvFailLint.setText("验证成功");
                    mBinding.tvTurn.setText("完成并提交信息");
                }
            }

        }
    }

    private MediaPlayer mMediaPlayer = null;

    private void doPlay(int rawId) {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        mMediaPlayer.reset();
        try {
            AssetFileDescriptor localAssetFileDescriptor = getResources()
                    .openRawResourceFd(rawId);
            mMediaPlayer.setDataSource(
                    localAssetFileDescriptor.getFileDescriptor(),
                    localAssetFileDescriptor.getStartOffset(),
                    localAssetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception localIOException) {
            localIOException.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
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
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            EventBus.getDefault().post(new MessageEvent("认证更新"));
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


