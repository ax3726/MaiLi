package com.gsy.ml.ui.home;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.gsy.ml.R;
import com.gsy.ml.common.MaiLiApplication;
import com.gsy.ml.databinding.ActivityEditAddressBinding;
import com.gsy.ml.model.common.AddressModel;
import com.gsy.ml.model.common.CityModel;
import com.gsy.ml.ui.common.BaseActivity;
import com.gsy.ml.ui.utils.AMapUtil;
import com.gsy.ml.ui.utils.LocationHelper;
import com.gsy.ml.ui.utils.ProvinceAreaHelper;
import com.gsy.ml.ui.utils.ToastUtil;
import com.gsy.ml.ui.views.QuicLocationBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ml.gsy.com.library.adapters.recyclerview.CommonAdapter;
import ml.gsy.com.library.adapters.recyclerview.base.ViewHolder;

public class EditAddressActivity extends BaseActivity implements Inputtips.InputtipsListener, GeocodeSearch.OnGeocodeSearchListener, LocationHelper.ILocationListener {


    private ActivityEditAddressBinding mBinding;
    private CommonAdapter<Tip> mAdapter;
    private ml.gsy.com.library.adapters.abslistview.CommonAdapter<CityModel> mCityAdapter;
    private List<Tip> mData = new ArrayList<>();
    private List<CityModel> mCistys = new ArrayList<>();
    private HashMap<String, Integer> alphaIndexer;
    private int mType = -1;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_address;
    }

    private String mCity = "";
    private String mProvince = "";
    private ProvinceAreaHelper mProvinceAreaHelper;
    private GeocodeSearch geocoderSearch; //地理编码
    private boolean is_edit = false;

    @Override
    public void initData() {
        super.initData();
        mBinding = (ActivityEditAddressBinding) vdb;
        mType = getIntent().getIntExtra("type", -1);
        is_edit = getIntent().getBooleanExtra("is_edit", false);
        if (mType == -2) {//导游
            mBinding.etSearch.setHint("景点名");
        } else {
            mBinding.etSearch.setHint("小区名/大厦名");
        }
        mCity = MaiLiApplication.getInstance().getUserPlace().getCity();
        mProvince = MaiLiApplication.getInstance().getUserPlace().getProvince();
        mBinding.tvCity.setText(mCity);
        mBinding.etSearch.addTextChangedListener(mTextWatcher);
        mBinding.tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.flyCitys.getVisibility() == View.VISIBLE) {
                    mBinding.flyCitys.setVisibility(View.GONE);
                    mBinding.rcContent.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
            }
        });
        initView();
        initCitys();
        initSearchGood();
        mBinding.tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.rcContent.setVisibility(View.GONE);
                mBinding.flyCitys.setVisibility(View.VISIBLE);
            }
        });
        if (MaiLiApplication.mAMapLocation != null) {
            LatLonPoint latLonPoint = new LatLonPoint(MaiLiApplication.mAMapLocation.getLatitude(), MaiLiApplication.mAMapLocation.getLongitude());
            String district = MaiLiApplication.mAMapLocation.getDistrict();
            String street = MaiLiApplication.mAMapLocation.getStreet() + MaiLiApplication.mAMapLocation.getStreetNum();
            String name = MaiLiApplication.mAMapLocation.getPoiName();
            Tip tip = new Tip();
            tip.setDistrict(district);
            tip.setName(name);
            tip.setAddress(street);
            tip.setPostion(latLonPoint);
            tip.setAdcode("当前");
            mData.clear();
            mData.add(tip);
            mAdapter.notifyDataSetChanged();

        } else {
            startLocation();
        }
    }

    private void startLocation() {
        LocationHelper.getInstance().setILocationListener(this);
        LocationHelper.getInstance().startLocation(aty);

    }


    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location == null || location.getErrorCode() != 0) {
            return;
        }
        LatLonPoint latLonPoint = new LatLonPoint(location.getLatitude(), location.getLongitude());
        mProvince = location.getProvince();
        mCity = location.getCity();
        String district = location.getDistrict();
        String street = location.getStreet() + location.getStreetNum();
        String name = location.getPoiName();
        Tip tip = new Tip();
        tip.setDistrict(district);
        tip.setName(name);
        tip.setAddress(street);
        tip.setPostion(latLonPoint);
        tip.setAdcode("当前");
        mData.clear();
        mData.add(tip);
        mAdapter.notifyDataSetChanged();
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
    private void searchPoint(String name) {
        // name表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode
        GeocodeQuery query = new GeocodeQuery(name, mCity);
        geocoderSearch.getFromLocationNameAsyn(query);
    }
    private void searchPoint1(LatLonPoint latLonPoint) {
        mLatLonPoint = latLonPoint;
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }

    private void initSearchGood() {
        //地理编码事件监听
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    private void initCitys() {
        mProvinceAreaHelper = new ProvinceAreaHelper(aty);

        mProvinceAreaHelper.initProvinceData();
        mCistys = mProvinceAreaHelper.getCitys();
        sort();
        mCityAdapter = new ml.gsy.com.library.adapters.abslistview.CommonAdapter<CityModel>(aty, R.layout.item_city, mCistys) {

            @Override
            protected void convert(ml.gsy.com.library.adapters.abslistview.ViewHolder viewHolder, CityModel item, final int position) {

                TextView name = viewHolder.getView(R.id.item_city_name);
                TextView alpha = viewHolder.getView(R.id.item_city_alpha);
                name.setText(mCistys.get(position).getName());
                String currentStr = mCistys.get(position).getSort_name();
                String previewStr = (position - 1) >= 0 ? mCistys.get(position - 1).getSort_name() : " ";
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
                        mBinding.rcContent.setVisibility(View.VISIBLE);
                        mCity = mCistys.get(position).getName();
                        mProvince = mCistys.get(position).getProvince();
                        mBinding.tvCity.setText(mCity);
                    }
                });
            }
        };
        mBinding.lvCity.setAdapter(mCityAdapter);
        mBinding.cityLoactionbar.setOnTouchLitterChangedListener(new LetterListViewListener());
    }

    private void initView() {
        mAdapter = new CommonAdapter<Tip>(aty, R.layout.iten_search_result, mData) {
            @Override
            protected void convert(ViewHolder holder, final Tip tip, int position) {
                TextView tv_now = holder.getView(R.id.tv_now);
                if ("当前".equals(tip.getAdcode())) {
                    tv_now.setVisibility(View.VISIBLE);
                } else {
                    tv_now.setVisibility(View.GONE);
                }

                TextView tv_title = holder.getView(R.id.tv_title);
                TextView tv_add_info = holder.getView(R.id.tv_add_info);
                LinearLayout lly_content = holder.getView(R.id.lly_content);
                tv_title.setText(TextUtils.isEmpty(tip.getName()) ? "" : tip.getName());

                tv_add_info.setText(TextUtils.isEmpty(tip.getAddress()) ? "" : tip.getAddress());
                lly_content.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String district = tip.getDistrict();
                        String newDistrict = district.substring(district.indexOf("市") + 1, district.length());
                        LatLonPoint point = null;
                        mTip = tip;

                        if (tip.getPoint() == null) {

                            String address = TextUtils.isEmpty(tip.getAddress()) ? "" : tip.getAddress();
                            searchPoint(tip.getName() + address);
                        } else {
                            searchPoint1(tip.getPoint());
                          /*  point = tip.getPoint();
                            AddressModel addressModel = new AddressModel(mProvince, mCity, newDistrict, tip.getName(), tip.getAddress(), point, mType);
                            if (mType == -2) {
                                EventBus.getDefault().post(addressModel);
                            } else {
                                startActivity(new Intent(aty, SiteActivity.class).putExtra("address", addressModel).putExtra("is_edit",is_edit));
                            }
                            finish();*/
                        }
                    }
                });
            }
        };
        mBinding.rcContent.setLayoutManager(new LinearLayoutManager(aty));
        mBinding.rcContent.setAdapter(mAdapter);
    }

    private LatLonPoint mLatLonPoint;
    private Tip mTip = null;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String newText = s.toString().trim();
            if (!AMapUtil.IsEmptyOrNullString(newText)) {
                search(newText);
            } else {
                mData.clear();
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    private void search(String text) {
        InputtipsQuery inputquery = new InputtipsQuery(text, mCity);
        Inputtips inputTips = new Inputtips(aty, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }


    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        mBinding.flyCitys.setVisibility(View.GONE);
        mBinding.rcContent.setVisibility(View.VISIBLE);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            mData.clear();
            if (tipList != null) {
                for (int i = 0; i < tipList.size(); i++) {
                    if (TextUtils.isEmpty(tipList.get(i).getAddress())) {
                        tipList.remove(i);
                    }
                }
                mData.addAll(tipList);
            }
            mAdapter.notifyDataSetChanged();

        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int rCode) {

        if (regeocodeResult.getRegeocodeAddress() != null) {
            String province = regeocodeResult.getRegeocodeAddress().getProvince();
            String city = regeocodeResult.getRegeocodeAddress().getCity();
            String district = regeocodeResult.getRegeocodeAddress().getDistrict();

            AddressModel addressModel = new AddressModel(province, city, district, mTip.getName(), mTip.getAddress(), mLatLonPoint, mType);

            if (mType == -2) {
                EventBus.getDefault().post(addressModel);
            } else {
                startActivity(new Intent(aty, SiteActivity.class)
                        .putExtra("address", addressModel)
                        .putExtra("is_edit", is_edit)
                );
            }
            finish();
        } else {
            Toast.makeText(aty, "地址信息错误!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        if (geocodeResult.getGeocodeAddressList() != null && geocodeResult.getGeocodeAddressList().size() > 0) {
            String province = geocodeResult.getGeocodeAddressList().get(0).getProvince();
            String city = geocodeResult.getGeocodeAddressList().get(0).getCity();
            String district = geocodeResult.getGeocodeAddressList().get(0).getDistrict();

            AddressModel addressModel = new AddressModel(province, city, district, mTip.getName(), mTip.getAddress(), mLatLonPoint, mType);

            if (mType == -2) {
                EventBus.getDefault().post(addressModel);
            } else {
                startActivity(new Intent(aty, SiteActivity.class)
                        .putExtra("address", addressModel)
                        .putExtra("is_edit", is_edit)
                );
            }
            finish();
        }

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
        LocationHelper.getInstance().closeLocation();
        super.onDestroy();

    }
}
