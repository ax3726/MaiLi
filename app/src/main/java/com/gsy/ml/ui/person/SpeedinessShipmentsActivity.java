package com.gsy.ml.ui.person;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivitySpeedinessShipmentsBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.LatLonModel;
import com.gsy.ml.model.person.SpeedinessShipmentsModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.SpeedinessShipmentsPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.home.WorkType.PayoutActivity;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/8/7.
 */

public class SpeedinessShipmentsActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivitySpeedinessShipmentsBinding mBinding;
    private CommonAdapter<SpeedinessShipmentsModel.DataBean.RowsBean> mAdapter;
    private ArrayList<SpeedinessShipmentsModel.DataBean.RowsBean> mDataList = new ArrayList<>();
    private SpeedinessShipmentsPresenter presenter = new SpeedinessShipmentsPresenter(this);
    private SpeedinessShipmentsModel model;
    private boolean isCheck = false;  //是否为多选状态
    private int quantity = 0;
    int type;
    int page = 1;
    int row = 10;
    private SpeedinessShipmentsModel.DataBean.RowsBean mSelectItem = null;//单选
    private ArrayList<SpeedinessShipmentsModel.DataBean.RowsBean> mSelectList = new ArrayList<>();//多选
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_speediness_shipments;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("外卖一键发单");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivitySpeedinessShipmentsBinding) vdb;
        stateModel.setExpandRes("还没有外卖订单信息呢!", R.drawable.no_data1_icon);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                commodity();
            }
        });
        mBinding.setStateModel(stateModel);

        mBinding.tvMultipleChoice.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
        mBinding.tvFabu.setOnClickListener(this);
        type = getIntent().getIntExtra("type", 2);
        init();
        commodity();
    }

    public void commodity() {

        presenter.getListGoods(MaiLiApplication.getInstance().getUserInfo().getPhone(), type + "", page + "", row + "");
    }

    public void getJinwei(String address) {
        showWaitDialog();
        presenter.getLatLon(address);
    }

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    private String fromatToTime(long time) {
        if (time > 0) {
            if (Utils.isOneDay(time)) {
                return Utils.getDateToString(time, "HH:mm");
            } else {
                return Utils.getTimeStyle22(time);
            }
        } else {
            return "";
        }
    }

    //监听手机返回键
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isCheck) {
                for (SpeedinessShipmentsModel.DataBean.RowsBean s : mDataList) {
                    s.setPitchOn(false);
                }
                mBinding.tvFabu.setVisibility(View.GONE);
                mBinding.tvCancel.setVisibility(View.GONE);
                isCheck = false;
                mAdapter.notifyDataSetChanged();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void init() {
        mAdapter = new CommonAdapter<SpeedinessShipmentsModel.DataBean.RowsBean>(aty, R.layout.item_speediness_shipments, mDataList) {
            @Override
            protected void convert(ViewHolder holder, final SpeedinessShipmentsModel.DataBean.RowsBean model, final int position) {
                LinearLayout lly_item = holder.getView(R.id.lly_item);
                CardView cv_fabu = holder.getView(R.id.cd_fabu);
                final ImageView iv_choice = holder.getView(R.id.iv_choice); //勾选
                TextView order_number = holder.getView(R.id.tv_order_number); //订单号
                TextView time = holder.getView(R.id.tv_time); //时间
                TextView location = holder.getView(R.id.tv_location); //地址
                TextView describe = holder.getView(R.id.tv_describe); //物品描述
                TextView tv_send_order = holder.getView(R.id.tv_send_order); //一键发布

                order_number.setText(model.getOrderId() + "");
                time.setText(fromatToTime(model.getOrderTime() * 1000L));
                location.setText(model.getRecipientAddress());
                describe.setText("物品描述：" + model.getDishs());

                tv_send_order.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mSelectItem = model;
                        getJinwei(model.getRecipientAddress());
                    }
                });
                if (isCheck) {   //是否为多选状态
                    iv_choice.setVisibility(View.VISIBLE);
                    cv_fabu.setVisibility(View.GONE);
                    if (mDataList.get(position).isPitchOn()) {  //判断实体类是否为true
                        iv_choice.setSelected(true);
                    } else {
                        iv_choice.setSelected(false);
                    }
                    lly_item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (iv_choice.isSelected()) {  //勾选框是否选中
                                iv_choice.setSelected(false);
                                mDataList.get(position).setPitchOn(false);
                                quantity--;
                                mBinding.tvFabu.setText("一键发单(" + quantity + ")");
                            } else {
                                if (!isCheck) {  //判断
                                    iv_choice.setSelected(false);
                                } else {
                                    iv_choice.setSelected(true);
                                    if (quantity >= 5) {
                                        mDataList.get(position).setPitchOn(false);
                                        Toast.makeText(mContext, "留点单给别人吧，一次最多发5个单哦！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mDataList.get(position).setPitchOn(true);
                                        quantity++;
                                    }
                                    mBinding.tvFabu.setText("一键发单(" + quantity + ")");
                                }
                            }
                            notifyDataSetChanged();
                        }
                    });
                } else {
                    iv_choice.setVisibility(View.GONE);
                    cv_fabu.setVisibility(View.VISIBLE);
                    iv_choice.setSelected(false);
                    quantity = 0;
                    mBinding.tvFabu.setText("一键发单(" + quantity + ")");
                }


            }
        };
        mBinding.rvMessage.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rvMessage.setAdapter(mAdapter);
        mBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        commodity();
                    }
                }, 600);

            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                page++;
                commodity();
            }
        });
        commodity();
    }

    private void stopRefresh() {
        mBinding.mrlBody.finishRefresh();
        mBinding.mrlBody.finishRefreshLoadMore();
    }



    private void clearState() {
        for (SpeedinessShipmentsModel.DataBean.RowsBean s : mDataList) {
            s.setPitchOn(false);
        }
        mBinding.tvFabu.setVisibility(View.GONE);
        mBinding.tvCancel.setVisibility(View.GONE);
        isCheck = false;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_multiple_choice:   //多选
                isCheck = true;
                mBinding.tvCancel.setVisibility(View.VISIBLE);
                mBinding.tvFabu.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_cancel:            //取消
                clearState();
                break;
            case R.id.tv_fabu: //一键发布
                mSelectList.clear();
                String str = "";
                for (SpeedinessShipmentsModel.DataBean.RowsBean s : mDataList) {
                    if (s.isPitchOn()) {
                        mSelectList.add(s);
                        str = str + s.getRecipientAddress() + "|";
                    }
                }
                str = str.substring(0, str.length() - 1);
                getJinwei(str);
                break;
        }
    }

    public void SpeedinessShipments(SpeedinessShipmentsModel model) {
        if (model == null || model.getData() == null || model.getData().getRows().size() == 0) {
            stateModel.setEmptyState(EmptyState.EXPAND);
            return;
        }
        if (page == 1) {
            mDataList.clear();
            if (model.getData().getRows().size() == 0) {
                stateModel.setEmptyState(EmptyState.EXPAND);
            } else {
                stateModel.setEmptyState(EmptyState.NORMAL);
            }
        }
        mDataList.addAll(model.getData().getRows());
        if (mDataList.size() < row) {
            mBinding.mrlBody.setLoadMore(false);
        } else {
            mBinding.mrlBody.setLoadMore(true);
        }
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        stopRefresh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof SpeedinessShipmentsModel) {
            model = (SpeedinessShipmentsModel) object;


            SpeedinessShipments(model);
        } else if (object instanceof LatLonModel) {
            LatLonModel info = (LatLonModel) object;
            if (isCheck) {//是否多选状态
                boolean isTrue = false;
                if (info.getData() != null && info.getData().size() > 0) {
                    for (int i = 0; i < info.getData().size(); i++) {
                        if (info.getData().get(i) != null) {

                            mSelectList.get(i).setProvince(info.getData().get(i).getProvince());
                            mSelectList.get(i).setCity(info.getData().get(i).getCity());
                            mSelectList.get(i).setDistrict(info.getData().get(i).getDistrict());
                            mSelectList.get(i).setLatitude(info.getData().get(i).getLatitude());
                            mSelectList.get(i).setLongitude(info.getData().get(i).getLongitude());
                        } else {
                            isTrue = true;
                        }
                    }
                    if (!isTrue) {
                        startActivity(new Intent(aty, PayoutActivity.class).putParcelableArrayListExtra("data_list", mSelectList));
                        clearState();
                    } else {
                        Toast.makeText(MaiLiApplication.getInstance(), "订单信息错误!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "订单信息错误!", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (info.getData() != null && info.getData().size() > 0) {
                    mSelectItem.setProvince(info.getData().get(0).getProvince());
                    mSelectItem.setCity(info.getData().get(0).getCity());
                    mSelectItem.setDistrict(info.getData().get(0).getDistrict());
                    mSelectItem.setLatitude(info.getData().get(0).getLatitude());
                    mSelectItem.setLongitude(info.getData().get(0).getLongitude());
                    startActivity(new Intent(aty, PayoutActivity.class).putExtra("data_one", mSelectItem));

                } else {
                    Toast.makeText(MaiLiApplication.getInstance(), "订单信息错误!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cancelActivity(String action) {
        if ("关闭".equals(action)) {
            finish();
        }
    }
}
