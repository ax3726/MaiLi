package com.gsy.ml.ui.common;

import android.databinding.DataBindingUtil;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityPhotoPreviewBinding;
import com.gsy.ml.databinding.ItemPhotoPreviewBinding;

import java.util.ArrayList;
import java.util.List;

import ml.gsy.com.library.utils.Utils;


public class PhotoPreviewActivity extends BaseActivity implements View.OnClickListener {

    private ActivityPhotoPreviewBinding mBinding;
    private RequestManager requestManager;
    private List<String> mDataList=new ArrayList<>();
    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_preview;
    }

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.llyLeft.setOnClickListener(this);
    }

    private int mPosition=0;
    @Override
    public void initData() {
        super.initData();
        mBinding= (ActivityPhotoPreviewBinding) vdb;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, (int) Utils.dip2px(aty,50));
            layoutParams.setMargins(0,(int) Utils.dip2px(aty,25),0,0);
            mBinding.llColor.setLayoutParams(layoutParams);
        }

        mDataList =  getIntent().getStringArrayListExtra("imglist");
        if (mDataList==null) {
            mDataList=new ArrayList<>();
        }
        String img = getIntent().getStringExtra("img");
        if (!TextUtils.isEmpty(img)) {
            mDataList.add(img);
        }
        mPosition = getIntent().getIntExtra("position",0);


        if (mDataList==null) {
            mDataList=new ArrayList<>();
        }

    }

    @Override
    public void initWidget() {
        super.initWidget();
        requestManager = Glide.with(aty);
        requestManager.onLowMemory();
        mBinding.vpPhoto.setAdapter(new MyAdapter());
    }



    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.lly_left:
                finish();
                break;

        }
    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final ItemPhotoPreviewBinding binding= DataBindingUtil.inflate(getLayoutInflater(),R.layout.item_photo_preview,null,false);
            binding.img.enable();
            DrawableTypeRequest<String> load = requestManager.load(mDataList.get(position));
            load.listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                    binding.prImg.setVisibility(View.GONE);
                    Toast.makeText(MaiLiApplication.appliactionContext,"图片加载失败！",Toast.LENGTH_SHORT).show();
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                    binding.prImg.setVisibility(View.GONE);

                    return false;
                }
            }).thumbnail(0.9f)
                    .into(binding.img);
            container.addView(binding.getRoot());
            return binding.getRoot();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
