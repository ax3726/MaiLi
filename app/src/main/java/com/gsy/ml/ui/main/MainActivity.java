package com.gsy.ml.ui.main;


import android.app.Dialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityMainBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.NewAcitivityModel;
import com.gsy.ml.model.main.OrderInfoModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.main.VersionModel;
import com.gsy.ml.model.person.VoucherModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.CardPresenter;
import com.gsy.ml.prestener.main.NewActivityPresenter;
import com.gsy.ml.prestener.main.UpdateLocationPresenter;
import com.gsy.ml.prestener.main.VersionPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.LiveJobService;
import com.gsy.ml.ui.common.LocalService;
import com.gsy.ml.ui.common.RemoteService;
import com.gsy.ml.ui.home.MainFragment1;
import com.gsy.ml.ui.message.MessageFragment;
import com.gsy.ml.ui.person.PersonFragment;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.DoubleClickExitHelper;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.views.InformationDialog;
import com.gsy.ml.ui.views.NewCardDialog;
import com.gsy.ml.ui.views.NewMessageDialog;
import com.gsy.ml.ui.views.OrdersDialog;
import com.gsy.ml.ui.views.ProgressDialog;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.UpdateDownloadUtils;
import ml.gsy.com.library.utils.Utils;

public class MainActivity extends BaseActivity implements LocationHelper.ILocationListener, ILoadPVListener, GeocodeSearch.OnGeocodeSearchListener, MessageFragment.IupdateListener {

