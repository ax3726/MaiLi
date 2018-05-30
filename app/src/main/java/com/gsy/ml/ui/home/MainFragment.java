package com.gsy.ml.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.FragmentMainLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.NoReadModel;
import com.gsy.ml.model.home.RotateModel;
import com.gsy.ml.model.home.WorkingConditionModel;
import com.gsy.ml.model.main.AdvModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.NoReadPresenter;
import com.gsy.ml.prestener.home.WorkingConditionPresenter;
import com.gsy.ml.prestener.main.SelectAdvPresenter;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.main.MainActivity;
import com.gsy.ml.ui.person.MoreMessageActivity;
import com.gsy.ml.ui.person.OrderBillingActivity;
import com.gsy.ml.ui.person.OrderReceivingActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.utils.SpeedHelper;
import com.gsy.ml.ui.views.ChooseOccupationDialog;
import com.gsy.ml.ui.views.FullyGridLayoutManager;
import com.gsy.ml.ui.views.InformationDialog;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SynthesizerListener;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.SharedPreferencesUtils;
import ml.gsy.com.library.utils.Utils;
import ml.gsy.com.library.views.MarqueeView;
import ml.gsy.com.library.widget.flybanner.FlyBanner;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MainFragment extends BaseFragment implements View.OnClickListener, ChooseOccupationDialog.IChooseTypeListener, ILoadPVListener {

    private FragmentMainLayoutBinding mBinding;
    private WorkingConditionPresenter mPresenter = new WorkingConditionPresenter(this);
    private NoReadPresenter mNoReadPresenter = new NoReadPresenter(this);
    private SelectAdvPresenter mSelectAdvPresenter = new SelectAdvPresenter(this);
    private List<RotateModel.DataBean> mImages = new ArrayList<>();

    private int mJobPre = -1;//职位预选
    private int mAreaPre = -1;//地区预选
    private ViewPager1 view1;
    private ViewPager1 view2;
    private ViewPager1 view3;
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private List<ImageView> mCons = new ArrayList<>();//指示器
    private boolean mIsRole = false;
    private List<AdvModel.DataBean.AdvertisementListBean> mAdvList = new ArrayList<>();
    private CommonAdapter<AdvModel.DataBean.AdvertisementListBean> mAdvAdapter;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_main_layout;
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private StateModel stateModel = new StateModel();
    private Handler mHandler = new Handler();

    @Override
    protected void initData() {
        super.initData();
        mBinding = (FragmentMainLayoutBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                selectAdv();
            }
        });
        mBinding.setStateModel(stateModel);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stateModel.setEmptyState(EmptyState.NORMAL);
            }
        }, 1000);
        mBinding.img.setOnClickListener(this);
        EventBus.getDefault().register(this);
        initView();
        setUserInfo();
        getNoRead();
        setViewPager();
        setHeadStatus();
        selectAdv();
        initRefersh();

    }

    private void initRefersh() {
        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        selectAdv();
                    }
                }, 1000);
            }
        });
    }

    private void selectAdv() {
        mSelectAdvPresenter.selectAdv();//查询广告
    }

    private void setAdv() {


        mAdvAdapter = new CommonAdapter<AdvModel.DataBean.AdvertisementListBean>(aty, R.layout.item_home_adv, mAdvList) {
            @Override
            protected void convert(ViewHolder holder, final AdvModel.DataBean.AdvertisementListBean advertisementListBean, int position) {
                ImageView img_adv = holder.getView(R.id.img_adv);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) img_adv.getLayoutParams();
                if (advertisementListBean.getType() == 0) {//小项
                    // int height = position > 0&&mAdvList.get(1).getType()==1? getResources().getDimensionPixelSize(R.dimen.PX16) : 0;
                    int height = getResources().getDimensionPixelSize(R.dimen.PX16);
                    if (position == 0 || mAdvList.get(position - 1).getType() == 1 || (position > 1 && mAdvList.get(position - 2).getType() == 0)) {
                        layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.PX16), height, getResources().getDimensionPixelSize(R.dimen.PX8), 0);
                    } else {
                        layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.PX8), height, getResources().getDimensionPixelSize(R.dimen.PX16), 0);
                    }

                } else {
                    if (position > 0) {
                        layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.PX16), getResources().getDimensionPixelSize(R.dimen.PX16), getResources().getDimensionPixelSize(R.dimen.PX16), 0);
                    }
                }
                img_adv.setLayoutParams(layoutParams);
                Glide.with(MaiLiApplication.appliactionContext).load(advertisementListBean.getImgAddress()).into(img_adv);
                img_adv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(advertisementListBean.getImgUrl())) {
                            mContext.startActivity(new Intent(mContext, WebViewActivity.class)
                                    .putExtra("url", advertisementListBean.getImgUrl()));
                        }
                    }
                });
            }
        };

        FullyGridLayoutManager fullyGridLayoutManager = new FullyGridLayoutManager(aty, 2, mAdvList) {
            @Override
            public boolean canScrollVertically() {
                return false;//禁止rc的滑动
            }
        };
        fullyGridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int i = mAdvList.get(position).getType() == 1 ? 2 : 1;
                return i;
            }
        });
      /*  mBinding.rcAdv.setLayoutManager(fullyGridLayoutManager);

        mBinding.rcAdv.setAdapter(mAdvAdapter);*/

    }

    private void setViewPager() {

        view1 = new ViewPager1();
        Bundle bundle = new Bundle();
        bundle.putInt("page", 0);
        view1.setArguments(bundle);

        view2 = new ViewPager1();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("page", 1);
        view2.setArguments(bundle1);

        view3 = new ViewPager1();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("page", 2);
        view3.setArguments(bundle2);

        fragments.add(view1);
        fragments.add(view2);
        fragments.add(view3);

        mBinding.llyContainer.removeAllViews();
        for (int i = 0; i < fragments.size(); i++) {
            ImageView view = new ImageView(aty);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams((int) Utils.dip2px(aty, 6), (int) Utils.dip2px(aty, 6));
            layoutParams.setMargins((int) Utils.dip2px(aty, 5), 0, (int) Utils.dip2px(aty, 5), 0);
            view.setLayoutParams(layoutParams);
            view.setImageResource(R.drawable.selector_bgabanner_point);
            if (i == 0) {
                view.setEnabled(true);
            } else {
                view.setEnabled(false);
            }
            mCons.add(view);
            mBinding.llyContainer.addView(view);
        }
        mAdapter = new MyPagerAdapter(getFragmentManager());


        mBinding.vpViewpager.setAdapter(mAdapter);
        mBinding.vpViewpager.setOffscreenPageLimit(3);
        mBinding.vpViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateContain(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setHeadStatus() {
        Boolean role_status = (Boolean) SharedPreferencesUtils.getParam(aty, Constant.ROLESTATUE, true);//当前角色
        if (role_status) {//接单状态
            mBinding.imgRoleOff.setSelected(false);
            mBinding.ivOnOff.setVisibility(View.VISIBLE);
            setWorkStatus(true);
        } else {
            mBinding.imgRoleOff.setSelected(true);
            mBinding.ivOnOff.setVisibility(View.GONE);
            setWorkStatus(false);

        }
    }

    private void updateContain(int position) {
        for (int i = 0; i < mCons.size(); i++) {
            if (position == i) {

                mCons.get(i).setEnabled(true);
            } else {
                mCons.get(i).setEnabled(false);
            }
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);
            return obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }


    public void setUserInfo() {

    }



    private void initView() {
        mPresenter.selectRotate();


        mBinding.ivMessage.setOnClickListener(this);
        mBinding.tvAddress.setOnClickListener(this);
        mBinding.ivOnOff.setOnClickListener(this);
        mBinding.tvJiedan.setOnClickListener(this);

        mBinding.imgRoleOff.setOnClickListener(this);
        mBinding.ivOnOff.setSelected(MaiLiApplication.IsWork);

        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        setAddress(userPlace.getCity());
        /*UserInfoModel.UserInfoBean userInfo = MaiLiApplication.getInstance().getUser().getUserInfo();
        mBinding.tvName.setText(TextUtils.isEmpty(userInfo.getName()) ? "未知" : userInfo.getName());*/

    }

    public void setAddress(String city) {
        mBinding.tvAddress.setText(TextUtils.isEmpty(city) ? "正在定位" : city);
    }

    private InformationDialog mRoleDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message:
                startActivity(new Intent(getActivity(), ExerciseActivity.class));
                break;
            case R.id.img:
                Toast.makeText(MaiLiApplication.getInstance(), "正在重新定位...", Toast.LENGTH_SHORT).show();
                LocationHelper.getInstance1().startLocation(aty);
                break;
            case R.id.iv_on_off:
                showWaitDialog();
                mPresenter.selectdWork(MaiLiApplication.getInstance().getUserInfo().getPhone(), mBinding.ivOnOff.isSelected() ? "0" : "1");
                break;
            case R.id.tv_fadan://我的发单
                aty.startActivity(new Intent(aty, OrderBillingActivity.class).putExtra("type", 1));
                break;
            case R.id.tv_jiedan://我的接单
                aty.startActivity(new Intent(aty, OrderReceivingActivity.class).putExtra("type", 1));
                break;
            case R.id.img_role_off://角色切换
                showWaitDialog("正在切换角色...请稍后");
                if (mBinding.imgRoleOff.isSelected()) {//w我要接单
                    mPresenter.selectdWork(MaiLiApplication.getInstance().getUserInfo().getPhone(), "1");
                    mIsRole = true;

                } else {//我要发单
                    mPresenter.selectdWork(MaiLiApplication.getInstance().getUserInfo().getPhone(), "0");
                    mIsRole = true;
                }
                break;
        }
    }

    private void setWorkStatus(boolean is_work) {
        view1.setWorkStatus(is_work);
        view2.setWorkStatus(is_work);
        view3.setWorkStatus(is_work);
        SharedPreferencesUtils.setParam(aty, Constant.ROLESTATUE, is_work);
    }

    private void getNoRead() {
        mNoReadPresenter.getNoRead(MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    private void updateNoRead(NoReadModel info) {
        if (info == null) {
            return;
        }
        // mBinding.sendNotice.setVisibility(info.getSendCount() > 0 ? View.VISIBLE : View.GONE);
        //mBinding.acceptNotice.setVisibility(info.getAcceptCount() > 0 ? View.VISIBLE : View.GONE);
        // mBinding.messageNotice.setVisibility(info.getNoReadCount() > 0 ? View.VISIBLE : View.GONE);

        mJobPre = info.isUserJobBool() ? 1 : 0;
        mAreaPre = info.isUserPlace() ? 1 : 0;
        ((MainActivity) aty).updateMessageNoRead(info.getNoReadCount());

    }

    private void stopRefersh() {
        if (mBinding != null) {
            mBinding.mrlBody.finishRefresh();
        }
    }

    @Override
    public void chooseItem(int position, String item) {
        DemoUtils.TypeToActivity(aty, position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateTxt(UpdateNotice message) {
        getNoRead();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        stopRefersh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel model = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(model.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), model.getInfo(), Toast.LENGTH_LONG).show();
        } else if (object instanceof WorkingConditionModel) {
            WorkingConditionModel model = (WorkingConditionModel) object;
            if (!mIsRole) {
                if (mBinding.ivOnOff.isSelected()) {
                    mBinding.ivOnOff.setSelected(false);
                    StartSpeed("您已收工!");

                } else {
                    mBinding.ivOnOff.setSelected(true);
                    StartSpeed("您已开工!");
                    ToJobPre();
                }

            } else {
                if (mBinding.imgRoleOff.isSelected()) {//w我要接单
                    mBinding.imgRoleOff.setSelected(false);
                    mBinding.ivOnOff.setVisibility(View.VISIBLE);
                    mBinding.ivOnOff.setSelected(true);
                    setWorkStatus(true);
                    hideWaitDialog();
                    mRoleDialog = new InformationDialog(aty);
                    mRoleDialog.setTitle("提示");
                    mRoleDialog.setMessage("角色切换成功,可以开始接单了!");
                    mRoleDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                        }
                    });
                    mRoleDialog.show();

                } else {//我要发单
                    mBinding.imgRoleOff.setSelected(true);
                    mBinding.ivOnOff.setVisibility(View.GONE);
                    mBinding.ivOnOff.setSelected(false);
                    setWorkStatus(false);
                    mRoleDialog = new InformationDialog(aty);
                    mRoleDialog.setTitle("提示");
                    mRoleDialog.setMessage("角色切换成功,老板可以开始发单了!");
                    mRoleDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                        }
                    });
                    mRoleDialog.show();
                }
            }

        } else if (object instanceof RotateModel) {
            RotateModel model = (RotateModel) object;
            mImages.clear();

            if (model.getData() != null) {
                List<String> lists = new ArrayList<>();
                mImages.addAll(model.getData());
                for (int i = 0; i < mImages.size(); i++) {
                    lists.add(model.getData().get(i).getRotateImg());
                }
                mBinding.fbRoll.setImagesUrl(lists);
                mBinding.fbRoll.setOnItemClickListener(new FlyBanner.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (!TextUtils.isEmpty(mImages.get(position).getRotateImgUrl())) {
                            startActivity(new Intent(aty, WebViewActivity.class)
                                    .putExtra("url", mImages.get(position).getRotateImgUrl()));
                        }
                    }
                });
            }
        } else if (object instanceof NoReadModel) {
            NoReadModel info = (NoReadModel) object;
            updateNoRead(info);
        } else if (object instanceof AdvModel) {
            final AdvModel info = (AdvModel) object;
            mAdvList.clear();
            if (info.getData() != null && info.getData().getAdvertisList() != null && info.getData().getAdvertisList().size() > 0) {
                MaiLiApplication.getInstance().mMessageAdvList = info.getData().getAdvertisList();
            }

            if (info.getData() != null && info.getData().getAdvertisementList() != null && info.getData().getAdvertisementList().size() > 0) {
                mAdvList.addAll(info.getData().getAdvertisementList());
            }
            setAdv();
            if (info.getData() != null && info.getData().getAdvertisementMsgList() != null && info.getData().getAdvertisementMsgList().size() > 0) {
                mBinding.llyNotice.setVisibility(View.VISIBLE);


                //设置集合
                List<String> items = new ArrayList<>();

                for (AdvModel.DataBean.AdvertisementMsgListBean ad : info.getData().getAdvertisementMsgList()) {
                    items.add(ad.getMessage());
                }

                mBinding.mvNotice.startWithList(items);

                mBinding.mvNotice.setOnItemClickListener(new MarqueeView.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, TextView textView) {
                        if (!TextUtils.isEmpty(info.getData().getAdvertisementMsgList().get(position).getMessageUrl())) {
                            startActivity(new Intent(aty, WebViewActivity.class)
                                    .putExtra("url", info.getData().getAdvertisementMsgList().get(position).getMessageUrl()));
                        }
                    }
                });
            } else {
                mBinding.llyNotice.setVisibility(View.GONE);
            }

        }

        mIsRole = false;
    }

    private void ToJobPre() {
        if (mAreaPre == 0 || mJobPre == 0) {
            InformationDialog informationDialog = new InformationDialog(aty);
            informationDialog.setTitle("温馨提示");
            informationDialog.setMessage("只有设置了预选职位和预选区域才能收到抢单信息哦！");
            informationDialog.setPositiveButton("去设置", new InformationDialog.IDialogClickListener() {
                @Override
                public void onDialogClick(Dialog dlg, View view) {
                    startActivity(new Intent(aty, MoreMessageActivity.class).putExtra("type", "1"));
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

    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    private void StartSpeed(String content) {
        SpeedHelper.getInstance().startSpeed(content, mSynListener);
    }

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        //会话结束回调接口，没有错误时，error为null
        public void onCompleted(SpeechError error) {
        }

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        //开始播放
        public void onSpeakBegin() {

        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
        }

        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
}
