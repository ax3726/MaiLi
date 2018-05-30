package com.gsy.ml.ui.person;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gsy.ml.R;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityResumeLayoutBinding;
import com.gsy.ml.model.common.HttpErrorModel;
import com.gsy.ml.model.common.HttpSuccessModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.model.person.EducationModel;
import com.gsy.ml.model.person.ResumeModel;
import com.gsy.ml.prestener.common.ILoadPVListener;
import com.gsy.ml.prestener.person.ResumePresenter;
import com.gsy.ml.ui.common.PhotoActivity;
import com.gsy.ml.ui.common.PhotoPreviewActivity;
import com.gsy.ml.ui.utils.UpdataImgUtils;
import com.gsy.ml.ui.views.FullyGridLayoutManager;
import com.gsy.ml.ui.views.PopupOption;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;
import ml.gsy.com.library.utils.MyUUID;

/**
 * Created by Administrator on 2017/4/21.
 * 简历类
 */

public class ResumeActivity extends PhotoActivity implements View.OnClickListener, UpdataImgUtils.UpdataImgCallBack, ILoadPVListener {
    private ActivityResumeLayoutBinding mBinding;
    private List<String> mImages = new ArrayList<>();
    private CommonAdapter<String> mImageAdapter;
    private PopupOption mPopupOption;
    private UpdataImgUtils mUpdataImgUtils;
    private ResumePresenter mPresenter = new ResumePresenter(this);
    private EducationModel mEducationModel = new EducationModel("", "", "", "", "");


