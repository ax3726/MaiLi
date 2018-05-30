package com.gsy.ml.ui.person;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPartnerLayouBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.PartnerPresenter;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.ProvinceAreaHelper;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.Utils;


/**
 * Created by Administrator on 2017/5/22.
 */

public class PartnerActivity extends BaseActivity implements View.OnClickListener, ILoadPVListener {
    private ActivityPartnerLayouBinding mBinding;
    private PartnerPresenter pPresenter = new PartnerPresenter(this);
    public ChooseDatePopupWindow mAreaPopupWindow; //选择做工区域
    private String province = "", city = "", area = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_partner_layou;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.tvTitle.setText("招商加盟");
        mBinding.ilHead.tvTitle.setTextColor(getResources().getColor(R.color.colorFFFFFF));
        mBinding.ilHead.llColor.setBackgroundResource(R.color.color292929);
        mBinding.ilHead.idLeftBtnImg.setImageResource(R.drawable.back2_icon);
        mBinding.ilHead.vXian.setVisibility(View.GONE);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityPartnerLayouBinding) vdb;
        mBinding.tvWe.setOnClickListener(this);
        mBinding.btnOk.setOnClickListener(this);
        mBinding.llyAgency.setOnClickListener(this);

        /**
         * 获取状态栏高度——方法1
         * */
        int statusBarHeight1 = -1;
        //获取status_bar_height资源的ID
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            //根据资源ID获取响应的尺寸值
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
        }
        int height = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        int rel_height = (int) (height - statusBarHeight1 - Utils.dip2px(aty, 50));

        LinearLayout.LayoutParams map_layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, rel_height);
        mBinding.rlyBody.setLayoutParams(map_layoutParams);
        mBinding.svBody.setTitleHeight(rel_height);

        initArea();
    }

    private void initArea() {
        mAreaPopupWindow = new ChooseDatePopupWindow(aty, 3);
        mAreaPopupWindow.setTitle("请选择代理区域");
        mAreaPopupWindow.setArea(true);
        ProvinceAreaHelper provinceAreaHelper = new ProvinceAreaHelper(aty);
        provinceAreaHelper.initProvinceData();
        String[] pros = provinceAreaHelper.getmProvinceDatas();
        List<String> data1 = new ArrayList<>();
        if (pros != null) {
            for (int i = 0; i < pros.length; i++) {
                data1.add(pros[i]);
            }
        }
        mAreaPopupWindow.setData1(data1);
        mAreaPopupWindow.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                province = item1;
                if ("全省".equals(item2)) {
                    city = "";
                    area = "";
                } else if ("全市".equals(item3)) {
                    city = item2;
                    area = "";
                } else {
                    city = item2;
                    area = item3;
                }

                mBinding.etAgency.setText(province + city + area);
            }
        });
    }

    public void getInsertCooperative() {
        String phone = mBinding.etPhone.getText().toString().trim();
        String name = mBinding.etNames.getText().toString().trim();
        String company = mBinding.etCompany.getText().toString().trim();
        String wechatNumber = mBinding.etWechat.getText().toString().trim();
        String qqNumber = mBinding.etQq.getText().toString().trim();
        String email = mBinding.etEmail.getText().toString().trim();
        String message = mBinding.etMessage.getText().toString().trim();
        String industry = mBinding.etIndustry.getText().toString().trim();
        String agency = mBinding.etAgency.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入手机号码！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请输入姓名！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(agency)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请选择代理区域", Toast.LENGTH_SHORT).show();
            return;
        }

        showWaitDialog();

        pPresenter.insertCooperative(phone,
                name,
                TextUtils.isEmpty(company) ? "" : company,
                TextUtils.isEmpty(wechatNumber) ? "" : wechatNumber,
                TextUtils.isEmpty(qqNumber) ? "" : qqNumber,
                TextUtils.isEmpty(email) ? "" : email,
                TextUtils.isEmpty(province) ? "" : province,
                TextUtils.isEmpty(city) ? "" : city,
                TextUtils.isEmpty(area) ? "" : area,
                TextUtils.isEmpty(message) ? "" : message,
                TextUtils.isEmpty(industry) ? "" : industry
        );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.tv_we:
                //    mBinding.llyWe.setVisibility(View.GONE);
//                mBinding.svInputbox.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_ok:
                getInsertCooperative();
                break;
            case R.id.lly_agency:
                mAreaPopupWindow.showPopupWindow(mBinding.getRoot());
                break;
        }
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            Toast.makeText(MaiLiApplication.getInstance(), "提交成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }
}
