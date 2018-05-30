package com.gsy.ml.ui.person;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.gsy.ml.R;
import com.gsy.ml.databinding.ActivityLocationInfoBinding;
import com.gsy.ml.model.main.JingWeiModel;
import com.gsy.ml.model.person.SelectProgressModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.common.EmptyState;
import com.gsy.ml.ui.common.StateModel;
import com.gsy.ml.ui.utils.SocaketUtils;

import ml.gsy.com.library.utils.ParseJsonUtils;

public class LocationInfoActivity extends BaseActivity {


    private ActivityLocationInfoBinding mBinding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_location_info;
    }

    private StateModel stateModel = new StateModel();

    @Override
    public void initActionBar() {
        super.initActionBar();
        mBinding.inHead.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBinding.inHead.tvTitle.setText("查看位置");
    }

    float zoom = 0;
    private String mPhone = "";
    private Marker marker;
    SelectProgressModel mSelectProgressModel;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityLocationInfoBinding) vdb;
        mSelectProgressModel = (SelectProgressModel) getIntent().getSerializableExtra("data");
        if (mSelectProgressModel != null) {
            mPhone = mSelectProgressModel.getData().getAcceptPhone();
        }
        stateModel.setEmptyState(EmptyState.PROGRESS);
        mBinding.setStateModel(stateModel);
        initLocation(savedInstanceState);
        SocaketUtils.getInstance().setISocaketListener(mISocaketListener);
        mHandler.sendEmptyMessage(0);

    }

    SocaketUtils.ISocaketListener mISocaketListener = new SocaketUtils.ISocaketListener() {
        @Override
        public void onSuccess() {//socaket连接成功 开启连续定位

        }

        @Override
        public void onLocation(JingWeiModel jingWeiModel) {//读取到位置信息
            if (aty == null) {
                return;
            }

            stateModel.setEmptyState(EmptyState.NORMAL);

            if (mSelectProgressModel == null) {
                return;
            }
            LatLng latLngji = new LatLng(Double.valueOf(mSelectProgressModel.getData().getStartWei()),
                    Double.valueOf(mSelectProgressModel.getData().getStartJing()));
            LatLngBounds.Builder include = new LatLngBounds.Builder()
                    .include(latLngji);
            String workJing = mSelectProgressModel.getData().getWorkJing();
            String workWei = mSelectProgressModel.getData().getWorkWei();
            String[] workWei_split = workWei.split(",");
            String[] workJing_split = workJing.split(",");
            LatLng lat11 = null;
            if (workWei_split != null && workWei_split.length > 0) {
                lat11 = new LatLng(Double.valueOf(workWei_split[workWei_split.length - 1]),
                        Double.valueOf(workJing_split[workJing_split.length - 1]));
                if (workWei_split.length == 1) {
                    LatLng lat = new LatLng(Double.valueOf(workWei_split[0]),
                            Double.valueOf(workJing_split[0]));
                    include.include(lat);
                    aMap.addMarker((new MarkerOptions())
                            .position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_add_cion))
                    );

                } else {

                    for (int i = 0; i < workWei_split.length; i++) {
                        LatLng lat = new LatLng(Double.valueOf(workWei_split[i]),
                                Double.valueOf(workJing_split[i]));
                        include.include(lat);
                        if (i == 0) {
                            aMap.addMarker((new MarkerOptions())
                                    .position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_add1_cion))
                            );
                        } else if (i == 1) {
                            aMap.addMarker((new MarkerOptions())
                                    .position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_add2_cion))
                            );

                        } else if (i == 2) {
                            aMap.addMarker((new MarkerOptions())
                                    .position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_add3_cion))
                            );

                        } else if (i == 3) {
                            aMap.addMarker((new MarkerOptions())
                                    .position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_add4_cion))
                            );

                        } else if (i == 4) {
                            aMap.addMarker((new MarkerOptions())
                                    .position(lat).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_add5_cion))
                            );

                        }
                    }


                }
            }
            // lude来设置可视区域
            LatLngBounds bounds = include.build();

            Double wei = TextUtils.isEmpty(jingWeiModel.getWei()) ? Double.valueOf(mSelectProgressModel.getData().getStartWei()) : Double.valueOf(jingWeiModel.getWei());
            Double jing = TextUtils.isEmpty(jingWeiModel.getJing()) ? Double.valueOf(mSelectProgressModel.getData().getStartJing()) : Double.valueOf(jingWeiModel.getJing());
            LatLng latLng = new LatLng(wei, jing);
            // 移动地图，所有latlng自适应显示。LatLngBounds与地图边缘10像素的填充区域
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

            if (lat11 != null) {

                if (zoom > 0) {
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
                } else {
                    float zoomToSpanLevel = aMap.getZoomToSpanLevel(latLng, lat11);
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomToSpanLevel - 1));
                }

            }

            if (marker != null) {
                marker.remove();

            }
            ImageView imageView = new ImageView(aty);
            imageView.setImageResource(R.drawable.location_song_icon);
            marker = aMap.addMarker((new MarkerOptions())
                    .position(latLng).icon(BitmapDescriptorFactory.fromView(imageView))
            );
            aMap.addMarker((new MarkerOptions())
                    .position(latLngji).icon(BitmapDescriptorFactory.fromResource(R.drawable.location_ji_icon))
            );


        }
    };
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    String str = ParseJsonUtils.getjsonStr(new JingWeiModel(mPhone));
                    SocaketUtils.getInstance().sendMsg(str);
                }
            }.start();
            postDelayed(mRunnable, 10000);
        }
    };

    //初始化地图控制器对象+
    private AMap aMap;

    private void initLocation(Bundle savedInstanceState) {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mvLoaction.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mvLoaction.getMap();
        }

        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    mBinding.mvLoaction.getParent().requestDisallowInterceptTouchEvent(false);

                } else {
                    mBinding.mvLoaction.getParent().requestDisallowInterceptTouchEvent(true);
                }

            }
        });
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                zoom = cameraPosition.zoom;
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(mRunnable);

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mvLoaction.onDestroy();
        super.onDestroy();

    }


    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mvLoaction.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mvLoaction.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mvLoaction.onSaveInstanceState(outState);
    }
}
