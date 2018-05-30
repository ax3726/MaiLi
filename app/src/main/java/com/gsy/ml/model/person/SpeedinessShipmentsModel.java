package com.gsy.ml.model.person;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 */

public class SpeedinessShipmentsModel  {


    /**
     * status : 200
     * info : OK
     * data : {"total":4,"rows":[{"recipientAddress":"崇明东平森林公园房车402","recipientName":"黎明 先生","recipientPhone":15170193726,"orderId":3010583158205534364,"orderTime":1502936825,"dishs":"台湾凤梨"},{"recipientAddress":"崇明东平森林公园房车402","recipientName":"黎明 先生","recipientPhone":15170193726,"orderId":3010583350766031004,"orderTime":1502936885,"dishs":"优选红富士"},{"recipientAddress":"东平国家森林公园-狩猎区111","recipientName":"李涛 先生","recipientPhone":18397845702,"orderId":3010584475376323740,"orderTime":1502937533,"dishs":"台湾凤梨"},{"recipientAddress":"东平国家森林公园-狩猎区111","recipientName":"李涛 先生","recipientPhone":18397845702,"orderId":3010584690653170844,"orderTime":1502937608,"dishs":"优选红富士"}]}
     */

    private String status;
    private String info;
    private DataBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * total : 4
         * rows : [{"recipientAddress":"崇明东平森林公园房车402","recipientName":"黎明 先生","recipientPhone":15170193726,"orderId":3010583158205534364,"orderTime":1502936825,"dishs":"台湾凤梨"},{"recipientAddress":"崇明东平森林公园房车402","recipientName":"黎明 先生","recipientPhone":15170193726,"orderId":3010583350766031004,"orderTime":1502936885,"dishs":"优选红富士"},{"recipientAddress":"东平国家森林公园-狩猎区111","recipientName":"李涛 先生","recipientPhone":18397845702,"orderId":3010584475376323740,"orderTime":1502937533,"dishs":"台湾凤梨"},{"recipientAddress":"东平国家森林公园-狩猎区111","recipientName":"李涛 先生","recipientPhone":18397845702,"orderId":3010584690653170844,"orderTime":1502937608,"dishs":"优选红富士"}]
         */

        private int total;
        private List<RowsBean> rows;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public List<RowsBean> getRows() {
            return rows;
        }

        public void setRows(List<RowsBean> rows) {
            this.rows = rows;
        }

        public static class RowsBean implements Parcelable {
            /**
             * recipientAddress : 崇明东平森林公园房车402
             * recipientName : 黎明 先生
             * recipientPhone : 15170193726
             * orderId : 3010583158205534364
             * orderTime : 1502936825
             * dishs : 台湾凤梨
             */

            private boolean PitchOn=false;
            private String recipientAddress;
            private String recipientName;
            private long recipientPhone;
            private long orderId;
            private int orderTime;
            private String dishs;
            private String province;
            private String city;
            private String district;
            private String Latitude;
            private String Longitude;

            public boolean isPitchOn() {
                return PitchOn;
            }

            public void setPitchOn(boolean pitchOn) {
                PitchOn = pitchOn;
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

            public String getLatitude() {
                return Latitude;
            }

            public void setLatitude(String latitude) {
                Latitude = latitude;
            }

            public String getLongitude() {
                return Longitude;
            }

            public void setLongitude(String longitude) {
                Longitude = longitude;
            }

            public String getRecipientAddress() {
                return recipientAddress;
            }

            public void setRecipientAddress(String recipientAddress) {
                this.recipientAddress = recipientAddress;
            }

            public String getRecipientName() {
                return recipientName;
            }

            public void setRecipientName(String recipientName) {
                this.recipientName = recipientName;
            }

            public long getRecipientPhone() {
                return recipientPhone;
            }

            public void setRecipientPhone(long recipientPhone) {
                this.recipientPhone = recipientPhone;
            }

            public long getOrderId() {
                return orderId;
            }

            public void setOrderId(long orderId) {
                this.orderId = orderId;
            }

            public int getOrderTime() {
                return orderTime;
            }

            public void setOrderTime(int orderTime) {
                this.orderTime = orderTime;
            }

            public String getDishs() {
                return dishs;
            }

            public void setDishs(String dishs) {
                this.dishs = dishs;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeByte(this.PitchOn ? (byte) 1 : (byte) 0);
                dest.writeString(this.recipientAddress);
                dest.writeString(this.recipientName);
                dest.writeLong(this.recipientPhone);
                dest.writeLong(this.orderId);
                dest.writeInt(this.orderTime);
                dest.writeString(this.dishs);
                dest.writeString(this.province);
                dest.writeString(this.city);
                dest.writeString(this.district);
                dest.writeString(this.Latitude);
                dest.writeString(this.Longitude);
            }

            public RowsBean() {
            }

            protected RowsBean(Parcel in) {
                this.PitchOn = in.readByte() != 0;
                this.recipientAddress = in.readString();
                this.recipientName = in.readString();
                this.recipientPhone = in.readLong();
                this.orderId = in.readLong();
                this.orderTime = in.readInt();
                this.dishs = in.readString();
                this.province = in.readString();
                this.city = in.readString();
                this.district = in.readString();
                this.Latitude = in.readString();
                this.Longitude = in.readString();
            }

            public static final Parcelable.Creator<RowsBean> CREATOR = new Parcelable.Creator<RowsBean>() {
                @Override
                public RowsBean createFromParcel(Parcel source) {
                    return new RowsBean(source);
                }

                @Override
                public RowsBean[] newArray(int size) {
                    return new RowsBean[size];
                }
            };
        }
    }
}
