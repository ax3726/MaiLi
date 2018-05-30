package com.gsy.ml.ui.home;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityChooseAddressBinding;
import com.gsy.ml.model.common.CityModel;
import com.gsy.ml.model.main.UserInfoModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.utils.ProvinceAreaHelper;
import com.gsy.ml.ui.views.QuicLocationBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ml.gsy.com.library.adapters.abslistview.CommonAdapter;
import ml.gsy.com.library.adapters.abslistview.ViewHolder;

public class ChooseAddressActivity extends BaseActivity implements PoiSearch.OnPoiSearchListener, LocationHelper.ILocationListener, LocationSource, GeocodeSearch.OnGeocodeSearchListener {

    private ActivityChooseAddressBinding mBinding;
    private CommonAdapter<CityModel> mAdapter;
    private List<CityModel> mCistys = new ArrayList<>();
    private HashMap<String, Integer> alphaIndexer;
    private ProvinceAreaHelper mProvinceAreaHelper;
    private List<PoiItem> mPois = new ArrayList<>();
    private ml.gsy.com.library.adapters.recyclerview.CommonAdapter<PoiItem> mPosadapter;
    private GeocodeSearch geocoderSearch; //地理编码
    private String mCity = "";

    @Override
    public int getLayoutId() {
        return R.layout.activity_choose_address;
    }

