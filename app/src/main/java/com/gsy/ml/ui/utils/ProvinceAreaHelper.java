package com.gsy.ml.ui.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.gsy.ml.model.common.CityModel;
import com.gsy.ml.model.common.DistrictModel;
import com.gsy.ml.model.common.ProvinceModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ProvinceAreaHelper {
    private static final String TAG = "ProvinceAreaHelper";

    private Context mContext;

    /**
     * 所有省
     */
    protected String[] mProvinceDatas;

    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

    /**
     * key - 区 values - 邮编
     */
    protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

    /**
     * 当前省的名称
     */
    protected String mCurrentProviceName;

    /**
     * 当前市的名称
     */
    protected String mCurrentCityName;

    /**
     * 当前区的名称
     */
    protected String mCurrentDistrictName = "";

    /**
     * 当前区的邮政编码
     */
    protected String mCurrentZipCode = "";

    private List<CityModel> mCitys = new ArrayList<>();

    public ProvinceAreaHelper(Context context) {
        mContext = context;
    }

    /**
     * 解析省市区的XML数据
     */
    public void initProvinceData() {
        List<ProvinceModel> provinceModelList;
        AssetManager assetManager = mContext.getAssets();
        try {
            InputStream input = assetManager.open("province_data.xml");

            //创建一个解析xml 的工厂对象
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            XmlParserHelper xmlParserHelper = new XmlParserHelper();

            //开始解析xml
            parser.parse(input, xmlParserHelper);
            input.close();

            //获取解析出来的数据
            provinceModelList = xmlParserHelper.getDataList();

            //*/ 初始化默认选中的省、市、区
            if (provinceModelList != null && !provinceModelList.isEmpty()) {
                mCurrentProviceName = provinceModelList.get(0).getName();
                List<CityModel> cityList = provinceModelList.get(0).getCityList();
                if (cityList != null && !cityList.isEmpty()) {
                    mCurrentCityName = cityList.get(0).getName();
                    List<DistrictModel> districtList = cityList.get(0).getDistrictList();
                    mCurrentDistrictName = districtList.get(0).getName();
                    mCurrentZipCode = districtList.get(0).getZipcode();
                }
            }

            if (provinceModelList != null) {
                mProvinceDatas = new String[provinceModelList.size()];

                // 遍历所有省的数据
                for (int i = 0; i < provinceModelList.size(); i++) {
                    mProvinceDatas[i] = provinceModelList.get(i).getName();
                    List<CityModel> cityList = provinceModelList.get(i).getCityList();
                    String[] cityNames = new String[cityList.size()];

                    // 遍历省下面的所有市的数据
                    for (int j = 0; j < cityList.size(); j++) {
                        cityNames[j] = cityList.get(j).getName();
                        List<DistrictModel> districtList = cityList.get(j).getDistrictList();
                        String[] distrinctNameArray = new String[districtList.size()];

                        // 遍历市下面所有区/县的数据
                        for (int k = 0; k < districtList.size(); k++) {
                            DistrictModel districtModel = new DistrictModel(districtList.get(k).getName(), districtList.get(k).getZipcode());

                            // 区/县对于的邮编，保存到mZipcodeDatasMap
                            mZipcodeDatasMap.put(districtList.get(k).getName(), districtList.get(k).getZipcode());
                            distrinctNameArray[k] = districtModel.getName();
                        }

                        // 市-区/县的数据，保存到mDistrictDatasMap
                        mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
                    }
                    // 省-市的数据，保存到mCitisDatasMap
                    mCitisDatasMap.put(provinceModelList.get(i).getName(), cityNames);
                    for (String name : cityNames) {
                        CityModel cityModel = new CityModel();
                        cityModel.setName(name);
                        cityModel.setProvince(provinceModelList.get(i).getName());
                        mCitys.add(cityModel);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "解析省市区的XML数据 Exception=" + e.getMessage());
            e.printStackTrace();
        }
    }

    public String[] updateCities(String provinceName) {
        mCurrentProviceName = provinceName;
//        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
//        if (cities == null) {
//            cities = new String[] { "" };
//        }

        return mCitisDatasMap.get(provinceName);
    }

    public List<CityModel> getCitys() {
        return mCitys;
    }

    public String[] updateAreas(String cityName) {
        mCurrentCityName = cityName;
//        String[] areas = mDistrictDatasMap.get(mCurrentCityName);
//
//        if (areas == null) {
//            areas = new String[] { "" };
//        }

        return mDistrictDatasMap.get(cityName);
    }

    public String getmCurrentProviceName() {
        return mCurrentProviceName;
    }

    public String getmCurrentCityName() {
        return mCurrentCityName;
    }

    public String getmCurrentDistrictName() {
        return mCurrentDistrictName;
    }

    public String getmCurrentZipCode() {
        return mCurrentZipCode;
    }

    public String[] getmProvinceDatas() {
        return mProvinceDatas;
    }


}
