package com.gsy.ml.ui.main;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityInformationLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.main.InformationPresenter;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.PhotoActivity;
import com.gsy.ml.ui.common.PhotoPreviewActivity;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.DataUtils;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.utils.UpdataImgUtils;
import com.gsy.ml.ui.views.OccupationDialog;
import com.gsy.ml.ui.views.PopupOption;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ml.gsy.com.library.utils.MyUUID;
import ml.gsy.com.library.utils.Utils;

import static com.gsy.ml.R.id.btn_completeness;

/**
 * Created by Administrator on 2017/4/13.
 */

public class InformationAction extends PhotoActivity implements View.OnClickListener,
        com.gsy.ml.ui.views.OccupationDialog.IDialogClickListener, ILoadPVListener, UpdataImgUtils.UpdataImgCallBack, LocationHelper.ILocationListener

{
    private ActivityInformationLayoutBinding informationLayoutBinding;
    private OccupationDialog mOccupationDialog;
    private PopupOption mPopupOption;
    private int photoType = 1;
    private String headimg = "";//头像
    private String zhenimg = "";//身份证正面
    private String fanimg = "";//身份证反面
    private String shouimg = "";//手持身份证
    private InformationPresenter mPresenter = new InformationPresenter(this);
    private UpdataImgUtils mUpdataImgUtils;
    private UserInfoModel.UserInfoBean user;
    private StateModel stateModel = new StateModel();

    @Override
    public int getLayoutId() {
        return R.layout.activity_information_layout;

    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        informationLayoutBinding.ilHead.tvTitle.setText("完善信息");
        informationLayoutBinding.ilHead.llyLeft.setOnClickListener(this);
    }


    private HashMap<String, String> objectKey = new HashMap<>();
    private int type = 0;
    private boolean is_head = false, is_zhen = false, is_fan = false, is_shou = false;

    @Override
    public void initData() {
        super.initData();
        informationLayoutBinding = (ActivityInformationLayoutBinding) vdb;
        type = getIntent().getIntExtra("type", 0);


        user = MaiLiApplication.getInstance().getUserInfo();
        if (user.getCheckStatus() == 1
               ) {
            informationLayoutBinding.btnCompleteness.setVisibility(View.GONE);
        }
        initView();
        LocationHelper.getInstance().setILocationListener(this);

        initListener();
        if (type == 1) {
            stateModel.setEmptyState(EmptyState.PROGRESS);
            informationLayoutBinding.setStateModel(stateModel);

            mPresenter.getUserInfo(user.getPhone());

            stateModel.setIOnClickListener(new StateModel.IOnClickListener() {
                @Override
                public void click(View view) {
                    stateModel.setEmptyState(EmptyState.PROGRESS);
                    mPresenter.getUserInfo(user.getPhone());
                }
            });
        } else {
            LocationHelper.getInstance().startLocation(aty);
        }
    }

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
    }

    private void initListener() {
        informationLayoutBinding.tvOccupationChoose.setOnClickListener(this);
        informationLayoutBinding.imgZhen.setOnClickListener(this);
        informationLayoutBinding.imgFan.setOnClickListener(this);
        informationLayoutBinding.imgHead.setOnClickListener(this);
        informationLayoutBinding.imgShou.setOnClickListener(this);
        informationLayoutBinding.rbMan.setChecked(true);

        informationLayoutBinding.btnCompleteness.setOnClickListener(this);
        informationLayoutBinding.llyBody.setOnClickListener(this);
        informationLayoutBinding.tvCity.setOnClickListener(this);
    }


    private int updataNum = 0;
    private int updataSusNum = 0;
    private boolean isupdataAllSus = true;

    /**
     * 上传图片
     */
    private void submitImg() {

        String nick_name = informationLayoutBinding.etNickName.getText().toString().trim();
        String name = informationLayoutBinding.etName.getText().toString().trim();
        String man_id = informationLayoutBinding.etManId.getText().toString().trim();
        String city = informationLayoutBinding.tvCity.getText().toString().trim();
        if (TextUtils.isEmpty(nick_name)) {
            Toast.makeText(aty, "昵称不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(aty, "姓名不能为空!", Toast.LENGTH_SHORT).show();
            return;
        }

        String man = "";
        try {
            man = DataUtils.IDCardValidate(man_id);
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(aty, "您输入的身份证信息有误!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.isEmpty(man)) {
            Toast.makeText(aty, man, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(headimg)) {
            Toast.makeText(aty, "请上传头像!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(zhenimg)) {
            Toast.makeText(aty, "请上传身份证正面照片!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(fanimg)) {
            Toast.makeText(aty, "请上传身份证反面照片!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(shouimg)) {
            Toast.makeText(aty, "请上传手持身份证照片!", Toast.LENGTH_SHORT).show();
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
        if (!zhenimg.contains("http")) {
            objectKey.put("2", Link.ISNUMBERPHOTOURL + MyUUID.nolineUUID() + ".jpg");
            mImgList.add(zhenimg);
            imglenth++;
            object.add(objectKey.get("2"));
        }
        if (!fanimg.contains("http")) {
            objectKey.put("3", Link.ISNUMBERPHOTOURL + MyUUID.nolineUUID() + ".jpg");
            mImgList.add(fanimg);
            imglenth++;
            object.add(objectKey.get("3"));
        }
        if (!shouimg.contains("http")) {
            objectKey.put("4", Link.ISNUMBERPHOTOURL + MyUUID.nolineUUID() + ".jpg");
            mImgList.add(shouimg);
            imglenth++;
            object.add(objectKey.get("4"));
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

    /**
     * 提交信息
     */
    private void getCheck() {
        String nick_name = informationLayoutBinding.etNickName.getText().toString().trim();
        String name = informationLayoutBinding.etName.getText().toString().trim();
        String man_id = informationLayoutBinding.etManId.getText().toString().trim();
        String city = informationLayoutBinding.tvCity.getText().toString().trim();
/*
        mPresenter.updateInfo(user.getPhone(),
                nick_name,
                name,
                informationLayoutBinding.rbMan.isChecked() ? "男" : "女",
                man_id,
                city,
                informationLayoutBinding.tvOccupationChoose.getText().toString().trim(),
                TextUtils.isEmpty(objectKey.get("1")) ? "" : objectKey.get("1"),
                TextUtils.isEmpty(objectKey.get("2")) ? "" : objectKey.get("2"),
                TextUtils.isEmpty(objectKey.get("3")) ? "" : objectKey.get("3"),
                TextUtils.isEmpty(objectKey.get("4")) ? "" : objectKey.get("4"),
                ""
        );*/
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_body:
                Utils.closeInputPad(aty);
                break;
            case R.id.tv_occupation_choose://身份选择
                mOccupationDialog.show();
                break;
            case R.id.img_zhen://身份证正面

                if (is_zhen) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class)
                            .putExtra("img", zhenimg));
                } else {
                    photoType = 1;
                    mPopupOption.showPopupWindow();
                }

                break;
            case R.id.img_fan://身份证反面

                if (is_fan) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class)
                            .putExtra("img", fanimg));
                } else {
                    photoType = 2;
                    mPopupOption.showPopupWindow();
                }
                break;
            case R.id.img_head://头像

                if (is_head) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class)
                            .putExtra("img", headimg));
                } else {
                    photoType = 3;
                    mPopupOption.showPopupWindow();
                }
                break;
            case R.id.img_shou://手持身份证
                if (is_shou) {
                    startActivity(new Intent(aty, PhotoPreviewActivity.class)
                            .putExtra("img", shouimg));
                } else {
                    photoType = 4;
                    mPopupOption.showPopupWindow();
                }
                break;
            case btn_completeness://完善信息
                submitImg();
                break;
            case R.id.tv_city:    //启动定位
                Toast.makeText(aty, "正在重新定位！", Toast.LENGTH_SHORT).show();
                LocationHelper.getInstance().startLocation(aty);
                break;
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onOk(int type) {
        switch (type) {
            case 1:
                informationLayoutBinding.tvOccupationChoose.setText("在校学生");
                break;
            case 2:
                informationLayoutBinding.tvOccupationChoose.setText("家庭主妇");
                break;
            case 3:
                informationLayoutBinding.tvOccupationChoose.setText("上班族");
                break;
            case 4:
                informationLayoutBinding.tvOccupationChoose.setText("自由职业");
                break;

        }
    }

    @Override
    public void photoSuccess(String path, File file, int... queue) {
        if (photoType == 1) {
            Glide.with(aty.getApplicationContext()).load(path).asBitmap().placeholder(R.drawable.photo_error_icon).centerCrop().into(informationLayoutBinding.imgZhen);
            this.zhenimg = path;
        } else if (photoType == 2) {
            Glide.with(aty.getApplicationContext()).load(path).asBitmap().placeholder(R.drawable.photo_error_icon).centerCrop().into(informationLayoutBinding.imgFan);
            this.fanimg = path;
        } else if (photoType == 3) {
            Glide.with(aty.getApplicationContext()).load(path).asBitmap().placeholder(R.drawable.photo_error_icon).centerCrop().into(informationLayoutBinding.imgHead);
            this.headimg = path;
        } else if (photoType == 4) {
            Glide.with(aty.getApplicationContext()).load(path).asBitmap().placeholder(R.drawable.photo_error_icon).centerCrop().into(informationLayoutBinding.imgShou);
            this.shouimg = path;
        }
        // Toast.makeText(aty,path,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void photoFaild() {
        Toast.makeText(aty, "图片加载失败！", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onUpdataImgResult(String key, boolean state) {
        if (!state) {
            isupdataAllSus = false;
            hideWaitDialog();
            Toast.makeText(aty, "图片上传失败", Toast.LENGTH_SHORT).show();
            mUpdataImgUtils.cancelUpdata();
        } else {
            updataSusNum++;
        }
        if (updataSusNum >= updataNum && isupdataAllSus) {
            getCheck();
            isupdataAllSus = false;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation == null || aMapLocation.getErrorCode() != 0) {
            Toast.makeText(aty, "定位失败,点击重新定位!", Toast.LENGTH_SHORT).show();
            return;
        }
        informationLayoutBinding.tvCity.setText(aMapLocation.getCity());
    }

    private void setUserInfo(UserInfoModel info) {
        if (info == null || info.getUserInfo() == null) {
            return;
        }
       /* if (!TextUtils.isEmpty(info.getUserInfo().getHeadUrl())) {
            Glide.with(aty.getApplicationContext()).load(info.getUserInfo().getHeadUrl()).asBitmap().centerCrop().into(informationLayoutBinding.imgHead);
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getIdcardBeforeUrl())) {
            Glide.with(aty.getApplicationContext()).load(info.getUserInfo().getIdcardBeforeUrl()).asBitmap().centerCrop().into(informationLayoutBinding.imgZhen);
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getIdcardAfterUrl())) {
            Glide.with(aty.getApplicationContext()).load(info.getUserInfo().getIdcardAfterUrl()).asBitmap().centerCrop().into(informationLayoutBinding.imgFan);
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getIdcardHoldUrl())) {
            Glide.with(aty.getApplicationContext()).load(info.getUserInfo().getIdcardHoldUrl()).asBitmap().centerCrop().into(informationLayoutBinding.imgShou);
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getCity())) {
            informationLayoutBinding.tvCity.setText(info.getUserInfo().getCity());
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getIdNumber())) {
            informationLayoutBinding.etManId.setText(info.getUserInfo().getIdNumber());
            informationLayoutBinding.etManId.setEnabled(false);
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getName())) {
            informationLayoutBinding.etName.setText(info.getUserInfo().getName());
            informationLayoutBinding.etName.setEnabled(false);
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getNickname())) {
            informationLayoutBinding.etNickName.setText(info.getUserInfo().getNickname());
            informationLayoutBinding.etNickName.setEnabled(false);
        }


        if (!TextUtils.isEmpty(info.getUserInfo().getSex())) {
            if (info.getUserInfo().getSex().equals("男")) {
                informationLayoutBinding.rbMan.setChecked(true);
            } else {
                informationLayoutBinding.rbWomen.setChecked(true);
            }
        }
        if (!TextUtils.isEmpty(info.getUserInfo().getIdType())) {
            informationLayoutBinding.tvOccupationChoose.setText(info.getUserInfo().getIdType());
        }

        if (info.getUserInfo().getIdcardBeforeUrlStatus() == 1
                && info.getUserInfo().getIdcardAfterUrlStatus() == 1
                && info.getUserInfo().getHeadUrlStatus() == 1
                && info.getUserInfo().getIdcardHoldUrlStatus() == 1) {
            informationLayoutBinding.btnCompleteness.setVisibility(View.GONE);
        } else {
            informationLayoutBinding.btnCompleteness.setVisibility(View.VISIBLE);

        }

        zhenimg = info.getUserInfo().getIdcardBeforeUrl();
        fanimg = info.getUserInfo().getIdcardAfterUrl();
        headimg = info.getUserInfo().getHeadUrl();
        shouimg = info.getUserInfo().getIdcardHoldUrl();

        if (info.getUserInfo().getIdcardBeforeUrlStatus() != 1) {
            if (!TextUtils.isEmpty(info.getUserInfo().getIdcardBeforeUrl())) {
                informationLayoutBinding.tvZhen.setVisibility(View.VISIBLE);
                if (info.getUserInfo().getIdcardBeforeUrlStatus() == 0) {
                    informationLayoutBinding.tvZhen.setText("正在审核...");

                } else {
                    informationLayoutBinding.tvZhen.setText("审核未通过");
                }
            }
        } else {

            is_zhen = true;
        }
        if (info.getUserInfo().getIdcardAfterUrlStatus() != 1) {
            if (!TextUtils.isEmpty(info.getUserInfo().getIdcardAfterUrl())) {
                informationLayoutBinding.tvFan.setVisibility(View.VISIBLE);
                if (info.getUserInfo().getIdcardAfterUrlStatus() == 0) {
                    informationLayoutBinding.tvFan.setText("正在审核...");

                } else {
                    informationLayoutBinding.tvFan.setText("审核未通过");
                }
            }
        } else {
            is_fan = true;

        }
        if (info.getUserInfo().getIdcardHoldUrlStatus() != 1) {

            if (!TextUtils.isEmpty(info.getUserInfo().getIdcardHoldUrl())) {
                informationLayoutBinding.tvShou.setVisibility(View.VISIBLE);
                if (info.getUserInfo().getIdcardHoldUrlStatus() == 0) {
                    informationLayoutBinding.tvShou.setText("正在审核...");

                } else {
                    informationLayoutBinding.tvShou.setText("审核未通过");
                }
            }
        } else {
            is_shou = true;
        }
        if (info.getUserInfo().getHeadUrlStatus() != 1) {
            if (!TextUtils.isEmpty(info.getUserInfo().getHeadUrl())) {
                informationLayoutBinding.tvHead.setVisibility(View.VISIBLE);
                if (info.getUserInfo().getIdcardHoldUrlStatus() == 0) {
                    informationLayoutBinding.tvHead.setText("正在审核...");

                } else {
                    informationLayoutBinding.tvHead.setText("审核未通过");
                }
            }
        } else {
            is_head = true;
        }*/


    }
}
