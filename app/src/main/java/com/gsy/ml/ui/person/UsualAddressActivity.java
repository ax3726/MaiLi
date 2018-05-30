package com.gsy.ml.ui.person;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.LatLonPoint;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityUsualAddressLayoutBinding;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.EditAddressModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.UsualAddressModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.UsualAddressPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.home.EditAddressActivity;
import com.gsy.ml.ui.views.InformationDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2017/5/16.
 */

public class UsualAddressActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityUsualAddressLayoutBinding mBinding;
    private UsualAddressModel mStartAddress;
    private UsualAddressPresenter presenter = new UsualAddressPresenter(this);
    private List<UsualAddressModel.DataBean> list = new ArrayList<>();
    private CommonAdapter<UsualAddressModel.DataBean> adapter;
    private int mtype;
    private int mID;
    private int mPage = 0;
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_usual_address_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("常用地址");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty); //注册事件
        mBinding = (ActivityUsualAddressLayoutBinding) vdb;
        mPage = getIntent().getIntExtra("page", 0);
        mBinding.llyAdd.setOnClickListener(this);
        stateModel.setExpandRes("没有添加地址,点击下方按钮添加", R.drawable.no_data_ml_icon);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                initMessage3();
            }
        });
        mBinding.setStateModel(stateModel);

        initAdapter();
    }


    public void initAdapter() {
        adapter = new CommonAdapter<UsualAddressModel.DataBean>(aty, R.layout.item_usual_address, list) {
            @Override
            protected void convert(ViewHolder holder, final UsualAddressModel.DataBean data, final int position) {
                TextView textView1 = holder.getView(R.id.tv_location);
                TextView textView2 = holder.getView(R.id.tv_name);
                TextView textView3 = holder.getView(R.id.tv_phone);
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                ImageView img = holder.getView(R.id.img_compile);
                textView1.getPaint().setFakeBoldText(true);//字体加粗
                textView1.setText(data.getProvince() + data.getCity() + data.getArea() + data.getPlace() + data.getDoorplate());
                textView2.setText(data.getContactsName());
                textView3.setText(data.getContactsPhone());
                //删除
                lly_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        InformationDialog informationDialog = new InformationDialog(aty);
                        informationDialog.setTitle("提示");
                        informationDialog.setMessage("您确定要删除该地址吗？");
                        informationDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                showWaitDialog();
                                presenter.setMessage(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                                        4 + "",
                                        data.getId() + "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""
                                );
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
                        return false;
                    }
                });

                lly_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPage == 1) {
                            AddressModel addressModel = new AddressModel(data.getProvince(),
                                    data.getCity(),
                                    data.getArea(),
                                    data.getPlace(),
                                    "",
                                    new LatLonPoint(Float.valueOf(data.getWei()), Float.valueOf(data.getJing())),
                                    1);
                            addressModel.setAdd_phone(data.getContactsPhone());
                            addressModel.setAdd_name(data.getContactsName());
                            addressModel.setDoor_info(data.getDoorplate());
                            EventBus.getDefault().post(addressModel);
                            finish();
                        }
                    }
                });

                //修改
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mtype = 2;
                        mID = data.getId();
                        startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", 10).putExtra("is_edit",true));
                    }
                });
            }
        };

        mBinding.rvMessage.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rvMessage.setAdapter(adapter);
        initMessage3();
    }

    //查询
    public void initMessage3() {
        presenter.setMessage(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                3 + "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_add:  //新增常用地址
                mtype = 1;
                startActivity(new Intent(aty, EditAddressActivity.class).putExtra("type", 3).putExtra("is_edit",true));
                break;
        }
    }

    public void UsualAddress(UsualAddressModel model) {
        if (model == null) {
            stateModel.setEmptyState(EmptyState.EXPAND);
            return;
        }
        list.clear();
        if (model.getData() == null || model.getData().size() == 0) {
            stateModel.setEmptyState(EmptyState.EXPAND);
        } else {
            stateModel.setEmptyState(EmptyState.NORMAL);
        }
        list.addAll(model.getData());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof UsualAddressModel) {
            UsualAddressModel model = (UsualAddressModel) object;
            UsualAddress(model);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(EditAddressModel mEditAddressModel) {
        AddressModel addressModel = mEditAddressModel.getmAddress();

        if (addressModel.getType() != 1) {
            //新增
            showWaitDialog();
            if (mtype == 1) {
                presenter.setMessage(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                        mtype + "",
                        "",
                        addressModel.getAdd_name(),
                        addressModel.getAdd_phone(),
                        addressModel.getProvince(),
                        addressModel.getCity(),
                        addressModel.getDistrict(),
                        addressModel.getName(),
                        addressModel.getPoint().getLongitude() + "",
                        addressModel.getPoint().getLatitude() + "",
                        addressModel.getDoor_info()
                );
            } else if (mtype == 2) {
                //修改
                presenter.setMessage(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                        mtype + "",
                        mID + "",
                        addressModel.getAdd_name(),
                        addressModel.getAdd_phone(),
                        addressModel.getProvince(),
                        addressModel.getCity(),
                        addressModel.getDistrict(),
                        addressModel.getName(),
                        addressModel.getPoint().getLongitude() + "",
                        addressModel.getPoint().getLatitude() + "",
                        addressModel.getDoor_info()
                );
            }
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