    @Override
    public int getLayoutId() {
        return R.layout.activity_resume_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityResumeLayoutBinding) vdb;
        initListener();
        initView();
        getData();
    }

    private void initListener() {
        mBinding.tvAddEducation.setOnClickListener(this);
    }

    @Override
    public void initActionBar() {
        super.initActionBar();

        mBinding.ilHead.tvTitle.setText("我的简历");
        mBinding.ilHead.tvRight.setText("保存");
        mBinding.ilHead.tvRight.setVisibility(View.VISIBLE);
        mBinding.ilHead.llyLeft.setOnClickListener(this);
        mBinding.ilHead.llyRight.setOnClickListener(this);

    }

    private int mWidth = 0;

    private int updataNum = 0;
    private int updataSusNum = 0;
    private boolean isupdataAllSus = true;
    private UserInfoModel.UserInfoBean userInfo;

    private void initView() {
        EventBus.getDefault().register(aty);
        userInfo = MaiLiApplication.getInstance().getUserInfo();
        mUpdataImgUtils = new UpdataImgUtils(this, aty);
        mWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        mImages.add("add");
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
                    doPhoto();
                } else {
                    pickphoto();
                }
            }
        });


        mImageAdapter = new CommonAdapter<String>(aty, R.layout.item_resume_add_img, mImages) {
            @Override
            protected void convert(ViewHolder holder, final String s, final int position) {
                ImageView img = holder.getView(R.id.img);
                ImageView img_del = holder.getView(R.id.img_del);
                FrameLayout fly_item = holder.getView(R.id.fly_item);

                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(mWidth / 5, mWidth / 5);

                fly_item.setLayoutParams(layoutParams);
                if (s.equals("add")) {
                    img_del.setVisibility(View.GONE);
                    Glide.with(aty.getApplicationContext()).load(R.drawable.add_photo_icon).asBitmap().placeholder(R.drawable.photo_error_icon).into(img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopupOption.showPopupWindow();
                        }
                    });

                } else {
                    img_del.setVisibility(View.VISIBLE);
                    Glide.with(aty.getApplicationContext()).load(s).asBitmap().placeholder(R.drawable.photo_error_icon).into(img);
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(aty, PhotoPreviewActivity.class).putExtra("img", s));
                        }
                    });
                }
                img_del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mImages.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }
        };
        mBinding.rcImg.setLayoutManager(new FullyGridLayoutManager(aty, 4));
        mBinding.rcImg.setAdapter(mImageAdapter);
    }

    private void getData() {
        mPresenter.getResume(userInfo.getPhone());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
            case R.id.lly_right:
                submitImg();
                break;
            case R.id.tv_add_education:
                startActivity(new Intent(aty, AddEducationActivity.class).putExtra("data", mEducationModel));
                break;
        }
    }

    @Override
    public void photoSuccess(String path, File file, int... queue) {
        mImages.add(mImages.size() - 1, path);
        mImageAdapter.notifyDataSetChanged();
    }

    @Override
    public void photoFaild() {
        Toast.makeText(MaiLiApplication.getInstance(), "图片加载失败！", Toast.LENGTH_SHORT).show();
    }

    private List<String> objectKey = new ArrayList<>();

    /**
     * 上传图片
     */
    private void submitImg() {
        boolean bl = false;
        for (String s : mImages) {
            if (!s.contains("http") && !s.equals("add")) {
                bl = true;
            }
        }
        if (bl) {
            objectKey.clear();
            List<String> mImgList = new ArrayList<>();
            for (String s : mImages) {
                if (!s.contains("http") && !s.equals("add")) {
                    objectKey.add(Link.CERTIFICATE + MyUUID.nolineUUID() + ".jpg");
                    mImgList.add(s);
                }
            }
            isupdataAllSus = true;
            updataNum = mImgList.size();
            updataSusNum = 0;
            showWaitDialog();
            mUpdataImgUtils.updataImgs(Link.bucketName, objectKey, mImgList);
        } else {
            showWaitDialog();
            submitData();
        }
    }

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
            submitData();
            isupdataAllSus = false;
        }
    }

    /**
     * 提交数据
     */
    private void submitData() {
        String imgsList = "";
        List<String> images = new ArrayList<>();
        for (int i = 0; i < mImages.size(); i++) {
            if (mImages.get(i).contains("http")) {
                images.add(mImages.get(i));
            }
        }
        images.addAll(objectKey);
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).contains("http")) {
                imgsList = imgsList + images.get(i) + ",";
            } else {
                imgsList = imgsList + Link.ALIIMAGEURL + images.get(i) + ",";
            }
        }
        String evaluate = mBinding.etEvaluate.getText().toString().trim();
        String hoppy = mBinding.etHoppy.getText().toString().trim();
        String workex = mBinding.etWorkEx.getText().toString().trim();

        mPresenter.updateResume(userInfo.getPhone(),
                TextUtils.isEmpty(evaluate) ? "" : evaluate,
                TextUtils.isEmpty(hoppy) ? "" : hoppy,
                mEducationModel.getName(),
                mEducationModel.getZhuanye(),
                mEducationModel.getXueli(),
                TextUtils.isEmpty(mEducationModel.getTime()) ? "" : mEducationModel.getTime(),
                mEducationModel.getContent(),
                TextUtils.isEmpty(imgsList) ? "" : imgsList,
                TextUtils.isEmpty(workex) ? "" : workex
        );
    }

    @Override
    public void onLoadComplete(Object object, int... parms) {
        hideWaitDialog();
        if (object instanceof HttpErrorModel) {
            HttpErrorModel errorModel = (HttpErrorModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), errorModel.getinfo(), Toast.LENGTH_SHORT).show();
        } else if (object instanceof HttpSuccessModel) {
            HttpSuccessModel info = (HttpSuccessModel) object;
            Toast.makeText(MaiLiApplication.getInstance(), info.getInfo(), Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }.start();
        } else if (object instanceof ResumeModel) {
            ResumeModel info = (ResumeModel) object;
            setView(info);
        }
    }

    @Override
    public void onLoadComplete(Object object, Class itemClass) {

    }

    private void setView(ResumeModel info) {
        if (info == null || info.getData() == null) {
            return;
        }
        ResumeModel.DataBean data = info.getData();

        mBinding.etEvaluate.setText(data.getUserInfo().getPersonalEvaluation());
        mBinding.etHoppy.setText(data.getUserInfo().getPersonalHobbies());
        mBinding.etWorkEx.setText(data.getUserInfo().getWorkExp());


        if (!TextUtils.isEmpty(data.getUserInfo().getCertificate())) {
            String[] split = data.getUserInfo().getCertificate().split(",");
            if (split.length > 0) {
                mImages.clear();
                for (String s : split) {
                    if (!TextUtils.isEmpty(s)) {
                        mImages.add(s);
                    }
                }
                mImages.add("add");
            }
        }
        String name = TextUtils.isEmpty(data.getUserInfo().getGraduateSchool()) ? "" : data.getUserInfo().getGraduateSchool();
        String xueli = TextUtils.isEmpty(data.getUserInfo().getQualifications()) ? "" : data.getUserInfo().getQualifications();
        String zhuanye = TextUtils.isEmpty(data.getUserInfo().getMajor()) ? "" : data.getUserInfo().getMajor();
        String time = TextUtils.isEmpty(data.getUserInfo().getSchoolTime()) ? "" : data.getUserInfo().getSchoolTime().replace(",", "-");
        String content = TextUtils.isEmpty(data.getUserInfo().getSchoolLife()) ? "" : data.getUserInfo().getSchoolLife();
        mEducationModel.setContent(content);
        mEducationModel.setName(name);
        mEducationModel.setZhuanye(zhuanye);
        mEducationModel.setXueli(xueli);
        mEducationModel.setTime(data.getUserInfo().getSchoolTime());
        content = TextUtils.isEmpty(content) ? "" : "\n学校经历:" + content;
        mBinding.tvEducation.setText(name + "\t\t" + time + "\t\t" + zhuanye + "\t\t" + xueli + content);

        if (TextUtils.isEmpty(mBinding.tvEducation.getText().toString().trim())) {
            mBinding.tvEducation.setVisibility(View.GONE);
        } else {
            mBinding.tvEducation.setVisibility(View.VISIBLE);
        }

        mImageAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEducation(EducationModel educationModel) {
        mEducationModel = educationModel;
        String name = educationModel.getName();
        String xueli = educationModel.getXueli();
        String zhuanye = educationModel.getZhuanye();
        String time = educationModel.getTime().replace(",", "-");
        String content = educationModel.getContent();
        content = TextUtils.isEmpty(content) ? "" : "\n学校经历:" + content;
        mBinding.tvEducation.setVisibility(View.VISIBLE);
        mBinding.tvEducation.setText(name + "\t\t" + time + "\t\t" + zhuanye + "\t\t" + xueli + content);
    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(aty);
        super.onDestroy();
    }
}