    private boolean isResult = false;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityChooseAddressBinding) vdb;
        mCity = MaiLiApplication.getInstance().getUserPlace().getCity();
        mBinding.tvCity.setText(mCity);
        mBinding.imgStartLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationHelper.getInstance().startLocation(aty);
            }
        });
        mBinding.etSearch.requestFocus();
        initCitys();
        initMapView();
        initLocationSearch();
        initSearchData();
        LocationHelper.getInstance().setILocationListener(this);
        LocationHelper.getInstance().startLocation(aty);
        initSearchGood();
        mBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initLocationSearch();
            }
        });
        mBinding.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.llyLocation.setVisibility(View.GONE);
                mBinding.flyCitys.setVisibility(View.VISIBLE);
            }
        });

        mBinding.llyLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initCitys() {
        mProvinceAreaHelper = new ProvinceAreaHelper(aty);
        mProvinceAreaHelper.initProvinceData();
        mCistys = mProvinceAreaHelper.getCitys();
        sort();
        mAdapter = new CommonAdapter<CityModel>(aty, R.layout.item_city, mCistys) {
            @Override
            protected void convert(ViewHolder viewHolder, CityModel item, final int position) {
                TextView name = viewHolder.getView(R.id.item_city_name);
                TextView alpha = viewHolder.getView(R.id.item_city_alpha);
                name.setText(mCistys.get(position).getName());
                String currentStr = mCistys.get(position).getSort_name();
                String previewStr = (position - 1) >= 0 ? mCistys.get(position - 1)
                        .getSort_name() : " ";
                if (!previewStr.equals(currentStr)) {
                    alpha.setVisibility(View.VISIBLE);
                    alpha.setText(currentStr);
                } else {
                    alpha.setVisibility(View.GONE);
                }
                name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBinding.flyCitys.setVisibility(View.GONE);
                        mBinding.llyLocation.setVisibility(View.VISIBLE);
                        mCity = mCistys.get(position).getName();
                        mBinding.tvCity.setText(mCity);
                    }
                });
            }
        };
        mBinding.lvCity.setAdapter(mAdapter);
        mBinding.cityLoactionbar.setOnTouchLitterChangedListener(new LetterListViewListener());
    }

    //初始化地图控制器对象+
    AMap aMap;

    private void initMapView() {
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mBinding.mv.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mBinding.mv.getMap();
        }
        aMap.setLocationSource(this);
        aMap.setMinZoomLevel(10);
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoPosition(AMapOptions.ZOOM_POSITION_RIGHT_BUTTOM);
        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    LatLng latLng = aMap.getProjection().fromScreenLocation(new Point((int) mBinding.ivLocation.getX(), (int) mBinding.ivLocation.getY()));
                    poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(latLng.latitude, latLng.longitude), 500));//设置周边搜索的中心点以及半径
                    poiSearch.searchPOIAsyn();
                    geocoderSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(latLng.latitude, latLng.longitude), 100, "autonavi"));//逆向地理
                    isResult = false;
                }
            }
        });

        UserInfoModel.UserPlaceBean userPlace = MaiLiApplication.getInstance().getUserPlace();
        if (!TextUtils.isEmpty(userPlace.getJing()) && !TextUtils.isEmpty(userPlace.getWei())) {
            setLocation(Float.valueOf(userPlace.getWei()), Float.valueOf(userPlace.getJing()));

        }
    }

    private int currentPage = 1;
    PoiSearch poiSearch;

    private void initLocationSearch() {
        PoiSearch.Query query = new PoiSearch.Query(mBinding.etSearch.getText().toString().trim(), "", mCity);
/**
 * keyWord表示搜索字符串，
 * 第二个参数表示POI搜索类型，二者选填其一，
 * POI搜索类型共分为以下20种：汽车服务|汽车销售|
 * 汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|
 * 住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|
 * 金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施
 * cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
 **/
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);//设置查询页码

        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(this);
        poiSearch.searchPOIAsyn();

    }

    private void initSearchData() {
        mPosadapter = new ml.gsy.com.library.adapters.recyclerview.CommonAdapter<PoiItem>(aty, R.layout.iten_search_result, mPois) {
            @Override
            protected void convert(ml.gsy.com.library.adapters.recyclerview.base.ViewHolder holder, final PoiItem poiItem, int position) {
                TextView tv_now = holder.getView(R.id.tv_now);
                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_add_info = holder.getView(R.id.tv_add_info);
                LinearLayout lly_content = holder.getView(R.id.lly_content);
                if (position == 0) {
                    tv_now.setVisibility(View.VISIBLE);
                    tv_add_info.setText(poiItem == null ? "" : poiItem.getAdName());
                } else {
                    tv_now.setVisibility(View.GONE);
                    String provinceName = TextUtils.isEmpty(poiItem.getProvinceName())?"":poiItem.getProvinceName();
                    String cityName = TextUtils.isEmpty(poiItem.getCityName())?"":poiItem.getCityName();
                    String AdName = TextUtils.isEmpty(poiItem.getAdName())?"":poiItem.getAdName();
                    tv_add_info.setText(provinceName+ cityName+ AdName+ poiItem.getSnippet());
                }

                tv_title.setText(poiItem == null ? "" : poiItem.getTitle());
                lly_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setResult(RESULT_OK, new Intent().putExtra("data", poiItem));
                        finish();
                    }
                });

            }
        };
        mBinding.rcAddList.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcAddList.setAdapter(mPosadapter);
    }

    private void initSearchGood() {
        //地理编码事件监听
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }


    public void sort() {
        // 排序
        Collections.sort(mCistys, new Comparator<CityModel>() {
            @Override
            public int compare(CityModel lhs, CityModel rhs) {
                if (lhs.getSort_name().equals(rhs.getSort_name())) {
                    return lhs.getName().compareTo(rhs.getName());
                } else {
                    if ("#".equals(lhs.getSort_name())) {
                        return 1;
                    } else if ("#".equals(rhs.getSort_name())) {
                        return -1;
                    }
                    return lhs.getSort_name().compareTo(rhs.getSort_name());
                }
            }
        });
        alphaIndexer = new HashMap<String, Integer>();
        for (int i = 0; i < mCistys.size(); i++) {
            String currentStr = mCistys.get(i).getSort_name();
            String previewStr = (i - 1) >= 0 ? mCistys.get(i - 1).getSort_name()
                    : " ";
            if (!previewStr.equals(currentStr)) {//前一个首字母与当前首字母不同时加入HashMap中同时显示该字母
                String name = mCistys.get(i).getSort_name();
                alphaIndexer.put(name, i);

            }
        }
    }

    private PoiResult poiResult;

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        poiResult = result;

        mPois.clear();

        if (mPois == null) {
            mPois = new ArrayList<>();
        }
        mPosadapter.notifyDataSetChanged();
        if (isResult) {
            mPois.add(poiItem);

        }
        mPois.addAll(poiResult.getPois());
        isResult = true;
    }


    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        isResult = false;
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        setLocation(lat, lon);
        poiSearch.setBound(new PoiSearch.SearchBound(new LatLonPoint(lat, lon), 500));//设置周边搜索的中心点以及半径
        poiSearch.searchPOIAsyn();
        geocoderSearch.getFromLocationAsyn(new RegeocodeQuery(new LatLonPoint(lat, lon), 100, "autonavi"));//逆向地理
    }

    private void setLocation(double lat, double lon) {
        // 设置当前地图显示为当前位置
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17));
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }

    private PoiItem poiItem;

    //地理逆编码回调函数（坐标转地址）
    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {


        String title = "";
        if (!TextUtils.isEmpty(regeocodeResult.getRegeocodeAddress().getNeighborhood())) {
            title = regeocodeResult.getRegeocodeAddress().getNeighborhood();
        } else {
            title=regeocodeResult.getRegeocodeAddress().getStreetNumber().getStreet();
        }
        poiItem = new PoiItem("", null, title,
                regeocodeResult.getRegeocodeAddress().getStreetNumber().getStreet() + regeocodeResult.getRegeocodeAddress().getStreetNumber().getNumber());
        String formatAddress = regeocodeResult.getRegeocodeAddress().getFormatAddress();
        poiItem.setAdName(formatAddress);
        if (isResult && !TextUtils.isEmpty(formatAddress)) {
            mPois.add(0, poiItem);
            mPosadapter.notifyDataSetChanged();
        }
        isResult = true;
    }

    GeocodeResult dd;

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
        dd = geocodeResult;
    }


    private class LetterListViewListener implements QuicLocationBar.OnTouchLetterChangedListener {

        @Override
        public void touchLetterChanged(String s) {
            // TODO Auto-generated method stub
            if (alphaIndexer.get(s) != null) {
                int position = alphaIndexer.get(s);
                mBinding.lvCity.setSelection(position);
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mBinding.mv.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mBinding.mv.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mBinding.mv.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mBinding.mv.onSaveInstanceState(outState);
    }
}
