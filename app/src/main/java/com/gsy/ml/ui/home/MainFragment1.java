package com.gsy.ml.ui.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.FragmentMainLayoutBinding;
import com.gsy.ml.model.EventMessage.UpdateNotice;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.home.NoReadModel;
import com.gsy.ml.model.home.ReceivingModel;
import com.gsy.ml.model.home.RotateModel;
import com.gsy.ml.model.home.WorkingConditionModel;
import com.gsy.ml.model.main.AdvModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.home.NoReadPresenter;
import com.gsy.ml.prestener.home.ReceivingPresenter;
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
import com.gsy.ml.ui.utils.ProvinceAreaHelper;
import com.gsy.ml.ui.utils.SpeedHelper;
import com.gsy.ml.ui.views.ChooseOccupationDialog;
import com.gsy.ml.ui.views.ChoosePopuWindow;
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

import ml.gsy.com.library.adapters.abslistview.CommonAdapter;
import ml.gsy.com.library.adapters.abslistview.ViewHolder;
import ml.gsy.com.library.utils.SharedPreferencesUtils;
import ml.gsy.com.library.utils.Utils;
import ml.gsy.com.library.widget.flybanner.FlyBanner;
import ml.gsy.com.library.widget.slide.ISlideScrollListener;
import ml.gsy.com.library.widget.slide.SlideState;

/**
 * Created by Administrator on 2017/4/18.
 */

public class MainFragment1 extends BaseFragment implements View.OnClickListener, ChooseOccupationDialog.IChooseTypeListener, ILoadPVListener, ChoosePopuWindow.IChooseListener {

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
    private ViewPager1 view4;
    private List<Fragment> fragments = new ArrayList<>();
    private MyPagerAdapter mAdapter;
    private List<ImageView> mCons = new ArrayList<>();//指示器
    private boolean mIsRole = false;
    private CommonAdapter<ReceivingModel.DataBean> mOrderAdapter;
    private List<ReceivingModel.DataBean> mList = new ArrayList<>();
    private ReceivingPresenter presenter = new ReceivingPresenter(this);
    private ChoosePopuWindow mSortPop;//排序
    private ChoosePopuWindow mTypeOrderPop;//工种筛选
    private ChoosePopuWindow mAreaPop;//地区筛选

