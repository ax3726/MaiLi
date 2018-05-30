package com.gsy.ml.ui.person;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityBankCardLayoutBinding;
import com.gsy.ml.model.common.AddBankModel;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.BankCardModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.BankCardPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.views.InformationDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2017/5/31.
 */

public class BankCardActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityBankCardLayoutBinding mBinding;
    private CommonAdapter<BankCardModel.DataBean> adapter;
    private List<BankCardModel.DataBean> list = new ArrayList<>();
    private BankCardPresenter presenter = new BankCardPresenter(this);
    private StateModel stateModel = new StateModel();
    private BankCardModel model;
    private int mType = 0;
    private int mPage = 0;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card_layout;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("银行卡");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty); //注册事件
        mBinding = (ActivityBankCardLayoutBinding) vdb;

        mPage = getIntent().getIntExtra("page", 0);
        mBinding.llyAdd.setOnClickListener(this);
        stateModel.setEmptyState(EmptyState.PROGRESS);
        stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
            @Override
            public void click(View view) {
                stateModel.setEmptyState(EmptyState.PROGRESS);
                inquire();
            }
        });
        mBinding.setStateModel(stateModel);
        myBank();
    }

    public void myBank() {
        adapter = new CommonAdapter<BankCardModel.DataBean>(aty, R.layout.item_bank, list) {
            @Override
            protected void convert(ViewHolder holder, final BankCardModel.DataBean bank, int position) {
                TextView textView1 = holder.getView(R.id.tv_bank);
                TextView textView2 = holder.getView(R.id.tv_bank_num);
                LinearLayout llyItem = holder.getView(R.id.lly_item);
                textView1.setText(bank.getBankType());
                if (bank.getBankNum().length() > 4) {
                    String num = bank.getBankNum().substring(bank.getBankNum().length() - 4, bank.getBankNum().length());
                    textView2.setText(num);
                }
                llyItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mPage == 1) {
                            EventBus.getDefault().post(bank);
                            finish();
                        }
                    }
                });

                //删除
                llyItem.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        InformationDialog informationDialog = new InformationDialog(aty);
                        informationDialog.setTitle("提示");
                        informationDialog.setMessage("您确定要删除该银行卡吗？");
                        informationDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                            @Override
                            public void onDialogClick(Dialog dlg, View view) {
                                showWaitDialog();
                                presenter.myBank(MaiLiApplication.getInstance().getUserInfo().getPhone(), bank.getId() + "", "", "", 4 + "");
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
            }
        };

        mBinding.rvMessage.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rvMessage.setAdapter(adapter);
        inquire();
    }

    //查询
    public void inquire() {
        presenter.myBank(MaiLiApplication.getInstance().getUserInfo().getPhone(), "", "", "", 3 + "");
    }

    private InformationDialog mStateDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_add:
                if (MaiLiApplication.getInstance().getUserInfo().getCheckStatus() == 0) {//没有实名认证
                    mStateDialog = new InformationDialog(aty);
                    mStateDialog.setTitle("提示");
                    mStateDialog.setMessage("请先认证身份才能继续操作哦!");
                    mStateDialog.setPositiveButton("确定", new InformationDialog.IDialogClickListener() {
                        @Override
                        public void onDialogClick(Dialog dlg, View view) {
                            dlg.dismiss();
                            startActivity(new Intent(aty, IdentityCardActivity.class).putExtra("type", 1));
                        }
                    });
                    mStateDialog.show();
                } else {
                    mType = 1;
                    startActivity(new Intent(aty, AddBankActivity.class));
                }

                break;
        }
    }

    public void BankCard(BankCardModel model) {
        if (model == null) {
            stateModel.setEmptyState(EmptyState.EXPAND1);
            return;
        }
        list.clear();
        if (model.getData() == null || model.getData().size() == 0) {
            stateModel.setEmptyState(EmptyState.EXPAND1);
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
        } else if (object instanceof BankCardModel) {
            model = (BankCardModel) object;
            BankCard(model);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAddress(AddBankModel addBankModel) {
        showWaitDialog();
        if (mType == 1) {  //新增
            presenter.myBank(MaiLiApplication.getInstance().getUserInfo().getPhone(),
                    "",
                    addBankModel.getNum(),
                    addBankModel.getBankType(),
                    mType + "");
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
