package com.gsy.ml.ui.person;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivitySettingsLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.VersionModel;
import com.gsy.ml.model.person.PaymentPasswordModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.VersionPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.main.LoginActivity;
import com.gsy.ml.ui.utils.GlideCacheUtil;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.ProgressDialog;
import com.hyphenate.chat.EMClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import ml.gsy.com.library.utils.CacheUtils;
import ml.gsy.com.library.utils.UpdateDownloadUtils;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/20.
 * 设置类
 */

public class SettingsActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    public ActivitySettingsLayoutBinding mBinding;

    private VersionPresenter mVersionPresenter = new VersionPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_settings_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();

        mBinding.tvOpinion.setOnClickListener(this);
        mBinding.ilHead.tvTitle.setText("设置");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.tvLoginOut.setOnClickListener(this);
        mBinding.llyClearCache.setOnClickListener(this);
        mBinding.tvPassword.setOnClickListener(this);
        mBinding.tvMaili.setOnClickListener(this);
        mBinding.llyCheckVersion.setOnClickListener(this);
        mBinding.rlPaymentPassword.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivitySettingsLayoutBinding) vdb;
        EventBus.getDefault().register(aty);
        setCacheSize();
        if (MaiLiApplication.IsPayPwd) {//设置了
            mBinding.tvPayHint.setText("");
        } else {
            mBinding.tvPayHint.setText("未设置");
        }
        mBinding.tvVersion.setText("v" + Utils.getVersionName(aty));
    }

    private void setCacheSize() {
        String cacheSize = GlideCacheUtil.getInstance().getCacheSize(aty.getApplicationContext());
        mBinding.tvCache.setText(cacheSize);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_clear_cache:
                informationDialog = new InformationDialog(aty);
                informationDialog.setTitle("提示");
                informationDialog.setMessage("您确定要清除缓存吗？");
                informationDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                    @Override
                    public void onDialogClick(Dialog dlg, View view) {
                        GlideCacheUtil.getInstance().clearImageAllCache(aty.getApplicationContext());
                        mBinding.tvCache.setText("0B");
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
                break;
            case R.id.tv_login_out:
                informationDialog = new InformationDialog(aty);
                informationDialog.setTitle("提示");
                informationDialog.setMessage("您确定要退出登录吗？");
                informationDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                    @Override
                    public void onDialogClick(Dialog dlg, View view) {
                        CacheUtils.getInstance().saveCache(Constant.USERINFO, null);//清除用户信息 缓存
                        EMClient.getInstance().logout(true);//退出登录
                        MaiLiApplication.backToLogin(aty,
                                new Intent(aty, LoginActivity.class)
                                        .putExtra("show_phone", true));
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
                break;
            case R.id.tv_password:
                startActivity(new Intent(aty, ChangePasswordActivity.class));
                break;
            case R.id.tv_opinion:
                startActivity(new Intent(aty, OpinionActivity.class));
                break;
            case R.id.tv_maili:
                startActivity(new Intent(aty, ProductActivity.class));
                break;
            case R.id.rl_payment_password:
                startActivity(new Intent(aty, PaymentPasswordActivity.class));
                break;
            case R.id.lly_check_version:
                showWaitDialog();
                getVersion();
                break;
        }
    }

    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(PaymentPasswordModel paymentPasswordModel) {
        mBinding.tvPayHint.setText("");
    }


    private InformationDialog informationDialog;
    InformationDialog mVersionDialog;
    Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UpdateDownloadUtils.FAILURE:
                    pd.dismiss();
                    Toast.makeText(MaiLiApplication.getInstance(), "下载失败", Toast.LENGTH_SHORT).show();
                    break;
                case UpdateDownloadUtils.SUCCESS://下载 成功
                    pd.dismiss();
                    File file = (File) msg.obj;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                    startActivity(i);

                    break;
                case UpdateDownloadUtils.PROGRESS://下载 进度
                    int progress = (int) msg.obj;
                    pd.setProgressMaxValue(100);
                    pd.setProgressValue(progress);

                   /* if (oldProgress == 0) {
                        oldProgress = progress;
                    }
                    int step = progress - oldProgress;
                    oldProgress = progress;

                    if (step > 0) {
                        mDownDialog.incrementProgress(step);
                    }*/
                    break;
            }
        }
    };

    private UpdateDownloadUtils mUdl;
    private ProgressDialog pd;

    private void checkVersion(final VersionModel info) {
        if (info == null) {
            return;
        }
        final VersionModel.DataBean data = info.getData();
        final int versionCode = Utils.getVersionCode(aty);
        if (versionCode < data.getVersionNumber()) {//要更新
            mVersionDialog = new InformationDialog(aty);
            mVersionDialog.setTitle("检测到版本更新");
            mVersionDialog.setMessage(data.getUpdateContent());
            mVersionDialog.setPositiveButton("立即更新", new InformationDialog.IDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dlg, View view) {
                    mUdl = new UpdateDownloadUtils(aty.getApplicationContext(), downloadHandler);
                    try {
                        mUdl.downloadFile(data.getPackageAddress(), "蚂蚁快服_" + versionCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pd = new ProgressDialog(aty);
                    pd.setTitle("下载");
                    pd.setMessage("蚂蚁快服正在下载中。请稍后!");
                    pd.setValueTextType(ProgressDialog.SCALE_DIVIDE, 1024, "KB");
                    pd.setProgressValue(0);
                    pd.setCancelable(false);
                    pd.show();
                    dlg.dismiss();
                }
            });
            mVersionDialog.setNegativeButton("稍后再说", new InformationDialog.IDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dlg, View view) {
                    dlg.dismiss();
                }
            });
            mVersionDialog.show();
        } else {
            Toast.makeText(MaiLiApplication.getInstance(), "当前已是最新版本!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getVersion() {
        mVersionPresenter.versionUpdate();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof VersionModel) {
            VersionModel info = (VersionModel) object;
            checkVersion(info);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
