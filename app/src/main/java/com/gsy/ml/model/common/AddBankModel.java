package com.gsy.ml.model.common;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/6/1.
 */

public class AddBankModel implements Parcelable {

    public String name;   //姓名
    public String phone;  //电话号码
    public String bank;   //银行卡
    public String num;    //银行卡号码
    public String bankType;    // 提现类型
    public int type;    // id
    public int id;    // id

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AddBankModel(String name, String phone, String bank, String num, String bankType,int type,int id) {
        this.name = name;
        this.phone = phone;
        this.bank = bank;
        this.num = num;
        this.bankType = bankType;
        this.type = type;
        this.id = id;

    }
    public AddBankModel()
    {

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.phone);
        dest.writeString(this.bank);
        dest.writeString(this.num);
        dest.writeString(this.bankType);
        dest.writeInt(this.type);
        dest.writeInt(this.id);
    }


    protected AddBankModel(Parcel in) {
        this.name = in.readString();
        this.phone = in.readString();
        this.bank = in.readString();
        this.num = in.readString();
        this.bankType = in.readString();
        this.type = in.readInt();
        this.id = in.readInt();
    }

    public static final Creator<AddBankModel> CREATOR = new Creator<AddBankModel>() {
        @Override
        public AddBankModel createFromParcel(Parcel in) {
            return new AddBankModel(in);
        }

        @Override
        public AddBankModel[] newArray(int size) {
            return new AddBankModel[size];
        }
    };

}