    private int mPage = 1;
    private int mPageSize = 12;
    private int mSortPosition = 0;//排序下标
    private int mTypeOrder = 0;//默认
    private String mArea = "区域不限";//地区
    private StateModel stateModel1 = new StateModel();

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
        getNoRead();
        setViewPager();
        setHeadStatus();
        selectAdv();
        initOrders();
        initOrderListener();

    }

    private void initOrderListener() {
        mBinding.ilHead.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBinding.ssvContent.scrolltoSlide();
                mAreaPop.setCurtxt(mBinding.ilHead.tvCity.getText().toString().trim());
                mAreaPop.showPopupWindow(mBinding.ilHead.llyHead);
            }
        });
        mBinding.ilHead.tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBinding.ssvContent.scrolltoSlide();
                mSortPop.setCurtxt(mBinding.ilHead.tvSort.getText().toString().trim());
                mSortPop.showPopupWindow(mBinding.ilHead.llyHead);
            }
        });
        mBinding.ilHead.tvOrderType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.ssvContent.scrolltoSlide();
                mTypeOrderPop.setCurtxt(mBinding.ilHead.tvOrderType.getText().toString().trim());
                mTypeOrderPop.showPopupWindow(mBinding.ilHead.llyHead);
            }
        });


        mBinding.ilHead1.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAreaPop.setCurtxt(mBinding.ilHead1.tvCity.getText().toString().trim());
                mAreaPop.showPopupWindow(mBinding.ilHead1.llyHead);
            }
        });
        mBinding.ilHead1.tvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSortPop.setCurtxt(mBinding.ilHead1.tvSort.getText().toString().trim());
                mSortPop.showPopupWindow(mBinding.ilHead1.llyHead);
            }
        });
        mBinding.ilHead1.tvOrderType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTypeOrderPop.setCurtxt(mBinding.ilHead1.tvOrderType.getText().toString().trim());
                mTypeOrderPop.showPopupWindow(mBinding.ilHead1.llyHead);
            }
        });
        mBinding.rlySearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(aty, OrderSearchActivity.class));
            }
        });
        mBinding.ilHead.imgRefersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateModel1.setEmptyState(EmptyState.PROGRESS);
                mPage = 1;
                inquireOrder();

            }
        });
        mBinding.ilHead1.imgRefersh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateModel1.setEmptyState(EmptyState.PROGRESS);
                mPage = 1;
                inquireOrder();
                mBinding.ssvContent.scrolltoSlide();
            }
        });
    }

    private void  selectAdv() {
        mSelectAdvPresenter.selectAdv();//查询广告
    }

    private void initOrders() {
        mBinding.ilHead.tvCity.setText(mArea);
        mBinding.ilHead1.tvCity.setText(mArea);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBinding.ilHead1.llyHead.setElevation(15f);

        }
        mBinding.ilHead1.llyHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        stateModel1.setEmptyState(EmptyState.PROGRESS);
        stateModel1.setExpandRes("还没有发布信息呢!", R.drawable.no_data1_icon);
        mBinding.setStateModel1(stateModel1);
        stateModel1.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel1.setEmptyState(EmptyState.PROGRESS);
                inquireOrder();
            }
        });
        mBinding.ssvContent.setHeadView(mBinding.llySsvHead);//设置悬浮头部上面的VIEW   目的是为了计算悬浮View的Y轴位置
        mBinding.ssvContent.setSlideView(mBinding.ilHead1.llyHead);//设置根布局替换悬浮的VIEW
        mBinding.ssvContent.setISlideScrollListener(new ISlideScrollListener() {
            @Override
            public void onScrollChange(int Y) {
                Log.e("msg", "y:" + Y);
            }

            @Override
            public void onScrollState(int state) {
                Log.e("msg", "state:" + state);
                if (state == SlideState.SHOW) {//
                    if (mBinding.rlySearch.getVisibility() == View.GONE) {
                        mBinding.rlySearch.setVisibility(View.VISIBLE);
                        Animation translateAnimation = new TranslateAnimation(0, 0, getResources().getDimensionPixelSize(R.dimen.PX164), 0);
                        translateAnimation.setDuration(500);
                        mBinding.rlySearch.startAnimation(translateAnimation);
                    }
                } else {
                    if (mBinding.rlySearch.getVisibility() == View.VISIBLE) {
                        mBinding.rlySearch.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onScrollScale(float alpha) {
                Log.e("msg", "alpha:" + alpha);
            }
        });

        mSortPop = new ChoosePopuWindow(aty);
        List<String> data = new ArrayList<>();
        data.add("推荐排序");
        data.add("离我最近");
        data.add("最新发布");
        data.add("工资最高");
        mSortPop.setData(data, 0);
        mSortPop.setIChooseListener(this);

        mTypeOrderPop = new ChoosePopuWindow(aty);
        List<String> data1 = new ArrayList<>();
        for (int i = 0; i <= 41; i++) {
            if (i != 13) {
                data1.add(i + "");
            }
        }
        mTypeOrderPop.setData(data1, 1);
        mTypeOrderPop.setIChooseListener(this);

        mAreaPop = new ChoosePopuWindow(aty);
        ProvinceAreaHelper mAreaHelper = new ProvinceAreaHelper(aty);
        mAreaHelper.initProvinceData();
        String[] strings = mAreaHelper.updateAreas(MaiLiApplication.getInstance().getUserPlace().getCity());
        List<String> data2 = new ArrayList<>();
        data2.add("区域不限");
        if (strings != null) {
            for (String s : strings) {
                data2.add(s);
            }
        }
        mAreaPop.setData(data2, 2);
        mAreaPop.setIChooseListener(this);
        mOrderAdapter = new CommonAdapter<ReceivingModel.DataBean>(aty, R.layout.item_receiving_message, mList) {
            @Override
            protected void convert(ViewHolder holder, final ReceivingModel.DataBean dataBean, int position) {
                LinearLayout lly_body = holder.getView(R.id.lly_body);
                TextView tv_title = holder.getView(R.id.tv_title);  //名字
                TextView tv_location = holder.getView(R.id.tv_location);  //地点
                TextView tv_grade = holder.getView(R.id.tv_grade);     //兼职等级
                TextView tv_money = holder.getView(R.id.tv_money);    // 金钱
                TextView tv_distance = holder.getView(R.id.tv_distance);    // 金钱
                TextView tv_time = holder.getView(R.id.tv_time);    // 时间
                ImageView img_type = holder.getView(R.id.img_type);
                ImageView img_yuyue = holder.getView(R.id.img_yuyue);
                img_type.setImageResource(DemoUtils.TypeToImage(Integer.valueOf(dataBean.getWorkType())));
                tv_title.getPaint().setFakeBoldText(true);//字体加粗
                if (DemoUtils.TypeToNoAddress(Integer.valueOf(dataBean.getWorkType()))) {
                    tv_distance.setText("");
                    tv_title.setText(DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType()))+"\t" + DemoUtils.TypeToContent2(Integer.valueOf(dataBean.getWorkType()), dataBean.getWorkContent()));
                } else {
                    tv_title.setText(dataBean.getStartPlace() + DemoUtils.TypeToOccupation(Integer.valueOf(dataBean.getWorkType())));
                    tv_distance.setText(DemoUtils.countDistance1(dataBean.getJuli()));
                }
                tv_location.setText(dataBean.getStartArea());
                tv_money.setText(dataBean.getWorkCost() + "");
                tv_grade.setText(dataBean.getWorkLevel() + "兼职");
                tv_time.setText(Utils.getTimeStyle2(dataBean.getSendTime()));


                img_yuyue.setVisibility(dataBean.getReservationTime() > 0 ? View.VISIBLE : View.GONE);


                lly_body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("1".equals(dataBean.getWorkType())) {//同城配送
                            startActivity(new Intent(aty, PartTimeJobActivity.class)
                                    .putExtra("order", dataBean.getOrder())
                                    .putExtra("isShowAdd", true)
                                    .putExtra("isShowPhone", false));
                        } else {
                            startActivity(new Intent(aty, PartTimeActivity.class)
                                    .putExtra("order", dataBean.getOrder())
                            );
                        }
                    }
                });
            }
        };


        mBinding.lvAdv.setAdapter(mOrderAdapter);
        mBinding.mrlBody.setAutoLoading(true);
        mBinding.mrlBody.setLoadMore(true);


        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        inquireOrder();
                    }
                }, 400);
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                mPage++;
                inquireOrder();
            }
        });
        mPage = 1;
        inquireOrder();
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

        view4 = new ViewPager1();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("page",3);
        view4.setArguments(bundle3);

        fragments.add(view1);
        fragments.add(view2);
        fragments.add(view3);
        fragments.add(view4);

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

    public void inquireOrder() {
        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        int order = 2;
        if (mSortPosition == 2 || mSortPosition == 0) {
            order = 2;
        } else {
            order = mSortPosition;
        }

        presenter.inquire(userPlace.getCity(),
                mArea,
                mTypeOrder + "",
                userPlace.getJing(),
                userPlace.getWei(),
                order + "",
                mPage + "",
                mPageSize + ""
        );
    }

    @Override
    public void selectItem(int position, String item, int type) {
        if (type == 0) {
            mSortPosition = position;
            mBinding.ilHead.tvSort.setText(item);
            mBinding.ilHead1.tvSort.setText(item);
        } else if (type == 1) {
            mTypeOrder = Integer.valueOf(item);
            mBinding.ilHead.tvOrderType.setText(DemoUtils.TypeToOccupation(mTypeOrder));
            mBinding.ilHead1.tvOrderType.setText(DemoUtils.TypeToOccupation(mTypeOrder));
        } else if (type == 2) {
            mArea = item;
            mBinding.ilHead.tvCity.setText(mArea);
            mBinding.ilHead1.tvCity.setText(mArea);
        }
        mPage = 1;
        inquireOrder();
        mBinding.ssvContent.scrolltoSlide();
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
        view4.setWorkStatus(is_work);
        SharedPreferencesUtils.setParam(aty, Constant.ROLESTATUE, is_work);
    }

    private void getNoRead() {
        mNoReadPresenter.getNoRead(MaiLiApplication.getInstance().getUserInfo().getPhone());
    }

    private void updateNoRead(NoReadModel info) {
        if (info == null) {
            return;
        }
        mJobPre = info.isUserJobBool() ? 1 : 0;
        mAreaPre = info.isUserPlace() ? 1 : 0;
        ((MainActivity) aty).updateMessageNoRead(info.getNoReadCount());

    }

    private void stopRefersh() {
        if (mBinding != null) {
            mBinding.mrlBody.finishRefresh();
            mBinding.mrlBody.finishRefreshLoadMore();
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
            if (info.getData() != null && info.getData().getAdvertisList() != null && info.getData().getAdvertisList().size() > 0) {
                MaiLiApplication.getInstance().mMessageAdvList = info.getData().getAdvertisList();
            }
         /* if (info.getData() != null && info.getData().getAdvertisementMsgList() != null && info.getData().getAdvertisementMsgList().size() > 0) {
                mBinding.llyNotice.setVisibility(View.VISIBLE);
                mBinding.viewSlideLine.setVisibility(View.GONE);
                mBinding.ssvContent.refshHeight();//重新计算悬浮的高度
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
            }*/
            // mBinding.llyNotice.setVisibility(View.GONE);
        } else if (object instanceof ReceivingModel) {
            ReceivingModel info = (ReceivingModel) object;

            if (mPage == 1) {
                mList.clear();
                if (info == null || info.getData() == null || info.getData().size() == 0) {
                    stateModel1.setEmptyState(EmptyState.EXPAND);
                } else {
                    stateModel1.setEmptyState(EmptyState.NORMAL);
                }
            }
            if (info != null && info.getData() != null) {
                if (info.getData().size() < mPageSize) {
                    if (mPage != 1) {
                        Toast.makeText(MaiLiApplication.getInstance(), "已经到底了!", Toast.LENGTH_SHORT).show();
                    }
                    mBinding.mrlBody.setLoadMore(false);
                } else {
                    mBinding.mrlBody.setLoadMore(true);
                }
                mList.addAll(info.getData());

                if (mList.size() < 6) {
                    View inflate = View.inflate(aty, R.layout.item_receiving_message, null);
                    int w = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                            ViewGroup.MeasureSpec.UNSPECIFIED);
                    int h = ViewGroup.MeasureSpec.makeMeasureSpec(0,
                            ViewGroup.MeasureSpec.UNSPECIFIED);
                    inflate.measure(w, h);
                    int height = inflate.getMeasuredHeight();
                    int count_height = height * (6 - mList.size());
                    mBinding.viewFoodHeight.setVisibility(View.VISIBLE);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, count_height);
                    mBinding.viewFoodHeight.setLayoutParams(layoutParams);
                } else {
                    mBinding.viewFoodHeight.setVisibility(View.GONE);
                }


                mOrderAdapter.notifyDataSetChanged();
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
