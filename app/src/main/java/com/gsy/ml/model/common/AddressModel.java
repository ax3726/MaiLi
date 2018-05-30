package com.gsy.ml.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.amap.api.services.core.LatLonPoint;

/**
 * Created by Administrator on 2017/5/11.
 */

public class AddressModel implements Parcelable {
    private String id;
    private String province;   //省
    private String city;        //市
    private String district;    //区
    private String name;        //地址名
    private String address;
    private String door_info;
    private LatLonPoint point;
    private String add_name;
    private String add_phone;


    private int type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdd_name() {
        return add_name;
    }

    public String getDoor_info() {
        return door_info;
    }

    public void setDoor_info(String door_info) {
        this.door_info = TextUtils.isEmpty(door_info) ? "\u3000" : door_info;
    }

    public void setAdd_name(String add_name) {
        this.add_name = add_name;
    }

    public String getAdd_phone() {
        return add_phone;
    }

    public void setAdd_phone(String add_phone) {
        this.add_phone = add_phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LatLonPoint getPoint() {
        return point;
    }

    public void setPoint(LatLonPoint point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressModel() {
    }

    public AddressModel(String province, String city, String district, String name, String address, LatLonPoint point, int type) {
        this.province = province;
        this.city = city;
        this.district = district;
        this.name = name;
        this.address = address;
        this.point = point;
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeString(this.door_info);
        dest.writeParcelable(this.point, flags);
        dest.writeString(this.add_name);
        dest.writeString(this.add_phone);
        dest.writeInt(this.type);
    }

    protected AddressModel(Parcel in) {
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.name = in.readString();
        this.address = in.readString();
        this.door_info = in.readString();
        this.point = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.add_name = in.readString();
        this.add_phone = in.readString();
        this.type = in.readInt();
    }

    public static final Parcelable.Creator<AddressModel> CREATOR = new Parcelable.Creator<AddressModel>() {
        @Override
        public AddressModel createFromParcel(Parcel source) {
            return new AddressModel(source);
        }

        @Override
        public AddressModel[] newArray(int size) {
            return new AddressModel[size];
        }
    };
}
