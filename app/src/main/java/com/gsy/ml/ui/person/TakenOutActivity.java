package com.gsy.ml.ui.person;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityTakeOutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.person.TakenOutModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.TokenOutPresenter;
import com.gsy.ml.ui.common.BaseActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by Administrator on 2017/8/7.
 * 登录各大外卖平台
 */

public class TakenOutActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityTakeOutBinding mBinding;
    private TokenOutPresenter presenter = new TokenOutPresenter(this);

    @Override
    public int getLayoutId() {
        return R.layout.activity_take_out;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("绑定外卖平台");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
    }

    //setCompoundDrawablesWithIntrinsicBounds
    @Override
    public void initData() {
        super.initData();
        EventBus.getDefault().register(aty);
        mBinding = (ActivityTakeOutBinding) vdb;
        int parameter = getIntent().getIntExtra("parameter", 1);
        mBinding.tvRegister.setOnClickListener(this);
        switch (parameter) {
            case 1:
                Drawable drawable1 = getResources().getDrawable(R.drawable.e_icon);
                mBinding.tvName.setCompoundDrawablesWithIntrinsicBounds(null, drawable1, null, null);
                mBinding.tvName.setTextColor(getResources().getColor(R.color.colorFF1579DE));
                mBinding.tvName.setText("饿了么");
                mBinding.etId.setHint("请输入饿了么账号");
                mBinding.etPwd.setVisibility(View.GONE);
                break;
            case 3:
                Drawable drawable3 = getResources().getDrawable(R.drawable.baidu_take_out_icon);
                mBinding.tvName.setCompoundDrawablesWithIntrinsicBounds(null, drawable3, null, null);
                mBinding.tvName.setTextColor(getResources().getColor(R.color.colorFF0400));
                mBinding.tvName.setText("百度外卖");
                mBinding.etId.setHint("请输入百度外卖登录账号");
                break;
        }
    }

    @Override
    public void initWidget() {
        super.initWidget();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.cv_register:
                startActivity(new Intent(aty, SpeedinessShipmentsActivity.class));
                break;
            case R.id.tv_register:

                bindElme();
                break;
        }
    }

    private void bindElme() {
        String elm_name = mBinding.etId.getText().toString().trim();
        if (TextUtils.isEmpty(elm_name)) {
            Toast.makeText(aty, "门店名不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        presenter.addEleStoreBin(MaiLiApplication.getInstance().getUserInfo().getPhone(), elm_name);
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof TakenOutModel) {
            TakenOutModel model = (TakenOutModel) object;
            startActivity(new Intent(aty, MeiTuanWebActivity.class).putExtra("url", model.getData()));

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

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
