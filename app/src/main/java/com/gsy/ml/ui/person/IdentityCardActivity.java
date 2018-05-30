package com.gsy.ml.ui.person;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityIdentityCardBinding;
import com.gsy.ml.model.EventMessage.MessageEvent;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.InformationPresenter;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.PhotoActivity;
import com.gsy.ml.ui.common.PhotoPreviewActivity;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.main.MainActivity;
import com.gsy.ml.ui.usercard.UserCardFragment;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.utils.UpdataImgUtils;
import com.gsy.ml.ui.views.ChooseDatePopupWindow;
import com.gsy.ml.ui.views.OccupationDialog;
import com.gsy.ml.ui.views.PopupOption;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ml.gsy.com.library.utils.MyUUID;

/**
 * Created by Administrator on 2017/6/30.
 */

public class IdentityCardActivity extends PhotoActivity implements View.OnClickListener,
        LocationHelper.ILocationListener, OccupationDialog.IDialogClickListener, UpdataImgUtils.UpdataImgCallBack, ILoadPVListener {
    private ActivityIdentityCardBinding mBinding;
    private boolean is_head = false;
    private String headimg = "";//头像
    private int photoType = 1;
    private PopupOption mPopupOption;
    private UpdataImgUtils mUpdataImgUtils;
    private OccupationDialog mOccupationDialog;
    private ChooseDatePopupWindow datePopupWindow;
    private int nameRequest = 1001;//请求码|
    private InformationPresenter mPresenter = new InformationPresenter(this);
    private HashMap<String, String> objectKey = new HashMap<>();
    private int type = 0;
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_identity_card;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.ilHead.tvTitle.setText("完善信息");
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.tvRight.setText("保存");
        mBinding.ilHead.tvRight.setOnClickListener(this);
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityIdentityCardBinding) vdb;
        EventBus.getDefault().register(aty);
        LocationHelper.getInstance().setILocationListener(this);
        LocationHelper.getInstance().startLocation(aty);
        mBinding.rlCity.setOnClickListener(this);
        mBinding.imgHead.setOnClickListener(this);
        mBinding.rlName.setOnClickListener(this);
        mBinding.rlSex.setOnClickListener(this);
        mBinding.rlOccupationChoose.setOnClickListener(this);
        mBinding.rlyRenzhen.setOnClickListener(this);
        mBinding.imgErweima.setOnClickListener(this);
        mBinding.tvPhone.setText(MaiLiApplication.getInstance().getUserInfo().getPhone());
        initView();
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            mPresenter.getUserInfo(MaiLiApplication.getInstance().getUserInfo().getPhone());
            stateModel.setEmptyState(EmptyState.PROGRESS);
            mBinding.setStateModel(stateModel);
            stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
                @Override
                public void click(View view) {
                    stateModel.setEmptyState(EmptyState.PROGRESS);
                }
            });
        } else {
            LocationHelper.getInstance().startLocation(aty);
        }

        if (MaiLiApplication.getInstance().getUserInfo().getCheckStatus() == 1) {
            mBinding.tvState.setText("已实名认证");
            mBinding.tvState.setBackgroundColor(getResources().getColor(R.color.colorFFFFFF));
            mBinding.tvState.setTextColor(getResources().getColor(R.color.colorFF6C00));
            mBinding.rlyRenzhen.setVisibility(View.GONE);
            //cardCornerRadius
        } else {
            mBinding.tvState.setText("未实名认证");
            mBinding.tvState.setTextColor(getResources().getColor(R.color.colorEEEEEE));
            mBinding.rlyRenzhen.setVisibility(View.VISIBLE);
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.img_head:
                if (is_head) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class).putExtra("img", headimg));
                } else {
                    photoType = 3;
                    mPopupOption.showPopupWindow();
                }
                break;
            case R.id.rl_name:
                startActivityForResult(new Intent(aty, NameActivity.class), nameRequest);
                break;
            case R.id.rl_sex:
                datePopupWindow.showPopupWindow(mBinding.getRoot());
                break;
            case R.id.rl_occupation_choose:
                mOccupationDialog.show();
                break;
            case R.id.rl_city:
                Toast.makeText(MaiLiApplication.getInstance(), "正在重新定位！", Toast.LENGTH_SHORT).show();
                LocationHelper.getInstance().startLocation(aty);
                break;
            case R.id.tv_right:
                submitImg();
                break;
            case R.id.rly_renzhen:
                startActivity(new Intent(aty, IdentityImgActivity.class));
                break;
            case R.id.img_erweima:
                startActivity(new Intent(aty, UserCardFragment.class));
                break;
        }
    }

    //打开摄像头拍摄头像或选取图片作为头像
    private void initView() {
        mUpdataImgUtils = new UpdataImgUtils(this, aty);
        mOccupationDialog = new OccupationDialog(aty, 1);
        mOccupationDialog.setIDialogClickListener(this);
        final String[] diagnossArr = new String[]{"拍照", "从手机相册选择"};
        mPopupOption = new PopupOption(aty, null);
        mPopupOption.setItemText(diagnossArr);
        mPopupOption.setCancelText("取消");
        mPopupOption.setCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupOption.dismiss();
            }
        });
        mPopupOption.setItemClickListener(new PopupOption.onPopupWindowItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position == 0) {
                    if (photoType == 3) {
                        takephoto(1);
                    } else {
                        doPhoto();
                    }
                } else {
                    if (photoType == 3) {
                        pickphoto(1);
                    } else {
                        pickphoto();
                    }
                }
            }
        });


        datePopupWindow = new ChooseDatePopupWindow(aty, 1);
        datePopupWindow.setTitle("请选择性别");
        List<String> data1 = new ArrayList<>();
        data1.add("男");
        data1.add("女");
        datePopupWindow.setData1(data1);
        datePopupWindow.setIOccupationListener(new ChooseDatePopupWindow.IOccupationListener() {
            @Override
            public void selectItem(int position1, String item1, int position2, String item2, int position3, String item3) {
                mBinding.tvSex.setText(item1);
            }
        });
    }

    //上传头像
    public void submitImg() {
        String nick_name = mBinding.tvName.getText().toString().trim();//
        String sex = mBinding.tvSex.getText().toString().trim();
        String occupation = mBinding.tvOccupationChoose.getText().toString().trim();
        String city = mBinding.tvCity.getText().toString().trim();

        if (TextUtils.isEmpty(nick_name)) {
            Toast.makeText(MaiLiApplication.getInstance(), "昵称不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sex)) {
            Toast.makeText(MaiLiApplication.getInstance(), "性别不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(occupation)) {
            Toast.makeText(MaiLiApplication.getInstance(), "职业不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(headimg)) {
            Toast.makeText(MaiLiApplication.getInstance(), "请上传头像!", Toast.LENGTH_SHORT).show();
            return;
        }

        objectKey.clear();
        List<String> mImgList = new ArrayList<>();
        List<String> object = new ArrayList<>();

        int imglenth = 0;
        if (!headimg.contains("http")) {
            mImgList.add(headimg);
            imglenth++;
            objectKey.put("1", Link.HEADPHOTOURL + MyUUID.nolineUUID() + ".jpg");
            object.add(objectKey.get("1"));
        }
        showWaitDialog();
        if (imglenth == 0) {
            getCheck();
        } else {
            updataNum = mImgList.size();
            updataSusNum = 0;
            mUpdataImgUtils.updataImgs(Link.bucketName, object, mImgList);
            isupdataAllSus = true;
        }
    }

    //提交数据
    private void getCheck() {
        String nick_name = mBinding.tvName.getText().toString().trim();
        String sex = mBinding.tvSex.getText().toString().trim();
        String occupation = mBinding.tvOccupationChoose.getText().toString().trim();
        String phone = MaiLiApplication.getInstance().getUserInfo().getPhone().trim();
        String city = mBinding.tvCity.getText().toString().trim();

        if (TextUtils.isEmpty(nick_name)) {
            Toast.makeText(MaiLiApplication.getInstance(), "昵称不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(sex)) {
            Toast.makeText(MaiLiApplication.getInstance(), "性别不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(occupation)) {
            Toast.makeText(MaiLiApplication.getInstance(), "职业不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(city)) {
            Toast.makeText(MaiLiApplication.getInstance(), "地址不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        mPresenter.updateInfo(phone, nick_name, sex, city, occupation, TextUtils.isEmpty(objectKey.get("1")) ? "" : objectKey.get("1"));
    }

    //地理位置定位
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
            Toast.makeText(MaiLiApplication.getInstance(), "定位失败,点击重新定位!", Toast.LENGTH_SHORT).show();
            return;
        }
        mBinding.tvCity.setText(aMapLocation.getCity());
    }

    @Override
    public void onCancel() {

    }

    //选择职业
    @Override
    public void onOk(int type) {
        switch (type) {
            case 1:
                mBinding.tvOccupationChoose.setText("在校学生");
                break;
            case 2:
                mBinding.tvOccupationChoose.setText("家庭主妇");
                break;
            case 3:
                mBinding.tvOccupationChoose.setText("上班族");
                break;
            case 4:
                mBinding.tvOccupationChoose.setText("自由职业");
                break;
        }
    }


    private int updataNum = 0;
    private int updataSusNum = 0;
    private boolean isupdataAllSus = true;

    //图片上传失败重新上传
    @Override
    public void onUpdataImgResult(String key, boolean state) {
        if (!state) {
            isupdataAllSus = false;
            hideWaitDialog();
            Toast.makeText(MaiLiApplication.getInstance(), "图片上传失败", Toast.LENGTH_SHORT).show();
            mUpdataImgUtils.cancelUpdata();
        } else {
            updataSusNum++;
        }
        if (updataSusNum >= updataNum && isupdataAllSus) {
            getCheck();    //提交信息
            isupdataAllSus = false;
        }
    }

    //显示头像
    @Override
    public void photoSuccess(String path, File file, int... queue) {
        if (photoType == 3) {
            Glide.with(aty.getApplicationContext())
                    .load(path)
                    .asBitmap()
                    .placeholder(R.drawable.home_head_img_icon)
                    .centerCrop().into(mBinding.imgHead);
            this.headimg = path;
        }
    }

    @Override
    public void photoFaild() {
        Toast.makeText(MaiLiApplication.getInstance(), "图片加载失败！", Toast.LENGTH_SHORT).show();
    }

    //昵称
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (nameRequest == requestCode && RESULT_OK == resultCode && data != null) {
            mBinding.tvName.setText(TextUtils.isEmpty(data.getStringExtra("name")) ? "未知" : data.getStringExtra("name"));
        }
    }

    //请求数据返回
    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            if (stateModel.getEmptyState() == EmptyState.PROGRESS) {
                stateModel.setErrorType(errorModel.getStatus());
            }

            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof UserInfoModel) {
            if (parms.length > 0) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (type == 0) {
                            MaiLiApplication.backToLogin(aty, new Intent(aty, MainActivity.class));
                        }
                        finish();
                    }
                }.start();
                Toast.makeText(MaiLiApplication.getInstance(), "用户信息完善成功!", Toast.LENGTH_SHORT).show();
            } else {
                UserInfoModel info = (UserInfoModel) object;
                setUserInfo(info);
                stateModel.setEmptyState(EmptyState.NORMAL);

            }
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    //
    private void setUserInfo(UserInfoModel info) {
        if (info == null || info.getUserInfo() == null) {
            return;
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getHeadUrl())) {
            Glide.with(aty.getApplicationContext()).load(info.getUserInfo().getHeadUrl())
                    .asBitmap().centerCrop()
                    .placeholder(R.drawable.home_head_img_icon).into(mBinding.imgHead);
        }

        if (!TextUtils.isEmpty(info.getUserInfo().getNickname())) {
            mBinding.tvName.setText(info.getUserInfo().getNickname());
        }

        if (!TextUtils.isEmpty(info.getUserInfo().getIdType())) {
            mBinding.tvOccupationChoose.setText(info.getUserInfo().getIdType());
        }

        if (!TextUtils.isEmpty(info.getUserInfo().getCity())) {
            mBinding.tvCity.setText(info.getUserInfo().getCity());
        }


        if (!TextUtils.isEmpty(info.getUserInfo().getSex())) {
            if (info.getUserInfo().getSex().equals("男")) {
                mBinding.tvSex.setText("男");
            } else {
                mBinding.tvSex.setText("女");
            }
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getIdType())) {
            mBinding.tvOccupationChoose.setText(info.getUserInfo().getIdType());
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getHeadUrl())) {
            headimg = info.getUserInfo().getHeadUrl();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateData(MessageEvent me) {
        if ("认证更新".equals(me.getMessage())) {
            MaiLiApplication.getInstance().getUserInfo().setCheckStatus(1);
            EventBus.getDefault().post(MaiLiApplication.getInstance().getUser());
            mBinding.rlyRenzhen.setVisibility(View.GONE);
            mBinding.tvState.setText("已实名认证");
            mBinding.tvState.setBackgroundColor(getResources().getColor(R.color.colorFFFFFF));
            mBinding.tvState.setTextColor(getResources().getColor(R.color.colorFF6C00));
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        LocationHelper.getInstance().closeLocation();
        super.onDestroy();
    }
}
