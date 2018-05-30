package com.gsy.ml.ui.person;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityWalletLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.WalletModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.WalletPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.DemoUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/4/21.
 * 钱包类
 */

public class WalletActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityWalletLayoutBinding wBinding;

    private WalletPresenter wPerson = new WalletPresenter(this);


    private int pageNo = 1;
    private int pageSize = 10;

    @Override
    public int getLayoutId() {
        return R.layout.activity_wallet_layout;
    }

    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        wBinding = (ActivityWalletLayoutBinding) vdb;
        wBinding.tvMore.setOnClickListener(this);
        wBinding.rlTopUp.setOnClickListener(this);
        wBinding.rlDrawMoney.setOnClickListener(this);
        wBinding.rlAddBank.setOnClickListener(this);
        wBinding.rlShareMoney.setOnClickListener(this);
        wBinding.rlManey.setOnClickListener(this);
        showWaitDialog();
        getMoney();
    }

    private void getMoney() {
        wPerson.selectWaller(MaiLiApplication.getInstance().getUserInfo().getPhone(), pageNo + "", pageSize + "");
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        wBinding.ilHead.tvTitle.setText("我的钱包");
        wBinding.ilHead.llyLeft.setOnClickListener(this);
        wBinding.ilHead.vXian.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_more:
                startActivity(new Intent(aty, MoreActivity.class));
                break;
            case R.id.rl_top_up:
                startActivity(new Intent(aty, TopUpActivity.class));
                break;
            case R.id.rl_draw_money:
                startActivity(new Intent(aty, DrawMoneyActivity.class));
                break;
            case R.id.rl_add_bank:
                startActivity(new Intent(aty, BankCardActivity.class));
                break;
            case R.id.rl_share_money:
                startActivity(new Intent(aty, ShareMoneyActivity.class));
                break;
            case R.id.rl_maney:
                startActivity(new Intent(aty, VoucherActivity.class));
                break;
        }
    }

    private void setWallet(WalletModel model) {
        if (model == null || model.getData() == null) {
            return;
        }
        Double monney = model.getData().getWorkGet();
        wBinding.tvEarnings.setText(DemoUtils.formatDoubleDOWN(model.getData().getTotalMoney()) + "");
        wBinding.tvTotalMoney.setText(DemoUtils.formatDoubleDOWN(monney) + "");
        wBinding.tvComplete.setText(model.getData().getCount() + "");
    }


    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof WalletModel) {
            WalletModel models = (WalletModel) object;
            setWallet(models);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    /**
     * 更新价格
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateNotice(String message) {
        if ("更新价格".equals(message)) {
            getMoney();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
