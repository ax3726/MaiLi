package com.gsy.ml.ui.usercard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.zxing.WriterException;
import com.gsy.ml.R;
import com.gsy.ml.common.Link;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.FragmentUserCardLayoutBinding;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.BaseFragment;
import com.gsy.ml.ui.person.IdentityCardActivity;
import com.gsy.ml.ui.utils.QrUtils;

/**
 * Created by Administrator on 2017/4/18.
 * 名片类
 */

public class UserCardFragment extends BaseActivity implements View.OnClickListener {

    private FragmentUserCardLayoutBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_card_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding = (FragmentUserCardLayoutBinding) vdb;
        mBinding.inHead.tvTitle.setText("我的名片");
        mBinding.inHead.llyLeft.setOnClickListener(this);
        mBinding.imgHead.setOnClickListener(this);
        initUser();
    }

    private UserInfoModel.UserInfoBean userInfo;

    public void initUser() {
        if (mBinding == null) {
            return;
        }

        userInfo = MaiLiApplication.getInstance().getUserInfo();
//        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
//        mBinding.tvCity.setText(userPlace.getCity() + userPlace.getArea());

        Glide.with(MaiLiApplication.appliactionContext).load(userInfo.getHeadUrl()).asBitmap()
                .placeholder(R.drawable.home_head_img_icon)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        mBinding.imgHead.setImageBitmap(resource);
                        getQR_code(mBinding.imgBusQrCode, resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        Bitmap bitmap = BitmapFactory.decodeResource(aty.getResources(), R.drawable.home_head_img_icon);
                        getQR_code(mBinding.imgBusQrCode, bitmap);
                    }
                });

        switch (userInfo.getStar()) {
            case 1:
                mBinding.imgStar.setImageResource(R.drawable.stars_one1_icon);
                break;
            case 2:
                mBinding.imgStar.setImageResource(R.drawable.stars_two2_icon);
                break;
            case 3:
                mBinding.imgStar.setImageResource(R.drawable.stars_three3_icon);
                break;
            case 4:
                mBinding.imgStar.setImageResource(R.drawable.stars_four4_icon);
                break;
            case 5:
                mBinding.imgStar.setImageResource(R.drawable.stars_five5_icon);
                break;
        }
        mBinding.tvName.setText(TextUtils.isEmpty(userInfo.getNickname()) ? "未知" : userInfo.getNickname());
    }

    /**
     * 生成二维码
     *
     * @param imageView
     * @param bitmap
     */
    private void getQR_code(ImageView imageView, Bitmap bitmap) {
        try {
            String contents = Link.SEREVE + "web/shareRequest.shtml?phone=" + userInfo.getPhone();
            //Bitmap bm=  qr_code(contents, BarcodeFormat.QR_CODE);
            Bitmap bm = QrUtils.createCode(contents, bitmap);
            imageView.setImageBitmap(bm);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lly_left:
                finish();
                break;
        }
    }
}
