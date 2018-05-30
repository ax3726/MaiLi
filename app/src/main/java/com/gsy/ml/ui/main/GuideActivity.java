package com.gsy.ml.ui.main;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityGuideLayoutBinding;
import com.gsy.ml.ui.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity {
    private ActivityGuideLayoutBinding mBinding;
    private List<Integer> mImageList=new ArrayList<>();
    private GuideAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide_layout;
    }

    @Override
    public void initData() {
        super.initData();
        mBinding= (ActivityGuideLayoutBinding) vdb;
        mImageList.add(R.drawable.guide_icon);
//        mImageList.add(R.drawable.guide_icon);
//        mImageList.add(R.drawable.guide_icon);
//        mImageList.add(R.drawable.guide_icon);
        mAdapter=new GuideAdapter();
        mBinding.vpGuide.setAdapter(mAdapter);
    }
    class GuideAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView=new ImageView(aty);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(mImageList.get(position));
            if (position==mImageList.size()-1) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(aty,LoginActivity.class));
                        finish();
                    }
                });
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);//删除页卡   示例：提交
        }
    }
}
