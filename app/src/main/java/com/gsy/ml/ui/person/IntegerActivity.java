package com.gsy.ml.ui.person;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityIntegralLayoutBinding;
import com.gsy.ml.databinding.ItemIntegerHeadBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.IntegerModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.IntegerPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.common.WebViewActivity;
import com.gsy.ml.ui.utils.DemoUtils;
import com.lm.material_refresh_lib.MaterialRefreshLayout;
import com.lm.material_refresh_lib.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.Utils;

/**
 * Created by Administrator on 2017/4/21.
 * 积分详情类
 */

public class IntegerActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityIntegralLayoutBinding iBinding;
    private ItemIntegerHeadBinding hBinding;
    private CommonAdapter<IntegerModel.DataBean.ListBean> mAdapter;
    private List<IntegerModel.DataBean.ListBean> list = new ArrayList<>();
    private IntegerPresenter ipresenter = new IntegerPresenter(this);
    private int pageNo = 1;
    private int pageSize = 20;
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_integral_layout;
    }

    @Override
    public void initData() {
        super.initData();
        iBinding = (ActivityIntegralLayoutBinding) vdb;
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                ipresenter.selectInteger(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
            }
        });
        iBinding.setStateModel(stateModel);

        hBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.item_integer_head, null, false);
        iBinding.rvMessage.addHeaderView(hBinding.getRoot());

        mAdapter = new CommonAdapter<IntegerModel.DataBean.ListBean>(aty, R.layout.item_integral_message, list) {
            @Override
            protected void convert(ViewHolder holder, IntegerModel.DataBean.ListBean listBean, int position) {
                TextView text1 = holder.getView(R.id.tv_explain);
                TextView text2 = holder.getView(R.id.tv_integers);
                TextView text3 = holder.getView(R.id.tv_time);

                text1.setText(listBean.getIntegralExplain());
                text3.setText(Utils.getTimeStyle3(listBean.getCreateTime()));
                text2.setText(listBean.getIntegralNum() + "积分");
            }
        };
        iBinding.rvMessage.setLayoutManager(new LinearLayoutManager(aty));
        iBinding.rvMessage.setAdapter(mAdapter);

        ipresenter.selectInteger(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
        iBinding.mrlBody.setSunStyle(true);
        iBinding.mrlBody.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                pageNo = 1;
                ipresenter.selectInteger(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                super.onRefreshLoadMore(materialRefreshLayout);
                pageNo++;
                ipresenter.selectInteger(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
            }
        });
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        iBinding.ilHead.tvTitle.setText("积分详情");
        iBinding.ilHead.vXian.setVisibility(View.GONE);
        iBinding.ilHead.imgRight.setVisibility(View.VISIBLE);

        iBinding.ilHead.llyLeft.setOnClickListener(this);
        iBinding.ilHead.llyRight.setOnClickListener(this);
        iBinding.ilHead.llColor.setBackgroundColor(getResources().getColor(R.color.colorFFd249));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_right:
                startActivity(new Intent(aty, WebViewActivity.class).putExtra("url", DemoUtils.TypeToPriceInfo(-1)));
                break;
        }
    }

    private void setInteger(IntegerModel model) {
        if (model == null || model.getData() == null) {
            if (pageNo == 1) {
                stateModel.setEmptyState(EmptyState.EMPTY);
            }
            return;
        }
        if (pageNo == 1) {
            list.clear();
            if (model.getData().getList().size() == 0) {
                stateModel.setEmptyState(EmptyState.EMPTY);
            } else {
                stateModel.setEmptyState(EmptyState.NORMAL);
            }
        }

        hBinding.tvIntegeral.setText(model.getData().getIntegral() + "");
        list.addAll(model.getData().getList());
        iBinding.rvMessage.notifyDataSetChanged();
        if (list.size() < pageSize) {
            iBinding.mrlBody.setLoadMore(false);
        } else {
            iBinding.mrlBody.setLoadMore(true);
        }
    }

    private void stopRefresh() {
        iBinding.mrlBody.finishRefresh();
        iBinding.mrlBody.finishRefreshLoadMore();
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        stopRefresh();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof IntegerModel) {
            IntegerModel model = (IntegerModel) object;
            stateModel.setEmptyState(EmptyState.NORMAL);
            setInteger(model);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}