    public ActivityMainBinding mBinding;
    private FragmentManager mFm;
    private MainFragment1 mMainFragment;
    //private UserCardFragment mUserCardFragment;
    private MessageFragment mMessageFragment;
    private PersonFragment mPersonFragment;
    private FragmentTransaction mTransaction;
    private List<Fragment> mFragments = new ArrayList<>();
    private UpdateLocationPresenter mUpdateLocationPresenter = new UpdateLocationPresenter(this);
    private NewActivityPresenter mNewActivityPresenter = new NewActivityPresenter(this);
    private CardPresenter mCardPresenter = new CardPresenter(this);
    private VersionPresenter mVersionPresenter = new VersionPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    updateNoRead(EMClient.getInstance().chatManager().getUnreadMessageCount());
                    break;
            }
        }
    };

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityMainBinding) vdb;

        EventBus.getDefault().register(aty);
        mDoubleClickExit = new DoubleClickExitHelper(this);
        LocationHelper.getInstance1().setILocationListener(this);
        LocationHelper.getInstance1().startLocation(aty);
        mBinding.rgButtom.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_main:
                        if (currentFragmentPosition != 0) {
                            changeFragment(0);
                        }
                        break;
                    case R.id.rb_user_card:
                        if (currentFragmentPosition != 1) {
                            changeFragment(1);
                        }
                        break;
                    case R.id.rb_person:
                        if (currentFragmentPosition != 2) {
                            changeFragment(2);
                        }
                        break;
                }
            }
        });
        initFragment();
        initView();
        if (MaiLiApplication.mOrderInfoModel != null) {
            long cur = System.currentTimeMillis();
            if (MaiLiApplication.mOrderInfoModel.getData() != null) {
                OrderInfoModel.DataBean data = MaiLiApplication.mOrderInfoModel.getData();
                if ((cur - data.getSendTime() < 1000 * 60 * 5)) {//大于等于5分钟后    抢单不显示
                    OrdersDialog dialog = new OrdersDialog(MaiLiApplication.mList.get(MaiLiApplication.mList.size() - 1), MaiLiApplication.mOrderInfoModel);
                    dialog.show();
                }
            }
            MaiLiApplication.mOrderInfoModel = null;
        }
        getVersion();
        getNewCard();//检测是否有新的卡卷

        if (MaiLiApplication.DoingWorkType1) {
            Intent intent = new Intent("SocaketReceiver");
            sendBroadcast(intent);
        }
        checkLocation();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//开启双进程守护
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
            JobInfo jobInfo = new JobInfo.Builder(1, new ComponentName(getPackageName(), LiveJobService.class.getName()))
                    .setPeriodic(2000)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .build();
            jobScheduler.schedule(jobInfo);
        } else {

        }
        openJobService();
        getNewActivity();
        updateNoRead(EMClient.getInstance().chatManager().getUnreadMessageCount());
        EMClient.getInstance().chatManager().addMessageListener(new EMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> list) {
                mHandler.sendEmptyMessage(1);

            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        });

    }

    public void updateMessageNoRead(int count) {
        if (mMessageFragment != null) {
            mMessageFragment.updateMessageNoReadCount(count);
        }
    }

    private void getNewActivity() {
        mNewActivityPresenter.getNewActivity(MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    private void openJobService() {
        startService(new Intent(this, LocalService.class));
        startService(new Intent(this, RemoteService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 检测权限是否被禁止
     */
    private void checkLocation() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            //  boolean ok=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);//2.通过网络定位，对定位精度度不高或省点情况可考
            if (lm.getProvider(LocationManager.NETWORK_PROVIDER) == null) {
                //无法定位：1、提示用户打开定位服务；2、跳转到设置界面
                InformationDialog informationDialog = new InformationDialog(aty);
                informationDialog.setTitle("温馨提示");
                informationDialog.setMessage("无法定位，请打开定位服务");
                informationDialog.setPositiveButton("去设置", new InformationDialog.IDialogClickListener() {
                    @Override
                    public void onDialogClick(Dialog dlg, View view) {
                        DemoUtils.getAppDetailSettingIntent(aty);
                        dlg.dismiss();
                    }
                });
                informationDialog.setNegativeButton("稍后再说", new InformationDialog.IDialogClickListener() {
                    @Override
                    public void onDialogClick(Dialog dlg, View view) {
                        dlg.dismiss();
                    }
                });
                informationDialog.show();
            }
        }

    }

    private void getVersion() {
        mVersionPresenter.versionUpdate();
    }

    private void getNewCard() {
        mCardPresenter.getNewCard(MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    private void initFragment() {
        mMainFragment = new MainFragment1();
        // mUserCardFragment = new UserCardFragment();
        mMessageFragment = new MessageFragment();
        mMessageFragment.setIupdateListener(this);

        mPersonFragment = new PersonFragment();
        mFragments.add(mMainFragment);
        // mFragments.add(mUserCardFragment);

        mFragments.add(mMessageFragment);
        mFragments.add(mPersonFragment);
        mFm = getSupportFragmentManager();
        mTransaction = mFm.beginTransaction();
        mTransaction.add(R.id.fly_contain, mMainFragment);
        mTransaction.show(mFragments.get(0));
        mTransaction.commitAllowingStateLoss();
    }

    private int currentFragmentPosition = 0;

    public void changeFragment(final int position) {
        mFm = getSupportFragmentManager();
        mTransaction = mFm.beginTransaction();
        if (position != currentFragmentPosition) {
            mTransaction.hide(mFragments.get(currentFragmentPosition));
            if (!mFragments.get(position).isAdded()) {
                mTransaction.add(R.id.fly_contain, mFragments.get(position));
            }
            mTransaction.show(mFragments.get(position));
            mTransaction.commitAllowingStateLoss();
        }
        currentFragmentPosition = position;
    }

    private void initView() {

    }

    @Override
    public void initActionBar() {
        super.initActionBar();
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    private int mLocationCount = 0;

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location == null || location.getErrorCode() != 0) {
            mLocationCount++;
            if (mLocationCount <= 3) {
                LocationHelper.getInstance1().startLocation(aty);
            }
            return;
        }
        MaiLiApplication.mAMapLocation = location;
        String province = location.getProvince();
        String city = location.getCity();
        String district = location.getDistrict();
        String address = location.getAddress();
        mMainFragment.setAddress(city);
        updateLocation(province, city, district, address, location.getLongitude() + "", location.getLatitude() + "");
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1001) {//定位返回

            LocationHelper.getInstance1().startLocation(aty);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void updateLocation(String province, String city, String district, String address, String Longitude, String Latitude) {
        if (!TextUtils.isEmpty(province)) {
            boolean is_first = false;
            if (TextUtils.isEmpty(MaiLiApplication.getInstance().getUserPlace().getCity()) && mMainFragment != null) {//第一次定位
                is_first = true;
            }
            mUpdateLocationPresenter.UpdateLocation(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                    province, city, district, Longitude, Latitude, address);
            if (is_first) {
                mMainFragment.inquireOrder();//刷新订单列表
            }
        } else {
            LocationHelper.getInstance().startLocation(aty);
        }
    }

    Handler downloadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UpdateDownloadUtils.FAILURE:
                    pd.dismiss();
                    Toast.makeText(aty, "下载失败", Toast.LENGTH_SHORT).show();
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
    private InformationDialog informationDialog;
    private UpdateDownloadUtils mUdl;
    private ProgressDialog pd;

    private void checkVersion(final VersionModel info) {
        if (info == null) {
            return;
        }
        final VersionModel.DataBean data = info.getData();
        final int versionCode = Utils.getVersionCode(aty);
        if (versionCode < data.getVersionNumber()) {//要更新
            informationDialog = new InformationDialog(aty);
            informationDialog.setTitle("版本更新");
            informationDialog.setMessage(data.getUpdateContent());
            informationDialog.setPositiveButton("立即更新", new InformationDialog.IDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dlg, View view) {
                    mUdl = new UpdateDownloadUtils(getApplicationContext(), downloadHandler);
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
            informationDialog.setNegativeButton("稍后再说", new InformationDialog.IDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dlg, View view) {
                    dlg.dismiss();
                }
            });
            informationDialog.show();
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
            mMainFragment.setAddress(userPlace.getCity());

        } else if (object instanceof VersionModel) {
            VersionModel info = (VersionModel) object;
            checkVersion(info);
        } else if (object instanceof VoucherModel) {
            VoucherModel info = (VoucherModel) object;
            if (info.getData() != null && info.getData().size() > 0) {//有新的卡卷
                new NewCardDialog(aty).show();
            }
        } else if (object instanceof NewAcitivityModel) {
            NewAcitivityModel info = (NewAcitivityModel) object;
            if (info.getData() != null && info.getData().size() > 0) {//有新的活动
                NewAcitivityModel.DataBean dataBean = info.getData().get(0);
                new NewMessageDialog(aty, dataBean.getEventDetails(), dataBean.getActivityUrl()).show();
            }
        }
    }


    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        //解析result获取坐标信息
        if (geocodeResult.getGeocodeAddressList().size() > 0) {

            GeocodeAddress geocodeAddress = geocodeResult.getGeocodeAddressList().get(0);
            String province = geocodeAddress.getProvince();
            String city = geocodeAddress.getCity();
            String dis = geocodeAddress.getDistrict();
            String formatAddress = geocodeAddress.getFormatAddress();
            LatLonPoint latLonPoint = geocodeResult.getGeocodeAddressList().get(0).getLatLonPoint();
            updateLocation(province, city, dis, formatAddress, latLonPoint.getLongitude() + "", latLonPoint.getLatitude() + "");

        }
    }

    private DoubleClickExitHelper mDoubleClickExit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return mDoubleClickExit.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateUser(UserInfoModel serInfoModel) {

        if (mPersonFragment != null) {
            mPersonFragment.initUser();
        }
       /* if (mUserCardFragment != null) {
            mUserCardFragment.initUser();
        }*/

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMessage(String message) {
        if ("更新消息".equals(message)) {
            updateNoRead(EMClient.getInstance().chatManager().getUnreadMessageCount());
        }
    }

    /**
     * 更新消息未读
     *
     * @param count
     */
    public void updateNoRead(int count) {
        if (mBinding != null) {
            if (count > 0) {
                mBinding.tvUnreadMsgNumber.setVisibility(View.VISIBLE);
                mBinding.tvUnreadMsgNumber.setText(count > 99 ? "99" : String.valueOf(count));
            } else {
                mBinding.tvUnreadMsgNumber.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }


    @Override
    public void updateMessage(int count) {
        updateNoRead(count);
    }
}
