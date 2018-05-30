package com.gsy.ml.model.common;

/**
 * Created by Administrator on 2017/7/12.
 */

public class EditAddressModel  {
    private int status=0;
    private AddressModel mAddress;

    public AddressModel getmAddress() {
        return mAddress;
    }

    public void setmAddress(AddressModel mAddress) {
        this.mAddress = mAddress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
