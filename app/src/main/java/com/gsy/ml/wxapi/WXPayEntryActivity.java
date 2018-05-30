package com.gsy.ml.wxapi;

import android.content.Intent;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.Constant;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPayResultBinding;
import com.gsy.ml.ui.common.BaseActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;
    private ActivityPayResultBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay_result;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityPayResultBinding) vdb;
        api = WXAPIFactory.createWXAPI(this, Constant.WEXAPPID);
        api.handleIntent(getIntent(), this);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        int code = resp.errCode;
        String transaction = resp.transaction;
        String errStr = resp.errStr;
        if (code == 0) {
            mBinding.tvResult.setText("充值成功了!");
            //显示充值成功的页面和需要的操作
            EventBus.getDefault().post("更新价格");//充值成功 更新价格
            Toast.makeText(MaiLiApplication.appliactionContext, "充值成功了!", Toast.LENGTH_SHORT).show();
        }
        if (code == -1) {
            //错误	可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
            mBinding.tvResult.setText("充值失败!");
            Toast.makeText(MaiLiApplication.appliactionContext, "充值失败!", Toast.LENGTH_SHORT).show();
        }

        if (code == -2) {
            mBinding.tvResult.setText("用户取消了!");
            Toast.makeText(MaiLiApplication.appliactionContext, "用户取消了!", Toast.LENGTH_SHORT).show();
            //用户取消
        }
        finish();

    }
}